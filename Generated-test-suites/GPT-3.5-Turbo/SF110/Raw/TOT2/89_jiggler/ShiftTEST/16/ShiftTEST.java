import static org.junit.Assert.*;
import org.junit.Test;

public class ShiftTest {

    @Test
    public void testConstructor() {
        Shift shift = new Shift(1, 2, Shift.WRAP);
        assertEquals(1, shift.xoffset);
        assertEquals(2, shift.yoffset);
        assertTrue(shift.wrap);
    }

    @Test
    public void testConstructorNoWrap() {
        Shift shift = new Shift(3, 4, Shift.NOWRAP);
        assertEquals(3, shift.xoffset);
        assertEquals(4, shift.yoffset);
        assertFalse(shift.wrap);
    }

    @Test
    public void testApplyGrayImage() {
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 5);
        Shift shift = new Shift(1, 1, Shift.WRAP);
        GrayImage result = shift.apply(image);
        assertEquals(5, result.get(1, 1));
    }

    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 5.0f);
        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealGrayImage result = shift.apply(image);
        assertEquals(5.0f, result.get(1, 1), 0.001);
    }

    @Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage(3, 3);
        ColorImage result = new Shift(1, 1, Shift.WRAP).apply(image);
        // Test color image logic here
    }

    @Test
    public void testApplyRealColorImage() {
        RealColorImage image = new RealColorImage(3, 3);
        RealColorImage result = new Shift(1, 1, Shift.WRAP).apply(image);
        // Test real color image logic here
    }
}