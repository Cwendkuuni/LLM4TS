package org.apache.commons.math.util;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testGetDimension() {
        // Test case 1
        MultidimensionalCounter counter1 = new MultidimensionalCounter(2, 3, 4);
        int dimension1 = counter1.getDimension();
        Assert.assertEquals(3, dimension1);

        // Test case 2
        MultidimensionalCounter counter2 = new MultidimensionalCounter(4, 1, 5, 2);
        int dimension2 = counter2.getDimension();
        Assert.assertEquals(4, dimension2);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidSize() {
        // Test case 1
        MultidimensionalCounter counter1 = new MultidimensionalCounter(-2, -3);
    }

    @Test
    public void testSize() {
        // Test case 1
        MultidimensionalCounter counter1 = new MultidimensionalCounter(2, 3, 4);
        int size1 = counter1.getSize();
        Assert.assertEquals(24, size1);

        // Test case 2
        MultidimensionalCounter counter2 = new MultidimensionalCounter(4, 1, 5, 2);
        int size2 = counter2.getSize();
        Assert.assertEquals(40, size2);
    }

    @Test
    public void testGetSizes() {
        // Test case 1
        MultidimensionalCounter counter1 = new MultidimensionalCounter(2, 3, 4);
        int[] sizes1 = counter1.getSizes();
        Assert.assertArrayEquals(new int[]{2, 3, 4}, sizes1);

        // Test case 2
        MultidimensionalCounter counter2 = new MultidimensionalCounter(4, 1, 5, 2);
        int[] sizes2 = counter2.getSizes();
        Assert.assertArrayEquals(new int[]{4, 1, 5, 2}, sizes2);
    }

    @Test
    public void testGetCounts() {
        // Test case 1
        MultidimensionalCounter counter1 = new MultidimensionalCounter(2, 3, 4);
        int[] counts1 = counter1.getCounts(0);
        Assert.assertArrayEquals(new int[]{0, 0, 0}, counts1);

        // Test case 2
        MultidimensionalCounter counter2 = new MultidimensionalCounter(4, 1, 5, 2);
        int[] counts2 = counter2.getCounts(39);
        Assert.assertArrayEquals(new int[]{1, 3, 1, 1}, counts2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testInvalidIndex() {
        // Test case 1
        MultidimensionalCounter counter1 = new MultidimensionalCounter(2, 3, 4);
        int[] counts1 = counter1.getCounts(25);

        // Test case 2
        MultidimensionalCounter counter2 = new MultidimensionalCounter(4, 1, 5, 2);
        int[] counts2 = counter2.getCounts(40);
    }

     @Test(expected = DimensionMismatchException.class)
    public void testInvalidLengthArray() {
        // Test case 1
        MultidimensionalCounter counter1 = new MultidimensionalCounter(2, 3, 4);
        int count1 = counter1.getCount(0, 0);

        // Test case 2
        MultidimensionalCounter counter2 = new MultidimensionalCounter(4, 1, 5, 2);
        int count2 = counter2.getCount(1, 2, 3, 4, 5);
    }

    @Test(expected = OutOfRangeException.class)
    public void testInvalidRange() {
        // Test case 1
        MultidimensionalCounter counter1 = new MultidimensionalCounter(2, 3, 4);
        int count1 = counter1.getCount(0, 0, 5);

        // Test case 2
        MultidimensionalCounter counter2 = new MultidimensionalCounter(4, 1, 5, 2);
        int count2 = counter2.getCount(1, -2, 3, 4);
    }
}