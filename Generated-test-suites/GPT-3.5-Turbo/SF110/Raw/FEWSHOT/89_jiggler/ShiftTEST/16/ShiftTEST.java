package jigl.image.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import jigl.image.ColorImage;
import jigl.image.GrayImage;
import jigl.image.RealColorImage;
import jigl.image.RealGrayImage;

public class ShiftTest {
    
    @Test
    public void testApply_grayImage() {
        Shift shift = new Shift(1, 1, 0);
        GrayImage image = new GrayImage();
        // test cases
        try {
            shift.apply(image);
        } catch (Exception e) {
            fail("Exception thrown");
        }
        
        // add more test cases
        
    }
    
    @Test
    public void testApply_realGrayImage() {
        Shift shift = new Shift(1, 1, 0);
        RealGrayImage image = new RealGrayImage();
        // test cases
        try {
            shift.apply(image);
        } catch (Exception e) {
            fail("Exception thrown");
        }
        
        // add more test cases
        
    }
    
    @Test
    public void testApply_colorImage() {
        Shift shift = new Shift(1, 1, 0);
        ColorImage image = new ColorImage();
        // test cases
        try {
            shift.apply(image);
        } catch (Exception e) {
            fail("Exception thrown");
        }
        
        // add more test cases
        
    }
    
    @Test
    public void testApply_realColorImage() {
        Shift shift = new Shift(1, 1, 0);
        RealColorImage image = new RealColorImage();
        // test cases
        try {
            shift.apply(image);
        } catch (Exception e) {
            fail("Exception thrown");
        }
        
        // add more test cases
        
    }
}