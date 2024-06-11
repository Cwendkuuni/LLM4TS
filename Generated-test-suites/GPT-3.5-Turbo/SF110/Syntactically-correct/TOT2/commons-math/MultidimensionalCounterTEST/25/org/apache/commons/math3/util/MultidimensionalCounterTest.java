package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(size, counter.getSizes());
        assertEquals(60, counter.getSize());
    }

    @Test
    public void testGetCounts() {
        int[] size = {3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] counts = counter.getCounts(4);
        assertArrayEquals(new int[] {1, 0}, counts);
    }

    @Test
    public void testGetCount() {
        int[] size = {3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(4, counter.getCount(1, 0));
    }

    @Test
    public void testIterator() {
        int[] size = {2, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        int first = iterator.next();
        int second = iterator.next();
        int[] counts = iterator.getCounts();
        assertArrayEquals(new int[] {1, 1}, counts);
    }
}