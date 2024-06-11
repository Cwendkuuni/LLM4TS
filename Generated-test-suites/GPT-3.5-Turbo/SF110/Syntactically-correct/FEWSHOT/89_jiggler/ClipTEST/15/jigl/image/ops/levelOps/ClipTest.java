package jigl.image.ops.levelOps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import jigl.image.ColorImage;
import jigl.image.GrayImage;
import jigl.image.RealColorImage;
import jigl.image.RealGrayImage;
import jigl.image.types.Image;
import jigl.image.types.ROI;

public class ClipTest {
    @Test
    public void testConstructor1() {
        Clip clip = new Clip(0, 255);
        
        assertEquals(0, clip.int_min);
        assertEquals(255, clip.int_max);
        assertEquals(0.0f, clip.float_min, 0.0001);
        assertEquals(0.0f, clip.float_max, 0.0001);
    }
    
    @Test
    public void testConstructor2() {
        Clip clip = new Clip(0.0f, 1.0f);
        
        assertEquals(0, clip.int_min);
        assertEquals(0, clip.int_max);
        assertEquals(0.0f, clip.float_min, 0.0001);
        assertEquals(1.0f, clip.float_max, 0.0001);
    }
    
    @Test
    public void testApplyGrayImage() {
        Clip clip = new Clip(0, 255);
        GrayImage image = new GrayImage(10, 10);
        
        Image result = clip.apply(image);
        
        assertEquals(result.getWidth(), image.getWidth());
        assertEquals(result.getHeight(), image.getHeight());
        assertNotEquals(result, image);
    }
    
    @Test
    public void testApplyGrayImageRoi() {
        Clip clip = new Clip(0, 255);
        GrayImage image = new GrayImage(10, 10);
        ROI roi = new ROI(0, 0, 5, 5);
        
        Image result = clip.apply(image, roi);
        
        assertEquals(result.getWidth(), roi.getWidth());
        assertEquals(result.getHeight(), roi.getHeight());
        assertNotEquals(result, image);
    }
    
    @Test
    public void testApplyRealGrayImage() {
        Clip clip = new Clip(0.0f, 1.0f);
        RealGrayImage image = new RealGrayImage(10, 10);
        
        Image result = clip.apply(image);
        
        assertEquals(result.getWidth(), image.getWidth());
        assertEquals(result.getHeight(), image.getHeight());
        assertNotEquals(result, image);
    }
    
    @Test
    public void testApplyRealGrayImageRoi() {
        Clip clip = new Clip(0.0f, 1.0f);
        RealGrayImage image = new RealGrayImage(10, 10);
        ROI roi = new ROI(0, 0, 5, 5);
        
        Image result = clip.apply(image, roi);
        
        assertEquals(result.getWidth(), roi.getWidth());
        assertEquals(result.getHeight(), roi.getHeight());
        assertNotEquals(result, image);
    }
    
    @Test
    public void testApplyColorImage() {
        Clip clip = new Clip(0, 255);
        ColorImage image = new ColorImage(10, 10);
        
        Image result = clip.apply(image);
        
        assertEquals(result.getWidth(), image.getWidth());
        assertEquals(result.getHeight(), image.getHeight());
        assertNotEquals(result, image);
    }
    
    @Test
    public void testApplyColorImageRoi() {
        Clip clip = new Clip(0, 255);
        ColorImage image = new ColorImage(10, 10);
        ROI roi = new ROI(0, 0, 5, 5);
        
        Image result = clip.apply(image, roi);
        
        assertEquals(result.getWidth(), roi.getWidth());
        assertEquals(result.getHeight(), roi.getHeight());
        assertNotEquals(result, image);
    }
    
    @Test
    public void testApplyRealColorImage() {
        Clip clip = new Clip(0.0f, 1.0f);
        RealColorImage image = new RealColorImage(10, 10);
        
        Image result = clip.apply(image);
        
        assertEquals(result.getWidth(), image.getWidth());
        assertEquals(result.getHeight(), image.getHeight());
        assertNotEquals(result, image);
    }
    
    @Test
    public void testApplyRealColorImageRoi() {
        Clip clip = new Clip(0.0f, 1.0f);
        RealColorImage image = new RealColorImage(10, 10);
        ROI roi = new ROI(0, 0, 5, 5);
        
        Image result = clip.apply(image, roi);
        
        assertEquals(result.getWidth(), roi.getWidth());
        assertEquals(result.getHeight(), roi.getHeight());
        assertNotEquals(result, image);
    }
}