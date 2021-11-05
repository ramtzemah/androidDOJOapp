package com.example.raceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private int DELAY = 750;
    private TextView panel_textView;
    private int score =0;
    private ImageView[][] imgArray;
    private int[][] vals;
    private int[] rocketRoute;
    private ImageView panel_IMG_speed;
    private ImageView panel_IMG_heart1;
    private ImageView panel_IMG_heart2;
    private ImageView panel_IMG_heart3;
    private ImageView panel_IMG_lView;
    private ImageView panel_IMG_rView;
    private ImageView panel_IMG_lRocket;
    private ImageView panel_IMG_mRocket;
    private ImageView panel_IMG_rRocket;
    private ImageView panel_IMG_rArrow;
    private ImageView panel_IMG_lArrow;
    private int counter =0;
    private int rocksSpace =0;
    TimerTask ts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        setRocksInvisible(imgArray);

        //updateUI();

        //setDelay(750);

        panel_IMG_lArrow.setOnClickListener(v -> next(true));
        panel_IMG_lView.setOnClickListener(v -> next(true));
        panel_IMG_rArrow.setOnClickListener(v -> next(false));
        panel_IMG_rView.setOnClickListener(v -> next(false));

        panel_IMG_speed.setOnClickListener(v -> {

            counter++;
            ImageView im = panel_IMG_speed;
            if(counter == 1 ) {
                ts.cancel();
                setDelay(250);
                im.setImageResource(R.drawable.turtle);
            }else{
                ts.cancel();
                setDelay(750);
                im.setImageResource(R.drawable.bunny);
                panel_IMG_speed =im;
                counter = 0;
            }
        });


    }

    private void updateUI() {
        startTicker();
    }

    public void setDelay(int DELAY) {
        this.DELAY = DELAY;
        startTicker();
    }

    private void startTicker() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(ts = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                         checkHit();
                         rocksMaker();
                });
            }
        }, 0, DELAY);
    }

    private void rocksMaker(){
        Random rand = new Random();                     //random number 0-2
        int randomNum = rand.nextInt((2) + 1);
        int blockKind = 1 + rand.nextInt(5);
        for (int i = vals.length-1; i >= 1; i--) {
            for (int j = 0; j < vals[i].length; j++) {
                if (vals[i-1][j] >= 1){
                    vals[i][j]=vals[i-1][j];
                    vals[i-1][j]=0;
//                    vals[i-1][j]=0;
//                    vals[i][j]=1;
                    break;
                }
            }
        }

        if(rocksSpace == 0){
            vals[0][randomNum]=blockKind; //1
            rocksSpace++;
        }else{
            vals[0][randomNum]=0;
            rocksSpace--;
        }

        for (int i = 0; i < vals.length; i++) {
            for (int j = 0; j < vals[i].length; j++) {

                if (vals[i][j] == 1){
                    ImageView im2 = imgArray[i][j];
                    im2.setImageResource(R.drawable.rock);
                    imgArray[i][j].setVisibility(View.VISIBLE);
                }
                else if (vals[i][j] == 2) {
                    ImageView im2 = imgArray[i][j];
                    imgArray[i][j].setVisibility(View.VISIBLE);
                    im2.setImageResource(R.drawable.roadblock);
                }else if (vals[i][j] == 3) {
                    ImageView im2 = imgArray[i][j];
                    imgArray[i][j].setVisibility(View.VISIBLE);
                    im2.setImageResource(R.drawable.black_hole);
                }else if (vals[i][j] == 4) {
                    ImageView im2 = imgArray[i][j];
                    imgArray[i][j].setVisibility(View.VISIBLE);
                    im2.setImageResource(R.drawable.coin);
                }else if (vals[i][j] == 5) {
                    ImageView im2 = imgArray[i][j];
                    imgArray[i][j].setVisibility(View.VISIBLE);
                    im2.setImageResource(R.drawable.coins);
                }
                else{
                    imgArray[i][j].setVisibility(View.INVISIBLE);
                }
                //imgArray[i][j] = im2;
            }
        }
    }

    private void checkHit() {
        for (int i = 0 ; i < imgArray[4].length; i++) {
            if(vals[4][i] >= 3 && rocketRoute[i] == 1){
                addPoint(i);
                vals[4][i] = 0;
            }else if (vals[4][i] >= 1 && rocketRoute[i] == 1) {
                vals[4][i] = 0;
                heartCount();
            }else if(vals[4][i] >= 1 && rocketRoute[i] == 0){
                vals[4][i] = 0;
            }
        }
    }

    private void addPoint(int i) {
        if (vals[4][i] == 4) {
            score +=100;
           // panel_textView.append("score "+score);
        }else
            score+=200;
        panel_textView.setText("score "+score);
    }

    private void heartCount() {
        vibrate();
        if (panel_IMG_heart1.getVisibility() == View.VISIBLE){
            panel_IMG_heart1.setVisibility(View.INVISIBLE);
        }else if (panel_IMG_heart2.getVisibility() == View.VISIBLE){
            panel_IMG_heart2.setVisibility(View.INVISIBLE);
        }else{
            panel_IMG_heart3.setVisibility(View.INVISIBLE);
            finish();
        }
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(500);
        }
    }


    private void setRocksInvisible(ImageView[][] imgArray) {
        panel_IMG_lRocket.setVisibility(View.INVISIBLE);
        panel_IMG_rRocket.setVisibility(View.INVISIBLE);
        for (ImageView[] imageViews : imgArray) {
            for (ImageView imageView : imageViews) {
                imageView.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void next(boolean side) {
            if(!side) {
                if(panel_IMG_lRocket.getVisibility()==View.VISIBLE){
                    panel_IMG_lRocket.setVisibility(View.INVISIBLE);
                    panel_IMG_mRocket.setVisibility(View.VISIBLE);
                    panel_IMG_lArrow.setVisibility(View.VISIBLE);
                    rocketRoute[0]=0;
                    rocketRoute[1]=1;
                }else if(panel_IMG_mRocket.getVisibility()==View.VISIBLE){
                    panel_IMG_mRocket.setVisibility(View.INVISIBLE);
                    panel_IMG_rRocket.setVisibility(View.VISIBLE);
                    panel_IMG_rArrow.setVisibility(View.INVISIBLE);
                    rocketRoute[1]=0;
                    rocketRoute[2]=1;
                }
            }else {
                if(panel_IMG_rRocket.getVisibility()==View.VISIBLE){
                    panel_IMG_rRocket.setVisibility(View.INVISIBLE);
                    panel_IMG_mRocket.setVisibility(View.VISIBLE);
                    panel_IMG_rArrow.setVisibility(View.VISIBLE);
                    rocketRoute[2]=0;
                    rocketRoute[1]=1;
                }else if(panel_IMG_mRocket.getVisibility()==View.VISIBLE){
                    panel_IMG_mRocket.setVisibility(View.INVISIBLE);
                    panel_IMG_lRocket.setVisibility(View.VISIBLE);
                    panel_IMG_lArrow.setVisibility(View.INVISIBLE);
                    rocketRoute[1]=0;
                    rocketRoute[0]=1;
                }
            }

    }

    @SuppressLint("WrongViewCast")
    private void findViews() {
        imgArray = new ImageView[][]{
                {findViewById(R.id.panel_IMG_rockPlace1),findViewById(R.id.panel_IMG_rockPlace2),findViewById(R.id.panel_IMG_rockPlace3)},
                {findViewById(R.id.panel_IMG_rockPlace4),findViewById(R.id.panel_IMG_rockPlace5),findViewById(R.id.panel_IMG_rockPlace6)},
                {findViewById(R.id.panel_IMG_rockPlace7),findViewById(R.id.panel_IMG_rockPlace8),findViewById(R.id.panel_IMG_rockPlace9)},
                {findViewById(R.id.panel_IMG_rockPlace10),findViewById(R.id.panel_IMG_rockPlace11),findViewById(R.id.panel_IMG_rockPlace12)},
                {findViewById(R.id.panel_IMG_rockPlace13),findViewById(R.id.panel_IMG_rockPlace14),findViewById(R.id.panel_IMG_rockPlace15)}
        };
        int SIZEOFROUTE = 3;
        vals = new int[5][SIZEOFROUTE];
        rocketRoute = new int[SIZEOFROUTE];
        rocketRoute[1]=1;
        panel_IMG_lView=findViewById(R.id.panel_IMG_lView);
        panel_IMG_rView=findViewById(R.id.panel_IMG_rView);
        panel_textView = findViewById(R.id.panel_textView);
        panel_textView.setText("score "+score);
        panel_IMG_speed = findViewById(R.id.panel_IMG_speed);
        panel_IMG_heart1 = findViewById(R.id.panel_IMG_heart1);
        panel_IMG_heart2 = findViewById(R.id.panel_IMG_heart2);
        panel_IMG_heart3 = findViewById(R.id.panel_IMG_heart3);
        panel_IMG_lRocket = findViewById(R.id.panel_IMG_lRocket);
        panel_IMG_mRocket = findViewById(R.id.panel_IMG_mRocket);
        panel_IMG_rRocket = findViewById(R.id.panel_IMG_rRocket);
        panel_IMG_rArrow = findViewById(R.id.panel_IMG_rArrow);
        panel_IMG_lArrow = findViewById(R.id.panel_IMG_lArrow);

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopTicker();
    }

    private void stopTicker() {
        ts.cancel();
    }

}