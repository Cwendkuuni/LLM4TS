
import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.exceptions.*;

public class ShiftTest {
    
    @Test
    public void testApply_GrayImage_wrap() {
        GrayImage image = new GrayImage(4, 4);
        image.set(0, 0, 10);
        image.set(1, 1, 20);
        image.set(2, 2, 30);
        image.set(3, 3, 40);
        
        Shift shift = new Shift(2, 2, Shift.WRAP);
        GrayImage result = shift.apply(image);
        
        assertEquals(10, result.get(2, 2));
        assertEquals(20, result.get(3, 3));
        assertEquals(30, result.get(0, 0));
        assertEquals(40, result.get(1, 1));
    }
    
    @Test
    public void testApply_GrayImage_nowrap() {
        GrayImage image = new GrayImage(4, 4);
        image.set(0, 0, 10);
        image.set(1, 1, 20);
        image.set(2, 2, 30);
        image.set(3, 3, 40);
        
        Shift shift = new Shift(2, 2, Shift.NOWRAP);
        GrayImage result = shift.apply(image);
        
        assertEquals(0, result.get(0, 0));
        assertEquals(0, result.get(1, 1));
        assertEquals(0, result.get(2, 2));
        assertEquals(0, result.get(3, 3));
    }
    
    @Test
    public void testApply_RealGrayImage_wrap() {
        RealGrayImage image = new RealGrayImage(4, 4);
        image.set(0, 0, 10.0f);
        image.set(1, 1, 20.0f);
        image.set(2, 2, 30.0f);
        image.set(3, 3, 40.0f);
        
        Shift shift = new Shift(2, 2, Shift.WRAP);
        RealGrayImage result = shift.apply(image);
        
        assertEquals(10.0f, result.get(2, 2), 0.001f);
        assertEquals(20.0f, result.get(3, 3), 0.001f);
        assertEquals(30.0f, result.get(0, 0), 0.001f);
        assertEquals(40.0f, result.get(1, 1), 0.001f);
    }
    
    @Test
    public void testApply_RealGrayImage_nowrap() {
        RealGrayImage image = new RealGrayImage(4, 4);
        image.set(0, 0, 10.0f);
        image.set(1, 1, 20.0f);
        image.set(2, 2, 30.0f);
        image.set(3, 3, 40.0f);
        
        Shift shift = new Shift(2, 2, Shift.NOWRAP);
        RealGrayImage result = shift.apply(image);
        
        assertEquals(0.0f, result.get(0, 0), 0.001f);
        assertEquals(0.0f, result.get(1, 1), 0.001f);
        assertEquals(0.0f, result.get(2, 2), 0.001f);
        assertEquals(0.0f, result.get(3, 3), 0.001f);
    }
    
    @Test
    public void testApply_ColorImage() {
        ColorImage image = new ColorImage(4, 4);
        image.setPlane(0, new GrayImage(4, 4, 10));
        image.setPlane(1, new GrayImage(4, 4, 20));
        image.setPlane(2, new GrayImage(4, 4, 30));
        
        Shift shift = new Shift(2, 2, Shift.WRAP);
        ColorImage result = shift.apply(image);
        
        assertEquals(10, result.plane(0).get(2, 2));
        assertEquals(20, result.plane(1).get(2, 2));
        assertEquals(30, result.plane(2).get(2, 2));
    }
    
    @Test
    public void testApply_RealColorImage() {
        RealColorImage image = new RealColorImage(4, 4);
        image.setPlane(0, new RealGrayImage(4, 4, 10.0f));
        image.setPlane(1, new RealGrayImage(4, 4, 20.0f));
        image.setPlane(2, new RealGrayImage(4, 4, 30.0f));
        
        Shift shift = new Shift(2, 2, Shift.WRAP);
        RealColorImage result = shift.apply(image);
        
        assertEquals(10.0f, result.plane(0).get(2, 2), 0.001f);
        assertEquals(20.0f, result.plane(1).get(2, 2), 0.001f);
        assertEquals(30.0f, result.plane(2).get(2, 2), 0.001f);
    }
    
}
