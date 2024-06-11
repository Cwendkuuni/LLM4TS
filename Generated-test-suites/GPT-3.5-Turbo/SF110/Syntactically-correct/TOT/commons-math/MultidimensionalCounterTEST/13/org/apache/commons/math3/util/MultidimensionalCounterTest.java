import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.MultidimensionalCounter;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter mCounter;

    @Before
    public void setup() {
        mCounter = new MultidimensionalCounter(2, 3, 4);
    }

    @Test
    public void testGetDimension() {
        int expected = 3;
        int actual = mCounter.getDimension();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCounts() {
        int[] expected = {1, 2, 3};
        int[] actual = mCounter.getCounts(23);
        assertArrayEquals(expected, actual);

        try {
            mCounter.getCounts(60);
            fail("Expected OutOfRangeException was not thrown.");
        } catch (OutOfRangeException e) {
            assertEquals(60, e.getArgument());
            assertEquals(0, e.getLowerBound());
            assertEquals(23, e.getUpperBound());
        }
    }

    @Test
    public void testGetCount() {
        int[] input = {1, 2, 3};
        int expected = 23;
        int actual = mCounter.getCount(input);
        assertEquals(expected, actual);

        input = new int[]{2, 4, 5};
        try {
            mCounter.getCount(input);
            fail("Expected OutOfRangeException was not thrown.");
        } catch (OutOfRangeException e) {
            assertEquals(4, e.getArgument());
            assertEquals(0, e.getLowerBound());
            assertEquals(3, e.getUpperBound());
        }

        input = new int[]{2, 3, 4, 5};
        try {
            mCounter.getCount(input);
            fail("Expected DimensionMismatchException was not thrown.");
        } catch (DimensionMismatchException e) {
            assertEquals(4, e.getDimension1());
            assertEquals(3, e.getDimension2());
        }
    }

    @Test
    public void testGetSize() {
        int expected = 24;
        int actual = mCounter.getSize();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSizes() {
        int[] expected = {2, 3, 4};
        int[] actual = mCounter.getSizes();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "[0][1][2]";
        String actual = mCounter.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = mCounter.iterator();
        assertTrue(iterator.hasNext());

        int[][] expectedCounts = {
            {0, 0, 0},
            {0, 0, 1},
            {0, 0, 2},
            {0, 1, 0},
            {0, 1, 1},
            {0, 1, 2},
            {1, 0, 0},
            {1, 0, 1},
            {1, 0, 2},
            {1, 1, 0},
            {1, 1, 1},
            {1, 1, 2}
        };

        int count = 0;
        while (iterator.hasNext()) {
            int[] expected = expectedCounts[count];
            int[] actual = iterator.getCounts();
            assertArrayEquals(expected, actual);
            assertEquals(count, iterator.getCount());
            iterator.next();
            count++;
        }

        assertFalse(iterator.hasNext());
    }
}