package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Iterator;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Before
    public void setup() throws NotStrictlyPositiveException {
        int[] size = {2, 3};
        counter = new MultidimensionalCounter(size);
    }

    @Test
    public void testGetDimension() {
        int expectedDimension = 2;
        int actualDimension = counter.getDimension();
        assertEquals(expectedDimension, actualDimension);
    }

    @Test
    public void testGetCountsOutOfRange() {
        try {
            counter.getCounts(7);
            fail("Expected OutOfRangeException to be thrown");
        } catch (OutOfRangeException e) {
            // Exception expected, test passed
        }
    }

    @Test
    public void testGetCounts() {
        int[] expectedCounts = {1, 1};
        int[] actualCounts = counter.getCounts(3);
        assertArrayEquals(expectedCounts, actualCounts);
    }

    @Test
    public void testGetCount() {
        int[] counts = {1, 2};
        int expectedCount = 5;
        int actualCount = counter.getCount(counts);
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testGetSize() {
        int expectedSize = 6;
        int actualSize = counter.getSize();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testGetSizes() {
        int[] expectedSizes = {2, 3};
        int[] actualSizes = counter.getSizes();
        assertArrayEquals(expectedSizes, actualSizes);
    }

    @Test
    public void testIteratorHasNext() {
        Iterator<Integer> iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        Iterator<Integer> iterator = counter.iterator();
        int expectedCount1 = 1;
        int actualCount1 = iterator.next();
        assertEquals(expectedCount1, actualCount1);
        int expectedCount2 = 2;
        int actualCount2 = iterator.next();
        assertEquals(expectedCount2, actualCount2);
    }

    @Test
    public void testIteratorGetCount() {
        Iterator<Integer> iterator = counter.iterator();
        int expectedCount1 = 0;
        int actualCount1 = iterator.getCount();
        assertEquals(expectedCount1, actualCount1);
        iterator.next();
        int expectedCount2 = 1;
        int actualCount2 = iterator.getCount();
        assertEquals(expectedCount2, actualCount2);
    }

    @Test
    public void testIteratorGetCounts() {
        Iterator<Integer> iterator = counter.iterator();
        int[] expectedCounts1 = {0, 0};
        int[] actualCounts1 = iterator.getCounts();
        assertArrayEquals(expectedCounts1, actualCounts1);
        iterator.next();
        int[] expectedCounts2 = {0, 1};
        int[] actualCounts2 = iterator.getCounts();
        assertArrayEquals(expectedCounts2, actualCounts2);
    }

    @Test
    public void testIteratorGetCountByDimension() {
        Iterator<Integer> iterator = counter.iterator();
        int expectedCount1 = 0;
        int actualCount1 = iterator.getCount(0);
        assertEquals(expectedCount1, actualCount1);
        iterator.next();
        int expectedCount2 = 0;
        int actualCount2 = iterator.getCount(1);
        assertEquals(expectedCount2, actualCount2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        Iterator<Integer> iterator = counter.iterator();
        iterator.remove();
    }

}