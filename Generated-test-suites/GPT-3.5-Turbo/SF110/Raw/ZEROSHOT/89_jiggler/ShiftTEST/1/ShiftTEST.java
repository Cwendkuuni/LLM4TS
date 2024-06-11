import org.junit.Test;
import static org.junit.Assert.*;

public class ShiftTest {

    @Test
    public void testApply_GrayImage_NoWrap() {
        // Create test input
        GrayImage image = new GrayImage(5, 5);
        image.set(0, 0, 100);
        image.set(1, 2, 200);
        
        // Create expected output
        GrayImage expected = new GrayImage(5, 5);
        expected.set(2, 2, 100);
        expected.set(3, 4, 200);
        
        // Apply shift operator
        Shift shift = new Shift(2, 2, Shift.NOWRAP);
        GrayImage result = shift.apply(image);
        
        // Compare result with expected output
        assertEquals(expected, result);
    }
    
    @Test
    public void testApply_GrayImage_Wrap() {
        // Create test input
        GrayImage image = new GrayImage(5, 5);
        image.set(0, 0, 100);
        image.set(1, 2, 200);
        
        // Create expected output
        GrayImage expected = new GrayImage(5, 5);
        expected.set(2, 2, 100);
        expected.set(3, 4, 200);
        
        // Apply shift operator
        Shift shift = new Shift(2, 2, Shift.WRAP);
        GrayImage result = shift.apply(image);
        
        // Compare result with expected output
        assertEquals(expected, result);
    }
    
    @Test
    public void testApply_RealGrayImage_NoWrap() {
        // Create test input
        RealGrayImage image = new RealGrayImage(5, 5);
        image.set(0, 0, 0.5f);
        image.set(1, 2, 0.8f);
        
        // Create expected output
        RealGrayImage expected = new RealGrayImage(5, 5);
        expected.set(2, 2, 0.5f);
        expected.set(3, 4, 0.8f);
        
        // Apply shift operator
        Shift shift = new Shift(2, 2, Shift.NOWRAP);
        RealGrayImage result = shift.apply(image);
        
        // Compare result with expected output
        assertEquals(expected, result);
    }
    
    @Test
    public void testApply_RealGrayImage_Wrap() {
        // Create test input
        RealGrayImage image = new RealGrayImage(5, 5);
        image.set(0, 0, 0.5f);
        image.set(1, 2, 0.8f);
        
        // Create expected output
        RealGrayImage expected = new RealGrayImage(5, 5);
        expected.set(2, 2, 0.5f);
        expected.set(3, 4, 0.8f);
        
        // Apply shift operator
        Shift shift = new Shift(2, 2, Shift.WRAP);
        RealGrayImage result = shift.apply(image);
        
        // Compare result with expected output
        assertEquals(expected, result);
    }
    
    @Test
    public void testApply_ColorImage_NoWrap() {
        // Create test input
        ColorImage image = new ColorImage(5, 5);
        image.set(0, 0, 100, 150, 200);
        image.set(1, 2, 50, 75, 100);
        
        // Create expected output
        ColorImage expected = new ColorImage(5, 5);
        expected.set(2, 2, 100, 150, 200);
        expected.set(3, 4, 50, 75, 100);
        
        // Apply shift operator
        Shift shift = new Shift(2, 2, Shift.NOWRAP);
        ColorImage result = shift.apply(image);
        
        // Compare result with expected output
        assertEquals(expected, result);
    }
    
    @Test
    public void testApply_ColorImage_Wrap() {
        // Create test input
        ColorImage image = new ColorImage(5, 5);
        image.set(0, 0, 100, 150, 200);
        image.set(1, 2, 50, 75, 100);
        
        // Create expected output
        ColorImage expected = new ColorImage(5, 5);
        expected.set(2, 2, 100, 150, 200);
        expected.set(3, 4, 50, 75, 100);
        
        // Apply shift operator
        Shift shift = new Shift(2, 2, Shift.WRAP);
        ColorImage result = shift.apply(image);
        
        // Compare result with expected output
        assertEquals(expected, result);
    }
    
    @Test
    public void testApply_RealColorImage_NoWrap() {
        // Create test input
        RealColorImage image = new RealColorImage(5, 5);
        image.set(0, 0, 0.5f, 0.6f, 0.7f);
        image.set(1, 2, 0.8f, 0.9f, 1.0f);
        
        // Create expected output
        RealColorImage expected = new RealColorImage(5, 5);
        expected.set(2, 2, 0.5f, 0.6f, 0.7f);
        expected.set(3, 4, 0.8f, 0.9f, 1.0f);
        
        // Apply shift operator
        Shift shift = new Shift(2, 2, Shift.NOWRAP);
        RealColorImage result = shift.apply(image);
        
        // Compare result with expected output
        assertEquals(expected, result);
    }
    
    @Test
    public void testApply_RealColorImage_Wrap() {
        // Create test input
        RealColorImage image = new RealColorImage(5, 5);
        image.set(0, 0, 0.5f, 0.6f, 0.7f);
        image.set(1, 2, 0.8f, 0.9f, 1.0f);
        
        // Create expected output
        RealColorImage expected = new RealColorImage(5, 5);
        expected.set(2, 2, 0.5f, 0.6f, 0.7f);
        expected.set(3, 4, 0.8f, 0.9f, 1.0f);
        
        // Apply shift operator
        Shift shift = new Shift(2, 2, Shift.WRAP);
        RealColorImage result = shift.apply(image);
        
        // Compare result with expected output
        assertEquals(expected, result);
    }
}