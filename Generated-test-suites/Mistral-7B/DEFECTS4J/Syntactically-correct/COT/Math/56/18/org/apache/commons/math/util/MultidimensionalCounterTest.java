package org.apache.commons.math.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Iterator;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;

import org.apache.commons.math.util.MultidimensionalCounter;
import org.apache.commons.math.util.MathUtils;

/**
 * Test class for {@link MultidimensionalCounter}.
 */
public class MultidimensionalCounterTest {

    /**
     * Test constructor with valid input.
     */
    @Test
    public void testValidConstructor() {
        int[] sizes = { 2, 4, 3 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertEquals(6 * 4 * 3, mdc.getSize());
    }

    /**
     * Test constructor with empty array as input.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testEmptyConstructor() {
        new MultidimensionalCounter(new int[0]);
    }

    /**
     * Test constructor with negative size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSizes() {
        int[] sizes = { -1, 3, 3 };
        new MultidimensionalCounter(sizes);
    }

    /**
     * Test method getDimension().
     */
    @Test
    public void testGetDimension() {
        int[] sizes = { 2, 4, 3 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertEquals(3, mdc.getDimension());
    }

    /**
     * Test method getCounts().
     */
    @Test
    public void testGetCounts() {
        int[] sizes = { 2, 4, 3 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        int index = 10;
        int[] counts = mdc.getCounts(index);
        assertEquals(2, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(2, counts[2]);
    }

    /**
     * Test method getCount().
     */
    @Test
    public void testGetCount() {
        int[] sizes = { 2, 4, 3 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        int[] index = { 1, 2, 2 };
        int count = mdc.getCount(index);
        assertEquals(13, count);
    }

    /**
     * Test method getCounts() with out of range index.
     */
    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeGetCounts() {
        int[] sizes = { 2, 4, 3 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        int index = 15;
        mdc.getCounts(index);
    }

    /**
     * Test method getCount() with out of range index.
     */
    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeGetCount() {
        int[] sizes = { 2, 4, 3 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        int[] index = { 1, 6, 2 };
        mdc.getCount(index);
    }

    /**
     * Test method getCount() with invalid dimension length.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testInvalidDimensionGetCount() {
        int[] sizes = { 2, 4, 3 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        int[] index = { 1, 2 };
        mdc.getCount(index);
    }

    /**
     * Test method iterator().
     */
    @Test
    public void testIterator() {
        int[] sizes = { 2, 4, 3 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        Iterator<Integer> it = mdc.iterator();
        for (int i = 0; i < mdc.getSize(); i++) {
            assertEquals(i, it.next().getCount());
        }
        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    /**
     * Test method getSizes().
     */
    @Test
    public void testGetSizes() {
        int[] sizes = { 2, 4, 3 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        int[] sizes2 = mdc.getSizes();
        assertEquals(3, sizes2.length);
        assertEquals(2, sizes2[0]);
        assertEquals(4, sizes2[1]);
        assertEquals(3, sizes2[2]);
    }

    /**
     * Test method getSize().
     */
    @Test
    public void testGetSize() {
        int[] sizes = { 2, 4, 3 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        assertEquals(6 * 4 * 3, mdc.getSize());
    }

    /**
     * Test method toString().
     */
    @Test
    public void testToString() {
        int[] sizes = { 2, 4, 3 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        int[] coordinates = { 1, 1, 1 };
        String expected = "[1][1][1]";
        assertEquals(expected, mdc.toString());
    }
}