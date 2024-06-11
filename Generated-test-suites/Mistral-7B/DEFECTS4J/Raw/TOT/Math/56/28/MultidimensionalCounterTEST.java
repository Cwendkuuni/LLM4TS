package org.apache.commons.math.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    /**
     * Test creating a multidimensional counter with dimensions 1, 2, 3.
     */
    @Test
    public void testCreateMultidimensionalCounter() {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        assertEquals(6, counter.getSize());
    }

    /**
     * Test getting the multidimensional counts from an index in the unidimensional counter.
     */
    @Test
    public void testGetCounts() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        int[] counts = counter.getCounts(1);
        assertEquals(2, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(0, counts[2]);
    }

    /**
     * Test getting the index in the unidimensional counter from multidimensional counts.
     */
    @Test
    public void testGetCount() throws DimensionMismatchException, OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        int index = counter.getCount(1, 1, 0);
        assertEquals(3, index);
    }

    /**
     * Test creating a multidimensional counter with a negative size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSize() {
        new MultidimensionalCounter(-1);
    }

    /**
     * Test creating a multidimensional counter with a zero size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroSize() {
        new MultidimensionalCounter(0);
    }

    /**
     * Test creating a multidimensional counter with a multidimensional size that does not match the constructor.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatch() {
        new MultidimensionalCounter(1, 2, 3, 4);
    }

    /**
     * Test getting a multidimensional counter slot that is out of range.
     */
    @Test(expected = OutOfRangeException.class)
    public void testOutOfRange() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        counter.getCount(-1, 0, 0);
    }

    /**
     * Test getting a multidimensional counter slot that is out of range in a higher dimension.
     */
    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeHigherDimension() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        counter.getCount(1, 3, 0);
    }

    /**
     * Test getting a multidimensional counter slot that is out of range in the last dimension.
     */
    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeLastDimension() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        counter.getCount(1, 2, 3);
    }
}