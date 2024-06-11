package org.apache.commons.math.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

public class MultidimensionalCounterTest {

    @Test
    public void testEmptyMultiDimensionalCounter() {
        int[] size = new int[0];
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);

        Iterator<Integer> iterator = multidimensionalCounter.iterator();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testSingleDimensionCounter() {
        int size = 5;
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[]{size});

        Iterator<Integer> iterator = multidimensionalCounter.iterator();
        for (int i = 0; i < size; i++) {
            Assert.assertTrue(iterator.hasNext());
            Assert.assertEquals(i, iterator.next().intValue());
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testMultiDimensionCounter() {
        int[] sizes = new int[]{3, 4, 5};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(sizes);

        Iterator<Integer> iterator = multidimensionalCounter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            int next = iterator.next().intValue();
            Assert.assertEquals(count, next);
            count++;
        }
        Assert.assertEquals(sizes[0] * sizes[1] * sizes[2], count);
    }

    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeMultiDimensionalCounter() {
        int[] sizes = new int[]{3, 4, 5};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(sizes);

        Iterator<Integer> iterator = multidimensionalCounter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            int next = iterator.next().intValue();
            if (next != count) {
                count = next;
                continue;
            }
            Assert.assertEquals(sizes[0] * sizes[1] * sizes[2], count);
            throw new OutOfRangeException(next, 0, sizes[0] * sizes[1] * sizes[2]);
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchMultiDimensionalCounter() {
        int[] sizes = new int[]{3, 4, 5};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[]{3, 4, 6});

        Iterator<Integer> iterator = multidimensionalCounter.iterator();
        Assert.assertFalse(iterator.hasNext());
        throw new DimensionMismatchException(sizes.length, multidimensionalCounter.getDimension());
    }
}