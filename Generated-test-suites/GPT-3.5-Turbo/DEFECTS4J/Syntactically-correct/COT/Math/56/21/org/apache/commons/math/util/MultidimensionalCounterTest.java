
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int dimension = counter.getDimension();
        assertEquals(3, dimension);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int size = counter.getSize();
        assertEquals(24, size);
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] sizes = counter.getSizes();
        int[] expectedSizes = {2, 4, 3};
        assertArrayEquals(expectedSizes, sizes);
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] counts = counter.getCounts(23);
        int[] expectedCounts = {1, 3, 2};
        assertArrayEquals(expectedCounts, counts);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int count = counter.getCount(1, 3, 2);
        assertEquals(23, count);
    }
}
