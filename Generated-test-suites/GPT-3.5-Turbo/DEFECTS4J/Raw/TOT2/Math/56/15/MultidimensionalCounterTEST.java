import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;

public class MultidimensionalCounterTest {

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        assertEquals(60, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        int[] expectedSizes = {3, 4, 5};
        assertArrayEquals(expectedSizes, counter.getSizes());
    }

    @Test
    public void testCountsToIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        int[] expectedCounts = {1, 2, 3};
        assertEquals(23, counter.getCount(expectedCounts));
    }

    @Test(expected = OutOfRangeException.class)
    public void testCountsToIndexWithOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        int[] counts = {6, 4, 5};
        counter.getCount(counts);
    }

    @Test
    public void testIndexToCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        int[] expectedCounts = {1, 2, 3};
        assertArrayEquals(expectedCounts, counter.getCounts(23));
    }

    @Test(expected = OutOfRangeException.class)
    public void testIndexToCountsWithOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        counter.getCounts(61);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testCountWithInvalidLengthOfCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        int[] counts = {1, 2};
        counter.getCount(counts);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testCounterWithNegativeSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(-3, 4, 5);
    }
}