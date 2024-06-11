import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Test
    public void testGetDimension() {
        counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSize() {
        counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetCounts() {
        counter = new MultidimensionalCounter(2, 3, 4);
        int[] counts = counter.getCounts(9);
        assertArrayEquals(new int[]{1, 0, 1}, counts);
    }

    @Test
    public void testGetCount() {
        counter = new MultidimensionalCounter(2, 3, 4);
        int count = counter.getCount(1, 2, 2);
        assertEquals(14, count);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatchException() {
        counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(1, 2); // Incorrect number of dimensions
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeException() {
        counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(1, 2, 5); // Value out of range
    }

    @Test
    public void testGetSizes() {
        counter = new MultidimensionalCounter(2, 3, 4);
        int[] sizes = counter.getSizes();
        assertArrayEquals(new int[]{2, 3, 4}, sizes);
    }

    @Test
    public void testIterator() {
        counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            assertEquals(count, (int) iterator.next());
            int[] counts = iterator.getCounts();
            int calculatedCount = counter.getCount(counts);
            assertEquals(count, calculatedCount);
            count++;
        }
        assertEquals(24, count);
    }

    @Test
    public void testIteratorGetCount() {
        counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(-1, iterator.getCount());
        iterator.next();
        assertEquals(0, iterator.getCount());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }

    @Test
    public void testIteratorNext() {
        counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(0, (int) iterator.next());
        assertEquals(1, (int) iterator.next());
    }

    @Test
    public void testToString() {
        counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals("[0][0][0]", counter.toString());
    }
}