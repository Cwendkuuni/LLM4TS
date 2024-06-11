package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() throws Exception {
        // Test with positive size
        MultidimensionalCounter counter1 = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter1.getDimension());
        
        // Test with zero size
        try {
            MultidimensionalCounter counter2 = new MultidimensionalCounter(2, 0, 4);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Test passed
        }
    }

    @Test
    public void testIterator() throws Exception {
        // Test with different dimensions
        MultidimensionalCounter counter1 = new MultidimensionalCounter(2, 3, 4);

        MultidimensionalCounter.Iterator iter = counter1.iterator();
        for (int i = 0; i < counter1.getSize(); i++) {
            assertTrue(iter.hasNext());
            int[] counts = iter.getCounts();
            assertEquals(i, iter.getCount());
            assertEquals(counts[0], counter1.getCount(counts[0]));
            assertEquals(counts[1], counter1.getCount(counts[1]));
            assertEquals(counts[2], counter1.getCount(counts[2]));
            iter.next();
        }
        assertFalse(iter.hasNext());
    }
    
    @Test
    public void testGetDimension() throws Exception {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter.getDimension());
    }
    
    @Test
    public void testGetCounts() throws Exception {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        // Test with valid index
        int[] counts = counter.getCounts(5);
        assertEquals(1, counts[0]);
        assertEquals(2, counts[1]);
        assertEquals(1, counts[2]);

        // Test with invalid index
        try {
            counts = counter.getCounts(-1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Test passed
        }
    }
    
    @Test
    public void testGetCount() throws Exception {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        // Test with valid counts
        int[] counts = {1, 2, 1};
        int count = counter.getCount(counts);
        assertEquals(5, count);

        // Test with invalid counts
        try {
            counts = new int[]{1, -1, 1};
            count = counter.getCount(counts);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Test passed
        }

        try {
            counts = new int[]{1, 2, 4};
            count = counter.getCount(counts);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Test passed
        }

        try {
            counts = new int[]{1, 2};
            count = counter.getCount(counts);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Test passed
        }
    }
    
    @Test
    public void testGetSize() throws Exception {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(24, counter.getSize());
    }
    
    @Test
    public void testGetSizes() throws Exception {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] sizes = counter.getSizes();
        assertEquals(2, sizes[0]);
        assertEquals(3, sizes[1]);
        assertEquals(4, sizes[2]);
    }
    
    @Test
    public void testToString() throws Exception {
        MultidimensionalCounter counter1 = new MultidimensionalCounter(2, 3, 4);
        assertEquals("[0][0][0]", counter1.toString());
        
        MultidimensionalCounter counter2 = new MultidimensionalCounter(1, 2, 3);
        assertEquals("[0][0][0]", counter2.toString());
    }
    
}