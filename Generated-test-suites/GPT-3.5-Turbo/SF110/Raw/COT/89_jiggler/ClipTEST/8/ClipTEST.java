
import static org.junit.Assert.*;
import org.junit.Test;

public class ClipTest {
    
    @Test
    public void testApply_GrayImage() {
        GrayImage image = new GrayImage();
        Clip clip = new Clip(0, 255);
        
        Image result = clip.apply(image);
        
        // Assert the result
        assertEquals(image, result);
    }
    
    @Test
    public void testApply_GrayImage_ROI() {
        GrayImage image = new GrayImage();
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        Clip clip = new Clip(0, 255);
        
        Image result = clip.apply(image, roi);
        
        // Assert the result
        assertEquals(image, result);
    }
    
    @Test
    public void testApply_RealGrayImage() {
        RealGrayImage image = new RealGrayImage();
        Clip clip = new Clip(0.0f, 255.0f);
        
        Image result = clip.apply(image);
        
        // Assert the result
        assertEquals(image, result);
    }
    
    @Test
    public void testApply_RealGrayImage_ROI() {
        RealGrayImage image = new RealGrayImage();
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        Clip clip = new Clip(0.0f, 255.0f);
        
        Image result = clip.apply(image, roi);
        
        // Assert the result
        assertEquals(image, result);
    }
    
    @Test
    public void testApply_ColorImage() {
        ColorImage image = new ColorImage();
        Clip clip = new Clip(0, 255);
        
        Image result = clip.apply(image);
        
        // Assert the result
        assertEquals(image, result);
    }
    
    @Test
    public void testApply_ColorImage_ROI() {
        ColorImage image = new ColorImage();
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        Clip clip = new Clip(0, 255);
        
        Image result = clip.apply(image, roi);
        
        // Assert the result
        assertEquals(image, result);
    }
    
    @Test
    public void testApply_RealColorImage() {
        RealColorImage image = new RealColorImage();
        Clip clip = new Clip(0.0f, 255.0f);
        
        Image result = clip.apply(image);
        
        // Assert the result
        assertEquals(image, result);
    }
    
    @Test
    public void testApply_RealColorImage_ROI() {
        RealColorImage image = new RealColorImage();
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        Clip clip = new Clip(0.0f, 255.0f);
        
        Image result = clip.apply(image, roi);
        
        // Assert the result
        assertEquals(image, result);
    }
    
}
