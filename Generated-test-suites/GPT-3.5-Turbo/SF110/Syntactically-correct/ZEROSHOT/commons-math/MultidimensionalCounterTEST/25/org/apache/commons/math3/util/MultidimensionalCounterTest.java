package org.apache.commons.math3.util;

import org.junit.Test;
import org.junit.Assert;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(-1, 2, 3);
        } catch (NotStrictlyPositiveException e) {
            // expected exception
            return;
        }
        Assert.fail("NotStrictlyPositiveException not thrown");
    }
    
    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        Assert.assertEquals(3, counter.getDimension());
    }
    
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        int[] counts = counter.getCounts(2);
        Assert.assertArrayEquals(new int[]{1, 0}, counts);
    }
    
    @Test
    public void testGetCountsOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        try {
            int[] counts = counter.getCounts(5);
        } catch (OutOfRangeException e) {
            // expected exception
            return;
        }
        Assert.fail("OutOfRangeException not thrown");
    }
    
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        try {
            int count = counter.getCount(1, 0);
            Assert.assertEquals(2, count);
        } catch (OutOfRangeException | DimensionMismatchException e) {
            Assert.fail(e.getMessage());
        }
    }
    
    @Test
    public void testGetCountOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        try {
            int count = counter.getCount(1, 2);
        } catch (OutOfRangeException e) {
            // expected exception
            return;
        } catch (DimensionMismatchException e) {
            Assert.fail(e.getMessage());
        }
        Assert.fail("OutOfRangeException not thrown");
    }
    
    @Test
    public void testGetCountDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        try {
            int count = counter.getCount(1);
        } catch (DimensionMismatchException e) {
            // expected exception
            return;
        } catch (OutOfRangeException e) {
            Assert.fail(e.getMessage());
        }
        Assert.fail("DimensionMismatchException not thrown");
    }
    
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 3);
        Assert.assertEquals(12, counter.getSize());
    }
    
    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 3);
        int[] sizes = counter.getSizes();
        Assert.assertArrayEquals(new int[]{2, 2, 3}, sizes);
    }
    
    @Test
    public void testIteratorHasNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        Assert.assertTrue(iterator.hasNext());
    }
    
    @Test
    public void testIteratorNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = iterator.next();
        Assert.assertEquals(0, count);
    }
    
    @Test
    public void testIteratorGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = iterator.getCount();
        Assert.assertEquals(-1, count);
    }
    
    @Test
    public void testIteratorGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int[] counts = iterator.getCounts();
        Assert.assertArrayEquals(new int[]{0, 0}, counts);
    }
    
    @Test
    public void testIteratorGetCountByDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = iterator.getCount(0);
        Assert.assertEquals(0, count);
    }
    
    @Test
    public void testIteratorRemove() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        try {
            iterator.remove();
        } catch (UnsupportedOperationException e) {
            // expected exception
            return;
        }
        Assert.fail("UnsupportedOperationException not thrown");
    }
}