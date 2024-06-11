package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(new int[] {2, 3, 4}, counter.getSizes());
        assertEquals(24, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_ZeroSize() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(0, 3, 4);
    }

    @Test
    public void testGetCounts() throws NotStrictlyPositiveException, OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        assertArrayEquals(new int[] {0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[] {0, 0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[] {0, 1, 2}, counter.getCounts(3));
        assertArrayEquals(new int[] {1, 2, 3}, counter.getCounts(23));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_InvalidIndex() throws NotStrictlyPositiveException, OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCounts(24);
    }

    @Test
    public void testGetCount() throws NotStrictlyPositiveException, OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(3, counter.getCount(0, 1, 2));
        assertEquals(23, counter.getCount(1, 2, 3));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_InvalidIndex() throws NotStrictlyPositiveException, OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(2, 2, 3);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_DimensionMismatch() throws NotStrictlyPositiveException, OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(1, 2);
    }

    @Test
    public void testToString() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals("[0][0][0]", counter.toString());
    }

    @Test
    public void testIteratorHasNext() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        for (int i = 0; i < 23; i++) {
            iterator.next();
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        assertEquals(0, (int) iterator.next());
        assertEquals(1, (int) iterator.next());
        iterator.next();
        iterator.next();
        assertEquals(4, (int) iterator.next());
    }

    @Test
    public void testIteratorGetCount() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        assertEquals(-1, iterator.getCount());
        iterator.next();
        assertEquals(0, iterator.getCount());
        for (int i = 0; i < 23; i++) {
            iterator.next();
        }
        assertEquals(23, iterator.getCount());
    }

    @Test
    public void testIteratorGetCounts() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        assertArrayEquals(new int[] {0, 0, 0}, iterator.getCounts());
        iterator.next();
        assertArrayEquals(new int[] {0, 0, 1}, iterator.getCounts());
        for (int i = 0; i < 23; i++) {
            iterator.next();
        }
        assertArrayEquals(new int[] {1, 2, 3}, iterator.getCounts());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        iterator.remove();
    }

}