package org.apache.commons.math.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;

public class MultidimensionalCounterTest {
    @Test
    public void testConstructor() {
        int[] sizes = new int[] { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
        assertEquals(135, counter.getSize());
        assertArrayEquals(sizes, counter.getSizes());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSizes() {
        int[] sizes = new int[] { 3, -1, 5 };
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testGetCounts() {
        int[] sizes = new int[] { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Integer index = 12;
        int[] expected = new int[] { 1, 3, 3 };
        int[] actual = counter.getCounts(index);
        assertArrayEquals(expected, actual);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] sizes = new int[] { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int index = 135;
        counter.getCounts(index);
    }

    @Test
    public void testIteratorHasNext() {
        int[] sizes = new int[] { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();
        for (int i = 0; i < 135; i++) {
            assertTrue(iterator.hasNext());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        int[] sizes = new int[] { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            int actual = iterator.next();
            int expected = index;
            assertEquals(expected, actual);
            index++;
        }
    }

    @Test
    public void testGetCount() {
        int[] sizes = new int[] { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] c = new int[] { 1, 2, 3 };
        int expected = 41;
        int actual = counter.getCount(c);
        assertEquals(expected, actual);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWrongDimension() {
        int[] sizes = new int[] { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] c = new int[] { 1, 2 };
        counter.getCount(c);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = new int[] { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] c = new int[] { 6, 2, 3 };
        counter.getCount(c);
    }
}