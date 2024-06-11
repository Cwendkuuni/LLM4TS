import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Before
    public void setUp() throws Exception {
        counter = new MultidimensionalCounter(2, 3, 4);
    }

    @Test
    public void testConstructor() {
        assertEquals(3, counter.getDimension());
        assertEquals(2, counter.getSize());
        assertArrayEquals(new int[]{2, 3, 4}, counter.getSizes());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_InvalidSize() throws NotStrictlyPositiveException {
        counter = new MultidimensionalCounter(-1, 3, 4);
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{1, 0, 0}, counter.getCounts(1));
        assertArrayEquals(new int[]{0, 1, 0}, counter.getCounts(2));
        assertArrayEquals(new int[]{1, 1, 0}, counter.getCounts(3));
        assertArrayEquals(new int[]{0, 2, 0}, counter.getCounts(4));
        assertArrayEquals(new int[]{1, 2, 0}, counter.getCounts(5));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_InvalidIndex() throws OutOfRangeException {
        counter.getCounts(6);
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(1, 0, 0));
        assertEquals(2, counter.getCount(0, 1, 0));
        assertEquals(3, counter.getCount(1, 1, 0));
        assertEquals(4, counter.getCount(0, 2, 0));
        assertEquals(5, counter.getCount(1, 2, 0));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_InvalidDimension() throws DimensionMismatchException, OutOfRangeException {
        counter.getCount(0, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_InvalidIndex() throws DimensionMismatchException, OutOfRangeException {
        counter.getCount(2, 0, 0);
    }

    @Test
    public void testToString() {
        assertEquals("[0][0][0]", counter.toString());
    }

    @Test
    public void testIterator() {
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
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_Remove() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }

}