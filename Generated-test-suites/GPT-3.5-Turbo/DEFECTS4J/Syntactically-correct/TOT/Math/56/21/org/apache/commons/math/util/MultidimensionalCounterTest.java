import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testIteratorHasNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        // Test typical use case - counter has next element
        assertTrue(iterator.hasNext());

        // Test typical use case - counter does not have next element
        for (int i = 0; i < counter.getSize() - 1; i++) {
            iterator.next();
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        // Test typical use case - iterator returns next element
        assertEquals(Integer.valueOf(0), iterator.next());
        assertEquals(Integer.valueOf(1), iterator.next());
        assertEquals(Integer.valueOf(2), iterator.next());
    }

    @Test
    public void testIteratorGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        // Test typical use case - iterator returns current count
        assertEquals(0, iterator.getCount());

        // Test typical use case - iterator returns count in specific dimension
        assertEquals(0, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        assertEquals(0, iterator.getCount(2));
        iterator.next();
        assertEquals(1, iterator.getCount(2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        // Test error scenario - iterator remove is unsupported
        iterator.remove();
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        // Test typical use case - convert index to counts
        assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{1, 0, 0}, counter.getCounts(12));
        assertArrayEquals(new int[]{1, 2, 1}, counter.getCounts(23));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        // Test error scenario - index out of range
        counter.getCounts(24);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        // Test typical use case - convert counts to index
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(12, counter.getCount(1, 0, 0));
        assertEquals(23, counter.getCount(1, 2, 1));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        // Test error scenario - counts size does not match dimension
        counter.getCount(0, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        // Test error scenario - count value out of range
        counter.getCount(2, 0, 0);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        // Test typical use case - get total size
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        // Test typical use case - get sizes of dimensions
        assertArrayEquals(new int[]{2, 3, 4}, counter.getSizes());
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        // Test typical use case - convert counter to string representation
        assertEquals("[0][0][0]", counter.toString());
        counter.getCount(1, 2, 3);
        assertEquals("[1][2][3]", counter.toString());
    }
}