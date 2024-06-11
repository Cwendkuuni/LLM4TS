package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.util.MathUtils;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;

import java.util.Iterator;

import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testNewInstance() {
        int[] sizes = { 3, 4, 5 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertEquals(3, mdc.getDimension());
        assertArrayEquals(sizes, mdc.getSizes());
        assertEquals(135, mdc.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNewInstanceNegativeSizes() {
        int[] sizes = { -3, 4, 5 };
        new MultidimensionalCounter(sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNewInstanceZeroSizes() {
        int[] sizes = { 0, 4, 5 };
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testIterator() {
        int[] sizes = { 3, 4, 5 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        Iterator<Integer> it = mdc.iterator();
        assertFalse(it.hasNext());

        int[] expected = { 0, 0, 0 };
        Integer next = it.next();
        assertArrayEquals(expected, new int[] { 0, 0, 0 });
        assertEquals(0, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        expected = new int[] { 0, 0, 1 };
        next = it.next();
        assertArrayEquals(expected, new int[] { 0, 0, 1 });
        assertEquals(1, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        expected = new int[] { 0, 1, 0 };
        next = it.next();
        assertArrayEquals(expected, new int[] { 0, 1, 0 });
        assertEquals(3, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        expected = new int[] { 0, 1, 1 };
        next = it.next();
        assertArrayEquals(expected, new int[] { 0, 1, 1 });
        assertEquals(4, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        expected = new int[] { 0, 1, 2 };
        next = it.next();
        assertArrayEquals(expected, new int[] { 0, 1, 2 });
        assertEquals(5, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        expected = new int[] { 0, 2, 0 };
        next = it.next();
        assertArrayEquals(expected, new int[] { 0, 2, 0 });
        assertEquals(12, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        expected = new int[] { 0, 2, 1 };
        next = it.next();
        assertArrayEquals(expected, new int[] { 0, 2, 1 });
        assertEquals(13, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        expected = new int[] { 1, 0, 0 };
        next = it.next();
        assertArrayEquals(expected, new int[] { 1, 0, 0 });
        assertEquals(1, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        expected = new int[] { 1, 0, 1 };
        next = it.next();
        assertArrayEquals(expected, new int[] { 1, 0, 1 });
        assertEquals(4, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        expected = new int[] { 1, 0, 2 };
        next = it.next();
        assertArrayEquals(expected, new int[] { 1, 0, 2 });
        assertEquals(12, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        expected = new int[] { 1, 1, 0 };
        next = it.next();
        assertArrayEquals(expected, new int[] { 1, 1, 0 });
        assertEquals(15, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        expected = new int[] { 1, 1, 1 };
        next = it.next();
        assertArrayEquals(expected, new int[] { 1, 1, 1 });
        assertEquals(16, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        expected = new int[] { 1, 1, 2 };
        next = it.next();
        assertArrayEquals(expected, new int[] { 1, 1, 2 });
        assertEquals(17, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        expected = new int[] { 1, 2, 0 };
        next = it.next();
        assertArrayEquals(expected, new int[] { 1, 2, 0 });
        assertEquals(15, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        expected = new int[] { 1, 2, 1 };
        next = it.next();
        assertArrayEquals(expected, new int[] { 1, 2, 1 });
        assertEquals(16, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        expected = new int[] { 1, 2, 2 };
        next = it.next();
        assertArrayEquals(expected, new int[] { 1, 2, 2 });
        assertEquals(18, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        expected = new int[] { 2, 0, 0 };
        next = it.next();
        assertArrayEquals(expected, new int[] { 2, 0, 0 });
        assertEquals(3, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        expected = new int[] { 2, 0, 1 };
        next = it.next();
        assertArrayEquals(expected, new int[] { 2, 0, 1 });
        assertEquals(3 * 4 + 1, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        expected = new int[] { 2, 0, 2 };
        next = it.next();
        assertArrayEquals(expected, new int[] { 2, 0, 2 });
        assertEquals(3 * 4 + 2, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        expected = new int[] { 2, 1, 0 };
        next = it.next();
        assertArrayEquals(expected, new int[] { 2, 1, 0 });
        assertEquals(3 * 5 + 1, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        expected = new int[] { 2, 1, 1 };
        next = it.next();
        assertArrayEquals(expected, new int[] { 2, 1, 1 });
        assertEquals(3 * 5 + 2, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        expected = new int[] { 2, 1, 2 };
        next = it.next();
        assertArrayEquals(expected, new int[] { 2, 1, 2 });
        assertEquals(3 * 5 + 3, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        expected = new int[] { 2, 2, 0 };
        next = it.next();
        assertArrayEquals(expected, new int[] { 2, 2, 0 });
        assertEquals(3 * 4 * 5, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        expected = new int[] { 2, 2, 1 };
        next = it.next();
        assertArrayEquals(expected, new int[] { 2, 2, 1 });
        assertEquals(3 * 4 * 5 + 1, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        expected = new int[] { 2, 2, 2 };
        next = it.next();
        assertArrayEquals(expected, new int[] { 2, 2, 2 });
        assertEquals(3 * 4 * 5 + 2, next.intValue());
        assertArrayEquals(expected, it.getCounts());

        assertFalse(it.hasNext());
    }

    @Test(expected = OutOfRangeException.class)
    public void testIteratorOutOfRange() {
        int[] sizes = { 3, 4, 5 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator<Integer> it = mdc.iterator();

        it.next();
        it.next();
        it.next();
        it.next();
        it.next();
        it.next();
        it.next();
        it.next();
        it.next();
        it.next();
        it.next();
        it.next();
        it.next();

        it.next();
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountNegativeDimensions() {
        MultidimensionalCounter mdc = new MultidimensionalCounter();
        mdc.getCount(-1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountTooManyDimensions() {
        int[] sizes = { 3, 4, 5, 6 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCount(6);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = { 3, 4, 5 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCount(15 * 4 * 5 + 2);
    }

    @Test
    public void testGetCount() {
        int[] sizes = { 3, 4, 5 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        int index = 0;
        int expected = 0;
        assertArrayEquals(new int[] { index }, mdc.getCounts(index));
        assertEquals(expected, mdc.getCount(index));

        index++;
        expected = 3;
        assertArrayEquals(new int[] { index }, mdc.getCounts(index));
        assertEquals(expected, mdc.getCount(index));

        index++;
        expected = 3 * 4;
        assertArrayEquals(new int[] { index }, mdc.getCounts(index));
        assertEquals(expected, mdc.getCount(index));

        index++;
        expected = 3 * 4 * 5 - 1;
        assertArrayEquals(new int[] { index }, mdc.getCounts(index));
        assertEquals(expected, mdc.getCount(index));

        index++;
        expected = 3 * 4 * 5;
        assertArrayEquals(new int[] { index }, mdc.getCounts(index));
        assertEquals(expected, mdc.getCount(index));

        index++;
        expected = 3 * 4 * 5 + 1;
        assertArrayEquals(new int[] { index }, mdc.getCounts(index));
        assertEquals(expected, mdc.getCount(index));

        index++;
        expected = 3 * 4 * 5 + 2;
        assertArrayEquals(new int[] { index }, mdc.getCounts(index));
        assertEquals(expected, mdc.getCount(index));

        index++;
        expected = 3 * 4 * 5 + 3;
        assertArrayEquals(new int[] { index }, mdc.getCounts(index));
        assertEquals(expected, mdc.getCount(index));

        index++;
        expected = 3 * 4 * 5 + 4;
        assertArrayEquals(new int[] { index }, mdc.getCounts(index));
        assertEquals(expected, mdc.getCount(index));

        index++;
        expected = 3 * 4 * 5 + 5;
        assertArrayEquals(new int[] { index }, mdc.getCounts(index));
        assertEquals(expected, mdc.getCount(index));

        index++;
        expected = 3 * 4 * 5 + 6;
        assertArrayEquals(new int[] { index }, mdc.getCounts(index));
        assertEquals(expected, mdc.getCount(index));

        index++;
        expected = 3 * 4 * 5 + 7;
        assertArrayEquals(new int[] { index }, mdc.getCounts(index));
        assertEquals(expected, mdc.getCount(index));

        index++;
        expected = 3 * 4 * 5 + 8;
        assertArrayEquals(new int[] { index }, mdc.getCounts(index));
        assertEquals(expected, mdc.getCount(index));

        index++;
        expected = 3 * 4 * 5 + 9;
        assertArrayEquals(new int[] { index }, mdc.getCounts(index));
        assertEquals(expected, mdc.getCount(index));

        index++;
        expected = 3 * 4 * 5 + 10;
        assertArrayEquals(new int[] { index }, mdc.getCounts(index));
        assertEquals(expected, mdc.getCount(index));

        index++;
        expected = -1;
        assertNull(mdc.getCount(index));

        assertFalse(mdc.iterator().hasNext());
    }
}