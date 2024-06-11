import static org.junit.Assert.*;
import org.junit.*;

public class MultidimensionalCounterTest {
    
    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertEquals(2, counter.getDimension());
    }
    
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertArrayEquals(new int[]{1, 2}, counter.getCounts(5));
    }
    
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertEquals(5, counter.getCount(1, 2));
    }
    
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertEquals(6, counter.getSize());
    }
    
    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertArrayEquals(new int[]{2, 3}, counter.getSizes());
    }
    
    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertEquals("[0][0]", counter.toString());
    }
    
    @Test
    public void testIteratorHasNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
    }
    
    @Test
    public void testIteratorNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(Integer.valueOf(0), iterator.next());
    }
    
    @Test
    public void testIteratorGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(0, iterator.getCount());
    }
    
    @Test
    public void testIteratorGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertArrayEquals(new int[]{0, 0}, iterator.getCounts());
    }
    
    @Test
    public void testIteratorGetCountWithDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(0, iterator.getCount(0));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }
}