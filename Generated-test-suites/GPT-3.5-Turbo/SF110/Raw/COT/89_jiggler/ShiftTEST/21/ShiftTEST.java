
package jigl.image.ops;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShiftTest {

    @Test
    public void testApplyGrayImageNoWrap() {
        int[][] imageData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        GrayImage grayImage = new GrayImage(imageData);
        
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        
        GrayImage result = shift.apply(grayImage);
        
        int[][] expectedData = {{0, 0, 0}, {0, 1, 2}, {0, 4, 5}};
        
        assertEquals(expectedData, result.getData());
    }
    
    @Test
    public void testApplyGrayImageWrap() {
        int[][] imageData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        GrayImage grayImage = new GrayImage(imageData);
        
        Shift shift = new Shift(1, 1, Shift.WRAP);
        
        GrayImage result = shift.apply(grayImage);
        
        int[][] expectedData = {{9, 7, 8}, {3, 1, 2}, {6, 4, 5}};
        
        assertEquals(expectedData, result.getData());
    }
    
    // Add more basic test cases for other methods in the Shift class...
}
