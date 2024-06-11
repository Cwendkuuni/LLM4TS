package org.apache.commons.math.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructorWithOneDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(5);
        assertEquals(5, counter.getDimension());
        assertEquals(5, counter.getSize());
        assertEquals(5, counter.getSizes()[0]);
        Iterator<Integer> iterator = counter.iterator();
        assertEquals(5, iterator.next());
        assertNull(iterator.next());
    }

    @Test
    public void testConstructorWithTwoDimensions() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 7);
        assertEquals(2, counter.getDimension());
        assertEquals(21, counter.getSize());
        assertEquals(3, counter.getSizes()[0]);
        assertEquals(7, counter.getSizes()[1]);
        Iterator<Integer> iterator = counter.iterator();
        assertEquals(0, iterator.next());
        assertEquals(1, iterator.next());
        assertEquals(2, iterator.next());
        assertEquals(0, iterator.next()[0]);
        assertEquals(0, iterator.next()[1]);
        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testConstructorWithThreeDimensions() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 5);
        assertEquals(3, counter.getDimension());
        assertEquals(40, counter.getSize());
        assertEquals(2, counter.getSizes()[0]);
        assertEquals(4, counter.getSizes()[1]);
        assertEquals(5, counter.getSizes()[2]);
        Iterator<Integer> iterator = counter.iterator();
        assertEquals(0, iterator.next());
        assertEquals(0, iterator.next()[0]);
        assertEquals(0, iterator.next()[1]);
        assertEquals(0, iterator.next()[2]);
        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeSizes() {
        new MultidimensionalCounter(-1, 5, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroSizes() {
        new MultidimensionalCounter(1, 0, 3);
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        int[] counts = counter.getCounts(23);
        assertArrayEquals(new int[]{1, 3, 4}, counts);
        counts = counter.getCounts(5);
        assertArrayEquals(new int[]{0, 0, 0}, counts);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithWrongNumberOfDimensions() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        counter.getCount(6);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithOutOfRangeIndices() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        counter.getCount(3, 5);
    }

    @Test
    public void testGetCountWithValidIndices() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        int index = counter.getCount(1, 2);
        assertEquals(12, index);
    }

    @Test
    public void testGetCountWithLastDimensionZero() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 0);
        int index = counter.getCount(1, 2, 0);
        assertEquals(3, index);
    }

    @Test
    public void testGetCountWithLastDimensionNonZero() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        int index = counter.getCount(1, 2, 4);
        assertEquals(24, index);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        Iterator<Integer> iterator = counter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            assertEquals(Precision.round(count++ / (double) (3 * 4 * 5), 2), next.doubleValue());
            assertArrayEquals(iterator.getCounts(), counter.getCounts(next));
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        Iterator<Integer> iterator = counter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            String expectedToString = "[";
            for (int i = 0; i < counter.getDimension(); i++) {
                if (i > 0) {
                    expectedToString += ", ";
                }
                expectedToString += counter.getCount(i);
            }
            expectedToString += "]";
            assertEquals(expectedToString, counter.toString());
            assertEquals(expectedToString, next.toString());
            ++count;
        }
    }
}