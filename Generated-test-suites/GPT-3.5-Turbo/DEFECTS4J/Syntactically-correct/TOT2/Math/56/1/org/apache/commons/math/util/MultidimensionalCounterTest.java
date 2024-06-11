import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {
    
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        int expectedCount = 0;
        
        while(iterator.hasNext()) {
            int nextCount = iterator.next();
            assertEquals(expectedCount, nextCount);
            expectedCount++;
        }
    }
    
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        
        int[] counts = counter.getCounts(4);
        assertArrayEquals(new int[] {1, 0, 0}, counts);
    }
    
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        
        int count = counter.getCount(1, 2);
        assertEquals(5, count);
        
        try {
            count = counter.getCount(2, 2);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }
        
        try {
            count = counter.getCount(1, 3);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
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
        assertArrayEquals(new int[] {2, 3, 4}, sizes);
    }
}