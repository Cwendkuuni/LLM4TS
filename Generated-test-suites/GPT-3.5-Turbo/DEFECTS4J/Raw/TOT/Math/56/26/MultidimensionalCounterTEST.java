import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] counts = counter.getCounts(12);
        assertArrayEquals(new int[]{1, 0, 0}, counts);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCounts(50);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int count = counter.getCount(1, 0, 0);
        assertEquals(12, count);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(1, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(2, 0, 0);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int[] counts = iterator.getCounts();
        assertArrayEquals(new int[]{0, 0, 0}, counts);
        assertEquals(0, iterator.getCount());

        assertTrue(iterator.hasNext());
        int nextCount = iterator.next();
        assertEquals(0, nextCount);
        counts = iterator.getCounts();
        assertArrayEquals(new int[]{0, 0, 1}, counts);
        assertEquals(1, iterator.getCount());

        for (int i = 0; i < 5; i++) {
            assertTrue(iterator.hasNext());
            nextCount = iterator.next();
        }
        assertEquals(5, nextCount);
        counts = iterator.getCounts();
        assertArrayEquals(new int[]{1, 0, 1}, counts);
        assertEquals(5, iterator.getCount());

        for (int i = 0; i < 10; i++) {
            assertTrue(iterator.hasNext());
            nextCount = iterator.next();
        }
        assertEquals(15, nextCount);
        counts = iterator.getCounts();
        assertArrayEquals(new int[]{1, 1, 3}, counts);
        assertEquals(15, iterator.getCount());

        assertFalse(iterator.hasNext());
    }
}