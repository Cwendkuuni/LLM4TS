package org.apache.commons.math.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for MultidimensionalCounter class.
 */
public class MultidimensionalCounterTest {

    private MultidimensionalCounter multidimensionalCounter;

    @Before
    public void setUp() {
        multidimensionalCounter = new MultidimensionalCounter(3, 2, 2);
    }

    // Testing constructor
    @Test
    public void testMultidimensionalCounterConstruction() {
        Assert.assertEquals(3, multidimensionalCounter.getDimension());
        Assert.assertArrayEquals(new int[]{3, 2, 2}, multidimensionalCounter.getSizes());
        Assert.assertEquals(18, multidimensionalCounter.getSize());
    }

    // Testing getCounts() method
    @Test
    public void testGetCounts() {
        int[] expectedCounts = new int[]{0, 0, 0};
        Assert.assertArrayEquals(expectedCounts, multidimensionalCounter.iterator().getCounts());

        multidimensionalCounter.next();
        expectedCounts = new int[]{0, 0, 1};
        Assert.assertArrayEquals(expectedCounts, multidimensionalCounter.iterator().getCounts());

        multidimensionalCounter.next();
        expectedCounts = new int[]{0, 0, 2};
        Assert.assertArrayEquals(expectedCounts, multidimensionalCounter.iterator().getCounts());

        multidimensionalCounter.next();
        expectedCounts = new int[]{1, 0, 0};
        Assert.assertArrayEquals(expectedCounts, multidimensionalCounter.iterator().getCounts());

        multidimensionalCounter.next();
        expectedCounts = new int[]{1, 0, 1};
        Assert.assertArrayEquals(expectedCounts, multidimensionalCounter.iterator().getCounts());

        multidimensionalCounter.next();
        expectedCounts = new int[]{1, 0, 2};
        Assert.assertArrayEquals(expectedCounts, multidimensionalCounter.iterator().getCounts());

        multidimensionalCounter.next();
        expectedCounts = new int[]{1, 1, 0};
        Assert.assertArrayEquals(expectedCounts, multidimensionalCounter.iterator().getCounts());

        multidimensionalCounter.next();
        expectedCounts = new int[]{1, 1, 1};
        Assert.assertArrayEquals(expectedCounts, multidimensionalCounter.iterator().getCounts());

        multidimensionalCounter.next();
        expectedCounts = new int[]{1, 1, 2};
        Assert.assertArrayEquals(expectedCounts, multidimensionalCounter.iterator().getCounts());

        multidimensionalCounter.next();
        expectedCounts = new int[]{1, 2, 0};
        Assert.assertArrayEquals(expectedCounts, multidimensionalCounter.iterator().getCounts());

        multidimensionalCounter.next();
        expectedCounts = new int[]{1, 2, 1};
        Assert.assertArrayEquals(expectedCounts, multidimensionalCounter.iterator().getCounts());

        multidimensionalCounter.next();
        expectedCounts = new int[]{1, 2, 2};
        Assert.assertArrayEquals(expectedCounts, multidimensionalCounter.iterator().getCounts());

        multidimensionalCounter.next();
        expectedCounts = new int[]{2, 0, 0};
        Assert.assertArrayEquals(expectedCounts, multidimensionalCounter.iterator().getCounts());

        multidimensionalCounter.next();
        expectedCounts = new int[]{2, 0, 1};
        Assert.assertArrayEquals(expectedCounts, multidimensionalCounter.iterator().getCounts());

        multidimensionalCounter.next();
        expectedCounts = new int[]{2, 0, 2};
        Assert.assertArrayEquals(expectedCounts, multidimensionalCounter.iterator().getCounts());

        multidimensionalCounter.next();
        expectedCounts = new int[]{2, 1, 0};
        Assert.assertArrayEquals(expectedCounts, multidimensionalCounter.iterator().getCounts());

        multidimensionalCounter.next();
        expectedCounts = new int[]{2, 1, 1};
        Assert.assertArrayEquals(expectedCounts, multidimensionalCounter.iterator().getCounts());

        multidimensionalCounter.next();
        expectedCounts = new int[]{2, 1, 2};
        Assert.assertArrayEquals(expectedCounts, multidimensionalCounter.iterator().getCounts());

        multidimensionalCounter.next();
        expectedCounts = new int[]{2, 2, 0};
        Assert.assertArrayEquals(expectedCounts, multidimensionalCounter.iterator().getCounts());

        multidimensionalCounter.next();
        expectedCounts = new int[]{2, 2, 1};
        Assert.assertArrayEquals(expectedCounts, multidimensionalCounter.iterator().getCounts());

        multidimensionalCounter.next();
        expectedCounts = new int[]{2, 2, 2};
        Assert.assertArrayEquals(expectedCounts, multidimensionalCounter.iterator().getCounts());
    }

    // Testing getCount() method
    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        multidimensionalCounter.getCount(-1, -1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange1() {
        multidimensionalCounter.getCount(3, 2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange2() {
        multidimensionalCounter.getCount(2, 3);
    }

    @Test
    public void testGetCount() {
        int expectedCount = 0;
        int actualCount = multidimensionalCounter.getCount(0, 0, 0);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 1;
        actualCount = multidimensionalCounter.getCount(0, 0, 1);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 2;
        actualCount = multidimensionalCounter.getCount(0, 0, 2);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 0;
        actualCount = multidimensionalCounter.getCount(1, 0, 0);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 1;
        actualCount = multidimensionalCounter.getCount(1, 0, 1);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 2;
        actualCount = multidimensionalCounter.getCount(1, 0, 2);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 0;
        actualCount = multidimensionalCounter.getCount(2, 0, 0);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 1;
        actualCount = multidimensionalCounter.getCount(2, 0, 1);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 2;
        actualCount = multidimensionalCounter.getCount(2, 0, 2);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 0;
        actualCount = multidimensionalCounter.getCount(0, 1, 0);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 1;
        actualCount = multidimensionalCounter.getCount(0, 1, 1);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 2;
        actualCount = multidimensionalCounter.getCount(0, 1, 2);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 0;
        actualCount = multidimensionalCounter.getCount(1, 1, 0);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 1;
        actualCount = multidimensionalCounter.getCount(1, 1, 1);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 2;
        actualCount = multidimensionalCounter.getCount(1, 1, 2);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 0;
        actualCount = multidimensionalCounter.getCount(2, 1, 0);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 1;
        actualCount = multidimensionalCounter.getCount(2, 1, 1);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 2;
        actualCount = multidimensionalCounter.getCount(2, 1, 2);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 0;
        actualCount = multidimensionalCounter.getCount(0, 2, 0);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 1;
        actualCount = multidimensionalCounter.getCount(0, 2, 1);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 2;
        actualCount = multidimensionalCounter.getCount(0, 2, 2);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 0;
        actualCount = multidimensionalCounter.getCount(1, 2, 0);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 1;
        actualCount = multidimensionalCounter.getCount(1, 2, 1);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 2;
        actualCount = multidimensionalCounter.getCount(1, 2, 2);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 0;
        actualCount = multidimensionalCounter.getCount(2, 2, 0);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 1;
        actualCount = multidimensionalCounter.getCount(2, 2, 1);
        Assert.assertEquals(expectedCount, actualCount);

        multidimensionalCounter.next();
        expectedCount = 2;
        actualCount = multidimensionalCounter.getCount(2, 2, 2);
        Assert.assertEquals(expectedCount, actualCount);
    }

    // Testing getSize() method
    @Test
    public void testGetSize() {
        Assert.assertEquals(18, multidimensionalCounter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testGetSizeNegativeSizes() {
        new MultidimensionalCounter(-3, 2, 2);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testGetSizeZeroSizes() {
        new MultidimensionalCounter(0, 0, 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testGetSizeZeroSizeLastDimension() {
        new MultidimensionalCounter(3, 0, 2);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testGetSizeZeroSizePreviousDimension() {
        new MultidimensionalCounter(3, 2, 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testGetSizeZeroSizeAllDimensions() {
        new MultidimensionalCounter(0, 0, 0);
    }

    @Test
    public void testGetSizes() {
        Assert.assertArrayEquals(new int[]{3, 2, 2}, multidimensionalCounter.getSizes());
    }

    // Testing toString() method
    @Test
    public void testToString() {
        Assert.assertEquals("[0, 0, 0]", multidimensionalCounter.toString());

        multidimensionalCounter.next();
        Assert.assertEquals("[0, 0, 1]", multidimensionalCounter.toString());

        multidimensionalCounter.next();
        Assert.assertEquals("[0, 0, 2]", multidimensionalCounter.toString());

        multidimensionalCounter.next();
        Assert.assertEquals("[1, 0, 0]", multidimensionalCounter.toString());

        multidimensionalCounter.next();
        Assert.assertEquals("[1, 0, 1]", multidimensionalCounter.toString());

        multidimensionalCounter.next();
        Assert.assertEquals("[1, 0, 2]", multidimensionalCounter.toString());

        multidimensionalCounter.next();
        Assert.assertEquals("[1, 1, 0]", multidimensionalCounter.toString());

        multidimensionalCounter.next();
        Assert.assertEquals("[1, 1, 1]", multidimensionalCounter.toString());

        multidimensionalCounter.next();
        Assert.assertEquals("[1, 1, 2]", multidimensionalCounter.toString());

        multidimensionalCounter.next();
        Assert.assertEquals("[1, 2, 0]", multidimensionalCounter.toString());

        multidimensionalCounter.next();
        Assert.assertEquals("[1, 2, 1]", multidimensionalCounter.toString());

        multidimensionalCounter.next();
        Assert.assertEquals("[1, 2, 2]", multidimensionalCounter.toString());

        multidimensionalCounter.next();
        Assert.assertEquals("[2, 0, 0]", multidimensionalCounter.toString());

        multidimensionalCounter.next();
        Assert.assertEquals("[2, 0, 1]", multidimensionalCounter.toString());

        multidimensionalCounter.next();
        Assert.assertEquals("[2, 0, 2]", multidimensionalCounter.toString());

        multidimensionalCounter.next();
        Assert.assertEquals("[2, 1, 0]", multidimensionalCounter.toString());

        multidimensionalCounter.next();
        Assert.assertEquals("[2, 1, 1]", multidimensionalCounter.toString());

        multidimensionalCounter.next();
        Assert.assertEquals("[2, 1, 2]", multidimensionalCounter.toString());

        multidimensionalCounter.next();
        Assert.assertEquals("[2, 2, 0]", multidimensionalCounter.toString());

        multidimensionalCounter.next();
        Assert.assertEquals("[2, 2, 1]", multidimensionalCounter.toString());

        multidimensionalCounter.next();
        Assert.assertEquals("[2, 2, 2]", multidimensionalCounter.toString());
    }
}