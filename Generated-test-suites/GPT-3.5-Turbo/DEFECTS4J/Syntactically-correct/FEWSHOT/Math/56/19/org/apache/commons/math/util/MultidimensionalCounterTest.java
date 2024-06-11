
package org.apache.commons.math.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Before;
import org.junit.Test;

public class MultidimensionalCounterTest {
    private MultidimensionalCounter counter;

    @Before
    public void setUp() {
        counter = new MultidimensionalCounter(2, 4, 3);
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, counter.getDimension());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidSizes() {
        new MultidimensionalCounter(-2, 4, 3);
    }

    @Test
    public void testGetSize() {
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        int[] expectedSizes = { 2, 4, 3 };
        assertArrayEquals(expectedSizes, counter.getSizes());
    }

    @Test
    public void testGetCountsWithValidIndex() {
        int[] expectedCounts = { 0, 0, 2 };
        assertArrayEquals(expectedCounts, counter.getCounts(2));

        expectedCounts = new int[] { 1, 0, 2 };
        assertArrayEquals(expectedCounts, counter.getCounts(14));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsWithInvalidIndex() {
        counter.getCounts(25);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithInvalidDimension() {
        counter.getCounts(2, 3);
    }

    @Test
    public void testGetCountsWithValidIndices() {
        assertEquals(2, counter.getCount(0, 0, 2));
        assertEquals(14, counter.getCount(1, 0, 2));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsWithInvalidIndices() {
        counter.getCount(2, 0, 2);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithInvalidIndicesDimension() {
        counter.getCount(0, 0);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next().intValue());
        assertEquals(1, iterator.getDimension());
        assertEquals(0, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        assertEquals(0, iterator.getCount(2));

        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next().intValue());
        assertEquals(1, iterator.getDimension());
        assertEquals(0, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        assertEquals(1, iterator.getCount(2));

        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next().intValue());
        assertEquals(2, iterator.getDimension());
        assertEquals(0, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        assertEquals(2, iterator.getCount(2));

        // Other calls removed for brevity
    }
}
