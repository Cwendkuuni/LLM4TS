
import jigl.image.*;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.levelOps.GaussianSmooth;
import jigl.image.types.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClipTest {
    private Clip clip;
    private GrayImage grayImage;
    private RealGrayImage realGrayImage;
    private ColorImage colorImage;
    private RealColorImage realColorImage;

    @Before
    public void setUp() {
        clip = new Clip(0, 255);

        // Create sample images for testing
        int width = 10;
        int height = 10;
        grayImage = new GrayImage(width, height);
        realGrayImage = new RealGrayImage(width, height);
        colorImage = new ColorImage(width, height);
        realColorImage = new RealColorImage(width, height);

        // Initialize sample images with test data
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = x + y;
                grayImage.set(x, y, value);
                realGrayImage.set(x, y, value);
                colorImage.set(x, y, value, value, value);
                realColorImage.set(x, y, value, value, value);
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
    public void testApply_GrayImage() {
        GrayImage expected = new GrayImage(10, 10);
        expected.set(0, 0, 0);
        expected.set(1, 1, 1);
        expected.set(2, 2, 2);
        expected.set(3, 3, 3);
        expected.set(4, 4, 4);
        expected.set(5, 5, 5);
        expected.set(6, 6, 6);
        expected.set(7, 7, 7);
        expected.set(8, 8, 8);
        expected.set(9, 9, 9);

        Image result = clip.apply(grayImage);

        assertEquals(expected, result);
    }

    @Test
    public void testApply_GrayImageWithROI() {
        ROI roi = new ROI(1, 1, 8, 8);
        GrayImage expected = new GrayImage(10, 10);
        expected.set(0, 0, 0);
        expected.set(1, 1, 1);
        expected.set(2, 2, 2);
        expected.set(3, 3, 3);
        expected.set(4, 4, 4);
        expected.set(5, 5, 5);
        expected.set(6, 6, 6);
        expected.set(7, 7, 7);
        expected.set(8, 8, 8);
        expected.set(9, 9, 9);

        Image result = clip.apply(grayImage, roi);

        assertEquals(expected, result);
    }

    @Test
    public void testApply_RealGrayImage() {
        RealGrayImage expected = new RealGrayImage(10, 10);
        expected.set(0, 0, 0.0f);
        expected.set(1, 1, 1.0f);
        expected.set(2, 2, 2.0f);
        expected.set(3, 3, 3.0f);
        expected.set(4, 4, 4.0f);
        expected.set(5, 5, 5.0f);
        expected.set(6, 6, 6.0f);
        expected.set(7, 7, 7.0f);
        expected.set(8, 8, 8.0f);
        expected.set(9, 9, 9.0f);

        Image result = clip.apply(realGrayImage);

        assertEquals(expected, result);
    }

    @Test
    public void testApply_RealGrayImageWithROI() {
        ROI roi = new ROI(1, 1, 8, 8);
        RealGrayImage expected = new RealGrayImage(10, 10);
        expected.set(0, 0, 0.0f);
        expected.set(1, 1, 1.0f);
        expected.set(2, 2, 2.0f);
        expected.set(3, 3, 3.0f);
        expected.set(4, 4, 4.0f);
        expected.set(5, 5, 5.0f);
        expected.set(6, 6, 6.0f);
        expected.set(7, 7, 7.0f);
        expected.set(8, 8, 8.0f);
        expected.set(9, 9, 9.0f);

        Image result = clip.apply(realGrayImage, roi);

        assertEquals(expected, result);
    }

    @Test
    public void testApply_ColorImage() {
        ColorImage expected = new ColorImage(10, 10);
        expected.set(0, 0, 0, 0, 0);
        expected.set(1, 1, 1, 1, 1);
        expected.set(2, 2, 2, 2, 2);
        expected.set(3, 3, 3, 3, 3);
        expected.set(4, 4, 4, 4, 4);
        expected.set(5, 5, 5, 5, 5);
        expected.set(6, 6, 6, 6, 6);
        expected.set(7, 7, 7, 7, 7);
        expected.set(8, 8, 8, 8, 8);
        expected.set(9, 9, 9, 9, 9);

        Image result = clip.apply(colorImage);

        assertEquals(expected, result);
    }

    @Test
    public void testApply_ColorImageWithROI() {
        ROI roi = new ROI(1, 1, 8, 8);
        ColorImage expected = new ColorImage(10, 10);
        expected.set(0, 0, 0, 0, 0);
        expected.set(1, 1, 1, 1, 1);
        expected.set(2, 2, 2, 2, 2);
        expected.set(3, 3, 3, 3, 3);
        expected.set(4, 4, 4, 4, 4);
        expected.set(5, 5, 5, 5, 5);
        expected.set(6, 6, 6, 6, 6);
        expected.set(7, 7, 7, 7, 7);
        expected.set(8, 8, 8, 8, 8);
        expected.set(9, 9, 9, 9, 9);

        Image result = clip.apply(colorImage, roi);

        assertEquals(expected, result);
    }

    @Test
    public void testApply_RealColorImage() {
        RealColorImage expected = new RealColorImage(10, 10);
        expected.set(0, 0, 0.0f, 0.0f, 0.0f);
        expected.set(1, 1, 1.0f, 1.0f, 1.0f);
        expected.set(2, 2, 2.0f, 2.0f, 2.0f);
        expected.set(3, 3, 3.0f, 3.0f, 3.0f);
        expected.set(4, 4, 4.0f, 4.0f, 4.0f);
        expected.set(5, 5, 5.0f, 5.0f, 5.0f);
        expected.set(6, 6, 6.0f, 6.0f, 6.0f);
        expected.set(7, 7, 7.0f, 7.0f, 7.0f);
        expected.set(8, 8, 8.0f, 8.0f, 8.0f);
        expected.set(9, 9, 9.0f, 9.0f, 9.0f);

        Image result = clip.apply(realColorImage);

        assertEquals(expected, result);
    }

    @Test
    public void testApply_RealColorImageWithROI() {
        ROI roi = new ROI(1, 1, 8, 8);
        RealColorImage expected = new RealColorImage(10, 10);
        expected.set(0, 0, 0.0f, 0.0f, 0.0f);
        expected.set(1, 1, 1.0f, 1.0f, 1.0f);
        expected.set(2, 2, 2.0f, 2.0f, 2.0f);
        expected.set(3, 3, 3.0f, 3.0f, 3.0f);
        expected.set(4, 4, 4.0f, 4.0f, 4.0f);
        expected.set(5, 5, 5.0f, 5.0f, 5.0f);
        expected.set(6, 6, 6.0f, 6.0f, 6.0f);
        expected.set(7, 7, 7.0f, 7.0f, 7.0f);
        expected.set(8, 8, 8.0f, 8.0f, 8.0f);
        expected.set(9, 9, 9.0f, 9.0f, 9.0f);

        Image result = clip.apply(realColorImage, roi);

        assertEquals(expected, result);
    }

    // Additional test cases for potential edge cases and exceptions
    // ...

    // Additional test cases for potential edge cases and exceptions
    // ...

    // Additional test cases for potential edge cases and exceptions
    // ...

}
