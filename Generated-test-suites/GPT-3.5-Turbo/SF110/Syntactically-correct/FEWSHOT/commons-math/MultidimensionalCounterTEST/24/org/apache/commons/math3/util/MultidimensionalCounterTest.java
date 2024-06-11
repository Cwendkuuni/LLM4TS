
package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.util.Iterator;

public class MultidimensionalCounterTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_withNegativeSize() throws NotStrictlyPositiveException {
        new MultidimensionalCounter(-1);
    }

    @Test
    public void testConstructor_withPositiveSize() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2);

        assertEquals(1, counter.getDimension());
        assertEquals(2, counter.getSize());
        assertArrayEquals(new int[]{2}, counter.getSizes());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_withDimensionMismatch() throws DimensionMismatchException, OutOfRangeException, NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2);

        counter.getCount(0, 0, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_withOutOfRangeIndex() throws DimensionMismatchException, OutOfRangeException, NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2);

        counter.getCount(1, 1);
    }

    @Test
    public void testGetCount_withValidInputs() throws DimensionMismatchException, OutOfRangeException, NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4);

        assertEquals(13, counter.getCount(3, 1));
        assertEquals(2, counter.getCount(0, 2));
    }

    @Test
    public void testIterator() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2);

        Iterator<Integer> iterator = counter.iterator();

        assertEquals(0, (int) iterator.next());
        assertEquals(1, (int) iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(1);

        Iterator<Integer> iterator = counter.iterator();

        iterator.remove();
    }

    @Test
    public void testGetCounts_withOutOfRangeIndex() throws OutOfRangeException, NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2);

        try {
            counter.getCounts(3);
            fail("OutOfRangeException not thrown");
        } catch (OutOfRangeException e) {
            // expected
        }
    }

    @Test
    public void testGetCounts_withValidIndex() throws OutOfRangeException, NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        assertArrayEquals(new int[]{1, 1, 0}, counter.getCounts(5));
        assertArrayEquals(new int[]{0, 2, 3}, counter.getCounts(23));
    }
}
