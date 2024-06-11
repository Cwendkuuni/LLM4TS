package jigl.image.ops;

import org.junit.Test;
import static org.junit.Assert.*;

public class ShiftTest {
    
    @Test
    public void testApply_GrayImage() {
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 1, 2);
        image.set(2, 2, 3);
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage result = shift.apply(image);
        assertEquals(0, result.get(0, 0));
        assertEquals(0, result.get(1, 1));
        assertEquals(0, result.get(2, 2));
    }
    
    @Test
    public void testApply_RealGrayImage() {
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.5f);
        image.set(1, 1, 2.5f);
        image.set(2, 2, 3.5f);
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        RealGrayImage result = shift.apply(image);
        assertEquals(0.0f, result.get(0, 0), 0.0001f);
        assertEquals(0.0f, result.get(1, 1), 0.0001f);
        assertEquals(0.0f, result.get(2, 2), 0.0001f);
    }
    
    @Test
    public void testApply_ColorImage() {
        ColorImage image = new ColorImage(3, 3);
        image.setPlane(0, new GrayImage(3, 3));
        image.setPlane(1, new GrayImage(3, 3));
        image.setPlane(2, new GrayImage(3, 3));
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        ColorImage result = shift.apply(image);
        assertEquals(0, result.getPlane(0).get(0, 0));
        assertEquals(0, result.getPlane(1).get(0, 0));
        assertEquals(0, result.getPlane(2).get(0, 0));
        assertEquals(0, result.getPlane(0).get(1, 1));
        assertEquals(0, result.getPlane(1).get(1, 1));
        assertEquals(0, result.getPlane(2).get(1, 1));
        assertEquals(0, result.getPlane(0).get(2, 2));
        assertEquals(0, result.getPlane(1).get(2, 2));
        assertEquals(0, result.getPlane(2).get(2, 2));
    }
    
    @Test
    public void testApply_RealColorImage() {
        RealColorImage image = new RealColorImage(3, 3);
        image.setPlane(0, new RealGrayImage(3, 3));
        image.setPlane(1, new RealGrayImage(3, 3));
        image.setPlane(2, new RealGrayImage(3, 3));
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        RealColorImage result = shift.apply(image);
        assertEquals(0.0f, result.getPlane(0).get(0, 0), 0.0001f);
        assertEquals(0.0f, result.getPlane(1).get(0, 0), 0.0001f);
        assertEquals(0.0f, result.getPlane(2).get(0, 0), 0.0001f);
        assertEquals(0.0f, result.getPlane(0).get(1, 1), 0.0001f);
        assertEquals(0.0f, result.getPlane(1).get(1, 1), 0.0001f);
        assertEquals(0.0f, result.getPlane(2).get(1, 1), 0.0001f);
        assertEquals(0.0f, result.getPlane(0).get(2, 2), 0.0001f);
        assertEquals(0.0f, result.getPlane(1).get(2, 2), 0.0001f);
        assertEquals(0.0f, result.getPlane(2).get(2, 2), 0.0001f);
    }
}