
import org.junit.Test;
import org.apache.commons.math3.util.MultidimensionalCounter;

import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int dimension = counter.getDimension();
        assertEquals(2, dimension);
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int[] counts = counter.getCounts(3);
        int[] expectedCounts = {1, 0};
        assertArrayEquals(expectedCounts, counts);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int count = counter.getCount(1, 2);
        assertEquals(5, count);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int size = counter.getSize();
        assertEquals(6, size);
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int[] sizes = counter.getSizes();
        int[] expectedSizes = {2, 3};
        assertArrayEquals(expectedSizes, sizes);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(0, (int) iterator.next());
        assertEquals(1, iterator.getCount());
        int[] counts = iterator.getCounts();
        int[] expectedCounts = {0, 0};
        assertArrayEquals(expectedCounts, counts);
        assertEquals(0, iterator.getCount(0));

        assertTrue(iterator.hasNext());
        assertEquals(1, (int) iterator.next());
        assertEquals(2, iterator.getCount());
        counts = iterator.getCounts();
        expectedCounts = new int[]{0, 1};
        assertArrayEquals(expectedCounts, counts);
        assertEquals(1, iterator.getCount(1));

        assertTrue(iterator.hasNext());
        assertEquals(2, (int) iterator.next());
        assertEquals(3, iterator.getCount());
        counts = iterator.getCounts();
        expectedCounts = new int[]{1, 0};
        assertArrayEquals(expectedCounts, counts);
        assertEquals(0, iterator.getCount(0));

        assertTrue(iterator.hasNext());
        assertEquals(3, (int) iterator.next());
        assertEquals(4, iterator.getCount());

        assertTrue(iterator.hasNext());
        assertEquals(4, (int) iterator.next());
        assertEquals(5, iterator.getCount());

        assertTrue(iterator.hasNext());
        assertEquals(5, (int) iterator.next());
        assertEquals(6, iterator.getCount());

        assertFalse(iterator.hasNext());
    }

}
