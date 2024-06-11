import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ClipTest {

    private Clip clip;
    
    @Before
    public void setUp() {
        clip = new Clip(10, 20);
    }
    
    @Test
    public void testApply_GrayImage() {
        GrayImage image = new GrayImage(10, 10);
        Image result = clip.apply(image);
        
        assertEquals(image, result); // needs to be implemented properly
    }
    
    @Test
    public void testApply_GrayImage_Roi() {
        GrayImage image = new GrayImage(10, 10);
        ROI roi = new ROI(0, 0, 5, 5);
        Image result = clip.apply(image, roi);
        
        assertEquals(image, result); // needs to be implemented properly
    }
    
    @Test
    public void testApply_RealGrayImage() {
        RealGrayImage image = new RealGrayImage(10, 10);
        Image result = clip.apply(image);
        
        assertEquals(image, result); // needs to be implemented properly
    }
    
    @Test
    public void testApply_RealGrayImage_Roi() {
        RealGrayImage image = new RealGrayImage(10, 10);
        ROI roi = new ROI(0, 0, 5, 5);
        Image result = clip.apply(image, roi);
        
        assertEquals(image, result); // needs to be implemented properly
    }
    
    @Test
    public void testApply_ColorImage() {
        ColorImage image = new ColorImage(10, 10);
        Image result = clip.apply(image);
        
        assertEquals(image, result); // needs to be implemented properly
    }
    
    @Test
    public void testApply_ColorImage_Roi() {
        ColorImage image = new ColorImage(10, 10);
        ROI roi = new ROI(0, 0, 5, 5);
        Image result = clip.apply(image, roi);
        
        assertEquals(image, result); // needs to be implemented properly
    }
    
    @Test
    public void testApply_RealColorImage() {
        RealColorImage image = new RealColorImage(10, 10);
        Image result = clip.apply(image);
        
        assertEquals(image, result); // needs to be implemented properly
    }
    
    @Test
    public void testApply_RealColorImage_Roi() {
        RealColorImage image = new RealColorImage(10, 10);
        ROI roi = new ROI(0, 0, 5, 5);
        Image result = clip.apply(image, roi);
        
        assertEquals(image, result); // needs to be implemented properly
    }
}