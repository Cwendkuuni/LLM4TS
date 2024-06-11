import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Before
    public void setUp() throws NotStrictlyPositiveException {
        counter = new MultidimensionalCounter(2, 3, 4);
    }

    @Test
    public void testGetDimension() {
        // Typical use case
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSizes() {
        // Typical use case
        assertArrayEquals(new int[] {2, 3, 4}, counter.getSizes());
    }

    @Test
    public void testGetSize() {
        // Typical use case
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        // Typical use case
        assertArrayEquals(new int[] {0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[] {0, 0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[] {1, 0, 1}, counter.getCounts(5));
        assertArrayEquals(new int[] {1, 2, 3}, counter.getCounts(23));

        // Edge case
        assertArrayEquals(new int[] {0, 0, 3}, counter.getCounts(3));

        // Error scenario
        try {
            counter.getCounts(-1);
        } catch (OutOfRangeException e) {
            // Expected exception
        }

        try {
            counter.getCounts(24);
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        // Typical use case
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(5, counter.getCount(1, 0, 1));
        assertEquals(23, counter.getCount(1, 2, 3));

        // Edge case
        assertEquals(3, counter.getCount(0, 0, 3));

        // Error scenario
        try {
            counter.getCount(0, 0);
        } catch (DimensionMismatchException e) {
            // Expected exception
        }

        try {
            counter.getCount(0, -1, 1);
        } catch (OutOfRangeException e) {
            // Expected exception
        }

        try {
            counter.getCount(2, 0, 0);
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }

    @Test
    public void testToString() {
        // Typical use case
        assertEquals("[0][0][0]", counter.toString());

        // Edge case
        counter.next(); // Move to the next count
        assertEquals("[0][0][1]", counter.toString());
    }

    @Test
    public void testIterator() {
        // Typical use case
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(0, iterator.getCount());
        assertArrayEquals(new int[] {0, 0, 0}, iterator.getCounts());
        assertEquals(0, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        assertEquals(0, iterator.getCount(2));

        // Move to the next count
        iterator.next();
        assertEquals(1, iterator.getCount());
        assertArrayEquals(new int[] {0, 0, 1}, iterator.getCounts());
        assertEquals(0, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        assertEquals(1, iterator.getCount(2));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove(); // Should throw UnsupportedOperationException
    }
}