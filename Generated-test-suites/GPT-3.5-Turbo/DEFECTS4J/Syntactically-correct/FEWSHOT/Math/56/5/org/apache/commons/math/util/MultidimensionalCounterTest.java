package org.apache.commons.math.util;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test 
    public void testConstructorThrowsNotStrictlyPositiveException() {
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(-2, 3, 4);
            fail("Expected NotStrictlyPositiveException to be thrown");
        } catch (NotStrictlyPositiveException e) {
            // expected exception
        }
    }

    @Test 
    public void testConstructor() {
        int[] expectedSizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(expectedSizes);
        int[] actualSizes = counter.getSizes();
        assertArrayEquals(expectedSizes, actualSizes);
    }

    @Test 
    public void testIterator() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int[] expectedCounts = {0, 0, 0};
        int expectedCount = 0;
        int numIterations = (sizes[0] * sizes[1] * sizes[2]) - 1;

        for (int i = 0; i < numIterations; i++) {
            assertTrue(iterator.hasNext());
            int actualCount = iterator.next();
            assertEquals(expectedCount, actualCount);
            assertArrayEquals(expectedCounts, iterator.getCounts());
            
            // increment expected counts
            expectedCounts[2]++;
            if (expectedCounts[2] == sizes[2]) {
                expectedCounts[2] = 0;
                expectedCounts[1]++;
                if (expectedCounts[1] == sizes[1]) {
                    expectedCounts[1] = 0;
                    expectedCounts[0]++;
                }
            }
            expectedCount++;
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetCounts() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] expectedCounts = {0, 0, 0};
        int expectedIndex = 0;

        for (int i = 0; i < sizes[0]; i++) {
            for (int j = 0; j < sizes[1]; j++) {
                for (int k = 0; k < sizes[2]; k++) {
                    int[] actualCounts = counter.getCounts(expectedIndex);
                    assertArrayEquals(expectedCounts, actualCounts);

                    // increment expected counts
                    expectedCounts[2]++;
                    if (expectedCounts[2] == sizes[2]) {
                        expectedCounts[2] = 0;
                        expectedCounts[1]++;
                        if (expectedCounts[1] == sizes[1]) {
                            expectedCounts[1] = 0;
                            expectedCounts[0]++;
                        }
                    }
                    expectedIndex++;
                }
            }
        }
    }

    @Test 
    public void testCountOutOfRange() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        try {
            int[] counts = counter.getCounts(-1);
            fail("Expected OutOfRangeException to be thrown");
        } catch (OutOfRangeException e) {
            // expected exception
        }

        int invalidIndex = (sizes[0] * sizes[1] * sizes[2]) + 1;
        try {
            int[] counts = counter.getCounts(invalidIndex);
            fail("Expected OutOfRangeException to be thrown");
        } catch (OutOfRangeException e) {
            // expected exception
        }
    }

    @Test 
    public void testGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[][][] expectedCounts = new int[sizes[0]][sizes[1]][sizes[2]];
        int expectedIndex = 0;

        for (int i = 0; i < sizes[0]; i++) {
            for (int j = 0; j < sizes[1]; j++) {
                for (int k = 0; k < sizes[2]; k++) {
                    int actualIndex = counter.getCount(i, j, k);
                    assertEquals(expectedIndex, actualIndex);
                    expectedCounts[i][j][k] = expectedIndex++;
                }
            }
        }
    }

    @Test 
    public void testOutOfDimensionMismatch() {
        int[] sizes = {2, 3, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        try {
            int[] counts = counter.getCount(1, 2);
            fail("Expected DimensionMismatchException to be thrown");
        } catch (DimensionMismatchException e) {
            // expected exception
        }
    }

    @Test 
    public void testCountOutOfRangeForDimension() {
        int[] sizes = {2, 3, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        try {
            int[] counts = counter.getCount(1, 5, 2);
            fail("Expected OutOfRangeException to be thrown");
        } catch (OutOfRangeException e) {
            // expected exception
        }

        try {
            int[] counts = counter.getCount(1, 2, -1);
            fail("Expected OutOfRangeException to be thrown");
        } catch (OutOfRangeException e) {
            // expected exception
        }
    }

    @Test
    public void testToString() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        String expected = "[0][0][0]";
        String actual = counter.toString();
        assertEquals(expected, actual);
    }
}