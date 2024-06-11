import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MultidimensionalCounterTest {
    
    private MultidimensionalCounter counter;
    
    @Before
    public void setup() {
        counter = new MultidimensionalCounter(2, 4, 3);
    }
    
    @Test
    public void testGetDimension() {
        assertEquals(3, counter.getDimension());
    }
    
    @Test
    public void testGetSize() {
        assertEquals(24, counter.getSize());
    }
    
    @Test
    public void testGetCounts() {
        int[] expected1 = {0, 0, 0};
        int[] counts1 = counter.getCounts(0);
        assertArrayEquals(expected1, counts1);
        
        int[] expected2 = {1, 3, 2};
        int[] counts2 = counter.getCounts(23);
        assertArrayEquals(expected2, counts2);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        counter.getCounts(24);
    }
    
    @Test
    public void testGetCount() {
        int[] counts1 = {0, 0, 0};
        assertEquals(0, counter.getCount(counts1));
        
        int[] counts2 = {1, 3, 2};
        assertEquals(23, counter.getCount(counts2));
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        int[] counts = {0, 0};
        counter.getCount(counts);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] counts = {0, 0, 3};
        counter.getCount(counts);
    }
    
    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next().intValue());
        assertArrayEquals(new int[] {0, 0, 0}, iterator.getCounts());
        
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next().intValue());
        assertArrayEquals(new int[] {0, 0, 1}, iterator.getCounts());
        
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next().intValue());
        assertArrayEquals(new int[] {0, 0, 2}, iterator.getCounts());
        
        // ... More assertions
        
        assertTrue(iterator.hasNext());
        assertEquals(23, iterator.next().intValue());
        assertArrayEquals(new int[] {1, 3, 2}, iterator.getCounts());
        
        assertFalse(iterator.hasNext());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }

    @Test
    public void testToString() {
        assertEquals("[0][0][0]", counter.toString());
    }
}