import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.levelOps.ROI;
import jigl.image.types.ColorImage;
import jigl.image.types.GrayImage;
import jigl.image.types.RealColorImage;
import jigl.image.types.RealGrayImage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClipTest {

    private Clip clip;

    @Before
    public void setUp() {
        clip = new Clip(0, 0);
    }

    @After
    public void tearDown() {
        clip = null;
    }

    @Test
    public void testApply_GrayImage() {
        GrayImage image = new GrayImage(10, 10);
        image.set(5, 5, 100);

        GrayImage result = clip.apply(image);

        int expectedValue = 0;
        assertEquals(expectedValue, result.get(5, 5));
    }

    @Test
    public void testApply_GrayImage_ROI() {
        GrayImage image = new GrayImage(10, 10);
        image.set(5, 5, 100);

        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        GrayImage result = clip.apply(image, roi);

        int expectedValue = 0;
        assertEquals(expectedValue, result.get(5, 5));
    }

    @Test
    public void testApply_RealGrayImage() {
        RealGrayImage image = new RealGrayImage(10, 10);
        image.set(5, 5, 100.0f);

        RealGrayImage result = clip.apply(image);

        float expectedValue = 0.0f;
        assertEquals(Float.compare(expectedValue, result.get(5, 5)), 0);
    }

    @Test
    public void testApply_RealGrayImage_ROI() {
        RealGrayImage image = new RealGrayImage(10, 10);
        image.set(5, 5, 100.0f);

        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        RealGrayImage result = clip.apply(image, roi);

        float expectedValue = 0.0f;
        assertEquals(Float.compare(expectedValue, result.get(5, 5)), 0);
    }

    @Test
    public void testApply_ColorImage() {
        ColorImage image = new ColorImage(10, 10);
        image.setPlane(0, new GrayImage(10, 10));
        image.setPlane(1, new GrayImage(10, 10));
        image.setPlane(2, new GrayImage(10, 10));
        image.getPlane(0).set(5, 5, 100);
        image.getPlane(1).set(5, 5, 100);
        image.getPlane(2).set(5, 5, 100);

        ColorImage result = clip.apply(image);

        int expectedValue = 0;
        assertEquals(expectedValue, result.getPlane(0).get(5, 5));
        assertEquals(expectedValue, result.getPlane(1).get(5, 5));
        assertEquals(expectedValue, result.getPlane(2).get(5, 5));
    }

    @Test
    public void testApply_ColorImage_ROI() {
        ColorImage image = new ColorImage(10, 10);
        image.setPlane(0, new GrayImage(10, 10));
        image.setPlane(1, new GrayImage(10, 10));
        image.setPlane(2, new GrayImage(10, 10));
        image.getPlane(0).set(5, 5, 100);
        image.getPlane(1).set(5, 5, 100);
        image.getPlane(2).set(5, 5, 100);

        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        ColorImage result = clip.apply(image, roi);

        int expectedValue = 0;
        assertEquals(expectedValue, result.getPlane(0).get(5, 5));
        assertEquals(expectedValue, result.getPlane(1).get(5, 5));
        assertEquals(expectedValue, result.getPlane(2).get(5, 5));
    }

    @Test
    public void testApply_RealColorImage() {
        RealColorImage image = new RealColorImage(10, 10);
        image.setPlane(0, new RealGrayImage(10, 10));
        image.setPlane(1, new RealGrayImage(10, 10));
        image.setPlane(2, new RealGrayImage(10, 10));
        image.getPlane(0).set(5, 5, 100.0f);
        image.getPlane(1).set(5, 5, 100.0f);
        image.getPlane(2).set(5, 5, 100.0f);

        RealColorImage result = clip.apply(image);

        float expectedValue = 0.0f;
        assertEquals(Float.compare(expectedValue, result.getPlane(0).get(5, 5)), 0);
        assertEquals(Float.compare(expectedValue, result.getPlane(1).get(5, 5)), 0);
        assertEquals(Float.compare(expectedValue, result.getPlane(2).get(5, 5)), 0);
    }

    @Test
    public void testApply_RealColorImage_ROI() {
        RealColorImage image = new RealColorImage(10, 10);
        image.setPlane(0, new RealGrayImage(10, 10));
        image.setPlane(1, new RealGrayImage(10, 10));
        image.setPlane(2, new RealGrayImage(10, 10));
        image.getPlane(0).set(5, 5, 100.0f);
        image.getPlane(1).set(5, 5, 100.0f);
        image.getPlane(2).set(5, 5, 100.0f);

        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        RealColorImage result = clip.apply(image, roi);

        float expectedValue = 0.0f;
        assertEquals(Float.compare(expectedValue, result.getPlane(0).get(5, 5)), 0);
        assertEquals(Float.compare(expectedValue, result.getPlane(1).get(5, 5)), 0);
        assertEquals(Float.compare(expectedValue, result.getPlane(2).get(5, 5)), 0);
    }
}