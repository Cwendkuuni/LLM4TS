
package jigl.image.ops.levelOps;

import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ClipTest {

    @Test
    public void testApply_GrayImage() {
        GrayImage image = new GrayImage();
        Clip clip = new Clip(0, 255);
        
        Image result = clip.apply(image);
        
        assertNotNull(result);
        assertTrue(result instanceof GrayImage);
    }

    @Test
    public void testApply_GrayImageWithROI() {
        GrayImage image = new GrayImage();
        ROI roi = new ROI(0, 0, 100, 100);
        Clip clip = new Clip(0, 255);
        
        Image result = clip.apply(image, roi);
        
        assertNotNull(result);
        assertTrue(result instanceof GrayImage);
    }

    @Test
    public void testApply_RealGrayImage() {
        RealGrayImage image = new RealGrayImage();
        Clip clip = new Clip(0, 255);
        
        Image result = clip.apply(image);
        
        assertNotNull(result);
        assertTrue(result instanceof RealGrayImage);
    }

    @Test
    public void testApply_RealGrayImageWithROI() {
        RealGrayImage image = new RealGrayImage();
        ROI roi = new ROI(0, 0, 100, 100);
        Clip clip = new Clip(0, 255);
        
        Image result = clip.apply(image, roi);
        
        assertNotNull(result);
        assertTrue(result instanceof RealGrayImage);
    }

    @Test
    public void testApply_ColorImage() {
        ColorImage image = new ColorImage();
        Clip clip = new Clip(0, 255);
        
        Image result = clip.apply(image);
        
        assertNotNull(result);
        assertTrue(result instanceof ColorImage);
    }

    @Test
    public void testApply_ColorImageWithROI() {
        ColorImage image = new ColorImage();
        ROI roi = new ROI(0, 0, 100, 100);
        Clip clip = new Clip(0, 255);
        
        Image result = clip.apply(image, roi);
        
        assertNotNull(result);
        assertTrue(result instanceof ColorImage);
    }

    @Test
    public void testApply_RealColorImage() {
        RealColorImage image = new RealColorImage();
        Clip clip = new Clip(0, 255);
        
        Image result = clip.apply(image);
        
        assertNotNull(result);
        assertTrue(result instanceof RealColorImage);
    }

    @Test
    public void testApply_RealColorImageWithROI() {
        RealColorImage image = new RealColorImage();
        ROI roi = new ROI(0, 0, 100, 100);
        Clip clip = new Clip(0, 255);
        
        Image result = clip.apply(image, roi);
        
        assertNotNull(result);
        assertTrue(result instanceof RealColorImage);
    }
    
    // Additional test cases for potential edge cases and exceptions
    
    @Test
    public void testApply_GrayImageWithNegativeMinMax() {
        GrayImage image = new GrayImage();
        Clip clip = new Clip(-255, -1);
        
        Image result = clip.apply(image);
        
        assertNotNull(result);
        assertTrue(result instanceof GrayImage);
    }

    @Test
    public void testApply_GrayImageWithFloatMinMax() {
        GrayImage image = new GrayImage();
        Clip clip = new Clip(-255.5f, 255.5f);
        
        Image result = clip.apply(image);
        
        assertNotNull(result);
        assertTrue(result instanceof GrayImage);
    }
    
    @Test
    public void testApply_GrayImageOutsideROI() {
        GrayImage image = new GrayImage();
        ROI roi = new ROI(100, 100, 200, 200);
        Clip clip = new Clip(0, 255);
        
        Image result = clip.apply(image, roi);
        
        assertNotNull(result);
        assertTrue(result instanceof GrayImage);
    }

}
