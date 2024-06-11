package org.apache.commons.math.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Iterator;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testCreateCounter() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(new int[]{2, 4, 3}, counter.getSizes());
        assertEquals(24 * 4 * 3, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testCreateCounterNegativeSizes() {
        int[] size = {-2, 4, 3};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testCreateCounterZeroSizes() {
        int[] size = {0, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(new int[]{0, 0, 0}, counter.getSizes());
        assertEquals(0, counter.getSize());
    }

    @Test
    public void testGetCounts() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int[] index = {0, 0, 0};
        int[] counts = counter.getCounts(counter.getCount(index));
        assertArrayEquals(index, counts);

        index = new int[]{1, 0, 0};
        counts = counter.getCounts(counter.getCount(index));
        assertArrayEquals(index, counts);

        index = new int[]{1, 3, 2};
        counts = counter.getCounts(counter.getCount(index));
        assertArrayEquals(index, counts);

        index = new int[]{1, 0, 1};
        assertThrows(OutOfRangeException.class, () -> counter.getCounts(counter.getCount(index)));

        index = new int[]{1, 4, 2};
        assertThrows(OutOfRangeException.class, () -> counter.getCounts(counter.getCount(index)));
    }

    @Test
    public void testGetCount() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int[] index = {0, 0, 0};
        int count = counter.getCount(index);
        assertEquals(0, count);

        index = new int[]{0, 0, 1};
        count = counter.getCount(index);
        assertEquals(1, count);

        index = new int[]{1, 0, 0};
        count = counter.getCount(index);
        assertEquals(12, count);

        index = new int[]{1, 3, 2};
        count = counter.getCount(index);
        assertEquals(23, count);

        index = new int[]{1, 0, 1};
        index = counter.getCounts(counter.getCount(index));
        assertEquals(new int[]{1, 0, 1}, index);

        index = new int[]{1, 4, 2};
        assertThrows(OutOfRangeException.class, () -> counter.getCount(index));
    }

    @Test
    public void testIterator() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator<Integer> iterator = counter.iterator();

        assertFalse(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next());

        int[] index = {0, 0, 1};
        iterator.next();
        index = counter.getCounts(iterator.next());
        assertArrayEquals(index, new int[]{0, 0, 1});

        index = new int[]{1, 0, 0};
        iterator.next();
        index = counter.getCounts(iterator.next());
        assertArrayEquals(index, new int[]{1, 0, 0});

        index = new int[]{1, 3, 2};
        iterator.next();
        index = counter.getCounts(iterator.next());
        assertArrayEquals(index, new int[]{1, 3, 2});

        assertFalse(iterator.hasNext());
    }
}