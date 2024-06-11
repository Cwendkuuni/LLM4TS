import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClipTest {

    private Clip clip;

    @Before
    public void setUp() {
        clip = new Clip(0, 255);
    }

    @Test
    public void testApply_withGrayImageAndNoROI_shouldReturnImage() {
        GrayImage image = new GrayImage();
        Image result = clip.apply(image);
        assertNotNull(result);
        assertEquals(image, result);
    }

    @Test
    public void testApply_withGrayImageAndROI_shouldReturnImage() {
        GrayImage image = new GrayImage();
        ROI roi = new ROI(0, 0, 10, 10);
        Image result = clip.apply(image, roi);
        assertNotNull(result);
        assertEquals(image, result);
    }

    @Test
    public void testApply_withRealGrayImageAndNoROI_shouldReturnImage() {
        RealGrayImage image = new RealGrayImage();
        Image result = clip.apply(image);
        assertNotNull(result);
        assertEquals(image, result);
    }

    @Test
    public void testApply_withRealGrayImageAndROI_shouldReturnImage() {
        RealGrayImage image = new RealGrayImage();
        ROI roi = new ROI(0, 0, 10, 10);
        Image result = clip.apply(image, roi);
        assertNotNull(result);
        assertEquals(image, result);
    }

    @Test
    public void testApply_withColorImageAndNoROI_shouldReturnImage() {
        ColorImage image = new ColorImage();
        Image result = clip.apply(image);
        assertNotNull(result);
        assertEquals(image, result);
    }

    @Test
    public void testApply_withColorImageAndROI_shouldReturnImage() {
        ColorImage image = new ColorImage();
        ROI roi = new ROI(0, 0, 10, 10);
        Image result = clip.apply(image, roi);
        assertNotNull(result);
        assertEquals(image, result);
    }

    @Test
    public void testApply_withRealColorImageAndNoROI_shouldReturnImage() {
        RealColorImage image = new RealColorImage();
        Image result = clip.apply(image);
        assertNotNull(result);
        assertEquals(image, result);
    }

    @Test
    public void testApply_withRealColorImageAndROI_shouldReturnImage() {
        RealColorImage image = new RealColorImage();
        ROI roi = new ROI(0, 0, 10, 10);
        Image result = clip.apply(image, roi);
        assertNotNull(result);
        assertEquals(image, result);
    }
}