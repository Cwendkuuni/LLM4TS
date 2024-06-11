
package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NegativeSize() {
        new MultidimensionalCounter(-1);
    }

    @Test
    public void testConstructor() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2);
        assertEquals(2, counter.getDimension());
        assertEquals(1, counter.getCount(0));
        assertEquals(2, counter.getCount(1));
        assertEquals(2, counter.getSize());
        assertArrayEquals(new int[] { 2 }, counter.getSizes());
    }

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertArrayEquals(new int[] { 0, 0, 0 }, counter.getCounts(0));
        assertArrayEquals(new int[] { 1, 0, 0 }, counter.getCounts(1));
        assertArrayEquals(new int[] { 0, 1, 0 }, counter.getCounts(2));
        assertArrayEquals(new int[] { 1, 1, 0 }, counter.getCounts(3));
        assertArrayEquals(new int[] { 0, 2, 0 }, counter.getCounts(4));
        assertArrayEquals(new int[] { 1, 2, 0 }, counter.getCounts(5));

        assertArrayEquals(new int[] { 0, 0, 1 }, counter.getCounts(6));
        assertArrayEquals(new int[] { 1, 0, 1 }, counter.getCounts(7));
        assertArrayEquals(new int[] { 0, 1, 1 }, counter.getCounts(8));
        assertArrayEquals(new int[] { 1, 1, 1 }, counter.getCounts(9));
        assertArrayEquals(new int[] { 0, 2, 1 }, counter.getCounts(10));
        assertArrayEquals(new int[] { 1, 2, 1 }, counter.getCounts(11));

        assertArrayEquals(new int[] { 0, 0, 2 }, counter.getCounts(12));
        assertArrayEquals(new int[] { 1, 0, 2 }, counter.getCounts(13));
        assertArrayEquals(new int[] { 0, 1, 2 }, counter.getCounts(14));
        assertArrayEquals(new int[] { 1, 1, 2 }, counter.getCounts(15));
        assertArrayEquals(new int[] { 0, 2, 2 }, counter.getCounts(16));
        assertArrayEquals(new int[] { 1, 2, 2 }, counter.getCounts(17));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_OutOfRange() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCounts(18);
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(23, counter.getCount(1, 2, 3));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_DimensionMismatch() throws OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(1, 2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_OutOfRange() throws OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(2, 3, 4);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertArrayEquals(new int[] { 2, 3, 4 }, counter.getSizes());
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals("[0][0][0]", counter.toString());
    }

    @Test
    public void testIterator_HasNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.getCount());
    }

    @Test
    public void testIterator_Next() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(Integer.valueOf(1), iterator.next());
        assertEquals(1, iterator.getCount());
    }

    @Test
    public void testIterator_Next_Last() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        assertFalse(iterator.hasNext());
        assertEquals(23, iterator.getCount());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_Remove() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }
}
