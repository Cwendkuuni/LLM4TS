
import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.*;
import jigl.image.ops.levelOps.*;
import jigl.image.io.*;

public class ClipTest {

    @Test
    public void testApply_GrayImage() {
        GrayImage image = new GrayImage(10, 10);
        image.randomize(0, 255);
        Clip clip = new Clip(0, 255);
        Image result = clip.apply(image);
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
        // Add more assertions
    }
    
    @Test
    public void testApply_GrayImageWithROI() {
        GrayImage image = new GrayImage(10, 10);
        image.randomize(0, 255);
        ROI roi = new ROI(1, 1, 8, 8);
        Clip clip = new Clip(0, 255);
        Image result = clip.apply(image, roi);
        assertEquals(roi.width(), result.X());
        assertEquals(roi.height(), result.Y());
        // Add more assertions
    }
    
    @Test
    public void testApply_RealGrayImage() {
        RealGrayImage image = new RealGrayImage(10, 10);
        image.randomize(0.0, 1.0);
        Clip clip = new Clip(0.0f, 1.0f);
        Image result = clip.apply(image);
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
        // Add more assertions
    }
    
    @Test
    public void testApply_RealGrayImageWithROI() {
        RealGrayImage image = new RealGrayImage(10, 10);
        image.randomize(0.0, 1.0);
        ROI roi = new ROI(1, 1, 8, 8);
        Clip clip = new Clip(0.0f, 1.0f);
        Image result = clip.apply(image, roi);
        assertEquals(roi.width(), result.X());
        assertEquals(roi.height(), result.Y());
        // Add more assertions
    }
    
    @Test
    public void testApply_ColorImage() {
        ColorImage image = new ColorImage(10, 10);
        image.randomize(0, 255);
        Clip clip = new Clip(0, 255);
        Image result = clip.apply(image);
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
        // Add more assertions
    }
    
    @Test
    public void testApply_ColorImageWithROI() {
        ColorImage image = new ColorImage(10, 10);
        image.randomize(0, 255);
        ROI roi = new ROI(1, 1, 8, 8);
        Clip clip = new Clip(0, 255);
        Image result = clip.apply(image, roi);
        assertEquals(roi.width(), result.X());
        assertEquals(roi.height(), result.Y());
        // Add more assertions
    }
    
    @Test
    public void testApply_RealColorImage() {
        RealColorImage image = new RealColorImage(10, 10);
        image.randomize(0.0, 1.0);
        Clip clip = new Clip(0.0f, 1.0f);
        Image result = clip.apply(image);
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
        // Add more assertions
    }
    
    @Test
    public void testApply_RealColorImageWithROI() {
        RealColorImage image = new RealColorImage(10, 10);
        image.randomize(0.0, 1.0);
        ROI roi = new ROI(1, 1, 8, 8);
        Clip clip = new Clip(0.0f, 1.0f);
        Image result = clip.apply(image, roi);
        assertEquals(roi.width(), result.X());
        assertEquals(roi.height(), result.Y());
        // Add more assertions
    }
}
