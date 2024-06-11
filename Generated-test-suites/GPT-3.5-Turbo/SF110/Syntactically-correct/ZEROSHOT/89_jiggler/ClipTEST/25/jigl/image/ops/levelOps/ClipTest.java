import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClipTest {
    
    private Clip clip;
    
    @Before
    public void setup() {
        clip = new Clip(0, 0);
    }
    
    @Test
    public void testApply_withGrayImage_shouldReturnImage() {
        GrayImage image = new GrayImage();
        Image result = clip.apply(image);
        
        assertNotNull(result);
        assertTrue(result instanceof GrayImage);
    }
    
    @Test
    public void testApply_withGrayImageAndROI_shouldReturnImage() {
        GrayImage image = new GrayImage();
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        
        Image result = clip.apply(image, roi);
        
        assertNotNull(result);
        assertTrue(result instanceof GrayImage);
    }
    
    @Test
    public void testApply_withRealGrayImage_shouldReturnImage() {
        RealGrayImage image = new RealGrayImage();
        Image result = clip.apply(image);
        
        assertNotNull(result);
        assertTrue(result instanceof RealGrayImage);
    }
    
    @Test
    public void testApply_withRealGrayImageAndROI_shouldReturnImage() {
        RealGrayImage image = new RealGrayImage();
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        
        Image result = clip.apply(image, roi);
        
        assertNotNull(result);
        assertTrue(result instanceof RealGrayImage);
    }
    
    @Test
    public void testApply_withColorImage_shouldReturnImage() {
        ColorImage image = new ColorImage();
        Image result = clip.apply(image);
        
        assertNotNull(result);
        assertTrue(result instanceof ColorImage);
    }
    
    @Test
    public void testApply_withColorImageAndROI_shouldReturnImage() {
        ColorImage image = new ColorImage();
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        
        Image result = clip.apply(image, roi);
        
        assertNotNull(result);
        assertTrue(result instanceof ColorImage);
    }
    
    @Test
    public void testApply_withRealColorImage_shouldReturnImage() {
        RealColorImage image = new RealColorImage();
        Image result = clip.apply(image);
        
        assertNotNull(result);
        assertTrue(result instanceof RealColorImage);
    }
    
    @Test
    public void testApply_withRealColorImageAndROI_shouldReturnImage() {
        RealColorImage image = new RealColorImage();
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        
        Image result = clip.apply(image, roi);
        
        assertNotNull(result);
        assertTrue(result instanceof RealColorImage);
    }
    
    @Test
    public void testMain_withValidArguments_shouldNotThrowException() {
        String[] argv = {"0.0", "0.0", "inputfile.jpg", "outputfile.jpg"};
        try {
            Clip.main(argv);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }
    
    @Test
    public void testMain_withInvalidArguments_shouldThrowException() {
        String[] argv = {"a", "0.0", "inputfile.jpg", "outputfile.jpg"};
        try {
            Clip.main(argv);
            fail("Should throw exception");
        } catch (Exception e) {
            assertNotNull(e);
        }
    }
}