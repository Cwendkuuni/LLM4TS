import static org.junit.Assert.*;
import org.junit.Test;

public class ClipTest {
    
    @Test
    public void testApplyGrayImage() {
        Clip clip = new Clip(0, 255);
        GrayImage image = new GrayImage(10, 10);
        image.set(5, 5, 100);
        
        clip.apply(image);
        
        assertEquals(100, image.get(5, 5));
    }
    
    @Test
    public void testApplyGrayImageROI() {
        Clip clip = new Clip(0, 255);
        GrayImage image = new GrayImage(10, 10);
        image.set(5, 5, 300);
        
        clip.apply(image, new ROI(0, 0, 9, 9));
        
        assertEquals(255, image.get(5, 5));
    }
    
    @Test
    public void testApplyRealGrayImage() {
        Clip clip = new Clip(0, 255);
        RealGrayImage image = new RealGrayImage(10, 10);
        image.set(5, 5, 100.5f);
        
        clip.apply(image);
        
        assertEquals(100.5f, image.get(5, 5), 0.000001);
    }
    
    @Test
    public void testApplyRealGrayImageROI() {
        Clip clip = new Clip(0, 255);
        RealGrayImage image = new RealGrayImage(10, 10);
        image.set(5, 5, 300.5f);
        
        clip.apply(image, new ROI(0, 0, 9, 9));
        
        assertEquals(255, image.get(5, 5), 0.000001);
    }
    
    @Test
    public void testApplyColorImage() {
        Clip clip = new Clip(0, 255);
        ColorImage image = new ColorImage(10, 10);
        image.set(5, 5, new int[]{100, 150, 200});
        
        clip.apply(image);
        
        assertEquals(100, image.getPlane(0).get(5, 5));
        assertEquals(150, image.getPlane(1).get(5, 5));
        assertEquals(200, image.getPlane(2).get(5, 5));
    }
    
    @Test
    public void testApplyColorImageROI() {
        Clip clip = new Clip(0, 255);
        ColorImage image = new ColorImage(10, 10);
        image.set(5, 5, new int[]{300, 400, 500});
        
        clip.apply(image, new ROI(0, 0, 9, 9));
        
        assertEquals(255, image.getPlane(0).get(5, 5));
        assertEquals(255, image.getPlane(1).get(5, 5));
        assertEquals(255, image.getPlane(2).get(5, 5));
    }
    
    @Test
    public void testApplyRealColorImage() {
        Clip clip = new Clip(0, 255);
        RealColorImage image = new RealColorImage(10, 10);
        image.set(5, 5, new float[]{100.5f, 150.5f, 200.5f});
        
        clip.apply(image);
        
        assertEquals(100.5f, image.getPlane(0).get(5, 5), 0.000001);
        assertEquals(150.5f, image.getPlane(1).get(5, 5), 0.000001);
        assertEquals(200.5f, image.getPlane(2).get(5, 5), 0.000001);
    }
    
    @Test
    public void testApplyRealColorImageROI() {
        Clip clip = new Clip(0, 255);
        RealColorImage image = new RealColorImage(10, 10);
        image.set(5, 5, new float[]{300.5f, 400.5f, 500.5f});
        
        clip.apply(image, new ROI(0, 0, 9, 9));
        
        assertEquals(255, image.getPlane(0).get(5, 5), 0.000001);
        assertEquals(255, image.getPlane(1).get(5, 5), 0.000001);
        assertEquals(255, image.getPlane(2).get(5, 5), 0.000001);
    }
}