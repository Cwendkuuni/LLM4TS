import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    private static MultidimensionalCounter counter;

    @BeforeClass
    public static void setUp() throws NotStrictlyPositiveException {
        int[] size = {2, 3, 4};
        counter = new MultidimensionalCounter(size);
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        assertArrayEquals(new int[] {0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[] {1, 1, 1}, counter.getCounts(17));
        assertArrayEquals(new int[] {0, 2, 2}, counter.getCounts(11));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() throws OutOfRangeException {
        counter.getCounts(20);
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(17, counter.getCount(1, 1, 1));
        assertEquals(11, counter.getCount(0, 2, 2));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() throws OutOfRangeException, DimensionMismatchException {
        counter.getCount(0, 1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() throws OutOfRangeException, DimensionMismatchException {
        counter.getCount(0, 0, 3);
    }

    @Test
    public void testGetSize() {
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        assertArrayEquals(new int[] {2, 3, 4}, counter.getSizes());
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(0), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(1), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(2), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(3), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(4), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(5), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(6), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(7), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(8), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(9), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(10), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(11), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(12), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(13), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(14), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(15), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(16), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(17), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(18), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(19), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(20), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(21), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(22), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(23), iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(23, iterator.getCount());
        assertArrayEquals(new int[] {1, 2, 3}, iterator.getCounts());
        assertEquals(1, iterator.getCount(0));
        assertEquals(2, iterator.getCount(1));
        assertEquals(3, iterator.getCount(2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }

    @Test
    public void testToString() {
        assertEquals("[0][0][0]", counter.toString());
    }
}