package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.*;

public class MultidimensionalCounterTest {
    
    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3);
        assertEquals(1, counter.getDimension());
    }
    
    @Test
    public void testGetDimensionWithInvalidSize() {
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(-3);
            fail("Expected exception NotStrictlyPositiveException was not thrown.");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception caught
        }
    }
    
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4);
        assertEquals(8, counter.getSize());
    }
    
    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4);
        int[] expectedSizes = {2, 4};
        assertArrayEquals(expectedSizes, counter.getSizes());
    }
    
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int[] expectedCounts = {1, 2};
        assertArrayEquals(expectedCounts, counter.getCounts(5));
    }
    
    @Test
    public void testGetCountsOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        try {
            int[] counts = counter.getCounts(6);
            fail("Expected exception OutOfRangeException was not thrown.");
        } catch (OutOfRangeException e) {
            // Expected exception caught
        }
    }
    
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertEquals(3, counter.getCount(1, 0));
    }
    
    @Test
    public void testGetCountWithInvalidDimensions() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        try {
            int count = counter.getCount(1);
            fail("Expected exception DimensionMismatchException was not thrown.");
        } catch (DimensionMismatchException e) {
            // Expected exception caught
        }
    }
    
    @Test
    public void testGetCountOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        try {
            int count = counter.getCount(1, 5);
            fail("Expected exception OutOfRangeException was not thrown.");
        } catch (OutOfRangeException e) {
            // Expected exception caught
        }
    }
    
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(3, iterator.next().intValue());
        assertFalse(iterator.hasNext());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        iterator.remove();
    }
}