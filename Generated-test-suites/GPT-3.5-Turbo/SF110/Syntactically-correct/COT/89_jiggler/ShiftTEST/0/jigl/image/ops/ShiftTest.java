import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ShiftTest {

    @Test
    public void testShiftGrayImageWrap() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        GrayImage image = new GrayImage(3, 3);
      
        // Set pixels in the image
        
        // Expected result after shifting
        GrayImage expected = new GrayImage(3, 3);
      
        // Validate the result
        assertEquals(expected, shift.apply(image));
    }
    
    @Test
    public void testShiftGrayImageNoWrap() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage image = new GrayImage(3, 3);
      
        // Set pixels in the image
        
        // Expected result after shifting
        GrayImage expected = new GrayImage(3, 3);
      
        // Validate the result
        assertEquals(expected, shift.apply(image));
    }
    
    @Test
    public void testShiftRealGrayImageWrap() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealGrayImage image = new RealGrayImage(3, 3);
      
        // Set pixels in the image
        
        // Expected result after shifting
        RealGrayImage expected = new RealGrayImage(3, 3);
      
        // Validate the result
        assertEquals(expected, shift.apply(image));
    }
    
    @Test
    public void testShiftRealGrayImageNoWrap() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        RealGrayImage image = new RealGrayImage(3, 3);
      
        // Set pixels in the image
        
        // Expected result after shifting
        RealGrayImage expected = new RealGrayImage(3, 3);
      
        // Validate the result
        assertEquals(expected, shift.apply(image));
    }
    
    @Test
    public void testShiftColorImageWrap() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        ColorImage image = new ColorImage(3, 3);
      
        // Set pixels in the image
        
        // Expected result after shifting
        ColorImage expected = new ColorImage(3, 3);
      
        // Validate the result
        assertEquals(expected, shift.apply(image));
    }
    
    @Test
    public void testShiftColorImageNoWrap() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        ColorImage image = new ColorImage(3, 3);
      
        // Set pixels in the image
        
        // Expected result after shifting
        ColorImage expected = new ColorImage(3, 3);
      
        // Validate the result
        assertEquals(expected, shift.apply(image));
    }
    
    @Test
    public void testShiftRealColorImageWrap() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealColorImage image = new RealColorImage(3, 3);
      
        // Set pixels in the image
        
        // Expected result after shifting
        RealColorImage expected = new RealColorImage(3, 3);
      
        // Validate the result
        assertEquals(expected, shift.apply(image));
    }
    
    @Test
    public void testShiftRealColorImageNoWrap() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        RealColorImage image = new RealColorImage(3, 3);
      
        // Set pixels in the image
        
        // Expected result after shifting
        RealColorImage expected = new RealColorImage(3, 3);
      
        // Validate the result
        assertEquals(expected, shift.apply(image));
    }
    
}