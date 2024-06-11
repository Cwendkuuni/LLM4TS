package jigl.image.ops;

import jigl.image.exceptions.*;
import jigl.image.ops.Shift;
import jigl.image.*;
import jigl.image.types.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class ShiftTest {
    
    @Test
    public void testShift_grayImage_withWrap() {
        GrayImage image = new GrayImage(3, 3);
        for (int i = 0; i < image.X(); i++) {
            for (int j = 0; j < image.Y(); j++) {
                image.set(i, j, i + j);
            }
        }
        
        Shift shift = new Shift(1, 2, Shift.WRAP);
        GrayImage result = shift.apply(image);
        
        assertEquals(0, result.get(0, 0));
        assertEquals(0, result.get(0, 1));
        assertEquals(2, result.get(0, 2));
        assertEquals(0, result.get(1, 0));
        assertEquals(1, result.get(1, 1));
        assertEquals(0, result.get(1, 2));
        assertEquals(0, result.get(2, 0));
        assertEquals(0, result.get(2, 1));
        assertEquals(1, result.get(2, 2));
    }
    
    @Test
    public void testShift_grayImage_withoutWrap() {
        GrayImage image = new GrayImage(3, 3);
        for (int i = 0; i < image.X(); i++) {
            for (int j = 0; j < image.Y(); j++) {
                image.set(i, j, i + j);
            }
        }
        
        Shift shift = new Shift(1, 2, Shift.NOWRAP);
        GrayImage result = shift.apply(image);
        
        assertEquals(3, result.get(0, 0));
        assertEquals(3, result.get(0, 1));
        assertEquals(3, result.get(0, 2));
        assertEquals(3, result.get(1, 0));
        assertEquals(4, result.get(1, 1));
        assertEquals(3, result.get(1, 2));
        assertEquals(3, result.get(2, 0));
        assertEquals(3, result.get(2, 1));
        assertEquals(3, result.get(2, 2));
    }
    
    @Test
    public void testShift_realGrayImage_withWrap() {
        RealGrayImage image = new RealGrayImage(3, 3);
        for (int i = 0; i < image.X(); i++) {
            for (int j = 0; j < image.Y(); j++) {
                image.set(i, j, (float)(i + j));
            }
        }
        
        Shift shift = new Shift(1, 2, Shift.WRAP);
        RealGrayImage result = shift.apply(image);
        
        assertEquals(0, result.get(0, 0), 0.001);
        assertEquals(0, result.get(0, 1), 0.001);
        assertEquals(2, result.get(0, 2), 0.001);
        assertEquals(0, result.get(1, 0), 0.001);
        assertEquals(1, result.get(1, 1), 0.001);
        assertEquals(0, result.get(1, 2), 0.001);
        assertEquals(0, result.get(2, 0), 0.001);
        assertEquals(0, result.get(2, 1), 0.001);
        assertEquals(1, result.get(2, 2), 0.001);
    }
    
    @Test
    public void testShift_realGrayImage_withoutWrap() {
        RealGrayImage image = new RealGrayImage(3, 3);
        for (int i = 0; i < image.X(); i++) {
            for (int j = 0; j < image.Y(); j++) {
                image.set(i, j, (float)(i + j));
            }
        }
        
        Shift shift = new Shift(1, 2, Shift.NOWRAP);
        RealGrayImage result = shift.apply(image);
        
        assertEquals(3, result.get(0, 0), 0.001);
        assertEquals(3, result.get(0, 1), 0.001);
        assertEquals(3, result.get(0, 2), 0.001);
        assertEquals(3, result.get(1, 0), 0.001);
        assertEquals(4, result.get(1, 1), 0.001);
        assertEquals(3, result.get(1, 2), 0.001);
        assertEquals(3, result.get(2, 0), 0.001);
        assertEquals(3, result.get(2, 1), 0.001);
        assertEquals(3, result.get(2, 2), 0.001);
    }
    
    @Test
    public void testShift_colorImage_withWrap() {
        ColorImage image = new ColorImage(3, 3);
        for (int i = 0; i < image.X(); i++) {
            for (int j = 0; j < image.Y(); j++) {
                image.set(i, j, i + j);
            }
        }
        
        Shift shift = new Shift(1, 2, Shift.WRAP);
        ColorImage result = shift.apply(image);
        
        assertEquals(0, result.get(0, 0, 0));
        assertEquals(0, result.get(0, 0, 1));
        assertEquals(0, result.get(0, 0, 2));
        assertEquals(0, result.get(1, 1, 0));
        assertEquals(0, result.get(1, 1, 1));
        assertEquals(0, result.get(1, 1, 2));
        assertEquals(2, result.get(2, 2, 0));
        assertEquals(2, result.get(2, 2, 1));
        assertEquals(2, result.get(2, 2, 2));
    }
    
    @Test
    public void testShift_colorImage_withoutWrap() {
        ColorImage image = new ColorImage(3, 3);
        for (int i = 0; i < image.X(); i++) {
            for (int j = 0; j < image.Y(); j++) {
                image.set(i, j, i + j);
            }
        }
        
        Shift shift = new Shift(1, 2, Shift.NOWRAP);
        ColorImage result = shift.apply(image);
        
        assertEquals(3, result.get(0, 0, 0));
        assertEquals(3, result.get(0, 0, 1));
        assertEquals(3, result.get(0, 0, 2));
        assertEquals(3, result.get(1, 1, 0));
        assertEquals(3, result.get(1, 1, 1));
        assertEquals(3, result.get(1, 1, 2));
        assertEquals(3, result.get(2, 2, 0));
        assertEquals(3, result.get(2, 2, 1));
        assertEquals(3, result.get(2, 2, 2));
    }
    
    @Test
    public void testShift_realColorImage_withWrap() {
        RealColorImage image = new RealColorImage(3, 3);
        for (int i = 0; i < image.X(); i++) {
            for (int j = 0; j < image.Y(); j++) {
                image.set(i, j, (float)(i + j));
            }
        }
        
        Shift shift = new Shift(1, 2, Shift.WRAP);
        RealColorImage result = shift.apply(image);
        
        assertEquals(0, result.get(0, 0, 0), 0.001);
        assertEquals(0, result.get(0, 0, 1), 0.001);
        assertEquals(0, result.get(0, 0, 2), 0.001);
        assertEquals(0, result.get(1, 1, 0), 0.001);
        assertEquals(0, result.get(1, 1, 1), 0.001);
        assertEquals(0, result.get(1, 1, 2), 0.001);
        assertEquals(2, result.get(2, 2, 0), 0.001);
        assertEquals(2, result.get(2, 2, 1), 0.001);
        assertEquals(2, result.get(2, 2, 2), 0.001);
    }
    
    @Test
    public void testShift_realColorImage_withoutWrap() {
        RealColorImage image = new RealColorImage(3, 3);
        for (int i = 0; i < image.X(); i++) {
            for (int j = 0; j < image.Y(); j++) {
                image.set(i, j, (float)(i + j));
            }
        }
        
        Shift shift = new Shift(1, 2, Shift.NOWRAP);
        RealColorImage result = shift.apply(image);
        
        assertEquals(3, result.get(0, 0, 0), 0.001);
        assertEquals(3, result.get(0, 0, 1), 0.001);
        assertEquals(3, result.get(0, 0, 2), 0.001);
        assertEquals(3, result.get(1, 1, 0), 0.001);
        assertEquals(3, result.get(1, 1, 1), 0.001);
        assertEquals(3, result.get(1, 1, 2), 0.001);
        assertEquals(3, result.get(2, 2, 0), 0.001);
        assertEquals(3, result.get(2, 2, 1), 0.001);
        assertEquals(3, result.get(2, 2, 2), 0.001);
    }
}