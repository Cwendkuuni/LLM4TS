import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.util.MultidimensionalCounter;

public class MultidimensionalCounterTest {

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            assertEquals(count, iterator.next().intValue());
            count++;
        }
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] counts = counter.getCounts(10);
        assertArrayEquals(new int[]{1, 0, 2}, counts);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_OutOfRangeException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCounts(25);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int index = counter.getCount(1, 2, 3);
        assertEquals(23, index);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_DimensionMismatchException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        counter.getCount(1, 2); // mismatched dimensions
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_OutOfRangeException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        counter.getCount(2, 3, 10); // out of range
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(24, counter.getSize());
    }

}