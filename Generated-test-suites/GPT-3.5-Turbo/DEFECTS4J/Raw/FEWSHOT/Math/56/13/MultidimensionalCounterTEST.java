package org.apache.commons.math.util;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next().intValue());
        assertEquals(1, iterator.next().intValue());
        assertEquals(2, iterator.next().intValue());
        assertEquals(3, iterator.next().intValue());
        assertEquals(4, iterator.next().intValue());
        assertEquals(5, iterator.next().intValue());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertArrayEquals(new int[] {0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[] {1, 0, 0}, counter.getCounts(1));
        assertArrayEquals(new int[] {1, 2, 3}, counter.getCounts(23));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCounts(24);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(1, 0, 0));
        assertEquals(23, counter.getCount(1, 2, 3));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(2, 2, 3);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatchException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(1, 2);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertArrayEquals(new int[] {2, 3, 4}, counter.getSizes());
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals("[0][0][0]", counter.toString());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException() {
        new MultidimensionalCounter(0, -1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCounts(25);
    }
}