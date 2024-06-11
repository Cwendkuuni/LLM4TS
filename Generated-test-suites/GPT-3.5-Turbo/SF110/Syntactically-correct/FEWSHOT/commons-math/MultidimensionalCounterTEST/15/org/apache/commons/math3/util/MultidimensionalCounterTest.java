package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Test;

public class MultidimensionalCounterTest {
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NotStrictlyPositiveException() {
        new MultidimensionalCounter(0);
    }
    
    @Test
    public void testConstructor_ValidSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2);
        assertEquals(1, counter.getDimension());
        assertEquals(2, counter.getSize());
        assertArrayEquals(new int[] {2}, counter.getSizes());
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertArrayEquals(new int[] {0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[] {0, 0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[] {1, 1, 2}, counter.getCounts(11));
        assertArrayEquals(new int[] {1, 2, 3}, counter.getCounts(17));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_OutOfRangeException() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCounts(-1);
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(11, counter.getCount(1, 1, 2));
        assertEquals(17, counter.getCount(1, 2, 3));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_DimensionMismatchException() throws OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(0, 0, 0, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_OutOfRangeException() throws OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(0, 0, 4);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);

        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(true, iterator.hasNext());
        assertEquals(0, iterator.next().intValue());
        assertEquals(1, iterator.next().intValue());
        assertEquals(2, iterator.next().intValue());
        assertEquals(3, iterator.next().intValue());
        assertEquals(4, iterator.next().intValue());
        assertEquals(5, iterator.next().intValue());
        assertEquals(false, iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_RemoveUnsupportedOperationException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.iterator().remove();
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals("[0][0][0]", counter.toString());
        assertEquals("[0][0][1]", counter.toString());
        assertEquals("[1][2][3]", counter.toString());
        assertEquals("[1][2][3]", counter.toString());
    }
    
}