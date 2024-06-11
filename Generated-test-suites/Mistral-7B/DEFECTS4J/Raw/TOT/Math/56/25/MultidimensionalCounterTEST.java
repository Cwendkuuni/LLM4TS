package org.apache.commons.math.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link MultidimensionalCounter}.
 */
public class MultidimensionalCounterTest {

    /**
     * Test constructor with valid sizes.
     */
    @Test
    public void testValidSizes() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(sizes.length, counter.getDimension());
        assertEquals(MathUtils.multiply(MathUtils.product(sizes), sizes[sizes.length - 1]), counter.getSize());
        assertEquals(sizes, counter.getSizes());
    }

    /**
     * Test constructor with empty size array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptySizes() {
        new MultidimensionalCounter(new int[0]);
    }

    /**
     * Test constructor with negative sizes.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSizes() {
        new MultidimensionalCounter(-1, 2, 3);
    }

    /**
     * Test iterator with valid indices.
     */
    @Test
    public void testIteratorValidIndices() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        int count = -1;
        int[] multidimensionalCount = null;
        int unidimensionalCount = -1;
        while (iterator.hasNext()) {
            unidimensionalCount = iterator.next();
            multidimensionalCount = iterator.getCounts();

            // Check multidimensional counts
            for (int i = 0; i < sizes.length; i++) {
                assertEquals(sizes[i], multidimensionalCount[i]);
            }
            // Check unidimensional count
            assertEquals(iterator.getCount(), unidimensionalCount);
            count++;
        }
        assertEquals(MathUtils.multiply(MathUtils.product(sizes), sizes[sizes.length - 1]), count);
    }

    /**
     * Test iterator with out-of-range index.
     */
    @Test(expected = OutOfRangeException.class)
    public void testIteratorOutOfRangeIndex() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        int index = MathUtils.multiply(MathUtils.product(sizes), sizes[sizes.length - 1]) + 1;
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        while (iterator.hasNext()) {
            iterator.next();
        }
        iterator.next();
    }

    /**
     * Test iterator with invalid multidimensional count.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testIteratorInvalidMultidimensionalCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        int[] invalidCount = new int[sizes.length + 1];
        while (iterator.hasNext()) {
            iterator.next();
            iterator.next();
            iterator.next();
            iterator.next();
            iterator.next();
            iterator.next();
            iterator.next();
            assertEquals(iterator.getCounts(), invalidCount);
        }
    }
}