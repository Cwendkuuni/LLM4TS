
package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {
    @Test
    public void testConstructor() {
        try {
            new MultidimensionalCounter();
            fail("Expected exception NotStrictlyPositiveException not thrown");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
        try {
            new MultidimensionalCounter(1, 2, -3);
            fail("Expected exception NotStrictlyPositiveException not thrown");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        assertEquals(3, counter.getDimension());
        counter = new MultidimensionalCounter(4, 5);
        assertEquals(2, counter.getDimension());
    }
    
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] counts = counter.getCounts(0);
        assertArrayEquals(new int[]{0, 0, 0}, counts);
        counts = counter.getCounts(1);
        assertArrayEquals(new int[]{1, 0, 0}, counts);
        counts = counter.getCounts(23);
        assertArrayEquals(new int[]{1, 2, 3}, counts);
        try {
            counter.getCounts(-1);
            fail("Expected exception OutOfRangeException not thrown");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
        try {
            counter.getCounts(24);
            fail("Expected exception OutOfRangeException not thrown");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int count = counter.getCount(0, 0, 0);
        assertEquals(0, count);
        count = counter.getCount(1, 0, 0);
        assertEquals(1, count);
        count = counter.getCount(1, 2, 3);
        assertEquals(23, count);
        try {
            counter.getCount(0);
            fail("Expected exception DimensionMismatchException not thrown");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
        try {
            counter.getCount(0, 0);
            fail("Expected exception DimensionMismatchException not thrown");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
        try {
            counter.getCount(0, 0, -1);
            fail("Expected exception OutOfRangeException not thrown");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
        try {
            counter.getCount(0, 0, 4);
            fail("Expected exception OutOfRangeException not thrown");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(24, counter.getSize());
        counter = new MultidimensionalCounter(5, 5);
        assertEquals(25, counter.getSize());
    }
    
    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] sizes = counter.getSizes();
        assertArrayEquals(new int[]{2, 3, 4}, sizes);
        counter = new MultidimensionalCounter(5, 5);
        sizes = counter.getSizes();
        assertArrayEquals(new int[]{5, 5}, sizes);
    }
    
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(0), iterator.next());
        assertEquals(0, iterator.getCount());
        assertArrayEquals(new int[]{0, 0}, iterator.getCounts());
        assertEquals(0, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(1), iterator.next());
        assertEquals(1, iterator.getCount());
        assertArrayEquals(new int[]{1, 0}, iterator.getCounts());
        assertEquals(1, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(2), iterator.next());
        assertEquals(2, iterator.getCount());
        assertArrayEquals(new int[]{0, 1}, iterator.getCounts());
        assertEquals(0, iterator.getCount(0));
        assertEquals(1, iterator.getCount(1));
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(3), iterator.next());
        assertEquals(3, iterator.getCount());
        assertArrayEquals(new int[]{1, 1}, iterator.getCounts());
        assertEquals(1, iterator.getCount(0));
        assertEquals(1, iterator.getCount(1));
        assertFalse(iterator.hasNext());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }
    
    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals("[0][0][0]", counter.toString());
        counter = new MultidimensionalCounter(1, 2);
        assertEquals("[0][0]", counter.toString());
    }
}
