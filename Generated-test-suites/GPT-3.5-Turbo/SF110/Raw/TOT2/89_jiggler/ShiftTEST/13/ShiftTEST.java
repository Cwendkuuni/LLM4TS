import org.junit.Test;
import static org.junit.Assert.*;

public class ShiftTest {
    
    @Test
    public void testApplyGrayImage() {
        GrayImage image = new GrayImage(3, 3);
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage result = shift.apply(image);
        // Add assertions here to test the functionality of apply method with GrayImage
    }
    
    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage image = new RealGrayImage(3, 3);
        Shift shift = new Shift(-2, 2, Shift.WRAP);
        RealGrayImage result = shift.apply(image);
        // Add assertions here to test the functionality of apply method with RealGrayImage
    }
    
    @Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage(3, 3);
        Shift shift = new Shift(0, -1, Shift.NOWRAP);
        ColorImage result = shift.apply(image);
        // Add assertions here to test the functionality of apply method with ColorImage
    }
    
    @Test
    public void testApplyRealColorImage() {
        RealColorImage image = new RealColorImage(3, 3);
        Shift shift = new Shift(2, -2, Shift.WRAP);
        RealColorImage result = shift.apply(image);
        // Add assertions here to test the functionality of apply method with RealColorImage
    }
}