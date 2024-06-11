import org.junit.Test;
import static org.junit.Assert.*;

public class ShiftTest {
    
    @Test
    public void testShiftConstructor() {
        Shift shift = new Shift(2, 3, 1);
        assertEquals(2, shift.xoffset);
        assertEquals(3, shift.yoffset);
        assertTrue(shift.wrap);
    }
    
    @Test
    public void testShiftConstructor_ZeroWrap() {
        Shift shift = new Shift(1, 0, 0);
        assertEquals(1, shift.xoffset);
        assertEquals(0, shift.yoffset);
        assertFalse(shift.wrap);
    }
    
    @Test
    public void testApply_GrayImage_Wrap() {
        GrayImage image = new GrayImage(3, 3);
        for (int i = 0; i < 3; i++) {
            image.set(i, 0, 1);
            image.set(i, 1, 2);
            image.set(i, 2, 3);
        }
        
        Shift shift = new Shift(1, 1, 1);
        GrayImage result = shift.apply(image);
        
        assertEquals(2, result.get(0, 0));
        assertEquals(3, result.get(1, 0));
        assertEquals(0, result.get(2, 0));
        assertEquals(3, result.get(0, 1));
        assertEquals(1, result.get(1, 1));
        assertEquals(2, result.get(2, 1));
        assertEquals(0, result.get(0, 2));
        assertEquals(2, result.get(1, 2));
        assertEquals(1, result.get(2, 2));
    }
    
    @Test
    public void testApply_GrayImage_NoWrap() {
        GrayImage image = new GrayImage(3, 3);
        for (int i = 0; i < 3; i++) {
            image.set(i, 0, 1);
            image.set(i, 1, 2);
            image.set(i, 2, 3);
        }
        
        Shift shift = new Shift(-1, 1, 0);
        GrayImage result = shift.apply(image);
        
        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(1, 0));
        assertEquals(3, result.get(2, 0));
        assertEquals(0, result.get(0, 1));
        assertEquals(1, result.get(1, 1));
        assertEquals(2, result.get(2, 1));
        assertEquals(0, result.get(0, 2));
        assertEquals(0, result.get(1, 2));
        assertEquals(0, result.get(2, 2));
    }
    
    @Test
    public void testApply_RealGrayImage_Wrap() {
        RealGrayImage image = new RealGrayImage(3, 3);
        for (int i = 0; i < 3; i++) {
            image.set(i, 0, 1.0);
            image.set(i, 1, 2.0);
            image.set(i, 2, 3.0);
        }
        
        Shift shift = new Shift(1, 1, 1);
        RealGrayImage result = shift.apply(image);
        
        assertEquals(2.0, result.get(0, 0), 0.1);
        assertEquals(3.0, result.get(1, 0), 0.1);
        assertEquals(0.0, result.get(2, 0), 0.1);
        assertEquals(3.0, result.get(0, 1), 0.1);
        assertEquals(1.0, result.get(1, 1), 0.1);
        assertEquals(2.0, result.get(2, 1), 0.1);
        assertEquals(0.0, result.get(0, 2), 0.1);
        assertEquals(2.0, result.get(1, 2), 0.1);
        assertEquals(1.0, result.get(2, 2), 0.1);
    }
    
    @Test
    public void testApply_RealGrayImage_NoWrap() {
        RealGrayImage image = new RealGrayImage(3, 3);
        for (int i = 0; i < 3; i++) {
            image.set(i, 0, 1.0);
            image.set(i, 1, 2.0);
            image.set(i, 2, 3.0);
        }
        
        Shift shift = new Shift(-1, 1, 0);
        RealGrayImage result = shift.apply(image);
        
        assertEquals(1.0, result.get(0, 0), 0.1);
        assertEquals(2.0, result.get(1, 0), 0.1);
        assertEquals(3.0, result.get(2, 0), 0.1);
        assertEquals(0.0, result.get(0, 1), 0.1);
        assertEquals(1.0, result.get(1, 1), 0.1);
        assertEquals(2.0, result.get(2, 1), 0.1);
        assertEquals(0.0, result.get(0, 2), 0.1);
        assertEquals(0.0, result.get(1, 2), 0.1);
        assertEquals(0.0, result.get(2, 2), 0.1);
    }
    
    @Test
    public void testApply_ColorImage_Wrap() {
        ColorImage image = new ColorImage(3, 3);
        for (int p = 0; p < 3; p++) {
            for (int i = 0; i < 3; i++) {
                image.set(p, i, 0, 1);
                image.set(p, i, 1, 2);
                image.set(p, i, 2, 3);
            }
        }
        
        Shift shift = new Shift(1, 1, 1);
        ColorImage result = shift.apply(image);
        
        assertEquals(2, result.plane(0).get(0, 0));
        assertEquals(3, result.plane(0).get(1, 0));
        assertEquals(0, result.plane(0).get(2, 0));
        assertEquals(3, result.plane(0).get(0, 1));
        assertEquals(1, result.plane(0).get(1, 1));
        assertEquals(2, result.plane(0).get(2, 1));
        assertEquals(0, result.plane(0).get(0, 2));
        assertEquals(2, result.plane(0).get(1, 2));
        assertEquals(1, result.plane(0).get(2, 2));
        
        assertEquals(2, result.plane(1).get(0, 0));
        assertEquals(3, result.plane(1).get(1, 0));
        assertEquals(0, result.plane(1).get(2, 0));
        assertEquals(3, result.plane(1).get(0, 1));
        assertEquals(1, result.plane(1).get(1, 1));
        assertEquals(2, result.plane(1).get(2, 1));
        assertEquals(0, result.plane(1).get(0, 2));
        assertEquals(2, result.plane(1).get(1, 2));
        assertEquals(1, result.plane(1).get(2, 2));
        
        assertEquals(2, result.plane(2).get(0, 0));
        assertEquals(3, result.plane(2).get(1, 0));
        assertEquals(0, result.plane(2).get(2, 0));
        assertEquals(3, result.plane(2).get(0, 1));
        assertEquals(1, result.plane(2).get(1, 1));
        assertEquals(2, result.plane(2).get(2, 1));
        assertEquals(0, result.plane(2).get(0, 2));
        assertEquals(2, result.plane(2).get(1, 2));
        assertEquals(1, result.plane(2).get(2, 2));
    }
    
    @Test
    public void testApply_RealColorImage_NoWrap() {
        RealColorImage image = new RealColorImage(3, 3);
        for (int p = 0; p < 3; p++) {
            for (int i = 0; i < 3; i++) {
                image.set(p, i, 0, 1.0);
                image.set(p, i, 1, 2.0);
                image.set(p, i, 2, 3.0);
            }
        }
        
        Shift shift = new Shift(-1, 1, 0);
        RealColorImage result = shift.apply(image);
        
        assertEquals(1.0, result.plane(0).get(0, 0), 0.1);
        assertEquals(2.0, result.plane(0).get(1, 0), 0.1);
        assertEquals(3.0, result.plane(0).get(2, 0), 0.1);
        assertEquals(0.0, result.plane(0).get(0, 1), 0.1);
        assertEquals(1.0, result.plane(0).get(1, 1), 0.1);
        assertEquals(2.0, result.plane(0).get(2, 1), 0.1);
        assertEquals(0.0, result.plane(0).get(0, 2), 0.1);
        assertEquals(0.0, result.plane(0).get(1, 2), 0.1);
        assertEquals(0.0, result.plane(0).get(2, 2), 0.1);
        
        assertEquals(1.0, result.plane(1).get(0, 0), 0.1);
        assertEquals(2.0, result.plane(1).get(1, 0), 0.1);
        assertEquals(3.0, result.plane(1).get(2, 0), 0.1);
        assertEquals(0.0, result.plane(1).get(0, 1), 0.1);
        assertEquals(1.0, result.plane(1).get(1, 1), 0.1);
        assertEquals(2.0, result.plane(1).get(2, 1), 0.1);
        assertEquals(0.0, result.plane(1).get(0, 2), 0.1);
        assertEquals(0.0, result.plane(1).get(1, 2), 0.1);
        assertEquals(0.0, result.plane(1).get(2, 2), 0.1);
        
        assertEquals(1.0, result.plane(2).get(0, 0), 0.1);
        assertEquals(2.0, result.plane(2).get(1, 0), 0.1);
        assertEquals(3.0, result.plane(2).get(2, 0), 0.1);
        assertEquals(0.0, result.plane(2).get(0, 1), 0.1);
        assertEquals(1.0, result.plane(2).get(1, 1), 0.1);
        assertEquals(2.0, result.plane(2).get(2, 1), 0.1);
        assertEquals(0.0, result.plane(2).get(0, 2), 0.1);
        assertEquals(0.0, result.plane(2).get(1, 2), 0.1);
        assertEquals(0.0, result.plane(2).get(2, 2), 0.1);
    }
}