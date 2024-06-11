package org.apache.commons.math.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for MultidimensionalCounter class
 */
public class MultidimensionalCounterTest {

    @Test
    public void testEmptyMultiDimensionalCounter() {
        int[] size = new int[0];
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertNotNull(counter);
        assertTrue(counter.iterator().hasNext());
        int[] multidimensionalIndex = counter.iterator().next().getCounts();
        assertEquals(0, multidimensionalIndex.length);
        assertArrayEquals(new int[0], multidimensionalIndex);
    }

    @Test
    public void testMultiDimensionalCounterWithOneDimension() {
        int[] size = new int[1];
        size[0] = 5;
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertNotNull(counter);
        assertTrue(counter.iterator().hasNext());
        int[] multidimensionalIndex = counter.iterator().next().getCounts();
        assertEquals(1, multidimensionalIndex.length);
        assertEquals(0, multidimensionalIndex[0]);
    }

    @Test
    public void testMultiDimensionalCounterWithTwoDimensions() {
        int[] size = new int[2];
        size[0] = 3;
        size[1] = 4;
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertNotNull(counter);
        assertTrue(counter.iterator().hasNext());
        int[] multidimensionalIndex = counter.iterator().next().getCounts();
        assertEquals(2, multidimensionalIndex.length);
        assertEquals(0, multidimensionalIndex[0]);
        assertEquals(0, multidimensionalIndex[1]);

        for (int i = 0; i < size[0] * size[1]; i++) {
            multidimensionalIndex = counter.iterator().next().getCounts();
            assertNotSame(multidimensionalIndex, new int[]{0, 0});
        }

        try {
            counter.iterator().next();
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultiDimensionalCounterWithNegativeSize() {
        int[] size = new int[]{-1};
        new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultiDimensionalCounterWithNegativeSizeInMultiDimensions() {
        int[] size = new int[]{-1, 5};
        new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultiDimensionalCounterWithZeroSizeInMultiDimensions() {
        int[] size = new int[]{5, 0};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testMultiDimensionalCounterGetCount() {
        int[] size = new int[]{3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int index = 0;
        assertEquals(index, counter.getCount(0, 0));

        for (int i = 0; i < size[0]; i++) {
            for (int j = 0; j < size[1]; j++) {
                index = counter.getCount(i, j);
                assertTrue(index < size[0] * size[1]);
            }
        }

        try {
            counter.getCount(-1, 0);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }

        try {
            counter.getCount(0, -1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }

        try {
            counter.getCount(-1, -1);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test
    public void testMultiDimensionalCounterGetSizes() {
        int[] size = new int[]{3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] sizes = counter.getSizes();
        assertEquals(size.length, sizes.length);
        assertArrayEquals(size, sizes);
    }

    @Test
    public void testMultiDimensionalCounterGetDimension() {
        int[] size = new int[]{3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(size.length, counter.getDimension());
    }

    @Test
    public void testMultiDimensionalCounterToString() {
        int[] size = new int[]{3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        String representation = counter.toString();
        assertTrue(representation.matches("\\[.*\\]\\[.*\\]"));
    }

    @Test
    public void testMultiDimensionalCounterIteratorHasNext() {
        int[] size = new int[]{3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        for (int i = 0; i < size[0] * size[1]; i++) {
            assertTrue(iterator.hasNext());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMultiDimensionalCounterIteratorGetCount() {
        int[] size = new int[]{3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        int[] multidimensionalIndex;
        int index = -1;
        while (iterator.hasNext()) {
            multidimensionalIndex = iterator.next().getCounts();
            index = counter.getCount(multidimensionalIndex);
            assertEquals(index, multidimensionalIndex[0] + multidimensionalIndex[1] * size[0]);
        }
    }

    @Test
    public void testMultiDimensionalCounterIteratorGetCounts() {
        int[] size = new int[]{3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        int[] multidimensionalIndex;
        while (iterator.hasNext()) {
            multidimensionalIndex = iterator.next().getCounts();
            assertEquals(size.length, multidimensionalIndex.length);
        }
    }
}