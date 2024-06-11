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
        realGrayImage = new RealGrayImage(10, 10);
        colorImage = new ColorImage(10, 10);
        realColorImage = new RealColorImage(10, 10);
    }

    @Test
    public void testApply_GrayImage() {
        // Typical case
        grayImage.set(5, 5, 300);
        Image result = clip.apply(grayImage);
        assertEquals(255, result.get(5, 5));

        // Edge cases
        grayImage.set(5, 5, 200);
        result = clip.apply(grayImage);
        assertEquals(200, result.get(5, 5));

        grayImage.set(5, 5, 0);
        result = clip.apply(grayImage);
        assertEquals(0, result.get(5, 5));

        // Error case
        grayImage.set(-1, 5, 100);
        result = clip.apply(grayImage);
        assertNull(result);
    }

    @Test
    public void testApply_GrayImageWithROI() {
        // Typical case
        grayImage.set(5, 5, 300);
        ROI roi = new ROI(0, 0, 9, 9);
        Image result = clip.apply(grayImage, roi);
        assertEquals(255, result.get(5, 5));

        // Edge cases
        grayImage.set(5, 5, 200);
        result = clip.apply(grayImage, roi);
        assertEquals(200, result.get(5, 5));

        grayImage.set(5, 5, 0);
        result = clip.apply(grayImage, roi);
        assertEquals(0, result.get(5, 5));

        // Error case
        grayImage.set(-1, 5, 100);
        result = clip.apply(grayImage, roi);
        assertNull(result);
    }

    @Test
    public void testApply_RealGrayImage() {
        // Typical case
        realGrayImage.set(5, 5, 300.5f);
        Image result = clip.apply(realGrayImage);
        assertEquals(255.0f, result.get(5, 5));

        // Edge cases
        realGrayImage.set(5, 5, 200.5f);
        result = clip.apply(realGrayImage);
        assertEquals(200.5f, result.get(5, 5));

        realGrayImage.set(5, 5, 0.5f);
        result = clip.apply(realGrayImage);
        assertEquals(0.5f, result.get(5, 5));

        // Error case
        realGrayImage.set(5, 5, -0.5f);
        result = clip.apply(realGrayImage);
        assertNull(result);
    }

    @Test
    public void testApply_RealGrayImageWithROI() {
        // Typical case
        realGrayImage.set(5, 5, 300.5f);
        ROI roi = new ROI(0, 0, 9, 9);
        Image result = clip.apply(realGrayImage, roi);
        assertEquals(255.0f, result.get(5, 5));

        // Edge cases
        realGrayImage.set(5, 5, 200.5f);
        result = clip.apply(realGrayImage, roi);
        assertEquals(200.5f, result.get(5, 5));

        realGrayImage.set(5, 5, 0.5f);
        result = clip.apply(realGrayImage, roi);
        assertEquals(0.5f, result.get(5, 5));

        // Error case
        realGrayImage.set(5, 5, -0.5f);
        result = clip.apply(realGrayImage, roi);
        assertNull(result);
    }

    @Test
    public void testApply_ColorImage() {
        // Typical case
        colorImage.set(5, 5, 1, 300);
        Image result = clip.apply(colorImage);
        assertEquals(255, ((ColorImage)result).get(5, 5, 1));

        // Edge cases
        colorImage.set(5, 5, 1, 200);
        result = clip.apply(colorImage);
        assertEquals(200, ((ColorImage)result).get(5, 5, 1));

        colorImage.set(5, 5, 1, 0);
        result = clip.apply(colorImage);
        assertEquals(0, ((ColorImage)result).get(5, 5, 1));

        // Error case
        colorImage.set(-1, 5, 5, 100);
        result = clip.apply(colorImage);
        assertNull(result);
    }

    @Test
    public void testApply_ColorImageWithROI() {
        // Typical case
        colorImage.set(5, 5, 1, 300);
        ROI roi = new ROI(0, 0, 9, 9);
        Image result = clip.apply(colorImage, roi);
        assertEquals(255, ((ColorImage)result).get(5, 5, 1));

        // Edge cases
        colorImage.set(5, 5, 1, 200);
        result = clip.apply(colorImage, roi);
        assertEquals(200, ((ColorImage)result).get(5, 5, 1));

        colorImage.set(5, 5, 1, 0);
        result = clip.apply(colorImage, roi);
        assertEquals(0, ((ColorImage)result).get(5, 5, 1));

        // Error case
        colorImage.set(-1, 5, 5, 100);
        result = clip.apply(colorImage, roi);
        assertNull(result);
    }

    @Test
    public void testApply_RealColorImage() {
        // Typical case
        realColorImage.set(5, 5, 1, 300.5f);
        Image result = clip.apply(realColorImage);
        assertEquals(255.0f, ((RealColorImage)result).get(5, 5, 1));

        // Edge cases
        realColorImage.set(5, 5, 1, 200.5f);
        result = clip.apply(realColorImage);
        assertEquals(200.5f, ((RealColorImage)result).get(5, 5, 1));

        realColorImage.set(5, 5, 1, 0.5f);
        result = clip.apply(realColorImage);
        assertEquals(0.5f, ((RealColorImage)result).get(5, 5, 1));

        // Error case
        realColorImage.set(5, 5, 1, -0.5f);
        result = clip.apply(realColorImage);
        assertNull(result);
    }

    @Test
    public void testApply_RealColorImageWithROI() {
        // Typical case
        realColorImage.set(5, 5, 1, 300.5f);
        ROI roi = new ROI(0, 0, 9, 9);
        Image result = clip.apply(realColorImage, roi);
        assertEquals(255.0f, ((RealColorImage)result).get(5, 5, 1));

        // Edge cases
        realColorImage.set(5, 5, 1, 200.5f);
        result = clip.apply(realColorImage, roi);
        assertEquals(200.5f, ((RealColorImage)result).get(5, 5, 1));

        realColorImage.set(5, 5, 1, 0.5f);
        result = clip.apply(realColorImage, roi);
        assertEquals(0.5f, ((RealColorImage)result).get(5, 5, 1));

        // Error case
        realColorImage.set(5, 5, 1, -0.5f);
        result = clip.apply(realColorImage, roi);
        assertNull(result);
    }

    @Test
    public void testMain() {
        // Test case with valid input
        String[] args = {"0.5", "0.8", "inputfile.jpg", "outputfile.jpg"};
        Clip.main(args);

        // Test case with invalid input
        String[] args2 = {"0.5", "-0.8", "inputfile.jpg", "outputfile.jpg"};
        Clip.main(args2);
    }
}