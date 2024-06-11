import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidSize() {
        new MultidimensionalCounter(0, 1, 2);
    }

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

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWithInvalidDimensions() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(1, 2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithInvalidIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(2, 3, 5);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(2, counter.getCount(0, 0, 2));
        assertEquals(3, counter.getCount(0, 1, 0));
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{0, 0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[]{0, 0, 2}, counter.getCounts(2));
        assertArrayEquals(new int[]{0, 1, 0}, counter.getCounts(3));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsInvalidIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCounts(24);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(1, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(2, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(3, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(4, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(5, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(6, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(7, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(8, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(9, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(10, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(11, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(12, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(13, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(14, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(15, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(16, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(17, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(18, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(19, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(20, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(21, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(22, (int) iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(23, (int) iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }
}