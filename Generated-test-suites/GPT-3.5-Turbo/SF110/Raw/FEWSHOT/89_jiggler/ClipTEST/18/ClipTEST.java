
package jigl.image.ops.levelOps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.ColorImage;
import jigl.image.RealColorImage;
import jigl.image.Image;
import jigl.image.ROI;

public class ClipTest {
  
    private Clip clip;
  
    @Test
    public void testApply_GrayImage() {
        GrayImage image = new GrayImage(5, 5);
        ROI roi = new ROI(0, 0, 4, 4);
        
        clip = new Clip(0, 255);
        Image result = clip.apply(image);
        
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                assertEquals(image.get(x, y), result.get(x, y));
            }
        }
    }
  
    @Test
    public void testApply_GrayImageWithROI() {
        GrayImage image = new GrayImage(5, 5);
        ROI roi = new ROI(1, 1, 3, 3);
        
        clip = new Clip(0, 255);
        Image result = clip.apply(image, roi);
        
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                assertEquals(image.get(x, y), result.get(x, y));
            }
        }
    }
  
    @Test
    public void testApply_RealGrayImage() {
        RealGrayImage image = new RealGrayImage(5, 5);
        ROI roi = new ROI(0, 0, 4, 4);
        
        clip = new Clip(0.0f, 1.0f);
        Image result = clip.apply(image);
        
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                assertEquals(image.get(x, y), result.get(x, y));
            }
        }
    }
  
    @Test
    public void testApply_RealGrayImageWithROI() {
        RealGrayImage image = new RealGrayImage(5, 5);
        ROI roi = new ROI(1, 1, 3, 3);
        
        clip = new Clip(0.0f, 1.0f);
        Image result = clip.apply(image, roi);
        
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                assertEquals(image.get(x, y), result.get(x, y));
            }
        }
    }
  
    @Test
    public void testApply_ColorImage() {
        ColorImage image = new ColorImage(5, 5);
        ROI roi = new ROI(0, 0, 4, 4);
        
        clip = new Clip(0, 255);
        Image result = clip.apply(image);
        
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                assertEquals(image.get(x, y), result.get(x, y));
            }
        }
    }
  
    @Test
    public void testApply_RealColorImageWithROI() {
        RealColorImage image = new RealColorImage(5, 5);
        ROI roi = new ROI(0, 0, 4, 4);
        
        clip = new Clip(0.0f, 1.0f);
        Image result = clip.apply(image, roi);
        
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                assertEquals(image.get(x, y), result.get(x, y));
            }
        }
    }
}
