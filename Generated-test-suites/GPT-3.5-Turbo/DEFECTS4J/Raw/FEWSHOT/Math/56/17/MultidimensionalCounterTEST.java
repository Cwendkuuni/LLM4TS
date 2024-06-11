
package org.apache.commons.math.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;

public class MultidimensionalCounterTest {
    @Test
    public void testConstructor() {
        try {
            new MultidimensionalCounter();
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }

        try {
            new MultidimensionalCounter(2, -1);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }

        MultidimensionalCounter mdc = new MultidimensionalCounter(2);
        assertEquals(1, mdc.getSize());
        assertEquals(1, mdc.getDimension());
        assertArrayEquals(new int[]{2}, mdc.getSizes());

        mdc = new MultidimensionalCounter(2, 3);
        assertEquals(6, mdc.getSize());
        assertEquals(2, mdc.getDimension());
        assertArrayEquals(new int[]{2, 3}, mdc.getSizes());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(2, 3);
        assertArrayEquals(new int[]{0, 0}, mdc.getCounts(0));
        assertArrayEquals(new int[]{0, 1}, mdc.getCounts(1));
        assertArrayEquals(new int[]{0, 2}, mdc.getCounts(2));
        assertArrayEquals(new int[]{1, 0}, mdc.getCounts(3));
        assertArrayEquals(new int[]{1, 1}, mdc.getCounts(4));
        assertArrayEquals(new int[]{1, 2}, mdc.getCounts(5));

        try {
            mdc.getCounts(-1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }

        try {
            mdc.getCounts(6);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(2, 3);
        assertEquals(0, mdc.getCount(0, 0));
        assertEquals(1, mdc.getCount(0, 1));
        assertEquals(2, mdc.getCount(0, 2));
        assertEquals(3, mdc.getCount(1, 0));
        assertEquals(4, mdc.getCount(1, 1));
        assertEquals(5, mdc.getCount(1, 2));

        try {
            mdc.getCount(-1, 0);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }

        try {
            mdc.getCount(0, -1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }

        try {
            mdc.getCount(2, 0);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }

        try {
            mdc.getCount(0, 3);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iter = mdc.iterator();

        assertEquals(0, iter.getCount());
        assertArrayEquals(new int[]{0, 0}, iter.getCounts());
        assertEquals(0, iter.getCount(0));
        assertEquals(0, iter.getCount(1));

        assertEquals(1, iter.next().intValue());
        assertEquals(1, iter.getCount());
        assertArrayEquals(new int[]{0, 1}, iter.getCounts());
        assertEquals(0, iter.getCount(0));
        assertEquals(1, iter.getCount(1));

        assertEquals(2, iter.next().intValue());
        assertEquals(2, iter.getCount());
        assertArrayEquals(new int[]{0, 2}, iter.getCounts());
        assertEquals(0, iter.getCount(0));
        assertEquals(2, iter.getCount(1));

        assertEquals(3, iter.next().intValue());
        assertEquals(3, iter.getCount());
        assertArrayEquals(new int[]{1, 0}, iter.getCounts());
        assertEquals(1, iter.getCount(0));
        assertEquals(0, iter.getCount(1));

        assertEquals(4, iter.next().intValue());
        assertEquals(4, iter.getCount());
        assertArrayEquals(new int[]{1, 1}, iter.getCounts());
        assertEquals(1, iter.getCount(0));
        assertEquals(1, iter.getCount(1));

        assertEquals(5, iter.next().intValue());
        assertEquals(5, iter.getCount());
        assertArrayEquals(new int[]{1, 2}, iter.getCounts());
        assertEquals(1, iter.getCount(0));
        assertEquals(2, iter.getCount(1));

        assertEquals(false, iter.hasNext());
        assertEquals(null, iter.next());
    }
}
