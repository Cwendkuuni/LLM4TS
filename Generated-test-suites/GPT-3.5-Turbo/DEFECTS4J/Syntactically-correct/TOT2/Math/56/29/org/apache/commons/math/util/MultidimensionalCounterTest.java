import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.util.MultidimensionalCounter;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;

public class MultidimensionalCounterTest {

    @Test
    public void testGetDimension() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSize() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetCount() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(4, counter.getCount(1, 1));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCounts(6);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(1, 2, 3); // not matching dimensions
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterNotStrictlyPositive() {
        int[] sizes = {0, 2, 3}; // zero size
        new MultidimensionalCounter(sizes);
    }

    // Add more test cases for edge cases and error scenarios

}