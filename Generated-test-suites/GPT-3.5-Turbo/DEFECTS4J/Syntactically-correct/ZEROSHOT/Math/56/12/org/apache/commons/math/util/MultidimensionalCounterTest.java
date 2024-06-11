import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(0), iterator.next());
        assertEquals(0, iterator.getCount());
        assertArrayEquals(new int[] {0, 0, 0}, iterator.getCounts());

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(1), iterator.next());
        assertEquals(1, iterator.getCount());
        assertArrayEquals(new int[] {0, 0, 1}, iterator.getCounts());

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(2), iterator.next());
        assertEquals(2, iterator.getCount());
        assertArrayEquals(new int[] {0, 1, 0}, iterator.getCounts());

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(3), iterator.next());
        assertEquals(3, iterator.getCount());
        assertArrayEquals(new int[] {0, 1, 1}, iterator.getCounts());

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(4), iterator.next());
        assertEquals(4, iterator.getCount());
        assertArrayEquals(new int[] {1, 0, 0}, iterator.getCounts());

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(5), iterator.next());
        assertEquals(5, iterator.getCount());
        assertArrayEquals(new int[] {1, 0, 1}, iterator.getCounts());

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(6), iterator.next());
        assertEquals(6, iterator.getCount());
        assertArrayEquals(new int[] {1, 1, 0}, iterator.getCounts());

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(7), iterator.next());
        assertEquals(7, iterator.getCount());
        assertArrayEquals(new int[] {1, 1, 1}, iterator.getCounts());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);

        assertArrayEquals(new int[] {0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[] {0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[] {0, 2}, counter.getCounts(2));
        assertArrayEquals(new int[] {1, 0}, counter.getCounts(3));
        assertArrayEquals(new int[] {1, 1}, counter.getCounts(4));
        assertArrayEquals(new int[] {1, 2}, counter.getCounts(5));
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);

        assertEquals(0, counter.getCount(0, 0));
        assertEquals(1, counter.getCount(0, 1));
        assertEquals(2, counter.getCount(0, 2));
        assertEquals(3, counter.getCount(1, 0));
        assertEquals(4, counter.getCount(1, 1));
        assertEquals(5, counter.getCount(1, 2));
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);

        assertEquals(6, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);

        assertArrayEquals(new int[] {2, 3}, counter.getSizes());
    }
}