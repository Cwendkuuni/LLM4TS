import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.math3.util.MathUtils.copyOf;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
        Assert.assertEquals(3, multidimensionalCounter.getDimension());
        Assert.assertArrayEquals(size, multidimensionalCounter.getSizes());
        Assert.assertEquals(2 * 3 * 4, multidimensionalCounter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeSize() {
        int[] size = {2, -1, 3};
        new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroSize() {
        int[] size = {2, 0, 3};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testIteratorHasNext() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
        Iterator iterator = multidimensionalCounter.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
        Iterator iterator = multidimensionalCounter.iterator();
        int expectedCount = 0;
        int[] expectedCounts = new int[3];
        while (iterator.hasNext()) {
            int actualCount = iterator.next();
            Assert.assertEquals(expectedCount, actualCount);
            expectedCount++;
            int[] actualCounts = iterator.getCounts();
            Assert.assertArrayEquals(copyOf(expectedCounts), actualCounts);
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetCountNegativeIndex() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
        multidimensionalCounter.getCount(-1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDifferentDimensionSize() {
        int[] size1 = {2, 3};
        int[] size2 = {4, 3};
        MultidimensionalCounter multidimensionalCounter1 = new MultidimensionalCounter(size1);
        MultidimensionalCounter multidimensionalCounter2 = new MultidimensionalCounter(size2);
        multidimensionalCounter1.getCount(size2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
        multidimensionalCounter.getCount(6 * size[0] * size[1] + size[2] * size[1] + size[1]);
    }

    @Test
    public void testGetCount() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
        int[] c = {1, 2, 3};
        int actualCount = multidimensionalCounter.getCount(c);
        int expectedCount = 1 * size[0] * size[1] * size[2] + 1 * size[0] * size[1] * 3 + 1 * size[0] * 2 + 1;
        Assert.assertEquals(expectedCount, actualCount);
    }
}