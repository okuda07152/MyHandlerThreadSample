package com.example.okuda0715.myhandlerthreadsample;

import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private HandlerThread handlerThread;
    private Handler handler;
    private Runnable runnable;
    int count;
    private Button quitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quitButton = findViewById(R.id.quit_button);

        // 終了ボタンタップ時の処理
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ルーパーに溜まっている処理が全て完了してからルーパーを停止する。
                handlerThread.quitSafely();
                // ルーパーに処理が残っていても即座に終了する。
                // handlerThread.quit();

                // 参照を破棄すればガベージコレクションでメモリを解放してくれるはず。
                handlerThread = null;
                Log.v("MyHandlerThreadSample","スレッドを終了しました。");
            }
        });

        runnable = new Runnable(){
            // ワーカースレッドで実行したい処理
            @Override
            public void run() {
                count++;
                Log.v("MyHandlerThreadSample","count = " + String.valueOf(count));

                // 再帰処理
                handler.postDelayed(this, 1000);
            }
        };

        // ワーカースレッドの生成と開始（nameパラメータはワーカースレッドのスレッド名）
        handlerThread = new HandlerThread("my_worker_thread");
        handlerThread.start();

        // ワーカースレッドのルーパーに紐づくHandlerを生成
        handler = new Handler(handlerThread.getLooper());
        // ワーカースレッドのルーパーに実行して欲しい処理を投げる。
        handler.post(runnable);
    }

}
