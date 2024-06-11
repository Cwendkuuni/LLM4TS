package org.apache.commons.math.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link MultidimensionalCounter}.
 */
public class MultidimensionalCounterTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        int[] size = new int[] {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Assert.assertEquals(2, mdc.getDimension());
        Assert.assertArrayEquals(new int[] {2, 3, 4}, mdc.getSizes());
        Assert.assertEquals(32, mdc.getSize());
    }

    /**
     * Test iteration.
     */
    @Test
    public void testIterator() {
        int[] size = new int[] {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator itr = mdc.iterator();
        int index = 0;
        while (itr.hasNext()) {
            int multidimensional = itr.next();
            int[] multidimensionalCounts = itr.getCounts();
            int unidimensional = mdc.getCount(multidimensionalCounts);
            int[] expectedMultidimensionalCounts = new int[] { index % 2, index % 3, index % 4 };
            Assert.assertArrayEquals(expectedMultidimensionalCounts, multidimensionalCounts);
            Assert.assertEquals(index, unidimensional);
            index++;
        }
        Assert.assertFalse(itr.hasNext());
    }

    /**
     * Test getCounts with valid index.
     */
    @Test
    public void testGetCountsValidIndex() {
        int[] size = new int[] {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int[] multidimensionalCounts = mdc.getCounts(0);
        int[] expectedMultidimensionalCounts = new int[] { 0, 0, 0 };
        Assert.assertArrayEquals(expectedMultidimensionalCounts, multidimensionalCounts);
    }

    /**
     * Test getCounts with out-of-range index.
     */
    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] size = new int[] {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCounts(32);
    }

    /**
     * Test getCount with valid index.
     */
    @Test
    public void testGetCountValidIndex() {
        int[] size = new int[] {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int unidimensional = mdc.getCount(new int[] {0, 0, 0});
        Assert.assertEquals(0, unidimensional);
    }

    /**
     * Test getCount with invalid dimension index.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountInvalidDimensionIndex() {
        int[] size = new int[] {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCount(new int[] {2});
    }

    /**
     * Test getCount with out-of-range index.
     */
    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] size = new int[] {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCount(new int[] {2, 3, 4});
    }
}