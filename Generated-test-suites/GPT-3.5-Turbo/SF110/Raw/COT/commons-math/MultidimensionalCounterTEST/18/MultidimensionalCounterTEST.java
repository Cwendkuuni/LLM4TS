import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultidimensionalCounterTest {
    private MultidimensionalCounter counter;

    @Before
    public void setUp() {
        try {
            counter = new MultidimensionalCounter(2, 3, 4);
        } catch (NotStrictlyPositiveException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSizes() {
        assertArrayEquals(new int[]{2, 3, 4}, counter.getSizes());
    }

    @Test
    public void testGetSize() {
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetCounts() {
        assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{1, 0, 0}, counter.getCounts(1));
        assertArrayEquals(new int[]{0, 1, 0}, counter.getCounts(2));
        assertArrayEquals(new int[]{1, 1, 0}, counter.getCounts(3));
        assertArrayEquals(new int[]{0, 2, 0}, counter.getCounts(4));
        assertArrayEquals(new int[]{1, 2, 0}, counter.getCounts(5));
        assertArrayEquals(new int[]{0, 0, 1}, counter.getCounts(6));
        assertArrayEquals(new int[]{1, 0, 1}, counter.getCounts(7));
        assertArrayEquals(new int[]{0, 1, 1}, counter.getCounts(8));
        assertArrayEquals(new int[]{1, 1, 1}, counter.getCounts(9));
        assertArrayEquals(new int[]{0, 2, 1}, counter.getCounts(10));
        assertArrayEquals(new int[]{1, 2, 1}, counter.getCounts(11));
        assertArrayEquals(new int[]{0, 0, 2}, counter.getCounts(12));
        assertArrayEquals(new int[]{1, 0, 2}, counter.getCounts(13));
        assertArrayEquals(new int[]{0, 1, 2}, counter.getCounts(14));
        assertArrayEquals(new int[]{1, 1, 2}, counter.getCounts(15));
        assertArrayEquals(new int[]{0, 2, 2}, counter.getCounts(16));
        assertArrayEquals(new int[]{1, 2, 2}, counter.getCounts(17));
        assertArrayEquals(new int[]{0, 0, 3}, counter.getCounts(18));
        assertArrayEquals(new int[]{1, 0, 3}, counter.getCounts(19));
        assertArrayEquals(new int[]{0, 1, 3}, counter.getCounts(20));
        assertArrayEquals(new int[]{1, 1, 3}, counter.getCounts(21));
        assertArrayEquals(new int[]{0, 2, 3}, counter.getCounts(22));
        assertArrayEquals(new int[]{1, 2, 3}, counter.getCounts(23));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() throws OutOfRangeException {
        counter.getCounts(24);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsNegative() throws OutOfRangeException {
        counter.getCounts(-1);
    }

    @Test
    public void testGetCount() {
        try {
            assertEquals(0, counter.getCount(0, 0, 0));
            assertEquals(1, counter.getCount(1, 0, 0));
            assertEquals(4, counter.getCount(0, 1, 0));
            assertEquals(5, counter.getCount(1, 1, 0));
            assertEquals(8, counter.getCount(0, 2, 0));
            assertEquals(9, counter.getCount(1, 2, 0));
            assertEquals(12, counter.getCount(0, 0, 1));
            assertEquals(13, counter.getCount(1, 0, 1));
            assertEquals(16, counter.getCount(0, 1, 1));
            assertEquals(17, counter.getCount(1, 1, 1));
            assertEquals(20, counter.getCount(0, 2, 1));
            assertEquals(21, counter.getCount(1, 2, 1));
            assertEquals(24, counter.getCount(0, 0, 2));
            assertEquals(25, counter.getCount(1, 0, 2));
            assertEquals(28, counter.getCount(0, 1, 2));
            assertEquals(29, counter.getCount(1, 1, 2));
            assertEquals(32, counter.getCount(0, 2, 2));
            assertEquals(33, counter.getCount(1, 2, 2));
            assertEquals(36, counter.getCount(0, 0, 3));
            assertEquals(37, counter.getCount(1, 0, 3));
            assertEquals(40, counter.getCount(0, 1, 3));
            assertEquals(41, counter.getCount(1, 1, 3));
            assertEquals(44, counter.getCount(0, 2, 3));
            assertEquals(45, counter.getCount(1, 2, 3));
        } catch (OutOfRangeException | DimensionMismatchException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() throws DimensionMismatchException, OutOfRangeException {
        counter.getCount(0, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() throws DimensionMismatchException, OutOfRangeException {
        counter.getCount(0, 0, 4);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountNegative() throws DimensionMismatchException, OutOfRangeException {
        counter.getCount(0, 0, -1);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, (int) iterator.next());
        assertEquals(1, iterator.getCount());
        assertArrayEquals(new int[]{0, 0, 0}, iterator.getCounts());
        assertEquals(1, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        assertEquals(0, iterator.getCount(2));
        assertTrue(iterator.hasNext());
        assertEquals(1, (int) iterator.next());
        assertEquals(2, iterator.getCount());
        assertArrayEquals(new int[]{1, 0, 0}, iterator.getCounts());
        assertEquals(1, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        assertEquals(0, iterator.getCount(2));
        assertTrue(iterator.hasNext());
        //...
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }
}



// Add additional test cases for potential edge cases and exception handling scenarios here

