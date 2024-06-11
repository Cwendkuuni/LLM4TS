package jigl.image.ops;

import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.ColorImage;
import jigl.image.RealColorImage;

public class ShiftTest {
    
    @Test
    public void testApply_GrayImage() {
        // Test basic functionality of apply method for GrayImage input
        GrayImage image = new GrayImage(4, 4);
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage result = shift.apply(image);
        // Add assertions here
        // Example: assertArrayEquals(image.getArray(), result.getArray());
    }
    
    @Test
    public void testApply_RealGrayImage() {
        // Test basic functionality of apply method for RealGrayImage input
        RealGrayImage image = new RealGrayImage(4, 4);
        Shift shift = new Shift(-1, 2, Shift.WRAP);
        RealGrayImage result = shift.apply(image);
        // Add assertions here
        // Example: assertArrayEquals(image.getArray(), result.getArray());
    }
    
    @Test
    public void testApply_ColorImage() {
        // Test basic functionality of apply method for ColorImage input
        ColorImage image = new ColorImage(4, 4);
        Shift shift = new Shift(2, 0, Shift.NOWRAP);
        ColorImage result = shift.apply(image);
        // Add assertions here
        // Example: assertArrayEquals(image.getArray(), result.getArray());
    }
    
    @Test
    public void testApply_RealColorImage() {
        // Test basic functionality of apply method for RealColorImage input
        RealColorImage image = new RealColorImage(4, 4);
        Shift shift = new Shift(0, -2, Shift.WRAP);
        RealColorImage result = shift.apply(image);
        // Add assertions here
        // Example: assertArrayEquals(image.getArray(), result.getArray());
    }
}