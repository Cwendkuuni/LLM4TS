import static org.junit.Assert.*;
import org.junit.Test;

public class ClipTest {

    @Test
    public void testApplyGrayImage() {
        Clip clip = new Clip(0, 255);
        GrayImage image = new GrayImage(10, 10);
        image.set(5, 5, 200);
        
        Image result = clip.apply(image);
        
        assertEquals(200, result.get(5, 5));
    }
    
    @Test
    public void testApplyGrayImageROI() {
        Clip clip = new Clip(0, 255);
        GrayImage image = new GrayImage(10, 10);
        image.set(5, 5, 200);
        
        Image result = clip.apply(image, new ROI(4, 4, 6, 6));
        
        assertEquals(200, result.get(5, 5));
    }
    
    @Test
    public void testApplyRealGrayImage() {
        Clip clip = new Clip(0, 1);
        RealGrayImage image = new RealGrayImage(10, 10);
        image.set(5, 5, 0.8f);
        
        Image result = clip.apply(image);
        
        assertEquals(0.8f, result.get(5, 5));
    }
    
    @Test
    public void testApplyRealGrayImageROI() {
        Clip clip = new Clip(0, 1);
        RealGrayImage image = new RealGrayImage(10, 10);
        image.set(5, 5, 0.8f);
        
        Image result = clip.apply(image, new ROI(4, 4, 6, 6));
        
        assertEquals(0.8f, result.get(5, 5));
    }
    
    @Test
    public void testApplyColorImage() {
        Clip clip = new Clip(0, 255);
        ColorImage image = new ColorImage(10, 10);
        image.setPlane(0, new GrayImage(10, 10));
        image.setPlane(1, new GrayImage(10, 10));
        image.setPlane(2, new GrayImage(10, 10));
        image.getPlane(0).set(5, 5, 200);
        
        Image result = clip.apply(image);
        
        assertEquals(200, result.getPlane(0).get(5, 5));
    }
    
    @Test
    public void testApplyColorImageROI() {
        Clip clip = new Clip(0, 255);
        ColorImage image = new ColorImage(10, 10);
        image.setPlane(0, new GrayImage(10, 10));
        image.setPlane(1, new GrayImage(10, 10));
        image.setPlane(2, new GrayImage(10, 10));
        image.getPlane(0).set(5, 5, 200);
        
        Image result = clip.apply(image, new ROI(4, 4, 6, 6));
        
        assertEquals(200, result.getPlane(0).get(5, 5));
    }
    
    @Test
    public void testApplyRealColorImage() {
        Clip clip = new Clip(0, 1);
        RealColorImage image = new RealColorImage(10, 10);
        image.setPlane(0, new RealGrayImage(10, 10));
        image.setPlane(1, new RealGrayImage(10, 10));
        image.setPlane(2, new RealGrayImage(10, 10));
        image.getPlane(0).set(5, 5, 0.8f);
        
        Image result = clip.apply(image);
        
        assertEquals(0.8f, result.getPlane(0).get(5, 5));
    }
    
    @Test
    public void testApplyRealColorImageROI() {
        Clip clip = new Clip(0, 1);
        RealColorImage image = new RealColorImage(10, 10);
        image.setPlane(0, new RealGrayImage(10, 10));
        image.setPlane(1, new RealGrayImage(10, 10));
        image.setPlane(2, new RealGrayImage(10, 10));
        image.getPlane(0).set(5, 5, 0.8f);
        
        Image result = clip.apply(image, new ROI(4, 4, 6, 6));
        
        assertEquals(0.8f, result.getPlane(0).get(5, 5));
    }
}