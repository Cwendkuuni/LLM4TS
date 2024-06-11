package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidSize() {
        new MultidimensionalCounter(-1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidSizes() {
        new MultidimensionalCounter(-1, 2, 3);
    }

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3);
        assertEquals(1, counter.getDimension());
        counter = new MultidimensionalCounter(2, 3);
        assertEquals(2, counter.getDimension());
        counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3);
        assertArrayEquals(new int[]{3}, counter.getSizes());
        counter = new MultidimensionalCounter(2, 3);
        assertArrayEquals(new int[]{2, 3}, counter.getSizes());
        counter = new MultidimensionalCounter(2, 3, 4);
        assertArrayEquals(new int[]{2, 3, 4}, counter.getSizes());
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3);
        assertEquals("[0][1][2]", counter.toString());
        counter = new MultidimensionalCounter(2, 3);
        assertEquals("[0][0][0][1][1][1]", counter.toString());
        counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals("[0][0][0][0][1][1][1][2][2][2]", counter.toString());
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(0, (int) iterator.next());
        assertEquals(1, (int) iterator.next());
        assertEquals(2, (int) iterator.next());
        assertEquals(3, (int) iterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertEquals(0, counter.getCount(0, 0));
        assertEquals(1, counter.getCount(0, 1));
        assertEquals(2, counter.getCount(0, 2));
        assertEquals(3, counter.getCount(1, 0));
        assertEquals(4, counter.getCount(1, 1));
        assertEquals(5, counter.getCount(1, 2));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithInvalidIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.getCount(2, 0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWithInvalidDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.getCount(0);
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertArrayEquals(new int[]{0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[]{0, 2}, counter.getCounts(2));
        assertArrayEquals(new int[]{1, 0}, counter.getCounts(3));
        assertArrayEquals(new int[]{1, 1}, counter.getCounts(4));
        assertArrayEquals(new int[]{1, 2}, counter.getCounts(5));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsWithInvalidIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.getCounts(6);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3);
        assertEquals(3, counter.getSize());
        counter = new MultidimensionalCounter(2, 3);
        assertEquals(6, counter.getSize());
        counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(24, counter.getSize());
    }
}