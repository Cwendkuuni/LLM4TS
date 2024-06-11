import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import jigl.image.ColorImage;
import jigl.image.GrayImage;
import jigl.image.RealColorImage;
import jigl.image.RealGrayImage;
import jigl.image.ROI;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.SimpleOperator;

public class ClipTest {

    private Clip clip;
    
    @Before
    public void setUp() {
        clip = new Clip(0, 255);
    }
    
    @Test
    public void testApply_GrayImage() {
        GrayImage image = new GrayImage(10, 10);
        image.fill(100);
        
        GrayImage result = (GrayImage)clip.apply(image);
        
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                assertEquals(100, result.get(x, y));
            }
        }
    }
    
    @Test
    public void testApply_GrayImageWithROI() {
        GrayImage image = new GrayImage(10, 10);
        image.fill(100);
        ROI roi = new ROI(2, 2, 6, 6);
        
        GrayImage result = (GrayImage)clip.apply(image, roi);
        
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                assertEquals(100, result.get(x, y));
            }
        }
    }
    
    @Test
    public void testApply_RealGrayImage() {
        RealGrayImage image = new RealGrayImage(10, 10);
        image.fill(100.0f);
        
        RealGrayImage result = (RealGrayImage)clip.apply(image);
        
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                assertEquals(100.0f, result.get(x, y), 0.01);
            }
        }
    }
    
    @Test
    public void testApply_RealGrayImageWithROI() {
        RealGrayImage image = new RealGrayImage(10, 10);
        image.fill(100.0f);
        ROI roi = new ROI(2, 2, 6, 6);
        
        RealGrayImage result = (RealGrayImage)clip.apply(image, roi);
        
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                assertEquals(100.0f, result.get(x, y), 0.01);
            }
        }
    }
    
    @Test
    public void testApply_ColorImage() {
        ColorImage image = new ColorImage(10, 10);
        image.plane(0).fill(100);
        image.plane(1).fill(100);
        image.plane(2).fill(100);
        
        ColorImage result = (ColorImage)clip.apply(image);
        
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                assertEquals(100, result.plane(0).get(x, y));
                assertEquals(100, result.plane(1).get(x, y));
                assertEquals(100, result.plane(2).get(x, y));
            }
        }
    }
    
    @Test
    public void testApply_ColorImageWithROI() {
        ColorImage image = new ColorImage(10, 10);
        image.plane(0).fill(100);
        image.plane(1).fill(100);
        image.plane(2).fill(100);
        ROI roi = new ROI(2, 2, 6, 6);
        
        ColorImage result = (ColorImage)clip.apply(image, roi);
        
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                assertEquals(100, result.plane(0).get(x, y));
                assertEquals(100, result.plane(1).get(x, y));
                assertEquals(100, result.plane(2).get(x, y));
            }
        }
    }
    
    @Test
    public void testApply_RealColorImage() {
        RealColorImage image = new RealColorImage(10, 10);
        image.plane(0).fill(100.0f);
        image.plane(1).fill(100.0f);
        image.plane(2).fill(100.0f);
        
        RealColorImage result = (RealColorImage)clip.apply(image);
        
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                assertEquals(100.0f, result.plane(0).get(x, y), 0.01);
                assertEquals(100.0f, result.plane(1).get(x, y), 0.01);
                assertEquals(100.0f, result.plane(2).get(x, y), 0.01);
            }
        }
    }
    
    @Test
    public void testApply_RealColorImageWithROI() {
        RealColorImage image = new RealColorImage(10, 10);
        image.plane(0).fill(100.0f);
        image.plane(1).fill(100.0f);
        image.plane(2).fill(100.0f);
        ROI roi = new ROI(2, 2, 6, 6);
        
        RealColorImage result = (RealColorImage)clip.apply(image, roi);
        
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                assertEquals(100.0f, result.plane(0).get(x, y), 0.01);
                assertEquals(100.0f, result.plane(1).get(x, y), 0.01);
                assertEquals(100.0f, result.plane(2).get(x, y), 0.01);
            }
        }
    }
}