
package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(new int[]{2, 3, 4}, counter.getSizes());
        assertEquals(24, counter.getSize());

        counter = new MultidimensionalCounter(5);
        assertEquals(1, counter.getDimension());
        assertArrayEquals(new int[]{5}, counter.getSizes());
        assertEquals(5, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_withZeroSize() throws NotStrictlyPositiveException {
        new MultidimensionalCounter(0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_withNegativeSize() throws NotStrictlyPositiveException {
        new MultidimensionalCounter(-1);
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
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
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_withOutOfRangeIndex() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCounts(12);
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(1, 0, 0));
        assertEquals(2, counter.getCount(0, 1, 0));
        assertEquals(3, counter.getCount(1, 1, 0));
        assertEquals(4, counter.getCount(0, 2, 0));
        assertEquals(5, counter.getCount(1, 2, 0));
        assertEquals(6, counter.getCount(0, 0, 1));
        assertEquals(7, counter.getCount(1, 0, 1));
        assertEquals(8, counter.getCount(0, 1, 1));
        assertEquals(9, counter.getCount(1, 1, 1));
        assertEquals(10, counter.getCount(0, 2, 1));
        assertEquals(11, counter.getCount(1, 2, 1));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_withOutOfRangeIndex() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(2, 1, 1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_withDimensionMismatch() throws DimensionMismatchException, OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(1, 2);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);

        java.util.Iterator<Integer> iterator = counter.iterator();
        assertEquals(0, iterator.next().intValue());
        assertEquals(1, iterator.next().intValue());
        assertEquals(2, iterator.next().intValue());
        assertEquals(3, iterator.next().intValue());
        assertEquals(4, iterator.next().intValue());
        assertEquals(5, iterator.next().intValue());

        assertEquals(false, iterator.hasNext());
    }

    @Test
    public void testIterator_withSingleDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(4);

        java.util.Iterator<Integer> iterator = counter.iterator();
        assertEquals(0, iterator.next().intValue());
        assertEquals(1, iterator.next().intValue());
        assertEquals(2, iterator.next().intValue());
        assertEquals(3, iterator.next().intValue());

        assertEquals(false, iterator.hasNext());
    }

    @Test
    public void testToString() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals("[0][0][0]", counter.toString());
        assertEquals("[0][0][1]", counter.getCounts(1).toString());
        assertEquals("[0][1][0]", counter.getCounts(2).toString());
        assertEquals("[0][1][1]", counter.getCounts(3).toString());
    }
}
