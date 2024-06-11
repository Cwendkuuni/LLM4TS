import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClipTest {

    private Clip clip;
    private GrayImage grayImage;
    private RealGrayImage realGrayImage;
    private ColorImage colorImage;
    private RealColorImage realColorImage;

    @Before
    public void setUp() {
        clip = new Clip(0, 255);
        grayImage = new GrayImage(10, 10);
        for (int y = 0; y < grayImage.Y(); y++) {
            for (int x = 0; x < grayImage.X(); x++) {
                grayImage.set(x, y, 100);
            }
        }
        realGrayImage = new RealGrayImage(10, 10);
        for (int y = 0; y < realGrayImage.Y(); y++) {
            for (int x = 0; x < realGrayImage.X(); x++) {
                realGrayImage.set(x, y, 100.0f);
            }
        }
        colorImage = new ColorImage(10, 10);
        for (int y = 0; y < colorImage.Y(); y++) {
            for (int x = 0; x < colorImage.X(); x++) {
                colorImage.set(x, y, new int[] {100, 100, 100});
            }
        }
        realColorImage = new RealColorImage(10, 10);
        for (int y = 0; y < realColorImage.Y(); y++) {
            for (int x = 0; x < realColorImage.X(); x++) {
                realColorImage.set(x, y, new float[] {100.0f, 100.0f, 100.0f});
            }
        }
    }

    @After
    public void tearDown() {
        clip = null;
        grayImage = null;
        realGrayImage = null;
        colorImage = null;
        realColorImage = null;
    }

    @Test
    public void testApplyGrayImage() {
        GrayImage result = (GrayImage)clip.apply(grayImage);
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                assertEquals(100, result.get(x, y));
            }
        }
    }

    @Test
    public void testApplyGrayImageWithROI() {
        ROI roi = new ROI(2, 2, 6, 6);
        GrayImage result = (GrayImage)clip.apply(grayImage, roi);
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                assertEquals(100, result.get(x, y));
            }
        }
    }

    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage result = (RealGrayImage)clip.apply(realGrayImage);
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                assertEquals(100.0f, result.get(x, y), 0.001);
            }
        }
    }

    @Test
    public void testApplyRealGrayImageWithROI() {
        ROI roi = new ROI(2, 2, 6, 6);
        RealGrayImage result = (RealGrayImage)clip.apply(realGrayImage, roi);
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                assertEquals(100.0f, result.get(x, y), 0.001);
            }
        }
    }

    @Test
    public void testApplyColorImage() {
        ColorImage result = (ColorImage)clip.apply(colorImage);
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                int[] expected = {100, 100, 100};
                assertArrayEquals(expected, result.get(x, y));
            }
        }
    }

    @Test
    public void testApplyColorImageWithROI() {
        ROI roi = new ROI(2, 2, 6, 6);
        ColorImage result = (ColorImage)clip.apply(colorImage, roi);
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                int[] expected = {100, 100, 100};
                assertArrayEquals(expected, result.get(x, y));
            }
        }
    }

    @Test
    public void testApplyRealColorImage() {
        RealColorImage result = (RealColorImage)clip.apply(realColorImage);
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                float[] expected = {100.0f, 100.0f, 100.0f};
                assertArrayEquals(expected, result.get(x, y), 0.001);
            }
        }
    }

    @Test
    public void testApplyRealColorImageWithROI() {
        ROI roi = new ROI(2, 2, 6, 6);
        RealColorImage result = (RealColorImage)clip.apply(realColorImage, roi);
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                float[] expected = {100.0f, 100.0f, 100.0f};
                assertArrayEquals(expected, result.get(x, y), 0.001);
            }
        }
    }

}