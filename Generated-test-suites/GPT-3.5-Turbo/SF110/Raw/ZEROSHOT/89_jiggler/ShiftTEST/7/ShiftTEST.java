import static org.junit.Assert.*;
import org.junit.Test;

public class ShiftTest {

    @Test
    public void testApplyGrayImage() {
        GrayImage image = new GrayImage(5, 5);
        image.set(0, 0, 10);
        image.set(1, 1, 20);
        image.set(2, 2, 30);
        image.set(3, 3, 40);
        image.set(4, 4, 50);
        
        Shift shift = new Shift(1, 1, 0);
        GrayImage result = shift.apply(image);
        
        assertEquals(0, result.get(0, 0));
        assertEquals(0, result.get(1, 0));
        assertEquals(0, result.get(0, 1));
        assertEquals(10, result.get(1, 1));
        assertEquals(20, result.get(2, 2));
        assertEquals(0, result.get(3, 3));
        assertEquals(0, result.get(4, 4));
    }
    
    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage image = new RealGrayImage(5, 5);
        image.set(0, 0, 10.5f);
        image.set(1, 1, 20.5f);
        image.set(2, 2, 30.5f);
        image.set(3, 3, 40.5f);
        image.set(4, 4, 50.5f);
        
        Shift shift = new Shift(1, 1, 0);
        RealGrayImage result = shift.apply(image);
        
        assertEquals(0.0f, result.get(0, 0), 0.001);
        assertEquals(0.0f, result.get(1, 0), 0.001);
        assertEquals(0.0f, result.get(0, 1), 0.001);
        assertEquals(10.5f, result.get(1, 1), 0.001);
        assertEquals(20.5f, result.get(2, 2), 0.001);
        assertEquals(0.0f, result.get(3, 3), 0.001);
        assertEquals(0.0f, result.get(4, 4), 0.001);
    }
    
    @Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage(5, 5);
        image.set(0, 0, new int[]{10, 20, 30});
        image.set(1, 1, new int[]{20, 30, 40});
        image.set(2, 2, new int[]{30, 40, 50});
        image.set(3, 3, new int[]{40, 50, 60});
        image.set(4, 4, new int[]{50, 60, 70});
        
        Shift shift = new Shift(1, 1, 0);
        ColorImage result = shift.apply(image);
        
        assertArrayEquals(new int[]{0, 0, 0}, result.get(0, 0));
        assertArrayEquals(new int[]{0, 0, 0}, result.get(1, 0));
        assertArrayEquals(new int[]{0, 0, 0}, result.get(0, 1));
        assertArrayEquals(new int[]{10, 20, 30}, result.get(1, 1));
        assertArrayEquals(new int[]{20, 30, 40}, result.get(2, 2));
        assertArrayEquals(new int[]{0, 0, 0}, result.get(3, 3));
        assertArrayEquals(new int[]{0, 0, 0}, result.get(4, 4));
    }
    
    @Test
    public void testApplyRealColorImage() {
        RealColorImage image = new RealColorImage(5, 5);
        image.set(0, 0, new float[]{10.5f, 20.5f, 30.5f});
        image.set(1, 1, new float[]{20.5f, 30.5f, 40.5f});
        image.set(2, 2, new float[]{30.5f, 40.5f, 50.5f});
        image.set(3, 3, new float[]{40.5f, 50.5f, 60.5f});
        image.set(4, 4, new float[]{50.5f, 60.5f, 70.5f});
        
        Shift shift = new Shift(1, 1, 0);
        RealColorImage result = shift.apply(image);
        
        assertArrayEquals(new float[]{0.0f, 0.0f, 0.0f}, result.get(0, 0), 0.001);
        assertArrayEquals(new float[]{0.0f, 0.0f, 0.0f}, result.get(1, 0), 0.001);
        assertArrayEquals(new float[]{0.0f, 0.0f, 0.0f}, result.get(0, 1), 0.001);
        assertArrayEquals(new float[]{10.5f, 20.5f, 30.5f}, result.get(1, 1), 0.001);
        assertArrayEquals(new float[]{20.5f, 30.5f, 40.5f}, result.get(2, 2), 0.001);
        assertArrayEquals(new float[]{0.0f, 0.0f, 0.0f}, result.get(3, 3), 0.001);
        assertArrayEquals(new float[]{0.0f, 0.0f, 0.0f}, result.get(4, 4), 0.001);
    }
}