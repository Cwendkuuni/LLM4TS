import org.apache.commons.math3.exception.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testGetDimension() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorThrowsException() {
        int[] size = {0, 0, 0};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
    }
    
    @Test
    public void testGetCounts() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] counts = counter.getCounts(10);
        int[] expectedCounts = {0, 1, 1};
        assertArrayEquals(expectedCounts, counts);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountsThrowsException() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCounts(30);
    }
    
    @Test
    public void testGetCount() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int count = counter.getCount(0, 1, 2);
        assertEquals(8, count);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountThrowsException() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(0, 1);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountThrowsException2() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(0, 4, 2);
    }
    
    @Test
    public void testGetSize() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(24, counter.getSize());
    }
    
    @Test
    public void testGetSizes() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] sizes = counter.getSizes();
        int[] expectedSizes = {2, 3, 4};
        assertArrayEquals(expectedSizes, sizes);
    }
    
    @Test
    public void testIterator() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next().intValue());
        assertEquals(1, iterator.getCount());
        
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next().intValue());
        assertEquals(2, iterator.getCount());
        
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next().intValue());
        assertEquals(3, iterator.getCount());
        
        assertTrue(iterator.hasNext());
        assertEquals(3, iterator.next().intValue());
        assertEquals(4, iterator.getCount());
        
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testIteratorGetCount() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        assertEquals(0, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        
        assertEquals(0, iterator.next().intValue());
        assertEquals(1, iterator.getCount());
        
        assertEquals(1, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        
        assertEquals(1, iterator.next().intValue());
        assertEquals(2, iterator.getCount());
        
        assertEquals(1, iterator.getCount(0));
        assertEquals(1, iterator.getCount(1));
        
        assertEquals(2, iterator.next().intValue());
        assertEquals(3, iterator.getCount());
        
        assertEquals(1, iterator.getCount(0));
        assertEquals(2, iterator.getCount(1));
        
        assertEquals(3, iterator.next().intValue());
        assertEquals(4, iterator.getCount());
        
        assertEquals(2, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        iterator.remove();
    }
}