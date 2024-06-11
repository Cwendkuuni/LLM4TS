
import org.junit.*;
import static org.junit.Assert.*;

public class ShiftTest {

    @Test
    public void testShiftWithWrap() {
        // Test case using wrap
        int x = 3;
        int y = 2;
        int wrap = 1;
        int[][] input = {
            { 1, 2, 3, 4 },
            { 5, 6, 7, 8 },
            { 9, 10, 11, 12 },
            { 13, 14, 15, 16 }
        };
        int[][] expectedOutput = {
            { 7, 8, 9, 10 },
            { 11, 12, 13, 14 },
            { 15, 16, 1, 2 },
            { 3, 4, 5, 6 }
        };
      
        GrayImage inputImage = new GrayImage(input);
        GrayImage expectedOutputImage = new GrayImage(expectedOutput);
      
        Shift shift = new Shift(x, y, wrap);
        GrayImage result = shift.apply(inputImage);
      
        assertEquals(expectedOutputImage.getData(), result.getData());
    }
  
    @Test
    public void testShiftWithoutWrap() {
        // Test case without wrap
        int x = -2;
        int y = 1;
        int wrap = 0;
        int[][] input = {
            { 1, 2, 3, 4 },
            { 5, 6, 7, 8 },
            { 9, 10, 11, 12 },
            { 13, 14, 15, 16 }
        };
        int[][] expectedOutput = {
            { 0, 0, 0, 0 },
            { 0, 1, 2, 3 },
            { 0, 5, 6, 7 },
            { 0, 9, 10, 11 }
        };
      
        GrayImage inputImage = new GrayImage(input);
        GrayImage expectedOutputImage = new GrayImage(expectedOutput);
      
        Shift shift = new Shift(x, y, wrap);
        GrayImage result = shift.apply(inputImage);
      
        assertEquals(expectedOutputImage.getData(), result.getData());
    }
  
    @Test
    public void testShiftWithWrapAndNegativeOffset() {
        // Test case using wrap and negative offsets
        int x = -2;
        int y = -3;
        int wrap = 1;
        int[][] input = {
            { 1, 2, 3, 4 },
            { 5, 6, 7, 8 },
            { 9, 10, 11, 12 },
            { 13, 14, 15, 16 }
        };
        int[][] expectedOutput = {
            { 10, 11, 12, 13 },
            { 14, 15, 16, 0 },
            { 1, 2, 3, 4 },
            { 5, 6, 7, 8 }
        };
      
        GrayImage inputImage = new GrayImage(input);
        GrayImage expectedOutputImage = new GrayImage(expectedOutput);
      
        Shift shift = new Shift(x, y, wrap);
        GrayImage result = shift.apply(inputImage);
      
        assertEquals(expectedOutputImage.getData(), result.getData());
    }
  
    @Test
    public void testShiftWithLargeOffsets() {
        // Test case with large offsets
        int x = 10;
        int y = 5;
        int wrap = 1;
        int[][] input = {
            { 1, 2, 3, 4 },
            { 5, 6, 7, 8 },
            { 9, 10, 11, 12 },
            { 13, 14, 15, 16 }
        };
        int[][] expectedOutput = {
            { 11, 12, 13, 14 },
            { 15, 16, 0, 0 },
            { 1, 2, 3, 4 },
            { 5, 6, 7, 8 }
        };
      
        GrayImage inputImage = new GrayImage(input);
        GrayImage expectedOutputImage = new GrayImage(expectedOutput);
      
        Shift shift = new Shift(x, y, wrap);
        GrayImage result = shift.apply(inputImage);
      
        assertEquals(expectedOutputImage.getData(), result.getData());
    }
  
    @Test
    public void testShiftWithZeroOffsets() {
        // Test case with zero offsets
        int x = 0;
        int y = 0;
        int wrap = 1;
        int[][] input = {
            { 1, 2, 3, 4 },
            { 5, 6, 7, 8 },
            { 9, 10, 11, 12 },
            { 13, 14, 15, 16 }
        };
      
        GrayImage inputImage = new GrayImage(input);
      
        Shift shift = new Shift(x, y, wrap);
        GrayImage result = shift.apply(inputImage);
      
        assertEquals(inputImage.getData(), result.getData());
    }
  
    @Test
    public void testShiftWithEmptyImage() {
        // Test case with empty image
        int x = 1;
        int y = 1;
        int wrap = 1;
        int[][] input = {
            {}
        };
      
        GrayImage inputImage = new GrayImage(input);
      
        Shift shift = new Shift(x, y, wrap);
        GrayImage result = shift.apply(inputImage);
      
        assertTrue(result.isEmpty());
    }
  
    @Test
    public void testShiftWithNullImage() {
        // Test case with null image
        int x = 1;
        int y = 1;
        int wrap = 1;
        GrayImage inputImage = null;
      
        Shift shift = new Shift(x, y, wrap);
        GrayImage result = shift.apply(inputImage);
      
        assertNull(result);
    }
}
