
package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructorThrowsException() {
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter();
            fail("Expected NotStrictlyPositiveException to be thrown");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testGetDimension() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSizes() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] sizes = counter.getSizes();
        assertArrayEquals(new int[]{2, 3, 4}, sizes);
    }

    @Test
    public void testGetSize() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetCounts() throws NotStrictlyPositiveException, OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] counts = counter.getCounts(0);
        assertArrayEquals(new int[]{0, 0, 0}, counts);
    }

    @Test
    public void testGetCount() throws NotStrictlyPositiveException, OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int count = counter.getCount(0, 1, 2);
        assertEquals(17, count);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsThrowsOutOfRangeException() throws NotStrictlyPositiveException, OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] counts = counter.getCounts(30);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountThrowsDimensionMismatchException() throws NotStrictlyPositiveException, OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int count = counter.getCount(0, 1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountThrowsOutOfRangeException() throws NotStrictlyPositiveException, OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int count = counter.getCount(0, 1, 6);
    }

    @Test
    public void testIterator() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        assertEquals(counter.getSize(), count);
    }

    @Test
    public void testIteratorGetCount() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(-1, iterator.getCount());

        while (iterator.hasNext()) {
            iterator.next();
        }

        assertEquals(counter.getSize() - 1, iterator.getCount());
    }

    @Test
    public void testIteratorGetCounts() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int[] counts = iterator.getCounts();
        assertArrayEquals(new int[]{0, 0, 0}, counts);

        while (iterator.hasNext()) {
            iterator.next();
        }

        counts = iterator.getCounts();
        assertArrayEquals(new int[]{1, 2, 3}, counts);
    }

    @Test
    public void testIteratorGetCountByDimension() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(-1, iterator.getCount(0));

        while (iterator.hasNext()) {
            iterator.next();
        }

        assertEquals(1, iterator.getCount(0));
        assertEquals(2, iterator.getCount(1));
        assertEquals(3, iterator.getCount(2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }
}
