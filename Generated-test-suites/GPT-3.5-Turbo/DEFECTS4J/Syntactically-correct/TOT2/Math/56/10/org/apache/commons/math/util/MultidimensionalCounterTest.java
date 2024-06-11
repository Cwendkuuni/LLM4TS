import static org.junit.Assert.*;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(0), iterator.next());
        assertEquals(0, iterator.getCount());
        assertArrayEquals(new int[]{0, 0, 0}, iterator.getCounts());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);

        assertArrayEquals(new int[]{1, 1, 1}, counter.getCounts(7));
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);

        assertEquals(7, counter.getCount(1, 1, 1));
    }

    @Test(expected = OutOfRangeException.class)
    public void testInvalidGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);

        counter.getCounts(8);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);

        counter.getCount(1, 1);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);

        assertEquals(8, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);

        assertArrayEquals(new int[]{2, 2, 2}, counter.getSizes());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidCounterCreation() {
        new MultidimensionalCounter(0, 2, 2);
    }
}