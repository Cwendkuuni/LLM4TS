import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {
    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] expectedSizes = {2, 4, 3};
        assertArrayEquals(expectedSizes, counter.getSizes());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] counts = counter.getCounts(12);
        int[] expectedCounts = {1, 0, 0};
        assertArrayEquals(expectedCounts, counts);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int count = counter.getCount(1, 2, 1);
        assertEquals(17, count);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] counts = counter.getCounts(30);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int count = counter.getCount(1, 2);
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testIteratorHasNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        for (int i = 0; i < 23; i++) {
            iterator.next();
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int[] expectedCounts = {0, 0, 0};
        assertEquals(0, (int) iterator.next());
        assertArrayEquals(expectedCounts, iterator.getCounts());
        expectedCounts = new int[]{0, 0, 1};
        assertEquals(1, (int) iterator.next());
        assertArrayEquals(expectedCounts, iterator.getCounts());
        expectedCounts = new int[]{0, 0, 2};
        assertEquals(2, (int) iterator.next());
        assertArrayEquals(expectedCounts, iterator.getCounts());
    }

    // 
    // Generated test cases from above
    // 
}