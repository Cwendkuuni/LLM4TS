import org.apache.commons.math3.exception.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        assertEquals(3, counter.getDimension());
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testDimensionZero() {
        MultidimensionalCounter counter = new MultidimensionalCounter(0);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testDimensionNegative() {
        MultidimensionalCounter counter = new MultidimensionalCounter(-1);
    }
    
    @Test
    public void testCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        
        // Test typical use case
        assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{2, 3, 4}, counter.getCounts(59));
        
        // Test edge cases
        assertArrayEquals(new int[]{2, 0, 0}, counter.getCounts(40));
        assertArrayEquals(new int[]{0, 3, 0}, counter.getCounts(11));
        assertArrayEquals(new int[]{0, 0, 4}, counter.getCounts(4));
        
        // Test error scenarios
        try {
            counter.getCounts(-1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Test passed
        }
        
        try {
            counter.getCounts(60);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Test passed
        }
    }
    
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        
        // Test typical use case
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(59, counter.getCount(2, 3, 4));
        
        // Test edge cases
        assertEquals(40, counter.getCount(2, 0, 0));
        assertEquals(11, counter.getCount(0, 3, 0));
        assertEquals(4, counter.getCount(0, 0, 4));
        
        // Test error scenarios
        try {
            counter.getCount(-1, 0, 0);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Test passed
        }
        
        try {
            counter.getCount(3, 0, 0);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Test passed
        }
        
        try {
            counter.getCount(2, -1, 0);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Test passed
        }
        
        try {
            counter.getCount(0, 4, 0);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Test passed
        }
        
        try {
            counter.getCount(0, 0, -1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Test passed
        }
        
        try {
            counter.getCount(0, 0, 5);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Test passed
        }
        
        try {
            counter.getCount(0, 0);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Test passed
        }
        
        try {
            counter.getCount(0, 0, 0, 0);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Test passed
        }
    }
    
    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        assertArrayEquals(new int[]{3, 4, 5}, counter.getSizes());
    }
    
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        assertEquals(60, counter.getSize());
    }
    
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        
        // Test typical use case
        int count = 0;
        for (int i : counter) {
            count++;
        }
        assertEquals(4, count);
        
        // Test edge case
        counter = new MultidimensionalCounter(1);
        count = 0;
        for (int i : counter) {
            count++;
        }
        assertEquals(1, count);
    }
}