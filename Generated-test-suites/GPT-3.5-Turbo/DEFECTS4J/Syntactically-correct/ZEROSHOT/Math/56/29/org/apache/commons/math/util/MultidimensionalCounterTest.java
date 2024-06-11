import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next().intValue());

        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next().intValue());

        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next().intValue());

        assertTrue(iterator.hasNext());
        assertEquals(3, iterator.next().intValue());

        assertTrue(iterator.hasNext());
        assertEquals(4, iterator.next().intValue());

        assertTrue(iterator.hasNext());
        assertEquals(5, iterator.next().intValue());

        assertTrue(iterator.hasNext());
        assertEquals(6, iterator.next().intValue());

        assertTrue(iterator.hasNext());
        assertEquals(7, iterator.next().intValue());

        assertTrue(iterator.hasNext());
        assertEquals(8, iterator.next().intValue());

        assertTrue(iterator.hasNext());
        assertEquals(9, iterator.next().intValue());

        assertTrue(iterator.hasNext());
        assertEquals(10, iterator.next().intValue());

        assertTrue(iterator.hasNext());
        assertEquals(11, iterator.next().intValue());

        assertTrue(iterator.hasNext());
        assertEquals(12, iterator.next().intValue());

        assertTrue(iterator.hasNext());
        assertEquals(13, iterator.next().intValue());

        assertTrue(iterator.hasNext());
        assertEquals(14, iterator.next().intValue());

        assertTrue(iterator.hasNext());
        assertEquals(15, iterator.next().intValue());

        assertTrue(iterator.hasNext());
        assertEquals(16, iterator.next().intValue());

        assertTrue(iterator.hasNext());
        assertEquals(17, iterator.next().intValue());

        assertTrue(iterator.hasNext());
        assertEquals(18, iterator.next().intValue());

        assertTrue(iterator.hasNext());
        assertEquals(19, iterator.next().intValue());

        assertTrue(iterator.hasNext());
        assertEquals(20, iterator.next().intValue());

        assertTrue(iterator.hasNext());
        assertEquals(21, iterator.next().intValue());

        assertTrue(iterator.hasNext());
        assertEquals(22, iterator.next().intValue());

        assertTrue(iterator.hasNext());
        assertEquals(23, iterator.next().intValue());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);

        assertArrayEquals(new int[] {0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[] {0, 0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[] {0, 0, 2}, counter.getCounts(2));
        assertArrayEquals(new int[] {0, 1, 0}, counter.getCounts(3));
        assertArrayEquals(new int[] {0, 1, 1}, counter.getCounts(4));
        assertArrayEquals(new int[] {0, 1, 2}, counter.getCounts(5));
        assertArrayEquals(new int[] {0, 2, 0}, counter.getCounts(6));
        assertArrayEquals(new int[] {0, 2, 1}, counter.getCounts(7));
        assertArrayEquals(new int[] {0, 2, 2}, counter.getCounts(8));
        assertArrayEquals(new int[] {0, 3, 0}, counter.getCounts(9));
        assertArrayEquals(new int[] {0, 3, 1}, counter.getCounts(10));
        assertArrayEquals(new int[] {0, 3, 2}, counter.getCounts(11));
        assertArrayEquals(new int[] {1, 0, 0}, counter.getCounts(12));
        assertArrayEquals(new int[] {1, 0, 1}, counter.getCounts(13));
        assertArrayEquals(new int[] {1, 0, 2}, counter.getCounts(14));
        assertArrayEquals(new int[] {1, 1, 0}, counter.getCounts(15));
        assertArrayEquals(new int[] {1, 1, 1}, counter.getCounts(16));
        assertArrayEquals(new int[] {1, 1, 2}, counter.getCounts(17));
        assertArrayEquals(new int[] {1, 2, 0}, counter.getCounts(18));
        assertArrayEquals(new int[] {1, 2, 1}, counter.getCounts(19));
        assertArrayEquals(new int[] {1, 2, 2}, counter.getCounts(20));
        assertArrayEquals(new int[] {1, 3, 0}, counter.getCounts(21));
        assertArrayEquals(new int[] {1, 3, 1}, counter.getCounts(22));
        assertArrayEquals(new int[] {1, 3, 2}, counter.getCounts(23));
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);

        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(2, counter.getCount(0, 0, 2));
        assertEquals(3, counter.getCount(0, 1, 0));
        assertEquals(4, counter.getCount(0, 1, 1));
        assertEquals(5, counter.getCount(0, 1, 2));
        assertEquals(6, counter.getCount(0, 2, 0));
        assertEquals(7, counter.getCount(0, 2, 1));
        assertEquals(8, counter.getCount(0, 2, 2));
        assertEquals(9, counter.getCount(0, 3, 0));
        assertEquals(10, counter.getCount(0, 3, 1));
        assertEquals(11, counter.getCount(0, 3, 2));
        assertEquals(12, counter.getCount(1, 0, 0));
        assertEquals(13, counter.getCount(1, 0, 1));
        assertEquals(14, counter.getCount(1, 0, 2));
        assertEquals(15, counter.getCount(1, 1, 0));
        assertEquals(16, counter.getCount(1, 1, 1));
        assertEquals(17, counter.getCount(1, 1, 2));
        assertEquals(18, counter.getCount(1, 2, 0));
        assertEquals(19, counter.getCount(1, 2, 1));
        assertEquals(20, counter.getCount(1, 2, 2));
        assertEquals(21, counter.getCount(1, 3, 0));
        assertEquals(22, counter.getCount(1, 3, 1));
        assertEquals(23, counter.getCount(1, 3, 2));
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertArrayEquals(new int[] {2, 4, 3}, counter.getSizes());
    }
}