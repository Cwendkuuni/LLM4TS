package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        Assert.assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] counts = counter.getCounts(8);
        Assert.assertArrayEquals(new int[]{0, 2, 0}, counts);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCounts(27);
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int count = counter.getCount(1, 2, 3);
        Assert.assertEquals(23, count);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() throws OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(1, 2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() throws OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(1, 5, 3);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        Assert.assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] sizes = counter.getSizes();
        Assert.assertArrayEquals(new int[]{2, 3, 4}, sizes);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(0, iterator.getCount());
        Assert.assertArrayEquals(new int[]{0, 0}, iterator.getCounts());
        Assert.assertEquals(0, iterator.getCount(0));
        Assert.assertEquals(0, iterator.getCount(1));
        iterator.next();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(1, iterator.getCount());
        Assert.assertArrayEquals(new int[]{1, 0}, iterator.getCounts());
        Assert.assertEquals(1, iterator.getCount(0));
        Assert.assertEquals(0, iterator.getCount(1));
        iterator.next();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(2, iterator.getCount());
        Assert.assertArrayEquals(new int[]{0, 1}, iterator.getCounts());
        Assert.assertEquals(0, iterator.getCount(0));
        Assert.assertEquals(1, iterator.getCount(1));
        iterator.next();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(3, iterator.getCount());
        Assert.assertArrayEquals(new int[]{1, 1}, iterator.getCounts());
        Assert.assertEquals(1, iterator.getCount(0));
        Assert.assertEquals(1, iterator.getCount(1));
        iterator.next();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        String str = counter.toString();
        Assert.assertEquals("[0][0][0]", str);
    }

    // Additional test cases for edge cases and exceptions can be added here

}

