import static org.junit.Assert.*;
import org.junit.Test;

public class ShiftTest {
    
    @Test
    public void testShiftConstructor() {
        Shift shift = new Shift(2, 3, 1);
        assertEquals(2, shift.xoffset);
        assertEquals(3, shift.yoffset);
        assertTrue(shift.wrap);
    }
    
    @Test
    public void testApplyGrayImage() {
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 100);
        
        Shift shift = new Shift(1, 1, 1);
        GrayImage result = shift.apply(image);
        
        assertEquals(100, result.get(1, 1));
    }
    
    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(1, 1, 0.5f);
        
        Shift shift = new Shift(-1, -1, 1);
        RealGrayImage result = shift.apply(image);
        
        assertEquals(0.5f, result.get(2, 2), 0.001);
    }
    
    @Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage(3, 3);
        image.setPlane(0, new GrayImage(3, 3));
        
        Shift shift = new Shift(1, 1, 0);
        ColorImage result = shift.apply(image);
        
        assertNotNull(result);
    }
    
    @Test
    public void testApplyRealColorImage() {
        RealColorImage image = new RealColorImage(3, 3);
        image.setPlane(0, new RealGrayImage(3, 3));
        
        Shift shift = new Shift(-2, -2, 0);
        RealColorImage result = shift.apply(image);
        
        assertNotNull(result);
    }
}