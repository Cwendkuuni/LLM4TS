package jigl.image.ops.levelOps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import jigl.image.ColorImage;
import jigl.image.GrayImage;
import jigl.image.RealColorImage;
import jigl.image.RealGrayImage;
import jigl.image.ROI;

public class ClipTest {
    private Clip clip;

    @Before
    public void setUp() {
        clip = new Clip(0, 255);
    }

    //Test apply(GrayImage) method
    @Test
    public void testApplyGrayImage() {
        GrayImage inputImage = new GrayImage(10, 10);
        GrayImage resultImage = (GrayImage)clip.apply(inputImage);
        
        for (int y = 0; y < resultImage.Y(); y++) {
            for (int x = 0; x < resultImage.X(); x++) {
                assertEquals(0, resultImage.get(x, y));
            }
        }
    }

    //Test apply(GrayImage, ROI) method
    @Test
    public void testApplyGrayImageWithROI() {
        GrayImage inputImage = new GrayImage(10, 10);
        ROI roi = new ROI(0, 0, 4, 4);
        GrayImage resultImage = (GrayImage)clip.apply(inputImage, roi);
        
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                assertEquals(0, resultImage.get(x, y));
            }
        }
    }
    
    //Test apply(RealGrayImage) method
    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage inputImage = new RealGrayImage(10, 10);
        RealGrayImage resultImage = (RealGrayImage)clip.apply(inputImage);
        
        for (int y = 0; y < resultImage.Y(); y++) {
            for (int x = 0; x < resultImage.X(); x++) {
                assertTrue(resultImage.get(x, y) >= 0.0f && resultImage.get(x, y) <= 255.0f);
            }
        }
    }

    //Test apply(RealGrayImage, ROI) method
    @Test
    public void testApplyRealGrayImageWithROI() {
        RealGrayImage inputImage = new RealGrayImage(10, 10);
        ROI roi = new ROI(0, 0, 4, 4);
        RealGrayImage resultImage = (RealGrayImage)clip.apply(inputImage, roi);
        
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                assertTrue(resultImage.get(x, y) >= 0.0f && resultImage.get(x, y) <= 255.0f);
            }
        }
    }
    
    //Test apply(ColorImage) method
    @Test
    public void testApplyColorImage() {
        ColorImage inputImage = new ColorImage(10, 10);
        ColorImage resultImage = (ColorImage)clip.apply(inputImage);
        
        for (int y = 0; y < resultImage.Y(); y++) {
            for (int x = 0; x < resultImage.X(); x++) {
                for (int c = 0; c < resultImage.channels(); c++) {
                    assertEquals(0, resultImage.getPlane(c).get(x, y));
                }
            }
        }
    }

    //Test apply(ColorImage, ROI) method
    @Test
    public void testApplyColorImageWithROI() {
        ColorImage inputImage = new ColorImage(10, 10);
        ROI roi = new ROI(0, 0, 4, 4);
        ColorImage resultImage = (ColorImage)clip.apply(inputImage, roi);
        
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                for (int c = 0; c < resultImage.channels(); c++) {
                    assertEquals(0, resultImage.getPlane(c).get(x, y));
                }
            }
        }
    }
    
    //Test apply(RealColorImage) method
    @Test
    public void testApplyRealColorImage() {
        RealColorImage inputImage = new RealColorImage(10, 10);
        RealColorImage resultImage = (RealColorImage)clip.apply(inputImage);
        
        for (int y = 0; y < resultImage.Y(); y++) {
            for (int x = 0; x < resultImage.X(); x++) {
                for (int c = 0; c < resultImage.channels(); c++) {
                    assertTrue(resultImage.getPlane(c).get(x, y) >= 0.0f && resultImage.getPlane(c).get(x, y) <= 255.0f);
                }
            }
        }
    }

    //Test apply(RealColorImage, ROI) method
    @Test
    public void testApplyRealColorImageWithROI() {
        RealColorImage inputImage = new RealColorImage(10, 10);
        ROI roi = new ROI(0, 0, 4, 4);
        RealColorImage resultImage = (RealColorImage)clip.apply(inputImage, roi);
        
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                for (int c = 0; c < resultImage.channels(); c++) {
                    assertTrue(resultImage.getPlane(c).get(x, y) >= 0.0f && resultImage.getPlane(c).get(x, y) <= 255.0f);
                }
            }
        }
    }
}