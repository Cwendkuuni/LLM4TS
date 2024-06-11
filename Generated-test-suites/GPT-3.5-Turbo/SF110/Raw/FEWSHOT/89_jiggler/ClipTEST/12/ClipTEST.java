package jigl.image.ops.levelOps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.ColorImage;
import jigl.image.RealColorImage;
import jigl.image.ROI;

public class ClipTest {
    
    @Test
    public void testClip_minMaxConstructor_int() {
        Clip clip = new Clip(0, 255);
        assertNotNull(clip);
    }
    
    @Test
    public void testClip_minMaxConstructor_float() {
        Clip clip = new Clip(0.0f, 1.0f);
        assertNotNull(clip);
    }
    
    @Test
    public void testApply_grayImage_noROI() {
        Clip clip = new Clip(0, 255);
        GrayImage image = new GrayImage(10, 10);
        GrayImage result = (GrayImage) clip.apply(image);
        assertNotNull(result);
    }
    
    @Test
    public void testApply_grayImage_withROI() {
        Clip clip = new Clip(0, 255);
        GrayImage image = new GrayImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        GrayImage result = (GrayImage) clip.apply(image, roi);
        assertNotNull(result);
    }
    
    @Test
    public void testApply_realGrayImage_noROI() {
        Clip clip = new Clip(0.0f, 1.0f);
        RealGrayImage image = new RealGrayImage(10, 10);
        RealGrayImage result = (RealGrayImage) clip.apply(image);
        assertNotNull(result);
    }
    
    @Test
    public void testApply_realGrayImage_withROI() {
        Clip clip = new Clip(0.0f, 1.0f);
        RealGrayImage image = new RealGrayImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        RealGrayImage result = (RealGrayImage) clip.apply(image, roi);
        assertNotNull(result);
    }
    
    @Test
    public void testApply_colorImage_noROI() {
        Clip clip = new Clip(0, 255);
        ColorImage image = new ColorImage(10, 10);
        ColorImage result = (ColorImage) clip.apply(image);
        assertNotNull(result);
    }
    
    @Test
    public void testApply_colorImage_withROI() {
        Clip clip = new Clip(0, 255);
        ColorImage image = new ColorImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        ColorImage result = (ColorImage) clip.apply(image, roi);
        assertNotNull(result);
    }
    
    @Test
    public void testApply_realColorImage_noROI() {
        Clip clip = new Clip(0.0f, 1.0f);
        RealColorImage image = new RealColorImage(10, 10);
        RealColorImage result = (RealColorImage) clip.apply(image);
        assertNotNull(result);
    }
    
    @Test
    public void testApply_realColorImage_withROI() {
        Clip clip = new Clip(0.0f, 1.0f);
        RealColorImage image = new RealColorImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        RealColorImage result = (RealColorImage) clip.apply(image, roi);
        assertNotNull(result);
    }
    
    @Test
    public void testMain() {
        Clip clip = new Clip(0.0f, 1.0f);
        String[] argv = {"0.0", "1.0", "inputfile.jpg", "outputfile.jpg"};
        clip.main(argv);
        // Add assertion for main method if needed
    }
}