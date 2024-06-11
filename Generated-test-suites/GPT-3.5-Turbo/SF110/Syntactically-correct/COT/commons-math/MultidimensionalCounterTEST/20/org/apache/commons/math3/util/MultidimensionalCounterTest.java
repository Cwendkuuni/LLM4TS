
package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        int expectedDimension = 3;
        int actualDimension = counter.getDimension();
        assertEquals(expectedDimension, actualDimension);
    }
    
    @Test
    public void testGetCounts() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        int[] expectedCounts = {1, 2, 3};
        int[] actualCounts = counter.getCounts(38);
        assertArrayEquals(expectedCounts, actualCounts);
    }
    
    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        int[] c = {1, 2, 3};
        int expectedCount = 38;
        int actualCount = counter.getCount(c);
        assertEquals(expectedCount, actualCount);
    }
    
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        int expectedSize = 60;
        int actualSize = counter.getSize();
        assertEquals(expectedSize, actualSize);
    }
    
    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        int[] expectedSizes = {3, 4, 5};
        int[] actualSizes = counter.getSizes();
        assertArrayEquals(expectedSizes, actualSizes);
    }
}

