import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Before
    public void setUp() throws NotStrictlyPositiveException {
        int[] sizes = {3, 4, 2};
        counter = new MultidimensionalCounter(sizes);
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
    public void testGetCounts() throws OutOfRangeException {
        int[] indices = counter.getCounts(10);
        assertEquals(0, indices[0]);
        assertEquals(2, indices[1]);
        assertEquals(2, indices[2]);

        indices = counter.getCounts(23);
        assertEquals(2, indices[0]);
        assertEquals(3, indices[1]);
        assertEquals(1, indices[2]);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() throws OutOfRangeException {
        counter.getCounts(24);
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        int[] indices = {0, 2, 1};
        assertEquals(6, counter.getCount(indices));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() throws OutOfRangeException, DimensionMismatchException {
        int[] indices = {0, 2};
        counter.getCount(indices);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() throws OutOfRangeException, DimensionMismatchException {
        int[] indices = {0, 2, 3};
        counter.getCount(indices);
    }

    @Test
    public void testGetSizes() {
        int[] sizes = counter.getSizes();
        assertEquals(3, sizes.length);
        assertEquals(3, sizes[0]);
        assertEquals(4, sizes[1]);
        assertEquals(2, sizes[2]);
    }

    @Test
    public void testToString() {
        assertEquals("[0][0][0]", counter.toString());
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(0), iterator.next());
        assertEquals(0, iterator.getCount());

        int[] counts = iterator.getCounts();
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(0, counts[2]);

        counts = iterator.getCounts();
        assertEquals(1, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(0, counts[2]);
    }

}