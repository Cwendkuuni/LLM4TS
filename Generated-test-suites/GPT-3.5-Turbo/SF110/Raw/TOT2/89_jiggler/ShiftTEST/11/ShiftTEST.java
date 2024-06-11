import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.ops.Shift;
import jigl.image.exceptions.*;

public class ShiftTest {

    @Test
    public void testConstructor() {
        Shift shift = new Shift(1, 2, Shift.WRAP);
        assertEquals(1, shift.xoffset);
        assertEquals(2, shift.yoffset);
        assertTrue(shift.wrap);
    }

    @Test
    public void testShiftGrayImageNoWrap() {
        // Create a GrayImage
        GrayImage grayImage = new GrayImage(3, 3);
        // Populate the image
        for (int y = 0; y < grayImage.Y(); y++) {
            for (int x = 0; x < grayImage.X(); x++) {
                grayImage.set(x, y, x + y);
            }
        }
        // Create a Shift instance
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        // Apply the shift
        GrayImage result = shift.apply(grayImage);

        // Check the pixels of the result image
        assertEquals(0, result.get(0, 0));
        assertEquals(1, result.get(1, 0));
        assertEquals(0, result.get(2, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(2, result.get(1, 1));
        assertEquals(0, result.get(2, 1));
        assertEquals(0, result.get(0, 2));
        assertEquals(0, result.get(1, 2));
        assertEquals(0, result.get(2, 2));
    }

    @Test
    public void testShiftRealColorImageWrap() {
        // Create a RealColorImage
        RealColorImage realColorImage = new RealColorImage(3, 3);
        // Populate the image
        for (int y = 0; y < realColorImage.Y(); y++) {
            for (int x = 0; x < realColorImage.X(); x++) {
                realColorImage.setPlane(0, x, y, x);
                realColorImage.setPlane(1, x, y, y);
                realColorImage.setPlane(2, x, y, x + y);
            }
        }
        // Create a Shift instance
        Shift shift = new Shift(-1, -1, Shift.WRAP);
        // Apply the shift
        RealColorImage result = shift.apply(realColorImage);

        // Check the pixels of the result image
        assertEquals(2, result.getPlane(0, 0, 0), 0.01);
        assertEquals(2, result.getPlane(1, 0, 0), 0.01);
        assertEquals(3, result.getPlane(2, 0, 0), 0.01);
        assertEquals(1, result.getPlane(0, 1, 1), 0.01);
        assertEquals(1, result.getPlane(1, 1, 1), 0.01);
        assertEquals(2, result.getPlane(2, 1, 1), 0.01);
        assertEquals(2, result.getPlane(0, 2, 2), 0.01);
        assertEquals(2, result.getPlane(1, 2, 2), 0.01);
        assertEquals(3, result.getPlane(2, 2, 2), 0.01);
    }

    // Add more test cases as needed

}