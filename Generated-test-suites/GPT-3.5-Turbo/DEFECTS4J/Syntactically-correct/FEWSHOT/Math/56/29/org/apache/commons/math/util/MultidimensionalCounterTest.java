import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {
    @Test
    public void testConstructorAndGetters() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(new int[] {2, 4, 3}, counter.getSizes());
        assertEquals(24, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroSize() {
        new MultidimensionalCounter(2, 0, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeSize() {
        new MultidimensionalCounter(2, -4, 3);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, (int) iterator.next());
        assertEquals(1, (int) iterator.next());
        assertEquals(2, (int) iterator.next());
        assertEquals(3, (int) iterator.next());
        assertEquals(4, (int) iterator.next());
        assertEquals(5, (int) iterator.next());
        assertEquals(6, (int) iterator.next());
        assertEquals(7, (int) iterator.next());
        assertEquals(8, (int) iterator.next());
        assertEquals(9, (int) iterator.next());
        assertEquals(10, (int) iterator.next());
        assertEquals(11, (int) iterator.next());
        assertEquals(12, (int) iterator.next());
        assertEquals(13, (int) iterator.next());
        assertEquals(14, (int) iterator.next());
        assertEquals(15, (int) iterator.next());
        assertEquals(16, (int) iterator.next());
        assertEquals(17, (int) iterator.next());
        assertEquals(18, (int) iterator.next());
        assertEquals(19, (int) iterator.next());
        assertEquals(20, (int) iterator.next());
        assertEquals(21, (int) iterator.next());
        assertEquals(22, (int) iterator.next());
        assertEquals(23, (int) iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertArrayEquals(new int[] {0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[] {0, 0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[] {1, 0, 0}, counter.getCounts(12));
        assertArrayEquals(new int[] {1, 3, 2}, counter.getCounts(23));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCounts(24);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(12, counter.getCount(1, 0, 0));
        assertEquals(23, counter.getCount(1, 3, 2));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(0, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(0, 0, 4);
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals("[0][0][0]", counter.toString());
        assertEquals("[1][3][2]", counter.getCounts(23).toString());
    }
}