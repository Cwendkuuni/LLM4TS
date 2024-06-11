package jigl.image.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import jigl.image.ColorImage;
import jigl.image.GrayImage;
import jigl.image.RealColorImage;
import jigl.image.RealGrayImage;
import org.junit.Test;

public class ShiftTest {

    @Test
    public void testApplyGrayImageWithoutWrap() {
        Shift shift = new Shift(1, 1, 0);
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 1, 2);
        image.set(2, 2, 3);
        GrayImage expected = new GrayImage(3, 3);
        expected.set(0, 0, 0);
        expected.set(1, 1, 1);
        expected.set(2, 2, 0);

        GrayImage result = shift.apply(image);

        assertEquals(expected, result);
    }

    @Test
    public void testApplyGrayImageWithWrap() {
        Shift shift = new Shift(1, 1, 1);
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 1, 2);
        image.set(2, 2, 3);
        GrayImage expected = new GrayImage(3, 3);
        expected.set(0, 0, 3);
        expected.set(1, 1, 1);
        expected.set(2, 2, 2);

        GrayImage result = shift.apply(image);

        assertEquals(expected, result);
    }

    @Test
    public void testApplyRealGrayImageWithoutWrap() {
        Shift shift = new Shift(1, 1, 0);
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.0f);
        image.set(1, 1, 2.0f);
        image.set(2, 2, 3.0f);
        RealGrayImage expected = new RealGrayImage(3, 3);
        expected.set(0, 0, 0.0f);
        expected.set(1, 1, 1.0f);
        expected.set(2, 2, 0.0f);

        RealGrayImage result = shift.apply(image);

        assertEquals(expected, result);
    }

    @Test
    public void testApplyRealGrayImageWithWrap() {
        Shift shift = new Shift(1, 1, 1);
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.0f);
        image.set(1, 1, 2.0f);
        image.set(2, 2, 3.0f);
        RealGrayImage expected = new RealGrayImage(3, 3);
        expected.set(0, 0, 3.0f);
        expected.set(1, 1, 1.0f);
        expected.set(2, 2, 2.0f);

        RealGrayImage result = shift.apply(image);

        assertEquals(expected, result);
    }

    @Test
    public void testApplyColorImage() {
        Shift shift = new Shift(1, 1, 0);
        ColorImage image = new ColorImage(3, 3);
        ColorImage expected = new ColorImage(3, 3);
        expected.setPlane(0, new GrayImage(3, 3));
        expected.setPlane(1, new GrayImage(3, 3));
        expected.setPlane(2, new GrayImage(3, 3));

        ColorImage result = shift.apply(image);

        assertEquals(expected, result);
    }

    @Test
    public void testApplyRealColorImage() {
        Shift shift = new Shift(1, 1, 0);
        RealColorImage image = new RealColorImage(3, 3);
        RealColorImage expected = new RealColorImage(3, 3);
        expected.setPlane(0, new RealGrayImage(3, 3));
        expected.setPlane(1, new RealGrayImage(3, 3));
        expected.setPlane(2, new RealGrayImage(3, 3));

        RealColorImage result = shift.apply(image);

        assertEquals(expected, result);
    }

    @Test
    public void testShiftConstructorWithWrap() {
        Shift shift = new Shift(1, 1, 1);
        assertTrue(shift.wrap);
    }

    @Test
    public void testShiftConstructorWithoutWrap() {
        Shift shift = new Shift(1, 1, 0);
        assertTrue(!shift.wrap);
    }

    @Test
    public void testShiftConstructorWithZeroOffset() {
        Shift shift = new Shift(0, 0, 1);
        assertEquals(0, shift.xoffset);
        assertEquals(0, shift.yoffset);
    }

    @Test
    public void testShiftConstructorWithPositiveOffset() {
        Shift shift = new Shift(1, 1, 1);
        assertEquals(1, shift.xoffset);
        assertEquals(1, shift.yoffset);
    }

    @Test
    public void testShiftConstructorWithNegativeOffset() {
        Shift shift = new Shift(-1, -1, 1);
        assertEquals(-1, shift.xoffset);
        assertEquals(-1, shift.yoffset);
    }
}