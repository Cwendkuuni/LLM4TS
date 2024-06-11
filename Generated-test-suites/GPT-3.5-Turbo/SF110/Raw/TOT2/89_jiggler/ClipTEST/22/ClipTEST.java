import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.ops.levelOps.Clip;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

    @Test
    public void testApplyGrayImageWithoutROI() {
        GrayImage image = new GrayImage(5, 5);
        Clip clip = new Clip(0, 255);
        Image result = clip.apply(image);
        assertNotNull(result);
        assertTrue(result instanceof GrayImage);
    }

    @Test
    public void testApplyRealGrayImageWithROI() {
        RealGrayImage image = new RealGrayImage(5, 5);
        Clip clip = new Clip(0.0f, 1.0f);
        ROI roi = new ROI(1, 1, 3, 3);
        Image result = clip.apply(image, roi);
        assertNotNull(result);
        assertTrue(result instanceof RealGrayImage);
    }

    @Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage(5, 5);
        Clip clip = new Clip(0, 255);
        Image result = clip.apply(image);
        assertNotNull(result);
        assertTrue(result instanceof ColorImage);
    }

    @Test
    public void testApplyRealColorImageWithInvalidROI() {
        RealColorImage image = new RealColorImage(5, 5);
        Clip clip = new Clip(0.0f, 1.0f);
        ROI roi = new ROI(5, 5, 3, 3); // Invalid ROI
        Image result = clip.apply(image, roi);
        assertNotNull(result);
        assertTrue(result instanceof RealColorImage);
    }

    @Test
    public void testMainMethodWithInvalidInput() {
        String[] argv = {"a", "b", "input.jpg", "output.jpg"};
        Clip.main(argv); // Expect exception due to invalid input
    }

}