
package org.apache.commons.math.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Before;
import org.junit.Test;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Before
    public void setUp() {
        counter = new MultidimensionalCounter(2, 3, 4);
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSizes() {
        int[] expectedSizes = {2, 3, 4};
        assertArrayEquals(expectedSizes, counter.getSizes());
    }

    @Test
    public void testGetSize() {
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetCounts() {
        int[] expectedCounts = {1, 2, 3};
        assertArrayEquals(expectedCounts, counter.getCounts(23));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        counter.getCounts(24);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        counter.getCount(1, 2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        counter.getCount(1, 2, 5);
    }

    @Test
    public void testGetCount() {
        assertEquals(23, counter.getCount(1, 2, 3));
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = 0;
        while(iterator.hasNext()) {
            assertEquals(count, iterator.next().intValue());
            count++;
        }
        assertEquals(23, iterator.getCount());
        assertArrayEquals(new int[]{1, 2, 3}, iterator.getCounts());
        assertEquals(3, iterator.getCount(1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }
}
