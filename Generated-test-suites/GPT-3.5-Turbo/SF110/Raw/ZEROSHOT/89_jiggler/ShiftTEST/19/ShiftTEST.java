package jigl.image.ops;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

import jigl.image.GrayImage;
import jigl.image.exceptions.ImageNotSupportedException;
import jigl.image.types.ColorImage;
import jigl.image.types.RealColorImage;
import jigl.image.types.RealGrayImage;

public class ShiftTest {

    private Shift shift;
    private GrayImage grayImage;
    private RealGrayImage realGrayImage;
    private ColorImage colorImage;
    private RealColorImage realColorImage;

    @Before
    public void setUp() {
        shift = new Shift(2, 3, Shift.WRAP);
        grayImage = new GrayImage(3, 3);
        realGrayImage = new RealGrayImage(3, 3);
        colorImage = new ColorImage(3, 3);
        realColorImage = new RealColorImage(3, 3);
    }

    @After
    public void tearDown() {
        shift = null;
        grayImage = null;
        realGrayImage = null;
        colorImage = null;
        realColorImage = null;
    }

    @Test
    public void testApply_grayImage_wrap() {
        grayImage.set(0, 0, 100);
        grayImage.set(1, 1, 200);
        grayImage.set(2, 2, 50);

        GrayImage result = shift.apply(grayImage);

        assertEquals(100, result.get(2, 3));
        assertEquals(200, result.get(0, 1));
        assertEquals(50, result.get(1, 2));
    }

    @Test
    public void testApply_grayImage_noWrap() {
        grayImage.set(0, 0, 100);
        grayImage.set(1, 1, 200);
        grayImage.set(2, 2, 50);

        shift = new Shift(2, 3, Shift.NOWRAP);

        GrayImage result = shift.apply(grayImage);

        assertEquals(0, result.get(2, 3));
        assertEquals(0, result.get(0, 1));
        assertEquals(0, result.get(1, 2));
    }

    @Test
    public void testApply_realGrayImage_wrap() {
        realGrayImage.set(0, 0, 0.5f);
        realGrayImage.set(1, 1, 0.8f);
        realGrayImage.set(2, 2, 0.2f);

        RealGrayImage result = shift.apply(realGrayImage);

        assertEquals(0.5f, result.get(2, 3), 0.001);
        assertEquals(0.8f, result.get(0, 1), 0.001);
        assertEquals(0.2f, result.get(1, 2), 0.001);
    }

    @Test
    public void testApply_realGrayImage_noWrap() {
        realGrayImage.set(0, 0, 0.5f);
        realGrayImage.set(1, 1, 0.8f);
        realGrayImage.set(2, 2, 0.2f);

        shift = new Shift(2, 3, Shift.NOWRAP);

        RealGrayImage result = shift.apply(realGrayImage);

        assertEquals(0, result.get(2, 3), 0.001);
        assertEquals(0, result.get(0, 1), 0.001);
        assertEquals(0, result.get(1, 2), 0.001);
    }

    @Test
    public void testApply_colorImage_wrap() {
        colorImage.set(0, 0, 100, 50, 25);
        colorImage.set(1, 1, 200, 150, 100);
        colorImage.set(2, 2, 50, 75, 25);

        ColorImage result = shift.apply(colorImage);

        assertEquals(100, result.get(2, 3, 0));
        assertEquals(50, result.get(2, 3, 1));
        assertEquals(25, result.get(2, 3, 2));

        assertEquals(200, result.get(0, 1, 0));
        assertEquals(150, result.get(0, 1, 1));
        assertEquals(100, result.get(0, 1, 2));

        assertEquals(50, result.get(1, 2, 0));
        assertEquals(75, result.get(1, 2, 1));
        assertEquals(25, result.get(1, 2, 2));
    }

    @Test
    public void testApply_colorImage_noWrap() {
        colorImage.set(0, 0, 100, 50, 25);
        colorImage.set(1, 1, 200, 150, 100);
        colorImage.set(2, 2, 50, 75, 25);

        shift = new Shift(2, 3, Shift.NOWRAP);

        ColorImage result = shift.apply(colorImage);

        assertEquals(0, result.get(2, 3, 0));
        assertEquals(0, result.get(2, 3, 1));
        assertEquals(0, result.get(2, 3, 2));

        assertEquals(0, result.get(0, 1, 0));
        assertEquals(0, result.get(0, 1, 1));
        assertEquals(0, result.get(0, 1, 2));

        assertEquals(0, result.get(1, 2, 0));
        assertEquals(0, result.get(1, 2, 1));
        assertEquals(0, result.get(1, 2, 2));
    }

    @Test
    public void testApply_realColorImage_wrap() {
        realColorImage.set(0, 0, 0.5f, 0.2f, 0.8f);
        realColorImage.set(1, 1, 0.8f, 0.6f, 0.4f);
        realColorImage.set(2, 2, 0.2f, 0.4f, 0.6f);

        RealColorImage result = shift.apply(realColorImage);

        assertEquals(0.5f, result.get(2, 3, 0), 0.001);
        assertEquals(0.2f, result.get(2, 3, 1), 0.001);
        assertEquals(0.8f, result.get(2, 3, 2), 0.001);

        assertEquals(0.8f, result.get(0, 1, 0), 0.001);
        assertEquals(0.6f, result.get(0, 1, 1), 0.001);
        assertEquals(0.4f, result.get(0, 1, 2), 0.001);

        assertEquals(0.2f, result.get(1, 2, 0), 0.001);
        assertEquals(0.4f, result.get(1, 2, 1), 0.001);
        assertEquals(0.6f, result.get(1, 2, 2), 0.001);
    }

    @Test
    public void testApply_realColorImage_noWrap() {
        realColorImage.set(0, 0, 0.5f, 0.2f, 0.8f);
        realColorImage.set(1, 1, 0.8f, 0.6f, 0.4f);
        realColorImage.set(2, 2, 0.2f, 0.4f, 0.6f);

        shift = new Shift(2, 3, Shift.NOWRAP);

        RealColorImage result = shift.apply(realColorImage);

        assertEquals(0, result.get(2, 3, 0), 0.001);
        assertEquals(0, result.get(2, 3, 1), 0.001);
        assertEquals(0, result.get(2, 3, 2), 0.001);

        assertEquals(0, result.get(0, 1, 0), 0.001);
        assertEquals(0, result.get(0, 1, 1), 0.001);
        assertEquals(0, result.get(0, 1, 2), 0.001);

        assertEquals(0, result.get(1, 2, 0), 0.001);
        assertEquals(0, result.get(1, 2, 1), 0.001);
        assertEquals(0, result.get(1, 2, 2), 0.001);
    }
}