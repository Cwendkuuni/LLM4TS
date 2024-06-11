
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {
    
    @Test
    public void testConstructor() throws NotStrictlyPositiveException {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertNotNull(counter);
        assertEquals(2, counter.getDimension());
        assertArrayEquals(size, counter.getSizes());
        assertEquals(6, counter.getSize());
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNotStrictlyPositiveSize() throws NotStrictlyPositiveException {
        int[] size = {2, -3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
    }
    
    @Test
    public void testGetCounts() throws OutOfRangeException, NotStrictlyPositiveException {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        assertArrayEquals(new int[]{0,0}, counter.getCounts(0));
        assertArrayEquals(new int[]{0,1}, counter.getCounts(1));
        assertArrayEquals(new int[]{1,0}, counter.getCounts(2));
        assertArrayEquals(new int[]{1,2}, counter.getCounts(5));
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountsWithOutOfRangeIndex() throws OutOfRangeException, NotStrictlyPositiveException {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] counts = counter.getCounts(6);
    }
    
    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException, NotStrictlyPositiveException {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        assertEquals(0, counter.getCount(0, 0));
        assertEquals(1, counter.getCount(0, 1));
        assertEquals(2, counter.getCount(1, 0));
        assertEquals(5, counter.getCount(1, 2));
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWithInvalidDimensionCount() throws DimensionMismatchException, OutOfRangeException, NotStrictlyPositiveException {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int count = counter.getCount(0);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithOutOfRangeIndex() throws OutOfRangeException, DimensionMismatchException, NotStrictlyPositiveException {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int count = counter.getCount(1, 3);
    }
    
    @Test
    public void testIterator() throws NotStrictlyPositiveException {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next().intValue());
        assertEquals(1, iterator.next().intValue());
        assertEquals(2, iterator.next().intValue());
        assertEquals(3, iterator.next().intValue());
        assertEquals(4, iterator.next().intValue());
        assertEquals(5, iterator.next().intValue());
        assertFalse(iterator.hasNext());
    }
    
    // TODO: Add more test cases for edge cases and exceptions
    
}
