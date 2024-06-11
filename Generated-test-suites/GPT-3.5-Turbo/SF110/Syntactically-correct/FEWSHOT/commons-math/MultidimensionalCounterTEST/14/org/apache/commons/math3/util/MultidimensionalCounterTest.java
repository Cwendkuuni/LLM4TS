
package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultidimensionalCounterTest {
    @Test
    public void testConstructor() {
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(-1, 2);
            fail("Constructor should throw NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            assertEquals("Size must be strictly positive: -1", e.getMessage());
        }

        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(2, 0);
            fail("Constructor should throw NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            assertEquals("Size must be strictly positive: 0", e.getMessage());
        }

        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertArrayEquals(new int[]{2, 3}, counter.getSizes());
        assertEquals(6, counter.getSize());
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(0, (int) iterator.next());
        assertArrayEquals(new int[]{0, 0}, iterator.getCounts());

        assertTrue(iterator.hasNext());
        assertEquals(1, (int) iterator.next());
        assertArrayEquals(new int[]{0, 1}, iterator.getCounts());

        assertTrue(iterator.hasNext());
        assertEquals(2, (int) iterator.next());
        assertArrayEquals(new int[]{0, 2}, iterator.getCounts());

        assertTrue(iterator.hasNext());
        assertEquals(3, (int) iterator.next());
        assertArrayEquals(new int[]{1, 0}, iterator.getCounts());

        assertTrue(iterator.hasNext());
        assertEquals(4, (int) iterator.next());
        assertArrayEquals(new int[]{1, 1}, iterator.getCounts());

        assertTrue(iterator.hasNext());
        assertEquals(5, (int) iterator.next());
        assertArrayEquals(new int[]{1, 2}, iterator.getCounts());

        assertFalse(iterator.hasNext());
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

        try {
            counter.getCounts(-1);
            fail("getCounts should throw OutOfRangeException");
        } catch (OutOfRangeException e) {
            assertEquals("Index -1 out of allowed range [0, 5]", e.getMessage());
        }

        try {
            counter.getCounts(6);
            fail("getCounts should throw OutOfRangeException");
        } catch (OutOfRangeException e) {
            assertEquals("Index 6 out of allowed range [0, 5]", e.getMessage());
        }
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

        try {
            counter.getCount(2, 0);
            fail("getCount should throw DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            assertEquals("4, org.apache.commons.math3.exception.DimensionMismatchException: " +
                    "dimension mismatch: 2 != 1", e.toString());
        }

        try {
            counter.getCount(0, 3);
            fail("getCount should throw OutOfRangeException");
        } catch (OutOfRangeException e) {
            assertEquals("Index 3 out of allowed range [0, 2]", e.getMessage());
        }
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertEquals("[0][0]", counter.toString());
    }
}
