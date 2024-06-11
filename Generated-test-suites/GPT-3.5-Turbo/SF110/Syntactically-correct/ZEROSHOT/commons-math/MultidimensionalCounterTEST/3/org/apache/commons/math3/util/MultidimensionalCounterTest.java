import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;

import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        int[] expectedCounts = {0, 0, 0};
        assertArrayEquals(expectedCounts, counter.getCounts(0));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsThrowsException() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        counter.getCounts(60);
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        int[] c = {1, 2, 3};
        assertEquals(31, counter.getCount(c));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountThrowsException() throws OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        int[] c = {1, 2};
        counter.getCount(c);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountThrowsExceptionOutOfRange() throws OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        int[] c = {1, 5, 3};
        counter.getCount(c);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        assertEquals(60, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        int[] expectedSizes = {3, 4, 5};
        assertArrayEquals(expectedSizes, counter.getSizes());
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        assertEquals("[0][0][0]", counter.toString());
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(1, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(2, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(3, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(4, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(5, (int) iterator.next());
        assertFalse(iterator.hasNext());
    }
}