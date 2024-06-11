package org.apache.commons.math3.util;
import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
            assertEquals(4, counter.getDimension());
            assertEquals(24, counter.getSize());
            assertArrayEquals(new int[] {2, 3, 4}, counter.getSizes());
        } catch (Exception e) {
            fail("Exception thrown during Constructor test: " + e.getMessage());
        }
    }

    @Test
    public void testGetCounts() {
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
            assertArrayEquals(new int[] {0, 0, 0}, counter.getCounts(0));
            assertArrayEquals(new int[] {1, 2, 3}, counter.getCounts(17));
        } catch (Exception e) {
            fail("Exception thrown during getCounts test: " + e.getMessage());
        }
    }

    @Test
    public void testGetCount() {
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
            assertEquals(0, counter.getCount(0, 0, 0));
            assertEquals(17, counter.getCount(1, 2, 3));
        } catch (Exception e) {
            fail("Exception thrown during getCount test: " + e.getMessage());
        }
    }

    @Test
    public void testIterator() {
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
            MultidimensionalCounter.Iterator iterator = counter.iterator();
            assertTrue(iterator.hasNext());
            assertEquals(0, iterator.next().intValue());
            assertEquals(1, iterator.getCount());
            assertArrayEquals(new int[] {0, 0, 1}, iterator.getCounts());
        } catch (Exception e) {
            fail("Exception thrown during Iterator test: " + e.getMessage());
        }
    }
    
}