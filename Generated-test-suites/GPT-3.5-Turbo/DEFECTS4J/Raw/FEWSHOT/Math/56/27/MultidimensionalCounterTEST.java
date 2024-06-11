/*
* Class: MultidimensionalCounter
* Methods: getDimension(), getSize(), getSizes(), getCounts(int), getCount(int...)
*/
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultidimensionalCounterTest {
    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter.getDimension());
    }
    
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(24, counter.getSize());
    }
    
    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] expectedSizes = {2, 3, 4};
        assertArrayEquals(expectedSizes, counter.getSizes());
    }
    
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] counts = counter.getCounts(5);
        int[] expectedCounts = {0, 1, 1};
        assertArrayEquals(expectedCounts, counts);
    }
    
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int count = counter.getCount(0, 1, 1);
        assertEquals(5, count);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWithInvalidDimensions() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(0, 1);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithInvalidValues() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(0, 1, 10);
    }
    
}