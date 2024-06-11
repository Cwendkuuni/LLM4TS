
package org.apache.commons.math3.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructorNotStrictlyPositiveException() {
        try {
            new MultidimensionalCounter(-1, 2); // size is negative
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // success
        }
    }

    @Test
    public void testCountsOutOfRangeException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4);
        try {
            counter.getCounts(15); // index exceeds total size
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // success
        }
    }

    @Test
    public void testCountDimensionMismatchException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4);
        try {
            counter.getCount(1); // only one index provided
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // success
        }
    }

    @Test
    public void testCountOutOfRangeException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4);
        try {
            counter.getCount(-1, 2); // index is negative
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // success
        }
    }

    @Test
    public void testIteratorRemoveUnsupportedOperationException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        try {
            iterator.remove();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }
}
