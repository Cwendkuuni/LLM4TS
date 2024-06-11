package org.apache.commons.math3.util;
import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;

public class MultidimensionalCounterTest {
    
    // Test case for constructor
    @Test
    public void testConstructor() {
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
            assertNotNull(counter);
        } catch (NotStrictlyPositiveException e) {
            fail("Constructor should not throw NotStrictlyPositiveException");
        }
    }
    
    // Test case for getCounts method
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        try {
            int[] counts = counter.getCounts(4);
            assertArrayEquals(new int[]{0, 1}, counts);
        } catch (OutOfRangeException e) {
            fail("getCounts() should not throw OutOfRangeException");
        }
    }

    // Test case for getCount method
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        try {
            int count = counter.getCount(1, 2);
            assertEquals(5, count);
        } catch (OutOfRangeException | DimensionMismatchException e) {
            fail("getCount() should not throw OutOfRangeException or DimensionMismatchException");
        }
    }
    
    // Test case for Iterator's hasNext method
    @Test
    public void testIteratorHasNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
    }
    
    // Test case for Iterator's next method
    @Test
    public void testIteratorNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(Integer.valueOf(0), iterator.next());
        assertEquals(0, iterator.getCount());
    }
    
    // Test case for Iterator's remove method
    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }

}