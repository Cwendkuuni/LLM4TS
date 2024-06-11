
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(0, (int) iterator.next());
        assertEquals(1, (int) iterator.next());
        //...
        assertEquals(23, (int) iterator.next());
        assertFalse(iterator.hasNext());
        
        assertEquals(23, iterator.getCount());
        int[] counts = iterator.getCounts();
        assertEquals(1, counts[0]);
        assertEquals(3, counts[1]);
        assertEquals(2, counts[2]);
        assertEquals(2, iterator.getCount(2));
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        int[] counts = counter.getCounts(12);
        assertEquals(1, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(0, counts[2]);
        
        counts = counter.getCounts(23);
        assertEquals(1, counts[0]);
        assertEquals(3, counts[1]);
        assertEquals(2, counts[2]);
        
        try {
            counts = counter.getCounts(30);
            fail("OutOfRangeException not thrown");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        int count = counter.getCount(1, 1, 1);
        assertEquals(16, count);
        
        try {
            count = counter.getCount(2, 3, 4);
            fail("OutOfRangeException not thrown");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
        
        try {
            count = counter.getCount(1, 1, -1);
            fail("OutOfRangeException not thrown");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        assertEquals(24, counter.getSize());
    }
    
    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        int[] sizes = counter.getSizes();
        assertEquals(2, sizes[0]);
        assertEquals(3, sizes[1]);
        assertEquals(4, sizes[2]);
    }
    
    @Test
    public void testConstructor() {
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(-2, 3, 4);
            fail("NotStrictlyPositiveException not thrown");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }
}
