package jigl.image.ops;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.ColorImage;
import jigl.image.RealColorImage;

public class ShiftTest {

    private Shift shift;

    @Before
    public void setUp() {
        shift = new Shift(2, 2, Shift.WRAP);
    }

    @Test
    public void testApply_GrayImage() {
        GrayImage image = new GrayImage(5, 5);
        // Set image values
        
        GrayImage expected = new GrayImage(5, 5);
        // Set expected values
        
        GrayImage result = shift.apply(image);
        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testApply_RealGrayImage() {
        RealGrayImage image = new RealGrayImage(5, 5);
        // Set image values
        
        RealGrayImage expected = new RealGrayImage(5, 5);
        // Set expected values
        
        RealGrayImage result = shift.apply(image);
        assertArrayEquals(expected.toArray(), result.toArray(), 0.001);
    }

    @Test
    public void testApply_ColorImage() {
        ColorImage image = new ColorImage(5, 5);
        // Set image values
        
        ColorImage expected = new ColorImage(5, 5);
        // Set expected values
        
        ColorImage result = shift.apply(image);
        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testApply_RealColorImage() {
        RealColorImage image = new RealColorImage(5, 5);
        // Set image values
        
        RealColorImage expected = new RealColorImage(5, 5);
        // Set expected values
        
        RealColorImage result = shift.apply(image);
        assertArrayEquals(expected.toArray(), result.toArray(), 0.001);
    }
}