import org.junit.Test;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.util.MathUtils;

import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor_validSizes() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
        int[] expectedSizes = {2, 4, 3};
        assertArrayEquals(expectedSizes, counter.getSizes());
        assertEquals(24, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_zeroSize() {
        int[] sizes = {2, 0, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_negativeSize() {
        int[] sizes = {2, -3, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
    }

    @Test
    public void testIterator() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(3, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(4, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(5, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(6, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(7, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(8, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(9, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(10, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(11, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(12, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(13, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(14, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(15, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(16, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(17, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(18, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(19, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(20, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(21, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(22, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(23, iterator.next().intValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetCounts_validIndex() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] counts = counter.getCounts(17);
        int[] expectedCounts = {1, 0, 1};
        assertArrayEquals(expectedCounts, counts);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_invalidIndex() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] counts = counter.getCounts(30);
    }

    @Test
    public void testGetCount_validIndex() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int count = counter.getCount(1, 0, 1);
        assertEquals(17, count);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_dimensionMismatch() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int count = counter.getCount(1, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_outOfRange() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int count = counter.getCount(1, 0, 4);
    }
}