package org.apache.commons.math.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Iterator;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;

public class MultidimensionalCounterTest {
    @Test
    public void testMultidimensionalCounterConstructor() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertEquals(3, mdc.getDimension());
        assertEquals(sizes, mdc.getSizes());
        assertEquals(24 * 3, mdc.getSize());
    }

    @Test
    public void testMultidimensionalCounterIteratorHasNext() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator<Integer> it = mdc.iterator();
        for (int i = 0; i < 24; i++) {
            assertTrue(it.hasNext());
        }
        assertFalse(it.hasNext());
    }

    @Test
    public void testMultidimensionalCounterIteratorNext() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator<Integer> it = mdc.iterator();
        for (int i = 0; i < 24; i++) {
            Integer next = it.next();
            assertEquals(i, next);
        }
        assertNull(it.next());
    }

    @Test
    public void testMultidimensionalCounterIteratorGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator<Integer> it = mdc.iterator();
        for (int i = 0; i < 24; i++) {
            assertEquals(i, it.getCount());
            assertEquals(new int[]{ i / 3, i % 3, i % 4 }, it.getCounts());
        }
        assertNull(it.next());
    }

    @Test
    public void testMultidimensionalCounterGetCounts() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        int[] counts = mdc.getCounts(0);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(0, counts[2]);

        counts = mdc.getCounts(1);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(1, counts[2]);

        counts = mdc.getCounts(23);
        assertEquals(1, counts[0]);
        assertEquals(3, counts[1]);
        assertEquals(2, counts[2]);
    }

    @Test
    public void testMultidimensionalCounterGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertEquals(0, mdc.getCount(new int[]{0, 0, 0}));
        assertEquals(1, mdc.getCount(new int[]{0, 0, 1}));
        assertEquals(2, mdc.getCount(new int[]{0, 0, 2}));
        assertEquals(3, mdc.getCount(new int[]{0, 1, 0}));
        assertEquals(4, mdc.getCount(new int[]{1, 0, 0}));
        assertEquals(24, mdc.getCount(new int[]{1, 3, 2}));

        int[] invalidCounts = new int[]{5, 5, 5};
        assertThrows(DimensionMismatchException.class, () -> mdc.getCount(invalidCounts));

        int[] outOfRangeCounts = new int[]{3, 4, 5};
        assertThrows(OutOfRangeException.class, () -> mdc.getCount(outOfRangeCounts));
    }

    @Test
    public void testMultidimensionalCounterToString() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        for (int i = 0; i < 24; i++) {
            assertEquals("[" + i + "]", mdc.toString());
            mdc.next();
        }
        assertNull(mdc.next());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterNotStrictlyPositiveException() {
        int[] sizes = new int[]{-1, 4, 3};
        new MultidimensionalCounter(sizes);
    }
}