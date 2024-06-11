import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.ops.levelOps.*;

public class ClipTest {

    private Clip clip;

    @Before
    public void setUp() {
        clip = new Clip(-10, 10);
    }

    @Test
    public void testApplyGrayImage() {
        GrayImage image = new GrayImage(5, 5);
        image.set(2, 2, 15);

        Image result = clip.apply(image);

        assertEquals(10, result.get(2, 2));
    }

    @Test
    public void testApplyGrayImageWithROI() {
        GrayImage image = new GrayImage(5, 5);
        image.set(2, 2, 15);

        ROI roi = new ROI(1, 1, 3, 3);

        Image result = clip.apply(image, roi);

        assertEquals(10, result.get(2, 2));
    }

    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage image = new RealGrayImage(5, 5);
        image.set(2, 2, 15.5f);

        Image result = clip.apply(image);

        assertEquals(10.0f, result.get(2, 2), 0.001f);
    }

    @Test
    public void testApplyRealGrayImageWithROI() {
        RealGrayImage image = new RealGrayImage(5, 5);
        image.set(2, 2, 15.5f);

        ROI roi = new ROI(1, 1, 3, 3);

        Image result = clip.apply(image, roi);

        assertEquals(10.0f, result.get(2, 2), 0.001f);
    }

    @Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage(5, 5);
        image.setPlane(0, new GrayImage(5, 5));
        image.setPlane(1, new GrayImage(5, 5));
        image.setPlane(2, new GrayImage(5, 5));

        image.getPlane(0).set(2, 2, 15);
        image.getPlane(1).set(2, 2, 20);
        image.getPlane(2).set(2, 2, 25);

        Image result = clip.apply(image);

        assertEquals(10, result.getPlane(0).get(2, 2));
        assertEquals(10, result.getPlane(1).get(2, 2));
        assertEquals(10, result.getPlane(2).get(2, 2));
    }

    @Test
    public void testApplyColorImageWithROI() {
        ColorImage image = new ColorImage(5, 5);
        image.setPlane(0, new GrayImage(5, 5));
        image.setPlane(1, new GrayImage(5, 5));
        image.setPlane(2, new GrayImage(5, 5));

        image.getPlane(0).set(2, 2, 15);
        image.getPlane(1).set(2, 2, 20);
        image.getPlane(2).set(2, 2, 25);

        ROI roi = new ROI(1, 1, 3, 3);

        Image result = clip.apply(image, roi);

        assertEquals(10, result.getPlane(0).get(2, 2));
        assertEquals(10, result.getPlane(1).get(2, 2));
        assertEquals(10, result.getPlane(2).get(2, 2));
    }

    @Test
    public void testApplyRealColorImage() {
        RealColorImage image = new RealColorImage(5, 5);
        image.setPlane(0, new RealGrayImage(5, 5));
        image.setPlane(1, new RealGrayImage(5, 5));
        image.setPlane(2, new RealGrayImage(5, 5));

        image.getPlane(0).set(2, 2, 15.5f);
        image.getPlane(1).set(2, 2, 20.5f);
        image.getPlane(2).set(2, 2, 25.5f);

        Image result = clip.apply(image);

        assertEquals(10.0f, result.getPlane(0).get(2, 2), 0.001f);
        assertEquals(10.0f, result.getPlane(1).get(2, 2), 0.001f);
        assertEquals(10.0f, result.getPlane(2).get(2, 2), 0.001f);
    }

    @Test
    public void testApplyRealColorImageWithROI() {
        RealColorImage image = new RealColorImage(5, 5);
        image.setPlane(0, new RealGrayImage(5, 5));
        image.setPlane(1, new RealGrayImage(5, 5));
        image.setPlane(2, new RealGrayImage(5, 5));

        image.getPlane(0).set(2, 2, 15.5f);
        image.getPlane(1).set(2, 2, 20.5f);
        image.getPlane(2).set(2, 2, 25.5f);

        ROI roi = new ROI(1, 1, 3, 3);

        Image result = clip.apply(image, roi);

        assertEquals(10.0f, result.getPlane(0).get(2, 2), 0.001f);
        assertEquals(10.0f, result.getPlane(1).get(2, 2), 0.001f);
        assertEquals(10.0f, result.getPlane(2).get(2, 2), 0.001f);
    }

    @Test
    public void testMain() {
        // TODO: Implement this test case
    }
}