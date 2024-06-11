
package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Before;
import org.junit.Test;

public class MultidimensionalCounterTest {
    private MultidimensionalCounter counter;

    @Before
    public void setup() throws NotStrictlyPositiveException {
        int[] size = {3, 4};
        counter = new MultidimensionalCounter(size);
    }

    @Test
    public void testGetDimension() {
        assertEquals(2, counter.getDimension());
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        assertArrayEquals(new int[]{0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{1, 0}, counter.getCounts(1));
        assertArrayEquals(new int[]{2, 0}, counter.getCounts(2));
        assertArrayEquals(new int[]{0, 1}, counter.getCounts(3));
        assertArrayEquals(new int[]{1, 1}, counter.getCounts(4));
        assertArrayEquals(new int[]{2, 1}, counter.getCounts(5));
        assertArrayEquals(new int[]{0, 2}, counter.getCounts(6));
        assertArrayEquals(new int[]{1, 2}, counter.getCounts(7));
        assertArrayEquals(new int[]{2, 2}, counter.getCounts(8));
        assertArrayEquals(new int[]{0, 3}, counter.getCounts(9));
        assertArrayEquals(new int[]{1, 3}, counter.getCounts(10));
        assertArrayEquals(new int[]{2, 3}, counter.getCounts(11));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() throws OutOfRangeException {
        counter.getCounts(12);
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        assertEquals(0, counter.getCount(0, 0));
        assertEquals(1, counter.getCount(1, 0));
        assertEquals(2, counter.getCount(2, 0));
        assertEquals(3, counter.getCount(0, 1));
        assertEquals(4, counter.getCount(1, 1));
        assertEquals(5, counter.getCount(2, 1));
        assertEquals(6, counter.getCount(0, 2));
        assertEquals(7, counter.getCount(1, 2));
        assertEquals(8, counter.getCount(2, 2));
        assertEquals(9, counter.getCount(0, 3));
        assertEquals(10, counter.getCount(1, 3));
        assertEquals(11, counter.getCount(2, 3));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() throws DimensionMismatchException, OutOfRangeException {
        counter.getCount(0, 0, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() throws OutOfRangeException, DimensionMismatchException {
        counter.getCount(0, 0, 4);
    }

    @Test
    public void testGetSize() {
        assertEquals(12, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        assertArrayEquals(new int[]{3, 4}, counter.getSizes());
    }

    @Test
    public void testToString() {
        assertEquals("[0][0]", counter.toString());
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(1), iterator.next());
        assertEquals(1, iterator.getCount());
        assertArrayEquals(new int[]{1, 0}, iterator.getCounts());
        assertEquals(1, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(2), iterator.next());
        assertEquals(2, iterator.getCount());
        assertArrayEquals(new int[]{2, 0}, iterator.getCounts());
        assertEquals(2, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));

        for (int i = 0; i < 10; i++) {
            iterator.next();
        }

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(13), iterator.next());
        assertEquals(13, iterator.getCount());
        assertArrayEquals(new int[]{1, 3}, iterator.getCounts());
        assertEquals(1, iterator.getCount(0));
        assertEquals(3, iterator.getCount(1));

        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }
}


