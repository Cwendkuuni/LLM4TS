import static org.junit.Assert.*;
import jigl.image.*;
import jigl.image.ops.levelOps.*;
import org.junit.Test;

public class ClipTest {
    @Test
    public void testApply_GrayImage() {
        Clip clip = new Clip(10, 100);
        GrayImage image = new GrayImage(10, 10);
        image.initImage(50);
        
        Image result = clip.apply(image);
        
        for (int i = 0; i < result.X(); i++) {
            for (int j = 0; j < result.Y(); j++) {
                int value = result.get(i, j);
                assertTrue(value >= 10 && value <= 100);
            }
        }
    }
    
    @Test
    public void testApply_GrayImage_ROI() {
        Clip clip = new Clip(10, 100);
        GrayImage image = new GrayImage(10, 10);
        image.initImage(50);
        ROI roi = new ROI(2, 2, 6, 6);
        
        Image result = clip.apply(image, roi);
        
        for (int i = roi.ux(); i <= roi.lx(); i++) {
            for (int j = roi.uy(); j <= roi.ly(); j++) {
                int value = result.get(i, j);
                assertTrue(value >= 10 && value <= 100);
            }
        }
        
        for (int i = 0; i < result.X(); i++) {
            for (int j = 0; j < result.Y(); j++) {
                if (i >= roi.ux() && i <= roi.lx() && j >= roi.uy() && j <= roi.ly()) {
                    continue;
                }
                int value = result.get(i, j);
                assertEquals(50, value);
            }
        }
    }
    
    @Test
    public void testApply_RealGrayImage() {
        Clip clip = new Clip(0.1f, 0.9f);
        RealGrayImage image = new RealGrayImage(10, 10);
        image.initImage(0.5f);
        
        Image result = clip.apply(image);
        
        for (int i = 0; i < result.X(); i++) {
            for (int j = 0; j < result.Y(); j++) {
                float value = result.get(i, j);
                assertTrue(value >= 0.1f && value <= 0.9f);
            }
        }
    }
    
    @Test
    public void testApply_RealGrayImage_ROI() {
        Clip clip = new Clip(0.1f, 0.9f);
        RealGrayImage image = new RealGrayImage(10, 10);
        image.initImage(0.5f);
        ROI roi = new ROI(2, 2, 6, 6);
        
        Image result = clip.apply(image, roi);
        
        for (int i = roi.ux(); i <= roi.lx(); i++) {
            for (int j = roi.uy(); j <= roi.ly(); j++) {
                float value = result.get(i, j);
                assertTrue(value >= 0.1f && value <= 0.9f);
            }
        }
        
        for (int i = 0; i < result.X(); i++) {
            for (int j = 0; j < result.Y(); j++) {
                if (i >= roi.ux() && i <= roi.lx() && j >= roi.uy() && j <= roi.ly()) {
                    continue;
                }
                float value = result.get(i, j);
                assertEquals(0.5f, value, 0.01f);
            }
        }
    }
    
    @Test
    public void testApply_ColorImage() {
        Clip clip = new Clip(10, 100);
        ColorImage image = new ColorImage(10, 10);
        image.initImage(50);
        
        Image result = clip.apply(image);
        
        for (int i = 0; i < result.X(); i++) {
            for (int j = 0; j < result.Y(); j++) {
                int value = result.get(i, j);
                assertTrue(value >= 10 && value <= 100);
            }
        }
    }
    
    @Test
    public void testApply_ColorImage_ROI() {
        Clip clip = new Clip(10, 100);
        ColorImage image = new ColorImage(10, 10);
        image.initImage(50);
        ROI roi = new ROI(2, 2, 6, 6);
        
        Image result = clip.apply(image, roi);
        
        for (int i = roi.ux(); i <= roi.lx(); i++) {
            for (int j = roi.uy(); j <= roi.ly(); j++) {
                int value = result.get(i, j);
                assertTrue(value >= 10 && value <= 100);
            }
        }
        
        for (int i = 0; i < result.X(); i++) {
            for (int j = 0; j < result.Y(); j++) {
                if (i >= roi.ux() && i <= roi.lx() && j >= roi.uy() && j <= roi.ly()) {
                    continue;
                }
                int value = result.get(i, j);
                assertEquals(50, value);
            }
        }
    }
    
    @Test
    public void testApply_RealColorImage() {
        Clip clip = new Clip(0.1f, 0.9f);
        RealColorImage image = new RealColorImage(10, 10);
        image.initImage(0.5f);
        
        Image result = clip.apply(image);
        
        for (int i = 0; i < result.X(); i++) {
            for (int j = 0; j < result.Y(); j++) {
                float value = result.get(i, j);
                assertTrue(value >= 0.1f && value <= 0.9f);
            }
        }
    }
    
    @Test
    public void testApply_RealColorImage_ROI() {
        Clip clip = new Clip(0.1f, 0.9f);
        RealColorImage image = new RealColorImage(10, 10);
        image.initImage(0.5f);
        ROI roi = new ROI(2, 2, 6, 6);
        
        Image result = clip.apply(image, roi);
        
        for (int i = roi.ux(); i <= roi.lx(); i++) {
            for (int j = roi.uy(); j <= roi.ly(); j++) {
                float value = result.get(i, j);
                assertTrue(value >= 0.1f && value <= 0.9f);
            }
        }
        
        for (int i = 0; i < result.X(); i++) {
            for (int j = 0; j < result.Y(); j++) {
                if (i >= roi.ux() && i <= roi.lx() && j >= roi.uy() && j <= roi.ly()) {
                    continue;
                }
                float value = result.get(i, j);
                assertEquals(0.5f, value, 0.01f);
            }
        }
    }
}