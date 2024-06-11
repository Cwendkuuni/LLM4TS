import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Before
    public void setUp() {
        counter = new MultidimensionalCounter(2, 4, 3);
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
    public void testGetCounts() {
        int[] expectedCounts = {0, 0, 0};
        assertArrayEquals(expectedCounts, counter.getCounts(0));

        expectedCounts = new int[]{0, 0, 1};
        assertArrayEquals(expectedCounts, counter.getCounts(1));

        expectedCounts = new int[]{0, 0, 2};
        assertArrayEquals(expectedCounts, counter.getCounts(2));

        expectedCounts = new int[]{0, 1, 0};
        assertArrayEquals(expectedCounts, counter.getCounts(3));

        expectedCounts = new int[]{1, 0, 0};
        assertArrayEquals(expectedCounts, counter.getCounts(12));

        expectedCounts = new int[]{1, 3, 2};
        assertArrayEquals(expectedCounts, counter.getCounts(23));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfBounds() {
        counter.getCounts(100);
    }

    @Test
    public void testGetCount() {
        assertEquals(0, counter.getCount(0, 0, 0));

        assertEquals(1, counter.getCount(0, 0, 1));

        assertEquals(2, counter.getCount(0, 0, 2));

        assertEquals(3, counter.getCount(0, 1, 0));

        assertEquals(12, counter.getCount(1, 0, 0));

        assertEquals(23, counter.getCount(1, 3, 2));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfBounds() {
        counter.getCount(0, 0, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidSize() {
        MultidimensionalCounter invalidCounter = new MultidimensionalCounter(0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountMismatchedDimensions() {
        counter.getCount(0, 0);
    }
}