package org.apache.commons.math.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

import java.util.Iterator;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
        assertEquals(2 * 4 * 3, counter.getSize());
        assertEquals(sizes, counter.getSizes());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException() {
        new MultidimensionalCounter(new int[]{-1, 3, 2});
    }

    @Test
    public void testGetCounts() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] expected = {0, 0, 0};
        assertArrayEquals(expected, counter.getCounts(0));
        expected = new int[]{2, 0, 0};
        assertArrayEquals(expected, counter.getCounts(counter.getSize()));
        expected = new int[]{1, 0, 1};
        assertArrayEquals(expected, counter.getCounts(counter.getSize() / 2 + 1));
    }

    @Test
    public void testGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(0, counter.getCount(new int[0]));
        int[] expected = {0, 0, 0};
        assertArrayEquals(expected, counter.getCounts(counter.getCount(new int[0])));
        expected = new int[]{1, 1, 1};
        assertArrayEquals(expected, counter.getCounts(counter.getCount(new int[]{1, 1, 1})));
        expected = new int[]{1, 1, 2};
        assertArrayEquals(expected, counter.getCounts(counter.getCount(new int[]{1, 1, 2})));
        int[] invalidSize = new int[]{3, 4};
        assertThrows(DimensionMismatchException.class, () -> counter.getCount(invalidSize));
        int[] outOfRangeSize = new int[]{3, 5};
        assertThrows(OutOfRangeException.class, () -> counter.getCount(outOfRangeSize));
    }

    @Test
    public void testIterator() {
        int[] sizes = {3, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next());
        int[] count = {0, 0};
        while (iterator.hasNext()) {
            int i = iterator.next();
            count[iterator.getCount(0) % sizes[0]]++;
        }
        assertEquals(new int[]{2, 2}, count);
    }

    @Test
    public void testToString() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals("[0]", counter.toString());
        assertEquals("[1][0]", counter.toString().substring(0, 12));
        assertEquals("[1][1][0]", counter.toString().substring(0, 18));
        assertEquals("[1][1][1]", counter.toString().substring(0, 21));
    }
}