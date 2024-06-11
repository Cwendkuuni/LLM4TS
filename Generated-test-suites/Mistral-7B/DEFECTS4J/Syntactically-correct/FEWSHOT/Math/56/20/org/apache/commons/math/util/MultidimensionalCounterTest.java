package org.apache.commons.math.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.util.MathUtils;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = { 3, 4, 5 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertEquals(3, mdc.getDimension());
        assertEquals(sizes, mdc.getSizes());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSizes() {
        int[] sizes = { -1, 3, 5 };
        new MultidimensionalCounter(sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroSizes() {
        int[] sizes = { 0, 3, 5 };
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testIterator() {
        int[] sizes = { 2, 3, 5 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator it = mdc.iterator();
        int count = 0;
        while (it.hasNext()) {
            Integer next = it.next();
            assertEquals(count, next);
            count++;
        }
        assertNull(it.next());
    }

    @Test
    public void testGetCounts() {
        int[] sizes = { 2, 3, 5 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator it = mdc.iterator();
        int[] counts = mdc.getCounts(0);
        assertArrayEquals(new int[] { 0, 0, 0 }, counts);
        int[] counts2 = mdc.getCounts(1);
        assertArrayEquals(new int[] { 0, 0, 1 }, counts2);
        int[] counts3 = mdc.getCounts(2);
        assertArrayEquals(new int[] { 0, 1, 0 }, counts3);
        int[] counts4 = mdc.getCounts(3);
        assertArrayEquals(new int[] { 1, 0, 0 }, counts4);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfBounds() {
        int[] sizes = { 2, 3, 5 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCounts(6);
    }

    @Test
    public void testGetCount() {
        int[] sizes = { 2, 3, 5 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        for (int i = 0; i < mdc.getSize(); i++) {
            int c[] = mdc.getCounts(i);
            assertEquals(i, mdc.getCount(c));
        }
        assertNull(mdc.getCount(new int[] { 3, 4, 5 }));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        int[] sizes = { 2, 3, 5 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCount(new int[] { 2, 2, 5 });
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = { 2, 3, 5 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        int c[] = new int[] { 3, 4, 6 };
        mdc.getCount(c);
    }

    @Test
    public void testSize() {
        int[] sizes = { 2, 3, 5 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertEquals(15, mdc.getSize());
    }
}