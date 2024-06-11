package jigl.image.ops;

import org.junit.*;
import static org.junit.Assert.*;

import jigl.image.*;

public class ShiftTest {

    @Test
    public void testApply_GrayImage() {
        // Test with wrap = true
        Shift shift = new Shift(1, 1, 1);
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 1, 2);
        image.set(2, 2, 3);

        GrayImage result = shift.apply(image);

        GrayImage expected = new GrayImage(3, 3);
        expected.set(1, 1, 1);
        expected.set(2, 2, 2);
        expected.set(0, 0, 3);

        assertEquals(expected, result);

        // Test with wrap = false
        shift = new Shift(1, 1, 0);

        result = shift.apply(image);

        expected = new GrayImage(3, 3);
        expected.set(1, 1, 2);

        assertEquals(expected, result);
    }

    @Test
    public void testApply_RealGrayImage() {
        // Test with wrap = true
        Shift shift = new Shift(1, 1, 1);
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.0f);
        image.set(1, 1, 2.0f);
        image.set(2, 2, 3.0f);

        RealGrayImage result = shift.apply(image);

        RealGrayImage expected = new RealGrayImage(3, 3);
        expected.set(1, 1, 1.0f);
        expected.set(2, 2, 2.0f);
        expected.set(0, 0, 3.0f);

        assertEquals(expected, result);

        // Test with wrap = false
        shift = new Shift(1, 1, 0);

        result = shift.apply(image);

        expected = new RealGrayImage(3, 3);
        expected.set(1, 1, 2.0f);

        assertEquals(expected, result);
    }

    @Test
    public void testApply_ColorImage() {
        // Test with wrap = true
        Shift shift = new Shift(1, 1, 1);
        ColorImage image = new ColorImage(3, 3);
        GrayImage plane0 = new GrayImage(3, 3);
        GrayImage plane1 = new GrayImage(3, 3);
        GrayImage plane2 = new GrayImage(3, 3);
        plane0.set(0, 0, 1);
        plane1.set(1, 1, 2);
        plane2.set(2, 2, 3);
        image.setPlane(0, plane0);
        image.setPlane(1, plane1);
        image.setPlane(2, plane2);

        ColorImage result = shift.apply(image);

        ColorImage expected = new ColorImage(3, 3);
        GrayImage expectedPlane0 = new GrayImage(3, 3);
        GrayImage expectedPlane1 = new GrayImage(3, 3);
        GrayImage expectedPlane2 = new GrayImage(3, 3);
        expectedPlane0.set(1, 1, 1);
        expectedPlane1.set(2, 2, 2);
        expectedPlane2.set(0, 0, 3);
        expected.setPlane(0, expectedPlane0);
        expected.setPlane(1, expectedPlane1);
        expected.setPlane(2, expectedPlane2);

        assertEquals(expected, result);

        // Test with wrap = false
        shift = new Shift(1, 1, 0);

        result = shift.apply(image);

        expected = new ColorImage(3, 3);
        expectedPlane1 = new GrayImage(3, 3);
        expectedPlane1.set(1, 1, 2);
        expected.setPlane(1, expectedPlane1);

        assertEquals(expected, result);
    }

    @Test
    public void testApply_RealColorImage() {
        // Test with wrap = true
        Shift shift = new Shift(1, 1, 1);
        RealColorImage image = new RealColorImage(3, 3);
        RealGrayImage plane0 = new RealGrayImage(3, 3);
        RealGrayImage plane1 = new RealGrayImage(3, 3);
        RealGrayImage plane2 = new RealGrayImage(3, 3);
        plane0.set(0, 0, 1.0f);
        plane1.set(1, 1, 2.0f);
        plane2.set(2, 2, 3.0f);
        image.setPlane(0, plane0);
        image.setPlane(1, plane1);
        image.setPlane(2, plane2);

        RealColorImage result = shift.apply(image);

        RealColorImage expected = new RealColorImage(3, 3);
        RealGrayImage expectedPlane0 = new RealGrayImage(3, 3);
        RealGrayImage expectedPlane1 = new RealGrayImage(3, 3);
        RealGrayImage expectedPlane2 = new RealGrayImage(3, 3);
        expectedPlane0.set(1, 1, 1.0f);
        expectedPlane1.set(2, 2, 2.0f);
        expectedPlane2.set(0, 0, 3.0f);
        expected.setPlane(0, expectedPlane0);
        expected.setPlane(1, expectedPlane1);
        expected.setPlane(2, expectedPlane2);

        assertEquals(expected, result);

        // Test with wrap = false
        shift = new Shift(1, 1, 0);

        result = shift.apply(image);

        expected = new RealColorImage(3, 3);
        expectedPlane1 = new RealGrayImage(3, 3);
        expectedPlane1.set(1, 1, 2.0f);
        expected.setPlane(1, expectedPlane1);

        assertEquals(expected, result);
    }
}