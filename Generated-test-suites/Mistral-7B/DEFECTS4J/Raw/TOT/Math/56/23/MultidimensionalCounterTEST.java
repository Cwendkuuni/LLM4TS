package org.apache.commons.math.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test for {@link MultidimensionalCounter} class.
 */
public class MultidimensionalCounterTest {

    /**
     * Test creation of a multidimensional counter with valid arguments.
     */
    @Test
    public void testValidUsage() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Assert.assertEquals(2 * 3 * 4, counter.getSize());
        Assert.assertEquals(3, counter.getDimension());
    }

    /**
     * Test creation of a multidimensional counter with negative sizes.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSizes() {
        int[] sizes = {2, -3, 4};
        new MultidimensionalCounter(sizes);
    }

    /**
     * Test creation of a multidimensional counter with zero sizes.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroSizes() {
        int[] sizes = {2, 0, 4};
        new MultidimensionalCounter(sizes);
    }

    /**
     * Test iterating through multidimensional counter.
     */
    @Test
    public void testIterate() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator it = counter.iterator();
        int index = 0;
        while (it.hasNext()) {
            int actual = it.next();
            int expected = index;
            Assert.assertEquals(expected, actual);
            index++;
        }
    }

    /**
     * Test getting multidimensional counter for valid indices.
     */
    @Test
    public void testGetCounts() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        for (int i = 0; i < sizes[0] * sizes[1] * sizes[2]; i++) {
            int[] actual = counter.getCounts(i);
            int[] expected = new int[sizes.length];
            for (int j = last = sizes.length - 1; j >= 0; j--) {
                expected[j] = i / (MathUtils.multiply(MathUtils.multiply(uniCounterOffset[j], sizes[j]), 1.0));
                i -= uniCounterOffset[j] * expected[j];
            }
            Assert.assertArrayEquals(expected, actual);
        }
    }

    /**
     * Test getting unidimensional counter for valid indices.
     */
    @Test
    public void testGetCount() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        for (int i = 0; i < sizes[0] * sizes[1] * sizes[2]; i++) {
            int actual = counter.getCount(counter.getCounts(i));
            int expected = i;
            Assert.assertEquals(expected, actual);
        }
    }

    /**
     * Test getting multidimensional counter for invalid indices.
     */
    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCounts(-1);
    }

    /**
     * Test getting multidimensional counter for invalid sizes.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountInvalidSizes() {
        int[] sizes = {2, 3, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCounts(new int[]{4, 4, 4});
    }

    /**
     * Test equality of multidimensional counters.
     */
    @Test
    public void testEquals() {
        int[] sizes1 = {2, 3, 4};
        int[] sizes2 = {2, 3, 4};
        MultidimensionalCounter counter1 = new MultidimensionalCounter(sizes1);
        MultidimensionalCounter counter2 = new MultidimensionalCounter(sizes2);
        MultidimensionalCounter counter3 = new MultidimensionalCounter(new int[]{2, 3, 5});
        Assert.assertEquals(counter1, counter1);
        Assert.assertEquals(counter1, counter2);
        Assert.assertNotEquals(counter1, counter3);
    }

    /**
     * Test hashCode for multidimensional counters.
     */
    @Test
    public void testHashCode() {
        int[] sizes1 = {2, 3, 4};
        int[] sizes2 = {2, 3, 4};
        MultidimensionalCounter counter1 = new MultidimensionalCounter(sizes1);
        MultidimensionalCounter counter2 = new MultidimensionalCounter(sizes2);
        MultidimensionalCounter counter3 = new MultidimensionalCounter(new int[]{2, 3, 5});
        Assert.assertEquals(counter1.hashCode(), counter1.hashCode());
        Assert.assertEquals(counter1.hashCode(), counter2.hashCode());
        Assert.assertNotEquals(counter1.hashCode(), counter3.hashCode());
    }
}