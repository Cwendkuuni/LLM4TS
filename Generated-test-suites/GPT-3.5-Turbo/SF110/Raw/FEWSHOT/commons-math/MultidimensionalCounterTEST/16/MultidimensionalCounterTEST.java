
package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;

public class MultidimensionalCounterTest {
    
    private final int[] size = {3, 2, 4};
    private final int totalSize = 3 * 2 * 4;
    
    @Test
    public void testConstructor() {
        MultidimensionalCounter counter;
        
        // Test constructor without exception
        counter = new MultidimensionalCounter(size);
        assertEquals(size.length, counter.getDimension());
        assertArrayEquals(size, counter.getSizes());
        assertEquals(totalSize, counter.getSize());
        
        // Test constructor with NotStrictlyPositiveException
        try {
            counter = new MultidimensionalCounter(-1, 2, 4);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Exception is expected
        }
    }
    
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        // Test valid indices
        try {
            assertArrayEquals(new int[] {0, 0, 0}, counter.getCounts(0));
            assertArrayEquals(new int[] {2, 0, 0}, counter.getCounts(2));
            assertArrayEquals(new int[] {2, 1, 3}, counter.getCounts(totalSize - 1));
        } catch (OutOfRangeException e) {
            fail("Unexpected OutOfRangeException");
        }
        
        // Test invalid indices
        try {
            counter.getCounts(totalSize); // Index out of range
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Exception is expected
        }
        try {
            counter.getCounts(-1); // Index out of range
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Exception is expected
        }
    }
    
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        // Test valid counts
        try {
            assertEquals(0, counter.getCount(0, 0, 0));
            assertEquals(2, counter.getCount(2, 0, 0));
            assertEquals(totalSize - 1, counter.getCount(2, 1, 3));
        } catch (OutOfRangeException | DimensionMismatchException e) {
            fail("Unexpected exception");
        }
        
        // Test invalid counts
        try {
            counter.getCount(3, 0, 0); // Index out of range
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Exception is expected
        } catch (DimensionMismatchException e) {
            fail("Unexpected DimensionMismatchException");
        }
        try {
            counter.getCount(0, 0, -1); // Index out of range
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Exception is expected
        } catch (DimensionMismatchException e) {
            fail("Unexpected DimensionMismatchException");
        }
        try {
            counter.getCount(0, 0); // Dimension mismatch
            fail("Expected DimensionMismatchException");
        } catch (OutOfRangeException e) {
            fail("Unexpected OutOfRangeException");
        } catch (DimensionMismatchException e) {
            // Exception is expected
        }
    }
    
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        int count = 0;
        for (Integer num : counter) {
            assertEquals(count, num.intValue());
            
            // Verify counts
            assertArrayEquals(counter.getCounts(count), counter.iterator().getCounts());
            
            count++;
        }
        
        assertEquals(totalSize, count);
    }
    
    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        String expected = "[0][0][0]";
        assertEquals(expected, counter.toString());
        
        expected = "[0][0][1]";
        assertEquals(expected, counter.iterator().next().toString());
        
        expected = "[0][0][3]";
        for (int i = 0; i < totalSize - 1; i++) {
            counter.iterator().next();
        }
        assertEquals(expected, counter.iterator().next().toString());
    }
    
    @Test
    public void testIteratorCounts() {
        MultidimensionalCounter.Iterator iterator = new MultidimensionalCounter(size).iterator();
        
        assertEquals(-1, iterator.getCount());
        
        // Test increment
        int[] expectedCounts = new int[] {0, 0, 0};
        for (int i = 0; i < totalSize; i++) {
            assertTrue(iterator.hasNext());
            int[] counts = iterator.getCounts();
            assertArrayEquals(expectedCounts, counts);
            int dimCount = iterator.getCount(0);
            assertEquals(expectedCounts[0], dimCount);
            
            // Test random access getCount(dim)
            for (int j = 1; j < size.length; j++) {
                dimCount = iterator.getCount(j);
                assertEquals(expectedCounts[j], dimCount);
            }
            
            iterator.next();
            incrementCounts(expectedCounts);
        }
        
        assertEquals(totalSize, iterator.getCount());
        assertTrue(iterator.hasNext() == false);
    }
    
    private void incrementCounts(int[] counts) {
        for (int i = counts.length - 1; i >= 0; i--) {
            if (counts[i] != size[i] - 1) {
                counts[i]++;
                break;
            }
            counts[i] = 0;
        }
    }
}
