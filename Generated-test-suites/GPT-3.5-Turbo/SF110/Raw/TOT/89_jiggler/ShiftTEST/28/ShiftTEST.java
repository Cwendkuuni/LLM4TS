import org.junit.Test;
import static org.junit.Assert.*;

public class ShiftTest {

    // Test case for constructor
    @Test
    public void testConstructor() {
        Shift shift = new Shift(3, -2, 1);
        assertEquals(3, shift.xoffset);
        assertEquals(-2, shift.yoffset);
        assertTrue(shift.wrap);
    }

    // Test cases for apply(GrayImage image) method
    @Test
    public void testApplyGrayImage() {
        Shift shift = new Shift(2, 2, 1);

        // Test case for typical use case
        GrayImage image = new GrayImage(5, 5);
        image.set(2, 2, 127);
        GrayImage result = shift.apply(image);
        assertEquals(127, result.get(4, 4));

        // Test case for edge case
        image = new GrayImage(1, 1);
        image.set(0, 0, 255);
        result = shift.apply(image);
        assertEquals(255, result.get(0, 0));

        // Test case for error scenario
        image = null;
        result = shift.apply(image);
        assertNull(result);
    }

    // Test cases for apply(RealGrayImage image) method
    @Test
    public void testApplyRealGrayImage() {
        Shift shift = new Shift(2, 2, 1);

        // Test case for typical use case
        RealGrayImage image = new RealGrayImage(5, 5);
        image.set(2, 2, 0.5f);
        RealGrayImage result = shift.apply(image);
        assertEquals(0.5f, result.get(4, 4), 0.01);

        // Test case for edge case
        image = new RealGrayImage(1, 1);
        image.set(0, 0, 1.0f);
        result = shift.apply(image);
        assertEquals(1.0f, result.get(0, 0), 0.01);

        // Test case for error scenario
        image = null;
        result = shift.apply(image);
        assertNull(result);
    }

    // Test cases for apply(ColorImage image) method
    @Test
    public void testApplyColorImage() {
        Shift shift = new Shift(2, 2, 1);

        // Test case for typical use case
        ColorImage image = new ColorImage(5, 5);
        image.setPlane(0, new GrayImage(5, 5));
        image.setPlane(1, new GrayImage(5, 5));
        image.setPlane(2, new GrayImage(5, 5));
        image.plane(0).set(2, 2, 127);
        ColorImage result = shift.apply(image);
        assertEquals(127, result.plane(0).get(4, 4));

        // Test case for edge case
        image = new ColorImage(1, 1);
        image.setPlane(0, new GrayImage(1, 1));
        image.setPlane(1, new GrayImage(1, 1));
        image.setPlane(2, new GrayImage(1, 1));
        image.plane(0).set(0, 0, 255);
        result = shift.apply(image);
        assertEquals(255, result.plane(0).get(0, 0));

        // Test case for error scenario
        image = null;
        result = shift.apply(image);
        assertNull(result);
    }

    // Test cases for apply(RealColorImage image) method
    @Test
    public void testApplyRealColorImage() {
        Shift shift = new Shift(2, 2, 1);

        // Test case for typical use case
        RealColorImage image = new RealColorImage(5, 5);
        image.setPlane(0, new RealGrayImage(5, 5));
        image.setPlane(1, new RealGrayImage(5, 5));
        image.setPlane(2, new RealGrayImage(5, 5));
        image.plane(0).set(2, 2, 0.5f);
        RealColorImage result = shift.apply(image);
        assertEquals(0.5f, result.plane(0).get(4, 4), 0.01);

        // Test case for edge case
        image = new RealColorImage(1, 1);
        image.setPlane(0, new RealGrayImage(1, 1));
        image.setPlane(1, new RealGrayImage(1, 1));
        image.setPlane(2, new RealGrayImage(1, 1));
        image.plane(0).set(0, 0, 1.0f);
        result = shift.apply(image);
        assertEquals(1.0f, result.plane(0).get(0, 0), 0.01);

        // Test case for error scenario
        image = null;
        result = shift.apply(image);
        assertNull(result);
    }

}