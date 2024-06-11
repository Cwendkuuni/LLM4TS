package jigl.image.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import jigl.image.RealColorImage;
import jigl.image.ColorImage;
import jigl.image.RealGrayImage;
import jigl.image.GrayImage;
import jigl.image.SimpleOperator;
import jigl.image.exceptions.ImageNotSupportedException;
import jigl.image.exceptions.ProcessingException;
import jigl.image.types.ImageNotSupportedException;

public class ShiftTest {

    @Test
    public void testConstructor() {
        int x = 2;
        int y = 3;
        int wrap = 0;
        Shift shift = new Shift(x, y, wrap);

        assertEquals(x, shift.xoffset);
        assertEquals(y, shift.yoffset);
        assertEquals(false, shift.wrap);

        wrap = 1;
        shift = new Shift(x, y, wrap);

        assertEquals(x, shift.xoffset);
        assertEquals(y, shift.yoffset);
        assertEquals(true, shift.wrap);
    }

    @Test
    public void testApplyGrayImage() {
        int x = 2;
        int y = 3;
        int wrap = 0;
        Shift shift = new Shift(x, y, wrap);

        GrayImage image = new GrayImage(4, 5);
        GrayImage result = null;

        try {
            result = shift.apply(image);
        } catch (ProcessingException e) {
            fail("ProcessingException should not be thrown");
        }

        assertNotNull(result);
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
        assertEquals(image.getType(), result.getType());
    }

    @Test
    public void testApplyRealGrayImage() {
        int x = 2;
        int y = 3;
        int wrap = 0;
        Shift shift = new Shift(x, y, wrap);

        RealGrayImage image = new RealGrayImage(4, 5);
        RealGrayImage result = null;

        try {
            result = shift.apply(image);
        } catch (ProcessingException e) {
            fail("ProcessingException should not be thrown");
        }

        assertNotNull(result);
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
        assertEquals(image.getType(), result.getType());
    }

    @Test
    public void testApplyColorImage() {
        int x = 2;
        int y = 3;
        int wrap = 0;
        Shift shift = new Shift(x, y, wrap);

        ColorImage image = new ColorImage(4, 5);
        ColorImage result = null;

        try {
            result = shift.apply(image);
        } catch (ProcessingException e) {
            fail("ProcessingException should not be thrown");
        }

        assertNotNull(result);
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
        assertEquals(image.getType(), result.getType());
    }

    @Test
    public void testApplyRealColorImage() {
        int x = 2;
        int y = 3;
        int wrap = 0;
        Shift shift = new Shift(x, y, wrap);

        RealColorImage image = new RealColorImage(4, 5);
        RealColorImage result = null;

        try {
            result = shift.apply(image);
        } catch (ProcessingException e) {
            fail("ProcessingException should not be thrown");
        }

        assertNotNull(result);
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
        assertEquals(image.getType(), result.getType());
    }

}