package org.apache.commons.math.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

import java.util.Iterator;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertEquals(3, mdc.getDimension());
        assertArrayEquals(sizes, mdc.getSizes());
        assertEquals(3 * 4 * 5, mdc.getSize());
    }

    @Test
    public void testMultidimensionalCounterIterator() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = mdc.iterator();

        assertEquals(0, iterator.next());
        assertArrayEquals(new int[]{0, 0, 0}, mdc.getCounts(0));

        for (int i = 1; i < mdc.getSize(); i++) {
            Integer next = iterator.next();
            int[] counts = mdc.getCounts(next);
            int lastDim = mdc.getDimension() - 1;
            for (int j = 0; j < lastDim; j++) {
                assertEquals(counts[j] + 1, sizes[j]);
            }
            assertEquals(counts[lastDim] + 1, sizes[lastDim] - 1);
        }

        assertThrows(UnsupportedOperationException.class, () -> iterator.remove());
    }

    @Test
    public void testGetCount() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        for (int i = 0; i < mdc.getSize(); i++) {
            Integer index = mdc.iterator().next();
            int expectedCount = mdc.getCount(i);
            int[] counts = mdc.getCounts(index);
            for (int j = 0; j < mdc.getDimension(); j++) {
                assertEquals(counts[j], i / Math.pow(mdc.getSizes()[j], mdc.getDimension() - 1 - j));
            }
            assertEquals(expectedCount, i);
        }
    }

    @Test
    public void testGetCountNegativeIndex() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertThrows(OutOfRangeException.class, () -> mdc.getCount(-1));
    }

    @Test
    public void testGetCountOutOfRangeIndex() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        int size = mdc.getSize();
        assertThrows(OutOfRangeException.class, () -> mdc.getCount(size));
    }

    @Test
    public void testGetCounts() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        for (int i = 0; i < mdc.getSize(); i++) {
            int[] counts = mdc.getCounts(i);
            for (int j = 0; j < mdc.getDimension(); j++) {
                assertEquals(counts[j], i / Math.pow(mdc.getSizes()[j], mdc.getDimension() - 1 - j));
            }
        }

        assertArrayEquals(new int[]{0, 0, 0}, mdc.getCounts(0));
        int[] sizes2 = {3, 4, 5, 6};
        MultidimensionalCounter mdc2 = new MultidimensionalCounter(sizes2);
        assertThrows(DimensionMismatchException.class, () -> mdc2.getCounts(new int[]{3, 3, 3, 2}));
    }

    @Test
    public void testGetCountsNegativeIndex() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertThrows(OutOfRangeException.class, () -> mdc.getCounts(-1));
    }

    @Test
    public void testGetCountsOutOfRangeIndex() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        int size = mdc.getSize();
        assertThrows(OutOfRangeException.class, () -> mdc.getCounts(size));
    }

    @Test
    public void testGetSize() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertEquals(3 * 4 * 5, mdc.getSize());
    }

    @Test
    public void testGetSizes() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertArrayEquals(sizes, mdc.getSizes());
    }

    @Test
    public void testToString() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        for (int i = 0; i < mdc.getSize(); i++) {
            String expectedToString = "[";
            for (int j = 0; j < mdc.getDimension(); j++) {
                expectedToString += getCount(i, j) + "]";
                if (j < mdc.getDimension() - 1) {
                    expectedToString += ", ";
                }
            }
            assertEquals(expectedToString, mdc.toString());
            mdc.next();
        }
        assertNull(mdc.next());
    }

    @Test
    public void testEmptyMultidimensionalCounter() {
        MultidimensionalCounter mdc = new MultidimensionalCounter();
        assertEquals(0, mdc.getDimension());
        assertArrayEquals(new int[0], mdc.getSizes());
        assertEquals(0, mdc.getSize());
    }

    @Test
    public void testEmptyMultidimensionalCounterIterator() {
        MultidimensionalCounter mdc = new MultidimensionalCounter();
        Iterator<Integer> iterator = mdc.iterator();
        assertNull(iterator.next());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveSizes() {
        int[] sizes = {3, -4, 5};
        new MultidimensionalCounter(sizes);
    }
}