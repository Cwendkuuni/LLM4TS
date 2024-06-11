import org.junit.*;
import static org.junit.Assert.*;

public class ClipTest {

    @Test
    public void testApplyGrayImage() {
        Clip clip = new Clip(10, 100);
        GrayImage image = new GrayImage();
        image.set(0, 0, 50);
        image.set(1, 1, 200);
        image.set(2, 2, 5);
        
        GrayImage result = (GrayImage) clip.apply(image);
        
        assertEquals(50, result.get(0, 0));
        assertEquals(100, result.get(1, 1));
        assertEquals(10, result.get(2, 2));
    }

    @Test
    public void testApplyGrayImageWithROI() {
        Clip clip = new Clip(10, 100);
        GrayImage image = new GrayImage();
        image.set(0, 0, 50);
        image.set(1, 1, 200);
        image.set(2, 2, 5);
        ROI roi = new ROI(0, 0, 1, 1);
        
        GrayImage result = (GrayImage) clip.apply(image, roi);
        
        assertEquals(50, result.get(0, 0));
        assertEquals(100, result.get(1, 1));
        assertEquals(5, result.get(2, 2));
    }

    @Test
    public void testApplyRealGrayImage() {
        Clip clip = new Clip(0.0f, 1.0f);
        RealGrayImage image = new RealGrayImage();
        image.set(0, 0, 0.5f);
        image.set(1, 1, 1.5f);
        image.set(2, 2, -0.5f);
        
        RealGrayImage result = (RealGrayImage) clip.apply(image);
        
        assertEquals(0.5f, result.get(0, 0), 0.001f);
        assertEquals(1.0f, result.get(1, 1), 0.001f);
        assertEquals(0.0f, result.get(2, 2), 0.001f);
    }

    @Test
    public void testApplyRealGrayImageWithROI() {
        Clip clip = new Clip(-1.0f, 1.0f);
        RealGrayImage image = new RealGrayImage();
        image.set(0, 0, 0.5f);
        image.set(1, 1, 1.5f);
        image.set(2, 2, -0.5f);
        ROI roi = new ROI(0, 0, 1, 1);
        
        RealGrayImage result = (RealGrayImage) clip.apply(image, roi);
        
        assertEquals(0.5f, result.get(0, 0), 0.001f);
        assertEquals(1.0f, result.get(1, 1), 0.001f);
        assertEquals(-0.5f, result.get(2, 2), 0.001f);
    }

    @Test
    public void testApplyColorImage() {
        Clip clip = new Clip(10, 100);
        ColorImage image = new ColorImage();
        image.set(0, 0, 0, 50);
        image.set(0, 0, 1, 200);
        image.set(0, 0, 2, 5);
        
        ColorImage result = (ColorImage) clip.apply(image);
        
        assertEquals(50, result.get(0, 0, 0));
        assertEquals(100, result.get(0, 0, 1));
        assertEquals(10, result.get(0, 0, 2));
    }

    @Test
    public void testApplyColorImageWithROI() {
        Clip clip = new Clip(10, 100);
        ColorImage image = new ColorImage();
        image.set(0, 0, 0, 50);
        image.set(0, 0, 1, 200);
        image.set(0, 0, 2, 5);
        ROI roi = new ROI(0, 0, 0, 0);
        
        ColorImage result = (ColorImage) clip.apply(image, roi);
        
        assertEquals(50, result.get(0, 0, 0));
        assertEquals(100, result.get(0, 0, 1));
        assertEquals(5, result.get(0, 0, 2));
    }

    @Test
    public void testApplyRealColorImage() {
        Clip clip = new Clip(0.0f, 1.0f);
        RealColorImage image = new RealColorImage();
        image.set(0, 0, 0, 0.5f);
        image.set(0, 0, 1, 1.5f);
        image.set(0, 0, 2, -0.5f);
        
        RealColorImage result = (RealColorImage) clip.apply(image);
        
        assertEquals(0.5f, result.get(0, 0, 0), 0.001f);
        assertEquals(1.0f, result.get(0, 0, 1), 0.001f);
        assertEquals(0.0f, result.get(0, 0, 2), 0.001f);
    }

    @Test
    public void testApplyRealColorImageWithROI() {
        Clip clip = new Clip(-1.0f, 1.0f);
        RealColorImage image = new RealColorImage();
        image.set(0, 0, 0, 0.5f);
        image.set(0, 0, 1, 1.5f);
        image.set(0, 0, 2, -0.5f);
        ROI roi = new ROI(0, 0, 0, 0);
        
        RealColorImage result = (RealColorImage) clip.apply(image, roi);
        
        assertEquals(0.5f, result.get(0, 0, 0), 0.001f);
        assertEquals(1.0f, result.get(0, 0, 1), 0.001f);
        assertEquals(-0.5f, result.get(0, 0, 2), 0.001f);
    }

}