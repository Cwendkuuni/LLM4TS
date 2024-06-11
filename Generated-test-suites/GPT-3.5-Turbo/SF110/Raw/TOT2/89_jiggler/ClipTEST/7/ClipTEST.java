import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

    @Test
    public void testApplyGrayImage() {
        GrayImage grayImage = new GrayImage(5, 5);
        Clip clip = new Clip(0, 255);
        Image result = clip.apply(grayImage);
        assertNotNull(result);
        assertEquals(grayImage, result);
    }

    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage realGrayImage = new RealGrayImage(5, 5);
        Clip clip = new Clip(0.0f, 1.0f);
        Image result = clip.apply(realGrayImage);
        assertNotNull(result);
        assertEquals(realGrayImage, result);
    }

    @Test
    public void testApplyColorImage() {
        ColorImage colorImage = new ColorImage(5, 5);
        Clip clip = new Clip(0, 255);
        Image result = clip.apply(colorImage);
        assertNotNull(result);
        assertEquals(colorImage, result);
    }

    @Test
    public void testApplyRealColorImage() {
        RealColorImage realColorImage = new RealColorImage(5, 5);
        Clip clip = new Clip(0.0f, 1.0f);
        Image result = clip.apply(realColorImage);
        assertNotNull(result);
        assertEquals(realColorImage, result);
    }

    @Test
    public void testApplyWithRoi() {
        GrayImage grayImage = new GrayImage(5, 5);
        ROI roi = new ROI(0, 0, 3, 3);
        Clip clip = new Clip(0, 255);
        Image result = clip.apply(grayImage, roi);
        assertNotNull(result);
        assertEquals(grayImage, result);
    }

    @Test
    public void testApplyWithInvalidRoi() {
        GrayImage grayImage = new GrayImage(5, 5);
        ROI roi = new ROI(5, 5, 10, 10);
        Clip clip = new Clip(0, 255);
        Image result = clip.apply(grayImage, roi);
        assertNotNull(result);
        assertEquals(grayImage, result);
    }

    @Test
    public void testApplyWithZeroMinMax() {
        GrayImage grayImage = new GrayImage(5, 5);
        Clip clip = new Clip(0, 0);
        Image result = clip.apply(grayImage);
        assertNotNull(result);
        assertEquals(grayImage, result);
    }

    @Test
    public void testApplyRealColorImageWithRoi() {
        RealColorImage realColorImage = new RealColorImage(5, 5);
        ROI roi = new ROI(0, 0, 3, 3);
        Clip clip = new Clip(0.0f, 1.0f);
        Image result = clip.apply(realColorImage, roi);
        assertNotNull(result);
        assertEquals(realColorImage, result);
    }

    @Test
    public void testApplyWithException() {
        GrayImage grayImage = new GrayImage(5, 5);
        Clip clip = new Clip(0, 255);
        try {
            clip.apply(null); // passing null image should throw an exception
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

}