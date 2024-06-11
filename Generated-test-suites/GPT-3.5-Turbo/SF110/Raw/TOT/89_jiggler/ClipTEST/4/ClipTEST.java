import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClipTest {

    private static Clip clip;

    @BeforeClass
    public static void setUp() {
        clip = new Clip(0, 255);
    }

    @Test
    public void testApply_GrayImage_NoROI() {
        // Typical use case
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 100);
        image.set(1, 1, 200);
        image.set(2, 2, 300); // Out of range value

        Image result = clip.apply(image);

        assertEquals(100, result.get(0, 0));
        assertEquals(200, result.get(1, 1));
        assertEquals(255, result.get(2, 2));
    }

    @Test
    public void testApply_GrayImage_WithROI() {
        // Edge case
        GrayImage image = new GrayImage(5, 5);
        ROI roi = new ROI(1, 1, 3, 3);
        image.set(0, 0, 100);
        image.set(1, 1, 200);
        image.set(2, 2, 300); // Out of range value
        image.set(3, 3, 0); // Minimum value
        image.set(4, 4, 255); // Maximum value

        Image result = clip.apply(image, roi);

        assertEquals(0, result.get(0, 0));
        assertEquals(200, result.get(1, 1));
        assertEquals(255, result.get(2, 2));
        assertEquals(0, result.get(3, 3));
        assertEquals(255, result.get(4, 4));
    }

    @Test
    public void testApply_RealGrayImage_NoROI() {
        // Typical use case
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 0.5f);
        image.set(1, 1, 1.5f);
        image.set(2, 2, 2.5f); // Out of range value

        Image result = clip.apply(image);

        assertEquals(0.5f, result.get(0, 0), 0.01);
        assertEquals(1.5f, result.get(1, 1), 0.01);
        assertEquals(2.0f, result.get(2, 2), 0.01);
    }

    @Test
    public void testApply_RealGrayImage_WithROI() {
        // Edge case
        RealGrayImage image = new RealGrayImage(5, 5);
        ROI roi = new ROI(1, 1, 3, 3);
        image.set(0, 0, 0.5f);
        image.set(1, 1, 1.5f);
        image.set(2, 2, 2.5f); // Out of range value
        image.set(3, 3, 0.0f); // Minimum value
        image.set(4, 4, 1.0f); // Maximum value

        Image result = clip.apply(image, roi);

        assertEquals(0.0f, result.get(0, 0), 0.01);
        assertEquals(1.5f, result.get(1, 1), 0.01);
        assertEquals(2.0f, result.get(2, 2), 0.01);
        assertEquals(0.0f, result.get(3, 3), 0.01);
        assertEquals(1.0f, result.get(4, 4), 0.01);
    }

    @Test
    public void testApply_ColorImage_NoROI() {
        // Typical use case
        ColorImage image = new ColorImage(3, 3);
        image.set(0, 0, new RGB(100, 0, 0));
        image.set(1, 1, new RGB(0, 100, 0));
        image.set(2, 2, new RGB(0, 0, 100)); // Out of range value

        Image result = clip.apply(image);

        assertEquals(new RGB(100, 0, 0), result.get(0, 0));
        assertEquals(new RGB(0, 100, 0), result.get(1, 1));
        assertEquals(new RGB(0, 0, 255), result.get(2, 2));
    }

    @Test
    public void testApply_ColorImage_WithROI() {
        // Edge case
        ColorImage image = new ColorImage(5, 5);
        ROI roi = new ROI(1, 1, 3, 3);
        image.set(0, 0, new RGB(100, 0, 0));
        image.set(1, 1, new RGB(0, 100, 0));
        image.set(2, 2, new RGB(0, 0, 100)); // Out of range value
        image.set(3, 3, new RGB(0, 0, 0)); // Minimum value
        image.set(4, 4, new RGB(255, 255, 255)); // Maximum value

        Image result = clip.apply(image, roi);

        assertEquals(new RGB(0, 0, 0), result.get(0, 0));
        assertEquals(new RGB(0, 100, 0), result.get(1, 1));
        assertEquals(new RGB(0, 0, 255), result.get(2, 2));
        assertEquals(new RGB(0, 0, 0), result.get(3, 3));
        assertEquals(new RGB(255, 255, 255), result.get(4, 4));
    }

    @Test
    public void testApply_RealColorImage_NoROI() {
        // Typical use case
        RealColorImage image = new RealColorImage(3, 3);
        image.set(0, 0, new RealRGB(0.5f, 0.0f, 0.0f));
        image.set(1, 1, new RealRGB(0.0f, 0.5f, 0.0f));
        image.set(2, 2, new RealRGB(0.0f, 0.0f, 0.5f)); // Out of range value

        Image result = clip.apply(image);

        assertEquals(new RealRGB(0.5f, 0.0f, 0.0f), result.get(0, 0));
        assertEquals(new RealRGB(0.0f, 0.5f, 0.0f), result.get(1, 1));
        assertEquals(new RealRGB(0.0f, 0.0f, 0.5f), result.get(2, 2));
    }

    @Test
    public void testApply_RealColorImage_WithROI() {
        // Edge case
        RealColorImage image = new RealColorImage(5, 5);
        ROI roi = new ROI(1, 1, 3, 3);
        image.set(0, 0, new RealRGB(0.5f, 0.0f, 0.0f));
        image.set(1, 1, new RealRGB(0.0f, 0.5f, 0.0f));
        image.set(2, 2, new RealRGB(0.0f, 0.0f, 0.5f)); // Out of range value
        image.set(3, 3, new RealRGB(0.0f, 0.0f, 0.0f)); // Minimum value
        image.set(4, 4, new RealRGB(1.0f, 1.0f, 1.0f)); // Maximum value

        Image result = clip.apply(image, roi);

        assertEquals(new RealRGB(0.0f, 0.0f, 0.0f), result.get(0, 0));
        assertEquals(new RealRGB(0.0f, 0.5f, 0.0f), result.get(1, 1));
        assertEquals(new RealRGB(0.0f, 0.0f, 0.5f), result.get(2, 2));
        assertEquals(new RealRGB(0.0f, 0.0f, 0.0f), result.get(3, 3));
        assertEquals(new RealRGB(1.0f, 1.0f, 1.0f), result.get(4, 4));
    }
}