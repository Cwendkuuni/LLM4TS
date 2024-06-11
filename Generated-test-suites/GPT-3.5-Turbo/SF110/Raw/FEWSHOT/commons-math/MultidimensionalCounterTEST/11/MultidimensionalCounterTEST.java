
package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor_validSize() {
        int[] validSize = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(validSize);
        
        assertEquals(3, counter.getDimension());
        assertArrayEquals(validSize, counter.getSizes());
        assertEquals(24, counter.getSize());
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_invalidSize() {
        int[] invalidSize = {-1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(invalidSize);
    }
    
    @Test
    public void testGetCounts_withinRange() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        
        int[] counts1 = counter.getCounts(0);
        assertArrayEquals(new int[]{0,0,0}, counts1);
        
        int[] counts2 = counter.getCounts(11);
        assertArrayEquals(new int[]{1,2,3}, counts2);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_outOfRange() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        
        int[] counts = counter.getCounts(25);
    }
    
    @Test
    public void testGetCount_validC() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        
        int count1 = counter.getCount(0, 0, 0);
        assertEquals(0, count1);
        
        int count2 = counter.getCount(1, 2, 3);
        assertEquals(23, count2);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCount_invalidC() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        
        int count = counter.getCount(2, 3, 4);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_dimensionMismatch() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        
        int count = counter.getCount(1, 2);
    }
    
    @Test
    public void testIterator_hasNext() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
    }
    
    @Test
    public void testIterator_next() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = iterator.next();
        assertEquals(0, count);
        
        int[] counts = iterator.getCounts();
        assertArrayEquals(new int[]{0,0,0}, counts);
    }
    
    @Test
    public void testIterator_getCount() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = iterator.getCount();
        assertEquals(-1, count);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_remove() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }
}
