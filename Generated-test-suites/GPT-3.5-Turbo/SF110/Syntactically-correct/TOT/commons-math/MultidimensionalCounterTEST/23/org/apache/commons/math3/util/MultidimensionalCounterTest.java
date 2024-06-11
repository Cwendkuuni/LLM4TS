package org.apache.commons.math3.util;

import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstructor_WhenSizeIsPositive_ExpectNoException() {
        try {
            int[] size = {2, 3, 4};
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
        } catch (Exception e) {
            Assert.fail("Unexpected exception");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterConstructor_WhenSizeIsZero_ExpectNotStrictlyPositiveException() {
        int[] size = {0, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterConstructor_WhenSizeIsNegative_ExpectNotStrictlyPositiveException() {
        int[] size = {2, -3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
    }

    @Test
    public void testGetDimension_WhenConstructedWithSize_ExpectCorrectDimension() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int dimension = counter.getDimension();

        Assert.assertEquals(3, dimension);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_WhenIndexIsOutOfRange_ExpectOutOfRangeException() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int index = counter.getSize() + 1;

        counter.getCounts(index);
    }

    @Test
    public void testGetCounts_WhenIndexIsInRange_ExpectCorrectCounts() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int index = 9;

        int[] counts = counter.getCounts(index);

        int[] expectedCounts = {1, 2, 1};
        Assert.assertArrayEquals(expectedCounts, counts);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_WhenLengthOfArrayDoesNotMatchDimension_ExpectDimensionMismatchException() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int[] c = {1, 2};

        counter.getCount(c);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_WhenIndexIsOutOfRange_ExpectOutOfRangeException() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int[] c = {1, 2, 5};

        counter.getCount(c);
    }

    @Test
    public void testGetCount_WhenIndexIsInRange_ExpectCorrectCount() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int[] c = {1, 2, 1};

        int count = counter.getCount(c);

        Assert.assertEquals(14, count);
    }

    @Test
    public void testGetSize_WhenConstructedWithSize_ExpectCorrectTotalSize() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int totalSize = counter.getSize();

        Assert.assertEquals(24, totalSize);
    }

    @Test
    public void testGetSizes_WhenConstructedWithSize_ExpectCorrectSizes() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int[] sizes = counter.getSizes();

        int[] expectedSizes = {2, 3, 4};
        Assert.assertArrayEquals(expectedSizes, sizes);
    }

    @Test
    public void testIterator_WhenConstructed_ExpectHasNext() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        MultidimensionalCounter.Iterator iterator = counter.iterator();

        Assert.assertTrue(iterator.hasNext());
    }

    @Test
    public void testIterator_Next_ExpectCorrectCount() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        MultidimensionalCounter.Iterator iterator = counter.iterator();

        int count = iterator.next();

        Assert.assertEquals(0, count);
    }

    @Test
    public void testIterator_GetCount_ExpectCorrectCount() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        MultidimensionalCounter.Iterator iterator = counter.iterator();

        iterator.next();
        iterator.next();
        
        int count = iterator.getCount();
        
        Assert.assertEquals(2, count);
    }

    @Test
    public void testIterator_GetCounts_ExpectCorrectCounts() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        MultidimensionalCounter.Iterator iterator = counter.iterator();

        iterator.next();
        iterator.next();
        
        int[] counts = iterator.getCounts();
        
        int[] expectedCounts = {0, 0, 2};
        Assert.assertArrayEquals(expectedCounts, counts);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_Remove_ExpectUnsupportedOperationException() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        MultidimensionalCounter.Iterator iterator = counter.iterator();

        iterator.remove();
    }

}