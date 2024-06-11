package jigl.image.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import jigl.image.ColorImage;
import jigl.image.GrayImage;
import jigl.image.RealColorImage;
import jigl.image.RealGrayImage;

public class ShiftTest {
    private Shift shift;

    @Before
    public void setUp() {
        shift = new Shift(1, 2, 1);
    }

    @Test
    public void testConstructor() {
        assertEquals(1, shift.xoffset);
        assertEquals(2, shift.yoffset);
        assertEquals(true, shift.wrap);
    }

    @Test
    public void testApply_GrayImage() {
        GrayImage image = new GrayImage(4, 4);
        image.set(0, 0, 1);
        image.set(1, 1, 2);
        image.set(2, 2, 3);
        image.set(3, 3, 4);

        GrayImage result = shift.apply(image);

        assertNotNull(result);
        assertEquals(4, result.X());
        assertEquals(4, result.Y());
        assertEquals(0, result.get(0, 0));
        assertEquals(0, result.get(1, 0));
        assertEquals(0, result.get(0, 1));
        assertEquals(1, result.get(3, 1));
        assertEquals(0, result.get(3, 3));
        assertEquals(1, result.get(0, 2));
        assertEquals(2, result.get(1, 2));
        assertEquals(0, result.get(2, 2));
        assertEquals(0, result.get(2, 3));
        assertEquals(3, result.get(3, 3));
    }

    @Test
    public void testApply_RealGrayImage() {
        RealGrayImage image = new RealGrayImage(4, 4);
        image.set(0, 0, 0.1f);
        image.set(1, 1, 0.2f);
        image.set(2, 2, 0.3f);
        image.set(3, 3, 0.4f);

        RealGrayImage result = shift.apply(image);

        assertNotNull(result);
        assertEquals(4, result.X());
        assertEquals(4, result.Y());
        assertEquals(0.0f, result.get(0, 0), 0.00001f);
        assertEquals(0.0f, result.get(1, 0), 0.00001f);
        assertEquals(0.0f, result.get(0, 1), 0.00001f);
        assertEquals(0.1f, result.get(3, 1), 0.00001f);
        assertEquals(0.0f, result.get(3, 3), 0.00001f);
        assertEquals(0.1f, result.get(0, 2), 0.00001f);
        assertEquals(0.2f, result.get(1, 2), 0.00001f);
        assertEquals(0.0f, result.get(2, 2), 0.00001f);
        assertEquals(0.0f, result.get(2, 3), 0.00001f);
        assertEquals(0.3f, result.get(3, 3), 0.00001f);
    }

    @Test
    public void testApply_ColorImage() {
        ColorImage image = new ColorImage(4, 4);
        image.set(0, 0, 1, 2, 3);
        image.set(1, 1, 4, 5, 6);
        image.set(2, 2, 7, 8, 9);
        image.set(3, 3, 10, 11, 12);

        ColorImage result = shift.apply(image);

        assertNotNull(result);
        assertEquals(4, result.X());
        assertEquals(4, result.Y());
        assertEquals(0, result.get(0, 0, 0));
        assertEquals(0, result.get(1, 0, 0));
        assertEquals(0, result.get(0, 1, 0));
        assertEquals(1, result.get(3, 1, 0));
        assertEquals(0, result.get(3, 3, 0));
        assertEquals(1, result.get(0, 2, 0));
        assertEquals(2, result.get(1, 2, 0));
        assertEquals(0, result.get(2, 2, 0));
        assertEquals(0, result.get(2, 3, 0));
        assertEquals(3, result.get(3, 3, 0));
        assertEquals(0, result.get(0, 0, 1));
        assertEquals(0, result.get(1, 0, 1));
        assertEquals(0, result.get(0, 1, 1));
        assertEquals(2, result.get(3, 1, 1));
        assertEquals(0, result.get(3, 3, 1));
        assertEquals(2, result.get(0, 2, 1));
        assertEquals(3, result.get(1, 2, 1));
        assertEquals(0, result.get(2, 2, 1));
        assertEquals(0, result.get(2, 3, 1));
        assertEquals(4, result.get(3, 3, 1));
        assertEquals(0, result.get(0, 0, 2));
        assertEquals(0, result.get(1, 0, 2));
        assertEquals(0, result.get(0, 1, 2));
        assertEquals(3, result.get(3, 1, 2));
        assertEquals(0, result.get(3, 3, 2));
        assertEquals(3, result.get(0, 2, 2));
        assertEquals(4, result.get(1, 2, 2));
        assertEquals(0, result.get(2, 2, 2));
        assertEquals(0, result.get(2, 3, 2));
        assertEquals(5, result.get(3, 3, 2));
    }

    @Test
    public void testApply_RealColorImage() {
        RealColorImage image = new RealColorImage(4, 4);
        image.set(0, 0, 0.1f, 0.2f, 0.3f);
        image.set(1, 1, 0.4f, 0.5f, 0.6f);
        image.set(2, 2, 0.7f, 0.8f, 0.9f);
        image.set(3, 3, 1.0f, 1.1f, 1.2f);

        RealColorImage result = shift.apply(image);

        assertNotNull(result);
        assertEquals(4, result.X());
        assertEquals(4, result.Y());
        assertEquals(0.0f, result.get(0, 0, 0), 0.00001f);
        assertEquals(0.0f, result.get(1, 0, 0), 0.00001f);
        assertEquals(0.0f, result.get(0, 1, 0), 0.00001f);
        assertEquals(0.4f, result.get(3, 1, 0), 0.00001f);
        assertEquals(0.0f, result.get(3, 3, 0), 0.00001f);
        assertEquals(0.4f, result.get(0, 2, 0), 0.00001f);
        assertEquals(0.5f, result.get(1, 2, 0), 0.00001f);
        assertEquals(0.0f, result.get(2, 2, 0), 0.00001f);
        assertEquals(0.0f, result.get(2, 3, 0), 0.00001f);
        assertEquals(0.7f, result.get(3, 3, 0), 0.00001f);
        assertEquals(0.0f, result.get(0, 0, 1), 0.00001f);
        assertEquals(0.0f, result.get(1, 0, 1), 0.00001f);
        assertEquals(0.0f, result.get(0, 1, 1), 0.00001f);
        assertEquals(0.8f, result.get(3, 1, 1), 0.00001f);
        assertEquals(0.0f, result.get(3, 3, 1), 0.00001f);
        assertEquals(0.8f, result.get(0, 2, 1), 0.00001f);
        assertEquals(0.9f, result.get(1, 2, 1), 0.00001f);
        assertEquals(0.0f, result.get(2, 2, 1), 0.00001f);
        assertEquals(0.0f, result.get(2, 3, 1), 0.00001f);
        assertEquals(1.0f, result.get(3, 3, 1), 0.00001f);
        assertEquals(0.0f, result.get(0, 0, 2), 0.00001f);
        assertEquals(0.0f, result.get(1, 0, 2), 0.00001f);
        assertEquals(0.0f, result.get(0, 1, 2), 0.00001f);
        assertEquals(0.9f, result.get(3, 1, 2), 0.00001f);
        assertEquals(0.0f, result.get(3, 3, 2), 0.00001f);
        assertEquals(0.9f, result.get(0, 2, 2), 0.00001f);
        assertEquals(1.0f, result.get(1, 2, 2), 0.00001f);
        assertEquals(0.0f, result.get(2, 2, 2), 0.00001f);
        assertEquals(0.0f, result.get(2, 3, 2), 0.00001f);
        assertEquals(1.1f, result.get(3, 3, 2), 0.00001f);
    }
}