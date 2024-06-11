import org.junit.Test;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;

public class MultidimensionalCounterTest {

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        int expectedCount = 0;
        while (iterator.hasNext()) {
            int count = iterator.next();
            assert count == expectedCount;
            expectedCount++;
        }
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        int[][] expectedCounts = {{0, 0, 0}, {0, 0, 1}, {0, 1, 0}, {0, 1, 1}, {1, 0, 0}, {1, 0, 1}, {1, 1, 0}, {1, 1, 1}};

        for (int i = 0; i < counter.getSize(); i++) {
            int[] counts = counter.getCounts(i);
            assertArrayEquals(counts, expectedCounts[i]);
        }
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        int[][] counts = {{0, 0, 0}, {0, 0, 1}, {0, 1, 0}, {0, 1, 1}, {1, 0, 0}, {1, 0, 1}, {1, 1, 0}, {1, 1, 1}};

        for (int i = 0; i < counts.length; i++) {
            int count = counter.getCount(counts[i]);
            assert count == i;
        }
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        assert counter.getSize() == 8;
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        int[] sizes = counter.getSizes();
        int[] expectedSizes = {2, 2, 2};

        assertArrayEquals(sizes, expectedSizes);
    }
}