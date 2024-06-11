import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertNotNull(counter);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(size, counter.getSizes());
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetCounts() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] counts = counter.getCounts(4);
        assertArrayEquals(new int[]{0, 1}, counts);
    }

    @Test
    public void testGetCount() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int count = counter.getCount(1, 0);
        assertEquals(3, count);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstructor() {
        int[] size = {0, 1, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCounts(10);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(1, 2, 3);
    }

    // Add more test cases here for edge cases and error scenarios

}