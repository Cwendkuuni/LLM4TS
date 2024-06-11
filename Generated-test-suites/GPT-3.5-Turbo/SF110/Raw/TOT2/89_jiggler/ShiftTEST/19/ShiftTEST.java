import static org.junit.Assert.*;
import org.junit.Test;

public class ShiftTest {

    @Test
    public void testShiftGrayImage() {
        GrayImage image = new GrayImage(10, 10);
        Shift shift = new Shift(2, 3, Shift.WRAP);
        GrayImage result = shift.apply(image);
        // Add assertions for expected results
    }

    @Test
    public void testShiftRealGrayImage() {
        RealGrayImage image = new RealGrayImage(10, 10);
        Shift shift = new Shift(2, 3, Shift.NOWRAP);
        RealGrayImage result = shift.apply(image);
        // Add assertions for expected results
    }

    @Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage(10, 10);
        Shift shift = new Shift(2, 3, Shift.WRAP);
        ColorImage result = shift.apply(image);
        // Add assertions for expected results
    }

    @Test
    public void testApplyRealColorImage() {
        RealColorImage image = new RealColorImage(10, 10);
        Shift shift = new Shift(2, 3, Shift.NOWRAP);
        RealColorImage result = shift.apply(image);
        // Add assertions for expected results
    }

    // Add additional test cases for edge cases and error scenarios

}