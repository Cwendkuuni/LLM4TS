package jigl.image.ops;

import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.ColorImage;
import jigl.image.RealColorImage;

import static org.junit.Assert.*;
import org.junit.Test;

public class ShiftTest {

    @Test
    public void testApply_GrayImage() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 2);
        image.set(0, 2, 3);
        image.set(1, 0, 4);
        image.set(1, 1, 5);
        image.set(1, 2, 6);
        image.set(2, 0, 7);
        image.set(2, 1, 8);
        image.set(2, 2, 9);
        
        GrayImage expected = new GrayImage(3, 3);
        expected.set(0, 0, 9);
        expected.set(0, 1, 7);
        expected.set(0, 2, 8);
        expected.set(1, 0, 3);
        expected.set(1, 1, 1);
        expected.set(1, 2, 2);
        expected.set(2, 0, 4);
        expected.set(2, 1, 5);
        expected.set(2, 2, 6);
        
        GrayImage result = shift.apply(image);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testApply_RealGrayImage() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.0);
        image.set(0, 1, 2.0);
        image.set(0, 2, 3.0);
        image.set(1, 0, 4.0);
        image.set(1, 1, 5.0);
        image.set(1, 2, 6.0);
        image.set(2, 0, 7.0);
        image.set(2, 1, 8.0);
        image.set(2, 2, 9.0);
        
        RealGrayImage expected = new RealGrayImage(3, 3);
        expected.set(0, 0, 9.0);
        expected.set(0, 1, 7.0);
        expected.set(0, 2, 8.0);
        expected.set(1, 0, 3.0);
        expected.set(1, 1, 1.0);
        expected.set(1, 2, 2.0);
        expected.set(2, 0, 4.0);
        expected.set(2, 1, 5.0);
        expected.set(2, 2, 6.0);
        
        RealGrayImage result = shift.apply(image);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testApply_ColorImage() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        
        ColorImage image = new ColorImage(3, 3);
        image.set(0, 0, 0, 1);
        image.set(0, 1, 0, 2);
        image.set(0, 2, 0, 3);
        image.set(1, 0, 0, 4);
        image.set(1, 1, 0, 5);
        image.set(1, 2, 0, 6);
        image.set(2, 0, 0, 7);
        image.set(2, 1, 0, 8);
        image.set(2, 2, 0, 9);
        
        ColorImage expected = new ColorImage(3, 3);
        expected.set(0, 0, 0, 9);
        expected.set(0, 1, 0, 7);
        expected.set(0, 2, 0, 8);
        expected.set(1, 0, 0, 3);
        expected.set(1, 1, 0, 1);
        expected.set(1, 2, 0, 2);
        expected.set(2, 0, 0, 4);
        expected.set(2, 1, 0, 5);
        expected.set(2, 2, 0, 6);
        
        ColorImage result = shift.apply(image);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testApply_RealColorImage() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        
        RealColorImage image = new RealColorImage(3, 3);
        image.set(0, 0, 0, 1.0);
        image.set(0, 1, 0, 2.0);
        image.set(0, 2, 0, 3.0);
        image.set(1, 0, 0, 4.0);
        image.set(1, 1, 0, 5.0);
        image.set(1, 2, 0, 6.0);
        image.set(2, 0, 0, 7.0);
        image.set(2, 1, 0, 8.0);
        image.set(2, 2, 0, 9.0);
        
        RealColorImage expected = new RealColorImage(3, 3);
        expected.set(0, 0, 0, 9.0);
        expected.set(0, 1, 0, 7.0);
        expected.set(0, 2, 0, 8.0);
        expected.set(1, 0, 0, 3.0);
        expected.set(1, 1, 0, 1.0);
        expected.set(1, 2, 0, 2.0);
        expected.set(2, 0, 0, 4.0);
        expected.set(2, 1, 0, 5.0);
        expected.set(2, 2, 0, 6.0);
        
        RealColorImage result = shift.apply(image);
        
        assertEquals(expected, result);
    }
}