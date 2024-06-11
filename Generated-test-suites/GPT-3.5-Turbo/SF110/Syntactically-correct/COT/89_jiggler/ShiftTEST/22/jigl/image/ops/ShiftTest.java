
import org.junit.Test;
import static org.junit.Assert.*;

public class ShiftTest {
    
    @Test
    public void testApply() {
        // Test for GrayImage
        
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 1, 2);
        image.set(2, 2, 3);
        
        Shift shift = new Shift(1, 1, 1);
        GrayImage result = shift.apply(image);
        
        assertEquals(3, result.get(0, 0));
        assertEquals(0, result.get(1, 1));
        assertEquals(1, result.get(2, 2));
        
        // Test for RealGrayImage
        
        RealGrayImage realImage = new RealGrayImage(3, 3);
        realImage.set(0, 0, 1.1f);
        realImage.set(1, 1, 2.2f);
        realImage.set(2, 2, 3.3f);
        
        Shift realShift = new Shift(1, 1, 1);
        RealGrayImage realResult = realShift.apply(realImage);
        
        assertEquals(3.3f, realResult.get(0, 0), 0.001);
        assertEquals(0.0f, realResult.get(1, 1), 0.001);
        assertEquals(1.1f, realResult.get(2, 2), 0.001);
        
        // Test for ColorImage
        
        ColorImage colorImage = new ColorImage(3, 3);
        colorImage.set(0, 0, new int[]{1, 2, 3});
        colorImage.set(1, 1, new int[]{4, 5, 6});
        colorImage.set(2, 2, new int[]{7, 8, 9});
        
        Shift colorShift = new Shift(1, 1, 1);
        ColorImage colorResult = colorShift.apply(colorImage);
        
        assertArrayEquals(new int[]{7, 8, 9}, colorResult.get(0, 0));
        assertArrayEquals(new int[]{0, 0, 0}, colorResult.get(1, 1));
        assertArrayEquals(new int[]{1, 2, 3}, colorResult.get(2, 2));
        
        // Test for RealColorImage
        
        RealColorImage realColorImage = new RealColorImage(3, 3);
        realColorImage.set(0, 0, new float[]{1.1f, 2.2f, 3.3f});
        realColorImage.set(1, 1, new float[]{4.4f, 5.5f, 6.6f});
        realColorImage.set(2, 2, new float[]{7.7f, 8.8f, 9.9f});
        
        Shift realColorShift = new Shift(1, 1, 1);
        RealColorImage realColorResult = realColorShift.apply(realColorImage);
        
        assertArrayEquals(new float[]{7.7f, 8.8f, 9.9f}, realColorResult.get(0, 0), 0.001);
        assertArrayEquals(new float[]{0.0f, 0.0f, 0.0f}, realColorResult.get(1, 1), 0.001);
        assertArrayEquals(new float[]{1.1f, 2.2f, 3.3f}, realColorResult.get(2, 2), 0.001);
    }
}
