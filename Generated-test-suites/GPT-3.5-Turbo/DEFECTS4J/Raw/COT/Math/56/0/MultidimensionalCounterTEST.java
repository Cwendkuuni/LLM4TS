import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = 0;
        int[][] expected = {{0, 0, 0}, {0, 0, 1}, {0, 0, 2}, {0, 1, 0},
                {0, 1, 1}, {0, 1, 2}, {0, 2, 0}, {0, 2, 1}, {0, 2, 2},
                {0, 3, 0}, {0, 3, 1}, {0, 3, 2}, {1, 0, 0}, {1, 0, 1},
                {1, 0, 2}, {1, 1, 0}, {1, 1, 1}, {1, 1, 2}, {1, 2, 0},
                {1, 2, 1}, {1, 2, 2}, {1, 3, 0}, {1, 3, 1}, {1, 3, 2}};

        while (iterator.hasNext()) {
            int[] actual = iterator.getCounts();
            assertArrayEquals(expected[count], actual);
            count++;
            iterator.next();
        }
        assertEquals(24, count);
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] expected = {0, 1, 2};
        assertArrayEquals(expected, counter.getCounts(2));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        counter.getCounts(8);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int expected = 2;
        int actual = counter.getCount(0, 1, 2);
        assertEquals(expected, actual);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(0, 1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        counter.getCount(2, 1, 2);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int expected = 24;
        int actual = counter.getSize();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] expected = {2, 4, 3};
        assertArrayEquals(expected, counter.getSizes());
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        String expected = "[0][0][0]";
        String actual = counter.toString();
        assertEquals(expected, actual);
    }

}