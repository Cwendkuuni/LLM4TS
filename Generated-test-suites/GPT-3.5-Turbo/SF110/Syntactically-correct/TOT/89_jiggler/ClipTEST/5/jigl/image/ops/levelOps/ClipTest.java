import org.junit.Before;
import org.junit.Test;
import jigl.image.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ClipTest {

    private Clip clip;

    @Before
    public void setup() {
        clip = new Clip(0, 255);
    }

    @Test
    public void testApply_GrayImage() {
        GrayImage inputImage = new GrayImage(10, 10);
        GrayImage outputImage = (GrayImage) clip.apply(inputImage);

        assertNotNull(outputImage);
        assertEquals(inputImage.X(), outputImage.X());
        assertEquals(inputImage.Y(), outputImage.Y());
    }

    @Test
    public void testApply_GrayImageWithROI() {
        GrayImage inputImage = new GrayImage(10, 10);
        ROI roi = new ROI(2, 2, 7, 7);
        GrayImage outputImage = (GrayImage) clip.apply(inputImage, roi);

        assertNotNull(outputImage);
        assertEquals(roi.lx() - roi.ux() + 1, outputImage.X());
        assertEquals(roi.ly() - roi.uy() + 1, outputImage.Y());
    }

    @Test
    public void testApply_RealGrayImage() {
        RealGrayImage inputImage = new RealGrayImage(10, 10);
        RealGrayImage outputImage = (RealGrayImage) clip.apply(inputImage);

        assertNotNull(outputImage);
        assertEquals(inputImage.X(), outputImage.X());
        assertEquals(inputImage.Y(), outputImage.Y());
    }

    @Test
    public void testApply_RealGrayImageWithROI() {
        RealGrayImage inputImage = new RealGrayImage(10, 10);
        ROI roi = new ROI(2, 2, 7, 7);
        RealGrayImage outputImage = (RealGrayImage) clip.apply(inputImage, roi);

        assertNotNull(outputImage);
        assertEquals(roi.lx() - roi.ux() + 1, outputImage.X());
        assertEquals(roi.ly() - roi.uy() + 1, outputImage.Y());
    }

    @Test
    public void testApply_ColorImage() {
        ColorImage inputImage = new ColorImage(10, 10);
        ColorImage outputImage = (ColorImage) clip.apply(inputImage);

        assertNotNull(outputImage);
        assertEquals(inputImage.X(), outputImage.X());
        assertEquals(inputImage.Y(), outputImage.Y());
    }

    @Test
    public void testApply_ColorImageWithROI() {
        ColorImage inputImage = new ColorImage(10, 10);
        ROI roi = new ROI(2, 2, 7, 7);
        ColorImage outputImage = (ColorImage) clip.apply(inputImage, roi);

        assertNotNull(outputImage);
        assertEquals(roi.lx() - roi.ux() + 1, outputImage.X());
        assertEquals(roi.ly() - roi.uy() + 1, outputImage.Y());
    }

    @Test
    public void testApply_RealColorImage() {
        RealColorImage inputImage = new RealColorImage(10, 10);
        RealColorImage outputImage = (RealColorImage) clip.apply(inputImage);

        assertNotNull(outputImage);
        assertEquals(inputImage.X(), outputImage.X());
        assertEquals(inputImage.Y(), outputImage.Y());
    }

    @Test
    public void testApply_RealColorImageWithROI() {
        RealColorImage inputImage = new RealColorImage(10, 10);
        ROI roi = new ROI(2, 2, 7, 7);
        RealColorImage outputImage = (RealColorImage) clip.apply(inputImage, roi);

        assertNotNull(outputImage);
        assertEquals(roi.lx() - roi.ux() + 1, outputImage.X());
        assertEquals(roi.ly() - roi.uy() + 1, outputImage.Y());
    }

    @Test
    public void testMain() {
        // TODO: provide appropriate test case for main method
    }
}