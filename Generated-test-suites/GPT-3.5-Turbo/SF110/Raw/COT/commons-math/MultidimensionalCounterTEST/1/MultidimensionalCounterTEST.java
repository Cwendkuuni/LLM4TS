import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Before
    public void setUp() throws NotStrictlyPositiveException {
        counter = new MultidimensionalCounter(2, 3);
    }

    @Test
    public void testGetDimension() {
        assertEquals(2, counter.getDimension());
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        int[] counts = counter.getCounts(3);
        assertArrayEquals(new int[] {1, 0}, counts);
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        int count = counter.getCount(1, 2);
        assertEquals(5, count);
    }

    @Test
    public void testGetSize() {
        assertEquals(6, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        int[] sizes = counter.getSizes();
        assertArrayEquals(new int[] {2, 3}, sizes);
    }

    @Test
    public void testToString() {
        assertEquals("[0][3]", counter.toString());
    }
}