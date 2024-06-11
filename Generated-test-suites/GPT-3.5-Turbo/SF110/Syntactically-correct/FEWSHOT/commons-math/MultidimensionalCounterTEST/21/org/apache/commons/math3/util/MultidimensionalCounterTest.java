package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        try {
            MultidimensionalCounter counter1 = new MultidimensionalCounter(-1, 2, 3);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Exception expected
        }

        MultidimensionalCounter counter2 = new MultidimensionalCounter(2);
        assertEquals(1, counter2.getDimension());
        assertArrayEquals(new int[]{2}, counter2.getSizes());

        MultidimensionalCounter counter3 = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter3.getDimension());
        assertArrayEquals(new int[]{2, 3, 4}, counter3.getSizes());
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);

        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, (int) iterator.next());
        assertEquals(1, (int) iterator.next());
        assertEquals(2, (int) iterator.next());
        assertEquals(3, (int) iterator.next());
        assertEquals(4, (int) iterator.next());
        assertEquals(5, (int) iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetCounts() {
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

    @Test
    public void testGetCount() {
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
        assertEquals(12, counter.getCount(0, 0, 2));
        assertEquals(13, counter.getCount(1, 0, 2));
        assertEquals(14, counter.getCount(0, 1, 2));
        assertEquals(15, counter.getCount(1, 1, 2));
        assertEquals(16, counter.getCount(0, 2, 2));
        assertEquals(17, counter.getCount(1, 2, 2));
        assertEquals(18, counter.getCount(0, 0, 3));
        assertEquals(19, counter.getCount(1, 0, 3));
        assertEquals(20, counter.getCount(0, 1, 3));
        assertEquals(21, counter.getCount(1, 1, 3));
        assertEquals(22, counter.getCount(0, 2, 3));
        assertEquals(23, counter.getCount(1, 2, 3));

        try {
            counter.getCount(0);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Exception expected
        }

        try {
            counter.getCount(0, 0);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Exception expected
        }

        try {
            counter.getCount(0, 0, -1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Exception expected
        }

        try {
            counter.getCount(0, 0, 4);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Exception expected
        }
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals("[0][0][0]", counter.toString());

        MultidimensionalCounter.Iterator iterator = counter.iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        assertEquals("[1][2][3]", counter.toString());
    }
}