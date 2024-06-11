import static org.junit.Assert.*;

import org.junit.Test;

public class ShiftTest {

    @Test
    public void testConstructor() {
        Shift shift = new Shift(3, 4, Shift.WRAP);
        assertEquals(3, shift.xoffset);
        assertEquals(4, shift.yoffset);
        assertTrue(shift.wrap);
    }

    @Test
    public void testApplyGrayImage() {
        GrayImage image = new GrayImage(5, 5);

        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage result = shift.apply(image);

        // Add more assertions here for the functionality test
    }

    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage image = new RealGrayImage(5, 5);

        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealGrayImage result = shift.apply(image);

        // Add more assertions here for the functionality test
    }

    @Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage(5, 5);

        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        ColorImage result = shift.apply(image);

        // Add more assertions here for the functionality test
    }

    @Test
    public void testApplyRealColorImage() {
        RealColorImage image = new RealColorImage(5, 5);

        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealColorImage result = shift.apply(image);

        // Add more assertions here for the functionality test
    }

    // Add more test cases for edge cases and exception handling scenarios

}