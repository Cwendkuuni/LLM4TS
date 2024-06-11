import static org.junit.Assert.*;
import org.junit.Test;

public class ShiftTest {

    @Test
    public void testShiftConstructorPositiveOffsetsAndWrap() {
        Shift shift = new Shift(2, 2, Shift.WRAP);
        assertEquals(2, shift.getXoffset());
        assertEquals(2, shift.getYoffset());
        assertEquals(true, shift.isWrap());
    }

    // More test cases for Shift constructor...

    @Test
    public void testApplyGrayImageNullInput() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage result = shift.apply(null);
        assertNull(result);
    }

    // More test cases for apply(GrayImage)...

    @Test
    public void testApplyRealGrayImageNegativeDimensions() {
        Shift shift = new Shift(2, -1, Shift.NOWRAP);
        RealGrayImage image = new RealGrayImage(-10, -10);
        RealGrayImage result = shift.apply(image);
        assertEquals(0, result.X());
        assertEquals(0, result.Y());
    }

    // More test cases for apply(RealGrayImage)...

    @Test
    public void testApplyColorImageZeroDimensions() {
        Shift shift = new Shift(-1, -1, Shift.WRAP);
        ColorImage image = new ColorImage(0, 0);
        ColorImage result = shift.apply(image);
        assertEquals(0, result.X());
        assertEquals(0, result.Y());
    }

    // More test cases for apply(ColorImage)...

    @Test
    public void testApplyRealColorImagePositiveOffsetsNoWrap() {
        Shift shift = new Shift(2, 2, Shift.NOWRAP);
        RealColorImage image = new RealColorImage(10, 10);
        RealColorImage result = shift.apply(image);
        assertNotNull(result);
        assertEquals(10, result.X());
        assertEquals(10, result.Y());
    }

    // More test cases for apply(RealColorImage)...

    @Test
    public void testApplyRealColorImageNegativeOffsetsWrap() {
        Shift shift = new Shift(-1, -1, Shift.WRAP);
        RealColorImage image = new RealColorImage(100, 100);
        RealColorImage result = shift.apply(image);
        assertNotNull(result);
        assertEquals(100, result.X());
        assertEquals(100, result.Y());
    }

    // More test cases for apply(RealColorImage)...

}


