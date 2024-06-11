import org.junit.Test;
import static org.junit.Assert.*;

public class ShiftTest {

    @Test
    public void testApply_GrayImage() {
        GrayImage image = new GrayImage(3, 3);
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                image.set(x, y, x + y);
            }
        }
        Shift shift = new Shift(1, 1, Shift.WRAP);
        GrayImage result = shift.apply(image);
        assertEquals(result.get(0, 0), image.get(2, 2));
        assertEquals(result.get(1, 0), image.get(0, 2));
        assertEquals(result.get(2, 0), image.get(1, 2));
        // Other test cases
    }

    @Test
    public void testApply_RealGrayImage() {
        RealGrayImage image = new RealGrayImage(3, 3);
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                image.set(x, y, (float) (x + y));
            }
        }
        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealGrayImage result = shift.apply(image);
        assertEquals(result.get(0, 0), image.get(2, 2), 0.001f);
        assertEquals(result.get(1, 0), image.get(0, 2), 0.001f);
        assertEquals(result.get(2, 0), image.get(1, 2), 0.001f);
        // Other test cases
    }

    @Test
    public void testApply_ColorImage() {
        ColorImage image = new ColorImage(3, 3);
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                image.set(x, y, new Color(x + y, x + y, x + y));
            }
        }
        Shift shift = new Shift(1, 1, Shift.WRAP);
        ColorImage result = shift.apply(image);
        assertEquals(result.get(0, 0), image.get(2, 2));
        assertEquals(result.get(1, 0), image.get(0, 2));
        assertEquals(result.get(2, 0), image.get(1, 2));
        // Other test cases
    }

    @Test
    public void testApply_RealColorImage() {
        RealColorImage image = new RealColorImage(3, 3);
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                image.set(x, y, new Color((float) (x + y), (float) (x + y), (float) (x + y)));
            }
        }
        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealColorImage result = shift.apply(image);
        assertEquals(result.get(0, 0), image.get(2, 2));
        assertEquals(result.get(1, 0), image.get(0, 2));
        assertEquals(result.get(2, 0), image.get(1, 2));
        // Other test cases
    }
}