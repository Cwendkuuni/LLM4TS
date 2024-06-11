
import org.junit.Test;
import static org.junit.Assert.*;

public class ShiftTest {

    @Test
    public void testConstructor() {
        Shift shift = new Shift(1, 2, 1);
        assertEquals(1, shift.xoffset);
        assertEquals(2, shift.yoffset);
        assertTrue(shift.wrap);
    }

    @Test
    public void testApplyGrayImage() {
        GrayImage image = new GrayImage(3, 3);
        Shift shift = new Shift(1, 1, 1);
        GrayImage result = shift.apply(image);
        assertEquals(3, result.X());
        assertEquals(3, result.Y());
    }

    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage image = new RealGrayImage(3, 3);
        Shift shift = new Shift(1, 1, 1);
        RealGrayImage result = shift.apply(image);
        assertEquals(3, result.X());
        assertEquals(3, result.Y());
    }

    @Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage(3, 3);
        Shift shift = new Shift(1, 1, 1);
        ColorImage result = shift.apply(image);
        assertEquals(3, result.X());
        assertEquals(3, result.Y());
    }

    @Test
    public void testApplyRealColorImage() {
        RealColorImage image = new RealColorImage(3, 3);
        Shift shift = new Shift(1, 1, 1);
        RealColorImage result = shift.apply(image);
        assertEquals(3, result.X());
        assertEquals(3, result.Y());
    }
}