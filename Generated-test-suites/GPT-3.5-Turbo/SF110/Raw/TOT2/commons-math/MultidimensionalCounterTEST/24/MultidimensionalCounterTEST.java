import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.*;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Before
    public void setUp() {
        counter = new MultidimensionalCounter(3, 4, 5);
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSizes() {
        int[] expectedSizes = {3, 4, 5};
        assertArrayEquals(expectedSizes, counter.getSizes());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsInvalidIndex() {
        counter.getCounts(60);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountInvalidDimension() {
        counter.getCount(1, 2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        counter.getCount(3, 4, 6);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            assertEquals(count, iterator.next().intValue());
            count++;
        }
    }
}