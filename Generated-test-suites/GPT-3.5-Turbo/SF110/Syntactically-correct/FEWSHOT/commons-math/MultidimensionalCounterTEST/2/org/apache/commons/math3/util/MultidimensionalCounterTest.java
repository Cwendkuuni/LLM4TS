
package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultidimensionalCounterTest {
    
    private MultidimensionalCounter counter;
    
    @Before
    public void setUp() throws Exception {
        counter = new MultidimensionalCounter(2, 3, 4);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_ThrowsNotStrictlyPositiveException() throws NotStrictlyPositiveException {
        new MultidimensionalCounter(0);
    }
    
    @Test
    public void testGetDimension() {
        assertEquals(3, counter.getDimension());
    }
    
    @Test
    public void testGetSize() {
        assertEquals(24, counter.getSize());
    }
    
    @Test
    public void testGetSizes() {
        int[] expectedSizes = {2, 3, 4};
        assertArrayEquals(expectedSizes, counter.getSizes());
    }
    
    @Test
    public void testToString() {
        String expected = "[0][0][0]";
        assertEquals(expected, counter.toString());
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_ThrowsOutOfRangeException_IndexLessThanZero() throws OutOfRangeException {
        counter.getCounts(-1);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_ThrowsOutOfRangeException_IndexGreaterThanTotalSize() throws OutOfRangeException {
        counter.getCounts(25);
    }
    
    @Test
    public void testGetCounts() throws OutOfRangeException {
        int[] expectedCounts = {0, 0, 0};
        assertArrayEquals(expectedCounts, counter.getCounts(0));
        
        expectedCounts = new int[]{1, 0, 0};
        assertArrayEquals(expectedCounts, counter.getCounts(6));
        
        expectedCounts = new int[]{1, 2, 3};
        assertArrayEquals(expectedCounts, counter.getCounts(17));
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_ThrowsDimensionMismatchException() throws DimensionMismatchException, OutOfRangeException {
        counter.getCount(1, 2);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCount_ThrowsOutOfRangeException() throws DimensionMismatchException, OutOfRangeException {
        counter.getCount(1, 4, 3);
    }
    
    @Test
    public void testGetCount() throws DimensionMismatchException, OutOfRangeException {
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(2, counter.getCount(0, 0, 2));
        assertEquals(3, counter.getCount(0, 0, 3));
        assertEquals(4, counter.getCount(0, 1, 0));
        assertEquals(5, counter.getCount(0, 1, 1));
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(-1, iterator.getCount());
        assertArrayEquals(new int[]{0, 0, -1}, iterator.getCounts());
        assertTrue(iterator.hasNext());
        
        iterator.next();
        assertEquals(0, iterator.getCount());
        assertArrayEquals(new int[]{0, 0, 0}, iterator.getCounts());
        assertTrue(iterator.hasNext());
        
        iterator.next();
        assertEquals(1, iterator.getCount());
        assertArrayEquals(new int[]{0, 0, 1}, iterator.getCounts());
        assertTrue(iterator.hasNext());
        
        // ...
        
        iterator.next();
        assertEquals(23, iterator.getCount());
        assertArrayEquals(new int[]{1, 2, 3}, iterator.getCounts());
        assertFalse(iterator.hasNext());
    }
}
