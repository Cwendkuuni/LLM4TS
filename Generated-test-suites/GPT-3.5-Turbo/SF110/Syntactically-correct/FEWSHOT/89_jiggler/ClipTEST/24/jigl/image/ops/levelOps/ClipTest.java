import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ClipTest {
    
    @Test
    public void testApplyGrayImage() {
        GrayImage image = new GrayImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        
        Clip clip = new Clip(0, 255);
        Image result = clip.apply(image);
        
        assertEquals(image, result);
    }
    
    @Test
    public void testApplyGrayImageWithROI() {
        GrayImage image = new GrayImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        
        Clip clip = new Clip(0, 255);
        Image result = clip.apply(image, roi);
        
        assertEquals(image, result);
    }
    
    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage image = new RealGrayImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        
        Clip clip = new Clip(0.0f, 1.0f);
        Image result = clip.apply(image);
        
        assertEquals(image, result);
    }
    
    @Test
    public void testApplyRealGrayImageWithROI() {
        RealGrayImage image = new RealGrayImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        
        Clip clip = new Clip(0.0f, 1.0f);
        Image result = clip.apply(image, roi);
        
        assertEquals(image, result);
    }
    
    @Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        
        Clip clip = new Clip(0, 255);
        Image result = clip.apply(image);
        
        assertEquals(image, result);
    }
    
    @Test
    public void testApplyColorImageWithROI() {
        ColorImage image = new ColorImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        
        Clip clip = new Clip(0, 255);
        Image result = clip.apply(image, roi);
        
        assertEquals(image, result);
    }
    
    @Test
    public void testApplyRealColorImage() {
        RealColorImage image = new RealColorImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        
        Clip clip = new Clip(0.0f, 1.0f);
        Image result = clip.apply(image);
        
        assertEquals(image, result);
    }
    
    @Test
    public void testApplyRealColorImageWithROI() {
        RealColorImage image = new RealColorImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        
        Clip clip = new Clip(0.0f, 1.0f);
        Image result = clip.apply(image, roi);
        
        assertEquals(image, result);
    }
    
    @Test
    public void testMain() {
        String[] argv = {"0.0", "1.0", "input.jpg", "output.jpg"};
        
        Clip.main(argv);
    }
}