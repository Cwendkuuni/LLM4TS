package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Iterator;

import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = new int[] { 2, 4, 3 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
        assertEquals(sizes, counter.getSizes());
        assertEquals(28, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSize() {
        int[] sizes = new int[] { -1, 4, 3 };
        new MultidimensionalCounter(sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroSize() {
        int[] sizes = new int[] { 0, 4, 3 };
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testGetCounts() {
        int[] sizes = new int[] { 2, 4, 3 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        int[] counts = counter.getCounts(0);
        assertArrayEquals(new int[] { 0, 0, 0 }, counts);
        assertEquals(0, counter.getCount(0, 0, 0));

        counts = counter.getCounts(28);
        assertArrayEquals(new int[] { 2, 3, 1 }, counts);
        assertEquals(27, counter.getCount(2, 3, 1));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] sizes = new int[] { 2, 4, 3 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        counter.getCounts(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = new int[] { 2, 4, 3 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        counter.getCount(-1, 0, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeDimension() {
        int[] sizes = new int[] { 2, 4, 3 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        counter.getCount(0, 4, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeLastDimension() {
        int[] sizes = new int[] { 2, 4, 3 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        counter.getCount(0, 0, 3);
    }

    @Test
    public void testIterator() {
        int[] sizes = new int[] { 2, 4, 3 };
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();

        Iterator<Integer> expectedIterator = new Iterator<Integer>() {
            int count = 0;

            @Override
            public boolean hasNext() {
                for (int i = 0; i < 3; i++) {
                    if (count < 27) {
                        return true;
                    }
                    count += sizes[i];
                }
                return false;
            }

            @Override
            public Integer next() {
                int[] counts = new int[3];
                int index = 0;
                while (count < count) {
                    counts[index++] = counter.getCount(index);
                    count += sizes[index];
                }
                --index;
                count -= sizes[index];
                return index;
            }

            @Override
            public int getCount() {
                return count;
            }

            @Override
            public int[] getCounts() {
                return counts;
            }

            @Override
            public int getCount(int dim) {
                return counts[dim];
            }
        };

        for (int i = 0; i < 28; i++) {
            assertEquals(iterator.next(), expectedIterator.next());
            assertEquals(iterator.getCount(), expectedIterator.getCount());
            assertEquals(iterator.getCounts(), expectedIterator.getCounts());
        }
        assertNull(iterator.next());
    }
}