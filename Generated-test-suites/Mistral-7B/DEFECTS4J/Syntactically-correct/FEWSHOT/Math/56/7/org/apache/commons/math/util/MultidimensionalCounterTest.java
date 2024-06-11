package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;

import org.junit.Test;

public class MultidimensionalCounterTest {
    @Test
    public void testMultidimensionalCounterCreation() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertEquals(3, mdc.getDimension());
        assertArrayEquals(sizes, mdc.getSizes());
        assertEquals(32, mdc.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterCreationWithNegativeSizes() {
        int[] sizes = {-1, 3, 4};
        new MultidimensionalCounter(sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterCreationWithZeroSizes() {
        int[] sizes = {0, 3, 4};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testIterator() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator it = mdc.iterator();

        int[] first = it.getCounts();
        assertEquals(0, first[0]);
        assertEquals(0, first[1]);
        assertEquals(0, first[2]);

        int i = 0;
        while (it.hasNext()) {
            int[] counts = it.getCounts();
            assertEquals(i / (MathUtils.product(sizes) - 1) + 1, counts[0]);
            assertEquals(i % (MathUtils.product(sizes) - 1) / sizes[last] + 1, counts[1]);
            assertEquals(i % (MathUtils.product(sizes) - 1) % sizes[last] + 1, counts[2]);
            i++;
            it.next();
        }

        assertFalse(it.hasNext());
    }

    @Test
    public void testGetCount() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator it = mdc.iterator();

        int firstCount = it.next().getCount();
        assertEquals(0, firstCount);

        int counter = 0;
        while (it.hasNext()) {
            int count = it.next().getCount();
            assertEquals(counter++, count);
        }

        assertNull(it.next().getCount());
    }

    @Test
    public void testGetCounts() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator it = mdc.iterator();

        int[] firstCounts = it.next().getCounts();
        assertArrayEquals(new int[]{0, 0, 0}, firstCounts);

        int counter = 0;
        while (it.hasNext()) {
            int[] counts = it.next().getCounts();
            assertArrayEquals(new int[]{counter % sizes[0], counter % (MathUtils.product(sizes) - MathUtils.product(Arrays.copyOfRange(sizes, 0, sizes.length - 1))) % sizes[last], counter % sizes[last]}, counts);
            counter++;
        }

        assertArrayEquals(new int[]{-1, -1, -1}, it.next().getCounts());
    }

    @Test
    public void testGetCountAt() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        int[] firstCounts = mdc.getCounts(0);
        assertArrayEquals(new int[]{0, 0, 0}, firstCounts);

        int[] lastCounts = mdc.getCounts(MathUtils.product(sizes) - 1);
        assertArrayEquals(new int[]{sizes[0] - 1, sizes[1] - 1, sizes[2] - 1}, lastCounts);

        int counter = 0;
        for (int i = 0; i < MathUtils.product(sizes) - 1; i++) {
            int[] counts = mdc.getCounts(i);
            assertArrayEquals(new int[]{counter % sizes[0], counter % (MathUtils.product(Arrays.copyOfRange(sizes, 0, sizes.length - 1))) % sizes[last], counter % sizes[last]}, counts);
            counter++;
        }

        assertArrayEquals(new int[]{-1, -1, -1}, mdc.getCounts(MathUtils.product(sizes)));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountAtOutOfRange() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCounts(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountAtOutOfSize() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCounts(MathUtils.product(sizes) + 1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountAtDimensionMismatch() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCount(new int[]{2, 3});
    }

    @Test
    public void testToString() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        Iterator it = mdc.iterator();
        int counter = 0;
        while (it.hasNext()) {
            int[] counts = it.next().getCounts();
            String expected = "[";
            for (int i = 0; i < sizes.length; i++) {
                expected += String.format("%d, ", counts[i]);
            }
            expected = expected.substring(0, expected.length() - 2) + "]";
            assertEquals(expected, mdc.toString());
            it.next();
            counter++;
        }

        assertEquals("[]", mdc.toString());
    }
}