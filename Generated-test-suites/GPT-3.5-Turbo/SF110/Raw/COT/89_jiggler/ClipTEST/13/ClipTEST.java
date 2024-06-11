
import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.*;
import jigl.image.ops.levelOps.Clip;

public class ClipTest {

    /**
     * Test the apply(GrayImage) method with default ROI
     */
    @Test
    public void testApplyGrayImageDefaultROI() {
        GrayImage image = new GrayImage(10, 10);
        image.fill(50);
        
        Clip clip = new Clip(0, 100);
        Image result = clip.apply(image);
        
        assertEquals(image, result);
    }
    
    /**
     * Test the apply(GrayImage, ROI) method
     */
    @Test
    public void testApplyGrayImageROI() {
        GrayImage image = new GrayImage(10, 10);
        image.fill(50);
        
        Clip clip = new Clip(0, 100);
        
        ROI roi = new ROI(0, 0, 5, 5);
        
        Image result = clip.apply(image, roi);
        
        Image expected = new GrayImage(10, 10);
        expected.fill(50);
        
        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                expected.set(x, y, 0);
            }
        }
        
        assertEquals(expected, result);
    }
    
    /**
     * Test the apply(RealGrayImage) method with default ROI
     */
    @Test
    public void testApplyRealGrayImageDefaultROI() {
        RealGrayImage image = new RealGrayImage(10, 10);
        image.fill(50);
        
        Clip clip = new Clip(0, 100);
        Image result = clip.apply(image);
        
        assertEquals(image, result);
    }
    
    /**
     * Test the apply(RealGrayImage, ROI) method
     */
    @Test
    public void testApplyRealGrayImageROI() {
        RealGrayImage image = new RealGrayImage(10, 10);
        image.fill(50);
        
        Clip clip = new Clip(0, 100);
        
        ROI roi = new ROI(0, 0, 5, 5);
        
        Image result = clip.apply(image, roi);
        
        Image expected = new RealGrayImage(10, 10);
        expected.fill(50);
        
        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                expected.set(x, y, 0);
            }
        }
        
        assertEquals(expected, result);
    }
    
    /**
     * Test the apply(ColorImage) method with default ROI
     */
    @Test
    public void testApplyColorImageDefaultROI() {
        ColorImage image = new ColorImage(10, 10);
        image.fill(50);
        
        Clip clip = new Clip(0, 100);
        Image result = clip.apply(image);
        
        assertEquals(image, result);
    }
    
    /**
     * Test the apply(ColorImage, ROI) method
     */
    @Test
    public void testApplyColorImageROI() {
        ColorImage image = new ColorImage(10, 10);
        image.fill(50);
        
        Clip clip = new Clip(0, 100);
        
        ROI roi = new ROI(0, 0, 5, 5);
        
        Image result = clip.apply(image, roi);
        
        Image expected = new ColorImage(10, 10);
        expected.fill(50);
        
        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                expected.set(x, y, 0);
            }
        }
        
        assertEquals(expected, result);
    }
    
    /**
     * Test the apply(RealColorImage) method with default ROI
     */
    @Test
    public void testApplyRealColorImageDefaultROI() {
        RealColorImage image = new RealColorImage(10, 10);
        image.fill(50);
        
        Clip clip = new Clip(0, 100);
        Image result = clip.apply(image);
        
        assertEquals(image, result);
    }
    
    /**
     * Test the apply(RealColorImage, ROI) method
     */
    @Test
    public void testApplyRealColorImageROI() {
        RealColorImage image = new RealColorImage(10, 10);
        image.fill(50);
        
        Clip clip = new Clip(0, 100);
        
        ROI roi = new ROI(0, 0, 5, 5);
        
        Image result = clip.apply(image, roi);
        
        Image expected = new RealColorImage(10, 10);
        expected.fill(50);
        
        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                expected.set(x, y, 0);
            }
        }
        
        assertEquals(expected, result);
    }
}