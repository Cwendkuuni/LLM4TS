import static org.junit.Assert.*;
import org.junit.Test;

public class ShiftTest {
    
    @Test
    public void testShiftGrayImageWrap() {
        // Test case for applying shift operation on GrayImage with WRAP option
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 1, 2);
        int xoffset = 1;
        int yoffset = 1;
        Shift shift = new Shift(xoffset, yoffset, Shift.WRAP);
        GrayImage result = shift.apply(image);
        assertEquals(2, result.get(0, 0));
        assertEquals(1, result.get(2, 2));
    }
    
    @Test
    public void testShiftGrayImageNoWrap() {
        // Test case for applying shift operation on GrayImage with NO_WRAP option
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 1);
        int xoffset = 2;
        int yoffset = 2;
        Shift shift = new Shift(xoffset, yoffset, Shift.NOWRAP);
        GrayImage result = shift.apply(image);
        assertEquals(0, result.get(0, 0));
        assertEquals(0, result.get(2, 2));
    }
    
    @Test
    public void testShiftRealGrayImageWrap() {
        // Test case for applying shift operation on RealGrayImage with WRAP option
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.0f);
        image.set(1, 1, 2.0f);
        int xoffset = 1;
        int yoffset = 1;
        Shift shift = new Shift(xoffset, yoffset, Shift.WRAP);
        RealGrayImage result = shift.apply(image);
        assertEquals(2.0f, result.get(0, 0), 0.001);
        assertEquals(1.0f, result.get(2, 2), 0.001);
    }
    
    @Test
    public void testShiftRealGrayImageNoWrap() {
        // Test case for applying shift operation on RealGrayImage with NO_WRAP option
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.0f);
        int xoffset = 2;
        int yoffset = 2;
        Shift shift = new Shift(xoffset, yoffset, Shift.NOWRAP);
        RealGrayImage result = shift.apply(image);
        assertEquals(0.0f, result.get(0, 0), 0.001);
        assertEquals(0.0f, result.get(2, 2), 0.001);
    }
    
    @Test
    public void testShiftColorImage() {
        // Test case for applying shift operation on ColorImage
        ColorImage image = new ColorImage(3, 3);
        image.plane(0).set(0, 0, 1);
        image.plane(1).set(0, 0, 2);
        image.plane(2).set(0, 0, 3);
        int xoffset = 1;
        int yoffset = 1;
        Shift shift = new Shift(xoffset, yoffset, Shift.WRAP);
        ColorImage result = shift.apply(image);
        assertEquals(2, result.plane(0).get(1, 1));
        assertEquals(3, result.plane(1).get(1, 1));
        assertEquals(1, result.plane(2).get(1, 1));
    }
    
    @Test
    public void testShiftRealColorImage() {
        // Test case for applying shift operation on RealColorImage
        RealColorImage image = new RealColorImage(3, 3);
        image.plane(0).set(0, 0, 1.0f);
        image.plane(1).set(0, 0, 2.0f);
        image.plane(2).set(0, 0, 3.0f);
        int xoffset = 1;
        int yoffset = 1;
        Shift shift = new Shift(xoffset, yoffset, Shift.WRAP);
        RealColorImage result = shift.apply(image);
        assertEquals(2.0f, result.plane(0).get(1, 1), 0.001);
        assertEquals(3.0f, result.plane(1).get(1, 1), 0.001);
        assertEquals(1.0f, result.plane(2).get(1, 1), 0.001);
    }
}