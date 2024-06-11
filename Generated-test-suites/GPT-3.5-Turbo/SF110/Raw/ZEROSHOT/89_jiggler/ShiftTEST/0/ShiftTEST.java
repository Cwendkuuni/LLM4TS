package jigl.image.ops;

import org.junit.Test;
import static org.junit.Assert.*;

import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.ColorImage;
import jigl.image.RealColorImage;
import jigl.image.exceptions.*;

public class ShiftTest {

    @Test
    public void testApply_GrayImage() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        GrayImage input = new GrayImage(3, 3);
        input.set(0, 0, 1);
        input.set(1, 1, 2);
        input.set(2, 2, 3);
        GrayImage output = shift.apply(input);
        assertEquals(2, output.get(0, 0));
        assertEquals(3, output.get(1, 1));
        assertEquals(1, output.get(2, 2));

        shift = new Shift(1, 1, Shift.NOWRAP);
        output = shift.apply(input);
        assertEquals(0, output.get(0, 0));
        assertEquals(0, output.get(1, 1));
        assertEquals(0, output.get(2, 2));
    }

    @Test
    public void testApply_RealGrayImage() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealGrayImage input = new RealGrayImage(3, 3);
        input.set(0, 0, 1.0f);
        input.set(1, 1, 2.0f);
        input.set(2, 2, 3.0f);
        RealGrayImage output = shift.apply(input);
        assertEquals(2.0f, output.get(0, 0), 0.0f);
        assertEquals(3.0f, output.get(1, 1), 0.0f);
        assertEquals(1.0f, output.get(2, 2), 0.0f);

        shift = new Shift(1, 1, Shift.NOWRAP);
        output = shift.apply(input);
        assertEquals(0.0f, output.get(0, 0), 0.0f);
        assertEquals(0.0f, output.get(1, 1), 0.0f);
        assertEquals(0.0f, output.get(2, 2), 0.0f);
    }

    @Test
    public void testApply_ColorImage() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        ColorImage input = new ColorImage(3, 3);
        input.setPlane(0, new GrayImage(3, 3));
        input.setPlane(1, new GrayImage(3, 3));
        input.setPlane(2, new GrayImage(3, 3));
        input.plane(0).set(0, 0, 1);
        input.plane(1).set(1, 1, 2);
        input.plane(2).set(2, 2, 3);
        ColorImage output = shift.apply(input);
        assertEquals(2, output.plane(0).get(0, 0));
        assertEquals(3, output.plane(1).get(1, 1));
        assertEquals(1, output.plane(2).get(2, 2));

        shift = new Shift(1, 1, Shift.NOWRAP);
        output = shift.apply(input);
        assertEquals(0, output.plane(0).get(0, 0));
        assertEquals(0, output.plane(1).get(1, 1));
        assertEquals(0, output.plane(2).get(2, 2));
    }

    @Test
    public void testApply_RealColorImage() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealColorImage input = new RealColorImage(3, 3);
        input.setPlane(0, new RealGrayImage(3));
        input.setPlane(1, new RealGrayImage(3));
        input.setPlane(2, new RealGrayImage(3));
        input.plane(0).set(0, 0, 1.0f);
        input.plane(1).set(1, 1, 2.0f);
        input.plane(2).set(2, 2, 3.0f);
        RealColorImage output = shift.apply(input);
        assertEquals(2.0f, output.plane(0).get(0, 0), 0.0f);
        assertEquals(3.0f, output.plane(1).get(1, 1), 0.0f);
        assertEquals(1.0f, output.plane(2).get(2, 2), 0.0f);

        shift = new Shift(1, 1, Shift.NOWRAP);
        output = shift.apply(input);
        assertEquals(0.0f, output.plane(0).get(0, 0), 0.0f);
        assertEquals(0.0f, output.plane(1).get(1, 1), 0.0f);
        assertEquals(0.0f, output.plane(2).get(2, 2), 0.0f);
    }
}