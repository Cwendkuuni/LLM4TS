import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.*;
import java.util.*;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Before
    public void setUp() throws NotStrictlyPositiveException {
        int[] size = {3, 4, 2};
        counter = new MultidimensionalCounter(size);
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        int[] indices = counter.getCounts(4);
        int[] expectedIndices = {0, 0, 1};
        assertArrayEquals(expectedIndices, indices);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() throws OutOfRangeException {
        counter.getCounts(20);
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        int count = counter.getCount(1, 2, 1);
        assertEquals(13, count);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() throws OutOfRangeException, DimensionMismatchException {
        counter.getCount(1, 2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() throws OutOfRangeException, DimensionMismatchException {
        counter.getCount(3, 4, 2);
    }

    @Test
    public void testGetSize() {
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        int[] sizes = counter.getSizes();
        int[] expectedSizes = {3, 4, 2};
        assertArrayEquals(expectedSizes, sizes);
    }

    @Test
    public void testToString() {
        String expectedString = "[0][0][0]";
        assertEquals(expectedString, counter.toString());
    }

    @Test
    public void testIterator() {
        Iterator<Integer> iterator = counter.iterator();
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
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        Iterator<Integer> iterator = counter.iterator();
        iterator.remove();
    }

}