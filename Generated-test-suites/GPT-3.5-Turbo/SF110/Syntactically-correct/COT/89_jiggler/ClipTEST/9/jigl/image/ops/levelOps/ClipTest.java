
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ClipTest {
    
    private Clip clip;
    
    @Before
    public void setUp() {
        clip = new Clip(0, 255);
    }
    
    @After
    public void tearDown() {
        clip = null;
    }
    
    @Test
    public void testApply_GrayImage() {
        GrayImage image = new GrayImage(10, 10);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        
        Image result = clip.apply(image);
        
        assertEquals(image, result);
    }
    
    @Test
    public void testApply_GrayImage2() {
        GrayImage image = new GrayImage(10, 10);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        
        Image result = clip.apply(image, roi);
        
        assertEquals(image, result);
    }
    
    @Test
    public void testApply_RealGrayImage() {
        RealGrayImage image = new RealGrayImage(10, 10);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        
        Image result = clip.apply(image);
        
        assertEquals(image, result);
    }
    
    @Test
    public void testApply_RealGrayImage2() {
        RealGrayImage image = new RealGrayImage(10, 10);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        
        Image result = clip.apply(image, roi);
        
        assertEquals(image, result);
    }
    
    @Test
    public void testApply_ColorImage() {
        ColorImage image = new ColorImage(10, 10);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        
        Image result = clip.apply(image);
        
        assertEquals(image, result);
    }
    
    @Test
    public void testApply_ColorImage2() {
        ColorImage image = new ColorImage(10, 10);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        
        Image result = clip.apply(image, roi);
        
        assertEquals(image, result);
    }
    
    @Test
    public void testApply_RealColorImage() {
        RealColorImage image = new RealColorImage(10, 10);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        
        Image result = clip.apply(image);
        
        assertEquals(image, result);
    }
    
    @Test
    public void testApply_RealColorImage2() {
        RealColorImage image = new RealColorImage(10, 10);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        
        Image result = clip.apply(image, roi);
        
        assertEquals(image, result);
    }
    
}
