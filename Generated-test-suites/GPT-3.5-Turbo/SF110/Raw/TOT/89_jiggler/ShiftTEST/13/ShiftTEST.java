import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.ColorImage;
import jigl.image.RealColorImage;
import static org.junit.Assert.*;
import org.junit.Test;

public class ShiftTest {

    @Test
    public void testGrayImageWrap() {
        // Typical use case
        GrayImage image = new GrayImage(5, 5);
        image.set(0, 0, 10);
        image.set(4, 4, 20);
        Shift shift = new Shift(2, 2, Shift.WRAP);
        GrayImage result = shift.apply(image);
        assertEquals(10, result.get(2, 2));
        assertEquals(20, result.get(1, 1));

        // Edge case - check if wrap-around occurs correctly
        image = new GrayImage(5, 5);
        image.set(0, 0, 10);
        shift = new Shift(6, 6, Shift.WRAP);
        result = shift.apply(image);
        assertEquals(10, result.get(1, 1));

        // Error scenario - check if invalid wrap parameter results in no wrap-around
        image = new GrayImage(5, 5);
        image.set(0, 0, 10);
        shift = new Shift(2, 2, 2);
        result = shift.apply(image);
        assertEquals(0, result.get(2, 2));
    }

    @Test
    public void testGrayImageNoWrap() {
        // Typical use case
        GrayImage image = new GrayImage(5, 5);
        image.set(0, 0, 10);
        image.set(4, 4, 20);
        Shift shift = new Shift(2, 2, Shift.NOWRAP);
        GrayImage result = shift.apply(image);
        assertEquals(10, result.get(2, 2));
        assertEquals(0, result.get(1, 1));

        // Edge case - check if shifting beyond image boundaries results in zeros
        image = new GrayImage(5, 5);
        image.set(0, 0, 10);
        shift = new Shift(6, 6, Shift.NOWRAP);
        result = shift.apply(image);
        assertEquals(0, result.get(1, 1));
    }

    @Test
    public void testRealGrayImageWrap() {
        // Typical use case
        RealGrayImage image = new RealGrayImage(5, 5);
        image.set(0, 0, 1.5f);
        image.set(4, 4, 2.5f);
        Shift shift = new Shift(2, 2, Shift.WRAP);
        RealGrayImage result = shift.apply(image);
        assertEquals(1.5f, result.get(2, 2), 0.001);
        assertEquals(2.5f, result.get(1, 1), 0.001);

        // Edge case - check if wrap-around occurs correctly
        image = new RealGrayImage(5, 5);
        image.set(0, 0, 1.5f);
        shift = new Shift(6, 6, Shift.WRAP);
        result = shift.apply(image);
        assertEquals(1.5f, result.get(1, 1), 0.001);

        // Error scenario - check if invalid wrap parameter results in no wrap-around
        image = new RealGrayImage(5, 5);
        image.set(0, 0, 1.5f);
        shift = new Shift(2, 2, 2);
        result = shift.apply(image);
        assertEquals(0.0f, result.get(2, 2), 0.001);
    }

    @Test
    public void testRealGrayImageNoWrap() {
        // Typical use case
        RealGrayImage image = new RealGrayImage(5, 5);
        image.set(0, 0, 1.5f);
        image.set(4, 4, 2.5f);
        Shift shift = new Shift(2, 2, Shift.NOWRAP);
        RealGrayImage result = shift.apply(image);
        assertEquals(1.5f, result.get(2, 2), 0.001);
        assertEquals(0.0f, result.get(1, 1), 0.001);

        // Edge case - check if shifting beyond image boundaries results in zeros
        image = new RealGrayImage(5, 5);
        image.set(0, 0, 1.5f);
        shift = new Shift(6, 6, Shift.NOWRAP);
        result = shift.apply(image);
        assertEquals(0.0f, result.get(1, 1), 0.001);
    }

    @Test
    public void testColorImageWrap() {
        // Typical use case
        ColorImage image = new ColorImage(5, 5);
        image.set(0, 0, new int[]{10, 20, 30});
        image.set(4, 4, new int[]{40, 50, 60});
        Shift shift = new Shift(2, 2, Shift.WRAP);
        ColorImage result = shift.apply(image);
        assertArrayEquals(new int[]{10, 20, 30}, result.get(2, 2));
        assertArrayEquals(new int[]{40, 50, 60}, result.get(1, 1));

        // Edge case - check if wrap-around occurs correctly
        image = new ColorImage(5, 5);
        image.set(0, 0, new int[]{10, 20, 30});
        shift = new Shift(6, 6, Shift.WRAP);
        result = shift.apply(image);
        assertArrayEquals(new int[]{10, 20, 30}, result.get(1, 1));

        // Error scenario - check if invalid wrap parameter results in no wrap-around
        image = new ColorImage(5, 5);
        image.set(0, 0, new int[]{10, 20, 30});
        shift = new Shift(2, 2, 2);
        result = shift.apply(image);
        assertArrayEquals(new int[]{0, 0, 0}, result.get(2, 2));
    }

    @Test
    public void testColorImageNoWrap() {
        // Typical use case
        ColorImage image = new ColorImage(5, 5);
        image.set(0, 0, new int[]{10, 20, 30});
        image.set(4, 4, new int[]{40, 50, 60});
        Shift shift = new Shift(2, 2, Shift.NOWRAP);
        ColorImage result = shift.apply(image);
        assertArrayEquals(new int[]{10, 20, 30}, result.get(2, 2));
        assertArrayEquals(new int[]{0, 0, 0}, result.get(1, 1));

        // Edge case - check if shifting beyond image boundaries results in zeros
        image = new ColorImage(5, 5);
        image.set(0, 0, new int[]{10, 20, 30});
        shift = new Shift(6, 6, Shift.NOWRAP);
        result = shift.apply(image);
        assertArrayEquals(new int[]{0, 0, 0}, result.get(1, 1));
    }

    @Test
    public void testRealColorImageWrap() {
        // Typical use case
        RealColorImage image = new RealColorImage(5, 5);
        image.set(0, 0, new float[]{1.5f, 2.5f, 3.5f});
        image.set(4, 4, new float[]{4.5f, 5.5f, 6.5f});
        Shift shift = new Shift(2, 2, Shift.WRAP);
        RealColorImage result = shift.apply(image);
        assertArrayEquals(new float[]{1.5f, 2.5f, 3.5f}, result.get(2, 2), 0.001);
        assertArrayEquals(new float[]{4.5f, 5.5f, 6.5f}, result.get(1, 1), 0.001);

        // Edge case - check if wrap-around occurs correctly
        image = new RealColorImage(5, 5);
        image.set(0, 0, new float[]{1.5f, 2.5f, 3.5f});
        shift = new Shift(6, 6, Shift.WRAP);
        result = shift.apply(image);
        assertArrayEquals(new float[]{1.5f, 2.5f, 3.5f}, result.get(1, 1), 0.001);

        // Error scenario - check if invalid wrap parameter results in no wrap-around
        image = new RealColorImage(5, 5);
        image.set(0, 0, new float[]{1.5f, 2.5f, 3.5f});
        shift = new Shift(2, 2, 2);
        result = shift.apply(image);
        assertArrayEquals(new float[]{0.0f, 0.0f, 0.0f}, result.get(2, 2), 0.001);
    }

    @Test
    public void testRealColorImageNoWrap() {
        // Typical use case
        RealColorImage image = new RealColorImage(5, 5);
        image.set(0, 0, new float[]{1.5f, 2.5f, 3.5f});
        image.set(4, 4, new float[]{4.5f, 5.5f, 6.5f});
        Shift shift = new Shift(2, 2, Shift.NOWRAP);
        RealColorImage result = shift.apply(image);
        assertArrayEquals(new float[]{1.5f, 2.5f, 3.5f}, result.get(2, 2), 0.001);
        assertArrayEquals(new float[]{0.0f, 0.0f, 0.0f}, result.get(1, 1), 0.001);

        // Edge case - check if shifting beyond image boundaries results in zeros
        image = new RealColorImage(5, 5);
        image.set(0, 0, new float[]{1.5f, 2.5f, 3.5f});
        shift = new Shift(6, 6, Shift.NOWRAP);
        result = shift.apply(image);
        assertArrayEquals(new float[]{0.0f, 0.0f, 0.0f}, result.get(1, 1), 0.001);
    }
}