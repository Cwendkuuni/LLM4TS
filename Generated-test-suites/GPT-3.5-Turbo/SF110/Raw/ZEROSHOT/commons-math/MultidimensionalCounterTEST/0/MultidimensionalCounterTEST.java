import org.apache.commons.math3.exception.*;
import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        Assert.assertEquals(3, counter.getDimension());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithDimensionMismatchException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCounts(1, 2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsWithOutOfRangeException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCounts(10, 2, 3);
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] expectedCounts = {0, 0, 0};
        Assert.assertArrayEquals(expectedCounts, counter.getCounts(0));

        int[] expectedCounts2 = {1, 0, 0};
        Assert.assertArrayEquals(expectedCounts2, counter.getCounts(1));

        int[] expectedCounts3 = {1, 2, 0};
        Assert.assertArrayEquals(expectedCounts3, counter.getCounts(5));

        int[] expectedCounts4 = {1, 2, 3};
        Assert.assertArrayEquals(expectedCounts4, counter.getCounts(23));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWithDimensionMismatchException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(1, 2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithOutOfRangeException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(10, 2, 3);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        Assert.assertEquals(0, counter.getCount(0, 0, 0));
        Assert.assertEquals(5, counter.getCount(1, 0, 0));
        Assert.assertEquals(23, counter.getCount(1, 2, 3));
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        Assert.assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] expectedSizes = {2, 3, 4};
        Assert.assertArrayEquals(expectedSizes, counter.getSizes());
    }
}