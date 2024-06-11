import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.*;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(size, counter.getSizes());
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetCounts() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertArrayEquals(new int[]{1, 1}, counter.getCounts(3));
    }

    @Test
    public void testGetCount() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(4, counter.getCount(1, 1));
    }

    @Test
    public void testIteratorNext() {
        int[] size = {2, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(0, (int) iterator.next());
        assertEquals(1, (int) iterator.next());
        assertEquals(2, (int) iterator.next());
        assertEquals(3, (int) iterator.next());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] size = {2, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCounts(8);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        int[] size = {2, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(1, 3);
    }
}