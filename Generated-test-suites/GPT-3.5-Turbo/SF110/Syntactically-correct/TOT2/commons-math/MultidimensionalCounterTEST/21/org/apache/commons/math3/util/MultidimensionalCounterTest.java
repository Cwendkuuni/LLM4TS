package org.apache.commons.math3.util;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroSizeArray() {
        new MultidimensionalCounter();
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsMethodWithIndexOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.getCounts(5);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountMethodWithDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.getCount(1, 2, 3);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorNextMethodWithCountExceedsTotalSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
    }
}