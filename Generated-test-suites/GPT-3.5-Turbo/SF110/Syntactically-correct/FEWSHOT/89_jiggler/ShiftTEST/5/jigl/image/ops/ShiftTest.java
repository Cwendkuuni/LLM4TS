package jigl.image.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.ColorImage;
import jigl.image.RealColorImage;

import org.junit.Test;

public class ShiftTest {
    @Test
    public void testApply_GrayImage() {
        // Given
        GrayImage image = new GrayImage(3, 3);
        Shift shift = new Shift(1, 1, Shift.WRAP);

        // When
        GrayImage result = shift.apply(image);

        // Then
        assertNotNull(result);
        assertEquals(3, result.X());
        assertEquals(3, result.Y());
    }

    @Test
    public void testApply_RealGrayImage() {
        // Given
        RealGrayImage image = new RealGrayImage(3, 3);
        Shift shift = new Shift(1, 1, Shift.WRAP);

        // When
        RealGrayImage result = shift.apply(image);

        // Then
        assertNotNull(result);
        assertEquals(3, result.X());
        assertEquals(3, result.Y());
    }

    @Test
    public void testApply_ColorImage() {
        // Given
        ColorImage image = new ColorImage(3, 3);
        Shift shift = new Shift(1, 1, Shift.WRAP);

        // When
        ColorImage result = shift.apply(image);

        // Then
        assertNotNull(result);
        assertEquals(3, result.X());
        assertEquals(3, result.Y());
    }

    @Test
    public void testApply_RealColorImage() {
        // Given
        RealColorImage image = new RealColorImage(3, 3);
        Shift shift = new Shift(1, 1, Shift.WRAP);

        // When
        RealColorImage result = shift.apply(image);

        // Then
        assertNotNull(result);
        assertEquals(3, result.X());
        assertEquals(3, result.Y());
    }
}