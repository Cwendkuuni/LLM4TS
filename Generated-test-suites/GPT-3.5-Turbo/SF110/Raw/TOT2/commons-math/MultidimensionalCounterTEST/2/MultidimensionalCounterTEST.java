import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        // Test typical case
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
            assertNotNull(counter);
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException should not be thrown for valid input");
        }

        // Test invalid input
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter();
            fail("NotStrictlyPositiveException should be thrown for empty input");
        } catch (NotStrictlyPositiveException e) {
            // Pass
        }
    }

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertEquals(2, counter.getDimension());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int[] counts = counter.getCounts(4);
        assertArrayEquals(new int[]{2, 1}, counts);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int count = counter.getCount(1, 2);
        assertEquals(7, count);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertEquals(6, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertArrayEquals(new int[]{2, 3}, counter.getSizes());
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        String expected = "[0][0]";
        assertEquals(expected, counter.toString());
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(1, (int) iterator.next());
        assertEquals(2, iterator.getCount());
        assertArrayEquals(new int[]{1, 2}, iterator.getCounts());
        assertEquals(2, iterator.getCount(0));
    }
}