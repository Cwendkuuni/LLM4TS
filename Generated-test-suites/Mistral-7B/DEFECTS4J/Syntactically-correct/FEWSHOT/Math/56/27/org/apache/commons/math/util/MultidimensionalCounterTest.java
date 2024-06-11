package org.apache.commons.math.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertEquals(3, mdc.getDimension());
        assertEquals(sizes, mdc.getSizes());
        assertEquals(32 * 3 * 4, mdc.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSizes() {
        int[] sizes = {-1, 3, 4};
        new MultidimensionalCounter(sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroSizes() {
        int[] sizes = {0, 3, 4};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testIteratorHasNext() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator it = mdc.iterator();
        for (int i = 0; i < Math.pow(sizes[0], sizes.length); i++) {
            assertTrue(it.hasNext());
        }
        assertFalse(it.hasNext());
    }

    @Test
    public void testIteratorNext() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator it = mdc.iterator();
        int unidimensionalCount = 0;
        while (it.hasNext()) {
            int multidimensionalCount = it.next();
            assertEquals(unidimensionalCount++, multidimensionalCount);
        }
    }

    @Test
    public void testGetCounts() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        for (int i = 0; i < Math.pow(sizes[0], sizes.length); i++) {
            int[] actual = mdc.getCounts(i);
            int[] expected = new int[sizes.length];
            for (int j = sizes.length - 1; j >= 0; j--) {
                int index = i / Math.pow(sizes[j + 1], sizes.length - j - 1);
                expected[j] = index % sizes[j];
            }
            assertArrayEquals(expected, actual);
        }
    }

    @Test
    public void testGetCount() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        for (int i = 0; i < Math.pow(sizes[0], sizes.length); i++) {
            int multidimensionalCount[] = mdc.getCounts(i);
            int unidimensionalCount = mdc.getCount(multidimensionalCount);
            assertEquals(i, unidimensionalCount);
        }
    }

    @Test
    public void testToString() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        for (int i = 0; i < Math.pow(sizes[0], sizes.length); i++) {
            String actual = mdc.toString();
            String expected = "[";
            for (int j = 0; j < sizes.length; j++) {
                if (j > 0) {
                    expected += ", ";
                }
                expected += String.valueOf(i / Math.pow(sizes[j + 1], sizes.length - j - 1)) + "]";
            }
            assertEquals(expected, actual);
        }
    }
}