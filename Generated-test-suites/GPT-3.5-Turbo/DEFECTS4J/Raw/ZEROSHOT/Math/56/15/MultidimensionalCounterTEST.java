import org.apache.commons.math.util.MultidimensionalCounter;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    // Create an instance of MultidimensionalCounter for testing
    private MultidimensionalCounter counter;

    // Method to initialize MultidimensionalCounter for testing
    public void initCounter() {
        try {
            counter = new MultidimensionalCounter(2, 4, 3);
        } catch (NotStrictlyPositiveException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetDimension() {
        initCounter();
        assertEquals(3, counter.getDimension());
    }
    
    @Test
    public void testGetSize() {
        initCounter();
        assertEquals(24, counter.getSize());
    }
    
    @Test
    public void testGetSizes() {
        initCounter();
        int[] expectedSizes = {2, 4, 3};
        assertArrayEquals(expectedSizes, counter.getSizes());
    }
    
    @Test
    public void testGetCounts() {
        initCounter();
        assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{0, 0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[]{0, 1, 0}, counter.getCounts(3));
        assertArrayEquals(new int[]{1, 3, 2}, counter.getCounts(23));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        initCounter();
        counter.getCounts(24);
    }
    
    @Test
    public void testGetCount() {
        initCounter();
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(3, counter.getCount(0, 1, 0));
        assertEquals(23, counter.getCount(1, 3, 2));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWrongDimensions() {
        initCounter();
        counter.getCount(0, 0);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        initCounter();
        counter.getCount(2, 0, 0);
    }

    @Test
    public void testIterator() {
        initCounter();
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, (int) iterator.next());
        int[][] expectedCounts = {
            {0, 0, 1},
            {0, 0, 2},
            {0, 1, 0},
            {0, 1, 1},
            {0, 1, 2},
            {0, 1, 3},
            {0, 2, 0},
            {0, 2, 1},
            {0, 2, 2},
            {0, 2, 3},
            {1, 0, 0},
            {1, 0, 1},
            {1, 0, 2},
            {1, 1, 0},
            {1, 1, 1},
            {1, 1, 2},
            {1, 1, 3},
            {1, 2, 0},
            {1, 2, 1},
            {1, 2, 2},
            {1, 2, 3},
            {1, 3, 0},
            {1, 3, 1},
            {1, 3, 2}
        };
        int count = 1;
        while(iterator.hasNext()) {
            assertEquals(count, (int) iterator.next());
            assertArrayEquals(expectedCounts[count-1], iterator.getCounts());
            count++;
        }
        assertEquals(24, count);
    }
}