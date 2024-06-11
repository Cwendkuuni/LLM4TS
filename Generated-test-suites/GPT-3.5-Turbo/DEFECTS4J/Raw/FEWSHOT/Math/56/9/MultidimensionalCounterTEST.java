import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

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

        counter = new MultidimensionalCounter(0, 0, 0);
        assertEquals(0, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertArrayEquals(new int[]{2, 4, 3}, counter.getSizes());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(-2, 4, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 0, 3);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        assertEquals(-1, iterator.getCount());
        assertArrayEquals(new int[]{0, 0, 0}, iterator.getCounts());

        int count = 0;
        while (iterator.hasNext()) {
            int nextCount = iterator.next();
            assertEquals(count, nextCount);
            count++;

            int[] expectedCounts = counter.getCounts(nextCount);
            assertArrayEquals(expectedCounts, iterator.getCounts());
        }
        assertEquals(23, iterator.getCount());
        assertArrayEquals(new int[]{1, 3, 2}, iterator.getCounts());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{1, 3, 2}, counter.getCounts(23));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCounts(24);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(23, counter.getCount(1, 3, 2));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(1, 3);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(1, 4, 2);
    }
}