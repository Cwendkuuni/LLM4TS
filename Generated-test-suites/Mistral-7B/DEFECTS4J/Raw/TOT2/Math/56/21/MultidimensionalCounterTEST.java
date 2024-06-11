package org.apache.commons.math.util;

import static org.junit.Assert.*;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterValidConstructor() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(sizes);
        int expectedDimension = 3;
        assertEquals(expectedDimension, mdCounter.getDimension());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterInvalidConstructor() {
        int[] sizes = {-1, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testGetCountsValidIndex() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(sizes);
        int expectedIndex = 0;
        int[] actualCounts = mdCounter.getCounts(expectedIndex);
        int[] expectedCounts = {0, 0, 0};
        assertArrayEquals(expectedCounts, actualCounts);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRangeIndex() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(sizes);
        int expectedIndex = 27;
        mdCounter.getCounts(expectedIndex);
    }

    @Test
    public void testGetCountValidCounts() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(sizes);
        int[] expectedCounts = {1, 0, 0};
        int expectedCount = mdCounter.getCount(expectedCounts);
        int actualCount = 0;
        for (int i = 0; i < dimension; i++) {
            actualCount += uniCounterOffset[i] * expectedCounts[i];
        }
        assertEquals(actualCount, expectedCount);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountInvalidCounts() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(sizes);
        int[] expectedCounts = {1, 5, 3};
        mdCounter.getCount(expectedCounts);
    }

    @Test
    public void testIteratorHasNext() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(sizes);
        Iterator it = mdCounter.iterator();
        int[] expectedCounts = {0, 0, 0};
        int expectedCount = 0;
        int result = 0;
        boolean hasNext = true;
        while (hasNext) {
            if (!it.hasNext()) {
                fail("Iterator should have next element.");
            }
            result = it.next();
            int[] actualCounts = it.getCounts();
            assertArrayEquals(expectedCounts, actualCounts);
            assertEquals(expectedCount, result);
            expectedCount++;
        }
        assertEquals(-1, it.next());
    }

    @Test
    public void testIteratorGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(sizes);
        Iterator it = mdCounter.iterator();
        int[] expectedCounts = {0, 0, 0};
        int expectedCount = 0;
        int result = 0;
        while (it.hasNext()) {
            result = it.next();
            int[] actualCounts = it.getCounts();
            assertArrayEquals(expectedCounts, actualCounts);
            assertEquals(expectedCount, result);
            expectedCount++;
        }
        // Test invalid dimension index
        try {
            it.getCount(-1);
            fail("Should throw IndexOutOfBoundsException.");
        } catch (IndexOutOfBoundsException e) {
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(sizes);
        Iterator it = mdCounter.iterator();
        it.remove();
    }

    @Test
    public void testTotalSize() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(sizes);
        int expectedTotalSize = 1 * 2 * 2 * 3;
        assertEquals(expectedTotalSize, mdCounter.getSize());
    }
}