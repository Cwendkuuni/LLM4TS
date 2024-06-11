
import org.junit.Test;
import static org.junit.Assert.*;

public class ClipTest {
    @Test
    public void testApplyGrayImage() {
        Clip clip = new Clip(0, 255);
        GrayImage image = new GrayImage(5, 5);
        image.set(1, 1, 100);
        image = clip.apply(image);
        assertEquals(100, image.get(1, 1));
        
        image.set(1, 1, 300);
        image = clip.apply(image);
        assertEquals(255, image.get(1, 1));
        
        image.set(1, 1, -100);
        image = clip.apply(image);
        assertEquals(0, image.get(1, 1));
    }
    
    @Test
    public void testApplyGrayImageWithROI() {
        Clip clip = new Clip(0, 255);
        GrayImage image = new GrayImage(5, 5);
        image.set(1, 1, 100);
        ROI roi = new ROI(0, 0, 4, 4);
        image = clip.apply(image, roi);
        assertEquals(100, image.get(1, 1));
        
        image.set(1, 1, 300);
        image = clip.apply(image, roi);
        assertEquals(255, image.get(1, 1));
        
        image.set(1, 1, -100);
        image = clip.apply(image, roi);
        assertEquals(0, image.get(1, 1));
    }
    
    @Test
    public void testApplyRealGrayImage() {
        Clip clip = new Clip(0, 1);
        RealGrayImage image = new RealGrayImage(5, 5);
        image.set(1, 1, 0.5f);
        image = clip.apply(image);
        assertEquals(0.5f, image.get(1, 1), 0.0001);
        
        image.set(1, 1, 2.0f);
        image = clip.apply(image);
        assertEquals(1.0f, image.get(1, 1), 0.0001);
        
        image.set(1, 1, -0.5f);
        image = clip.apply(image);
        assertEquals(0.0f, image.get(1, 1), 0.0001);
    }
    
    @Test
    public void testApplyRealGrayImageWithROI() {
        Clip clip = new Clip(0, 1);
        RealGrayImage image = new RealGrayImage(5, 5);
        image.set(1, 1, 0.5f);
        ROI roi = new ROI(0, 0, 4, 4);
        image = clip.apply(image, roi);
        assertEquals(0.5f, image.get(1, 1), 0.0001);
        
        image.set(1, 1, 2.0f);
        image = clip.apply(image, roi);
        assertEquals(1.0f, image.get(1, 1), 0.0001);
        
        image.set(1, 1, -0.5f);
        image = clip.apply(image, roi);
        assertEquals(0.0f, image.get(1, 1), 0.0001);
    }
    
    @Test
    public void testApplyColorImage() {
        Clip clip = new Clip(0, 255);
        ColorImage image = new ColorImage(5, 5);
        
        GrayImage plane0 = new GrayImage(5, 5);
        plane0.set(1, 1, 100);
        image.setPlane(0, plane0);
        
        GrayImage plane1 = new GrayImage(5, 5);
        plane1.set(1, 1, 200);
        image.setPlane(1, plane1);
        
        GrayImage plane2 = new GrayImage(5, 5);
        plane2.set(1, 1, 300);
        image.setPlane(2, plane2);
        
        image = clip.apply(image);
        
        assertEquals(100, image.plane(0).get(1, 1));
        assertEquals(200, image.plane(1).get(1, 1));
        assertEquals(255, image.plane(2).get(1, 1));
    }
    
    @Test
    public void testApplyColorImageWithROI() {
        Clip clip = new Clip(0, 255);
        ColorImage image = new ColorImage(5, 5);
        
        GrayImage plane0 = new GrayImage(5, 5);
        plane0.set(1, 1, 100);
        image.setPlane(0, plane0);
        
        GrayImage plane1 = new GrayImage(5, 5);
        plane1.set(1, 1, 200);
        image.setPlane(1, plane1);
        
        GrayImage plane2 = new GrayImage(5, 5);
        plane2.set(1, 1, 300);
        image.setPlane(2, plane2);
        
        ROI roi = new ROI(0, 0, 4, 4);
        image = clip.apply(image, roi);
        
        assertEquals(100, image.plane(0).get(1, 1));
        assertEquals(200, image.plane(1).get(1, 1));
        assertEquals(255, image.plane(2).get(1, 1));
    }
    
    @Test
    public void testApplyRealColorImage() {
        Clip clip = new Clip(0, 1);
        RealColorImage image = new RealColorImage(5, 5);
        
        RealGrayImage plane0 = new RealGrayImage(5, 5);
        plane0.set(1, 1, 0.5f);
        image.setPlane(0, plane0);
        
        RealGrayImage plane1 = new RealGrayImage(5, 5);
        plane1.set(1, 1, 0.8f);
        image.setPlane(1, plane1);
        
        RealGrayImage plane2 = new RealGrayImage(5, 5);
        plane2.set(1, 1, 1.2f);
        image.setPlane(2, plane2);
        
        image = clip.apply(image);
        
        assertEquals(0.5f, image.plane(0).get(1, 1), 0.0001);
        assertEquals(0.8f, image.plane(1).get(1, 1), 0.0001);
        assertEquals(1.0f, image.plane(2).get(1, 1), 0.0001);
    }
    
    @Test
    public void testApplyRealColorImageWithROI() {
        Clip clip = new Clip(0, 1);
        RealColorImage image = new RealColorImage(5, 5);
        
        RealGrayImage plane0 = new RealGrayImage(5, 5);
        plane0.set(1, 1, 0.5f);
        image.setPlane(0, plane0);
        
        RealGrayImage plane1 = new RealGrayImage(5, 5);
        plane1.set(1, 1, 0.8f);
        image.setPlane(1, plane1);
        
        RealGrayImage plane2 = new RealGrayImage(5, 5);
        plane2.set(1, 1, 1.2f);
        image.setPlane(2, plane2);
        
        ROI roi = new ROI(0, 0, 4, 4);
        image = clip.apply(image, roi);
        
        assertEquals(0.5f, image.plane(0).get(1, 1), 0.0001);
        assertEquals(0.8f, image.plane(1).get(1, 1), 0.0001);
        assertEquals(1.0f, image.plane(2).get(1, 1), 0.0001);
    }
}
