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

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(-2, 4, 3);
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] counts = counter.getCounts(23);
        int[] expectedCounts = {1, 3, 2};
        assertArrayEquals(expectedCounts, counts);
    }

    @Test(expected = OutOfRangeException.class)
    public void testInvalidIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] counts = counter.getCounts(30);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int count = counter.getCount(1, 3, 2);
        assertEquals(23, count);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidCountsDimensions() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int count = counter.getCount(1, 3);
    }

    @Test(expected = OutOfRangeException.class)
    public void testInvalidCountsRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int count = counter.getCount(1, 5, 2);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next().intValue());
        assertEquals(1, iterator.next().intValue());
        assertEquals(2, iterator.next().intValue());
        assertEquals(3, iterator.next().intValue());
        assertEquals(4, iterator.next().intValue());
        // Continue testing all elements

    }
}