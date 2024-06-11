import org.junit.Test;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.util.MultidimensionalCounter;

import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

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
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{0, 0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[]{0, 0, 2}, counter.getCounts(2));
        assertArrayEquals(new int[]{0, 1, 0}, counter.getCounts(3));
        assertArrayEquals(new int[]{1, 0, 0}, counter.getCounts(12));
        assertArrayEquals(new int[]{1, 3, 2}, counter.getCounts(23));

        try {
            counter.getCounts(-1);
            fail("Exception not thrown");
        } catch (OutOfRangeException e) {
            // Expected exception
        }

        try {
            counter.getCounts(24);
            fail("Exception not thrown");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(2, counter.getCount(0, 0, 2));
        assertEquals(3, counter.getCount(0, 1, 0));
        assertEquals(12, counter.getCount(1, 0, 0));
        assertEquals(23, counter.getCount(1, 3, 2));

        try {
            counter.getCount(0, 0, -1);
            fail("Exception not thrown");
        } catch (OutOfRangeException e) {
            // Expected exception
        }

        try {
            counter.getCount(0, 0, 3);
            fail("Exception not thrown");
        } catch (OutOfRangeException e) {
            // Expected exception
        }

        try {
            counter.getCount(0, -1, 2);
            fail("Exception not thrown");
        } catch (OutOfRangeException e) {
            // Expected exception
        }

        try {
            counter.getCount(2, 3, 2);
            fail("Exception not thrown");
        } catch (OutOfRangeException e) {
            // Expected exception
        }

        try {
            counter.getCount(0, 0, 0, 0);
            fail("Exception not thrown");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter1 = new MultidimensionalCounter(2, 3);
        assertEquals(6, counter1.getSize());

        MultidimensionalCounter counter2 = new MultidimensionalCounter(2, 3, 4);
        assertEquals(24, counter2.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertArrayEquals(new int[]{2, 3, 4}, counter.getSizes());
    }
}