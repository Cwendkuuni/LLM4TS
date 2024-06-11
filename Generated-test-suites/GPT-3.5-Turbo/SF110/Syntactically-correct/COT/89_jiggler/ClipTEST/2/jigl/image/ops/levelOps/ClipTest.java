
import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.*;

public class ClipTest {
    @Test
    public void testApply_GrayImage() {
        Clip clip = new Clip(0, 255);
        GrayImage image = new GrayImage(10, 10);
        Image result = clip.apply(image);
        assertNotNull(result);
    }
    
    @Test
    public void testApply_GrayImageROI() {
        Clip clip = new Clip(0, 255);
        GrayImage image = new GrayImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        Image result = clip.apply(image, roi);
        assertNotNull(result);
    }
    
    @Test
    public void testApply_RealGrayImage() {
        Clip clip = new Clip(0, 255);
        RealGrayImage image = new RealGrayImage(10, 10);
        Image result = clip.apply(image);
        assertNotNull(result);
    }
    
    @Test
    public void testApply_RealGrayImageROI() {
        Clip clip = new Clip(0, 255);
        RealGrayImage image = new RealGrayImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        Image result = clip.apply(image, roi);
        assertNotNull(result);
    }
    
    @Test
    public void testApply_ColorImage() {
        Clip clip = new Clip(0, 255);
        ColorImage image = new ColorImage(10, 10);
        Image result = clip.apply(image);
        assertNotNull(result);
    }
    
    @Test
    public void testApply_ColorImageROI() {
        Clip clip = new Clip(0, 255);
        ColorImage image = new ColorImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        Image result = clip.apply(image, roi);
        assertNotNull(result);
    }
    
    @Test
    public void testApply_RealColorImage() {
        Clip clip = new Clip(0, 255);
        RealColorImage image = new RealColorImage(10, 10);
        Image result = clip.apply(image);
        assertNotNull(result);
    }
    
    @Test
    public void testApply_RealColorImageROI() {
        Clip clip = new Clip(0, 255);
        RealColorImage image = new RealColorImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        Image result = clip.apply(image, roi);
        assertNotNull(result);
    }
}
