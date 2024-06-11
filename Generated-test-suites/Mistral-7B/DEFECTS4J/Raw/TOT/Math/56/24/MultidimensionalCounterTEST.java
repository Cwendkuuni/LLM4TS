package org.apache.commons.math.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

/**
 * Test for {@link MultidimensionalCounter}.
 */
public class MultidimensionalCounterTest {

    /**
     * Test constructor with positive dimensions.
     */
    @Test
    public void testConstructorPositive() {
        int[] size = new int[] { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] expected = new int[] { 15, 20, 25 };
        int totalSize = 15 * 20 * 25;
        assertEquals(expected[0], counter.getDimension());
        assertEquals(expected[1], counter.getSizes()[0]);
        assertEquals(expected[2], counter.getSizes()[1]);
        assertEquals(expected[2] * expected[1], counter.getSizes()[2]);
        assertEquals(totalSize, counter.getSize());
    }

    /**
     * Test constructor with zero dimension.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroDimension() {
        int[] size = new int[] { 0 };
        new MultidimensionalCounter(size);
    }

    /**
     * Test constructor with negative dimensions.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeDimensions() {
        int[] size = new int[] { -1, 2, 3 };
        new MultidimensionalCounter(size);
    }

    /**
     * Test iterator.
     */
    @Test
    public void testIterator() {
        int[] size = new int[] { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        int[] counts = iterator.getCounts();
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(0, counts[2]);
        int index = iterator.next();
        counts = iterator.getCounts();
        assertEquals(1, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(0, counts[2]);
        assertEquals(1, index);
        index = iterator.next();
        counts = iterator.getCounts();
        assertEquals(1, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(0, counts[2]);
        assertEquals(2, index);
        index = iterator.next();
        counts = iterator.getCounts();
        assertEquals(1, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(1, counts[2]);
        assertEquals(3, index);
        index = iterator.next();
        counts = iterator.getCounts();
        assertEquals(1, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(0, counts[2]);
        assertEquals(4, index);
        index = iterator.next();
        counts = iterator.getCounts();
        assertEquals(1, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);
        assertEquals(5, index);
        index = iterator.next();
        counts = iterator.getCounts();
        assertEquals(1, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(2, counts[2]);
        assertEquals(6, index);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    /**
     * Test getCount method in multidimensional counter.
     */
    @Test
    public void testGetCountMultidimensional() {
        int[] size = new int[] { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] counts = new int[] { 1, 2, 3 };
        int expected = MathUtils.sum(MathUtils.multiply(Arrays.stream(size).mapToInt(i -> i).toArray()));
        int result = counter.getCount(counts);
        assertEquals(result, expected);
    }

    /**
     * Test getCount method in multidimensional counter with negative dimensions.
     */
    @Test(expected = OutOfRangeException.class)
    public void testGetCountNegativeDimensions() {
        int[] size = new int[] { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] counts = new int[] { -1, 2, 3 };
        counter.getCount(counts);
    }

    /**
     * Test getCount method in multidimensional counter with different dimensions.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDifferentDimensions() {
        int[] size = new int[] { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] counts = new int[] { 1, 2 };
        counter.getCount(counts);
    }

    /**
     * Test getCounts method in multidimensional counter.
     */
    @Test
    public void testGetCountsMultidimensional() {
        int[] size = new int[] { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] index = new int[] { 1, 2, 3 };
        int[] expected = new int[] { 1, 2, 3 };
        int[] result = counter.getCounts(counter.getCount(expected));
        assertThat(Arrays.asList(expected), Arrays.asList(result));
    }

    /**
     * Test getCounts method in multidimensional counter with negative indices.
     */
    @Test(expected = OutOfRangeException.class)
    public void testGetCountsNegativeIndices() {
        int[] size = new int[] { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] index = new int[] { -1, 2, 3 };
        counter.getCounts(counter.getCount(index));
    }

    /**
     * Test getCounts method in multidimensional counter with different dimensions.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsDifferentDimensions() {
        int[] size = new int[] { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] index = new int[] { 1, 2 };
        counter.getCounts(counter.getCount(index));
    }
}