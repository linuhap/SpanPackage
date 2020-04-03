package com.agen.spanpackage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ImageSpan;

/**
 * 文字和背景图垂直居中显示
 */
public class CenterTextImageSpan extends ImageSpan {

    /**
     * 文字内容
     */
    private String mText;

    /**
     * 文字paint
     */
    private TextPaint mTextPaint;

    private Context mContext;

    public CenterTextImageSpan(Context context, final int drawableRes) {
        super(context, drawableRes);
    }

    public CenterTextImageSpan(Context context, final int drawableRes, String text, TextPaint textPaint) {
        super(context, drawableRes);
        this.mText = text;
        this.mTextPaint = textPaint;

        mContext = context;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        // 图片宽度
        Drawable b = getDrawable();
        int picHeight = paint.getFontMetricsInt().bottom - paint.getFontMetricsInt().top;
        return (int) (b.getMinimumWidth() * picHeight * 1F / b.getMinimumHeight());
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {

        canvas.save();
        // 绘制背景图片
        // image to draw
        Drawable b = getDrawable();
        // font metrics of text to be replaced
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        // 设置图片大小
        // 图片高度为TextView高度
        int picHeight = fm.bottom - fm.top;
        // 图片宽度按比例缩放
        int picWidth = (int) (b.getMinimumWidth() * picHeight * 1F / b.getMinimumHeight());
        b.setBounds(0, 0, picWidth, picHeight);
        b.draw(canvas);

        // 绘制文字
        if (!TextUtils.isEmpty(mText)) {
            Paint p = getCustomTextPaint(paint);
            Paint.FontMetricsInt fontFm = p.getFontMetricsInt();
            // 要绘制的文字高度
            int newTextHeight = fontFm.bottom - fontFm.top;
            // TextView中文字高度
            int oldTextHeight = bottom - top;
            // 文字baseline y轴移动的距离
            int transY = (oldTextHeight - newTextHeight) / 2;
            // 文字x轴移动距离
            int transX = (int) ((picWidth - p.measureText(mText)) / 2);
            // 此处重新计算坐标，使字体居中
            canvas.drawText(mText, x + transX, y - transY, p);
        }

        canvas.restore();
    }

    private TextPaint getCustomTextPaint(Paint srcPaint) {
        if (mTextPaint != null) {
            return mTextPaint;
        }
        return new TextPaint(srcPaint);
    }
}
