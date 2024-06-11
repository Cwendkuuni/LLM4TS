package org.apache.commons.math.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for MultidimensionalCounter class.
 */
public class MultidimensionalCounterTest {

    /**
     * Test constructor with valid sizes.
     */
    @Test
    public void testConstructorValidSizes() {
        int[] sizes = new int[] { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int expectedSize = 1 * 3 * 4 * 5;
        assertEquals("Unexpected size", expectedSize, counter.getSize());
    }

    /**
     * Test constructor with negative sizes.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSizes() {
        int[] sizes = new int[] { -3, 4, 5 };
        new MultidimensionalCounter(sizes);
    }

    /**
     * Test iterator hasNext() method with valid counts.
     */
    @Test
    public void testIteratorHasNextValidCounts() {
        int[] sizes = new int[] { 2, 3, 4 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        for (int i = 0; i < sizes[0] * sizes[1] * sizes[2]; i++) {
            assertTrue("Iterator should have next", iterator.hasNext());
        }
        assertFalse("Iterator should not have next", iterator.hasNext());
    }

    /**
     * Test iterator next() method with valid counts.
     */
    @Test
    public void testIteratorNextValidCounts() {
        int[] sizes = new int[] { 2, 3, 4 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            int unidimensionalIndex = iterator.next();
            int[] multidimensionalIndices = iterator.getCounts();
            assertEquals("Unexpected unidimensional index", count, unidimensionalIndex);
            assertEquals("Unexpected multidimensional indices", new int[] { count % sizes[2], (count / sizes[2]) % sizes[1], count / (sizes[2] * sizes[1]) }, multidimensionalIndices);
            count++;
        }
    }

    /**
     * Test iterator next() method when all counters reach their maximum.
     */
    @Test
    public void testIteratorNextMaxCounters() {
        int[] sizes = new int[] { 2, 2, 2 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        int expectedSize = sizes[0] * sizes[1] * sizes[2];
        assertEquals("Unexpected size", expectedSize, counter.getSize());
        assertFalse("Iterator should not have next", iterator.hasNext());
    }

    /**
     * Test getCounts() method with valid multidimensional index.
     */
    @Test
    public void testGetCountsValidIndex() {
        int[] sizes = new int[] { 2, 3, 4 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] multidimensionalIndex = new int[] { 1, 1, 1 };
        int[] expectedMultidimensionalIndex = new int[] { 1, 1, 1 };
        int[] actualMultidimensionalIndex = counter.getCounts(counter.getCount(multidimensionalIndex));
        assertArrayEquals("Unexpected multidimensional indices", expectedMultidimensionalIndex, actualMultidimensionalIndex);
    }

    /**
     * Test getCounts() method with invalid multidimensional index.
     */
    @Test(expected = OutOfRangeException.class)
    public void testGetCountsInvalidIndex() {
        int[] sizes = new int[] { 2, 3, 4 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] invalidMultidimensionalIndex = new int[] { 1, 1, 5 };
        counter.getCounts(counter.getCount(invalidMultidimensionalIndex));
    }

    /**
     * Test getCount() method with valid unidimensional index.
     */
    @Test
    public void testGetCountValidIndex() {
        int[] sizes = new int[] { 2, 3, 4 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] multidimensionalIndex = new int[] { 1, 1, 1 };
        int expectedUnidimensionalIndex = counter.getCount(multidimensionalIndex);
        int actualUnidimensionalIndex = counter.getCount(multidimensionalIndex);
        assertEquals("Unexpected unidimensional index", expectedUnidimensionalIndex, actualUnidimensionalIndex);
    }

    /**
     * Test getCount() method with invalid unidimensional index.
     */
    @Test(expected = OutOfRangeException.class)
    public void testGetCountInvalidIndex() {
        int[] sizes = new int[] { 2, 3, 4 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int invalidUnidimensionalIndex = sizes[0] * sizes[1] * sizes[2] + 1;
        counter.getCount(new int[sizes.length][]);
        counter.getCount(new int[] { invalidUnidimensionalIndex });
    }

    /**
     * Test getCounts() method with invalid size.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsInvalidSize() {
        int[] sizes = new int[] { 2, 3, 4 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int invalidSize = sizes.length + 1;
        counter.getCounts(invalidSize);
    }
}