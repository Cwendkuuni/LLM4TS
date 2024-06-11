import org.junit.*;
import static org.junit.Assert.*;

public class ShiftTest {

    @Test
    public void testApplyGrayImage() {
        // Typical use case
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 1, 2);
        image.set(2, 2, 3);

        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage result = shift.apply(image);

        assertEquals(result.get(0, 0), 0);
        assertEquals(result.get(1, 1), 1);
        assertEquals(result.get(2, 2), 2);

        // Edge case
        image = new GrayImage(1, 1);
        image.set(0, 0, 5);

        shift = new Shift(1, 1, Shift.NOWRAP);
        result = shift.apply(image);

        assertEquals(result.get(0, 0), 0);

        // Error scenario
        image = null;

        shift = new Shift(1, 1, Shift.NOWRAP);
        result = shift.apply(image);

        assertNull(result);
    }

    @Test
    public void testApplyRealGrayImage() {
        // Typical use case
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.2f);
        image.set(1, 1, 2.3f);
        image.set(2, 2, 3.4f);

        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        RealGrayImage result = shift.apply(image);

        assertEquals(result.get(0, 0), 0.0f, 0.0001);
        assertEquals(result.get(1, 1), 1.2f, 0.0001);
        assertEquals(result.get(2, 2), 2.3f, 0.0001);

        // Edge case
        image = new RealGrayImage(1, 1);
        image.set(0, 0, 5.6f);

        shift = new Shift(1, 1, Shift.NOWRAP);
        result = shift.apply(image);

        assertEquals(result.get(0, 0), 0.0f, 0.0001);

        // Error scenario
        image = null;

        shift = new Shift(1, 1, Shift.NOWRAP);
        result = shift.apply(image);

        assertNull(result);
    }

    @Test
    public void testApplyColorImage() {
        // Typical use case
        ColorImage image = new ColorImage(3, 3);
        image.setPlane(0, new GrayImage(3, 3));
        image.setPlane(1, new GrayImage(3, 3));
        image.setPlane(2, new GrayImage(3, 3));

        image.plane(0).set(0, 0, 1);
        image.plane(1).set(1, 1, 2);
        image.plane(2).set(2, 2, 3);

        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        ColorImage result = shift.apply(image);

        assertEquals(result.plane(0).get(0, 0), 0);
        assertEquals(result.plane(1).get(1, 1), 1);
        assertEquals(result.plane(2).get(2, 2), 2);

        // Edge case
        image = new ColorImage(1, 1);
        image.setPlane(0, new GrayImage(1, 1));
        image.setPlane(1, new GrayImage(1, 1));
        image.setPlane(2, new GrayImage(1, 1));

        image.plane(0).set(0, 0, 5);

        shift = new Shift(1, 1, Shift.NOWRAP);
        result = shift.apply(image);

        assertEquals(result.plane(0).get(0, 0), 0);

        // Error scenario
        image = null;

        shift = new Shift(1, 1, Shift.NOWRAP);
        result = shift.apply(image);

        assertNull(result);
    }

    @Test
    public void testApplyRealColorImage() {
        // Typical use case
        RealColorImage image = new RealColorImage(3, 3);
        image.setPlane(0, new RealGrayImage(3, 3));
        image.setPlane(1, new RealGrayImage(3, 3));
        image.setPlane(2, new RealGrayImage(3, 3));

        image.plane(0).set(0, 0, 1.2f);
        image.plane(1).set(1, 1, 2.3f);
        image.plane(2).set(2, 2, 3.4f);

        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        RealColorImage result = shift.apply(image);

        assertEquals(result.plane(0).get(0, 0), 0.0f, 0.0001);
        assertEquals(result.plane(1).get(1, 1), 1.2f, 0.0001);
        assertEquals(result.plane(2).get(2, 2), 2.3f, 0.0001);

        // Edge case
        image = new RealColorImage(1, 1);
        image.setPlane(0, new RealGrayImage(1, 1));
        image.setPlane(1, new RealGrayImage(1, 1));
        image.setPlane(2, new RealGrayImage(1, 1));

        image.plane(0).set(0, 0, 5.6f);

        shift = new Shift(1, 1, Shift.NOWRAP);
        result = shift.apply(image);

        assertEquals(result.plane(0).get(0, 0), 0.0f, 0.0001);

        // Error scenario
        image = null;

        shift = new Shift(1, 1, Shift.NOWRAP);
        result = shift.apply(image);

        assertNull(result);
    }
}