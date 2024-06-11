package org.apache.commons.math3.util;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.*;

public class MultidimensionalCounterTest {
    
    @Test
    public void testConstructor() {
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
            assertNotNull(counter);
            assertEquals(2, counter.getDimension());
            assertEquals(6, counter.getSize());
        } catch (NotStrictlyPositiveException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        
        int[] expectedCounts = {1, 2};
        int[] counts = counter.getCounts(5);
        
        assertArrayEquals(expectedCounts, counts);
        
        try {
            counter.getCounts(-1);
            fail("Exception should be thrown");
        } catch (OutOfRangeException e) {
            // Exception successfully thrown
        }
    }
    
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        
        int expectedCount = 5;
        int count = counter.getCount(1, 2);
        
        assertEquals(expectedCount, count);
        
        try {
            counter.getCount(3, 3);
            fail("Exception should be thrown");
        } catch (OutOfRangeException | DimensionMismatchException e) {
            // Exception successfully thrown
        }
    }
    
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        assertTrue(iterator.hasNext());
        
        int expectedNext = 0;
        int next = iterator.next();
        
        assertEquals(expectedNext, next);
        
        int[] expectedCounts = {0, 0};
        int[] counts = iterator.getCounts();
        
        assertArrayEquals(expectedCounts, counts);
    }
}