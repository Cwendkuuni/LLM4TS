package org.apache.commons.math.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructorSizeZero() {
        assertThrows(NotStrictlyPositiveException.class, () -> new MultidimensionalCounter(new int[0]));
    }

    @Test
    public void testConstructorPositive() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter mc = new MultidimensionalCounter(size);
        assertEquals(6 * 3 * 4, mc.getSize());
    }

    @Test
    public void testIteratorHasNext() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter mc = new MultidimensionalCounter(size);

        Iterator it = mc.iterator();
        for (int i = 0; i < size[0] * size[1] * size[2]; i++) {
            assertEquals(true, it.hasNext());
        }
        assertEquals(false, it.hasNext());
    }

    @Test
    public void testIteratorNext() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter mc = new MultidimensionalCounter(size);
        Iterator it = mc.iterator();

        for (int i = 0; i < size[0] * size[1] * size[2]; i++) {
            Integer next = it.next();
            assertEquals(i, next.intValue());
        }
    }

    @Test
    public void testIteratorGetCount() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter mc = new MultidimensionalCounter(size);
        Iterator it = mc.iterator();

        for (int i = 0; i < size[0] * size[1] * size[2]; i++) {
            int ind = it.getCount();
            assertEquals(i, ind);
        }
    }

    @Test
    public void testGetCounts() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter mc = new MultidimensionalCounter(size);

        for (int i = 0; i < size[0]; i++) {
            for (int j = 0; j < size[1]; j++) {
                for (int k = 0; k < size[2]; k++) {
                    int[] expected = new int[]{i, j, k};
                    int[] actual = mc.getCounts(mc.getCount(expected));
                    assertArrayEquals(expected, actual);
                }
            }
        }
    }

    @Test
    public void testGetCountOutOfRange() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter mc = new MultidimensionalCounter(size);

        for (int i = 0; i < size[0]; i++) {
            for (int j = 0; j < size[1]; j++) {
                for (int k = 0; k < size[2]; k++) {
                    int[] index = new int[]{i, j, k};
                    int indexOutOfRange = index[0] + 1;
                    assertThrows(OutOfRangeException.class, () -> mc.getCount(index));
                }
            }
        }
    }

    @Test
    public void testGetCountSizeMismatch() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter mc = new MultidimensionalCounter(new int[]{2, 3, 4, 5});

        assertThrows(DimensionMismatchException.class, () -> mc.getCount(new int[]{2, 3, 4}));
    }
}