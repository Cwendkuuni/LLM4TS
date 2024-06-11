package org.apache.commons.math.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{0, 0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[]{0, 0, 2}, counter.getCounts(2));
        assertArrayEquals(new int[]{0, 1, 0}, counter.getCounts(3));
        assertArrayEquals(new int[]{1, 0, 0}, counter.getCounts(12));
        assertArrayEquals(new int[]{1, 3, 2}, counter.getCounts(23));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCounts(24);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(2, counter.getCount(0, 0, 2));
        assertEquals(3, counter.getCount(0, 1, 0));
        assertEquals(12, counter.getCount(1, 0, 0));
        assertEquals(23, counter.getCount(1, 3, 2));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(1, 4);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(2, 3, 4);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter1 = new MultidimensionalCounter(2, 4, 3);
        assertEquals(24, counter1.getSize());

        MultidimensionalCounter counter2 = new MultidimensionalCounter(3, 5, 2);
        assertEquals(30, counter2.getSize());

        MultidimensionalCounter counter3 = new MultidimensionalCounter(1, 1, 1, 1);
        assertEquals(1, counter3.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter1 = new MultidimensionalCounter(2, 4, 3);
        assertArrayEquals(new int[]{2, 4, 3}, counter1.getSizes());

        MultidimensionalCounter counter2 = new MultidimensionalCounter(3, 5, 2);
        assertArrayEquals(new int[]{3, 5, 2}, counter2.getSizes());

        MultidimensionalCounter counter3 = new MultidimensionalCounter(1, 1, 1, 1);
        assertArrayEquals(new int[]{1, 1, 1, 1}, counter3.getSizes());
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals("[0][0][0]", counter.toString());

        counter.getCount(1, 3, 2);
        assertEquals("[1][3][2]", counter.toString());
    }
}