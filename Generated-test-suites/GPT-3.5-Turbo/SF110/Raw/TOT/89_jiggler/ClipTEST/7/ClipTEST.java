import org.junit.Test;
import static org.junit.Assert.*;

public class ClipTest {

    // Test cases for the constructor method

    @Test
    public void testConstructorWithIntParameters() {
        Clip clip = new Clip(-10, 10);
        assertEquals(-10, clip.int_min);
        assertEquals(10, clip.int_max);
    }

    @Test
    public void testConstructorWithFloatParameters() {
        Clip clip = new Clip(-10.5f, 10.5f);
        assertEquals(-10.5f, clip.float_min, 0.001);
        assertEquals(10.5f, clip.float_max, 0.001);
    }

    // Test cases for the apply method with GrayImage

    @Test
    public void testApplyWithGrayImage_noROI() {
        Clip clip = new Clip(-50, 50);
        GrayImage image = new GrayImage(3, 3);
        image.set(1, 1, 100);

        Image result = clip.apply(image);
        assertEquals(50, result.get(1, 1));
    }

    @Test
    public void testApplyWithGrayImage_withROI() {
        Clip clip = new Clip(-50, 50);
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 100);
        image.set(1, 1, -100);
        image.set(2, 2, 0);

        Image result = clip.apply(image, new ROI(0, 0, 2, 2));
        assertEquals(-50, result.get(1, 1));
    }

    // Test cases for the apply method with RealGrayImage

    @Test
    public void testApplyWithRealGrayImage_noROI() {
        Clip clip = new Clip(-50.5f, 50.5f);
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(1, 1, 100.5f);

        Image result = clip.apply(image);
        assertEquals(50.5f, result.get(1, 1), 0.001);
    }

    @Test
    public void testApplyWithRealGrayImage_withROI() {
        Clip clip = new Clip(-50.5f, 50.5f);
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 100.5f);
        image.set(1, 1, -100f);
        image.set(2, 2, 0.1f);

        Image result = clip.apply(image, new ROI(0, 0, 2, 2));
        assertEquals(-50.5f, result.get(1, 1), 0.001);
    }

    // Test cases for the apply method with ColorImage

    @Test
    public void testApplyWithColorImage_noROI() {
        Clip clip = new Clip(-100, 100);
        ColorImage image = new ColorImage(3, 3);
        image.set(1, 1, new RGBPixel(200, 200, 200));

        Image result = clip.apply(image);
        RGBPixel pixel = (RGBPixel) result.get(1, 1);
        assertEquals(100, pixel.getRed());
        assertEquals(100, pixel.getGreen());
        assertEquals(100, pixel.getBlue());
    }

    @Test
    public void testApplyWithColorImage_withROI() {
        Clip clip = new Clip(-100, 100);
        ColorImage image = new ColorImage(3, 3);
        image.set(0, 0, new RGBPixel(200, 200, 200));
        image.set(1, 1, new RGBPixel(0, 0, 0));
        image.set(2, 2, new RGBPixel(100, 100, 100));

        Image result = clip.apply(image, new ROI(0, 0, 2, 2));
        RGBPixel pixel = (RGBPixel) result.get(1, 1);
        assertEquals(0, pixel.getRed());
        assertEquals(0, pixel.getGreen());
        assertEquals(0, pixel.getBlue());
    }

    // Test cases for the apply method with RealColorImage

    @Test
    public void testApplyWithRealColorImage_noROI() {
        Clip clip = new Clip(-100.5f, 100.5f);
        RealColorImage image = new RealColorImage(3, 3);
        image.set(1, 1, new RGBPixel(200.5f, 200.5f, 200.5f));

        Image result = clip.apply(image);
        RGBPixel pixel = (RGBPixel) result.get(1, 1);
        assertEquals(100.5f, pixel.getRed(), 0.001);
        assertEquals(100.5f, pixel.getGreen(), 0.001);
        assertEquals(100.5f, pixel.getBlue(), 0.001);
    }

    @Test
    public void testApplyWithRealColorImage_withROI() {
        Clip clip = new Clip(-100.5f, 100.5f);
        RealColorImage image = new RealColorImage(3, 3);
        image.set(0, 0, new RGBPixel(200.5f, 200.5f, 200.5f));
        image.set(1, 1, new RGBPixel(0, 0, 0));
        image.set(2, 2, new RGBPixel(100.5f, 100.5f, 100.5f));

        Image result = clip.apply(image, new ROI(0, 0, 2, 2));
        RGBPixel pixel = (RGBPixel) result.get(1, 1);
        assertEquals(0, pixel.getRed(), 0.001);
        assertEquals(0, pixel.getGreen(), 0.001);
        assertEquals(0, pixel.getBlue(), 0.001);
    }

    // Test case for the main method

    @Test
    public void testMain() {
        String[] args = new String[]{"-10.5", "10.5", "input.txt", "output.txt"};
        Clip.main(args);
        // assert statements for checking if output.txt is created and its content is correct
    }

}