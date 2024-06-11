package org.apache.commons.math.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = new int[] {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
        assertEquals(2 * 4 * 3, counter.getSize());
    }

    @Test
    public void testGetCounts() {
        int[] size = new int[] {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int[] zero = new int[2];
        int[] one = new int[] {1, 0, 0};
        int[] two = new int[] {1, 0, 1};
        int[] three = new int[] {0, 3, 2};

        assertEquals(zero, counter.getCounts(0));
        assertEquals(one, counter.getCounts(1));
        assertEquals(two, counter.getCounts(2));
        assertEquals(three, counter.getCounts(3));
    }

    @Test
    public void testGetCount() {
        int[] size = new int[] {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        assertEquals(0, counter.getCount(new int[0]));
        assertEquals(0, counter.getCount(new int[] {1, 1, 1}));
        assertEquals(1, counter.getCount(new int[] {0, 0, 0}));
        assertEquals(4, counter.getCount(new int[] {0, 0, 1}));
        assertEquals(3, counter.getCount(new int[] {0, 1, 0}));
        assertEquals(12, counter.getCount(new int[] {1, 0, 0}));
        assertEquals(23, counter.getCount(new int[] {1, 3, 2}));

        assertThrows(OutOfRangeException.class, () -> {
            counter.getCount(new int[] {1, 5, 2});
        });
        assertThrows(DimensionMismatchException.class, () -> {
            counter.getCount(new int[] {1, 1, 5});
        });
    }

    @Test
    public void testIterator() {
        int[] size = new int[] {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();

        int count = 0;
        for (Integer index : iterator) {
            int[] counts = iterator.getCounts();
            assertEquals(count, counts[0]);
            assertEquals(count % 4, counts[1]);
            assertEquals(count % 3, counts[2]);
            assertEquals(count % (4 * 3), counts[3]);
            assertEquals(count + 1, index);
            count++;
        }

        assertNull(iterator.next());
    }
}