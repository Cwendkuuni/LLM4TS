package org.apache.commons.math.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for MultidimensionalCounter class.
 */
public class MultidimensionalCounterTest {

    /**
     * Test creation of a 1 dimensional counter.
     */
    @Test
    public void test1D() {
        int[] size = new int[]{1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        for (int i = 0; i < size[0]; i++) {
            assertTrue(iterator.hasNext());
            Integer next = iterator.next();
            assertEquals(next.intValue(), i);
        }
        assertFalse(iterator.hasNext());
    }

    /**
     * Test creation of a 2 dimensional counter.
     */
    @Test
    public void test2D() {
        int[] size = new int[]{2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        for (int i = 0; i < size[0]; i++) {
            for (int j = 0; j < size[1]; j++) {
                assertTrue(iterator.hasNext());
                Integer next = iterator.next();
                int[] counts = iterator.getCounts();
                int expected = i * size[1] + j;
                assertEquals(next.intValue(), expected);
                assertArrayEquals(new int[]{i, j}, counts);
            }
        }
        assertFalse(iterator.hasNext());
    }

    /**
     * Test creation of a 3 dimensional counter.
     */
    @Test
    public void test3D() {
        int[] size = new int[]{3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        for (int i = 0; i < size[0]; i++) {
            for (int j = 0; j < size[1]; j++) {
                for (int k = 0; k < size[2]; k++) {
                    assertTrue(iterator.hasNext());
                    Integer next = iterator.next();
                    int[] counts = iterator.getCounts();
                    int expected = i * size[1] * size[2] + j * size[2] + k;
                    assertEquals(next.intValue(), expected);
                    assertArrayEquals(new int[]{i, j, k}, counts);
                }
            }
        }
        assertFalse(iterator.hasNext());
    }

    /**
     * Test out of range index.
     */
    @Test(expected = OutOfRangeException.class)
    public void testOutOfRange() {
        int[] size = new int[]{3, 3, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        for (int i = 0; i < size[0] * size[1] * size[2]; i++) {
            iterator.next();
        }
        iterator.next();
    }

    /**
     * Test negative size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSize() {
        int[] size = new int[]{-1, 3, 3};
        new MultidimensionalCounter(size);
    }

    /**
     * Test different sizes.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDifferentSizes() {
        int[] size1 = new int[]{3, 3};
        int[] size2 = new int[]{3, 4};
        new MultidimensionalCounter(size1, size2);
    }
}