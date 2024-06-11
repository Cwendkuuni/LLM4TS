import org.apache.commons.math3.exception.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {
    
    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter.getDimension());
    }
    
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        
        // Test valid index
        int[] expected1 = {1, 2};
        assertArrayEquals(expected1, counter.getCounts(5));
        
        // Test index out of range
        try {
            counter.getCounts(6);
            fail("Expected exception not thrown");
        } catch (OutOfRangeException e) {
            assertEquals(6, e.getArgument());
            assertEquals(0, e.getLowerBound());
            assertEquals(6, e.getUpperBound());
        }
    }
    
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        
        // Test valid counts
        assertEquals(5, counter.getCount(1, 2));
        assertEquals(0, counter.getCount(0, 0));
        
        // Test counts with out of range indices
        try {
            counter.getCount(-1, 2);
            fail("Expected exception not thrown");
        } catch (OutOfRangeException e) {
            assertEquals(-1, e.getArgument());
            assertEquals(0, e.getLowerBound());
            assertEquals(1, e.getUpperBound());
        }
        
        // Test counts with dimension mismatch
        try {
            counter.getCount(1);
            fail("Expected exception not thrown");
        } catch (DimensionMismatchException e) {
            assertEquals(1, e.getDimension1());
            assertEquals(2, e.getDimension2());
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
        int[] expected = {2, 3, 4};
        assertArrayEquals(expected, counter.getSizes());
    }
    
    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertEquals("[0][0]", counter.toString());
    }
    
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        // Test initial state
        assertTrue(iterator.hasNext());
        assertEquals(-1, iterator.getCount());
        int[] expected1 = {0, 0};
        assertArrayEquals(expected1, iterator.getCounts());
        
        // Test next()
        assertEquals(0, (int)iterator.next());
        int[] expected2 = {0, 1};
        assertArrayEquals(expected2, iterator.getCounts());
        assertEquals(1, (int)iterator.next());
        int[] expected3 = {1, 0};
        assertArrayEquals(expected3, iterator.getCounts());
        
        // Test hasNext()
        assertTrue(iterator.hasNext());
        assertEquals(2, (int)iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(3, (int)iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(4, (int)iterator.next());
        assertFalse(iterator.hasNext());
        
        // Test remove()
        try {
            iterator.remove();
            fail("Expected exception not thrown");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
    
    // Add additional test cases for edge cases and exceptions
    
}

