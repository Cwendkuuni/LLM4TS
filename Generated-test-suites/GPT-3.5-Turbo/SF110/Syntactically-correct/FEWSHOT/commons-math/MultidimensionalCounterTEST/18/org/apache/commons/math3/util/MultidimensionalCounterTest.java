package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.*;
import org.junit.Test;

public class MultidimensionalCounterTest {
    
    @Test
    public void testConstructor() throws NotStrictlyPositiveException {
        int[] size = {3, 4, 2};
        
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        assertEquals(3, counter.getDimension());
        assertArrayEquals(size, counter.getSizes());
        assertEquals(3 * 4 * 2, counter.getSize());
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NegativeSize() throws NotStrictlyPositiveException {
        int[] size = {-3, 4, 2};
        
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
    }
    
    @Test
    public void testIterator() {
        int[] size = {2, 3};
        
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        
        assertEquals(2 * 3, count);
    }
    
    @Test
    public void testGetCounts() throws OutOfRangeException {
        int[] size = {3, 2, 4};
        
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{0, 0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[]{0, 0, 2}, counter.getCounts(2));
        assertArrayEquals(new int[]{0, 0, 3}, counter.getCounts(3));
        assertArrayEquals(new int[]{0, 1, 0}, counter.getCounts(4));
        assertArrayEquals(new int[]{0, 1, 1}, counter.getCounts(5));
        assertArrayEquals(new int[]{0, 1, 2}, counter.getCounts(6));
        assertArrayEquals(new int[]{0, 1, 3}, counter.getCounts(7));
        assertArrayEquals(new int[]{1, 0, 0}, counter.getCounts(8));
        assertArrayEquals(new int[]{1, 0, 1}, counter.getCounts(9));
        assertArrayEquals(new int[]{1, 0, 2}, counter.getCounts(10));
        assertArrayEquals(new int[]{1, 0, 3}, counter.getCounts(11));
        assertArrayEquals(new int[]{1, 1, 0}, counter.getCounts(12));
        assertArrayEquals(new int[]{1, 1, 1}, counter.getCounts(13));
        assertArrayEquals(new int[]{1, 1, 2}, counter.getCounts(14));
        assertArrayEquals(new int[]{1, 1, 3}, counter.getCounts(15));
        assertArrayEquals(new int[]{2, 0, 0}, counter.getCounts(16));
        assertArrayEquals(new int[]{2, 0, 1}, counter.getCounts(17));
        assertArrayEquals(new int[]{2, 0, 2}, counter.getCounts(18));
        assertArrayEquals(new int[]{2, 0, 3}, counter.getCounts(19));
        assertArrayEquals(new int[]{2, 1, 0}, counter.getCounts(20));
        assertArrayEquals(new int[]{2, 1, 1}, counter.getCounts(21));
        assertArrayEquals(new int[]{2, 1, 2}, counter.getCounts(22));
        assertArrayEquals(new int[]{2, 1, 3}, counter.getCounts(23));
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_IndexOutOfRange() throws OutOfRangeException {
        int[] size = {2, 2};
        
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        counter.getCounts(5);
    }
    
    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        int[] size = {3, 2, 4};
        
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(2, counter.getCount(0, 0, 2));
        assertEquals(3, counter.getCount(0, 0, 3));
        assertEquals(4, counter.getCount(0, 1, 0));
        assertEquals(5, counter.getCount(0, 1, 1));
        assertEquals(6, counter.getCount(0, 1, 2));
        assertEquals(7, counter.getCount(0, 1, 3));
        assertEquals(8, counter.getCount(1, 0, 0));
        assertEquals(9, counter.getCount(1, 0, 1));
        assertEquals(10, counter.getCount(1, 0, 2));
        assertEquals(11, counter.getCount(1, 0, 3));
        assertEquals(12, counter.getCount(1, 1, 0));
        assertEquals(13, counter.getCount(1, 1, 1));
        assertEquals(14, counter.getCount(1, 1, 2));
        assertEquals(15, counter.getCount(1, 1, 3));
        assertEquals(16, counter.getCount(2, 0, 0));
        assertEquals(17, counter.getCount(2, 0, 1));
        assertEquals(18, counter.getCount(2, 0, 2));
        assertEquals(19, counter.getCount(2, 0, 3));
        assertEquals(20, counter.getCount(2, 1, 0));
        assertEquals(21, counter.getCount(2, 1, 1));
        assertEquals(22, counter.getCount(2, 1, 2));
        assertEquals(23, counter.getCount(2, 1, 3));
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_DimensionMismatch() throws OutOfRangeException, DimensionMismatchException {
        int[] size = {2, 2};
        
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        counter.getCount(0, 0, 0);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCount_IndexOutOfRange() throws OutOfRangeException, DimensionMismatchException {
        int[] size = {2, 2};
        
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        counter.getCount(2, 1);
    }
    
    @Test
    public void testToString() {
        int[] size = {3, 2, 4};
        
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        assertEquals("[0][0][0]", counter.toString());
    }
    
    @Test
    public void testIterator_GetCount() {
        int[] size = {2, 3};
        
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        int count = 1;
        while (iterator.hasNext()) {
            assertEquals(count, iterator.getCount());
            iterator.next();
            count++;
        }
    }
    
    @Test
    public void testIterator_GetCounts() {
        int[] size = {2, 3};
        
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        int[][] expectedCounts = {{0, 0}, {0, 1}, {0, 2}, {1, 0}, {1, 1}, {1, 2}};
        int count = 0;
        while (iterator.hasNext()) {
            assertArrayEquals(expectedCounts[count], iterator.getCounts());
            iterator.next();
            count++;
        }
    }
    
    @Test
    public void testIterator_GetCount_Dimension() {
        int[] size = {2, 3};
        
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        assertEquals(0, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        
        iterator.next();
        
        assertEquals(0, iterator.getCount(0));
        assertEquals(1, iterator.getCount(1));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_Remove() {
        int[] size = {2, 3};
        
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        iterator.remove();
    }
}