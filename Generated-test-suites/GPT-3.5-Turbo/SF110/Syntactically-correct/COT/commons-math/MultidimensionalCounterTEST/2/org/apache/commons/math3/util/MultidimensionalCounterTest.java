
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter multidimensionalCounter;

    @Before
    public void setUp() throws NotStrictlyPositiveException {
        multidimensionalCounter = new MultidimensionalCounter(2, 3, 4);
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, multidimensionalCounter.getDimension());
    }

    @Test
    public void testGetSize() {
        assertEquals(24, multidimensionalCounter.getSize());
    }

    @Test
    public void testGetSizes() {
        int[] expectedSizes = {2, 3, 4};
        assertArrayEquals(expectedSizes, multidimensionalCounter.getSizes());
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        int[] expectedCounts = {0, 0, 0};
        assertArrayEquals(expectedCounts, multidimensionalCounter.getCounts(0));
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        int[] counts = {1, 2, 3};
        assertEquals(47, multidimensionalCounter.getCount(counts));
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals((Integer) 0, iterator.next());
        
        assertTrue(iterator.hasNext());
        assertEquals((Integer) 1, iterator.next());
        
        assertTrue(iterator.hasNext());
        assertEquals((Integer) 2, iterator.next());
        
        // ... Continue testing until the end
        
        assertFalse(iterator.hasNext());
    }
}
