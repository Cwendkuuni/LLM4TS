package org.apache.commons.math.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import org.junit.Assert.*;

/**
 * Test cases for {@link MultidimensionalCounter} class.
 */
public class MultidimensionalCounterTest {

    /**
     * Test empty multidimensional counter.
     */
    @Test
    public void testEmptyCounter() {
        int[] size = new int[0];
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        assertEquals(Iterator.class, iterator.getClass());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test one-dimensional multidimensional counter.
     */
    @Test
    public void testOneDimensionCounter() {
        int[] size = {1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            assertEquals(index, next);
            index++;
        }
        assertFalse(iterator.hasNext());
    }

    /**
     * Test two-dimensional multidimensional counter.
     */
    @Test
    public void testTwoDimensionCounter() {
        int[] size = {1, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            int[] counts = iterator.getCounts();
            assertEquals(counts.length, 2);
            assertEquals(index, next);
            assertEquals(counts[0], index % 2);
            assertEquals(counts[1], index / 2);
            index++;
        }
        assertFalse(iterator.hasNext());
    }

    /**
     * Test multidimensional counter with negative sizes.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSizes() {
        int[] size = {-1, 2};
        new MultidimensionalCounter(size);
    }

    /**
     * Test out-of-range index in multidimensional counter.
     */
    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeIndex() {
        int[] size = {1, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        iterator.next();
        iterator.next();
        int index = size[0] * size[1] + 1;
        iterator.next();
        fail("Exception expected but not thrown");
    }

    /**
     * Test out-of-range index in unidimensional counter.
     */
    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeUnidimensionalIndex() {
        int[] size = {1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        int index = totalSize(size) + 1;
        iterator.next();
        fail("Exception expected but not thrown");
    }

    /**
     * Test getCounts method with negative index.
     */
    @Test(expected = OutOfRangeException.class)
    public void testNegativeIndexGetCounts() {
        int[] size = {1, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] counts = counter.getCounts(-1);
        fail("Exception expected but not thrown");
    }

    /**
     * Test getCount method with negative index.
     */
    @Test(expected = OutOfRangeException.class)
    public void testNegativeIndexGetCount() {
        int[] size = {1, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        try {
            counter.getCount(-1);
            fail("Exception expected but not thrown");
        } catch (OutOfRangeException e) {
            // expected
        }
    }

    /**
     * Test getCount method with multidimensional index.
     */
    @Test
    public void testGetCountMultidimensionalIndex() {
        int[] size = {1, 1, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] index = {1, 1, 1};
        int expected = index[0] + unidimensionalOffset(size, index[0]) * (index[1] + unidimensionalOffset(size, index[1]) * index[2]);
        int result = counter.getCount(index);
        assertEquals(expected, result);
    }

    /**
     * Test getCounts method with multidimensional index.
     */
    @Test
    public void testGetCountsMultidimensionalIndex() {
        int[] size = {1, 1, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] index = {1, 1, 1};
        int[] expected = new int[]{index[0], index[1], index[2]};
        int[] result = counter.getCounts(index);
        assertArrayEquals(expected, result);
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        int[] size = {1, 1, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int expected = size[0] * size[1] * size[2];
        int result = counter.getSize();
        assertEquals(expected, result);
    }

    /**
     * Test getSizes method.
     */
    @Test
    public void testGetSizes() {
        int[] size = {1, 1, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] expected = MathUtils.copyOf(size);
        int[] result = counter.getSizes();
        assertArrayEquals(expected, result);
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        int[] size = {1, 1, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        String expected = "[0]";
        String result = counter.toString();
        assertEquals(expected, result);
    }

    /**
     * Test toString method with iterated counter.
     */
    @Test
    public void testToStringIteratedCounter() {
        int[] size = {1, 1, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        String expected = "[0][0][0]";
        String result = iterator.next().toString();
        assertEquals(expected, result);
    }

    /**
     * Test toString method with multidimensional index.
     */
    @Test
    public void testToStringMultidimensionalIndex() {
        int[] size = {1, 1, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] index = {1, 1, 1};
        String expected = "[1][1][1]";
        String result = counter.getCounts(index).toString();
        assertEquals(expected, result);
    }

    /**
     * Test getCount method with iterated counter.
     */
    @Test
    public void testGetCountIteratedCounter() {
        int[] size = {1, 1, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        int expected = 0;
        int result = iterator.next().getCount();
        assertEquals(expected, result);
    }

    /**
     * Test getCounts method with iterated counter.
     */
    @Test
    public void testGetCountsIteratedCounter() {
        int[] size = {1, 1, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        int[] expected = new int[]{0, 0, 0};
        int[] result = iterator.next().getCounts();
        assertArrayEquals(expected, result);
    }

    /**
     * Test iterator method with negative size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSizeIterator() {
        int[] size = {-1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
    }

    /**
     * Test iterator method with zero size.
     */
    @Test
    public void testZeroSizeIterator() {
        int[] size = {0};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        assertFalse(iterator.hasNext());
    }

    /**
     * Helper method to calculate unidimensional offset for given index in a given size.
     */
    private int unidimensionalOffset(int[] size, int index) {
        int offset = 1;
        for (int i = 0; i < size.length && index >= offset; i++) {
            offset *= size[i];
        }
        return offset - 1;
    }

    /**
     * Helper method to calculate total size for given size.
     */
    private int totalSize(int[] size) {
        int totalSize = 1;
        for (int i = 0; i < size.length; i++) {
            totalSize *= size[i];
        }
        return totalSize;
    }
}