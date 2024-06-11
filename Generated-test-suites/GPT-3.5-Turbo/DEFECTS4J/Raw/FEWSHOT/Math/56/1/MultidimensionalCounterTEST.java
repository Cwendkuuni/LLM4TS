
package org.apache.commons.math.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
        assertArrayEquals(new int[]{2, 3, 4}, counter.getSizes());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidSize() {
        counter = new MultidimensionalCounter(-1, 1, 2);
    }

    @Test
    public void testGetSize() {
        assertEquals(2 * 3 * 4, counter.getSize());
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        assertEquals(counter.getSize(), count);
    }

    @Test
    public void testGetCountWithIndices() {
        int[] indices = {1, 2, 3};
        int expectedCount = 21;
        assertEquals(expectedCount, counter.getCount(indices));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWithInvalidIndices() {
        int[] indices = {1, 2};
        counter.getCount(indices);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithOutOfRangeIndices() {
        int[] indices = {3, 2, 1};
        counter.getCount(indices);
    }

    @Test
    public void testGetCounts() {
        int index = 21;
        int[] expectedIndices = {1, 2, 3};
        assertArrayEquals(expectedIndices, counter.getCounts(index));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsWithOutOfRangeIndex() {
        int index = 24;
        counter.getCounts(index);
    }

    @Test
    public void testGetCountsWhenIndexIsZero() {
        int index = 0;
        int[] expectedIndices = {0, 0, 0};
        assertArrayEquals(expectedIndices, counter.getCounts(index));
    }

    @Test
    public void testNextWithDifferentIndices() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int prevIndex = iterator.next();
        while (iterator.hasNext()) {
            int currentIndex = iterator.next();
            assertNotEquals(prevIndex, currentIndex);
            prevIndex = currentIndex;
        }
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int expectedCount = 0;
        while (iterator.hasNext()) {
            int currentIndex = iterator.next();
            assertEquals(expectedCount, iterator.getCount());
            expectedCount++;
        }
    }
}
