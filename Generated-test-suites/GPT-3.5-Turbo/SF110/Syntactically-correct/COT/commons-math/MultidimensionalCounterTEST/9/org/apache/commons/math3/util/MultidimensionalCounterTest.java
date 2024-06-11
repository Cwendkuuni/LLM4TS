
package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.*;
import java.util.*;

public class MultidimensionalCounterTest {

    // Test the constructor
    @Test
    public void testConstructor() {
        int[] size = {3, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        assertEquals(3, counter.getDimension());
        assertArrayEquals(new int[] {3, 4, 2}, counter.getSizes());
    }
    
    // Test the iterator
    @Test
    public void testIterator() {
        int[] size = {3, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        Iterator<Integer> iterator = counter.iterator();
        
        // Test hasNext() and next() methods
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next().intValue());
        // ...
        
        // Test getCount() method
        assertEquals(0, iterator.getCount());
        
        // Test getCounts() method
        assertArrayEquals(new int[] {0, 0, 0}, iterator.getCounts());
        
        // Test getCount(dim) method
        assertEquals(0, iterator.getCount(0));
    }
    
    // Test getCounts() method
    @Test
    public void testGetCounts() {
        int[] size = {3, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int index = 10;
        int[] expectedCounts = {0, 2, 1};
        assertArrayEquals(expectedCounts, counter.getCounts(index));
    }
    
    // Test getCount() method
    @Test
    public void testGetCount() {
        int[] size = {3, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int[] c = {1, 2, 0};
        int expectedCount = 10;
        assertEquals(expectedCount, counter.getCount(c));
    }
    
    // Test getSize() method
    @Test
    public void testGetSize() {
        int[] size = {3, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int expectedSize = 24;
        assertEquals(expectedSize, counter.getSize());
    }
    
    // Test getSizes() method
    @Test
    public void testGetSizes() {
        int[] size = {3, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        assertArrayEquals(size, counter.getSizes());
    }
    
    // Test toString() method
    @Test
    public void testToString() {
        int[] size = {3, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        String expectedString = "[0][0][0]";
        assertEquals(expectedString, counter.toString());
    }
}
