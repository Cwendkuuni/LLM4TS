import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;

public class MultidimensionalCounterTest {
    
    private MultidimensionalCounter counter;
    
    @Before
    public void setUp() {
        int[] size = {2, 3, 4};
        try {
            counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetDimension() {
        assertEquals(3, counter.getDimension());
    }
    
    @Test
    public void testGetCounts_inRange() {
        int[] counts = {1, 2, 3};
        try {
            assertArrayEquals(counts, counter.getCounts(23));
        } catch (OutOfRangeException e) {
            e.printStackTrace();
        }
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_outOfRange() throws OutOfRangeException {
        counter.getCounts(32);
    }
    
    @Test
    public void testGetCount_inRange() {
        int[] counts = {1, 2, 3};
        try {
            assertEquals(23, counter.getCount(counts));
        } catch (OutOfRangeException | DimensionMismatchException e) {
            e.printStackTrace();
        }
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_dimensionMismatch() throws DimensionMismatchException, OutOfRangeException {
        int[] counts = {1, 2, 3, 4};
        counter.getCount(counts);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCount_outOfRange() throws DimensionMismatchException, OutOfRangeException {
        int[] counts = {1, 2, 5};
        counter.getCount(counts);
    }
    
    @Test
    public void testGetSize() {
        assertEquals(24, counter.getSize());
    }
    
    @Test
    public void testGetSizes() {
        int[] expectedSizes = {2, 3, 4};
        assertArrayEquals(expectedSizes, counter.getSizes());
    }
    
    @Test
    public void testIterator_hasNext() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        while (iterator.hasNext()) {
            iterator.next();
        }
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testIterator_next() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        assertEquals(0, iterator.next().intValue());
        assertEquals(1, iterator.next().intValue());
        assertEquals(2, iterator.next().intValue());
        assertEquals(3, iterator.getCount());
        assertArrayEquals(new int[]{0, 1, 0}, iterator.getCounts());
    }
    
    @Test
    public void testIterator_getCount() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        assertEquals(0, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        assertEquals(0, iterator.getCount(2));
        
        iterator.next();
        
        assertEquals(1, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        assertEquals(0, iterator.getCount(2));
    }
}