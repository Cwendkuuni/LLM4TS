import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ShiftTest {
    
    @Test
    public void testShiftGrayImage() {
        GrayImage grayImage = new GrayImage(5, 5);
        grayImage.set(2, 2, 100);
        
        Shift shift = new Shift(2, 2, Shift.NOWRAP);
        GrayImage result = shift.apply(grayImage);
        
        assertEquals(result.get(4, 4), 100);
    }
    
    @Test
    public void testShiftRealGrayImage() {
        RealGrayImage realGrayImage = new RealGrayImage(5, 5);
        realGrayImage.set(2, 2, 100.0f);
        
        Shift shift = new Shift(2, 2, Shift.WRAP);
        RealGrayImage result = shift.apply(realGrayImage);
        
        assertEquals(result.get(0, 0), 100.0f, 0.0001);
    }
    
    // Additional test cases can be added here
}