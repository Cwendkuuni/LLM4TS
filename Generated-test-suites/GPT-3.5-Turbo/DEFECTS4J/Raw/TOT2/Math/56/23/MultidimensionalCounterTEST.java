import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {
    
    @Test
    public void testConstructor() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertNotNull(counter);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(sizes, counter.getSizes());
        assertEquals(24, counter.getSize());
    }
    
    @Test
    public void testGetCounts() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        
        assertArrayEquals(new int[]{0, 0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[]{1, 2, 3}, counter.getCounts(23));
        
        assertThrows(OutOfRangeException.class, () -> counter.getCounts(-1));
        assertThrows(OutOfRangeException.class, () -> counter.getCounts(24));
    }
    
    @Test
    public void testGetCount() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(23, counter.getCount(1, 2, 3));
        
        assertThrows(DimensionMismatchException.class, () -> counter.getCount(0, 0));
        assertThrows(OutOfRangeException.class, () -> counter.getCount(-1, 0, 0));
        assertThrows(OutOfRangeException.class, () -> counter.getCount(2, 0, 0));
    }
    
    @Test
    public void testIterator() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(0, (int) iterator.next());
        assertArrayEquals(new int[]{0, 0, 0}, iterator.getCounts());
        
        assertTrue(iterator.hasNext());
        assertEquals(1, (int) iterator.next());
        assertArrayEquals(new int[]{0, 0, 1}, iterator.getCounts());
        
        // Continue iterating for more test cases
        // ...
    }
}