package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.util.MathUtils;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = new int[] {3, 4, 2};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertEquals(3, mdc.getDimension());
        assertEquals(sizes, mdc.getSizes());
        assertEquals(3 * 4 * 2, mdc.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroDimension() {
        new MultidimensionalCounter(new int[0]);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroSizeInDimension() {
        new MultidimensionalCounter(new int[] {3, 0, 2});
    }

    @Test
    public void testIterator() {
        int[] sizes = new int[] {3, 4, 2};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator it = mdc.iterator();
        int i = 0;
        while (it.hasNext()) {
            Integer next = it.next();
            assertEquals(i, next);
            i++;
        }
        assertEquals(-1, it.next());
    }

    @Test
    public void testGetCounts() {
        int[] sizes = new int[] {3, 4, 2};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        int[] counts = mdc.getCounts(0);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(0, counts[2]);

        counts = mdc.getCounts(12);
        assertEquals(1, counts[0]);
        assertEquals(2, counts[1]);
        assertEquals(0, counts[2]);

        counts = mdc.getCounts(23);
        assertEquals(1, counts[0]);
        assertEquals(3, counts[1]);
        assertEquals(2 - 1, counts[2]);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] sizes = new int[] {3, 4, 2};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCounts(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = new int[] {3, 4, 2};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCount(-1, 0);
        mdc.getCount(-1, 1);
        mdc.getCount(-1, 2);

        mdc.getCount(3, 0);
        mdc.getCount(3, 1);
        mdc.getCount(3, 2);
    }

    @Test
    public void testGetCount() {
        int[] sizes = new int[] {3, 4, 2};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertEquals(0, mdc.getCount());
        assertEquals(3, mdc.getCount(0, 0));
        assertEquals(4, mdc.getCount(0, 1));
        assertEquals(2, mdc.getCount(0, 2));
        assertEquals(12, mdc.getCount(1, 0));
        assertEquals(13, mdc.getCount(1, 1));
        assertEquals(22, mdc.getCount(1, 2));
        assertEquals(23, mdc.getCount(1, 2) + mdc.getCount(1, 1) * size[1] + mdc.getCount(1, 0) * size[0] * size[1] * size[2]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWithWrongDimension() {
        int[] sizes = new int[] {3, 4, 2};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCount(1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithOutOfRangeValue() {
        int[] sizes = new int[] {3, 4, 2};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCount(3, 4);
    }
}