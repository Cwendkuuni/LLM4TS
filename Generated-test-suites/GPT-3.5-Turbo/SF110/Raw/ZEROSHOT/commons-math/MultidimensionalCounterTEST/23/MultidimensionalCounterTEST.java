import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.*;
import java.util.*;

public class MultidimensionalCounterTest {
    
    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter.getDimension());
    }
    
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        try {
            int[] counts = counter.getCounts(0);
            assertEquals(0, counts[0]);
            assertEquals(0, counts[1]);
            
            counts = counter.getCounts(1);
            assertEquals(1, counts[0]);
            assertEquals(0, counts[1]);
            
            counts = counter.getCounts(2);
            assertEquals(0, counts[0]);
            assertEquals(1, counts[1]);
            
            counts = counter.getCounts(3);
            assertEquals(1, counts[0]);
            assertEquals(1, counts[1]);
            
        } catch (OutOfRangeException e) {
            fail("Unexpected OutOfRangeException");
        }
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountsException() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        counter.getCounts(4);
    }
    
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        try {
            int count = counter.getCount(0, 0, 0);
            assertEquals(0, count);
            
            count = counter.getCount(1, 1, 1);
            assertEquals(19, count);
            
            count = counter.getCount(0, 1, 2);
            assertEquals(14, count);
            
            count = counter.getCount(1, 2, 3);
            assertEquals(35, count);
            
        } catch (OutOfRangeException | DimensionMismatchException e) {
            fail("Unexpected exception");
        }
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeException() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(2, 1, 1);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatchException() throws DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(2, 1);
    }
    
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2, 2);
        assertEquals(16, counter.getSize());
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
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        String expected = "[0][0][0]";
        assertEquals(expected, counter.toString());
    }
    
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.getCount().intValue());
        int[] counts = iterator.getCounts();
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        
        assertEquals(1, iterator.next().intValue());
        assertEquals(1, iterator.getCount().intValue());
        counts = iterator.getCounts();
        assertEquals(1, counts[0]);
        assertEquals(0, counts[1]);
        
        assertEquals(2, iterator.next().intValue());
        assertEquals(2, iterator.getCount().intValue());
        counts = iterator.getCounts();
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        
        assertEquals(3, iterator.next().intValue());
        assertEquals(3, iterator.getCount().intValue());
        counts = iterator.getCounts();
        assertEquals(1, counts[0]);
        assertEquals(1, counts[1]);
        
        assertFalse(iterator.hasNext());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }
}