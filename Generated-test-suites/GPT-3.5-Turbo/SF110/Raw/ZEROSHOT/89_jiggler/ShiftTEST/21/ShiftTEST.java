import static org.junit.Assert.*;
import org.junit.Test;

public class ShiftTest {
    @Test
    public void testApplyGray() {
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

        Shift shift = new Shift(1, 1, 1);
        GrayImage result = shift.apply(image);
        
        assertEquals(5, result.get(0, 0));
        assertEquals(6, result.get(1, 0));
        assertEquals(7, result.get(2, 0));
        assertEquals(8, result.get(0, 1));
        assertEquals(9, result.get(1, 1));
        assertEquals(1, result.get(2, 1));
        assertEquals(2, result.get(0, 2));
        assertEquals(3, result.get(1, 2));
        assertEquals(4, result.get(2, 2));
    }
    
    @Test
    public void testApplyRealGray() {
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 0.1f);
        image.set(1, 0, 0.2f);
        image.set(2, 0, 0.3f);
        image.set(0, 1, 0.4f);
        image.set(1, 1, 0.5f);
        image.set(2, 1, 0.6f);
        image.set(0, 2, 0.7f);
        image.set(1, 2, 0.8f);
        image.set(2, 2, 0.9f);

        Shift shift = new Shift(1, 1, 1);
        RealGrayImage result = shift.apply(image);
        
        assertEquals(0.5f, result.get(0, 0), 0.001);
        assertEquals(0.6f, result.get(1, 0), 0.001);
        assertEquals(0.7f, result.get(2, 0), 0.001);
        assertEquals(0.8f, result.get(0, 1), 0.001);
        assertEquals(0.9f, result.get(1, 1), 0.001);
        assertEquals(0.1f, result.get(2, 1), 0.001);
        assertEquals(0.2f, result.get(0, 2), 0.001);
        assertEquals(0.3f, result.get(1, 2), 0.001);
        assertEquals(0.4f, result.get(2, 2), 0.001);
    }
    
    @Test
    public void testApplyColor() {
        ColorImage image = new ColorImage(3, 3);
        image.setPlane(0, new GrayImage(3, 3));
        image.setPlane(1, new GrayImage(3, 3));
        image.setPlane(2, new GrayImage(3, 3));
        
        Shift shift = new Shift(1, 1, 1);
        ColorImage result = shift.apply(image);
        
        assertEquals(2, result.plane(0).get(0, 0));
        assertEquals(1, result.plane(0).get(1, 0));
        assertEquals(2, result.plane(0).get(2, 0));
        assertEquals(4, result.plane(0).get(0, 1));
        assertEquals(5, result.plane(0).get(1, 1));
        assertEquals(6, result.plane(0).get(2, 1));
        assertEquals(8, result.plane(0).get(0, 2));
        assertEquals(7, result.plane(0).get(1, 2));
        assertEquals(8, result.plane(0).get(2, 2));
        
        assertEquals(2, result.plane(1).get(0, 0));
        assertEquals(1, result.plane(1).get(1, 0));
        assertEquals(2, result.plane(1).get(2, 0));
        assertEquals(4, result.plane(1).get(0, 1));
        assertEquals(5, result.plane(1).get(1, 1));
        assertEquals(6, result.plane(1).get(2, 1));
        assertEquals(8, result.plane(1).get(0, 2));
        assertEquals(7, result.plane(1).get(1, 2));
        assertEquals(8, result.plane(1).get(2, 2));
        
        assertEquals(2, result.plane(2).get(0, 0));
        assertEquals(1, result.plane(2).get(1, 0));
        assertEquals(2, result.plane(2).get(2, 0));
        assertEquals(4, result.plane(2).get(0, 1));
        assertEquals(5, result.plane(2).get(1, 1));
        assertEquals(6, result.plane(2).get(2, 1));
        assertEquals(8, result.plane(2).get(0, 2));
        assertEquals(7, result.plane(2).get(1, 2));
        assertEquals(8, result.plane(2).get(2, 2));
    }
    
    @Test
    public void testApplyRealColor() {
        RealColorImage image = new RealColorImage(3, 3);
        image.setPlane(0, new RealGrayImage(3, 3));
        image.setPlane(1, new RealGrayImage(3, 3));
        image.setPlane(2, new RealGrayImage(3, 3));
        
        Shift shift = new Shift(1, 1, 1);
        RealColorImage result = shift.apply(image);
        
        assertEquals(0.2f, result.plane(0).get(0, 0), 0.001);
        assertEquals(0.1f, result.plane(0).get(1, 0), 0.001);
        assertEquals(0.2f, result.plane(0).get(2, 0), 0.001);
        assertEquals(0.4f, result.plane(0).get(0, 1), 0.001);
        assertEquals(0.5f, result.plane(0).get(1, 1), 0.001);
        assertEquals(0.6f, result.plane(0).get(2, 1), 0.001);
        assertEquals(0.8f, result.plane(0).get(0, 2), 0.001);
        assertEquals(0.7f, result.plane(0).get(1, 2), 0.001);
        assertEquals(0.8f, result.plane(0).get(2, 2), 0.001);
        
        assertEquals(0.2f, result.plane(1).get(0, 0), 0.001);
        assertEquals(0.1f, result.plane(1).get(1, 0), 0.001);
        assertEquals(0.2f, result.plane(1).get(2, 0), 0.001);
        assertEquals(0.4f, result.plane(1).get(0, 1), 0.001);
        assertEquals(0.5f, result.plane(1).get(1, 1), 0.001);
        assertEquals(0.6f, result.plane(1).get(2, 1), 0.001);
        assertEquals(0.8f, result.plane(1).get(0, 2), 0.001);
        assertEquals(0.7f, result.plane(1).get(1, 2), 0.001);
        assertEquals(0.8f, result.plane(1).get(2, 2), 0.001);
        
        assertEquals(0.2f, result.plane(2).get(0, 0), 0.001);
        assertEquals(0.1f, result.plane(2).get(1, 0), 0.001);
        assertEquals(0.2f, result.plane(2).get(2, 0), 0.001);
        assertEquals(0.4f, result.plane(2).get(0, 1), 0.001);
        assertEquals(0.5f, result.plane(2).get(1, 1), 0.001);
        assertEquals(0.6f, result.plane(2).get(2, 1), 0.001);
        assertEquals(0.8f, result.plane(2).get(0, 2), 0.001);
        assertEquals(0.7f, result.plane(2).get(1, 2), 0.001);
        assertEquals(0.8f, result.plane(2).get(2, 2), 0.001);
    }
}