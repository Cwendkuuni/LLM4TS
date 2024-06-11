package jigl.image.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ShiftTest {
    @Test
    public void testApply_GrayImage() {
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 0, 2);
        image.set(2, 0, 3);
        image.set(0, 1, 4);
        image.set(1, 1, 5);
        image.set(2, 1, 6);
        image.set(0, 2, 7);
        image.set(1, 2, 8);
        image.set(2, 2, 9);
        
        Shift shift = new Shift(1, 1, 0);
        GrayImage result = shift.apply(image);
        
        assertEquals(5, result.get(0, 0));
        assertEquals(6, result.get(1, 0));
        assertEquals(0, result.get(2, 0));
        assertEquals(8, result.get(0, 1));
        assertEquals(9, result.get(1, 1));
        assertEquals(0, result.get(2, 1));
        assertEquals(0, result.get(0, 2));
        assertEquals(0, result.get(1, 2));
        assertEquals(0, result.get(2, 2));
    }
    
    @Test
    public void testApply_RealGrayImage() {
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.0);
        image.set(1, 0, 2.0);
        image.set(2, 0, 3.0);
        image.set(0, 1, 4.0);
        image.set(1, 1, 5.0);
        image.set(2, 1, 6.0);
        image.set(0, 2, 7.0);
        image.set(1, 2, 8.0);
        image.set(2, 2, 9.0);
        
        Shift shift = new Shift(1, 1, 0);
        RealGrayImage result = shift.apply(image);
        
        assertEquals(5.0, result.get(0, 0), 0.001);
        assertEquals(6.0, result.get(1, 0), 0.001);
        assertEquals(0.0, result.get(2, 0), 0.001);
        assertEquals(8.0, result.get(0, 1), 0.001);
        assertEquals(9.0, result.get(1, 1), 0.001);
        assertEquals(0.0, result.get(2, 1), 0.001);
        assertEquals(0.0, result.get(0, 2), 0.001);
        assertEquals(0.0, result.get(1, 2), 0.001);
        assertEquals(0.0, result.get(2, 2), 0.001);
    }
    
    @Test
    public void testApply_ColorImage() {
        ColorImage image = new ColorImage(3, 3);
        image.setPlane(0, new GrayImage(3, 3));
        image.setPlane(1, new GrayImage(3, 3));
        image.setPlane(2, new GrayImage(3, 3));
        
        Shift shift = new Shift(1, 1, 0);
        ColorImage result = shift.apply(image);
        
        assertEquals(3, result.plane(0).get(0, 0));
        assertEquals(3, result.plane(1).get(0, 0));
        assertEquals(3, result.plane(2).get(0, 0));
        assertEquals(0, result.plane(0).get(1, 0));
        assertEquals(0, result.plane(1).get(1, 0));
        assertEquals(0, result.plane(2).get(1, 0));
        assertEquals(0, result.plane(0).get(2, 0));
        assertEquals(0, result.plane(1).get(2, 0));
        assertEquals(0, result.plane(2).get(2, 0));
    }
    
    @Test
    public void testApply_RealColorImage() {
        RealColorImage image = new RealColorImage(3, 3);
        image.setPlane(0, new RealGrayImage(3, 3));
        image.setPlane(1, new RealGrayImage(3, 3));
        image.setPlane(2, new RealGrayImage(3, 3));
        
        Shift shift = new Shift(1, 1, 0);
        RealColorImage result = shift.apply(image);
        
        assertEquals(3.0, result.plane(0).get(0, 0), 0.001);
        assertEquals(3.0, result.plane(1).get(0, 0), 0.001);
        assertEquals(3.0, result.plane(2).get(0, 0), 0.001);
        assertEquals(0.0, result.plane(0).get(1, 0), 0.001);
        assertEquals(0.0, result.plane(1).get(1, 0), 0.001);
        assertEquals(0.0, result.plane(2).get(1, 0), 0.001);
        assertEquals(0.0, result.plane(0).get(2, 0), 0.001);
        assertEquals(0.0, result.plane(1).get(2, 0), 0.001);
        assertEquals(0.0, result.plane(2).get(2, 0), 0.001);
    }
}