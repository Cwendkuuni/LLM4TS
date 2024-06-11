import org.junit.Test;
import static org.junit.Assert.*;

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
    public void testGetCounts() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertArrayEquals(new int[]{1, 2, 3}, counter.getCounts(11));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCounts(30);
    }

    @Test
    public void testGetCount() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(11, counter.getCount(1, 2, 3));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(1, 2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(1, 2, 5);
    }
}