import static org.junit.Assert.*;
import org.junit.Test;

public class ShiftTest {

    @Test
    public void testShiftGrayImage() {
        GrayImage image = new GrayImage(3, 3);
        Shift shift = new Shift(1, 1, Shift.WRAP);
        GrayImage result = shift.apply(image);
        
        // Typical use case
        assertNotNull(result);
        
        // Edge case where x and y offsets are 0
        Shift shiftEdge = new Shift(0, 0, Shift.NOWRAP);
        GrayImage resultEdge = shiftEdge.apply(image);
        assertNotNull(resultEdge);
        
        // Error scenario where image is null
        GrayImage nullImage = null;
        Shift shiftError = new Shift(1, 1, Shift.NOWRAP);
        assertThrows(NullPointerException.class, () -> {
            shiftError.apply(nullImage);
        });
    }
    
    @Test
    public void testShiftRealGrayImage() {
        RealGrayImage image = new RealGrayImage(3, 3);
        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealGrayImage result = shift.apply(image);
        
        // Typical use case
        assertNotNull(result);
        
        // Edge case where x and y offsets are 0
        Shift shiftEdge = new Shift(0, 0, Shift.NOWRAP);
        RealGrayImage resultEdge = shiftEdge.apply(image);
        assertNotNull(resultEdge);
        
        // Error scenario where image is null
        RealGrayImage nullImage = null;
        Shift shiftError = new Shift(1, 1, Shift.NOWRAP);
        assertThrows(NullPointerException.class, () -> {
            shiftError.apply(nullImage);
        });
    }
    
    @Test
    public void testShiftColorImage() {
        ColorImage image = new ColorImage(3, 3);
        Shift shift = new Shift(1, 1, Shift.WRAP);
        ColorImage result = shift.apply(image);
        
        // Typical use case
        assertNotNull(result);
        
        // Edge case where x and y offsets are 0
        Shift shiftEdge = new Shift(0, 0, Shift.NOWRAP);
        ColorImage resultEdge = shiftEdge.apply(image);
        assertNotNull(resultEdge);
        
        // Error scenario where image is null
        ColorImage nullImage = null;
        Shift shiftError = new Shift(1, 1, Shift.NOWRAP);
        assertThrows(NullPointerException.class, () -> {
            shiftError.apply(nullImage);
        });
    }
    
    @Test
    public void testShiftRealColorImage() {
        RealColorImage image = new RealColorImage(3, 3);
        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealColorImage result = shift.apply(image);
        
        // Typical use case
        assertNotNull(result);
        
        // Edge case where x and y offsets are 0
        Shift shiftEdge = new Shift(0, 0, Shift.NOWRAP);
        RealColorImage resultEdge = shiftEdge.apply(image);
        assertNotNull(resultEdge);
        
        // Error scenario where image is null
        RealColorImage nullImage = null;
        Shift shiftError = new Shift(1, 1, Shift.NOWRAP);
        assertThrows(NullPointerException.class, () -> {
            shiftError.apply(nullImage);
        });
    }
}