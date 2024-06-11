package org.apache.commons.math.util;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NotStrictlyPositive() {
        new MultidimensionalCounter(-1, 2, 3);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_DimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        counter.getCount(0, 1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_OutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        counter.getCount(0, 3, 1);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        assertEquals(8, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        int[] expectedSizes = {2, 2, 2};
        int[] sizes = counter.getSizes();

        assertEquals(expectedSizes.length, sizes.length);
        for (int i = 0; i < expectedSizes.length; i++) {
            assertEquals(expectedSizes[i], sizes[i]);
        }
    }

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        assertEquals(true, iterator.hasNext());
        assertEquals(Integer.valueOf(0), iterator.next());
        assertEquals(Integer.valueOf(1), iterator.next());
        assertEquals(Integer.valueOf(2), iterator.next());
        assertEquals(Integer.valueOf(3), iterator.next());
        assertEquals(false, iterator.hasNext());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int[] expectedCounts = {0, 1};
        int[] counts = counter.getCounts(1);

        assertEquals(expectedCounts.length, counts.length);
        for (int i = 0; i < expectedCounts.length; i++) {
            assertEquals(expectedCounts[i], counts[i]);
        }
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        assertEquals(7, counter.getCount(1, 1, 1));
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        assertEquals("[0][0][0]", counter.toString());
    }
}