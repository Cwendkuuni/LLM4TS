import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.*;
import jigl.image.io.*;
import jigl.image.ops.levelOps.*;
import jigl.image.types.*;
import jigl.io.*;

public class ClipTest {
    
    // Test case for the first constructor
    @Test
    public void testConstructor1() {
        Clip clip = new Clip(0, 255);
        assertNotNull(clip);
    }
    
    // Test case for the second constructor
    @Test
    public void testConstructor2() {
        Clip clip = new Clip(0.0f, 1.0f);
        assertNotNull(clip);
    }
    
    // Test case for apply with GrayImage
    @Test
    public void testApplyGrayImage() {
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 10);
        image.set(1, 1, 20);
        image.set(2, 2, 30);
        
        Clip clip = new Clip(15, 25);
        Image result = clip.apply(image);
        
        assertEquals(10, result.get(0, 0));
        assertEquals(20, result.get(1, 1));
        assertEquals(25, result.get(2, 2));
    }
    
    // Test case for apply with GrayImage and ROI
    @Test
    public void testApplyGrayImageWithROI() {
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 10);
        image.set(1, 1, 20);
        image.set(2, 2, 30);
        
        ROI roi = new ROI(1, 1, 2, 2);
        
        Clip clip = new Clip(15, 25);
        Image result = clip.apply(image, roi);
        
        assertEquals(10, result.get(0, 0));
        assertEquals(15, result.get(1, 1));
        assertEquals(25, result.get(2, 2));
    }
    
    // Test case for apply with RealGrayImage
    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 0.5f);
        image.set(1, 1, 1.0f);
        image.set(2, 2, 1.5f);
        
        Clip clip = new Clip(0.8f, 1.2f);
        Image result = clip.apply(image);
        
        assertEquals(0.5f, result.get(0, 0));
        assertEquals(1.0f, result.get(1, 1));
        assertEquals(1.2f, result.get(2, 2));
    }
    
    // Test case for apply with RealGrayImage and ROI
    @Test
    public void testApplyRealGrayImageWithROI() {
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 0.5f);
        image.set(1, 1, 1.0f);
        image.set(2, 2, 1.5f);
        
        ROI roi = new ROI(1, 1, 2, 2);
        
        Clip clip = new Clip(0.8f, 1.2f);
        Image result = clip.apply(image, roi);
        
        assertEquals(0.5f, result.get(0, 0));
        assertEquals(0.8f, result.get(1, 1));
        assertEquals(1.2f, result.get(2, 2));
    }
    
    // Test case for apply with ColorImage
    @Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage(3, 3);
        image.set(0, 0, 0, 10);
        image.set(1, 1, 1, 20);
        image.set(2, 2, 2, 30);
        
        Clip clip = new Clip(15, 25);
        Image result = clip.apply(image);
        
        assertEquals(10, ((GrayImage)result.plane(0)).get(0, 0));
        assertEquals(20, ((GrayImage)result.plane(1)).get(1, 1));
        assertEquals(25, ((GrayImage)result.plane(2)).get(2, 2));
    }
    
    // Test case for apply with ColorImage and ROI
    @Test
    public void testApplyColorImageWithROI() {
        ColorImage image = new ColorImage(3, 3);
        image.set(0, 0, 0, 10);
        image.set(1, 1, 1, 20);
        image.set(2, 2, 2, 30);
        
        ROI roi = new ROI(1, 1, 2, 2);
        
        Clip clip = new Clip(15, 25);
        Image result = clip.apply(image, roi);
        
        assertEquals(10, ((GrayImage)result.plane(0)).get(0, 0));
        assertEquals(15, ((GrayImage)result.plane(1)).get(1, 1));
        assertEquals(25, ((GrayImage)result.plane(2)).get(2, 2));
    }
    
    // Test case for apply with RealColorImage
    @Test
    public void testApplyRealColorImage() {
        RealColorImage image = new RealColorImage(3, 3);
        image.set(0, 0, 0, 0.5f);
        image.set(1, 1, 1, 1.0f);
        image.set(2, 2, 2, 1.5f);
        
        Clip clip = new Clip(0.8f, 1.2f);
        Image result = clip.apply(image);
        
        assertEquals(0.5f, ((RealGrayImage)result.plane(0)).get(0, 0));
        assertEquals(1.0f, ((RealGrayImage)result.plane(1)).get(1, 1));
        assertEquals(1.2f, ((RealGrayImage)result.plane(2)).get(2, 2));
    }
    
    // Test case for apply with RealColorImage and ROI
    @Test
    public void testApplyRealColorImageWithROI() {
        RealColorImage image = new RealColorImage(3, 3);
        image.set(0, 0, 0, 0.5f);
        image.set(1, 1, 1, 1.0f);
        image.set(2, 2, 2, 1.5f);
        
        ROI roi = new ROI(1, 1, 2, 2);
        
        Clip clip = new Clip(0.8f, 1.2f);
        Image result = clip.apply(image, roi);
        
        assertEquals(0.5f, ((RealGrayImage)result.plane(0)).get(0, 0));
        assertEquals(0.8f, ((RealGrayImage)result.plane(1)).get(1, 1));
        assertEquals(1.2f, ((RealGrayImage)result.plane(2)).get(2, 2));
    }
    
}