package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.util.MathUtils;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] size = { 2, 4, 3 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
        assertEquals(81, counter.getSize());
        assertEquals(size, counter.getSizes());
    }

    @Test
    public void testMultidimensionalCounterIteration() {
        int[] size = { 2, 4, 3 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        int expectedTotalSize = size[0] * size[1] * size[2];

        int index = 0;
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            assertEquals(index, next);
            index++;
        }
        assertNull(iterator.next());
        assertEquals(expectedTotalSize, counter.getSize());
    }

    @Test
    public void testGetCounts() {
        int[] size = { 2, 4, 3 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int[] expected = { 0, 0, 0 };
        int[] actual = counter.getCounts(0);
        assertArrayEquals(expected, actual);

        expected = new int[] { 0, 0, 1 };
        actual = counter.getCounts(1);
        assertArrayEquals(expected, actual);

        expected = new int[] { 0, 1, 0 };
        actual = counter.getCounts(counter.getSize() - 1);
        assertArrayEquals(expected, actual);

        assertThrows(OutOfRangeException.class, () -> counter.getCounts(-1));
        assertThrows(OutOfRangeException.class, () -> counter.getCounts(counter.getSize()));
    }

    @Test
    public void testGetCount() {
        int[] size = { 2, 4, 3 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int expected = 0;
        int actual;

        actual = counter.getCount(new int[] { 0, 0, 0 });
        assertEquals(expected, actual);

        expected = 1;
        actual = counter.getCount(new int[] { 0, 0, 1 });
        assertEquals(expected, actual);

        expected = 23;
        actual = counter.getCount(new int[] { 1, 3, 2 });
        assertEquals(expected, actual);

        assertThrows(DimensionMismatchException.class, () -> counter.getCount(new int[] { 4, 5 }));
        assertThrows(DimensionMismatchException.class, () -> counter.getCount(new int[] { 1, 4 }));
        assertThrows(OutOfRangeException.class, () -> counter.getCount(new int[] { -1, 0, 0 }));
        assertThrows(OutOfRangeException.class, () -> counter.getCount(new int[] { 1, -1, 0 }));
        assertThrows(OutOfRangeException.class, () -> counter.getCount(new int[] { 1, 4, -1 }));
        assertThrows(OutOfRangeException.class, () -> counter.getCount(new int[] { 1, 4, 3 }));
    }

    @Test
    public void testNegativeSize() {
        int[] size = { -2, 4, 3 };
        assertThrows(NotStrictlyPositiveException.class, () -> new MultidimensionalCounter(size));
    }

    @Test
    public void testGetCountUninitialized() {
        int[] size = { 2, 4, 3 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertThrows(OutOfRangeException.class, () -> counter.getCount(new int[0][0]));
    }
}