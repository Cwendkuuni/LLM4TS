import jigl.image.*;
import jigl.image.exceptions.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShiftTest {

    @Test
    public void testShiftGrayImage() {

        // Typical Use Case
        GrayImage image = new GrayImage(5, 5);
        image.set(0, 0, 255);
        image.set(1, 1, 128);
        image.set(2, 2, 64);

        Shift shift = new Shift(1, 1, Shift.WRAP);
        GrayImage result = shift.apply(image);

        assertEquals(128, result.get(0, 0));
        assertEquals(64, result.get(1, 1));
        assertEquals(255, result.get(2, 2));

        // Edge Case: xoffset and yoffset are both 0
        shift = new Shift(0, 0, Shift.WRAP);
        result = shift.apply(image);
        assertEquals(255, result.get(0, 0));
        assertEquals(128, result.get(1, 1));
        assertEquals(64, result.get(2, 2));

        // Edge Case: xoffset and yoffset are both negative
        shift = new Shift(-1, -1, Shift.WRAP);
        result = shift.apply(image);
        assertEquals(64, result.get(0, 0));
        assertEquals(255, result.get(1, 1));
        assertEquals(128, result.get(2, 2));

        // Error Scenario: GrayImage is null
        assertThrows(NullPointerException.class, () -> shift.apply(null));

    }

    @Test
    public void testShiftRealGrayImage() {

        // Typical Use Case
        RealGrayImage image = new RealGrayImage(5, 5);
        image.set(0, 0, 1.5f);
        image.set(1, 1, 2.5f);
        image.set(2, 2, 3.5f);

        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealGrayImage result = shift.apply(image);

        assertEquals(2.5f, result.get(0, 0));
        assertEquals(3.5f, result.get(1, 1));
        assertEquals(1.5f, result.get(2, 2));

        // Edge Case: xoffset and yoffset are both 0
        shift = new Shift(0, 0, Shift.WRAP);
        result = shift.apply(image);
        assertEquals(1.5f, result.get(0, 0));
        assertEquals(2.5f, result.get(1, 1));
        assertEquals(3.5f, result.get(2, 2));

        // Edge Case: xoffset and yoffset are both negative
        shift = new Shift(-1, -1, Shift.WRAP);
        result = shift.apply(image);
        assertEquals(3.5f, result.get(0, 0));
        assertEquals(1.5f, result.get(1, 1));
        assertEquals(2.5f, result.get(2, 2));

        // Error Scenario: RealGrayImage is null
        assertThrows(NullPointerException.class, () -> shift.apply(null));

    }

    @Test
    public void testShiftColorImage() {

        // Typical Use Case
        ColorImage image = new ColorImage(5, 5);
        image.setPlane(0, new GrayImage(5, 5));
        image.setPlane(1, new GrayImage(5, 5));
        image.setPlane(2, new GrayImage(5, 5));
        image.set(0, 0, new int[]{255, 128, 64});
        image.set(1, 1, new int[]{128, 64, 32});
        image.set(2, 2, new int[]{64, 32, 16});

        Shift shift = new Shift(1, 1, Shift.WRAP);
        ColorImage result = shift.apply(image);

        assertArrayEquals(new int[]{128, 64, 32}, result.get(0, 0));
        assertArrayEquals(new int[]{64, 32, 16}, result.get(1, 1));
        assertArrayEquals(new int[]{255, 128, 64}, result.get(2, 2));

        // Edge Case: xoffset and yoffset are both 0
        shift = new Shift(0, 0, Shift.WRAP);
        result = shift.apply(image);
        assertArrayEquals(new int[]{255, 128, 64}, result.get(0, 0));
        assertArrayEquals(new int[]{128, 64, 32}, result.get(1, 1));
        assertArrayEquals(new int[]{64, 32, 16}, result.get(2, 2));

        // Edge Case: xoffset and yoffset are both negative
        shift = new Shift(-1, -1, Shift.WRAP);
        result = shift.apply(image);
        assertArrayEquals(new int[]{64, 32, 16}, result.get(0, 0));
        assertArrayEquals(new int[]{255, 128, 64}, result.get(1, 1));
        assertArrayEquals(new int[]{128, 64, 32}, result.get(2, 2));

        // Error Scenario: ColorImage is null
        assertThrows(NullPointerException.class, () -> shift.apply(null));

    }

    @Test
    public void testShiftRealColorImage() {

        // Typical Use Case
        RealColorImage image = new RealColorImage(5, 5);
        image.setPlane(0, new RealGrayImage(5, 5));
        image.setPlane(1, new RealGrayImage(5, 5));
        image.setPlane(2, new RealGrayImage(5, 5));
        image.set(0, 0, new float[]{1.5f, 0.8f, 0.4f});
        image.set(1, 1, new float[]{0.8f, 0.4f, 0.2f});
        image.set(2, 2, new float[]{0.4f, 0.2f, 0.1f});

        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealColorImage result = shift.apply(image);

        assertArrayEquals(new float[]{0.8f, 0.4f, 0.2f}, result.get(0, 0));
        assertArrayEquals(new float[]{0.4f, 0.2f, 0.1f}, result.get(1, 1));
        assertArrayEquals(new float[]{1.5f, 0.8f, 0.4f}, result.get(2, 2));

        // Edge Case: xoffset and yoffset are both 0
        shift = new Shift(0, 0, Shift.WRAP);
        result = shift.apply(image);
        assertArrayEquals(new float[]{1.5f, 0.8f, 0.4f}, result.get(0, 0));
        assertArrayEquals(new float[]{0.8f, 0.4f, 0.2f}, result.get(1, 1));
        assertArrayEquals(new float[]{0.4f, 0.2f, 0.1f}, result.get(2, 2));

        // Edge Case: xoffset and yoffset are both negative
        shift = new Shift(-1, -1, Shift.WRAP);
        result = shift.apply(image);
        assertArrayEquals(new float[]{0.4f, 0.2f, 0.1f}, result.get(0, 0));
        assertArrayEquals(new float[]{1.5f, 0.8f, 0.4f}, result.get(1, 1));
        assertArrayEquals(new float[]{0.8f, 0.4f, 0.2f}, result.get(2, 2));

        // Error Scenario: RealColorImage is null
        assertThrows(NullPointerException.class, () -> shift.apply(null));

    }
}