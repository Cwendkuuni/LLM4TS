package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_ThrowsExceptionWhenSizeNotStrictlyPositive() throws NotStrictlyPositiveException {
        new MultidimensionalCounter(-1);
    }

    @Test
    public void testConstructor_CreatesObjectWithCorrectDimension() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetCounts_ThrowsExceptionWhenIndexOutOfRange() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        try {
            counter.getCounts(6);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            assertEquals("6 is out of range (min=0, max=5)", e.getMessage());
        }
    }

    @Test
    public void testGetCounts_ReturnsCorrectCounts() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int[] counts = counter.getCounts(4);
        assertArrayEquals(new int[]{0, 1}, counts);
    }

    @Test
    public void testGetCount_ThrowsExceptionWhenDimensionMismatch() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        try {
            counter.getCount(2, 3, 4);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            assertEquals("3 is not equal to the required dimension (2)", e.getMessage());
        }
    }

    @Test
    public void testGetCount_ThrowsExceptionWhenIndexOutOfRange() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        try {
            counter.getCount(2, 4);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            assertEquals("4 is out of range (min=0, max=2)", e.getMessage());
        }
    }

    @Test
    public void testGetCount_ReturnsCorrectCount() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int count = counter.getCount(1, 2);
        assertEquals(5, count);
    }

    @Test
    public void testGetSize_ReturnsCorrectSize() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertEquals(6, counter.getSize());
    }

    @Test
    public void testGetSizes_ReturnsCorrectSizes() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int[] sizes = counter.getSizes();
        assertArrayEquals(new int[]{2, 3}, sizes);
    }

}