import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {
    private MultidimensionalCounter counter;
    
    @Test
    public void testGetDimension() {
        counter = new MultidimensionalCounter(3, 4, 5);
        assertEquals(3, counter.getDimension());
    }
    
    @Test
    public void testGetCounts() {
        counter = new MultidimensionalCounter(2, 3, 4);
        assertArrayEquals(new int[]{1, 2, 3}, counter.getCounts(11));
    }
    
    @Test
    public void testGetCount() {
        counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(11, counter.getCount(1, 2, 3));
    }
    
    @Test
    public void testGetSize() {
        counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(24, counter.getSize());
    }
    
    @Test
    public void testGetSizes() {
        counter = new MultidimensionalCounter(2, 3, 4);
        assertArrayEquals(new int[]{2, 3, 4}, counter.getSizes());
    }
    
    @Test
    public void testIterator() {
        counter = new MultidimensionalCounter(2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next().intValue());
        assertFalse(iterator.hasNext());
    }
    
    // Test exception handling cases for getCount method
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWithDimensionMismatchException() {
        counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(1, 2);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithOutOfRangeException() {
        counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(1, 2, 10);
    }
    
    // Test edge case for getCount method
    @Test
    public void testGetCountEdgeCase() {
        counter = new MultidimensionalCounter(2, 2, 2);
        assertEquals(7, counter.getCount(1, 1, 1));
    }
}