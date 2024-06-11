package jigl.image.ops.levelOps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import jigl.image.ColorImage;
import jigl.image.GrayImage;
import jigl.image.RealColorImage;
import jigl.image.RealGrayImage;
import jigl.image.ROI;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

import org.junit.Test;

public class ClipTest {
    @Test
    public void testApplyGrayImageWithoutROI() {
        Clip clip = new Clip(0, 0);
        GrayImage inputImage = new GrayImage(/*image properties*/);
        GrayImage expectedImage = new GrayImage(/*expected result*/);
        
        GrayImage actualImage = (GrayImage) clip.apply(inputImage);
        
        assertEquals(expectedImage, actualImage);
    }
    
    @Test
    public void testApplyGrayImageWithROI() {
        Clip clip = new Clip(0, 0);
        GrayImage inputImage = new GrayImage(/*image properties*/);
        ROI roi = new ROI(/*ROI properties*/);
        GrayImage expectedImage = new GrayImage(/*expected result*/);
        
        GrayImage actualImage = (GrayImage) clip.apply(inputImage, roi);
        
        assertEquals(expectedImage, actualImage);
    }
    
    @Test
    public void testApplyRealGrayImageWithoutROI() {
        Clip clip = new Clip(0, 0);
        RealGrayImage inputImage = new RealGrayImage(/*image properties*/);
        RealGrayImage expectedImage = new RealGrayImage(/*expected result*/);
        
        RealGrayImage actualImage = (RealGrayImage) clip.apply(inputImage);
        
        assertEquals(expectedImage, actualImage);
    }
    
    @Test
    public void testApplyRealGrayImageWithROI() {
        Clip clip = new Clip(0, 0);
        RealGrayImage inputImage = new RealGrayImage(/*image properties*/);
        ROI roi = new ROI(/*ROI properties*/);
        RealGrayImage expectedImage = new RealGrayImage(/*expected result*/);
        
        RealGrayImage actualImage = (RealGrayImage) clip.apply(inputImage, roi);
        
        assertEquals(expectedImage, actualImage);
    }
    
    @Test
    public void testApplyColorImageWithoutROI() {
        Clip clip = new Clip(0, 0);
        ColorImage inputImage = new ColorImage(/*image properties*/);
        ColorImage expectedImage = new ColorImage(/*expected result*/);
        
        ColorImage actualImage = (ColorImage) clip.apply(inputImage);
        
        assertEquals(expectedImage, actualImage);
    }
    
    @Test
    public void testApplyColorImageWithROI() {
        Clip clip = new Clip(0, 0);
        ColorImage inputImage = new ColorImage(/*image properties*/);
        ROI roi = new ROI(/*ROI properties*/);
        ColorImage expectedImage = new ColorImage(/*expected result*/);
        
        ColorImage actualImage = (ColorImage) clip.apply(inputImage, roi);
        
        assertEquals(expectedImage, actualImage);
    }
    
    @Test
    public void testApplyRealColorImageWithoutROI() {
        Clip clip = new Clip(0, 0);
        RealColorImage inputImage = new RealColorImage(/*image properties*/);
        RealColorImage expectedImage = new RealColorImage(/*expected result*/);
        
        RealColorImage actualImage = (RealColorImage) clip.apply(inputImage);
        
        assertEquals(expectedImage, actualImage);
    }
    
    @Test
    public void testApplyRealColorImageWithROI() {
        Clip clip = new Clip(0, 0);
        RealColorImage inputImage = new RealColorImage(/*image properties*/);
        ROI roi = new ROI(/*ROI properties*/);
        RealColorImage expectedImage = new RealColorImage(/*expected result*/);
        
        RealColorImage actualImage = (RealColorImage) clip.apply(inputImage, roi);
        
        assertEquals(expectedImage, actualImage);
    }
}