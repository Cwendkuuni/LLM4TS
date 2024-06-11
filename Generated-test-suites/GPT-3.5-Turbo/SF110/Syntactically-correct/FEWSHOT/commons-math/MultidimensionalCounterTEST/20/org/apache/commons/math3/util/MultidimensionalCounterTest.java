package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Before;
import org.junit.Test;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Before
    public void setUp() throws NotStrictlyPositiveException {
        counter = new MultidimensionalCounter(2, 3, 4);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroSize() throws NotStrictlyPositiveException {
        counter = new MultidimensionalCounter(2, 0);
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSize() {
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        assertArrayEquals(new int[] {2, 3, 4}, counter.getSizes());
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.getCount());
        assertArrayEquals(new int[] {0, 0, 0}, iterator.getCounts());
        assertEquals(0, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        assertEquals(0, iterator.getCount(2));

        iterator.next();
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.getCount());
        assertArrayEquals(new int[] {0, 0, 1}, iterator.getCounts());
        assertEquals(0, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        assertEquals(1, iterator.getCount(2));

        for (int i = 2; i < 24; i++) {
            iterator.next();
        }
        assertFalse(iterator.hasNext());
        assertEquals(23, iterator.getCount());
        assertArrayEquals(new int[] {1, 2, 3}, iterator.getCounts());
        assertEquals(1, iterator.getCount(0));
        assertEquals(2, iterator.getCount(1));
        assertEquals(3, iterator.getCount(2));
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        assertArrayEquals(new int[] {0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[] {0, 0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[] {0, 0, 2}, counter.getCounts(2));
        assertArrayEquals(new int[] {1, 1, 2}, counter.getCounts(17));
        assertArrayEquals(new int[] {1, 2, 3}, counter.getCounts(23));

        try {
            counter.getCounts(-1);
            fail("Exception not thrown");
        } catch (OutOfRangeException e) {
            // Exception thrown as expected
        }

        try {
            counter.getCounts(24);
            fail("Exception not thrown");
        } catch (OutOfRangeException e) {
            // Exception thrown as expected
        }
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(2, counter.getCount(0, 0, 2));
        assertEquals(17, counter.getCount(1, 1, 2));
        assertEquals(23, counter.getCount(1, 2, 3));

        try {
            counter.getCount(1, 2, 4);
            fail("Exception not thrown");
        } catch (OutOfRangeException e) {
            // Exception thrown as expected
        }

        try {
            counter.getCount(2);
            fail("Exception not thrown");
        } catch (DimensionMismatchException e) {
            // Exception thrown as expected
        }
    }

    @Test
    public void testToString() {
        assertEquals("[0][0][0]", counter.toString());
    }
}