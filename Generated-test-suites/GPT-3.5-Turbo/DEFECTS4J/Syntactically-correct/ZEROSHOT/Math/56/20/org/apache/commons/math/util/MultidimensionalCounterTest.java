import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidSize() {
        MultidimensionalCounter invalidCounter = new MultidimensionalCounter(-2, 4, 3);
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
    public void testGetCounts() {
        int[] expectedCounts = {0, 1, 2};
        assertArrayEquals(expectedCounts, counter.getCounts(2));
    }

    @Test
    public void testGetCount() {
        int expectedCount = 12;
        int[] counts = {1, 0, 0};
        assertEquals(expectedCount, counter.getCount(counts));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidCountArray() {
        int[] invalidCounts = {1, 0};
        int count = counter.getCount(invalidCounts);
    }

    @Test(expected = OutOfRangeException.class)
    public void testInvalidCountIndex() {
        int countIndex = 30;
        int[] counts = counter.getCounts(countIndex);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        int count = 0;
        while (iterator.hasNext()) {
            assertEquals(count, iterator.getCount().intValue());
            count++;
            iterator.next();
        }
        assertEquals(24, count);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }
}