package jigl.image.ops;

import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.ColorImage;
import jigl.image.RealColorImage;

import static org.junit.Assert.*;
import org.junit.Test;

public class ShiftTest {
    
    @Test
    public void testApply_GrayImage_wrap() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        
        GrayImage inputImage = new GrayImage(4, 4);
        for (int i = 0; i < inputImage.X(); i++) {
            for (int j = 0; j < inputImage.Y(); j++) {
                inputImage.set(i, j, i + j);
            }
        }
        
        GrayImage expectedOutput = new GrayImage(4, 4);
        expectedOutput.set(0, 0, 2);
        expectedOutput.set(1, 0, 3);
        expectedOutput.set(2, 0, 0);
        expectedOutput.set(3, 0, 1);
        expectedOutput.set(0, 1, 3);
        expectedOutput.set(1, 1, 4);
        expectedOutput.set(2, 1, 1);
        expectedOutput.set(3, 1, 2);
        expectedOutput.set(0, 2, 0);
        expectedOutput.set(1, 2, 1);
        expectedOutput.set(2, 2, 2);
        expectedOutput.set(3, 2, 3);
        expectedOutput.set(0, 3, 1);
        expectedOutput.set(1, 3, 2);
        expectedOutput.set(2, 3, 3);
        expectedOutput.set(3, 3, 4);
        
        GrayImage actualOutput = shift.apply(inputImage);
        
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testApply_GrayImage_nowrap() {
        Shift shift = new Shift(-1, 2, Shift.NOWRAP);
        
        GrayImage inputImage = new GrayImage(3, 3);
        for (int i = 0; i < inputImage.X(); i++) {
            for (int j = 0; j < inputImage.Y(); j++) {
                inputImage.set(i, j, i + j);
            }
        }
        
        GrayImage expectedOutput = new GrayImage(3, 3);
        expectedOutput.set(0, 0, 1);
        expectedOutput.set(1, 0, 0);
        expectedOutput.set(2, 0, 0);
        expectedOutput.set(0, 1, 3);
        expectedOutput.set(1, 1, 2);
        expectedOutput.set(2, 1, 0);
        expectedOutput.set(0, 2, 4);
        expectedOutput.set(1, 2, 3);
        expectedOutput.set(2, 2, 1);
        
        GrayImage actualOutput = shift.apply(inputImage);
        
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testApply_RealGrayImage_wrap() {
        Shift shift = new Shift(1, -2, Shift.WRAP);
        
        RealGrayImage inputImage = new RealGrayImage(3, 3);
        for (int i = 0; i < inputImage.X(); i++) {
            for (int j = 0; j < inputImage.Y(); j++) {
                inputImage.set(i, j, (float) (i + j) / 2);
            }
        }
        
        RealGrayImage expectedOutput = new RealGrayImage(3, 3);
        expectedOutput.set(0, 0, 0.5f);
        expectedOutput.set(1, 0, 0.5f);
        expectedOutput.set(2, 0, 0.0f);
        expectedOutput.set(0, 1, 1.0f);
        expectedOutput.set(1, 1, 1.0f);
        expectedOutput.set(2, 1, 0.5f);
        expectedOutput.set(0, 2, 1.5f);
        expectedOutput.set(1, 2, 1.5f);
        expectedOutput.set(2, 2, 1.0f);
        
        RealGrayImage actualOutput = shift.apply(inputImage);
        
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testApply_RealGrayImage_nowrap() {
        Shift shift = new Shift(-2, -1, Shift.NOWRAP);
        
        RealGrayImage inputImage = new RealGrayImage(4, 4);
        for (int i = 0; i < inputImage.X(); i++) {
            for (int j = 0; j < inputImage.Y(); j++) {
                inputImage.set(i, j, (float) (i + j) / 2);
            }
        }
        
        RealGrayImage expectedOutput = new RealGrayImage(4, 4);
        expectedOutput.set(0, 0, 0.5f);
        expectedOutput.set(1, 0, 0.5f);
        expectedOutput.set(2, 0, 0.5f);
        expectedOutput.set(3, 0, 0.0f);
        expectedOutput.set(0, 1, 1.0f);
        expectedOutput.set(1, 1, 1.0f);
        expectedOutput.set(2, 1, 1.0f);
        expectedOutput.set(3, 1, 0.5f);
        expectedOutput.set(0, 2, 1.5f);
        expectedOutput.set(1, 2, 1.5f);
        expectedOutput.set(2, 2, 1.5f);
        expectedOutput.set(3, 2, 1.0f);
        expectedOutput.set(0, 3, 1.0f);
        expectedOutput.set(1, 3, 1.0f);
        expectedOutput.set(2, 3, 1.0f);
        expectedOutput.set(3, 3, 0.5f);
        
        RealGrayImage actualOutput = shift.apply(inputImage);
        
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testApply_ColorImage_wrap() {
        Shift shift = new Shift(-2, 2, Shift.WRAP);
        
        ColorImage inputImage = new ColorImage(3, 3);
        for (int i = 0; i < inputImage.X(); i++) {
            for (int j = 0; j < inputImage.Y(); j++) {
                inputImage.set(i, j, new int[] {i + j, i + j, i + j});
            }
        }
        
        ColorImage expectedOutput = new ColorImage(3, 3);
        expectedOutput.set(0, 0, new int[] {0, 0, 0});
        expectedOutput.set(1, 0, new int[] {1, 1, 1});
        expectedOutput.set(2, 0, new int[] {2, 2, 2});
        expectedOutput.set(0, 1, new int[] {1, 1, 1});
        expectedOutput.set(1, 1, new int[] {2, 2, 2});
        expectedOutput.set(2, 1, new int[] {3, 3, 3});
        expectedOutput.set(0, 2, new int[] {2, 2, 2});
        expectedOutput.set(1, 2, new int[] {3, 3, 3});
        expectedOutput.set(2, 2, new int[] {4, 4, 4});
        
        ColorImage actualOutput = shift.apply(inputImage);
        
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testApply_RealColorImage_nowrap() {
        Shift shift = new Shift(1, -1, Shift.NOWRAP);
        
        RealColorImage inputImage = new RealColorImage(4, 4);
        for (int i = 0; i < inputImage.X(); i++) {
            for (int j = 0; j < inputImage.Y(); j++) {
                inputImage.set(i, j, new float[] {(float) (i + j) / 3, (float) (i + j) / 3, (float) (i + j) / 3});
            }
        }
        
        RealColorImage expectedOutput = new RealColorImage(4, 4);
        expectedOutput.set(0, 0, new float[] {0.0f, 0.0f, 0.0f});
        expectedOutput.set(1, 0, new float[] {0.3333f, 0.3333f, 0.3333f});
        expectedOutput.set(2, 0, new float[] {0.6666f, 0.6666f, 0.6666f});
        expectedOutput.set(3, 0, new float[] {1.0f, 1.0f, 1.0f});
        expectedOutput.set(0, 1, new float[] {0.6666f, 0.6666f, 0.6666f});
        expectedOutput.set(1, 1, new float[] {1.0f, 1.0f, 1.0f});
        expectedOutput.set(2, 1, new float[] {1.3333f, 1.3333f, 1.3333f});
        expectedOutput.set(3, 1, new float[] {1.6666f, 1.6666f, 1.6666f});
        expectedOutput.set(0, 2, new float[] {1.3333f, 1.3333f, 1.3333f});
        expectedOutput.set(1, 2, new float[] {1.6666f, 1.6666f, 1.6666f});
        expectedOutput.set(2, 2, new float[] {2.0f, 2.0f, 2.0f});
        expectedOutput.set(3, 2, new float[] {2.3333f, 2.3333f, 2.3333f});
        expectedOutput.set(0, 3, new float[] {2.0f, 2.0f, 2.0f});
        expectedOutput.set(1, 3, new float[] {2.3333f, 2.3333f, 2.3333f});
        expectedOutput.set(2, 3, new float[] {2.6666f, 2.6666f, 2.6666f});
        expectedOutput.set(3, 3, new float[] {3.0f, 3.0f, 3.0f});
        
        RealColorImage actualOutput = shift.apply(inputImage);
        
        assertEquals(expectedOutput, actualOutput);
    }
    
}