import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;
import jigl.image.ops.levelOps.*;

import org.junit.Test;

public class ClipTest {
    @Test
    public void testApplyGrayImage() {
        GrayImage image = new GrayImage(10, 10);
        image.randomize(0, 255);
        Clip clip = new Clip(50, 200);
        Image result = clip.apply(image);
        
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                int value = result.get(x, y);
                assertTrue(value >= 50 && value <= 200);
            }
        }
    }
    
    @Test
    public void testApplyGrayImageWithROI() {
        GrayImage image = new GrayImage(10, 10);
        image.randomize(0, 255);
        ROI roi = new ROI(2, 2, 7, 7);
        Clip clip = new Clip(50, 200);
        Image result = clip.apply(image, roi);
        
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                int value = result.get(x, y);
                assertTrue(value >= 50 && value <= 200);
            }
        }
    }
    
    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage image = new RealGrayImage(10, 10);
        image.randomize(0, 255);
        Clip clip = new Clip(50.0f, 200.0f);
        Image result = clip.apply(image);
        
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                float value = result.get(x, y);
                assertTrue(value >= 50.0f && value <= 200.0f);
            }
        }
    }
    
    @Test
    public void testApplyRealGrayImageWithROI() {
        RealGrayImage image = new RealGrayImage(10, 10);
        image.randomize(0, 255);
        ROI roi = new ROI(2, 2, 7, 7);
        Clip clip = new Clip(50.0f, 200.0f);
        Image result = clip.apply(image, roi);
        
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                float value = result.get(x, y);
                assertTrue(value >= 50.0f && value <= 200.0f);
            }
        }
    }
    
    @Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage(10, 10);
        image.randomize(0, 255);
        Clip clip = new Clip(50, 200);
        Image result = clip.apply(image);
        
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                int value = result.get(x, y, 0);
                assertTrue(value >= 50 && value <= 200);
            }
        }
    }
    
    @Test
    public void testApplyColorImageWithROI() {
        ColorImage image = new ColorImage(10, 10);
        image.randomize(0, 255);
        ROI roi = new ROI(2, 2, 7, 7);
        Clip clip = new Clip(50, 200);
        Image result = clip.apply(image, roi);
        
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                int value = result.get(x, y, 0);
                assertTrue(value >= 50 && value <= 200);
            }
        }
    }
    
    @Test
    public void testApplyRealColorImage() {
        RealColorImage image = new RealColorImage(10, 10);
        image.randomize(0, 255);
        Clip clip = new Clip(50.0f, 200.0f);
        Image result = clip.apply(image);
        
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                float value = result.get(x, y, 0);
                assertTrue(value >= 50.0f && value <= 200.0f);
            }
        }
    }
    
    @Test
    public void testApplyRealColorImageWithROI() {
        RealColorImage image = new RealColorImage(10, 10);
        image.randomize(0, 255);
        ROI roi = new ROI(2, 2, 7, 7);
        Clip clip = new Clip(50.0f, 200.0f);
        Image result = clip.apply(image, roi);
        
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                float value = result.get(x, y, 0);
                assertTrue(value >= 50.0f && value <= 200.0f);
            }
        }
    }
}