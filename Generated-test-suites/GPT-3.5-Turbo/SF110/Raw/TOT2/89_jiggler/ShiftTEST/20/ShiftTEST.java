import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.ops.Shift;
import jigl.image.types.GrayImage;

public class ShiftTest {

    @Test
    public void testConstructor() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        assertEquals(2, shift.xoffset);
        assertEquals(3, shift.yoffset);
        assertTrue(shift.wrap);
    }

    @Test
    public void testApplyGrayImage() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage input = new GrayImage(3, 3);
        input.init(255);

        GrayImage result = shift.apply(input);

        assertEquals(255, result.get(0, 0));
        assertEquals(0, result.get(1, 1));
    }

    @Test
    public void testApplyRealGrayImage() {
        Shift shift = new Shift(-1, -1, Shift.WRAP);
        RealGrayImage input = new RealGrayImage(3, 3);
        input.init(0.5f);

        RealGrayImage result = shift.apply(input);

        assertEquals(0.5f, result.get(2, 2), 0.001);
        assertEquals(0.0f, result.get(0, 0), 0.001);
    }

    @Test
    public void testApplyColorImage() {
        Shift shift = new Shift(0, 0, Shift.WRAP);
        ColorImage input = new ColorImage(2, 2);
        input.plane(0).init(255);
        input.plane(1).init(127);
        input.plane(2).init(63);

        ColorImage result = shift.apply(input);

        assertEquals(255, result.plane(0).get(0, 0));
        assertEquals(127, result.plane(1).get(0, 0));
        assertEquals(63, result.plane(2).get(0, 0));
    }

    @Test
    public void testApplyRealColorImage() {
        Shift shift = new Shift(2, 2, Shift.NOWRAP);
        RealColorImage input = new RealColorImage(3, 3);
        input.plane(0).init(0.5f);
        input.plane(1).init(0.2f);
        input.plane(2).init(0.8f);

        RealColorImage result = shift.apply(input);

        assertEquals(0.5f, result.plane(0).get(2, 2), 0.001);
        assertEquals(0.0f, result.plane(1).get(1, 1), 0.001);
        assertEquals(0.8f, result.plane(2).get(1, 1), 0.001);
    }
}