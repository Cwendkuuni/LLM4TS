package jigl.image.ops;

import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.ColorImage;
import jigl.image.RealColorImage;
import junit.framework.TestCase;

public class ShiftTest extends TestCase {

    public void testApply_GrayImage_wrap() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        GrayImage image = new GrayImage(3, 3);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                image.set(x, y, x + y);
            }
        }
        GrayImage result = shift.apply(image);
        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(1, 0));
        assertEquals(0, result.get(2, 0));
        assertEquals(3, result.get(0, 1));
        assertEquals(4, result.get(1, 1));
        assertEquals(5, result.get(2, 1));
        assertEquals(6, result.get(0, 2));
        assertEquals(7, result.get(1, 2));
        assertEquals(8, result.get(2, 2));
    }

    public void testApply_GrayImage_noWrap() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage image = new GrayImage(3, 3);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                image.set(x, y, x + y);
            }
        }
        GrayImage result = shift.apply(image);
        assertEquals(4, result.get(0, 0));
        assertEquals(5, result.get(1, 0));
        assertEquals(0, result.get(2, 0));
        assertEquals(7, result.get(0, 1));
        assertEquals(8, result.get(1, 1));
        assertEquals(0, result.get(2, 1));
        assertEquals(1, result.get(0, 2));
        assertEquals(2, result.get(1, 2));
        assertEquals(0, result.get(2, 2));
    }

    public void testApply_RealGrayImage_wrap() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealGrayImage image = new RealGrayImage(3, 3);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                image.set(x, y, x + y);
            }
        }
        RealGrayImage result = shift.apply(image);
        assertEquals(1.0f, result.get(0, 0));
        assertEquals(2.0f, result.get(1, 0));
        assertEquals(0.0f, result.get(2, 0));
        assertEquals(3.0f, result.get(0, 1));
        assertEquals(4.0f, result.get(1, 1));
        assertEquals(5.0f, result.get(2, 1));
        assertEquals(6.0f, result.get(0, 2));
        assertEquals(7.0f, result.get(1, 2));
        assertEquals(8.0f, result.get(2, 2));
    }

    public void testApply_RealGrayImage_noWrap() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        RealGrayImage image = new RealGrayImage(3, 3);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                image.set(x, y, x + y);
            }
        }
        RealGrayImage result = shift.apply(image);
        assertEquals(4.0f, result.get(0, 0));
        assertEquals(5.0f, result.get(1, 0));
        assertEquals(0.0f, result.get(2, 0));
        assertEquals(7.0f, result.get(0, 1));
        assertEquals(8.0f, result.get(1, 1));
        assertEquals(0.0f, result.get(2, 1));
        assertEquals(1.0f, result.get(0, 2));
        assertEquals(2.0f, result.get(1, 2));
        assertEquals(0.0f, result.get(2, 2));
    }

    public void testApply_ColorImage_wrap() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        ColorImage image = new ColorImage(3, 3);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                image.set(x, y, new int[]{x + y, 0, 0});
            }
        }
        ColorImage result = shift.apply(image);
        assertEquals(1, result.get(0, 0)[0]);
        assertEquals(2, result.get(1, 0)[0]);
        assertEquals(0, result.get(2, 0)[0]);
        assertEquals(3, result.get(0, 1)[0]);
        assertEquals(4, result.get(1, 1)[0]);
        assertEquals(5, result.get(2, 1)[0]);
        assertEquals(6, result.get(0, 2)[0]);
        assertEquals(7, result.get(1, 2)[0]);
        assertEquals(8, result.get(2, 2)[0]);
    }

    public void testApply_ColorImage_noWrap() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        ColorImage image = new ColorImage(3, 3);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                image.set(x, y, new int[]{x + y, 0, 0});
            }
        }
        ColorImage result = shift.apply(image);
        assertEquals(4, result.get(0, 0)[0]);
        assertEquals(5, result.get(1, 0)[0]);
        assertEquals(0, result.get(2, 0)[0]);
        assertEquals(7, result.get(0, 1)[0]);
        assertEquals(8, result.get(1, 1)[0]);
        assertEquals(0, result.get(2, 1)[0]);
        assertEquals(1, result.get(0, 2)[0]);
        assertEquals(2, result.get(1, 2)[0]);
        assertEquals(0, result.get(2, 2)[0]);
    }

    public void testApply_RealColorImage_wrap() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealColorImage image = new RealColorImage(3, 3);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                image.set(x, y, new float[]{x + y, 0.0f, 0.0f});
            }
        }
        RealColorImage result = shift.apply(image);
        assertEquals(1.0f, result.get(0, 0)[0]);
        assertEquals(2.0f, result.get(1, 0)[0]);
        assertEquals(0.0f, result.get(2, 0)[0]);
        assertEquals(3.0f, result.get(0, 1)[0]);
        assertEquals(4.0f, result.get(1, 1)[0]);
        assertEquals(5.0f, result.get(2, 1)[0]);
        assertEquals(6.0f, result.get(0, 2)[0]);
        assertEquals(7.0f, result.get(1, 2)[0]);
        assertEquals(8.0f, result.get(2, 2)[0]);
    }

    public void testApply_RealColorImage_noWrap() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        RealColorImage image = new RealColorImage(3, 3);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                image.set(x, y, new float[]{x + y, 0.0f, 0.0f});
            }
        }
        RealColorImage result = shift.apply(image);
        assertEquals(4.0f, result.get(0, 0)[0]);
        assertEquals(5.0f, result.get(1, 0)[0]);
        assertEquals(0.0f, result.get(2, 0)[0]);
        assertEquals(7.0f, result.get(0, 1)[0]);
        assertEquals(8.0f, result.get(1, 1)[0]);
        assertEquals(0.0f, result.get(2, 1)[0]);
        assertEquals(1.0f, result.get(0, 2)[0]);
        assertEquals(2.0f, result.get(1, 2)[0]);
        assertEquals(0.0f, result.get(2, 2)[0]);
    }
}