import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testIteratorHasNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 2, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        for (int i = 0; i < counter.getSize() - 1; i++) {
            iterator.next();
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(Integer.valueOf(0), iterator.next());
        int[] expectedCounts = {0, 0, 1};
        assertArrayEquals(expectedCounts, iterator.getCounts());
        assertEquals(Integer.valueOf(1), iterator.next());
        expectedCounts = new int[]{0, 0, 2};
        assertArrayEquals(expectedCounts, iterator.getCounts());
        // ... continue with similar assertions for all other test cases
    }

    @Test
    public void testIteratorGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(0, iterator.getCount());
        assertEquals(0, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        assertEquals(0, iterator.getCount(2));
        iterator.next();
        assertEquals(1, iterator.getCount());
        assertEquals(0, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        assertEquals(1, iterator.getCount(2));
        // ... continue with similar assertions for all other test cases
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int[] indices = counter.getCounts(0);
        int[] expectedCounts = {0, 0};
        assertArrayEquals(expectedCounts, indices);

        indices = counter.getCounts(3);
        expectedCounts = new int[]{1, 0};
        assertArrayEquals(expectedCounts, indices);

        indices = counter.getCounts(4);
        expectedCounts = new int[]{0, 1};
        assertArrayEquals(expectedCounts, indices);

        indices = counter.getCounts(5);
        expectedCounts = new int[]{1, 1};
        assertArrayEquals(expectedCounts, indices);

        // ... continue with similar assertions for all other test cases
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCounts(-1);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int count = counter.getCount(1, 2, 3);
        assertEquals(23, count);

        count = counter.getCount(0, 1, 1);
        assertEquals(9, count);

        // ... continue with similar assertions for all other test cases
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int count = counter.getCount(1, 2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int count = counter.getCount(2, 3, 4);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int size = counter.getSize();
        assertEquals(24, size);
    }

    @Test
    public void testGetDimensions() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int dimension = counter.getDimension();
        assertEquals(3, dimension);
    }
}