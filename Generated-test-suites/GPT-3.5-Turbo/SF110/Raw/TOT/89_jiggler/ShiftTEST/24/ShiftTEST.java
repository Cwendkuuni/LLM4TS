import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.ColorImage;
import jigl.image.RealColorImage;
import jigl.image.exceptions.*;

public class ShiftTest {

    @Test
    public void testApply_GrayImage_ShouldShiftWithoutWrap() {
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
        
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage result = shift.apply(image);
        
        assertEquals(result.get(0, 0), Integer.valueOf(5));
        assertEquals(result.get(1, 0), Integer.valueOf(6));
        assertEquals(result.get(2, 0), Integer.valueOf(0));
        assertEquals(result.get(0, 1), Integer.valueOf(8));
        assertEquals(result.get(1, 1), Integer.valueOf(9));
        assertEquals(result.get(2, 1), Integer.valueOf(0));
        assertEquals(result.get(0, 2), Integer.valueOf(0));
        assertEquals(result.get(1, 2), Integer.valueOf(0));
        assertEquals(result.get(2, 2), Integer.valueOf(0));
    }
    
    @Test
    public void testApply_GrayImage_ShouldShiftWithWrap() {
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
        
        Shift shift = new Shift(1, 1, Shift.WRAP);
        GrayImage result = shift.apply(image);
        
        assertEquals(result.get(0, 0), Integer.valueOf(9));
        assertEquals(result.get(1, 0), Integer.valueOf(7));
        assertEquals(result.get(2, 0), Integer.valueOf(8));
        assertEquals(result.get(0, 1), Integer.valueOf(3));
        assertEquals(result.get(1, 1), Integer.valueOf(1));
        assertEquals(result.get(2, 1), Integer.valueOf(2));
        assertEquals(result.get(0, 2), Integer.valueOf(6));
        assertEquals(result.get(1, 2), Integer.valueOf(4));
        assertEquals(result.get(2, 2), Integer.valueOf(5));
    }
    
    @Test
    public void testApply_GrayImage_ShouldShiftWithoutWrapAndHandleOutOfBounds() {
        GrayImage image = new GrayImage(2, 2);
        image.set(0, 0, 1);
        image.set(1, 0, 2);
        image.set(0, 1, 3);
        image.set(1, 1, 4);
        
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage result = shift.apply(image);
        
        assertEquals(result.get(0, 0), Integer.valueOf(0));
        assertEquals(result.get(1, 0), Integer.valueOf(0));
        assertEquals(result.get(0, 1), Integer.valueOf(0));
        assertEquals(result.get(1, 1), Integer.valueOf(0));
    }
    
    // Additional test cases can be added for each method
    
}