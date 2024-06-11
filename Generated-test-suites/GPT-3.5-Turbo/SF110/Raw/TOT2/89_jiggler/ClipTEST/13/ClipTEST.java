import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.*;
import jigl.image.ops.levelOps.Clip;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

public class ClipTest {

    @Test
    public void testApplyGrayImage() {
        GrayImage image = new GrayImage(100, 100);
        Clip clip = new Clip(0, 255);
        Image result = clip.apply(image);
        
        assertNotNull(result);
        assertEquals(image, result);
    }
    
    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage image = new RealGrayImage(100, 100);
        Clip clip = new Clip(0.0f, 1.0f);
        Image result = clip.apply(image);
        
        assertNotNull(result);
        assertEquals(image, result);
    }
    
    @Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage(100, 100);
        Clip clip = new Clip(0, 255);
        Image result = clip.apply(image);
        
        assertNotNull(result);
        assertEquals(image, result);
    }
    
    @Test
    public void testApplyRealColorImage() {
        RealColorImage image = new RealColorImage(100, 100);
        Clip clip = new Clip(0.0f, 1.0f);
        Image result = clip.apply(image);
        
        assertNotNull(result);
        assertEquals(image, result);
    }

    @Test
    public void testClipConstructorInt() {
        Clip clip = new Clip(0, 255);
        
        assertNotNull(clip);
        assertEquals(clip.int_min, 0);
        assertEquals(clip.int_max, 255);
    }
    
    @Test
    public void testClipConstructorFloat() {
        Clip clip = new Clip(0.0f, 1.0f);
        
        assertNotNull(clip);
        assertEquals(clip.float_min, 0.0f, 0.001);
        assertEquals(clip.float_max, 1.0f, 0.001);
    }
}