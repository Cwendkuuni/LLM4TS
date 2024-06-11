import org.junit.*;
import static org.junit.Assert.*;

public class ShiftTest {

    @Test
    public void testShiftXOffset() {
        int xoffset = 2;
        int yoffset = 0;
        int wrap = 0;
        Shift shift = new Shift(xoffset, yoffset, wrap);

        // Creating an input image with a single pixel equal to 255
        GrayImage inputImage = new GrayImage(1, 1);
        inputImage.set(0, 0, 255);

        // Shifting the image
        GrayImage outputImage = (GrayImage) shift.apply(inputImage);

        // Expected output image should be equal to the input image since the xoffset=2 will shift the pixel out of image range
        assertEquals(inputImage, outputImage);
    }

    @Test
    public void testShiftYOffset() {
        int xoffset = 0;
        int yoffset = 2;
        int wrap = 0;
        Shift shift = new Shift(xoffset, yoffset, wrap);

        // Creating an input image with a single pixel equal to 255
        GrayImage inputImage = new GrayImage(1, 1);
        inputImage.set(0, 0, 255);

        // Shifting the image
        GrayImage outputImage = (GrayImage) shift.apply(inputImage);

        // Expected output image should be equal to the input image since the yoffset=2 will shift the pixel out of image range
        assertEquals(inputImage, outputImage);
    }

    @Test
    public void testShiftWrapXOffset() {
        int xoffset = 2;
        int yoffset = 0;
        int wrap = 1;
        Shift shift = new Shift(xoffset, yoffset, wrap);

        // Creating an input image with a single pixel equal to 255
        GrayImage inputImage = new GrayImage(1, 1);
        inputImage.set(0, 0, 255);

        // Shifting the image with wrap enabled
        GrayImage outputImage = (GrayImage) shift.apply(inputImage);

        // Expected output image should be equal to the input image since the xoffset=2 will wrap around the image
        assertEquals(inputImage, outputImage);
    }

    @Test
    public void testShiftWrapYOffset() {
        int xoffset = 0;
        int yoffset = 2;
        int wrap = 1;
        Shift shift = new Shift(xoffset, yoffset, wrap);

        // Creating an input image with a single pixel equal to 255
        GrayImage inputImage = new GrayImage(1, 1);
        inputImage.set(0, 0, 255);

        // Shifting the image with wrap enabled
        GrayImage outputImage = (GrayImage) shift.apply(inputImage);

        // Expected output image should be equal to the input image since the yoffset=2 will wrap around the image
        assertEquals(inputImage, outputImage);
    }

    @Test
    public void testShiftOutOfRange() {
        int xoffset = 10;
        int yoffset = 10;
        int wrap = 0;
        Shift shift = new Shift(xoffset, yoffset, wrap);

        // Creating an input image with a single pixel equal to 255
        GrayImage inputImage = new GrayImage(1, 1);
        inputImage.set(0, 0, 255);

        // Shifting the image
        GrayImage outputImage = (GrayImage) shift.apply(inputImage);

        // Expected output image should be equal to the input image since the offsets are out of range
        assertEquals(inputImage, outputImage);
    }

    @Test
    public void testShiftWithRealGrayImage() {
        int xoffset = 2;
        int yoffset = 0;
        int wrap = 0;
        Shift shift = new Shift(xoffset, yoffset, wrap);

        // Creating an input image with a single pixel equal to 255
        RealGrayImage inputImage = new RealGrayImage(1, 1);
        inputImage.set(0, 0, 255.0f);

        // Shifting the image
        RealGrayImage outputImage = (RealGrayImage) shift.apply(inputImage);

        // Expected output image should be equal to the input image since the xoffset=2 will shift the pixel out of image range
        assertEquals(inputImage, outputImage);
    }

    @Test
    public void testShiftWithColorImage() {
        int xoffset = 2;
        int yoffset = 0;
        int wrap = 0;
        Shift shift = new Shift(xoffset, yoffset, wrap);

        // Creating an input image with a single pixel equal to red (255, 0, 0)
        ColorImage inputImage = new ColorImage(1, 1);
        inputImage.set(0, 0, 255, 0, 0);

        // Shifting the image
        ColorImage outputImage = (ColorImage) shift.apply(inputImage);

        // Expected output image should be equal to the input image since the xoffset=2 will shift the pixel out of image range
        assertEquals(inputImage, outputImage);
    }
}