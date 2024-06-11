package org.apache.commons.math.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;

import java.util.Iterator;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] sizes = new int[]{3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
        assertEquals(sizes, counter.getSizes());
        assertEquals(18, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSizes() {
        int[] sizes = new int[]{-1, 2, 1};
        new MultidimensionalCounter(sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroSizes() {
        int[] sizes = new int[]{1, 0, 1};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testGetCounts() {
        int[] sizes = new int[]{3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        int[] counts = counter.getCounts(0);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(0, counts[2]);

        counts = counter.getCounts(18);
        assertEquals(2, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] sizes = new int[]{3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCounts(-1);
    }

    @Test
    public void testGetCount() {
        int[] sizes = new int[]{3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        assertEquals(0, counter.getCount(new int[0]));
        assertEquals(0, counter.getCount(new int[]{0, 0, 0}));
        assertEquals(1, counter.getCount(new int[]{0, 0, 1}));
        assertEquals(12, counter.getCount(new int[]{1, 0, 0}));
        assertEquals(13, counter.getCount(new int[]{1, 0, 1}));
        assertEquals(23, counter.getCount(new int[]{1, 2, 1}));

        try {
            counter.getCount(new int[]{3, 2, 1});
            assert false;
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        int[] sizes = new int[]{3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(new int[]{2, 2, 1});
    }

    @Test
    public void testIterator() {
        int[] sizes = new int[]{3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> it = counter.iterator();

        // First call should return -1, as the iterator is not initialized
        assertNull(it.next());

        int count = 0;
        while (it.hasNext()) {
            Integer index = it.next();
            assertEquals(index, count);
            int[] counts = it.getCounts();
            assertArrayEquals(new int[]{count % sizes[0], count % (sizes[0] * sizes[1]), count % (sizes[0] * sizes[1] * sizes[2])}, counts);
            count++;
        }
        assertFalse(it.hasNext());
    }
}