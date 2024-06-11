package org.apache.commons.math.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Iterator;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(sizes, counter.getSizes());
        assertEquals(135, counter.getSize());
    }

    @Test
    public void testGetCounts() {
        int[] sizes = { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            int[] counts = iterator.next().getCounts();
            assertEquals(sizes.length, counts.length);
            assertArrayEquals(sizes, counts);
            index++;
        }
        assertFalse(iterator.hasNext());
        assertThrows(OutOfRangeException.class, () -> counter.getCounts(index));
    }

    @Test
    public void testGetCount() {
        int[] sizes = { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] index = { 0, 0, 0 };
        assertEquals(0, counter.getCount(index));

        index[0] = 1;
        index[1] = 1;
        index[2] = 1;
        assertEquals(1, counter.getCount(index));

        index[2] = 2;
        assertEquals(7, counter.getCount(index));

        index[1] = sizes[1] - 1;
        index[2] = sizes[2] - 1;
        assertEquals(totalSize(sizes) - 1, counter.getCount(index));

        int[] wrongSizeIndex = new int[sizes.length + 1];
        assertThrows(DimensionMismatchException.class, () -> counter.getCount(wrongSizeIndex));

        int[] negativeIndex = new int[sizes.length];
        for (int i = 0; i < sizes.length; i++) {
            negativeIndex[i] = -1;
        }
        assertThrows(OutOfRangeException.class, () -> counter.getCount(negativeIndex));
    }

    @Test
    public void testIterator() {
        int[] sizes = { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            int[] counts = next.getCounts();
            assertEquals(sizes.length, counts.length);
            assertArrayEquals(sizes, counts);
            index++;
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testTotalSize() {
        int[] sizes = { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(totalSize(sizes), counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSizes() {
        int[] sizes = { -3, 4, 5 };
        new MultidimensionalCounter(sizes);
    }

    private int totalSize(int[] sizes) {
        int total = 1;
        for (int size : sizes) {
            total *= size;
        }
        return total;
    }
}