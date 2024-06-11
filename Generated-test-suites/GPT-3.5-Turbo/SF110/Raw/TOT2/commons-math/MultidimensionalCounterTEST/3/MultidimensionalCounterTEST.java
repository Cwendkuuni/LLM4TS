import org.junit.Test;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;

import static org.junit.Assert.*;
import java.util.Iterator;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(2, counter.getDimension());
        assertArrayEquals(size, counter.getSizes());
        assertEquals(6, counter.getSize());
    }

    @Test
    public void testGetCounts() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] counts = counter.getCounts(0);
        assertArrayEquals(new int[]{0, 0}, counts);

        counts = counter.getCounts(5);
        assertArrayEquals(new int[]{1, 2}, counts);
    }

    @Test
    public void testGetCount() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int count = counter.getCount(0, 0);
        assertEquals(0, count);

        count = counter.getCount(1, 2);
        assertEquals(5, count);
    }

    @Test
    public void testIterator() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator<Integer> iterator = counter.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(new Integer(0), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new Integer(1), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new Integer(2), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new Integer(3), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new Integer(4), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new Integer(5), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstructor() {
        int[] size = {0, 3};
        new MultidimensionalCounter(size);
    }

    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeCounts() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCounts(6);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchCount() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(0);
    }
}