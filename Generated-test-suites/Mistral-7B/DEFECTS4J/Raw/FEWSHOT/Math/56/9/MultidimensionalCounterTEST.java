package org.apache.commons.math.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Iterator;

import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = new int[] { 2, 3, 4 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
        assertEquals(sizes, counter.getSizes());
        assertEquals(28 * 3 * 4, counter.getSize());
    }

    @Test
    public void testInvalidSizes() {
        assertThrows(NotStrictlyPositiveException.class, () -> new MultidimensionalCounter(new int[] { 0, 3, 4 }));
        assertThrows(NotStrictlyPositiveException.class, () -> new MultidimensionalCounter(new int[] { 2, -3, 4 }));
    }

    @Test
    public void testGetCounts() {
        int[] sizes = new int[] { 2, 3, 4 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        for (int i = 0; i < sizes[0] * sizes[1] * sizes[2]; i++) {
            int[] counts = counter.getCounts(i);
            for (int j = 0; j < sizes.length; j++) {
                assertEquals(i / (sizes[0] * sizes[1]) % sizes[j], counts[j]);
            }
        }
    }

    @Test
    public void testGetCount() {
        int[] sizes = new int[] { 2, 3, 4 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        for (int i = 0; i < sizes[0] * sizes[1] * sizes[2]; i++) {
            int count = counter.getCount(i);
            assertEquals(i, count);
        }
    }

    @Test
    public void testIteratorHasNext() {
        int[] sizes = new int[] { 2, 3, 4 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();

        for (int i = 0; i < sizes[0] * sizes[1] * sizes[2]; i++) {
            assertTrue(iterator.hasNext());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        int[] sizes = new int[] { 2, 3, 4 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();

        for (int i = 0; i < sizes[0] * sizes[1] * sizes[2]; i++) {
            Integer next = iterator.next();
            assertEquals(i, next.intValue());
        }

        assertNull(iterator.next());
    }

    @Test
    public void testGetCountsAndGetCount() {
        int[] sizes = new int[] { 2, 3, 4 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();

        for (int i = 0; i < sizes[0] * sizes[1] * sizes[2]; i++) {
            int[] counts = counter.getCounts(iterator.next());
            int count = counter.getCount(counts);
            assertArrayEquals(new int[] { i / sizes[0] % sizes[0], i / (sizes[0] * sizes[1]) % sizes[1], i / (sizes[0] * sizes[1] * sizes[2]) }, counts);
            assertEquals(i, count);
        }

        assertNull(iterator.next());
    }
}