import org.junit.Test;
import static org.junit.Assert.*;

public class ClipTest {

    @Test
    public void testApply_withGrayImage_ReturnsImageWithCorrectRange() {
        // Typical use case
        GrayImage image = new GrayImage(5, 5);
        // Setting some pixel values outside the range
        image.set(0, 0, 10);
        image.set(1, 1, -5);
        image.set(2, 2, 100);
        
        Clip clip = new Clip(0, 255);
        
        Image result = clip.apply(image);
        
        // Check if pixel values are clipped to the specified range
        assertEquals(0, result.get(0, 0));
        assertEquals(0, result.get(1, 1));
        assertEquals(100, result.get(2, 2));
    }

    @Test
    public void testApply_withGrayImageAndROI_ReturnsImageWithCorrectRange() {
        // Typical use case
        GrayImage image = new GrayImage(5, 5);
        // Setting some pixel values outside the range
        image.set(0, 0, 10);
        image.set(1, 1, -5);
        image.set(2, 2, 100);
        
        Clip clip = new Clip(0, 255);
        
        ROI roi = new ROI(1, 1, 3, 3);
        
        Image result = clip.apply(image, roi);
        
        // Check if pixel values within ROI are clipped to the specified range
        assertEquals(10, result.get(0, 0));
        assertEquals(0, result.get(1, 1));
        assertEquals(100, result.get(2, 2));
    }

    @Test
    public void testApply_withRealGrayImage_ReturnsImageWithCorrectRange() {
        // Typical use case
        RealGrayImage image = new RealGrayImage(5, 5);
        // Setting some pixel values outside the range
        image.set(0, 0, 0.5f);
        image.set(1, 1, -0.5f);
        image.set(2, 2, 1.5f);
        
        Clip clip = new Clip(0.0f, 1.0f);
        
        Image result = clip.apply(image);
        
        // Check if pixel values are clipped to the specified range
        assertEquals(0.0f, result.get(0, 0));
        assertEquals(0.0f, result.get(1, 1));
        assertEquals(1.0f, result.get(2, 2));
    }

    @Test
    public void testApply_withRealGrayImageAndROI_ReturnsImageWithCorrectRange() {
        // Typical use case
        RealGrayImage image = new RealGrayImage(5, 5);
        // Setting some pixel values outside the range
        image.set(0, 0, 0.5f);
        image.set(1, 1, -0.5f);
        image.set(2, 2, 1.5f);
        
        Clip clip = new Clip(0.0f, 1.0f);
        
        ROI roi = new ROI(1, 1, 3, 3);
        
        Image result = clip.apply(image, roi);
        
        // Check if pixel values within ROI are clipped to the specified range
        assertEquals(0.5f, result.get(0, 0));
        assertEquals(0.0f, result.get(1, 1));
        assertEquals(1.0f, result.get(2, 2));
    }

    @Test
    public void testApply_withColorImage_ReturnsImageWithCorrectRange() {
        // Typical use case
        ColorImage image = new ColorImage(5, 5);
        // Setting some pixel values outside the range
        image.set(0, 0, 10, 10, 10);
        image.set(1, 1, -5, -5, -5);
        image.set(2, 2, 100, 100, 100);
        
        Clip clip = new Clip(0, 255);
        
        Image result = clip.apply(image);
        
        // Check if pixel values in each color plane are clipped to the specified range
        assertEquals(0, result.get(0, 0, 0));
        assertEquals(0, result.get(0, 0, 1));
        assertEquals(0, result.get(0, 0, 2));
        assertEquals(0, result.get(1, 1, 0));
        assertEquals(0, result.get(1, 1, 1));
        assertEquals(0, result.get(1, 1, 2));
        assertEquals(100, result.get(2, 2, 0));
        assertEquals(100, result.get(2, 2, 1));
        assertEquals(100, result.get(2, 2, 2));
    }

    @Test
    public void testApply_withColorImageAndROI_ReturnsImageWithCorrectRange() {
        // Typical use case
        ColorImage image = new ColorImage(5, 5);
        // Setting some pixel values outside the range
        image.set(0, 0, 10, 10, 10);
        image.set(1, 1, -5, -5, -5);
        image.set(2, 2, 100, 100, 100);
        
        Clip clip = new Clip(0, 255);
        
        ROI roi = new ROI(1, 1, 3, 3);
        
        Image result = clip.apply(image, roi);
        
        // Check if pixel values within ROI in each color plane are clipped to the specified range
        assertEquals(10, result.get(0, 0, 0));
        assertEquals(10, result.get(0, 0, 1));
        assertEquals(10, result.get(0, 0, 2));
        assertEquals(0, result.get(1, 1, 0));
        assertEquals(0, result.get(1, 1, 1));
        assertEquals(0, result.get(1, 1, 2));
        assertEquals(100, result.get(2, 2, 0));
        assertEquals(100, result.get(2, 2, 1));
        assertEquals(100, result.get(2, 2, 2));
    }

    @Test
    public void testApply_withRealColorImage_ReturnsImageWithCorrectRange() {
        // Typical use case
        RealColorImage image = new RealColorImage(5, 5);
        // Setting some pixel values outside the range
        image.set(0, 0, 0.5f, 0.5f, 0.5f);
        image.set(1, 1, -0.5f, -0.5f, -0.5f);
        image.set(2, 2, 1.5f, 1.5f, 1.5f);
        
        Clip clip = new Clip(0.0f, 1.0f);
        
        Image result = clip.apply(image);
        
        // Check if pixel values in each color plane are clipped to the specified range
        assertEquals(0.0f, result.get(0, 0, 0));
        assertEquals(0.0f, result.get(0, 0, 1));
        assertEquals(0.0f, result.get(0, 0, 2));
        assertEquals(0.0f, result.get(1, 1, 0));
        assertEquals(0.0f, result.get(1, 1, 1));
        assertEquals(0.0f, result.get(1, 1, 2));
        assertEquals(1.0f, result.get(2, 2, 0));
        assertEquals(1.0f, result.get(2, 2, 1));
        assertEquals(1.0f, result.get(2, 2, 2));
    }

    @Test
    public void testApply_withRealColorImageAndROI_ReturnsImageWithCorrectRange() {
        // Typical use case
        RealColorImage image = new RealColorImage(5, 5);
        // Setting some pixel values outside the range
        image.set(0, 0, 0.5f, 0.5f, 0.5f);
        image.set(1, 1, -0.5f, -0.5f, -0.5f);
        image.set(2, 2, 1.5f, 1.5f, 1.5f);
        
        Clip clip = new Clip(0.0f, 1.0f);
        
        ROI roi = new ROI(1, 1, 3, 3);
        
        Image result = clip.apply(image, roi);
        
        // Check if pixel values within ROI in each color plane are clipped to the specified range
        assertEquals(0.5f, result.get(0, 0, 0));
        assertEquals(0.5f, result.get(0, 0, 1));
        assertEquals(0.5f, result.get(0, 0, 2));
        assertEquals(0.0f, result.get(1, 1, 0));
        assertEquals(0.0f, result.get(1, 1, 1));
        assertEquals(0.0f, result.get(1, 1, 2));
        assertEquals(1.0f, result.get(2, 2, 0));
        assertEquals(1.0f, result.get(2, 2, 1));
        assertEquals(1.0f, result.get(2, 2, 2));
    }
}