package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.apache.commons.math.util.MultidimensionalCounter;
import org.apache.commons.math.util.MathUtils;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter md = new MultidimensionalCounter(sizes);
        assertEquals(3, md.getDimension());
        assertEquals(sizes, md.getSizes());
        assertEquals(24 * 4 * 3, md.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterNegativeSizes() {
        int[] sizes = {-1, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterZeroSizes() {
        int[] sizes = {0, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testMultidimensionalCounterIterator() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter md = new MultidimensionalCounter(sizes);
        Iterator<Integer> it = md.iterator();
        int count = 0;

        for (int i = 0; i < sizes[0] * sizes[1] * sizes[2]; i++) {
            int index = it.next();
            int[] counts = it.getCounts();
            assertEquals(counts.length, 3);
            assertEquals(counts[0], i / (sizes[1] * sizes[2]));
            assertEquals(counts[1], i / sizes[1] % sizes[2]);
            assertEquals(counts[2], i % sizes[1]);
            count++;
        }
        assertFalse(it.hasNext());
    }

    @Test
    public void testMultidimensionalCounterGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter md = new MultidimensionalCounter(sizes);

        for (int i = 0; i < sizes[0] * sizes[1] * sizes[2]; i++) {
            int index = md.getCount(i / sizes[1], i % sizes[1], i % (sizes[1] * sizes[2]));
            assertEquals(i, index);
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterOutOfRangeGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter md = new MultidimensionalCounter(sizes);

        int index = md.getCount(-1, 0, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterOutOfRangeGetCount1() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter md = new MultidimensionalCounter(sizes);

        int index = md.getCount(2, 4, 3);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultidimensionalCounterGetCountWrongDimensions() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter md = new MultidimensionalCounter(sizes);

        int index = md.getCount(1, 1, 1, 1);
    }
}