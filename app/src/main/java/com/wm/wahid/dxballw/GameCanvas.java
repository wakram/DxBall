package com.wm.wahid.dxballw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Wahid on 5/6/2016.
 */
class GameCanvas extends View{
    int _count = 0;
    int _level;
    public static int _life = 2;
    public static boolean _gameOver;
    float _brickX =0, _brickY =0;
    static int _score =0;
    Canvas _canvas;
    boolean _createGame;
    Paint _paint;
    Bar _bar;
    Ball _ball;
    float _touchPoint;
    boolean _gameStart = true;
    ArrayList<Bricks> _bricks =new ArrayList<Bricks>();
    Stage _stage = new Stage();

    public GameCanvas(Context context) {
        super(context);
        _paint =new Paint();
        _level = 1;
        _createGame = true;
        _gameOver = false;
        _bar = new Bar();
        _ball = new Ball();

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int _action = MotionEventCompat.getActionMasked(event);
                if (_ball.isballAvailable()) {
                    _touchPoint = event.getX();
                    if (_touchPoint < v.getWidth() / 2 && _bar.getBarLeft()-20 > 0) {
                        _bar.setBarLeft(_bar.getBarLeft() - 10);
                        if(_count <1) {
                            _ball.setX(_ball.getX() - 10);
                        }
                        _touchPoint = -10;
                    } else if (_touchPoint >= v.getWidth() / 2 && _bar.getBarRight()+20 < v.getWidth()) {
                        _bar.setBarLeft(_bar.getBarLeft() + 10);
                        if(_count <1) {
                            _ball.setX(_ball.getX() + 10);
                        }
                        _touchPoint = -10;
                    }
                }

                if(_action == MotionEvent.ACTION_UP){
                    if(_count <2){
                        _count +=1;
                    }
                }
                return true;
            }
        });
    }


    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (_gameStart) {
            _gameStart = false;
            _bar.setBar(canvas);
            _ball.setBall(canvas, _bar);
        }
        canvas.drawRGB(255, 255, 255);
        _paint.setColor(Color.BLACK);
        _paint.setStyle(Paint.Style.FILL);
        _ball.drawBall(canvas, _paint);
        _bar.drawBar(canvas, _paint);

        if(_count == 2){
            _ball.nextPos(canvas, _bar, _paint);
        }
        if(_count <= 1){
            _paint.setColor(Color.BLACK);
            _paint.setTextSize(50);
            _paint.setFakeBoldText(true);
            canvas.drawText("Move Bar And Tap to Start",canvas.getWidth()/2-canvas.getWidth()/2+80,canvas.getHeight()/2+canvas.getHeight()/8, _paint);
        }

        this._canvas = canvas;

        _brickX = canvas.getWidth() / 7;
        _brickY = (canvas.getHeight() / 15) ;

        if (_createGame) {
            _createGame = false;
            if (_level == 1) {
                _stage.stage_level_one(canvas, _brickX, _brickY, _bricks);
            }
            else if (_level == 2) {
                _stage.stage_level_two(canvas, _brickX, _brickY, _bricks);
            }
            else
                _gameOver = true;
        }

        for(int i = 0; i< _bricks.size(); i++){
            canvas.drawRect(_bricks.get(i).getLeft(), _bricks.get(i).getTop(), _bricks.get(i).getRight(), _bricks.get(i).getBottom(), _bricks.get(i).getPaint());
        }

        _ball.ballBrickCollision(_bricks, _ball);

        if(_bricks.size() == 0){
            _count = -1;
            _level += 1;
            _createGame = true;
            _gameStart = true;
        }

        if( _count == -1 ){

                _paint.setColor(Color.BLACK);
                canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), _paint);
                _paint.setColor(Color.WHITE);
                _paint.setTextSize(50);
                _paint.setFakeBoldText(true);
                canvas.drawText("Level 2",canvas.getWidth()/2-canvas.getWidth()/9,canvas.getHeight()/2, _paint);
                canvas.drawText("Your Score: "+ _score,canvas.getWidth()/2-canvas.getWidth()/5,canvas.getHeight()/2+134, _paint);
                canvas.drawText("Tap To Next Level",canvas.getWidth()/2-canvas.getWidth()/3+50,canvas.getHeight()/2+300, _paint);
        }

        if(_ball.isballAvailable() == false){
            _ball.setBallAvailable(true);
            _count = 0;
            _gameStart = true;
            _life -= 1;
        }

        _paint.setTextSize(30);
        _paint.setFakeBoldText(true);
        canvas.drawText("Life: " + _life, 20, 40, _paint);
        canvas.drawText("Score: " + _score, canvas.getWidth() - 150 , 40, _paint);
        canvas.drawText("LEVEL " + _level, canvas.getWidth() / 2 - 60, 40, _paint);

        if(_life == 0 || _gameOver){
            _paint.setColor(Color.BLACK);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), _paint);
            _paint.setColor(Color.WHITE);
            _paint.setTextSize(80);
            _paint.setFakeBoldText(true);
            canvas.drawText("Game Over",canvas.getWidth()/2-canvas.getWidth()/4,canvas.getHeight()/2, _paint);
            canvas.drawText("Your Score: "+ _score,canvas.getWidth()/2-canvas.getWidth()/3,canvas.getHeight()/2+134, _paint);
            _gameOver = false;
            _level = 0;
        }

        invalidate();
    }


}