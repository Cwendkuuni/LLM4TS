import org.apache.commons.math.util.MultidimensionalCounter;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

/**
 * Test class for MultidimensionalCounter.
 */
public class MultidimensionalCounterTest {

    /**
     * Test constructor with valid parameters.
     */
    @Test
    public void testConstructorValidParameters() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Assert.assertEquals(6 * 4 * 3, counter.getSize());
        Assert.assertArrayEquals(new int[]{2, 4, 3}, counter.getSizes());
    }

    /**
     * Test constructor with negative or zero parameter.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeOrZeroParameter() {
        int[] size = {-1, 4, 3};
        new MultidimensionalCounter(size);
    }

    /**
     * Test getCounts method with valid index.
     */
    @Test
    public void testGetCountsValidIndex() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] expected = {1, 0, 0};
        int[] result = counter.getCounts(3);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test getCounts method with out of range index.
     */
    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRangeIndex() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCounts(6 * 4 * 3 + 1);
    }

    /**
     * Test getCount method with valid index.
     */
    @Test
    public void testGetCountValidIndex() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int expected = 3 * 4 * 2 + 1;
        int result = counter.getCount(1, 1, 1);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test getCount method with out of range index.
     */
    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeIndex() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(-1, 1, 1);
    }

    /**
     * Test getCount method with dimension mismatch index.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatchIndex() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(1, 5, 1);
    }

    /**
     * Test iterator hasNext method.
     */
    @Test
    public void testIteratorHasNext() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator<Integer> iterator = counter.iterator();
        Assert.assertTrue(iterator.hasNext());
        for (int i = 0; i < 6 * 4 * 3; i++) {
            Assert.assertTrue(iterator.hasNext());
        }
        Assert.assertFalse(iterator.hasNext());
    }

    /**
     * Test iterator next method.
     */
    @Test
    public void testIteratorNext() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator<Integer> iterator = counter.iterator();
        for (int i = 0; i < 6 * 4 * 3; i++) {
            int expected = i;
            int actual = iterator.next();
            Assert.assertEquals(expected, actual);
        }
        Assert.assertFalse(iterator.hasNext());
    }

    /**
     * Test iterator getCount method.
     */
    @Test
    public void testIteratorGetCount() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator<Integer> iterator = counter.iterator();
        for (int i = 0; i < 6 * 4 * 3; i++) {
            int expected = i;
            int[] actual = iterator.getCounts();
            Assert.assertArrayEquals(new int[]{expected / (6 * 4 * 3) % 2,
                    expected / (4 * 3) % 4, expected % 3}, actual);
            int actualCount = iterator.getCount();
            Assert.assertEquals(expected, actualCount);
        }
        Assert.assertFalse(iterator.hasNext());
    }
}