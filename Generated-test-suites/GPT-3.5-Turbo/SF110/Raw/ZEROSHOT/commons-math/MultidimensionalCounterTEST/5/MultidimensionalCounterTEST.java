package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Before
    public void setUp() {
        int[] size = {2, 3, 4};
        counter = new MultidimensionalCounter(size);
    }

    @Test
    public void testGetDimension() {
        int expected = 3;
        int actual = counter.getDimension();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetCounts() {
        int[] counts = counter.getCounts(8);

        int[] expected = {0, 2, 0};
        Assert.assertArrayEquals(expected, counts);
    }

    @Test
    public void testGetCount() {
        int[] counts = {1, 1, 2};
        int expected = 19;
        int actual = counter.getCount(counts);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetSize() {
        int expected = 24;
        int actual = counter.getSize();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetSizes() {
        int[] sizes = counter.getSizes();

        int[] expected = {2, 3, 4};
        Assert.assertArrayEquals(expected, sizes);
    }
}