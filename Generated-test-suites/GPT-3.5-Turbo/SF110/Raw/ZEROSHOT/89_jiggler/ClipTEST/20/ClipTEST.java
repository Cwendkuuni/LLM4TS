import org.junit.*;
import static org.junit.Assert.*;

public class ClipTest {

    @Test
    public void testApplyWithGrayImage() {
        Clip clip = new Clip(10, 100);
        GrayImage image = new GrayImage();
        image.set(0, 0, 50);
        image.set(0, 1, 150);
        image.set(0, 2, 5);
        
        Image result = clip.apply(image);
        
        assertEquals(50, result.get(0, 0));
        assertEquals(100, result.get(0, 1));
        assertEquals(10, result.get(0, 2));
    }

    @Test
    public void testApplyWithGrayImageAndROI() {
        Clip clip = new Clip(10, 100);
        GrayImage image = new GrayImage();
        image.set(0, 0, 50);
        image.set(0, 1, 150);
        image.set(0, 2, 5);
        ROI roi = new ROI(0, 0, 0, 2);
        
        Image result = clip.apply(image, roi);
        
        assertEquals(50, result.get(0, 0));
        assertEquals(100, result.get(0, 1));
        assertEquals(10, result.get(0, 2));
    }

    @Test
    public void testApplyWithRealGrayImage() {
        Clip clip = new Clip(10, 100);
        RealGrayImage image = new RealGrayImage();
        image.set(0, 0, 50.5f);
        image.set(0, 1, 150.5f);
        image.set(0, 2, 5.5f);
        
        Image result = clip.apply(image);
        
        assertEquals(50.5f, result.get(0, 0));
        assertEquals(100, result.get(0, 1));
        assertEquals(10, result.get(0, 2));
    }

    @Test
    public void testApplyWithRealGrayImageAndROI() {
        Clip clip = new Clip(10, 100);
        RealGrayImage image = new RealGrayImage();
        image.set(0, 0, 50.5f);
        image.set(0, 1, 150.5f);
        image.set(0, 2, 5.5f);
        ROI roi = new ROI(0, 0, 0, 2);
        
        Image result = clip.apply(image, roi);
        
        assertEquals(50.5f, result.get(0, 0));
        assertEquals(100, result.get(0, 1));
        assertEquals(10, result.get(0, 2));
    }

    @Test
    public void testApplyWithColorImage() {
        Clip clip = new Clip(10, 100);
        ColorImage image = new ColorImage();
        image.set(0, 0, new GrayPixel(50));
        image.set(0, 1, new GrayPixel(150));
        image.set(0, 2, new GrayPixel(5));
        
        Image result = clip.apply(image);
        
        assertEquals(50, ((GrayPixel)result.get(0, 0)).value());
        assertEquals(100, ((GrayPixel)result.get(0, 1)).value());
        assertEquals(10, ((GrayPixel)result.get(0, 2)).value());
    }

    @Test
    public void testApplyWithColorImageAndROI() {
        Clip clip = new Clip(10, 100);
        ColorImage image = new ColorImage();
        image.set(0, 0, new GrayPixel(50));
        image.set(0, 1, new GrayPixel(150));
        image.set(0, 2, new GrayPixel(5));
        ROI roi = new ROI(0, 0, 0, 2);
        
        Image result = clip.apply(image, roi);
        
        assertEquals(50, ((GrayPixel)result.get(0, 0)).value());
        assertEquals(100, ((GrayPixel)result.get(0, 1)).value());
        assertEquals(10, ((GrayPixel)result.get(0, 2)).value());
    }

    @Test
    public void testApplyWithRealColorImage() {
        Clip clip = new Clip(10, 100);
        RealColorImage image = new RealColorImage();
        image.set(0, 0, new RealGrayPixel(50.5f));
        image.set(0, 1, new RealGrayPixel(150.5f));
        image.set(0, 2, new RealGrayPixel(5.5f));
        
        Image result = clip.apply(image);
        
        assertEquals(50.5f, ((RealGrayPixel)result.get(0, 0)).value(), 0.001f);
        assertEquals(100, ((RealGrayPixel)result.get(0, 1)).value(), 0.001f);
        assertEquals(10, ((RealGrayPixel)result.get(0, 2)).value(), 0.001f);
    }

    @Test
    public void testApplyWithRealColorImageAndROI() {
        Clip clip = new Clip(10, 100);
        RealColorImage image = new RealColorImage();
        image.set(0, 0, new RealGrayPixel(50.5f));
        image.set(0, 1, new RealGrayPixel(150.5f));
        image.set(0, 2, new RealGrayPixel(5.5f));
        ROI roi = new ROI(0, 0, 0, 2);
        
        Image result = clip.apply(image, roi);
        
        assertEquals(50.5f, ((RealGrayPixel)result.get(0, 0)).value(), 0.001f);
        assertEquals(100, ((RealGrayPixel)result.get(0, 1)).value(), 0.001f);
        assertEquals(10, ((RealGrayPixel)result.get(0, 2)).value(), 0.001f);
    }

}