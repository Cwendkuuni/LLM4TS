
import static org.junit.Assert.*;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Before;
import org.junit.Test;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Before
    public void setUp() {
        counter = new MultidimensionalCounter(2, 4, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorThrowsNotStrictlyPositiveException() {
        counter = new MultidimensionalCounter(-1, 4, 3);
    }

    @Test
    public void testGetSizes() {
        int[] sizes = {2, 4, 3};
        assertArrayEquals(sizes, counter.getSizes());
    }

    @Test
    public void testGetSize() {
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetCounts() {
        int[] counts = {1, 2, 1};
        assertArrayEquals(counts, counter.getCounts(13));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsThrowsOutOfRangeException() {
        counter.getCounts(30);
    }

    @Test
    public void testGetCount() {
        assertEquals(13, counter.getCount(1, 2, 1));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountThrowsDimensionMismatchException() {
        counter.getCount(1, 2, 1, 4);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountThrowsOutOfRangeException() {
        counter.getCount(1, 2, 5);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = 0;
        int[] expectedCounts = {0, 0, 0};
        while (iterator.hasNext()) {
            assertEquals(count, iterator.next().intValue());
            assertArrayEquals(expectedCounts, iterator.getCounts());
            expectedCounts = incrementCounts(expectedCounts);
            count++;
        }
    }

    private int[] incrementCounts(int[] counts) {
        int dimension = counts.length;
        for (int i = dimension - 1; i >= 0; i--) {
            counts[i]++;
            if (counts[i] < counter.getSizes()[i]) {
                break;
            } else {
                counts[i] = 0;
            }
        }
        return counts;
    }
}
