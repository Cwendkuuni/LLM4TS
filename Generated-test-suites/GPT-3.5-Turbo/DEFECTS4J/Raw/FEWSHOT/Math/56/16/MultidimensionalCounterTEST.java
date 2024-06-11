
package org.apache.commons.math.util;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        // check initial state
        assertTrue(iterator.hasNext());
        assertEquals(-1, iterator.getCount());
        assertArrayEquals(new int[]{0, 0, 0}, iterator.getCounts());

        // check first iteration
        assertEquals(0, (int) iterator.next());
        assertEquals(0, iterator.getCount());
        assertArrayEquals(new int[]{0, 0, 0}, iterator.getCounts());

        // check second iteration
        assertEquals(1, (int) iterator.next());
        assertEquals(1, iterator.getCount());
        assertArrayEquals(new int[]{0, 0, 1}, iterator.getCounts());

        // check third to last iteration
        for (int i = 2; i < 12; i++) {
            assertTrue(iterator.hasNext());
            assertEquals(i, (int) iterator.next());
        }

        // check last iteration
        assertTrue(iterator.hasNext());
        assertEquals(23, (int) iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4);
        assertArrayEquals(new int[]{0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[]{0, 2}, counter.getCounts(2));
        assertArrayEquals(new int[]{0, 3}, counter.getCounts(3));
        assertArrayEquals(new int[]{1, 0}, counter.getCounts(4));
        assertArrayEquals(new int[]{1, 1}, counter.getCounts(5));
        assertArrayEquals(new int[]{1, 2}, counter.getCounts(6));
        assertArrayEquals(new int[]{1, 3}, counter.getCounts(7));
        assertArrayEquals(new int[]{2, 0}, counter.getCounts(8));
        assertArrayEquals(new int[]{2, 1}, counter.getCounts(9));
        assertArrayEquals(new int[]{2, 2}, counter.getCounts(10));
        assertArrayEquals(new int[]{2, 3}, counter.getCounts(11));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4);
        counter.getCounts(12);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4);
        assertEquals(0, counter.getCount(0, 0));
        assertEquals(1, counter.getCount(0, 1));
        assertEquals(2, counter.getCount(0, 2));
        assertEquals(3, counter.getCount(0, 3));
        assertEquals(4, counter.getCount(1, 0));
        assertEquals(5, counter.getCount(1, 1));
        assertEquals(6, counter.getCount(1, 2));
        assertEquals(7, counter.getCount(1, 3));
        assertEquals(8, counter.getCount(2, 0));
        assertEquals(9, counter.getCount(2, 1));
        assertEquals(10, counter.getCount(2, 2));
        assertEquals(11, counter.getCount(2, 3));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4);
        counter.getCount(3, 0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4);
        counter.getCount(0, 0, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4);
        counter.getCount(-1, 0);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertArrayEquals(new int[]{2, 3, 4}, counter.getSizes());
    }
}
