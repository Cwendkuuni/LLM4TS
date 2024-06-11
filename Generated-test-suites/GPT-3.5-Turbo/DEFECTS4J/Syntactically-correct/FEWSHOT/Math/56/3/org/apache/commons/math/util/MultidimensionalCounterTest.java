import org.junit.Test;

import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 2);
        assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{0, 0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[]{1, 0, 0}, counter.getCounts(12));
        assertArrayEquals(new int[]{1, 3, 2}, counter.getCounts(23));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 2);
        counter.getCounts(24);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 2);
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(12, counter.getCount(1, 0, 0));
        assertEquals(23, counter.getCount(1, 3, 2));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 2);
        counter.getCount(1, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 2);
        counter.getCount(1, 3, 3);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 2);
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 2);
        assertArrayEquals(new int[]{3, 4, 2}, counter.getSizes());
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(0, (int) iterator.next());
        assertArrayEquals(new int[]{0, 0}, iterator.getCounts());

        assertTrue(iterator.hasNext());
        assertEquals(1, (int) iterator.next());
        assertArrayEquals(new int[]{0, 1}, iterator.getCounts());

        assertTrue(iterator.hasNext());
        assertEquals(2, (int) iterator.next());
        assertArrayEquals(new int[]{0, 2}, iterator.getCounts());

        assertTrue(iterator.hasNext());
        assertEquals(3, (int) iterator.next());
        assertArrayEquals(new int[]{1, 0}, iterator.getCounts());

        assertTrue(iterator.hasNext());
        assertEquals(4, (int) iterator.next());
        assertArrayEquals(new int[]{1, 1}, iterator.getCounts());

        assertTrue(iterator.hasNext());
        assertEquals(5, (int) iterator.next());
        assertArrayEquals(new int[]{1, 2}, iterator.getCounts());

        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 2);
        assertEquals("[0][0]", counter.toString());

        counter.iterator().next();
        assertEquals("[0][1]", counter.toString());

        counter.iterator().next();
        assertEquals("[1][0]", counter.toString());

        counter.iterator().next();
        assertEquals("[1][1]", counter.toString());

        counter.iterator().next();
        assertEquals("[2][0]", counter.toString());

        counter.iterator().next();
        assertEquals("[2][1]", counter.toString());
    }
}