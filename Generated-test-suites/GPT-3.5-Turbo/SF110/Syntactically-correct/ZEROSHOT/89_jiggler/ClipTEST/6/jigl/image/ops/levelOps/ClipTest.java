import static org.junit.Assert.*;
import org.junit.Test;

public class ClipTest {

    @Test
    public void testApplyGrayImage() {
        // Create test inputs
        GrayImage image = new GrayImage(100, 100);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        int int_min = 0;
        int int_max = 255;
        float float_min = 0.0f;
        float float_max = 1.0f;
        
        // Create expected output
        GrayImage expected = new GrayImage(100, 100);
        
        // Instantiate Clip object
        Clip clip = new Clip(int_min, int_max);
        
        // Apply the method
        GrayImage result = clip.apply(image, roi);
        
        // Assert the result
        assertEquals(expected, result);
    }

    @Test
    public void testApplyRealGrayImage() {
        // Create test inputs
        RealGrayImage image = new RealGrayImage(100, 100);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        int int_min = 0;
        int int_max = 255;
        float float_min = 0.0f;
        float float_max = 1.0f;
        
        // Create expected output
        RealGrayImage expected = new RealGrayImage(100, 100);
        
        // Instantiate Clip object
        Clip clip = new Clip(float_min, float_max);
        
        // Apply the method
        RealGrayImage result = clip.apply(image, roi);
        
        // Assert the result
        assertEquals(expected, result);
    }

    @Test
    public void testApplyColorImage() {
        // Create test inputs
        ColorImage image = new ColorImage(100, 100);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        int int_min = 0;
        int int_max = 255;
        float float_min = 0.0f;
        float float_max = 1.0f;
        
        // Create expected output
        ColorImage expected = new ColorImage(100, 100);
        
        // Instantiate Clip object
        Clip clip = new Clip(int_min, int_max);
        
        // Apply the method
        ColorImage result = clip.apply(image, roi);
        
        // Assert the result
        assertEquals(expected, result);
    }

    @Test
    public void testApplyRealColorImage() {
        // Create test inputs
        RealColorImage image = new RealColorImage(100, 100);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        int int_min = 0;
        int int_max = 255;
        float float_min = 0.0f;
        float float_max = 1.0f;
        
        // Create expected output
        RealColorImage expected = new RealColorImage(100, 100);
        
        // Instantiate Clip object
        Clip clip = new Clip(float_min, float_max);
        
        // Apply the method
        RealColorImage result = clip.apply(image, roi);
        
        // Assert the result
        assertEquals(expected, result);
    }
}