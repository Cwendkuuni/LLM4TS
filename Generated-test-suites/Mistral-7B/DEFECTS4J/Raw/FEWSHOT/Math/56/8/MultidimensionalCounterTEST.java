package org.apache.commons.math.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.junit.Test;

public class MultidimensionalCounterTest {
    @Test
    public void testMultidimensionalCounter() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(8 * 3 * 4, counter.getSize());
        assertEquals(3, counter.getDimension());
        assertArrayEquals(sizes, counter.getSizes());
    }

    @Test
    public void testGetCounts() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        int[] counts = counter.getCounts(0);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(0, counts[2]);

        counts = counter.getCounts(counter.getSize() - 1);
        assertEquals(2 - 1, counts[0]);
        assertEquals(3 - 1, counts[1]);
        assertEquals(4 - 1, counts[2]);

        try {
            counter.getCounts(-1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }

        try {
            counter.getCounts(counter.getSize() + 1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test
    public void testGetCount() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        int index = 0;
        assertEquals(index, counter.getCount(index));

        index = counter.getSize() - 1;
        assertEquals(index, counter.getCount(index));

        int[] counts = new int[sizes.length];
        for (int i = 0; i < sizes.length; i++) {
            counts[i] = sizes[i] - 1;
        }
        index = counter.getCount(counts);
        assertArrayEquals(counts, counter.iterator().getCounts());

        try {
            counter.getCount(-1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }

        try {
            counter.getCount(counter.getSize() + 1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }

        try {
            counter.getCount(new int[] {2, 3, 4});
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test
    public void testIterator() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        Iterator<Integer> iterator = counter.iterator();
        assertFalse(iterator.hasNext());

        iterator.next();
        assertTrue(iterator.hasNext());

        iterator.next();
        assertTrue(iterator.hasNext());

        for (int i = 0; i < counter.getSize(); i++) {
            iterator.next();
            assertTrue(iterator.hasNext());
        }

        assertFalse(iterator.hasNext());

        try {
            iterator.remove();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}