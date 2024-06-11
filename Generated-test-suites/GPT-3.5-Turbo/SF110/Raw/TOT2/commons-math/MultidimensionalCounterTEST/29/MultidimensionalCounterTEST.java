
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = {3, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetDimension() {
        int[] size = {3, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
    }
    
    @Test
    public void testGetCounts() {
        int[] size = {3, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertArrayEquals(new int[]{2, 0, 1}, counter.getCounts(14));
    }
    
    @Test
    public void testGetCount() {
        int[] size = {3, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(14, counter.getCount(2, 0, 1));
    }
    
    @Test
    public void testIteratorHasNext() {
        int[] size = {3, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        for (int i = 0; i < 23; i++) {
            iterator.next();
        }
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testIteratorNext() {
        int[] size = {3, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(0, (int) iterator.next());
        assertEquals(1, (int) iterator.next());
    }
    
    @Test
    public void testGetSizes() {
        int[] size = {3, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertArrayEquals(new int[]{3, 4, 2}, counter.getSizes());
    }
    
    @Test
    public void testToString() {
        int[] size = {3, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals("[3][0][1]", counter.toString());
    }
    
    @Test
    public void testIteratorGetCount() {
        int[] size = {3, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.next();
        assertEquals(0, iterator.getCount());
    }
}
