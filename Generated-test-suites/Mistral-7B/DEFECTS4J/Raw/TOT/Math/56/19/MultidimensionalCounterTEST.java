package org.apache.commons.math.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for MultidimensionalCounter class.
 */
public class MultidimensionalCounterTest {
    private MultidimensionalCounter multidimensionalCounter;

    /**
     * Set up before each test.
     */
    @Before
    public void setUp() {
        multidimensionalCounter = new MultidimensionalCounter(2, 3, 3);
    }

    /**
     * Test constructor with valid sizes.
     */
    @Test
    public void testValidSizesConstructor() {
        assertEquals(3, multidimensionalCounter.getDimension());
        assertEquals(2, multidimensionalCounter.size[0]);
        assertEquals(3, multidimensionalCounter.size[1]);
        assertEquals(18, multidimensionalCounter.getSize());
    }

    /**
     * Test iterator.
     */
    @Test
    public void testIterator() {
        Iterator iterator = multidimensionalCounter.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            assertEquals(index, next);
            index++;
        }
    }

    /**
     * Test getting multidimensional counter slots.
     */
    @Test
    public void testGetCounts() {
        int[] counts = multidimensionalCounter.getCounts(0);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);

        for (int i = 0; i < multidimensionalCounter.getSize(); i++) {
            counts = multidimensionalCounter.getCounts(i);
            for (int j = 0; j < multidimensionalCounter.getDimension(); j++) {
                assertEquals(j, counts[j]);
            }
        }
    }

    /**
     * Test converting from multidimensional to unidimensional counter.
     */
    @Test
    public void testGetCount() {
        for (int i = 0; i < multidimensionalCounter.getSize(); i++) {
            int[] counts = multidimensionalCounter.getCounts(i);
            int result = multidimensionalCounter.getCount(counts);
            assertEquals(i, result);
        }
    }

    /**
     * Test getting total number of elements.
     */
    @Test
    public void testGetSize() {
        assertEquals(18, multidimensionalCounter.getSize());
    }

    /**
     * Test getting sizes in each dimension.
     */
    @Test
    public void testGetSizes() {
        int[] sizes = multidimensionalCounter.getSizes();
        assertEquals(2, sizes[0]);
        assertEquals(3, sizes[1]);
    }

    /**
     * Test invalid constructor size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidSizesConstructor() {
        new MultidimensionalCounter(-1, 3, 3);
    }
}