import org.junit.Test;
import static org.junit.Assert.*;

public class ShiftTest {
    
    @Test
    public void testConstructor() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        assertNotNull(shift);
        assertEquals(2, shift.xoffset);
        assertEquals(3, shift.yoffset);
        assertTrue(shift.wrap);
    }
    
    @Test
    public void testApplyGrayImage() {
        Shift shift = new Shift(2, 3, Shift.NOWRAP);
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 100);
        image.set(1, 1, 200);
        GrayImage result = shift.apply(image);
        assertEquals(100, result.get(2, 3));
        assertEquals(200, result.get(0, 1));
        assertEquals(0, result.get(0, 0));
    }
    
    @Test
    public void testApplyRealGrayImage() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.5f);
        image.set(1, 1, 2.5f);
        RealGrayImage result = shift.apply(image);
        assertEquals(1.5f, result.get(2, 3), 0.001);
        assertEquals(2.5f, result.get(0, 1), 0.001);
        assertEquals(0.0f, result.get(0, 0), 0.001);
    }
    
    @Test
    public void testApplyColorImage() {
        Shift shift = new Shift(2, 3, Shift.NOWRAP);
        ColorImage image = new ColorImage(3, 3);
        image.set(0, 0, 100, 0, 0);
        image.set(1, 1, 0, 200, 0);
        ColorImage result = shift.apply(image);
        assertEquals(100, result.get(2, 3, 0));
        assertEquals(200, result.get(0, 1, 1));
        assertEquals(0, result.get(0, 0, 2));
    }
    
    @Test
    public void testApplyRealColorImage() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        RealColorImage image = new RealColorImage(3, 3);
        image.set(0, 0, 1.5f, 0.0f, 0.0f);
        image.set(1, 1, 0.0f, 2.5f, 0.0f);
        RealColorImage result = shift.apply(image);
        assertEquals(1.5f, result.get(2, 3, 0), 0.001);
        assertEquals(2.5f, result.get(0, 1, 1), 0.001);
        assertEquals(0.0f, result.get(0, 0, 2), 0.001);
    }
    
    @Test
    public void testConstructorNegativeOffsets() {
        Shift shift = new Shift(-2, -3, Shift.WRAP);
        assertNotNull(shift);
        assertEquals(-2, shift.xoffset);
        assertEquals(-3, shift.yoffset);
        assertTrue(shift.wrap);
    }
    
    @Test(expected = NullPointerException.class)
    public void testApplyGrayImageNullInput() {
        Shift shift = new Shift(2, 3, Shift.NOWRAP);
        GrayImage image = null;
        shift.apply(image);
    }
    
    @Test(expected = NullPointerException.class)
    public void testApplyRealGrayImageNullInput() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        RealGrayImage image = null;
        shift.apply(image);
    }
    
    @Test(expected = NullPointerException.class)
    public void testApplyColorImageNullInput() {
        Shift shift = new Shift(2, 3, Shift.NOWRAP);
        ColorImage image = null;
        shift.apply(image);
    }
    
    @Test(expected = NullPointerException.class)
    public void testApplyRealColorImageNullInput() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        RealColorImage image = null;
        shift.apply(image);
    }
}