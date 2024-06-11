import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iter = counter.iterator();

        // Check hasNext() method
        assertTrue(iter.hasNext());

        // Check next() method
        int count = 0;
        while (iter.hasNext()) {
            int actualCount = iter.next();
            assertEquals(count, actualCount);
            count++;
        }
        assertEquals(24, count);

        // Check getCount() method
        assertEquals(24, iter.getCount());

        // Check getCounts() method
        int[] expectedCounts = {1, 3, 2};
        int[] actualCounts = iter.getCounts();
        assertArrayEquals(expectedCounts, actualCounts);

        // Check getCount(int dim) method
        assertEquals(2, iter.getCount(0));
        assertEquals(3, iter.getCount(1));
        assertEquals(1, iter.getCount(2));
    }

    @Test
    public void testMultidimensionalCounter() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);

        // Check getDimension() method
        assertEquals(3, counter.getDimension());

        // Check getCounts(int index) method
        int[] expectedCounts = {0, 1, 2};
        int[] actualCounts = counter.getCounts(2);
        assertArrayEquals(expectedCounts, actualCounts);

        // Check getCount() method
        int[] counts = {1, 3, 2};
        int expectedCount = 23;
        int actualCount = counter.getCount(counts);
        assertEquals(expectedCount, actualCount);

        // Check getSize() method
        assertEquals(24, counter.getSize());

        // Check getSizes() method
        int[] expectedSizes = {2, 4, 3};
        int[] actualSizes = counter.getSizes();
        assertArrayEquals(expectedSizes, actualSizes);
    }
}