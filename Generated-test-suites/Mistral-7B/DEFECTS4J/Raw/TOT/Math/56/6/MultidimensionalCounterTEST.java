package org.apache.commons.math.util;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.util.MultidimensionalCounter;
import org.apache.commons.math.util.MathUtils;

/**
 * Test class for MultidimensionalCounter.
 */
public class MultidimensionalCounterTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testValidConstruction() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertEquals(3, mdc.getDimension());
        assertEquals(2 * 4 * 3, mdc.getSize());
    }

    /**
     * Test iterator.
     */
    @Test
    public void testIterator() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        MultidimensionalCounter.Iterator iter = mdc.iterator();
        for (int i = 0; i < mdc.getSize(); i++) {
            assertEquals(i, iter.next());
            int[] counts = iter.getCounts();
            assertArrayEquals(new int[]{i / (mdc.getSize() / (int) Math.pow(sizes.length, 2)),
            i / sizes[0], i % sizes[0]}, counts);
        }
        assertFalse(iter.hasNext());
    }

    /**
     * Test getCount method with valid arguments.
     */
    @Test
    public void testValidGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        int[] counts = mdc.getCounts(0);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(0, counts[2]);

        int index = mdc.getCount(counts);
        assertEquals(0, index);

        mdc.next();

        counts = mdc.getCounts(0);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(0, counts[2]);

        index = mdc.getCount(counts);
        assertEquals(1, index);
    }

    /**
     * Test getCount method with invalid arguments (DimensionMismatchException).
     */
    @Test(expected = DimensionMismatchException.class)
    public void testInvalidDimensionGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        int[] invalidCounts = {5, 3, 2};
        mdc.getCounts(invalidCounts);
    }

    /**
     * Test getCount method with invalid arguments (OutOfRangeException).
     */
    @Test(expected = OutOfRangeException.class)
    public void testInvalidGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        int index = mdc.getCount(-1);
    }

    /**
     * Test getSizes method.
     */
    @Test
    public void testGetSizes() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        int[] sizesTest = mdc.getSizes();

        assertArrayEquals(sizes, sizesTest);
    }

    /**
     * Test constructor with negative or zero dimension sizes.
     */
    @Test(expected = org.apache.commons.math.exception.NotStrictlyPositiveException.class)
    public void testInvalidConstruction() {
        int[] sizes = {2, 0, 3};
        new MultidimensionalCounter(sizes);
    }
}