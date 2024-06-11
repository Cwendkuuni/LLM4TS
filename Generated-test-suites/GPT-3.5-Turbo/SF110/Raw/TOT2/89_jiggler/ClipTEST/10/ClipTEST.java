import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClipTest {

    private Clip clip;

    @Before
    public void setUp() {
        clip = new Clip(1, 10);
    }

    @Test
    public void testApplyGrayImage() {
        GrayImage image = new GrayImage(5, 5);
        image.fill(5);
        image.set(2, 2, 15);
        Image result = clip.apply(image);
        assertEquals(10, result.get(2, 2));
    }

    @Test
    public void testApplyGrayImageWithROI() {
        GrayImage image = new GrayImage(5, 5);
        image.fill(5);
        image.set(2, 2, 15);
        ROI roi = new ROI(1, 1, 3, 3);
        Image result = clip.apply(image, roi);
        assertEquals(10, result.get(2, 2));
    }

    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage image = new RealGrayImage(5, 5);
        image.fill(5.0f);
        image.set(2, 2, 15.0f);
        Image result = clip.apply(image);
        assertEquals(10.0, result.get(2, 2));
    }

    @Test
    public void testApplyRealGrayImageWithROI() {
        RealGrayImage image = new RealGrayImage(5, 5);
        image.fill(5.0f);
        image.set(2, 2, 15.0f);
        ROI roi = new ROI(1, 1, 3, 3);
        Image result = clip.apply(image, roi);
        assertEquals(10.0, result.get(2, 2));
    }

    @Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage(5, 5);
        image.fill(5);
        Image result = clip.apply(image);
        assertEquals(10, result.plane(0).get(2, 2));
        assertEquals(10, result.plane(1).get(2, 2));
        assertEquals(10, result.plane(2).get(2, 2));
    }

    @Test
    public void testApplyColorImageWithROI() {
        ColorImage image = new ColorImage(5, 5);
        image.fill(5);
        ROI roi = new ROI(1, 1, 3, 3);
        Image result = clip.apply(image, roi);
        assertEquals(10, result.plane(0).get(2, 2));
        assertEquals(10, result.plane(1).get(2, 2));
        assertEquals(10, result.plane(2).get(2, 2));
    }

    @Test
    public void testApplyRealColorImage() {
        RealColorImage image = new RealColorImage(5, 5);
        image.fill(5.0f);
        Image result = clip.apply(image);
        assertEquals(10.0, result.plane(0).get(2, 2));
        assertEquals(10.0, result.plane(1).get(2, 2));
        assertEquals(10.0, result.plane(2).get(2, 2));
    }

    @Test
    public void testApplyRealColorImageWithROI() {
        RealColorImage image = new RealColorImage(5, 5);
        image.fill(5.0f);
        ROI roi = new ROI(1, 1, 3, 3);
        Image result = clip.apply(image, roi);
        assertEquals(10.0, result.plane(0).get(2, 2));
        assertEquals(10.0, result.plane(1).get(2, 2));
        assertEquals(10.0, result.plane(2).get(2, 2));
    }
}