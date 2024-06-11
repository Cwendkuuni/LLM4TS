import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;

import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {
    
    @Test
    public void testGetDimension() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int expected = 3;
        int actual = counter.getDimension();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetCounts() throws OutOfRangeException {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] expected = {1, 1};
        int[] actual = counter.getCounts(4);
        assertArrayEquals(expected, actual);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() throws OutOfRangeException {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCounts(6);
    }
    
    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] c = {0, 2};
        int expected = 5;
        int actual = counter.getCount(c);
        assertEquals(expected, actual);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() throws OutOfRangeException, DimensionMismatchException {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] c = {0, 2, 1};
        counter.getCount(c);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() throws OutOfRangeException, DimensionMismatchException {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] c = {1, 4};
        counter.getCount(c);
    }
    
    @Test
    public void testGetSize() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int expected = 6;
        int actual = counter.getSize();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetSizes() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] expected = {2, 3};
        int[] actual = counter.getSizes();
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testToString() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        String expected = "[0][1]";
        String actual = counter.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIterator() {
        int[] sizes = {2, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next().intValue());
        assertEquals(1, iterator.next().intValue());
        assertEquals(2, iterator.next().intValue());
        assertEquals(3, iterator.next().intValue());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testIteratorGetCount() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(-1, iterator.getCount());
        iterator.next();
        assertEquals(0, iterator.getCount());
    }
    
    @Test
    public void testIteratorGetCounts() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int[] expected = {0, 0};
        int[] actual = iterator.getCounts();
        assertArrayEquals(expected, actual);
        iterator.next();
        actual = iterator.getCounts();
        expected = new int[]{0, 1};
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testIteratorGetCountDim() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(0, iterator.getCount(0));
        iterator.next();
        assertEquals(1, iterator.getCount(1));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }
}