
import org.junit.Test;
import static org.junit.Assert.*;

public class ShiftTest {

    @Test
    public void testApplyGrayImage() {
        // Create a GrayImage for testing
        GrayImage image = new GrayImage(10, 10);
        
        // Create a Shift object
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        
        // Apply the Shift operation to the GrayImage
        GrayImage result = shift.apply(image);
        
        // Check if the result has the same dimensions as the input image
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
        
        // TODO: Add additional asserts to check the functionality
        // of the apply method with GrayImage inputs
        assertTrue(result instanceof GrayImage);
    }

    @Test
    public void testApplyRealGrayImage() {
        // Create a RealGrayImage for testing
        RealGrayImage image = new RealGrayImage(10, 10);
        
        // Create a Shift object
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        
        // Apply the Shift operation to the RealGrayImage
        RealGrayImage result = shift.apply(image);
        
        // Check if the result has the same dimensions as the input image
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
        
        // TODO: Add additional asserts to check the functionality
        // of the apply method with RealGrayImage inputs
        assertTrue(result instanceof RealGrayImage);
    }
    
    @Test
    public void testApplyColorImage() {
        // Create a ColorImage for testing
        ColorImage image = new ColorImage(10, 10);
        
        // Create a Shift object
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        
        // Apply the Shift operation to the ColorImage
        ColorImage result = shift.apply(image);
        
        // Check if the result has the same dimensions as the input image
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
        
        // TODO: Add additional asserts to check the functionality
        // of the apply method with ColorImage inputs
        assertTrue(result instanceof ColorImage);
    }
    
    @Test
    public void testApplyRealColorImage() {
        // Create a RealColorImage for testing
        RealColorImage image = new RealColorImage(10, 10);
        
        // Create a Shift object
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        
        // Apply the Shift operation to the RealColorImage
        RealColorImage result = shift.apply(image);
        
        // Check if the result has the same dimensions as the input image
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
        
        // TODO: Add additional asserts to check the functionality
        // of the apply method with RealColorImage inputs
        assertTrue(result instanceof RealColorImage);
    }

}
