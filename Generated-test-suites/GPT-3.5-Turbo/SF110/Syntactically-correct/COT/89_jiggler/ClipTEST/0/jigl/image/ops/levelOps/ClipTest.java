
package jigl.image.ops.levelOps;

import static org.junit.Assert.*;
import org.junit.Test;

public class ClipTest {
    @Test
    public void testApply_grayImage() {
        Clip clip = new Clip(0, 255);
        GrayImage image = new GrayImage(10, 10);
        GrayImage result = clip.apply(image);
        assertEquals(image, result);
    }
    
    @Test
    public void testApply_grayImage_roi() {
        Clip clip = new Clip(0, 255);
        GrayImage image = new GrayImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        GrayImage result = clip.apply(image, roi);
        assertEquals(image, result);
    }
    
    @Test
    public void testApply_realGrayImage() {
        Clip clip = new Clip(0.0f, 1.0f);
        RealGrayImage image = new RealGrayImage(10, 10);
        RealGrayImage result = clip.apply(image);
        assertEquals(image, result);
    }
    
    @Test
    public void testApply_realGrayImage_roi() {
        Clip clip = new Clip(0.0f, 1.0f);
        RealGrayImage image = new RealGrayImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        RealGrayImage result = clip.apply(image, roi);
        assertEquals(image, result);
    }
    
    @Test
    public void testApply_colorImage() {
        Clip clip = new Clip(0, 255);
        ColorImage image = new ColorImage(10, 10);
        ColorImage result = clip.apply(image);
        assertEquals(image, result);
    }
    
    @Test
    public void testApply_colorImage_roi() {
        Clip clip = new Clip(0, 255);
        ColorImage image = new ColorImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        ColorImage result = clip.apply(image, roi);
        assertEquals(image, result);
    }
    
    @Test
    public void testApply_realColorImage() {
        Clip clip = new Clip(0.0f, 1.0f);
        RealColorImage image = new RealColorImage(10, 10);
        RealColorImage result = clip.apply(image);
        assertEquals(image, result);
    }
    
    @Test
    public void testApply_realColorImage_roi() {
        Clip clip = new Clip(0.0f, 1.0f);
        RealColorImage image = new RealColorImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        RealColorImage result = clip.apply(image, roi);
        assertEquals(image, result);
    }
}