package com.example.doroiapp;

import javax.security.auth.PrivateCredentialPermission;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnTouchListener;
import android.view.SurfaceView;

public class CharaSurfaceView extends SurfaceView {
	//member values
		private SurfaceHolder mHolder;
		private Bitmap mBitmapDoroi;
		private int mBitmapX;
		private int mBitmapY;
		private boolean mAttached;
		private Thread mThread;
		private static int ADD_XNUMBER = 10;
		private static int ADD_YNUMBER = 10;
		private GestureDetector mGestureDetector;
		
		private OnGestureListener onGestureListener = new OnGestureListener() {
			
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO 自動生成されたメソッド・スタブ
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent e) {
				// TODO 自動生成されたメソッド・スタブ
				
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
					float distanceY) {
				// TODO 自動生成されたメソッド・スタブ
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent e) {
				// TODO 自動生成されたメソッド・スタブ
				
			}
			
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				ADD_XNUMBER = (int) (e2.getX() - e1.getX())/20;
				ADD_YNUMBER = (int) (e2.getY() - e1.getY())/20;
				return true;
			}
			
			@Override
			public boolean onDown(MotionEvent e) {
				// TODO 自動生成されたメソッド・スタブ
				return false;
			}
		};
		
		private OnTouchListener onTouchListener = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return mGestureDetector.onTouchEvent(event);
			}
		};

		private Thread mRunnable = new Thread(new Runnable() {		
			@Override
			public void run() {
				while(mAttached){
					 try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
					doDraw();
					mBitmapX += ADD_XNUMBER;
					mBitmapY += ADD_YNUMBER;
					
					int tempH = getHeight();
					int getWidth = getWidth();
					
					if((getWidth()-50)<mBitmapX||mBitmapX<0){
						ADD_XNUMBER = -ADD_XNUMBER;
					}
					if((getHeight()-50)<mBitmapY||mBitmapY<0){
						ADD_YNUMBER = -ADD_YNUMBER;
					}
				}
				
			}
		});
		//Callback
		private SurfaceHolder.Callback callback = new Callback() {

			//when setContentView(new SurfaceGameView(this)) in MainActivity.class
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				//resource
				Resources resources = getResources();
				mBitmapDoroi = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher);
				//onDrawでは高速に処理をさせるためにあらかじめ必要なリソース・計算はここでしておく。
				mBitmapX = 10;
				mBitmapY = 10;
				mAttached = true;
				mThread = new Thread(mRunnable);
				mThread.start();
			}
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				// TODO 自動生成されたメソッド・スタブ

			}
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				
				mAttached = false;
				while(mThread.isAlive());
				
				if(mBitmapDoroi!=null){
					mBitmapDoroi.recycle();
					mBitmapDoroi = null;
				}
			}
		};

		/**
		 * construct
		 * @param context
		 */
		public CharaSurfaceView(Context context) {
			super(context);
			ini(context);
		}

		public CharaSurfaceView(Context context, AttributeSet attrs) {
			super(context, attrs);
			ini(context);
		}

		public CharaSurfaceView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			ini(context);
		}



		private void doDraw(){
			
			Canvas canvas = mHolder.lockCanvas();		

			if(canvas!=null){
				canvas.drawColor(Color.BLACK);
				canvas.drawBitmap(mBitmapDoroi, mBitmapX, mBitmapY, null);
				mHolder.unlockCanvasAndPost(canvas);
			}
		}

		/*
		 * private
		 */
		private void ini(Context context){
			mHolder = getHolder();
			mHolder.addCallback(callback);
			mAttached = false;
			setOnTouchListener(onTouchListener);
			mGestureDetector = new GestureDetector(context, onGestureListener);
			setClickable(mAttached);
		}
	}
