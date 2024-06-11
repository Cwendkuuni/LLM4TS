import jigl.image.ops.Shift;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;
import jigl.image.types.ColorImage;
import jigl.image.types.RealColorImage;
import org.junit.Test;
import static org.junit.Assert.*;

public class ShiftTest {

    @Test
    public void testShiftConstructor() {
        Shift shift = new Shift(2, 3, 1);
        assertEquals(2, shift.xoffset);
        assertEquals(3, shift.yoffset);
        assertTrue(shift.wrap);
    }

    @Test
    public void testApplyGrayImage() {
        Shift shift = new Shift(1, 1, 1);
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 255);
        GrayImage result = shift.apply(image);
        assertEquals(255, result.get(1, 1));
    }

    @Test
    public void testApplyRealGrayImage() {
        Shift shift = new Shift(2, 2, 0);
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(1, 1, 0.5f);
        RealGrayImage result = shift.apply(image);
        assertEquals(0.5f, result.get(3, 3), 0.001);
    }

    @Test
    public void testApplyColorImage() {
        Shift shift = new Shift(1, 1, 0);
        ColorImage image = new ColorImage(3, 3);
        image.setPlane(0, new GrayImage(3, 3));
        image.setPlane(1, new GrayImage(3, 3));
        image.setPlane(2, new GrayImage(3, 3));
        ColorImage result = shift.apply(image);
        assertEquals(0, result.getPlane(0).get(1, 1));
    }

    @Test
    public void testApplyRealColorImage() {
        Shift shift = new Shift(2, 2, 1);
        RealColorImage image = new RealColorImage(3, 3);
        image.setPlane(0, new RealGrayImage(3, 3));
        image.setPlane(1, new RealGrayImage(3, 3));
        image.setPlane(2, new RealGrayImage(3, 3));
        RealColorImage result = shift.apply(image);
        assertEquals(0, result.getPlane(2).get(3, 3), 0.001);
    }
}