import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.util.MathUtils;

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
    public void testGetSizes() {
        int[] expectedSizes = {2, 4, 3};
        assertArrayEquals(expectedSizes, counter.getSizes());
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        assertEquals(24, count);
    }

    @Test
    public void testGetCounts() {
        int[] expectedCounts = {1, 2, 1};
        assertArrayEquals(expectedCounts, counter.getCounts(13));
    }

    @Test
    public void testGetCountsOutOfRange() {
        try {
            counter.getCounts(30);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test
    public void testGetCount() {
        int[] counts = {1, 2, 1};
        assertEquals(13, counter.getCount(counts));
    }

    @Test
    public void testGetCountWithWrongDimension() {
        int[] counts = {1, 2, 1, 4};
        try {
            counter.getCount(counts);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test
    public void testGetCountWithOutOfRangeCount() {
        int[] counts = {1, 2, 5};
        try {
            counter.getCount(counts);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

}