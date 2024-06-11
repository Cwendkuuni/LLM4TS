import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {
    
    // Test Iterator class
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        // Check hasNext()
        assertTrue(iterator.hasNext());
        
        // Check next() and getCount()
        assertEquals(0, (int) iterator.next());
        assertEquals(1, iterator.getCount());
        
        // Check getCounts()
        int[] counts = iterator.getCounts();
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(0, counts[2]);
    }
    
    // Test getCounts() method
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        // Typical case
        int[] indices = counter.getCounts(7);
        assertEquals(1, indices[0]);
        assertEquals(1, indices[1]);
        assertEquals(3, indices[2]);
        
        // Edge case
        indices = counter.getCounts(0);
        assertEquals(0, indices[0]);
        assertEquals(0, indices[1]);
        assertEquals(0, indices[2]);
        
        // Exception handling
        try {
            counter.getCounts(30);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Pass
        }
    }
    
    // Test getCount() method
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        // Typical case
        assertEquals(11, counter.getCount(1, 1, 2));
        
        // Edge case
        assertEquals(0, counter.getCount(0, 0, 0));
        
        // Exception handling
        try {
            counter.getCount(3, 2, 1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Pass
        }
        
        try {
            counter.getCount(1, 3, 2);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Pass
        }
        
        try {
            counter.getCount(2, 1, 0);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Pass
        }
    }
    
    // Test getSize() method
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(24, counter.getSize());
    }
    
    // Test getSizes() method
    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] sizes = counter.getSizes();
        assertEquals(2, sizes[0]);
        assertEquals(3, sizes[1]);
        assertEquals(4, sizes[2]);
    }
    
    // Test toString() method
    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals("[0][0][0]", counter.toString());
    }

}