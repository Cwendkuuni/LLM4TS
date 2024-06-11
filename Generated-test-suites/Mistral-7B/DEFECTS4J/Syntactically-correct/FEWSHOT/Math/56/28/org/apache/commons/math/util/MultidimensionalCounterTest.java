package org.apache.commons.math.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testNewInstance() {
        int[] size = { 2, 4, 3 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertEquals(3, mdc.getDimension());
        assertEquals(81, mdc.getSize());
        assertArrayEquals(size, mdc.getSizes());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNewInstanceNegativeSizes() {
        int[] size = { -1, 4, 3 };
        new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNewInstanceZeroSize() {
        int[] size = { 2, 0, 3 };
        new MultidimensionalCounter(size);
    }

    @Test
    public void testNewInstanceSingleDimension() {
        int[] size = { 5 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertEquals(1, mdc.getDimension());
        assertEquals(5, mdc.getSize());
        assertArrayEquals(size, mdc.getSizes());
    }

    @Test
    public void testNewInstanceZeroDimensions() {
        MultidimensionalCounter mdc = new MultidimensionalCounter();
        assertEquals(0, mdc.getDimension());
        assertEquals(0, mdc.getSize());
        assertArrayEquals(new int[0], mdc.getSizes());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] size = { 2, 4, 3 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int index = mdc.getSize() + 1;
        mdc.getCounts(index);
    }

    @Test
    public void testGetCounts() {
        int[] size = { 2, 4, 3 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int[] expected = { 0, 0, 1 };
        int[] result = mdc.getCounts(1);
        assertArrayEquals(expected, result);

        expected = new int[] { 1, 0, 0 };
        result = mdc.getCounts(3);
        assertArrayEquals(expected, result);

        expected = new int[] { 1, 2, 2 };
        result = mdc.getCounts(23);
        assertArrayEquals(expected, result);

        expected = new int[] { 1, 3, 2 };
        result = mdc.getCounts(27);
        assertArrayEquals(expected, result);

        expected = new int[] { 2, 3, 0 };
        result = mdc.getCounts(55);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetCount() {
        int[] size = { 2, 4, 3 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int result;

        result = mdc.getCount(new int[] { 0, 0, 0 });
        assertEquals(0, result);

        result = mdc.getCount(new int[] { 0, 0, 1 });
        assertEquals(1, result);

        result = mdc.getCount(new int[] { 0, 0, 2 });
        assertEquals(2, result);

        result = mdc.getCount(new int[] { 0, 1, 0 });
        assertEquals(3, result);

        result = mdc.getCount(new int[] { 1, 0, 0 });
        assertEquals(12, result);

        result = mdc.getCount(new int[] { 1, 3, 2 });
        assertEquals(23, result);
    }

    @Test
    public void testIterator() {
        int[] size = { 2, 4, 3 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator it = mdc.iterator();

        List<Integer> expected = new ArrayList<>();
        for (int i = 0; i < mdc.getSize(); i++) {
            expected.add(it.next());
        }

        List<Integer> result = new ArrayList<>();
        while (it.hasNext()) {
            result.add(it.next());
        }
        assertArrayEquals(expected.stream().mapToInt(i -> i).toArray(), result.stream().mapToInt(i -> i).toArray());
    }

    @Test
    public void testIteratorGetCountsAndGetCount() {
        int[] size = { 2, 4, 3 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator it = mdc.iterator();

        int[] currentCounts;
        int currentCount;

        currentCounts = it.getCounts();
        assertArrayEquals(new int[] { 0, 0, 0 }, currentCounts);
        currentCount = it.getCount();
        assertEquals(0, currentCount);

        it.next();

        currentCounts = it.getCounts();
        assertArrayEquals(new int[] { 0, 0, 1 }, currentCounts);
        currentCount = it.getCount();
        assertEquals(1, currentCount);

        it.next();

        currentCounts = it.getCounts();
        assertArrayEquals(new int[] { 0, 0, 2 }, currentCounts);
        currentCount = it.getCount();
        assertEquals(2, currentCount);

        // Test with multidimensional counts
        it = mdc.iterator();
        it.next(); // Skip the first one (0, 0, 0)
        currentCounts = it.getCounts();
        assertArrayEquals(new int[] { 0, 1, 0 }, currentCounts);
        currentCount = it.getCount();
        assertEquals(3, currentCount);

        it.next(); // Skip the next one (0, 1, 1)
        currentCounts = it.getCounts();
        assertArrayEquals(new int[] { 0, 1, 2 }, currentCounts);
        currentCount = it.getCount();
        assertEquals(13, currentCount);

        it.next(); // Skip the next one (0, 2, 0)
        currentCounts = it.getCounts();
        assertArrayEquals(new int[] { 0, 2, 1 }, currentCounts);
        currentCount = it.getCount();
        assertEquals(14, currentCount);

        it.next(); // Skip the next one (0, 2, 2)
        currentCounts = it.getCounts();
        assertArrayEquals(new int[] { 1, 0, 0 }, currentCounts);
        currentCount = it.getCount();
        assertEquals(12, currentCount);

        it.next(); // Skip the next one (1, 0, 1)
        currentCounts = it.getCounts();
        assertArrayEquals(new int[] { 1, 0, 2 }, currentCounts);
        currentCount = it.getCount();
        assertEquals(17, currentCount);

        it.next(); // Skip the next one (1, 1, 0)
        currentCounts = it.getCounts();
        assertArrayEquals(new int[] { 1, 1, 1 }, currentCounts);
        currentCount = it.getCount();
        assertEquals(21, currentCount);

        it.next(); // Skip the next one (1, 1, 2)
        currentCounts = it.getCounts();
        assertArrayEquals(new int[] { 1, 2, 0 }, currentCounts);
        currentCount = it.getCount();
        assertEquals(25, currentCount);

        it.next(); // Skip the next one (1, 2, 1)
        currentCounts = it.getCounts();
        assertArrayEquals(new int[] { 1, 2, 2 }, currentCounts);
        currentCount = it.getCount();
        assertEquals(31, currentCount);

        it.next(); // Skip the next one (2, 0, 0)
        currentCounts = it.getCounts();
        assertArrayEquals(new int[] { 2, 0, 1 }, currentCounts);
        currentCount = it.getCount();
        assertEquals(34, currentCount);

        it.next(); // Skip the next one (2, 0, 2)
        currentCounts = it.getCounts();
        assertArrayEquals(new int[] { 2, 1, 0 }, currentCounts);
        currentCount = it.getCount();
        assertEquals(42, currentCount);

        it.next(); // Skip the next one (2, 1, 1)
        currentCounts = it.getCounts();
        assertArrayEquals(new int[] { 2, 1, 2 }, currentCounts);
        currentCount = it.getCount();
        assertEquals(51, currentCount);

        it.next(); // Skip the next one (2, 2, 0)
        currentCounts = it.getCounts();
        assertArrayEquals(new int[] { 2, 2, 1 }, currentCounts);
        currentCount = it.getCount();
        assertEquals(56, currentCount);

        it.next(); // Skip the next one (2, 2, 2)
        currentCounts = it.getCounts();
        assertArrayEquals(new int[] { 3, 0, 0 }, currentCounts);
        currentCount = it.getCount();
        assertEquals(63, currentCount);
    }

    @Test
    public void testToString() {
        int[] size = { 2, 4, 3 };
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator<Integer> it = mdc.iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            Integer next = it.next();
            sb.append(next);
        }
        assertEquals("[0][1][2]", sb.toString());
    }
}