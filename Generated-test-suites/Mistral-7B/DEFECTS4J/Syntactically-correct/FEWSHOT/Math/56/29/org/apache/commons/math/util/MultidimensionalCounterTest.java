package org.apache.commons.math.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = new int[] {3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(sizes, counter.getSizes());
        assertEquals(3 * 4 * 5, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSizes() {
        int[] sizes = new int[] {-1, 3, 4};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testIterator() {
        int[] sizes = new int[] {3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        assertNotNull(iterator.hasNext());
        for (int i = 0; i < sizes[0] * sizes[1] * sizes[2]; i++) {
            Integer next = iterator.next();
            assertNotNull(next);
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetCounts() {
        int[] sizes = new int[] {3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] counts;
        for (int i = 0; i < sizes[0] * sizes[1] * sizes[2]; i++) {
            counts = counter.getCounts(i);
            assertArrayEquals(new int[]{i / (sizes[0] * sizes[1]), i % (sizes[0] * sizes[1]) / sizes[1], i % sizes[1]}, counts);
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] sizes = new int[] {3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int index = sizes[0] * sizes[1] * sizes[2] + 1;
        counter.getCounts(index);
    }

    @Test
    public void testGetCount() {
        int[] sizes = new int[] {3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        for (int i = 0; i < sizes[0] * sizes[1] * sizes[2]; i++) {
            assertEquals(i, counter.getCount(i));
        }
        assertThrows(OutOfRangeException.class, () -> counter.getCount(-1));
        assertThrows(OutOfRangeException.class, () -> counter.getCount(sizes[0] * sizes[1] * sizes[2] + 1));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWrongDimension() {
        int[] sizes = new int[] {3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] wrongDim = new int[]{1, 2, 3, 4};
        counter.getCount(wrongDim);
    }
}