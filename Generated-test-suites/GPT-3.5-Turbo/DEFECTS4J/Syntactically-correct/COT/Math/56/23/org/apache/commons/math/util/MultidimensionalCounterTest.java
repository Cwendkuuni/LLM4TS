
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {
    
    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(3, counter.getDimension());
    }
    
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(24, counter.getSize());
    }
    
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{0, 2, 0}, counter.getCounts(5));
        assertArrayEquals(new int[]{1, 1, 2}, counter.getCounts(22));
    }
    
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(3, counter.getCount(0, 1, 0));
        assertEquals(23, counter.getCount(1, 3, 2));
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_Exception() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCounts(25);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_Exception() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(0, 0);
    }
    
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(0, (int) iterator.next());
        assertArrayEquals(new int[]{0, 0, 0}, iterator.getCounts());
        
        assertTrue(iterator.hasNext());
        assertEquals(1, (int) iterator.next());
        assertArrayEquals(new int[]{0, 0, 1}, iterator.getCounts());
        
        assertTrue(iterator.hasNext());
        assertEquals(2, (int) iterator.next());
        assertArrayEquals(new int[]{0, 0, 2}, iterator.getCounts());
        
        assertTrue(iterator.hasNext());
        assertEquals(3, (int) iterator.next());
        assertArrayEquals(new int[]{0, 1, 0}, iterator.getCounts());
        
        // ... continue testing for other elements
        
        assertFalse(iterator.hasNext());
        assertEquals(23, iterator.getCount());
        assertArrayEquals(new int[]{1, 3, 2}, iterator.getCounts());
    }
    
}
