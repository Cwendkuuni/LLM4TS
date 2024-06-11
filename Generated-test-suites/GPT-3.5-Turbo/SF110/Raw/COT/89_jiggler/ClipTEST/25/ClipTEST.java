import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import jigl.image.*;
import jigl.image.ops.levelOps.Clip;

public class ClipTest {

    private Clip clip;

    @Before
    public void setUp() {
        clip = new Clip(0, 255);
    }

    @Test
    public void testApplyGrayImage() {
        GrayImage image = new GrayImage(10, 10);
        GrayImage expectedImage = new GrayImage(10, 10);
        // TODO: Add test logic and assertions here
        Image result = clip.apply(image);
        assertEquals(expectedImage, result);
    }

    @Test
    public void testApplyGrayImageROI() {
        GrayImage image = new GrayImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        GrayImage expectedImage = new GrayImage(10, 10);
        // TODO: Add test logic and assertions here
        Image result = clip.apply(image, roi);
        assertEquals(expectedImage, result);
    }

    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage image = new RealGrayImage(10, 10);
        RealGrayImage expectedImage = new RealGrayImage(10, 10);
        // TODO: Add test logic and assertions here
        Image result = clip.apply(image);
        assertEquals(expectedImage, result);
    }

    @Test
    public void testApplyRealGrayImageROI() {
        RealGrayImage image = new RealGrayImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        RealGrayImage expectedImage = new RealGrayImage(10, 10);
        // TODO: Add test logic and assertions here
        Image result = clip.apply(image, roi);
        assertEquals(expectedImage, result);
    }

    @Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage(10, 10);
        ColorImage expectedImage = new ColorImage(10, 10);
        // TODO: Add test logic and assertions here
        Image result = clip.apply(image);
        assertEquals(expectedImage, result);
    }

    @Test
    public void testApplyColorImageROI() {
        ColorImage image = new ColorImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        ColorImage expectedImage = new ColorImage(10, 10);
        // TODO: Add test logic and assertions here
        Image result = clip.apply(image, roi);
        assertEquals(expectedImage, result);
    }

    @Test
    public void testApplyRealColorImage() {
        RealColorImage image = new RealColorImage(10, 10);
        RealColorImage expectedImage = new RealColorImage(10, 10);
        // TODO: Add test logic and assertions here
        Image result = clip.apply(image);
        assertEquals(expectedImage, result);
    }

    @Test
    public void testApplyRealColorImageROI() {
        RealColorImage image = new RealColorImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        RealColorImage expectedImage = new RealColorImage(10, 10);
        // TODO: Add test logic and assertions here
        Image result = clip.apply(image, roi);
        assertEquals(expectedImage, result);
    }
}