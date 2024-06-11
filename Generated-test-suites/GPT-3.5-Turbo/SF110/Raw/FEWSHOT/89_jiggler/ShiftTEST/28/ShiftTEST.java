package jigl.image.ops;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import jigl.image.*;
import jigl.image.exceptions.*;
import jigl.image.types.*;

public class ShiftTest {
    private Shift shift;

    @Before
    public void setUp() {
        shift = new Shift(0, 0, Shift.NOWRAP);
    }

    @Test
    public void testApplyGrayImage() {
        GrayImage image = new GrayImage(3, 3);
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, 1);
            }
        }

        GrayImage expected = new GrayImage(3, 3);
        for (int y = 0; y < expected.Y(); y++) {
            for (int x = 0; x < expected.X(); x++) {
                expected.set(x, y, 1);
            }
        }

        GrayImage result = shift.apply(image);
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                assertEquals(expected.get(x, y), result.get(x, y));
            }
        }
    }

    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage image = new RealGrayImage(3, 3);
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, 1.0f);
            }
        }

        RealGrayImage expected = new RealGrayImage(3, 3);
        for (int y = 0; y < expected.Y(); y++) {
            for (int x = 0; x < expected.X(); x++) {
                expected.set(x, y, 1.0f);
            }
        }

        RealGrayImage result = shift.apply(image);
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                assertEquals(expected.get(x, y), result.get(x, y), 0.001f);
            }
        }
    }

    @Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage(3, 3);
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, 1, 1, 1);
            }
        }

        ColorImage expected = new ColorImage(3, 3);
        for (int y = 0; y < expected.Y(); y++) {
            for (int x = 0; x < expected.X(); x++) {
                expected.set(x, y, 1, 1, 1);
            }
        }

        ColorImage result = shift.apply(image);
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                assertEquals(expected.get(x, y, 0), result.get(x, y, 0));
                assertEquals(expected.get(x, y, 1), result.get(x, y, 1));
                assertEquals(expected.get(x, y, 2), result.get(x, y, 2));
            }
        }
    }

    @Test
    public void testApplyRealColorImage() {
        RealColorImage image = new RealColorImage(3, 3);
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, 1.0f, 1.0f, 1.0f);
            }
        }

        RealColorImage expected = new RealColorImage(3, 3);
        for (int y = 0; y < expected.Y(); y++) {
            for (int x = 0; x < expected.X(); x++) {
                expected.set(x, y, 1.0f, 1.0f, 1.0f);
            }
        }

        RealColorImage result = shift.apply(image);
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                assertEquals(expected.get(x, y, 0), result.get(x, y, 0), 0.001f);
                assertEquals(expected.get(x, y, 1), result.get(x, y, 1), 0.001f);
                assertEquals(expected.get(x, y, 2), result.get(x, y, 2), 0.001f);
            }
        }
    }
}