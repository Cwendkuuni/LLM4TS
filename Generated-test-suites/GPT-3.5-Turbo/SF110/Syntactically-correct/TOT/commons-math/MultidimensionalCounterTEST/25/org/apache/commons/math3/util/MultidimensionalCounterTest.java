import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.DimensionMismatchException;
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
    public void testGetDimension() {
        assertEquals(3, counter.getDimension());
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
    public void testGetCounts() throws OutOfRangeException {
        assertArrayEquals(new int[] {0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[] {0, 0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[] {0, 2, 3}, counter.getCounts(11));
        try {
            counter.getCounts(24);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(11, counter.getCount(0, 2, 3));
        try {
            counter.getCount(2, 0, 0);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
        try {
            counter.getCount(0, 0, 4);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
        try {
            counter.getCount(0, 0);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, (int) iterator.next());
        assertEquals(1, iterator.getCount());
        assertArrayEquals(new int[] {0, 0, 0}, iterator.getCounts());
        assertTrue(iterator.hasNext());
        assertEquals(1, (int) iterator.next());
        assertEquals(2, iterator.getCount());
        assertArrayEquals(new int[] {0, 0, 1}, iterator.getCounts());
        // Iterate through all elements
        for (int i = 2; i < 24; i++) {
            iterator.next();
        }
        assertFalse(iterator.hasNext());
        try {
            iterator.next();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidSize() throws NotStrictlyPositiveException {
        new MultidimensionalCounter(-2, 3, 4);
    }
}