
package jigl.image.ops.levelOps;

import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.ColorImage;
import jigl.image.RealColorImage;
import jigl.image.ROI;
import jigl.image.ops.levelOps.Clip;
import org.junit.Assert;
import org.junit.Test;

public class ClipTest {
    
    @Test
    public void testApplyGrayImage() {
        Clip clip = new Clip(0, 255);
        
        GrayImage image = new GrayImage(10, 10);
        image.set(5, 5, 300);
        
        GrayImage expected = new GrayImage(10, 10);
        expected.set(5, 5, 255);
        
        image = (GrayImage)clip.apply(image);
        
        Assert.assertEquals(expected, image);
    }
    
    @Test
    public void testApplyGrayImageWithROI() {
        Clip clip = new Clip(0, 100);
        ROI roi = new ROI(2, 2, 6, 6);
        
        GrayImage image = new GrayImage(10, 10);
        image.set(5, 5, 200);
        
        GrayImage expected = new GrayImage(10, 10);
        expected.set(5, 5, 100);
        
        image = (GrayImage)clip.apply(image, roi);
        
        Assert.assertEquals(expected, image);
    }
    
    @Test
    public void testApplyRealGrayImage() {
        Clip clip = new Clip(0.0f, 1.0f);
        
        RealGrayImage image = new RealGrayImage(10, 10);
        image.set(5, 5, 1.5f);
        
        RealGrayImage expected = new RealGrayImage(10, 10);
        expected.set(5, 5, 1.0f);
        
        image = (RealGrayImage)clip.apply(image);
        
        Assert.assertEquals(expected, image);
    }
    
    @Test
    public void testApplyRealGrayImageWithROI() {
        Clip clip = new Clip(0.0f, 0.5f);
        ROI roi = new ROI(2, 2, 6, 6);
        
        RealGrayImage image = new RealGrayImage(10, 10);
        image.set(5, 5, 0.75f);
        
        RealGrayImage expected = new RealGrayImage(10, 10);
        expected.set(5, 5, 0.5f);
        
        image = (RealGrayImage)clip.apply(image, roi);
        
        Assert.assertEquals(expected, image);
    }
    
    @Test
    public void testApplyColorImage() {
        Clip clip = new Clip(0, 255);
        
        ColorImage image = new ColorImage(10, 10);
        image.setPlane(0, new GrayImage(10, 10));
        image.setPlane(1, new GrayImage(10, 10));
        image.setPlane(2, new GrayImage(10, 10));
        image.getPlane(0).set(5, 5, 300);
        image.getPlane(1).set(5, 5, 300);
        image.getPlane(2).set(5, 5, 300);
        
        ColorImage expected = new ColorImage(10, 10);
        expected.setPlane(0, new GrayImage(10, 10));
        expected.setPlane(1, new GrayImage(10, 10));
        expected.setPlane(2, new GrayImage(10, 10));
        expected.getPlane(0).set(5, 5, 255);
        expected.getPlane(1).set(5, 5, 255);
        expected.getPlane(2).set(5, 5, 255);
        
        image = (ColorImage)clip.apply(image);
        
        Assert.assertEquals(expected, image);
    }
    
    @Test
    public void testApplyColorImageWithROI() {
        Clip clip = new Clip(0, 100);
        ROI roi = new ROI(2, 2, 6, 6);
        
        ColorImage image = new ColorImage(10, 10);
        image.setPlane(0, new GrayImage(10, 10));
        image.setPlane(1, new GrayImage(10, 10));
        image.setPlane(2, new GrayImage(10, 10));
        image.getPlane(0).set(5, 5, 200);
        image.getPlane(1).set(5, 5, 200);
        image.getPlane(2).set(5, 5, 200);
        
        ColorImage expected = new ColorImage(10, 10);
        expected.setPlane(0, new GrayImage(10, 10));
        expected.setPlane(1, new GrayImage(10, 10));
        expected.setPlane(2, new GrayImage(10, 10));
        expected.getPlane(0).set(5, 5, 100);
        expected.getPlane(1).set(5, 5, 100);
        expected.getPlane(2).set(5, 5, 100);
        
        image = (ColorImage)clip.apply(image, roi);
        
        Assert.assertEquals(expected, image);
    }
    
    @Test
    public void testApplyRealColorImage() {
        Clip clip = new Clip(0.0f, 1.0f);
        
        RealColorImage image = new RealColorImage(10, 10);
        image.setPlane(0, new RealGrayImage(10, 10));
        image.setPlane(1, new RealGrayImage(10, 10));
        image.setPlane(2, new RealGrayImage(10, 10));
        image.getPlane(0).set(5, 5, 1.5f);
        image.getPlane(1).set(5, 5, 1.5f);
        image.getPlane(2).set(5, 5, 1.5f);
        
        RealColorImage expected = new RealColorImage(10, 10);
        expected.setPlane(0, new RealGrayImage(10, 10));
        expected.setPlane(1, new RealGrayImage(10, 10));
        expected.setPlane(2, new RealGrayImage(10, 10));
        expected.getPlane(0).set(5, 5, 1.0f);
        expected.getPlane(1).set(5, 5, 1.0f);
        expected.getPlane(2).set(5, 5, 1.0f);
        
        image = (RealColorImage)clip.apply(image);
        
        Assert.assertEquals(expected, image);
    }
    
    @Test
    public void testApplyRealColorImageWithROI() {
        Clip clip = new Clip(0.0f, 0.5f);
        ROI roi = new ROI(2, 2, 6, 6);
        
        RealColorImage image = new RealColorImage(10, 10);
        image.setPlane(0, new RealGrayImage(10, 10));
        image.setPlane(1, new RealGrayImage(10, 10));
        image.setPlane(2, new RealGrayImage(10, 10));
        image.getPlane(0).set(5, 5, 0.75f);
        image.getPlane(1).set(5, 5, 0.75f);
        image.getPlane(2).set(5, 5, 0.75f);
        
        RealColorImage expected = new RealColorImage(10, 10);
        expected.setPlane(0, new RealGrayImage(10, 10));
        expected.setPlane(1, new RealGrayImage(10, 10));
        expected.setPlane(2, new RealGrayImage(10, 10));
        expected.getPlane(0).set(5, 5, 0.5f);
        expected.getPlane(1).set(5, 5, 0.5f);
        expected.getPlane(2).set(5, 5, 0.5f);
        
        image = (RealColorImage)clip.apply(image, roi);
        
        Assert.assertEquals(expected, image);
    }
}
