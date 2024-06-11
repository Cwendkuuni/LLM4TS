
import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {
    
    @Test
    public void testMultidimensionalCounterIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int expectedCount = 0;
        while(iterator.hasNext()) {
            assertEquals(expectedCount, iterator.next().intValue());
            expectedCount++;
        }
    }
    
    @Test
    public void testMultidimensionalCounterGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertArrayEquals(new int[]{0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{1, 0}, counter.getCounts(1));
        assertArrayEquals(new int[]{0, 2}, counter.getCounts(5));
    }
    
    @Test
    public void testMultidimensionalCounterGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertEquals(0, counter.getCount(0, 0));
        assertEquals(5, counter.getCount(1, 2));
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterGetCountsException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.getCounts(8);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterGetCountException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.getCount(2, 0);
    }
    
    @Test
    public void testMultidimensionalCounterGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertEquals(6, counter.getSize());
    }
    
    @Test
    public void testMultidimensionalCounterGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertArrayEquals(new int[]{2, 3}, counter.getSizes());
    }
    
    @Test
    public void testMultidimensionalCounterToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertEquals("[0][0]", counter.toString());
    }
}
