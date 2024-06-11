import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {
    
    @Test
    public void testIterator() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        assertEquals(0, iterator.getCount());
        assertTrue(iterator.hasNext());
        
        int[] counts = iterator.getCounts();
        assertEquals(2, counts.length);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        
        assertEquals(0, (int) iterator.next());
        assertEquals(1, iterator.getCount());
        
        counts = iterator.getCounts();
        assertEquals(2, counts.length);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
    }
    
    @Test
    public void testGetCounts() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        
        int[] counts = counter.getCounts(1);
        assertEquals(2, counts.length);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        
        counts = counter.getCounts(4);
        assertEquals(2, counts.length);
        assertEquals(1, counts[0]);
        assertEquals(1, counts[1]);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfBoundsException() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        
        counter.getCounts(6);
    }
    
    @Test
    public void testGetCount() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        
        int count = counter.getCount(1, 1);
        assertEquals(4, count);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatchException() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        
        counter.getCount(1);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeException() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        
        counter.getCount(1, 3);
    }
    
    @Test
    public void testGetSize() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        
        assertEquals(6, counter.getSize());
    }
    
    @Test
    public void testGetSizes() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        
        int[] sizesResult = counter.getSizes();
        
        assertEquals(2, sizesResult.length);
        assertEquals(2, sizesResult[0]);
        assertEquals(3, sizesResult[1]);
    }
}