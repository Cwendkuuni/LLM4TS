import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Test
    public void testGetDimension() {
        int[] size = {3, 4, 2};
        counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetCounts() {
        int[] size = {3, 4, 2};
        counter = new MultidimensionalCounter(size);

        try {
            int[] counts = counter.getCounts(0);
            assertArrayEquals(new int[]{0, 0, 0}, counts);

            counts = counter.getCounts(1);
            assertArrayEquals(new int[]{0, 0, 1}, counts);

            counts = counter.getCounts(10);
            assertArrayEquals(new int[]{1, 0, 2}, counts);
        } catch (OutOfRangeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_OutOfRange() throws OutOfRangeException {
        int[] size = {3, 4, 2};
        counter = new MultidimensionalCounter(size);

        counter.getCounts(27); // Out of range index
    }

    @Test
    public void testGetCount() {
        int[] size = {3, 4, 2};
        counter = new MultidimensionalCounter(size);

        try {
            int count = counter.getCount(0, 0, 0);
            assertEquals(0, count);

            count = counter.getCount(1, 0, 1);
            assertEquals(9, count);

            count = counter.getCount(2, 3, 1);
            assertEquals(25, count);
        } catch (OutOfRangeException | DimensionMismatchException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_DimensionMismatch() throws DimensionMismatchException, OutOfRangeException {
        int[] size = {3, 4, 2};
        counter = new MultidimensionalCounter(size);

        counter.getCount(0, 0); // Mismatched dimensions
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_OutOfRange() throws DimensionMismatchException, OutOfRangeException {
        int[] size = {3, 4, 2};
        counter = new MultidimensionalCounter(size);

        counter.getCount(1, 5, 0); // Out of range index
    }

    @Test
    public void testGetSize() {
        int[] size = {3, 4, 2};
        counter = new MultidimensionalCounter(size);
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        int[] size = {3, 4, 2};
        counter = new MultidimensionalCounter(size);
        assertArrayEquals(size, counter.getSizes());
    }

    @Test
    public void testToString() {
        int[] size = {3, 4, 2};
        counter = new MultidimensionalCounter(size);
        assertEquals("[0][0][0]", counter.toString());
    }

    @Test
    public void testIterator() {
        int[] size = {3, 4, 2};
        counter = new MultidimensionalCounter(size);

        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, (int) iterator.next());
        assertEquals(1, iterator.getCount());

        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        assertTrue(iterator.hasNext());
        assertEquals(5, iterator.getCount());
        
        // Testing the counts() method of the iterator
        int[] counts = iterator.getCounts();
        assertArrayEquals(new int[]{2, 3, 0}, counts);

        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        assertFalse(iterator.hasNext());
        assertEquals(9, iterator.getCount());
    }
}