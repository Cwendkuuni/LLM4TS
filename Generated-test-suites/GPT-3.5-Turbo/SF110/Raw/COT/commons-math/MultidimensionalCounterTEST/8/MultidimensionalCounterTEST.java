import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

public class MultidimensionalCounterTest {

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        Assert.assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        Assert.assertEquals(60, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        int[] expectedSizes = {3, 4, 5};
        Assert.assertArrayEquals(expectedSizes, counter.getSizes());
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        int[] expectedCounts = {1, 2, 3};
        Assert.assertArrayEquals(expectedCounts, counter.getCounts(38));
    }

    @Test
    public void testGetCountsOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        try {
            counter.getCounts(61);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetCountsInvalidDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        try {
            counter.getCounts(1, 2);
            Assert.fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetCount() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        Assert.assertEquals(38, counter.getCount(1, 2, 3));
    }

    @Test
    public void testGetCountOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        try {
            counter.getCount(3, 5, 2);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetCountDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        try {
            counter.getCount(1, 2);
            Assert.fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4);
        Iterator<Integer> iterator = counter.iterator();

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(0, iterator.next().intValue());

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(1, iterator.next().intValue());

        // Test multiple iterations
        for (int i = 0; i < 10; i++) {
            iterator.hasNext();
            iterator.next();
        }

        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4);
        Iterator<Integer> iterator = counter.iterator();

        iterator.remove();
    }

    @Test
    public void testIteratorGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4);
        Iterator<Integer> iterator = counter.iterator();

        Assert.assertEquals(-1, ((MultidimensionalCounter.Iterator) iterator).getCount());

        iterator.next();
        Assert.assertEquals(0, ((MultidimensionalCounter.Iterator) iterator).getCount());
    }

    @Test
    public void testIteratorGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4);
        Iterator<Integer> iterator = counter.iterator();

        int[] expectedCounts = {0, 0};
        Assert.assertArrayEquals(expectedCounts, ((MultidimensionalCounter.Iterator) iterator).getCounts());

        iterator.next();
        expectedCounts[1] = 1;
        Assert.assertArrayEquals(expectedCounts, ((MultidimensionalCounter.Iterator) iterator).getCounts());
    }

    @Test
    public void testIteratorGetCountDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4);
        Iterator<Integer> iterator = counter.iterator();

        Assert.assertEquals(0, ((MultidimensionalCounter.Iterator) iterator).getCount(0));

        iterator.next();
        Assert.assertEquals(0, ((MultidimensionalCounter.Iterator) iterator).getCount(0));
        Assert.assertEquals(1, ((MultidimensionalCounter.Iterator) iterator).getCount(1));
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        Assert.assertEquals("[1][2][3]", counter.toString());
    }
}