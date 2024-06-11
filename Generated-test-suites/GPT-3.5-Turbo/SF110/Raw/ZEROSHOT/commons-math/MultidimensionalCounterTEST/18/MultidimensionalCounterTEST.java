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
            MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
            Assert.assertEquals(3, counter.getDimension());
            Assert.assertArrayEquals(new int[] {2, 3, 4}, counter.getSizes());
            Assert.assertEquals(24, counter.getSize());
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("NotStrictlyPositiveException thrown");
        }
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(-1, 2, 3);
            Assert.fail("NotStrictlyPositiveException not thrown");
        } catch (NotStrictlyPositiveException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        Iterator<Integer> iterator = counter.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(0, (int) iterator.next());
        Assert.assertEquals(1, (int) iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(2, (int) iterator.next());
        Assert.assertEquals(3, (int) iterator.next());
        Assert.assertEquals(4, (int) iterator.next());
        Assert.assertTrue(iterator.hasNext());
        while (iterator.hasNext()) {
            iterator.next();
        }
        Assert.assertEquals(23, iterator.getCount());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetCounts() {
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
            Assert.assertArrayEquals(new int[] {0, 0, 0}, counter.getCounts(0));
            Assert.assertArrayEquals(new int[] {1, 0, 0}, counter.getCounts(1));
            Assert.assertArrayEquals(new int[] {0, 1, 0}, counter.getCounts(2));
            Assert.assertArrayEquals(new int[] {1, 1, 0}, counter.getCounts(3));
            Assert.assertArrayEquals(new int[] {0, 2, 0}, counter.getCounts(4));
            Assert.assertArrayEquals(new int[] {1, 2, 0}, counter.getCounts(5));
            Assert.assertArrayEquals(new int[] {0, 0, 1}, counter.getCounts(6));
            Assert.assertArrayEquals(new int[] {1, 0, 1}, counter.getCounts(7));
            Assert.assertArrayEquals(new int[] {0, 1, 1}, counter.getCounts(8));
            Assert.assertArrayEquals(new int[] {1, 1, 1}, counter.getCounts(9));
            Assert.assertArrayEquals(new int[] {0, 2, 1}, counter.getCounts(10));
            Assert.assertArrayEquals(new int[] {1, 2, 1}, counter.getCounts(11));
            Assert.assertArrayEquals(new int[] {0, 0, 2}, counter.getCounts(12));
            Assert.assertArrayEquals(new int[] {1, 0, 2}, counter.getCounts(13));
            Assert.assertArrayEquals(new int[] {0, 1, 2}, counter.getCounts(14));
            Assert.assertArrayEquals(new int[] {1, 1, 2}, counter.getCounts(15));
            Assert.assertArrayEquals(new int[] {0, 2, 2}, counter.getCounts(16));
            Assert.assertArrayEquals(new int[] {1, 2, 2}, counter.getCounts(17));
            Assert.assertArrayEquals(new int[] {0, 0, 3}, counter.getCounts(18));
            Assert.assertArrayEquals(new int[] {1, 0, 3}, counter.getCounts(19));
            Assert.assertArrayEquals(new int[] {0, 1, 3}, counter.getCounts(20));
            Assert.assertArrayEquals(new int[] {1, 1, 3}, counter.getCounts(21));
            Assert.assertArrayEquals(new int[] {0, 2, 3}, counter.getCounts(22));
            Assert.assertArrayEquals(new int[] {1, 2, 3}, counter.getCounts(23));
        } catch (OutOfRangeException e) {
            Assert.fail("OutOfRangeException thrown");
        }
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
            counter.getCounts(24);
            Assert.fail("OutOfRangeException not thrown");
        } catch (OutOfRangeException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testGetCount() {
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
            Assert.assertEquals(0, counter.getCount(0, 0, 0));
            Assert.assertEquals(1, counter.getCount(1, 0, 0));
            Assert.assertEquals(2, counter.getCount(0, 1, 0));
            Assert.assertEquals(3, counter.getCount(1, 1, 0));
            Assert.assertEquals(4, counter.getCount(0, 2, 0));
            Assert.assertEquals(5, counter.getCount(1, 2, 0));
            Assert.assertEquals(6, counter.getCount(0, 0, 1));
            Assert.assertEquals(7, counter.getCount(1, 0, 1));
            Assert.assertEquals(8, counter.getCount(0, 1, 1));
            Assert.assertEquals(9, counter.getCount(1, 1, 1));
            Assert.assertEquals(10, counter.getCount(0, 2, 1));
            Assert.assertEquals(11, counter.getCount(1, 2, 1));
            Assert.assertEquals(12, counter.getCount(0, 0, 2));
            Assert.assertEquals(13, counter.getCount(1, 0, 2));
            Assert.assertEquals(14, counter.getCount(0, 1, 2));
            Assert.assertEquals(15, counter.getCount(1, 1, 2));
            Assert.assertEquals(16, counter.getCount(0, 2, 2));
            Assert.assertEquals(17, counter.getCount(1, 2, 2));
            Assert.assertEquals(18, counter.getCount(0, 0, 3));
            Assert.assertEquals(19, counter.getCount(1, 0, 3));
            Assert.assertEquals(20, counter.getCount(0, 1, 3));
            Assert.assertEquals(21, counter.getCount(1, 1, 3));
            Assert.assertEquals(22, counter.getCount(0, 2, 3));
            Assert.assertEquals(23, counter.getCount(1, 2, 3));
        } catch (DimensionMismatchException e) {
            Assert.fail("DimensionMismatchException thrown");
        } catch (OutOfRangeException e) {
            Assert.fail("OutOfRangeException thrown");
        }
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
            counter.getCount(0, 0);
            Assert.fail("DimensionMismatchException not thrown");
        } catch (DimensionMismatchException e) {
            Assert.assertTrue(true);
        } catch (OutOfRangeException e) {
            Assert.fail("OutOfRangeException thrown");
        }
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
            counter.getCount(1, 2, 5);
            Assert.fail("OutOfRangeException not thrown");
        } catch (DimensionMismatchException e) {
            Assert.fail("DimensionMismatchException thrown");
        } catch (OutOfRangeException e) {
            Assert.assertTrue(true);
        }
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
            counter.getCount(2, 2, 3);
            Assert.fail("OutOfRangeException not thrown");
        } catch (DimensionMismatchException e) {
            Assert.fail("DimensionMismatchException thrown");
        } catch (OutOfRangeException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        Assert.assertEquals("[0][0][0]", counter.toString());
        Iterator<Integer> iterator = counter.iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        Assert.assertEquals("[1][2][3]", counter.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        Iterator<Integer> iterator = counter.iterator();
        iterator.remove();
    }
}