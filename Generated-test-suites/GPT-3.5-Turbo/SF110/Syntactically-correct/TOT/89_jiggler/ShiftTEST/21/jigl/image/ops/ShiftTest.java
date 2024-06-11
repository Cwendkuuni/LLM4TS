import static org.junit.Assert.*;
import org.junit.*;

public class ShiftTest {

    @Test
    public void testShift_grayImageWrap() {
        GrayImage image = new GrayImage(4, 4);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                image.set(i, j, i + j);
            }
        }

        Shift shift = new Shift(1, 1, Shift.WRAP);
        GrayImage result = shift.apply(image);

        // Checking pixels shifted by 1 in each direction
        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(0, 1));
        assertEquals(2, result.get(1, 0));
        assertEquals(3, result.get(1, 1));

        // Checking pixels wrapped around in each direction
        assertEquals(0, result.get(3, 3));
        assertEquals(1, result.get(3, 2));
        assertEquals(1, result.get(2, 3));
        assertEquals(2, result.get(2, 2));
    }

    @Test
    public void testShift_grayImageNoWrap() {
        GrayImage image = new GrayImage(4, 4);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                image.set(i, j, i + j);
            }
        }

        Shift shift = new Shift(-1, -1, Shift.NOWRAP);
        GrayImage result = shift.apply(image);

        // Checking pixels shifted by -1 in each direction
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int expected;
                if (i - 1 >= 0 && i - 1 < 4 && j - 1 >= 0 && j - 1 < 4) {
                    expected = (i - 1) + (j - 1);
                } else {
                    expected = 0;
                }
                assertEquals(expected, result.get(i, j));
            }
        }
    }

    @Test
    public void testShift_realGrayImageWrap() {
        RealGrayImage image = new RealGrayImage(4, 4);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                image.set(i, j, i + j + 0.5f);
            }
        }

        Shift shift = new Shift(2, 2, Shift.WRAP);
        RealGrayImage result = shift.apply(image);

        // Checking pixels shifted by 2 in each direction
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals((i + 2) % 4 + (j + 2) % 4 + 0.5f, result.get(i, j), 0.001);
            }
        }
    }

    @Test
    public void testShift_realGrayImageNoWrap() {
        RealGrayImage image = new RealGrayImage(4, 4);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                image.set(i, j, i + j + 0.5f);
            }
        }

        Shift shift = new Shift(-2, -2, Shift.NOWRAP);
        RealGrayImage result = shift.apply(image);

        // Checking pixels shifted by -2 in each direction
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                float expected;
                if (i - 2 >= 0 && i - 2 < 4 && j - 2 >= 0 && j - 2 < 4) {
                    expected = (i - 2) + (j - 2) + 0.5f;
                } else {
                    expected = 0.0f;
                }
                assertEquals(expected, result.get(i, j), 0.001);
            }
        }
    }

    @Test
    public void testShift_colorImageWrap() {
        // Create a color image with separate planes
        ColorImage image = new ColorImage(4, 4);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                image.setPlane(0, i, j, i + j);
                image.setPlane(1, i, j, i + j + 0.5f);
                image.setPlane(2, i, j, i + j + 1.0f);
            }
        }

        Shift shift = new Shift(-2, 2, Shift.WRAP);
        ColorImage result = shift.apply(image);

        // Checking pixels shifted by -2 in x direction and 2 in y direction
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals((i - 2 + 4) % 4 + (j + 2) % 4, result.getPlane(0, i, j));
                assertEquals((i - 2 + 4) % 4 + (j + 2) % 4 + 0.5f, result.getPlane(1, i, j), 0.001);
                assertEquals((i - 2 + 4) % 4 + (j + 2) % 4 + 1.0f, result.getPlane(2, i, j), 0.001);
            }
        }
    }

    @Test
    public void testShift_realColorImageWrap() {
        // Create a real color image with separate planes
        RealColorImage image = new RealColorImage(4, 4);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                image.setPlane(0, i, j, i + j + 0.5f);
                image.setPlane(1, i, j, i + j + 1.0f);
                image.setPlane(2, i, j, i + j + 1.5f);
            }
        }

        Shift shift = new Shift(1, -1, Shift.WRAP);
        RealColorImage result = shift.apply(image);

        // Checking pixels shifted by 1 in x direction and -1 in y direction
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals((i + 1) % 4 + (j - 1 + 4) % 4 + 0.5f, result.getPlane(0, i, j), 0.001);
                assertEquals((i + 1) % 4 + (j - 1 + 4) % 4 + 1.0f, result.getPlane(1, i, j), 0.001);
                assertEquals((i + 1) % 4 + (j - 1 + 4) % 4 + 1.5f, result.getPlane(2, i, j), 0.001);
            }
        }
    }

    @Test
    public void testShift_errorScenario() {
        // Create an empty image
        GrayImage image = new GrayImage(0, 0);

        // Shift by any value should result in an empty image
        Shift shift = new Shift(1, 1, Shift.WRAP);
        GrayImage result = shift.apply(image);

        assertEquals(0, result.X());
        assertEquals(0, result.Y());
    }
}