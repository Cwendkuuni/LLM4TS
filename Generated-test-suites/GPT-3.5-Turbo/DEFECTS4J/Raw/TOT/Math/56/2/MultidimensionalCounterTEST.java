import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;
    private final int[] sizes = {2, 4, 3};

    @Before
    public void setUp() {
        counter = new MultidimensionalCounter(sizes);
    }

    @Test
    public void testGetDimension() {
        assertEquals(sizes.length, counter.getDimension());
    }

    @Test
    public void testGetSize() {
        int totalSize = sizes[0] * sizes[1] * sizes[2];
        assertEquals(totalSize, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        int[] expectedSizes = {2, 4, 3};
        assertArrayEquals(expectedSizes, counter.getSizes());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSize() {
        int[] sizes = {-1};
        MultidimensionalCounter invalidCounter = new MultidimensionalCounter(sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroSize() {
        int[] sizes = {0};
        MultidimensionalCounter invalidCounter = new MultidimensionalCounter(sizes);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsInvalidIndex() {
        int index = -1;
        counter.getCounts(index);
    }

    @Test
    public void testGetCountsValidIndex() {
        int index = 23;
        int[] expectedCounts = {1, 3, 2};
        assertArrayEquals(expectedCounts, counter.getCounts(index));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountInvalidLength() {
        int[] c = {1, 2};
        counter.getCount(c);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountInvalidRange() {
        int[] c = {1, 2, 3}; // Invalid count for last dimension
        counter.getCount(c);
    }

    @Test
    public void testGetCountValidRange() {
        int[] c = {1, 2, 2};
        int expectedCount = 23;
        assertEquals(expectedCount, counter.getCount(c));
    }

    @Test
    public void testIteratorNext() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            int count = iterator.next();
            assertEquals(index, iterator.getCount());
            index++;
        }
        assertEquals(counter.getSize() - 1, iterator.getCount());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }

}