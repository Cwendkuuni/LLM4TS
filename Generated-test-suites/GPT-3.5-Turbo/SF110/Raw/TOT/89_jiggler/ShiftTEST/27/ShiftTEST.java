import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.ColorImage;
import jigl.image.RealColorImage;

public class ShiftTest {
    
    @Test
    public void testApplyGrayImage_wrap() {
        // typical use case
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 1, 2);
        image.set(2, 2, 3);
        Shift shift = new Shift(1, 1, Shift.WRAP);
        GrayImage result = shift.apply(image);
        assertEquals(3, result.get(0, 0));
        assertEquals(1, result.get(1, 1));
        assertEquals(2, result.get(2, 2));
        
        // edge case
        shift = new Shift(1, 1, Shift.WRAP);
        result = shift.apply(image);
        assertEquals(2, result.get(0, 0));
        assertEquals(3, result.get(1, 1));
        assertEquals(1, result.get(2, 2));
        
        // error scenario
        shift = new Shift(4, 4, Shift.WRAP);
        result = shift.apply(image);
        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(1, 1));
        assertEquals(3, result.get(2, 2));
    }
    
    @Test
    public void testApplyGrayImage_noWrap() {
        // typical use case
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 1, 2);
        image.set(2, 2, 3);
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage result = shift.apply(image);
        assertEquals(0, result.get(0, 0));
        assertEquals(0, result.get(1, 1));
        assertEquals(0, result.get(2, 2));
        
        // edge case
        shift = new Shift(1, 1, Shift.NOWRAP);
        result = shift.apply(image);
        assertEquals(2, result.get(0, 0));
        assertEquals(3, result.get(1, 1));
        assertEquals(0, result.get(2, 2));
        
        // error scenario
        shift = new Shift(4, 4, Shift.NOWRAP);
        result = shift.apply(image);
        assertEquals(0, result.get(0, 0));
        assertEquals(0, result.get(1, 1));
        assertEquals(0, result.get(2, 2));
    }
    
    @Test
    public void testApplyRealGrayImage_wrap() {
        // typical use case
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.0f);
        image.set(1, 1, 2.0f);
        image.set(2, 2, 3.0f);
        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealGrayImage result = shift.apply(image);
        assertEquals(3.0f, result.get(0, 0), 0.001);
        assertEquals(1.0f, result.get(1, 1), 0.001);
        assertEquals(2.0f, result.get(2, 2), 0.001);
        
        // edge case
        shift = new Shift(1, 1, Shift.WRAP);
        result = shift.apply(image);
        assertEquals(2.0f, result.get(0, 0), 0.001);
        assertEquals(3.0f, result.get(1, 1), 0.001);
        assertEquals(1.0f, result.get(2, 2), 0.001);
        
        // error scenario
        shift = new Shift(4, 4, Shift.WRAP);
        result = shift.apply(image);
        assertEquals(1.0f, result.get(0, 0), 0.001);
        assertEquals(2.0f, result.get(1, 1), 0.001);
        assertEquals(3.0f, result.get(2, 2), 0.001);
    }
    
    @Test
    public void testApplyRealGrayImage_noWrap() {
        // typical use case
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.0f);
        image.set(1, 1, 2.0f);
        image.set(2, 2, 3.0f);
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        RealGrayImage result = shift.apply(image);
        assertEquals(0.0f, result.get(0, 0), 0.001);
        assertEquals(0.0f, result.get(1, 1), 0.001);
        assertEquals(0.0f, result.get(2, 2), 0.001);
        
        // edge case
        shift = new Shift(1, 1, Shift.NOWRAP);
        result = shift.apply(image);
        assertEquals(2.0f, result.get(0, 0), 0.001);
        assertEquals(3.0f, result.get(1, 1), 0.001);
        assertEquals(0.0f, result.get(2, 2), 0.001);
        
        // error scenario
        shift = new Shift(4, 4, Shift.NOWRAP);
        result = shift.apply(image);
        assertEquals(0.0f, result.get(0, 0), 0.001);
        assertEquals(0.0f, result.get(1, 1), 0.001);
        assertEquals(0.0f, result.get(2, 2), 0.001);
    }
    
    @Test
    public void testApplyColorImage_wrap() {
        // typical use case
        ColorImage image = new ColorImage(3, 3);
        image.set(0, 0, 1, 2, 3);
        image.set(1, 1, 4, 5, 6);
        image.set(2, 2, 7, 8, 9);
        Shift shift = new Shift(1, 1, Shift.WRAP);
        ColorImage result = shift.apply(image);
        assertEquals(7, result.get(0, 0, ColorImage.RED));
        assertEquals(1, result.get(1, 1, ColorImage.RED));
        assertEquals(4, result.get(2, 2, ColorImage.RED));
        
        // edge case
        shift = new Shift(1, 1, Shift.WRAP);
        result = shift.apply(image);
        assertEquals(5, result.get(0, 0, ColorImage.GREEN));
        assertEquals(6, result.get(1, 1, ColorImage.GREEN));
        assertEquals(2, result.get(2, 2, ColorImage.GREEN));
        
        // error scenario
        shift = new Shift(4, 4, Shift.WRAP);
        result = shift.apply(image);
        assertEquals(1, result.get(0, 0, ColorImage.BLUE));
        assertEquals(2, result.get(1, 1, ColorImage.BLUE));
        assertEquals(3, result.get(2, 2, ColorImage.BLUE));
    }
    
    @Test
    public void testApplyColorImage_noWrap() {
        // typical use case
        ColorImage image = new ColorImage(3, 3);
        image.set(0, 0, 1, 2, 3);
        image.set(1, 1, 4, 5, 6);
        image.set(2, 2, 7, 8, 9);
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        ColorImage result = shift.apply(image);
        assertEquals(0, result.get(0, 0, ColorImage.RED));
        assertEquals(0, result.get(1, 1, ColorImage.RED));
        assertEquals(0, result.get(2, 2, ColorImage.RED));
        
        // edge case
        shift = new Shift(1, 1, Shift.NOWRAP);
        result = shift.apply(image);
        assertEquals(5, result.get(0, 0, ColorImage.GREEN));
        assertEquals(6, result.get(1, 1, ColorImage.GREEN));
        assertEquals(0, result.get(2, 2, ColorImage.GREEN));
        
        // error scenario
        shift = new Shift(4, 4, Shift.NOWRAP);
        result = shift.apply(image);
        assertEquals(0, result.get(0, 0, ColorImage.BLUE));
        assertEquals(0, result.get(1, 1, ColorImage.BLUE));
        assertEquals(0, result.get(2, 2, ColorImage.BLUE));
    }
    
    @Test
    public void testApplyRealColorImage_wrap() {
        // typical use case
        RealColorImage image = new RealColorImage(3, 3);
        image.set(0, 0, 1.0f, 2.0f, 3.0f);
        image.set(1, 1, 4.0f, 5.0f, 6.0f);
        image.set(2, 2, 7.0f, 8.0f, 9.0f);
        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealColorImage result = shift.apply(image);
        assertEquals(7.0f, result.get(0, 0, RealColorImage.RED), 0.001);
        assertEquals(1.0f, result.get(1, 1, RealColorImage.RED), 0.001);
        assertEquals(4.0f, result.get(2, 2, RealColorImage.RED), 0.001);
        
        // edge case
        shift = new Shift(1, 1, Shift.WRAP);
        result = shift.apply(image);
        assertEquals(5.0f, result.get(0, 0, RealColorImage.GREEN), 0.001);
        assertEquals(6.0f, result.get(1, 1, RealColorImage.GREEN), 0.001);
        assertEquals(2.0f, result.get(2, 2, RealColorImage.GREEN), 0.001);
        
        // error scenario
        shift = new Shift(4, 4, Shift.WRAP);
        result = shift.apply(image);
        assertEquals(1.0f, result.get(0, 0, RealColorImage.BLUE), 0.001);
        assertEquals(2.0f, result.get(1, 1, RealColorImage.BLUE), 0.001);
        assertEquals(3.0f, result.get(2, 2, RealColorImage.BLUE), 0.001);
    }
    
    @Test
    public void testApplyRealColorImage_noWrap() {
        // typical use case
        RealColorImage image = new RealColorImage(3, 3);
        image.set(0, 0, 1.0f, 2.0f, 3.0f);
        image.set(1, 1, 4.0f, 5.0f, 6.0f);
        image.set(2, 2, 7.0f, 8.0f, 9.0f);
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        RealColorImage result = shift.apply(image);
        assertEquals(0.0f, result.get(0, 0, RealColorImage.RED), 0.001);
        assertEquals(0.0f, result.get(1, 1, RealColorImage.RED), 0.001);
        assertEquals(0.0f, result.get(2, 2, RealColorImage.RED), 0.001);
        
        // edge case
        shift = new Shift(1, 1, Shift.NOWRAP);
        result = shift.apply(image);
        assertEquals(5.0f, result.get(0, 0, RealColorImage.GREEN), 0.001);
        assertEquals(6.0f, result.get(1, 1, RealColorImage.GREEN), 0.001);
        assertEquals(0.0f, result.get(2, 2, RealColorImage.GREEN), 0.001);
        
        // error scenario
        shift = new Shift(4, 4, Shift.NOWRAP);
        result = shift.apply(image);
        assertEquals(0.0f, result.get(0, 0, RealColorImage.BLUE), 0.001);
        assertEquals(0.0f, result.get(1, 1, RealColorImage.BLUE), 0.001);
        assertEquals(0.0f, result.get(2, 2, RealColorImage.BLUE), 0.001);
    }
}