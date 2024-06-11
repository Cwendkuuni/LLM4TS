import static org.junit.Assert.*;
import org.junit.Test;

public class ClipTest {

    // Test case for apply method with GrayImage parameter
    @Test
    public void testApplyGrayImage() {
        GrayImage grayImage = new GrayImage(3, 3);
        Clip clip = new Clip(0, 255);
        
        Image result = clip.apply(grayImage);
        
        assertNotNull(result);
        assertTrue(result instanceof GrayImage);
    }

    // Test case for apply method with RealGrayImage parameter
    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage realGrayImage = new RealGrayImage(3, 3);
        Clip clip = new Clip(0.0f, 1.0f);
        
        Image result = clip.apply(realGrayImage);
        
        assertNotNull(result);
        assertTrue(result instanceof RealGrayImage);
    }

    // Test case for apply method with ColorImage parameter
    @Test
    public void testApplyColorImage() {
        ColorImage colorImage = new ColorImage(3, 3);
        Clip clip = new Clip(0, 255);
        
        Image result = clip.apply(colorImage);
        
        assertNotNull(result);
        assertTrue(result instanceof ColorImage);
    }

    // Test case for apply method with RealColorImage parameter
    @Test
    public void testApplyRealColorImage() {
        RealColorImage realColorImage = new RealColorImage(3, 3);
        Clip clip = new Clip(0.0f, 1.0f);
        
        Image result = clip.apply(realColorImage);
        
        assertNotNull(result);
        assertTrue(result instanceof RealColorImage);
    }

}