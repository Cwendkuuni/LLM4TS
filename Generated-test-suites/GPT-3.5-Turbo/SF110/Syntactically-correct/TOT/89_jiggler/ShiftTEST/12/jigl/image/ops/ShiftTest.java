package jigl.image.ops;

import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.ColorImage;
import jigl.image.RealColorImage;
import jigl.image.Image;
import jigl.image.ImageTestCase;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ShiftTest extends ImageTestCase {

    private Shift operator;
    private Image image;
    private Image result;

    @Before
    public void setUp() {
        operator = new Shift(1, 1, 1);
        image = createTestImage();
        result = operator.apply(image);
    }

    @Test
    public void testApply_onGrayImage_wrap_enabled() {
        // Typical case: Shift x and y offsets are within the bounds of the image
        assertPixelEquals(image.get(1, 1), result.get(0, 0));

        // Edge case: Shift x and y offsets are at the maximum bounds of the image
        assertPixelEquals(image.get(image.X() - 1, image.Y() - 1), result.get(0, 0));

        // Error scenario: Shift x and y offsets are outside the bounds of the image
        assertEquals(0, result.get(1, 1));
    }

    @Test
    public void testApply_onGrayImage_wrap_disabled() {
        Shift operator = new Shift(1, 1, 0);
        Image result = operator.apply(image);

        // Typical case: Shift x and y offsets are within the bounds of the image
        assertPixelEquals(image.get(1, 1), result.get(2, 2));

        // Edge case: Shift x and y offsets are at the maximum bounds of the image
        assertEquals(0, result.get(image.X() - 1, image.Y() - 1));

        // Error scenario: Shift x and y offsets are outside the bounds of the image
        assertEquals(0, result.get(0, 0));
    }

    @Test
    public void testApply_onRealGrayImage_wrap_enabled() {
        // Typical case: Shift x and y offsets are within the bounds of the image
        assertPixelEquals(image.get(1, 1), result.get(0, 0));

        // Edge case: Shift x and y offsets are at the maximum bounds of the image
        assertPixelEquals(image.get(image.X() - 1, image.Y() - 1), result.get(0, 0));

        // Error scenario: Shift x and y offsets are outside the bounds of the image
        assertEquals(0.0f, result.get(1, 1), 0.00001);
    }

    @Test
    public void testApply_onRealGrayImage_wrap_disabled() {
        Shift operator = new Shift(1, 1, 0);
        Image result = operator.apply(image);

        // Typical case: Shift x and y offsets are within the bounds of the image
        assertPixelEquals(image.get(1, 1), result.get(2, 2));

        // Edge case: Shift x and y offsets are at the maximum bounds of the image
        assertEquals(0.0f, result.get(image.X() - 1, image.Y() - 1), 0.00001);

        // Error scenario: Shift x and y offsets are outside the bounds of the image
        assertEquals(0.0f, result.get(0, 0), 0.00001);
    }

    @Test
    public void testApply_onColorImage_wrap_enabled() {
        // Typical case: Shift x and y offsets are within the bounds of the image
        assertPixelEquals(image.get(1, 1), result.get(0, 0));

        // Edge case: Shift x and y offsets are at the maximum bounds of the image
        assertPixelEquals(image.get(image.X() - 1, image.Y() - 1), result.get(0, 0));

        // Error scenario: Shift x and y offsets are outside the bounds of the image
        assertEquals(0, result.get(1, 1, 0));
    }

    @Test
    public void testApply_onColorImage_wrap_disabled() {
        Shift operator = new Shift(1, 1, 0);
        Image result = operator.apply(image);

        // Typical case: Shift x and y offsets are within the bounds of the image
        assertPixelEquals(image.get(1, 1), result.get(2, 2));

        // Edge case: Shift x and y offsets are at the maximum bounds of the image
        assertEquals(0, result.get(image.X() - 1, image.Y() - 1, 0));

        // Error scenario: Shift x and y offsets are outside the bounds of the image
        assertEquals(0, result.get(0, 0, 0));
    }

    @Test
    public void testApply_onRealColorImage_wrap_enabled() {
        // Typical case: Shift x and y offsets are within the bounds of the image
        assertPixelEquals(image.get(1, 1), result.get(0, 0));

        // Edge case: Shift x and y offsets are at the maximum bounds of the image
        assertPixelEquals(image.get(image.X() - 1, image.Y() - 1), result.get(0, 0));

        // Error scenario: Shift x and y offsets are outside the bounds of the image
        assertEquals(0.0f, result.get(1, 1, 0), 0.00001);
    }

    @Test
    public void testApply_onRealColorImage_wrap_disabled() {
        Shift operator = new Shift(1, 1, 0);
        Image result = operator.apply(image);

        // Typical case: Shift x and y offsets are within the bounds of the image
        assertPixelEquals(image.get(1, 1), result.get(2, 2));

        // Edge case: Shift x and y offsets are at the maximum bounds of the image
        assertEquals(0.0f, result.get(image.X() - 1, image.Y() - 1, 0), 0.00001);

        // Error scenario: Shift x and y offsets are outside the bounds of the image
        assertEquals(0.0f, result.get(0, 0, 0), 0.00001);
    }
}