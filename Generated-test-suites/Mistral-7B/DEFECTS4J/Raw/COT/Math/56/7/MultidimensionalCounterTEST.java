package org.apache.commons.math.util;

import static org.junit.Assert.*;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.util.MathUtils;
import org.apache.commons.math.util.MultidimensionalCounter;
import org.apache.commons.math.util.MultidimensionalCounter.Iterator;

import java.util.Arrays;

/**
 * JUnit test file for MultidimensionalCounter class
 */
public class MultidimensionalCounterTest {

    @org.junit.Test
    public void testMultidimensionalCounterConstructor() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(size, counter.getSizes());
        assertEquals(28, counter.getSize());
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterConstructorNegativeSizes() {
        int[] size = {-1, 3, 4};
        new MultidimensionalCounter(size);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterConstructorZeroSizes() {
        int[] size = {0, 3, 4};
        new MultidimensionalCounter(size);
    }

    @org.junit.Test
    public void testMultidimensionalCounterGetCounts() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            int[] counts = iterator.getCounts();
            assertArrayEquals(new int[]{index % 2, index % 3, index % 4}, counts);
            index++;
        }
    }

    @org.junit.Test(expected = DimensionMismatchException.class)
    public void testMultidimensionalCounterGetCountsInvalidDimension() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCounts(5);
    }

    @org.junit.Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterGetCountInvalidIndex() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(-1);
    }

    @org.junit.Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterGetCountInvalidIndexLastDimension() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(size[0] * size[1] + size[0] * size[1] + size[0]);
    }

    @org.junit.Test
    public void testMultidimensionalCounterIterator() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        int expectedCount = 0;
        while (iterator.hasNext()) {
            int actualCount = iterator.next();
            assertEquals(expectedCount, actualCount);
            expectedCount++;
        }
        assertEquals(28, expectedCount);
    }

    @org.junit.Test
    public void testMultidimensionalCounterToString() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        StringBuilder expectedToString = new StringBuilder();
        expectedToString.append("[0]").append("[0]").append("[0]");
        for (int i = 1; i < 28; i++) {
            int[] counts = counter.getCounts(i);
            for (int j = 0; j < dimension; j++) {
                expectedToString.append("[").append(counts[j]).append("]");
            }
            expectedToString.append(", ");
        }
        expectedToString.deleteCharAt(expectedToString.length() - 1);
        expectedToString.deleteCharAt(expectedToString.length() - 1);
        assertEquals(expectedToString.toString(), counter.toString());
    }
}