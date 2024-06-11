import static org.junit.Assert.*;
import org.junit.Test;

public class ShiftTest {
    
    @Test
    public void testApply_GrayImage() {
        GrayImage original = new GrayImage(3, 3);
        original.set(0, 0, 1);
        original.set(1, 1, 2);
        original.set(2, 2, 3);
        
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        
        GrayImage result = shift.apply(original);
        
        assertEquals(2, result.get(0, 0));
        assertEquals(0, result.get(0, 1));
        assertEquals(0, result.get(0, 2));
        assertEquals(0, result.get(1, 0));
        assertEquals(1, result.get(1, 1));
        assertEquals(0, result.get(1, 2));
        assertEquals(0, result.get(2, 0));
        assertEquals(0, result.get(2, 1));
        assertEquals(3, result.get(2, 2));
    }
    
    @Test
    public void testApply_RealGrayImage() {
        RealGrayImage original = new RealGrayImage(3, 3);
        original.set(0, 0, 1.0f);
        original.set(1, 1, 2.0f);
        original.set(2, 2, 3.0f);
        
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        
        RealGrayImage result = shift.apply(original);
        
        assertEquals(2.0f, result.get(0, 0), 0.01);
        assertEquals(0.0f, result.get(0, 1), 0.01);
        assertEquals(0.0f, result.get(0, 2), 0.01);
        assertEquals(0.0f, result.get(1, 0), 0.01);
        assertEquals(1.0f, result.get(1, 1), 0.01);
        assertEquals(0.0f, result.get(1, 2), 0.01);
        assertEquals(0.0f, result.get(2, 0), 0.01);
        assertEquals(0.0f, result.get(2, 1), 0.01);
        assertEquals(3.0f, result.get(2, 2), 0.01);
    }
    
    @Test
    public void testApply_ColorImage() {
        ColorImage original = new ColorImage(3, 3);
        original.setPlane(0, new GrayImage(3, 3));
        original.setPlane(1, new GrayImage(3, 3));
        original.setPlane(2, new GrayImage(3, 3));
        
        original.plane(0).set(0, 0, 1);
        original.plane(1).set(1, 1, 2);
        original.plane(2).set(2, 2, 3);
        
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        
        ColorImage result = shift.apply(original);
        
        assertEquals(2, result.plane(0).get(0, 0));
        assertEquals(0, result.plane(0).get(0, 1));
        assertEquals(0, result.plane(0).get(0, 2));
        assertEquals(0, result.plane(0).get(1, 0));
        assertEquals(1, result.plane(0).get(1, 1));
        assertEquals(0, result.plane(0).get(1, 2));
        assertEquals(0, result.plane(0).get(2, 0));
        assertEquals(0, result.plane(0).get(2, 1));
        assertEquals(3, result.plane(0).get(2, 2));
        
        assertEquals(2, result.plane(1).get(0, 0));
        assertEquals(0, result.plane(1).get(0, 1));
        assertEquals(0, result.plane(1).get(0, 2));
        assertEquals(0, result.plane(1).get(1, 0));
        assertEquals(1, result.plane(1).get(1, 1));
        assertEquals(0, result.plane(1).get(1, 2));
        assertEquals(0, result.plane(1).get(2, 0));
        assertEquals(0, result.plane(1).get(2, 1));
        assertEquals(3, result.plane(1).get(2, 2));
        
        assertEquals(2, result.plane(2).get(0, 0));
        assertEquals(0, result.plane(2).get(0, 1));
        assertEquals(0, result.plane(2).get(0, 2));
        assertEquals(0, result.plane(2).get(1, 0));
        assertEquals(1, result.plane(2).get(1, 1));
        assertEquals(0, result.plane(2).get(1, 2));
        assertEquals(0, result.plane(2).get(2, 0));
        assertEquals(0, result.plane(2).get(2, 1));
        assertEquals(3, result.plane(2).get(2, 2));
    }
    
    @Test
    public void testApply_RealColorImage() {
        RealColorImage original = new RealColorImage(3, 3);
        original.setPlane(0, new RealGrayImage(3, 3));
        original.setPlane(1, new RealGrayImage(3, 3));
        original.setPlane(2, new RealGrayImage(3, 3));
        
        original.plane(0).set(0, 0, 1.0f);
        original.plane(1).set(1, 1, 2.0f);
        original.plane(2).set(2, 2, 3.0f);
        
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        
        RealColorImage result = shift.apply(original);
        
        assertEquals(2.0f, result.plane(0).get(0, 0), 0.01);
        assertEquals(0.0f, result.plane(0).get(0, 1), 0.01);
        assertEquals(0.0f, result.plane(0).get(0, 2), 0.01);
        assertEquals(0.0f, result.plane(0).get(1, 0), 0.01);
        assertEquals(1.0f, result.plane(0).get(1, 1), 0.01);
        assertEquals(0.0f, result.plane(0).get(1, 2), 0.01);
        assertEquals(0.0f, result.plane(0).get(2, 0), 0.01);
        assertEquals(0.0f, result.plane(0).get(2, 1), 0.01);
        assertEquals(3.0f, result.plane(0).get(2, 2), 0.01);
        
        assertEquals(2.0f, result.plane(1).get(0, 0), 0.01);
        assertEquals(0.0f, result.plane(1).get(0, 1), 0.01);
        assertEquals(0.0f, result.plane(1).get(0, 2), 0.01);
        assertEquals(0.0f, result.plane(1).get(1, 0), 0.01);
        assertEquals(1.0f, result.plane(1).get(1, 1), 0.01);
        assertEquals(0.0f, result.plane(1).get(1, 2), 0.01);
        assertEquals(0.0f, result.plane(1).get(2, 0), 0.01);
        assertEquals(0.0f, result.plane(1).get(2, 1), 0.01);
        assertEquals(3.0f, result.plane(1).get(2, 2), 0.01);
        
        assertEquals(2.0f, result.plane(2).get(0, 0), 0.01);
        assertEquals(0.0f, result.plane(2).get(0, 1), 0.01);
        assertEquals(0.0f, result.plane(2).get(0, 2), 0.01);
        assertEquals(0.0f, result.plane(2).get(1, 0), 0.01);
        assertEquals(1.0f, result.plane(2).get(1, 1), 0.01);
        assertEquals(0.0f, result.plane(2).get(1, 2), 0.01);
        assertEquals(0.0f, result.plane(2).get(2, 0), 0.01);
        assertEquals(0.0f, result.plane(2).get(2, 1), 0.01);
        assertEquals(3.0f, result.plane(2).get(2, 2), 0.01);
    }
}