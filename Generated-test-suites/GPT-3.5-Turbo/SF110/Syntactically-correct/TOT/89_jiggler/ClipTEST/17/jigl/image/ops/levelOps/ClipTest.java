import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.*;
import jigl.image.ops.levelOps.*;

public class ClipTest {

    // Testing Clip(int, int) constructor

    @Test
    public void testConstructorInt() {
        Clip clip = new Clip(5, 10);
        assertEquals(5, clip.int_min);
        assertEquals(10, clip.int_max);
    }

    // Testing Clip(float, float) constructor

    @Test
    public void testConstructorFloat() {
        Clip clip = new Clip(2.5f, 7.5f);
        assertEquals(2.5f, clip.float_min, 0.01);
        assertEquals(7.5f, clip.float_max, 0.01);
    }

    // Testing apply(GrayImage) method

    @Test
    public void testApplyGrayImage() {
        GrayImage image = new GrayImage(4, 4);
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, 10);
            }
        }
        Clip clip = new Clip(5, 8);
        Image result = clip.apply(image);
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                int value = result.get(x, y);
                assertTrue(value >= 5 && value <= 8);
            }
        }
    }

    // Testing apply(GrayImage, ROI) method

    @Test
    public void testApplyGrayImageROI() {
        GrayImage image = new GrayImage(4, 4, 2);
        ROI roi = new ROI(1, 1, 2, 2);
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                image.set(x, y, 10);
            }
        }
        Clip clip = new Clip(5, 8);
        Image result = clip.apply(image, roi);
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                int value = result.get(x, y);
                assertTrue(value >= 5 && value <= 8);
            }
        }
    }

    // Testing apply(RealGrayImage) method

    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage image = new RealGrayImage(4, 4);
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, 10.5f);
            }
        }
        Clip clip = new Clip(5, 8);
        Image result = clip.apply(image);
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                float value = result.get(x, y);
                assertTrue(value >= 5 && value <= 8);
            }
        }
    }

    // Testing apply(RealGrayImage, ROI) method

    @Test
    public void testApplyRealGrayImageROI() {
        RealGrayImage image = new RealGrayImage(4, 4, 2);
        ROI roi = new ROI(1, 1, 2, 2);
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                image.set(x, y, 10.5f);
            }
        }
        Clip clip = new Clip(5, 8);
        Image result = clip.apply(image, roi);
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                float value = result.get(x, y);
                assertTrue(value >= 5 && value <= 8);
            }
        }
    }

    // Testing apply(ColorImage) method

    @Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage(4, 4);
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, 10, 10, 10);
            }
        }
        Clip clip = new Clip(5, 8);
        Image result = clip.apply(image);
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                int r = result.getPlane(0).get(x, y);
                int g = result.getPlane(1).get(x, y);
                int b = result.getPlane(2).get(x, y);
                assertTrue(r >= 5 && r <= 8);
                assertTrue(g >= 5 && g <= 8);
                assertTrue(b >= 5 && b <= 8);
            }
        }
    }

    // Testing apply(ColorImage, ROI) method

    @Test
    public void testApplyColorImageROI() {
        ColorImage image = new ColorImage(4, 4, 2);
        ROI roi = new ROI(1, 1, 2, 2);
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                image.set(x, y, 10, 10, 10);
            }
        }
        Clip clip = new Clip(5, 8);
        Image result = clip.apply(image, roi);
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                int r = result.getPlane(0).get(x, y);
                int g = result.getPlane(1).get(x, y);
                int b = result.getPlane(2).get(x, y);
                assertTrue(r >= 5 && r <= 8);
                assertTrue(g >= 5 && g <= 8);
                assertTrue(b >= 5 && b <= 8);
            }
        }
    }

    // Testing apply(RealColorImage) method

    @Test
    public void testApplyRealColorImage() {
        RealColorImage image = new RealColorImage(4, 4);
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, 10.5f, 10.5f, 10.5f);
            }
        }
        Clip clip = new Clip(5, 8);
        Image result = clip.apply(image);
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                float r = result.getPlane(0).get(x, y);
                float g = result.getPlane(1).get(x, y);
                float b = result.getPlane(2).get(x, y);
                assertTrue(r >= 5 && r <= 8);
                assertTrue(g >= 5 && g <= 8);
                assertTrue(b >= 5 && b <= 8);
            }
        }
    }

    // Testing apply(RealColorImage, ROI) method

    @Test
    public void testApplyRealColorImageROI() {
        RealColorImage image = new RealColorImage(4, 4, 2);
        ROI roi = new ROI(1, 1, 2, 2);
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                image.set(x, y, 10.5f, 10.5f, 10.5f);
            }
        }
        Clip clip = new Clip(5, 8);
        Image result = clip.apply(image, roi);
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                float r = result.getPlane(0).get(x, y);
                float g = result.getPlane(1).get(x, y);
                float b = result.getPlane(2).get(x, y);
                assertTrue(r >= 5 && r <= 8);
                assertTrue(g >= 5 && g <= 8);
                assertTrue(b >= 5 && b <= 8);
            }
        }
    }

}