import org.junit.*;
import static org.junit.Assert.*;

public class ClipTest {

    @Test
    public void testApply_GrayImage() {
        Clip clip = new Clip(0, 255);
        GrayImage image = new GrayImage(5, 5);
        image.set(0, 0, 100);
        image.set(2, 2, 300);
        image.set(4, 4, -50);
        
        Image result = clip.apply(image);
        
        assertEquals(0, result.get(0, 0));
        assertEquals(255, result.get(2, 2));
        assertEquals(0, result.get(4, 4));
    }
    
    @Test
    public void testApply_GrayImageWithROI() {
        Clip clip = new Clip(0, 255);
        GrayImage image = new GrayImage(5, 5);
        image.set(0, 0, 100);
        image.set(2, 2, 300);
        image.set(4, 4, -50);
        
        ROI roi = new ROI(1, 1, 3, 3);
        Image result = clip.apply(image, roi);
        
        assertEquals(100, result.get(0, 0));
        assertEquals(255, result.get(2, 2));
        assertEquals(-50, result.get(4, 4));
    }
    
    @Test
    public void testApply_RealGrayImage() {
        Clip clip = new Clip(0.0f, 1.0f);
        RealGrayImage image = new RealGrayImage(5, 5);
        image.set(0, 0, 0.5f);
        image.set(2, 2, 1.5f);
        image.set(4, 4, -0.1f);
        
        Image result = clip.apply(image);
        
        assertEquals(0.0f, result.get(0, 0));
        assertEquals(1.0f, result.get(2, 2));
        assertEquals(0.0f, result.get(4, 4));
    }
    
    @Test
    public void testApply_RealGrayImageWithROI() {
        Clip clip = new Clip(0.0f, 1.0f);
        RealGrayImage image = new RealGrayImage(5, 5);
        image.set(0, 0, 0.5f);
        image.set(2, 2, 1.5f);
        image.set(4, 4, -0.1f);
        
        ROI roi = new ROI(1, 1, 3, 3);
        Image result = clip.apply(image, roi);
        
        assertEquals(0.5f, result.get(0, 0));
        assertEquals(1.0f, result.get(2, 2));
        assertEquals(-0.1f, result.get(4, 4));
    }
    
    @Test
    public void testApply_ColorImage() {
        Clip clip = new Clip(0, 255);
        ColorImage image = new ColorImage(5, 5);
        image.set(0, 0, new RGBPixel(100, 0, 0));
        image.set(2, 2, new RGBPixel(0, 300, 0));
        image.set(4, 4, new RGBPixel(0, 0, -50));
        
        Image result = clip.apply(image);
        
        assertEquals(0, ((RGBPixel)result.get(0, 0)).getRed());
        assertEquals(255, ((RGBPixel)result.get(2, 2)).getGreen());
        assertEquals(0, ((RGBPixel)result.get(4, 4)).getBlue());
    }
    
    @Test
    public void testApply_ColorImageWithROI() {
        Clip clip = new Clip(0, 255);
        ColorImage image = new ColorImage(5, 5);
        image.set(0, 0, new RGBPixel(100, 0, 0));
        image.set(2, 2, new RGBPixel(0, 300, 0));
        image.set(4, 4, new RGBPixel(0, 0, -50));
        
        ROI roi = new ROI(1, 1, 3, 3);
        Image result = clip.apply(image, roi);
        
        assertEquals(100, ((RGBPixel)result.get(0, 0)).getRed());
        assertEquals(255, ((RGBPixel)result.get(2, 2)).getGreen());
        assertEquals(-50, ((RGBPixel)result.get(4, 4)).getBlue());
    }
    
    @Test
    public void testApply_RealColorImage() {
        Clip clip = new Clip(0.0f, 1.0f);
        RealColorImage image = new RealColorImage(5, 5);
        image.set(0, 0, new RealRGBPixel(0.5f, 0.0f, 0.0f));
        image.set(2, 2, new RealRGBPixel(0.0f, 1.5f, 0.0f));
        image.set(4, 4, new RealRGBPixel(0.0f, 0.0f, -0.1f));
        
        Image result = clip.apply(image);
        
        assertEquals(0.0f, ((RealRGBPixel)result.get(0, 0)).getRed());
        assertEquals(1.0f, ((RealRGBPixel)result.get(2, 2)).getGreen());
        assertEquals(0.0f, ((RealRGBPixel)result.get(4, 4)).getBlue());
    }
    
    @Test
    public void testApply_RealColorImageWithROI() {
        Clip clip = new Clip(0.0f, 1.0f);
        RealColorImage image = new RealColorImage(5, 5);
        image.set(0, 0, new RealRGBPixel(0.5f, 0.0f, 0.0f));
        image.set(2, 2, new RealRGBPixel(0.0f, 1.5f, 0.0f));
        image.set(4, 4, new RealRGBPixel(0.0f, 0.0f, -0.1f));
        
        ROI roi = new ROI(1, 1, 3, 3);
        Image result = clip.apply(image, roi);
        
        assertEquals(0.5f, ((RealRGBPixel)result.get(0, 0)).getRed());
        assertEquals(1.0f, ((RealRGBPixel)result.get(2, 2)).getGreen());
        assertEquals(-0.1f, ((RealRGBPixel)result.get(4, 4)).getBlue());
    }

    @Test
    public void testMain() {
        // Test main method here
    }
}