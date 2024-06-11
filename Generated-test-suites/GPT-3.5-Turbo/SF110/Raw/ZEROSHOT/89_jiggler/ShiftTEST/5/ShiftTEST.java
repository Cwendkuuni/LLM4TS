import static org.junit.Assert.*;
import org.junit.Test;

public class ShiftTest {

    @Test
    public void testApplyWithGrayImage() {
        Shift shift = new Shift(2, 2, Shift.NOWRAP);
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
        GrayImage result = shift.apply(image);
        assertEquals(result.get(2, 2), 1);
        assertEquals(result.get(0, 0), 5);
        assertEquals(result.get(2, 1), 8);
    }
    
    @Test
    public void testApplyWithRealGrayImage() {
        Shift shift = new Shift(2, 2, Shift.NOWRAP);
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
        RealGrayImage result = shift.apply(image);
        assertEquals(result.get(2, 2), 1.0, 0.001);
        assertEquals(result.get(0, 0), 5.0, 0.001);
        assertEquals(result.get(2, 1), 8.0, 0.001);
    }
    
    @Test
    public void testApplyWithColorImage() {
        Shift shift = new Shift(2, 2, Shift.NOWRAP);
        ColorImage image = new ColorImage(3, 3);
        
        GrayImage grayPlane = new GrayImage(3, 3);
        grayPlane.set(0, 0, 1);
        grayPlane.set(1, 0, 2);
        grayPlane.set(2, 0, 3);
        grayPlane.set(0, 1, 4);
        grayPlane.set(1, 1, 5);
        grayPlane.set(2, 1, 6);
        grayPlane.set(0, 2, 7);
        grayPlane.set(1, 2, 8);
        grayPlane.set(2, 2, 9);
        
        image.setPlane(0, grayPlane);
        image.setPlane(1, grayPlane);
        image.setPlane(2, grayPlane);
        
        ColorImage result = shift.apply(image);
        assertEquals(result.plane(0).get(2, 2), 1);
        assertEquals(result.plane(0).get(0, 0), 5);
        assertEquals(result.plane(0).get(2, 1), 8);
    }
    
    @Test
    public void testApplyWithRealColorImage() {
        Shift shift = new Shift(2, 2, Shift.NOWRAP);
        RealColorImage image = new RealColorImage(3, 3);
        
        RealGrayImage grayPlane = new RealGrayImage(3, 3);
        grayPlane.set(0, 0, 1.0);
        grayPlane.set(1, 0, 2.0);
        grayPlane.set(2, 0, 3.0);
        grayPlane.set(0, 1, 4.0);
        grayPlane.set(1, 1, 5.0);
        grayPlane.set(2, 1, 6.0);
        grayPlane.set(0, 2, 7.0);
        grayPlane.set(1, 2, 8.0);
        grayPlane.set(2, 2, 9.0);
        
        image.setPlane(0, grayPlane);
        image.setPlane(1, grayPlane);
        image.setPlane(2, grayPlane);
        
        RealColorImage result = shift.apply(image);
        assertEquals(result.plane(0).get(2, 2), 1.0, 0.001);
        assertEquals(result.plane(0).get(0, 0), 5.0, 0.001);
        assertEquals(result.plane(0).get(2, 1), 8.0, 0.001);
    }
}