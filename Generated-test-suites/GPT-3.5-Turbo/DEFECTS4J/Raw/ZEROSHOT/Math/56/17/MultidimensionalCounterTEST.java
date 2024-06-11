package org.apache.commons.math.util;

import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {
    
    // Test the constructor
    @Test
    public void testConstructor() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        Assert.assertEquals(3, counter.getDimension());
        Assert.assertArrayEquals(new int[]{2, 4, 3}, counter.getSizes());
        Assert.assertEquals(24, counter.getSize());
        
        MultidimensionalCounter counter2 = new MultidimensionalCounter(1);
        Assert.assertEquals(1, counter2.getDimension());
        Assert.assertArrayEquals(new int[]{1}, counter2.getSizes());
        Assert.assertEquals(1, counter2.getSize());
    }
    
    // Test the iterator
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        int[] counts = iterator.getCounts();
        Assert.assertArrayEquals(new int[]{0, 0, 0}, counts);
        
        Assert.assertTrue(iterator.hasNext());
        
        for (int i = 1; i < counter.getSize(); i++) {
            iterator.next();
        }
        
        counts = iterator.getCounts();
        Assert.assertArrayEquals(new int[]{1, 3, 2}, counts);
        
        Assert.assertFalse(iterator.hasNext());
    }
    
    // Test the getCounts method
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        
        int[] counts = counter.getCounts(0);
        Assert.assertArrayEquals(new int[]{0, 0, 0}, counts);
        
        counts = counter.getCounts(23);
        Assert.assertArrayEquals(new int[]{1, 3, 2}, counts);
    }
    
    // Test the getCount method
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        
        int count = counter.getCount(0, 0, 0);
        Assert.assertEquals(0, count);
        
        count = counter.getCount(1, 3, 2);
        Assert.assertEquals(23, count);
    }
}