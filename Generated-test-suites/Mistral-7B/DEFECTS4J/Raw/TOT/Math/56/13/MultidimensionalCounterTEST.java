package org.apache.commons.math.util;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Iterator;

public class MultidimensionalCounterTest {

    @Test
    public void testEmptyMultidimensionalCounter() {
        int[] size = new int[0];
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        Iterator<Integer> iterator = counter.iterator();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testOneDimensionalCounter() {
        int[] size = new int[1];
        size[0] = 5;
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        Iterator<Integer> iterator = counter.iterator();
        for (int i = 0; i < 5; i++) {
            Assert.assertTrue(iterator.hasNext());
            Integer next = iterator.next();
            Assert.assertEquals(i, next.intValue());
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testTwoDimensionalCounter() {
        int[] size = new int[2];
        size[0] = 3;
        size[1] = 4;
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        Iterator<Integer> iterator = counter.iterator();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                Integer next = iterator.next();
                int[] counts = iterator.getCounts();
                Assert.assertEquals(i, counts[0]);
                Assert.assertEquals(j, counts[1]);
                Assert.assertEquals(i * 4 + j, next.intValue());
            }
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountNegativeIndex() {
        int[] size = new int[1];
        size[0] = 5;
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        iterator.next();
        iterator.next();
        int index = iterator.getCount() - 2;
        int result = counter.getCount(index);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWrongDimension() {
        int[] size = new int[2];
        size[0] = 3;
        size[1] = 4;
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int[] wrongDimension = new int[1];
        int result = counter.getCount(wrongDimension);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] size = new int[2];
        size[0] = 3;
        size[1] = 4;
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        iterator.next();
        iterator.next();
        iterator.next();

        int index = iterator.getCount();
        int result = counter.getCount(index + 1);
    }
}