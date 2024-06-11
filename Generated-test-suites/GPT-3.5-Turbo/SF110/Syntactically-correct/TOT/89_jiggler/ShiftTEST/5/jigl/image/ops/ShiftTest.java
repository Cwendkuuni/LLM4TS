import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ShiftTest {

    private Shift shift;

    @Before
    public void setUp() {
        shift = new Shift(2, 3, 1);
    }

    @Test
    public void testApplyGrayImage_wrap_true() {
        GrayImage image = new GrayImage(5, 5);
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, x + y);
            }
        }

        GrayImage expected = new GrayImage(5, 5);
        expected.set(2, 3, 0);
        expected.set(3, 4, 1);
        expected.set(4, 0, 2);
        expected.set(0, 1, 3);
        expected.set(1, 2, 4);

        GrayImage result = shift.apply(image);

        assertEquals(expected, result);
    }

    @Test
    public void testApplyGrayImage_wrap_false() {
        Shift shift = new Shift(-2, -3, 0);

        GrayImage image = new GrayImage(5, 5);
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, x + y);
            }
        }

        GrayImage expected = new GrayImage(5, 5);
        expected.set(0, 0, 0);
        expected.set(1, 1, 1);
        expected.set(2, 2, 2);
        expected.set(3, 3, 3);
        expected.set(4, 4, 4);

        GrayImage result = shift.apply(image);

        assertEquals(expected, result);
    }

    @Test
    public void testApplyRealGrayImage_wrap_true() {
        RealGrayImage image = new RealGrayImage(5, 5);
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, (float) (x + y) / 10);
            }
        }

        RealGrayImage expected = new RealGrayImage(5, 5);
        expected.set(2, 3, 0.0f);
        expected.set(3, 4, 0.1f);
        expected.set(4, 0, 0.2f);
        expected.set(0, 1, 0.3f);
        expected.set(1, 2, 0.4f);

        RealGrayImage result = shift.apply(image);

        assertEquals(expected, result);
    }

    @Test
    public void testApplyRealGrayImage_wrap_false() {
        Shift shift = new Shift(-2, -3, 0);

        RealGrayImage image = new RealGrayImage(5, 5);
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, (float) (x + y) / 10);
            }
        }

        RealGrayImage expected = new RealGrayImage(5, 5);
        expected.set(0, 0, 0.0f);
        expected.set(1, 1, 0.1f);
        expected.set(2, 2, 0.2f);
        expected.set(3, 3, 0.3f);
        expected.set(4, 4, 0.4f);

        RealGrayImage result = shift.apply(image);

        assertEquals(expected, result);
    }

    @Test
    public void testApplyColorImage_wrap_true() {
        ColorImage image = new ColorImage(5, 5);
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, new Color(x + y, x + y, x + y));
            }
        }

        ColorImage expected = new ColorImage(5, 5);
        expected.set(2, 3, new Color(0, 0, 0));
        expected.set(3, 4, new Color(1, 1, 1));
        expected.set(4, 0, new Color(2, 2, 2));
        expected.set(0, 1, new Color(3, 3, 3));
        expected.set(1, 2, new Color(4, 4, 4));

        ColorImage result = shift.apply(image);

        assertEquals(expected, result);
    }

    @Test
    public void testApplyColorImage_wrap_false() {
        Shift shift = new Shift(-2, -3, 0);

        ColorImage image = new ColorImage(5, 5);
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, new Color(x + y, x + y, x + y));
            }
        }

        ColorImage expected = new ColorImage(5, 5);
        expected.set(0, 0, new Color(0, 0, 0));
        expected.set(1, 1, new Color(1, 1, 1));
        expected.set(2, 2, new Color(2, 2, 2));
        expected.set(3, 3, new Color(3, 3, 3));
        expected.set(4, 4, new Color(4, 4, 4));

        ColorImage result = shift.apply(image);

        assertEquals(expected, result);
    }

    @Test
    public void testApplyRealColorImage_wrap_true() {
        RealColorImage image = new RealColorImage(5, 5);
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, new Color((float) (x + y) / 10, (float) (x + y) / 10, (float) (x + y) / 10));
            }
        }

        RealColorImage expected = new RealColorImage(5, 5);
        expected.set(2, 3, new Color(0.0f, 0.0f, 0.0f));
        expected.set(3, 4, new Color(0.1f, 0.1f, 0.1f));
        expected.set(4, 0, new Color(0.2f, 0.2f, 0.2f));
        expected.set(0, 1, new Color(0.3f, 0.3f, 0.3f));
        expected.set(1, 2, new Color(0.4f, 0.4f, 0.4f));

        RealColorImage result = shift.apply(image);

        assertEquals(expected, result);
    }

    @Test
    public void testApplyRealColorImage_wrap_false() {
        Shift shift = new Shift(-2, -3, 0);

        RealColorImage image = new RealColorImage(5, 5);
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, new Color((float) (x + y) / 10, (float) (x + y) / 10, (float) (x + y) / 10));
            }
        }

        RealColorImage expected = new RealColorImage(5, 5);
        expected.set(0, 0, new Color(0.0f, 0.0f, 0.0f));
        expected.set(1, 1, new Color(0.1f, 0.1f, 0.1f));
        expected.set(2, 2, new Color(0.2f, 0.2f, 0.2f));
        expected.set(3, 3, new Color(0.3f, 0.3f, 0.3f));
        expected.set(4, 4, new Color(0.4f, 0.4f, 0.4f));

        RealColorImage result = shift.apply(image);

        assertEquals(expected, result);
    }

}