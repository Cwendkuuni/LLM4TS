import static org.junit.Assert.*;
import org.junit.Test;

public class ShiftTest {
    
    @Test
    public void testApply_GrayImage() {
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 2);
        image.set(0, 2, 3);
        image.set(1, 0, 4);
        image.set(1, 1, 5);
        image.set(1, 2, 6);
        image.set(2, 0, 7);
        image.set(2, 1, 8);
        image.set(2, 2, 9);
        
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage result = shift.apply(image);
        
        GrayImage expected = new GrayImage(3, 3);
        expected.set(0, 0, 0);
        expected.set(0, 1, 0);
        expected.set(0, 2, 0);
        expected.set(1, 0, 0);
        expected.set(1, 1, 1);
        expected.set(1, 2, 2);
        expected.set(2, 0, 0);
        expected.set(2, 1, 4);
        expected.set(2, 2, 5);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testApply_RealGrayImage() {
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.0f);
        image.set(0, 1, 2.0f);
        image.set(0, 2, 3.0f);
        image.set(1, 0, 4.0f);
        image.set(1, 1, 5.0f);
        image.set(1, 2, 6.0f);
        image.set(2, 0, 7.0f);
        image.set(2, 1, 8.0f);
        image.set(2, 2, 9.0f);
        
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        RealGrayImage result = shift.apply(image);
        
        RealGrayImage expected = new RealGrayImage(3, 3);
        expected.set(0, 0, 0.0f);
        expected.set(0, 1, 0.0f);
        expected.set(0, 2, 0.0f);
        expected.set(1, 0, 0.0f);
        expected.set(1, 1, 1.0f);
        expected.set(1, 2, 2.0f);
        expected.set(2, 0, 0.0f);
        expected.set(2, 1, 4.0f);
        expected.set(2, 2, 5.0f);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testApply_ColorImage() {
        ColorImage image = new ColorImage(3, 3);
        image.setPlane(0, new GrayImage(3, 3));
        image.setPlane(1, new GrayImage(3, 3));
        image.setPlane(2, new GrayImage(3, 3));
        
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        ColorImage result = shift.apply(image);
        
        ColorImage expected = new ColorImage(3, 3);
        expected.setPlane(0, new GrayImage(3, 3));
        expected.setPlane(1, new GrayImage(3, 3));
        expected.setPlane(2, new GrayImage(3, 3));
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testApply_RealColorImage() {
        RealColorImage image = new RealColorImage(3, 3);
        image.setPlane(0, new RealGrayImage(3, 3));
        image.setPlane(1, new RealGrayImage(3, 3));
        image.setPlane(2, new RealGrayImage(3, 3));
        
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        RealColorImage result = shift.apply(image);
        
        RealColorImage expected = new RealColorImage(3, 3);
        expected.setPlane(0, new RealGrayImage(3, 3));
        expected.setPlane(1, new RealGrayImage(3, 3));
        expected.setPlane(2, new RealGrayImage(3, 3));
        
        assertEquals(expected, result);
    }
    
}