import static org.junit.Assert.*;
import org.junit.Test;

public class ShiftTest {

    @Test
    public void testApply_grayImage() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 10);
        image.set(1, 1, 20);
        image.set(2, 2, 30);
        GrayImage result = shift.apply(image);
        assertEquals(10, result.get(1, 1));
        assertEquals(20, result.get(2, 2));
        assertEquals(30, result.get(0, 0));
    }

    @Test
    public void testApply_realGrayImage() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 10.0f);
        image.set(1, 1, 20.0f);
        image.set(2, 2, 30.0f);
        RealGrayImage result = shift.apply(image);
        assertEquals(10.0f, result.get(1, 1), 0.001);
        assertEquals(20.0f, result.get(2, 2), 0.001);
        assertEquals(30.0f, result.get(0, 0), 0.001);
    }

    @Test
    public void testApply_colorImage() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        ColorImage image = new ColorImage(3, 3);
        image.setPlane(0, new GrayImage(3, 3));
        image.setPlane(1, new GrayImage(3, 3));
        image.setPlane(2, new GrayImage(3, 3));
        image.set(0, 0, new int[]{10, 20, 30});
        image.set(1, 1, new int[]{40, 50, 60});
        image.set(2, 2, new int[]{70, 80, 90});
        ColorImage result = shift.apply(image);
        assertEquals(10, result.get(1, 1)[0]);
        assertEquals(20, result.get(1, 1)[1]);
        assertEquals(30, result.get(1, 1)[2]);
        assertEquals(40, result.get(2, 2)[0]);
        assertEquals(50, result.get(2, 2)[1]);
        assertEquals(60, result.get(2, 2)[2]);
        assertEquals(70, result.get(0, 0)[0]);
        assertEquals(80, result.get(0, 0)[1]);
        assertEquals(90, result.get(0, 0)[2]);
    }

    @Test
    public void testApply_realColorImage() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealColorImage image = new RealColorImage(3, 3);
        image.setPlane(0, new RealGrayImage(3, 3));
        image.setPlane(1, new RealGrayImage(3, 3));
        image.setPlane(2, new RealGrayImage(3, 3));
        image.set(0, 0, new float[]{10.0f, 20.0f, 30.0f});
        image.set(1, 1, new float[]{40.0f, 50.0f, 60.0f});
        image.set(2, 2, new float[]{70.0f, 80.0f, 90.0f});
        RealColorImage result = shift.apply(image);
        assertEquals(10.0f, result.get(1, 1)[0], 0.001);
        assertEquals(20.0f, result.get(1, 1)[1], 0.001);
        assertEquals(30.0f, result.get(1, 1)[2], 0.001);
        assertEquals(40.0f, result.get(2, 2)[0], 0.001);
        assertEquals(50.0f, result.get(2, 2)[1], 0.001);
        assertEquals(60.0f, result.get(2, 2)[2], 0.001);
        assertEquals(70.0f, result.get(0, 0)[0], 0.001);
        assertEquals(80.0f, result.get(0, 0)[1], 0.001);
        assertEquals(90.0f, result.get(0, 0)[2], 0.001);
    }
}