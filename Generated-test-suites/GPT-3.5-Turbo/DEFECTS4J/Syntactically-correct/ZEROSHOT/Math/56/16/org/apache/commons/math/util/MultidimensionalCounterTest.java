import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {
    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] expectedSizes = {2, 4, 3};
        assertArrayEquals(expectedSizes, counter.getSizes());
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] expectedCounts = {0, 0, 0};
        assertArrayEquals(expectedCounts, counter.getCounts(0));
        expectedCounts = new int[]{0, 0, 1};
        assertArrayEquals(expectedCounts, counter.getCounts(1));
        expectedCounts = new int[]{1, 0, 0};
        assertArrayEquals(expectedCounts, counter.getCounts(12));
        expectedCounts = new int[]{1, 3, 2};
        assertArrayEquals(expectedCounts, counter.getCounts(23));
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(3, counter.getCount(0, 1, 0));
        assertEquals(12, counter.getCount(1, 0, 0));
        assertEquals(23, counter.getCount(1, 3, 2));
    }
}