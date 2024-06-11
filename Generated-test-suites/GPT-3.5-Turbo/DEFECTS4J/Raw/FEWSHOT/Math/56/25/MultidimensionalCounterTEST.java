package org.apache.commons.math.util;

import static org.junit.Assert.*;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.util.MultidimensionalCounter;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor_invalidSize() {
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(0, 1, 2);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(-1, 2, 3);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertArrayEquals(new int[] {0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[] {0, 0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[] {0, 0, 2}, counter.getCounts(2));
        assertArrayEquals(new int[] {0, 1, 0}, counter.getCounts(3));
        assertArrayEquals(new int[] {1, 0, 0}, counter.getCounts(12));
        assertArrayEquals(new int[] {1, 3, 2}, counter.getCounts(23));
    }

    @Test
    public void testGetCounts_invalidIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        try {
            counter.getCounts(-1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // expected
        }
        try {
            counter.getCounts(24);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // expected
        }
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

    @Test
    public void testGetCount_invalidIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        try {
            counter.getCount(-1, 0, 0);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // expected
        }
        try {
            counter.getCount(2, 4, 3);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // expected
        }
    }

    @Test
    public void testGetCount_invalidSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        try {
            counter.getCount(0, 0, 0, 0);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // expected
        }
        try {
            counter.getCount(0, 1);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // expected
        }
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, (int) iterator.next());
        assertEquals(1, (int) iterator.next());
        assertEquals(2, (int) iterator.next());
        assertEquals(3, (int) iterator.next());
        assertEquals(4, (int) iterator.next());
        assertEquals(5, (int) iterator.next());
        assertEquals(6, (int) iterator.next());
        assertEquals(7, (int) iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_remove() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals("[0][0][0]", counter.toString());
        counter.getCount(1, 0, 2);
        assertEquals("[1][0][2]", counter.toString());
    }

}