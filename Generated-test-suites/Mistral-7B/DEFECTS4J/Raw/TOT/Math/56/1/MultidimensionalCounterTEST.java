package org.apache.commons.math.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class MultidimensionalCounterTest {

    @Test
    public void testEmptyMultidimensionalCounter() {
        int[] emptySize = new int[0];
        MultidimensionalCounter counter = new MultidimensionalCounter(emptySize);
        assertFalse(counter.hasNext());
    }

    @Test
    public void testZeroDimMultidimensionalCounter() {
        MultidimensionalCounter counter = new MultidimensionalCounter();
        assertFalse(counter.hasNext());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSize() {
        int[] negativeSize = new int[]{-1, 2};
        new MultidimensionalCounter(negativeSize);
    }

    @Test
    public void testOneDimensionalCounter() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2});
        Iterator iterator = counter.iterator();
        int[] counts = new int[1];
        int actualCount = iterator.next();
        counts[0] = actualCount;
        int expectedCount = 0;
        assertEquals(expectedCount, counts[0]);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testTwoDimensionalCounter() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 2});
        Iterator iterator = counter.iterator();
        int[] counts = new int[2];
        int actualCount = iterator.next();
        counts[0] = actualCount;
        int expectedCount = 0;
        assertEquals(expectedCount, counts[0]);
        assertTrue(iterator.hasNext());
        iterator.next();
        counts[1] = iterator.next().getCount();
        expectedCount = 1;
        assertEquals(expectedCount, counts[1]);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 2, 2});
        Iterator iterator = counter.iterator();
        int[] counts = new int[3];
        iterator.next();
        counts[0] = iterator.getCount(0);
        counts[1] = iterator.getCount(1);
        counts[2] = iterator.getCount(2);
        int[] expectedCounts = new int[]{0, 0, 0};
        assertArrayEquals(expectedCounts, counts);
        assertTrue(iterator.hasNext());
        iterator.next();
        counts = new int[3];
        counts[0] = iterator.getCount(0);
        counts[1] = iterator.getCount(1);
        counts[2] = iterator.getCount(2);
        expectedCounts = new int[]{0, 1, 0};
        assertArrayEquals(expectedCounts, counts);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 2, 2});
        Iterator iterator = counter.iterator();
        int actualCount = iterator.next().getCount();
        int expectedCount = 0;
        assertEquals(expectedCount, actualCount);
        assertTrue(iterator.hasNext());
        actualCount = iterator.next().getCount();
        expectedCount = 1;
        assertEquals(expectedCount, actualCount);
        assertFalse(iterator.hasNext());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWithWrongNumberOfDimensions() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 2, 2});
        int[] counts = new int[1];
        counter.getCount(counts);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithOutOfRangeValue() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 2, 2});
        Iterator iterator = counter.iterator();
        int[] counts = new int[3];
        iterator.next();
        iterator.next();
        int actualCount = iterator.next().getCount(1);
        int expectedCount = 2;
        assertEquals(expectedCount, actualCount);
        int indexOutOfRange = 3;
        counter.getCount(indexOutOfRange, counts);
    }

    @Test
    public void testSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 2, 2});
        int expectedSize = 8;
        int actualSize = counter.getSize();
        assertEquals(expectedSize, actualSize);
    }

    @Test   
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 2, 2});
        Iterator iterator = counter.iterator();
        String expectedString = "[0][0][0]";
        String actualString = iterator.next().toString();
        assertEquals(expectedString, actualString);
        expectedString = "[0][0][1]";
        actualString = iterator.next().toString();
        assertEquals(expectedString, actualString);
        expectedString = "[0][1][0]";
        actualString = iterator.next().toString();
        assertEquals(expectedString, actualString);
        expectedString = "[1][0][0]";
        actualString = iterator.next().toString();
        assertEquals(expectedString, actualString);
        expectedString = "[1][0][1]";
        actualString = iterator.next().toString();
        assertEquals(expectedString, actualString);
        expectedString = "[1][1][0]";
        actualString = iterator.next().toString();
        assertEquals(expectedString, actualString);
        expectedString = "[0][1][1]";
        actualString = iterator.next().toString();
        assertEquals(expectedString, actualString);
        expectedString = "[1][1][1]";
        actualString = iterator.next().toString();
        assertEquals(expectedString, actualString);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterateOverAllElements() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 2, 2});
        int[] expectedCounts = new int[8];
        int index = 0;
        Iterator iterator = counter.iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            int[] counts = iterator.getCounts();
            expectedCounts[index++] = Arrays.hashCode(counts);
        }
        int[] actualCounts = new int[8];
        iterator = counter.iterator();
        while (iterator.hasNext()) {
            actualCounts[index++] = iterator.getCount();
        }
        assertArrayEquals(expectedCounts, actualCounts);
    }
}