
import org.apache.commons.math3.exception.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {
    
    @Test
    public void testIterator() {
        MultidimensionalCounter mc = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = mc.iterator();
        
        // Test hasNext()
        assertTrue(iterator.hasNext());
        
        // Test next()
        assertEquals(Integer.valueOf(0), iterator.next());
        assertEquals(Integer.valueOf(1), iterator.next());
        assertEquals(Integer.valueOf(2), iterator.next());
        assertEquals(Integer.valueOf(3), iterator.next());
        assertEquals(Integer.valueOf(4), iterator.next());
        assertEquals(Integer.valueOf(5), iterator.next());
        
        // Test getCount()
        assertEquals(6, iterator.getCount());
        
        // Test getCounts()
        int[] counts = iterator.getCounts();
        assertEquals(0, counts[0]);
        assertEquals(2, counts[1]);
        
        // Test getCount(int dim)
        assertEquals(0, iterator.getCount(0));
        assertEquals(2, iterator.getCount(1));
        
        // Test remove()
        try {
            iterator.remove();
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testGetDimension() {
        MultidimensionalCounter mc = new MultidimensionalCounter(2, 3);
        assertEquals(2, mc.getDimension());
    }
    
    @Test
    public void testGetCounts() throws OutOfRangeException {
        MultidimensionalCounter mc = new MultidimensionalCounter(2, 3);
        
        // Test valid index
        int[] counts = mc.getCounts(3);
        assertEquals(1, counts[0]);
        assertEquals(0, counts[1]);
        
        // Test invalid index
        try {
            mc.getCounts(7);
            fail("Expected OutOfRangeException to be thrown");
        } catch (OutOfRangeException e) {
            // Expected
        }
    }
    
    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter mc = new MultidimensionalCounter(2, 3);
        
        // Test valid count
        int count = mc.getCount(1, 2);
        assertEquals(5, count);
        
        // Test invalid count
        try {
            mc.getCount(3, 2);
            fail("Expected OutOfRangeException to be thrown");
        } catch (OutOfRangeException e) {
            // Expected
        }
        
        // Test invalid dimension
        try {
            mc.getCount(1, 2, 3);
            fail("Expected DimensionMismatchException to be thrown");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }
    
    @Test
    public void testGetSize() {
        MultidimensionalCounter mc = new MultidimensionalCounter(2, 3);
        assertEquals(6, mc.getSize());
    }
    
    @Test
    public void testGetSizes() {
        MultidimensionalCounter mc = new MultidimensionalCounter(2, 3);
        int[] sizes = mc.getSizes();
        assertEquals(2, sizes[0]);
        assertEquals(3, sizes[1]);
    }
    
    @Test
    public void testToString() {
        MultidimensionalCounter mc = new MultidimensionalCounter(2, 3);
        assertEquals("[0][0]", mc.toString());
    }
}
