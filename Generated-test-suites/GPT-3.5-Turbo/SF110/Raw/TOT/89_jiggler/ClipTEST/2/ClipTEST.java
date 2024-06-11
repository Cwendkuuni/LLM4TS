import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class ClipTest {

    private Clip clip;

    @Before
    public void setup() {
        clip = new Clip(0, 255);
    }

    // Test for method: apply(GrayImage image)
    @Test
    public void testApplyGrayImage() {
        GrayImage image = new GrayImage(5, 5);
        image.fill(100);

        Image result = clip.apply(image);

        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                assertEquals(100, result.get(x, y));
            }
        }
    }

    // Test for method: apply(GrayImage image, ROI roi)
    @Test
    public void testApplyGrayImageWithROI() {
        GrayImage image = new GrayImage(5, 5);
        image.fill(100);
        ROI roi = new ROI(1, 1, 3, 3);

        Image result = clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                assertEquals(100, result.get(x, y));
            }
        }
    }

    // Test for method: apply(RealGrayImage image)
    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage image = new RealGrayImage(5, 5);
        image.fill(0.5f);

        Image result = clip.apply(image);

        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                assertEquals(0.5f, result.get(x, y));
            }
        }
    }

    // Test for method: apply(RealGrayImage image, ROI roi)
    @Test
    public void testApplyRealGrayImageWithROI() {
        RealGrayImage image = new RealGrayImage(5, 5);
        image.fill(0.5f);
        ROI roi = new ROI(1, 1, 3, 3);

        Image result = clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                assertEquals(0.5f, result.get(x, y));
            }
        }
    }

    // Test for method: apply(ColorImage image)
    @Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage(5, 5);
        image.fill(100);

        Image result = clip.apply(image);

        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                assertEquals(100, result.get(x, y));
            }
        }
    }

    // Test for method: apply(ColorImage image, ROI roi)
    @Test
    public void testApplyColorImageWithROI() {
        ColorImage image = new ColorImage(5, 5);
        image.fill(100);
        ROI roi = new ROI(1, 1, 3, 3);

        Image result = clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                assertEquals(100, result.get(x, y));
            }
        }
    }

    // Test for method: apply(RealColorImage image)
    @Test
    public void testApplyRealColorImage() {
        RealColorImage image = new RealColorImage(5, 5);
        image.fill(0.5f);

        Image result = clip.apply(image);

        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                assertEquals(0.5f, result.get(x, y));
            }
        }
    }

    // Test for method: apply(RealColorImage image, ROI roi)
    @Test
    public void testApplyRealColorImageWithROI() {
        RealColorImage image = new RealColorImage(5, 5);
        image.fill(0.5f);
        ROI roi = new ROI(1, 1, 3, 3);

        Image result = clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                assertEquals(0.5f, result.get(x, y));
            }
        }
    }
}