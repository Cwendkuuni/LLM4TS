import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.*;

public class MultidimensionalCounterTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NotStrictlyPositive() {
        MultidimensionalCounter counter = new MultidimensionalCounter(-5, 10, 20);
    }

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(5, 10, 20);
        assertEquals(3, counter.getDimension());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_OutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(5, 10, 20);
        counter.getCounts(1000);
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(5, 10, 20);
        int[] expectedCounts = {4, 9, 19};
        assertArrayEquals(expectedCounts, counter.getCounts(999));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_DimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(5, 10, 20);
        counter.getCount(1, 2, 3, 4);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_OutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(5, 10, 20);
        counter.getCount(5, 11, 21);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(5, 10, 20);
        assertEquals(999, counter.getCount(4, 9, 19));
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(5, 10, 20);
        assertEquals(1000, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(5, 10, 20);
        int[] expectedSizes = {5, 10, 20};
        assertArrayEquals(expectedSizes, counter.getSizes());
    }

    @Test
    public void testIterator_HasNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testIterator_Next() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(0, (int) iterator.next());
        assertEquals(1, (int) iterator.next());
        assertEquals(2, (int) iterator.next());
        assertEquals(3, (int) iterator.next());
        assertEquals(4, (int) iterator.next());
        assertEquals(5, (int) iterator.next());
        assertEquals(6, (int) iterator.next());
        assertEquals(7, (int) iterator.next());
    }

    @Test
    public void testIterator_GetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(-1, iterator.getCount());
        iterator.next();
        assertEquals(0, iterator.getCount());
    }

    @Test
    public void testIterator_GetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int[] expectedCounts = {0, 0, 0};
        assertArrayEquals(expectedCounts, iterator.getCounts());
        iterator.next();
        int[] expectedCounts2 = {1, 0, 0};
        assertArrayEquals(expectedCounts2, iterator.getCounts());
    }

    @Test
    public void testIterator_GetCount_ByDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(0, iterator.getCount(0));
        iterator.next();
        assertEquals(1, iterator.getCount(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_Remove() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }
}