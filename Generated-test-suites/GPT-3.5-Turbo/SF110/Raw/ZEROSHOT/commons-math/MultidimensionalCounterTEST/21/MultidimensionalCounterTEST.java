import org.apache.commons.math3.exception.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class MultidimensionalCounterTest {

    @Test
    public void testGetDimension() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCounts(100);
    }

    @Test
    public void testGetCounts() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] expected = {1, 2};
        assertArrayEquals(expected, counter.getCounts(5));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(1, 2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(2, 2, 5);
    }

    @Test
    public void testGetCount() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(21, counter.getCount(1, 2, 3));
    }

    @Test
    public void testGetSize() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] expected = {2, 3, 4};
        assertArrayEquals(expected, counter.getSizes());
    }

    @Test
    public void testToString() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals("[0][1]", counter.toString());
    }

    @Test
    public void testIteratorHasNext() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iter = counter.iterator();
        assertTrue(iter.hasNext());
        for (int i = 0; i < 5; i++) {
            iter.next();
        }
        assertFalse(iter.hasNext());
    }

    @Test
    public void testIteratorNext() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iter = counter.iterator();
        assertEquals(0, (int) iter.next());
        assertEquals(1, iter.getCount());
        int[] expected1 = {1, 0};
        assertArrayEquals(expected1, iter.getCounts());
        assertEquals(1, iter.getCount(0));
        assertEquals(0, iter.getCount(1));
        assertEquals(0, iter.getCount(2));
        assertEquals(1, (int) iter.next());
        assertEquals(2, iter.getCount());
        int[] expected2 = {1, 1};
        assertArrayEquals(expected2, iter.getCounts());
        assertEquals(1, iter.getCount(0));
        assertEquals(1, iter.getCount(1));
        assertEquals(0, iter.getCount(2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iter = counter.iterator();
        iter.remove();
    }

}