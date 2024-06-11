package jigl.image.ops;

import jigl.image.types.*;
import jigl.image.*;
import jigl.image.exceptions.*;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ShiftTest {
    
    @Test
    public void testApply_GrayImage() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 1, 2);
        image.set(2, 2, 3);
        
        GrayImage result = shift.apply(image);
        
        assertEquals(0, result.get(0, 0));
        assertEquals(0, result.get(1, 0));
        assertEquals(0, result.get(2, 0));
        assertEquals(0, result.get(0, 1));
        assertEquals(1, result.get(1, 1));
        assertEquals(0, result.get(2, 1));
        assertEquals(0, result.get(0, 2));
        assertEquals(0, result.get(1, 2));
        assertEquals(2, result.get(2, 2));
    }
    
    @Test
    public void testApply_RealGrayImage() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.0f);
        image.set(1, 1, 2.0f);
        image.set(2, 2, 3.0f);
        
        RealGrayImage result = shift.apply(image);
        
        assertEquals(0.0f, result.get(0, 0), 0.001);
        assertEquals(0.0f, result.get(1, 0), 0.001);
        assertEquals(0.0f, result.get(2, 0), 0.001);
        assertEquals(0.0f, result.get(0, 1), 0.001);
        assertEquals(1.0f, result.get(1, 1), 0.001);
        assertEquals(0.0f, result.get(2, 1), 0.001);
        assertEquals(0.0f, result.get(0, 2), 0.001);
        assertEquals(0.0f, result.get(1, 2), 0.001);
        assertEquals(2.0f, result.get(2, 2), 0.001);
    }

    @Test
    public void testApply_ColorImage() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        ColorImage image = new ColorImage(3, 3);
        image.set(0, 0, 1, 2, 3);
        image.set(1, 1, 4, 5, 6);
        image.set(2, 2, 7, 8, 9);
        
        ColorImage result = shift.apply(image);
        
        assertEquals(0, result.get(0, 0, 0));
        assertEquals(0, result.get(1, 0, 0));
        assertEquals(0, result.get(2, 0, 0));
        assertEquals(0, result.get(0, 1, 0));
        assertEquals(1, result.get(1, 1, 0));
        assertEquals(0, result.get(2, 1, 0));
        assertEquals(0, result.get(0, 2, 0));
        assertEquals(0, result.get(1, 2, 0));
        assertEquals(4, result.get(2, 2, 0));
        
        assertEquals(0, result.get(0, 0, 1));
        assertEquals(0, result.get(1, 0, 1));
        assertEquals(0, result.get(2, 0, 1));
        assertEquals(0, result.get(0, 1, 1));
        assertEquals(2, result.get(1, 1, 1));
        assertEquals(0, result.get(2, 1, 1));
        assertEquals(0, result.get(0, 2, 1));
        assertEquals(0, result.get(1, 2, 1));
        assertEquals(5, result.get(2, 2, 1));
        
        assertEquals(0, result.get(0, 0, 2));
        assertEquals(0, result.get(1, 0, 2));
        assertEquals(0, result.get(2, 0, 2));
        assertEquals(0, result.get(0, 1, 2));
        assertEquals(3, result.get(1, 1, 2));
        assertEquals(0, result.get(2, 1, 2));
        assertEquals(0, result.get(0, 2, 2));
        assertEquals(0, result.get(1, 2, 2));
        assertEquals(6, result.get(2, 2, 2));
    }
    
    @Test
    public void testApply_RealColorImage() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        RealColorImage image = new RealColorImage(3, 3);
        image.set(0, 0, 1.1f, 2.2f, 3.3f);
        image.set(1, 1, 4.4f, 5.5f, 6.6f);
        image.set(2, 2, 7.7f, 8.8f, 9.9f);
        
        RealColorImage result = shift.apply(image);
        
        assertEquals(0.0f, result.get(0, 0, 0), 0.001);
        assertEquals(0.0f, result.get(1, 0, 0), 0.001);
        assertEquals(0.0f, result.get(2, 0, 0), 0.001);
        assertEquals(0.0f, result.get(0, 1, 0), 0.001);
        assertEquals(1.1f, result.get(1, 1, 0), 0.001);
        assertEquals(0.0f, result.get(2, 1, 0), 0.001);
        assertEquals(0.0f, result.get(0, 2, 0), 0.001);
        assertEquals(0.0f, result.get(1, 2, 0), 0.001);
        assertEquals(4.4f, result.get(2, 2, 0), 0.001);
        
        assertEquals(0.0f, result.get(0, 0, 1), 0.001);
        assertEquals(0.0f, result.get(1, 0, 1), 0.001);
        assertEquals(0.0f, result.get(2, 0, 1), 0.001);
        assertEquals(0.0f, result.get(0, 1, 1), 0.001);
        assertEquals(2.2f, result.get(1, 1, 1), 0.001);
        assertEquals(0.0f, result.get(2, 1, 1), 0.001);
        assertEquals(0.0f, result.get(0, 2, 1), 0.001);
        assertEquals(0.0f, result.get(1, 2, 1), 0.001);
        assertEquals(5.5f, result.get(2, 2, 1), 0.001);
        
        assertEquals(0.0f, result.get(0, 0, 2), 0.001);
        assertEquals(0.0f, result.get(1, 0, 2), 0.001);
        assertEquals(0.0f, result.get(2, 0, 2), 0.001);
        assertEquals(0.0f, result.get(0, 1, 2), 0.001);
        assertEquals(3.3f, result.get(1, 1, 2), 0.001);
        assertEquals(0.0f, result.get(2, 1, 2), 0.001);
        assertEquals(0.0f, result.get(0, 2, 2), 0.001);
        assertEquals(0.0f, result.get(1, 2, 2), 0.001);
        assertEquals(6.6f, result.get(2, 2, 2), 0.001);
    }
}