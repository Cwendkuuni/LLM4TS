import org.junit.*;
import static org.junit.Assert.*;

public class ShiftTest {

    @Test
    public void testApply_GrayImage_wrap() {
        Shift shift = new Shift(2, 2, 1);
        GrayImage image = new GrayImage(4, 4);
        GrayImage expected = new GrayImage(4, 4);
        
        // Set initial image values
        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                image.set(x, y, y * 4 + x + 1);
            }
        }
        
        // Set expected image values after applying shift
        expected.set(2, 2, 1);
        expected.set(3, 2, 2);
        expected.set(0, 3, 5);
        expected.set(1, 3, 6);
        expected.set(2, 3, 9);
        expected.set(3, 3, 10);
        
        // Apply the shift
        GrayImage result = shift.apply(image);
        
        // Check if the result matches the expected values
        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                assertEquals(expected.get(x, y), result.get(x, y));
            }
        }
    }

    @Test
    public void testApply_GrayImage_noWrap() {
        Shift shift = new Shift(2, 2, 0);
        GrayImage image = new GrayImage(4, 4);
        GrayImage expected = new GrayImage(4, 4);
        
        // Set initial image values
        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                image.set(x, y, y * 4 + x + 1);
            }
        }
        
        // Set expected image values after applying shift
        expected.set(2, 2, 7);
        expected.set(3, 2, 8);
        expected.set(2, 3, 11);
        expected.set(3, 3, 12);
        
        // Apply the shift
        GrayImage result = shift.apply(image);
        
        // Check if the result matches the expected values
        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                assertEquals(expected.get(x, y), result.get(x, y));
            }
        }
    }

    @Test
    public void testApply_RealGrayImage_wrap() {
        Shift shift = new Shift(2, 2, 1);
        RealGrayImage image = new RealGrayImage(4, 4);
        RealGrayImage expected = new RealGrayImage(4, 4);
        
        // Set initial image values
        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                image.set(x, y, (float)(y * 4 + x + 1) / 10.0f);
            }
        }
        
        // Set expected image values after applying shift
        expected.set(2, 2, 0.1f);
        expected.set(3, 2, 0.2f);
        expected.set(0, 3, 0.5f);
        expected.set(1, 3, 0.6f);
        expected.set(2, 3, 0.9f);
        expected.set(3, 3, 1.0f);
        
        // Apply the shift
        RealGrayImage result = shift.apply(image);
        
        // Check if the result matches the expected values
        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                assertEquals(expected.get(x, y), result.get(x, y), 0.001f);
            }
        }
    }

    @Test
    public void testApply_RealGrayImage_noWrap() {
        Shift shift = new Shift(2, 2, 0);
        RealGrayImage image = new RealGrayImage(4, 4);
        RealGrayImage expected = new RealGrayImage(4, 4);
        
        // Set initial image values
        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                image.set(x, y, (float)(y * 4 + x + 1) / 10.0f);
            }
        }
        
        // Set expected image values after applying shift
        expected.set(2, 2, 0.7f);
        expected.set(3, 2, 0.8f);
        expected.set(2, 3, 1.1f);
        expected.set(3, 3, 1.2f);
        
        // Apply the shift
        RealGrayImage result = shift.apply(image);
        
        // Check if the result matches the expected values
        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                assertEquals(expected.get(x, y), result.get(x, y), 0.001f);
            }
        }
    }

    @Test
    public void testApply_ColorImage() {
        Shift shift = new Shift(2, 2, 1);
        ColorImage image = new ColorImage(4, 4);
        ColorImage expected = new ColorImage(4, 4);
        
        // Set initial image values
        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                image.set(x, y, new int[] { y * 4 + x + 1, y * 4 + x + 1, y * 4 + x + 1 });
            }
        }
        
        // Set expected image values after applying shift
        expected.set(2, 2, new int[] { 1, 1, 1 });
        expected.set(3, 2, new int[] { 2, 2, 2 });
        expected.set(0, 3, new int[] { 5, 5, 5 });
        expected.set(1, 3, new int[] { 6, 6, 6 });
        expected.set(2, 3, new int[] { 9, 9, 9 });
        expected.set(3, 3, new int[] { 10, 10, 10 });
        
        // Apply the shift
        ColorImage result = shift.apply(image);
        
        // Check if the result matches the expected values
        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                assertArrayEquals(expected.get(x, y), result.get(x, y));
            }
        }
    }

    @Test
    public void testApply_RealColorImage() {
        Shift shift = new Shift(2, 2, 1);
        RealColorImage image = new RealColorImage(4, 4);
        RealColorImage expected = new RealColorImage(4, 4);
        
        // Set initial image values
        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                image.set(x, y, new float[] { (float)(y * 4 + x + 1) / 10.0f, (float)(y * 4 + x + 1) / 10.0f, (float)(y * 4 + x + 1) / 10.0f });
            }
        }
        
        // Set expected image values after applying shift
        expected.set(2, 2, new float[] { 0.1f, 0.1f, 0.1f });
        expected.set(3, 2, new float[] { 0.2f, 0.2f, 0.2f });
        expected.set(0, 3, new float[] { 0.5f, 0.5f, 0.5f });
        expected.set(1, 3, new float[] { 0.6f, 0.6f, 0.6f });
        expected.set(2, 3, new float[] { 0.9f, 0.9f, 0.9f });
        expected.set(3, 3, new float[] { 1.0f, 1.0f, 1.0f });
        
        // Apply the shift
        RealColorImage result = shift.apply(image);
        
        // Check if the result matches the expected values
        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                assertArrayEquals(expected.get(x, y), result.get(x, y), 0.001f);
            }
        }
    }

}