import jigl.image.*;
import jigl.image.exceptions.*;
import jigl.image.ops.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ShiftTest {
    
    @Test
    public void testShiftGrayImage_withXAndYOffsets() {
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 1, 2);
        image.set(2, 2, 3);
        
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage result = shift.apply(image);
        
        assertEquals(3, result.get(0, 0));
        assertEquals(0, result.get(1, 1));
        assertEquals(0, result.get(2, 2));
    }
    
    @Test
    public void testShiftGrayImage_withNegativeXAndYOffsets() {
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 1, 2);
        image.set(2, 2, 3);
        
        Shift shift = new Shift(-1, -1, Shift.NOWRAP);
        GrayImage result = shift.apply(image);
        
        assertEquals(0, result.get(0, 0));
        assertEquals(2, result.get(1, 1));
        assertEquals(3, result.get(2, 2));
    }
    
    @Test
    public void testShiftGrayImage_withWrap() {
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 1);
        image.set(2, 2, 3);
        
        Shift shift = new Shift(1, 1, Shift.WRAP);
        GrayImage result = shift.apply(image);
        
        assertEquals(1, result.get(2, 2));
        assertEquals(1, result.get(0, 0));
    }
    
    @Test
    public void testShiftRealGrayImage_withXAndYOffsets() {
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.0f);
        image.set(1, 1, 2.0f);
        image.set(2, 2, 3.0f);
        
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        RealGrayImage result = shift.apply(image);
        
        assertEquals(3.0f, result.get(0, 0), 0.0f);
        assertEquals(0.0f, result.get(1, 1), 0.0f);
        assertEquals(0.0f, result.get(2, 2), 0.0f);
    }
    
    @Test
    public void testShiftRealGrayImage_withNegativeXAndYOffsets() {
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.0f);
        image.set(1, 1, 2.0f);
        image.set(2, 2, 3.0f);
        
        Shift shift = new Shift(-1, -1, Shift.NOWRAP);
        RealGrayImage result = shift.apply(image);
        
        assertEquals(0.0f, result.get(0, 0), 0.0f);
        assertEquals(2.0f, result.get(1, 1), 0.0f);
        assertEquals(3.0f, result.get(2, 2), 0.0f);
    }
    
    @Test
    public void testShiftRealGrayImage_withWrap() {
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.0f);
        image.set(2, 2, 3.0f);
        
        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealGrayImage result = shift.apply(image);
        
        assertEquals(1.0f, result.get(2, 2), 0.0f);
        assertEquals(1.0f, result.get(0, 0), 0.0f);
    }
    
    @Test
    public void testShiftColorImage_withXAndYOffsets() {
        ColorImage image = new ColorImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 1, 2);
        image.set(2, 2, 3);
        
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        ColorImage result = shift.apply(image);
        
        assertEquals(3, result.get(0, 0, 0));
        assertEquals(0, result.get(1, 1, 0));
        assertEquals(0, result.get(2, 2, 0));
    }
    
    @Test
    public void testShiftColorImage_withNegativeXAndYOffsets() {
        ColorImage image = new ColorImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 1, 2);
        image.set(2, 2, 3);
        
        Shift shift = new Shift(-1, -1, Shift.NOWRAP);
        ColorImage result = shift.apply(image);
        
        assertEquals(0, result.get(0, 0, 0));
        assertEquals(2, result.get(1, 1, 0));
        assertEquals(3, result.get(2, 2, 0));
    }
    
    @Test
    public void testShiftColorImage_withWrap() {
        ColorImage image = new ColorImage(3, 3);
        image.set(0, 0, 1);
        image.set(2, 2, 3);
        
        Shift shift = new Shift(1, 1, Shift.WRAP);
        ColorImage result = shift.apply(image);
        
        assertEquals(1, result.get(2, 2, 0));
        assertEquals(1, result.get(0, 0, 0));
    }
    
    @Test
    public void testShiftRealColorImage_withXAndYOffsets() {
        RealColorImage image = new RealColorImage(3, 3);
        image.set(0, 0, 1.0f);
        image.set(1, 1, 2.0f);
        image.set(2, 2, 3.0f);
        
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        RealColorImage result = shift.apply(image);
        
        assertEquals(3.0f, result.get(0, 0, 0), 0.0f);
        assertEquals(0.0f, result.get(1, 1, 0), 0.0f);
        assertEquals(0.0f, result.get(2, 2, 0), 0.0f);
    }
    
    @Test
    public void testShiftRealColorImage_withNegativeXAndYOffsets() {
        RealColorImage image = new RealColorImage(3, 3);
        image.set(0, 0, 1.0f);
        image.set(1, 1, 2.0f);
        image.set(2, 2, 3.0f);
        
        Shift shift = new Shift(-1, -1, Shift.NOWRAP);
        RealColorImage result = shift.apply(image);
        
        assertEquals(0.0f, result.get(0, 0, 0), 0.0f);
        assertEquals(2.0f, result.get(1, 1, 0), 0.0f);
        assertEquals(3.0f, result.get(2, 2, 0), 0.0f);
    }
    
    @Test
    public void testShiftRealColorImage_withWrap() {
        RealColorImage image = new RealColorImage(3, 3);
        image.set(0, 0, 1.0f);
        image.set(2, 2, 3.0f);
        
        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealColorImage result = shift.apply(image);
        
        assertEquals(1.0f, result.get(2, 2, 0), 0.0f);
        assertEquals(1.0f, result.get(0, 0, 0), 0.0f);
    }
    
    
}