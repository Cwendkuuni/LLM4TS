package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.*;
import org.junit.Assert;
import org.junit.Test;
import java.util.*;

public class MultidimensionalCounterTest {

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        Assert.assertEquals(2, counter.getDimension());
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int[] counts = counter.getCounts(2);
        int[] expectedCounts = {0, 2};
        Assert.assertArrayEquals(expectedCounts, counts);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.getCounts(6);
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int count = counter.getCount(1, 2);
        Assert.assertEquals(5, count);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() throws OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.getCount(1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() throws OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.getCount(1, 4);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        Assert.assertEquals(6, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int[] sizes = counter.getSizes();
        int[] expectedSizes = {2, 3};
        Assert.assertArrayEquals(expectedSizes, sizes);
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        String str = counter.toString();
        Assert.assertEquals("[0][0]", str);
    }
}