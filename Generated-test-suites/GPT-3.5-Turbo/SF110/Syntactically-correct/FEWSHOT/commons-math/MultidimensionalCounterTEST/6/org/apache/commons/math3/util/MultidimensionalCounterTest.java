package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;
import java.util.Iterator;

public class MultidimensionalCounterTest {
    @Test
    public void testConstructor() {
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(-1, 3, 4);
            Assert.fail("Expected NotStrictlyPositiveException exception");
        } catch (NotStrictlyPositiveException e) {
            // expected exception
        }
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        Assert.assertEquals(3, counter.getDimension());
        Assert.assertArrayEquals(new int[]{2, 3, 4}, counter.getSizes());
        Assert.assertEquals(24, counter.getSize());
    }
    
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 2);
        try {
            counter.getCounts(-1);
            Assert.fail("Expected OutOfRangeException exception");
        } catch (OutOfRangeException e) {
            // expected exception
        }
        try {
            counter.getCounts(10);
            Assert.fail("Expected OutOfRangeException exception");
        } catch (OutOfRangeException e) {
            // expected exception
        }
        int[] counts = counter.getCounts(0);
        Assert.assertArrayEquals(new int[]{0, 0}, counts);
        counts = counter.getCounts(4);
        Assert.assertArrayEquals(new int[]{1, 1}, counts);
    }
    
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 2);
        try {
            counter.getCount(1, 5);
            Assert.fail("Expected DimensionMismatchException exception");
        } catch (DimensionMismatchException e) {
            // expected exception
        }
        try {
            counter.getCount(1, -2);
            Assert.fail("Expected OutOfRangeException exception");
        } catch (OutOfRangeException e) {
            // expected exception
        }
        int count = counter.getCount(0, 1);
        Assert.assertEquals(1, count);
        count = counter.getCount(2, 0);
        Assert.assertEquals(4, count);
    }
    
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        Iterator<Integer> iterator = counter.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(0), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(1), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(2), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(3), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(4), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(5), iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }
}