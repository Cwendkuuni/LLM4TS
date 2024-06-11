import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {
    
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = 0;
        int[][] expectedCounts = {
            {0, 0, 0},
            {0, 0, 1},
            {0, 0, 2},
            {0, 1, 0},
            {0, 1, 1},
            {0, 1, 2},
            {0, 2, 0},
            {0, 2, 1},
            {0, 2, 2},
            {0, 3, 0},
            {0, 3, 1},
            {0, 3, 2},
            {1, 0, 0},
            {1, 0, 1},
            {1, 0, 2},
            {1, 1, 0},
            {1, 1, 1},
            {1, 1, 2},
            {1, 2, 0},
            {1, 2, 1},
            {1, 2, 2},
            {1, 3, 0},
            {1, 3, 1},
            {1, 3, 2}
        };
        while (iterator.hasNext()) {
            int[] counts = iterator.getCounts();
            assertEquals(expectedCounts[count][0], counts[0]);
            assertEquals(expectedCounts[count][1], counts[1]);
            assertEquals(expectedCounts[count][2], counts[2]);
            count++;
            iterator.next();
        }
        assertEquals(expectedCounts.length, count);
    }
    
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[][] expectedCounts = {
            {0, 0, 0},
            {0, 0, 1},
            {0, 0, 2},
            {0, 1, 0},
            {0, 1, 1},
            {0, 1, 2},
            {0, 2, 0},
            {0, 2, 1},
            {0, 2, 2},
            {0, 3, 0},
            {0, 3, 1},
            {0, 3, 2},
            {1, 0, 0},
            {1, 0, 1},
            {1, 0, 2},
            {1, 1, 0},
            {1, 1, 1},
            {1, 1, 2},
            {1, 2, 0},
            {1, 2, 1},
            {1, 2, 2},
            {1, 3, 0},
            {1, 3, 1},
            {1, 3, 2}
        };
        for (int i = 0; i < expectedCounts.length; i++) {
            int[] counts = counter.getCounts(i);
            assertEquals(expectedCounts[i][0], counts[0]);
            assertEquals(expectedCounts[i][1], counts[1]);
            assertEquals(expectedCounts[i][2], counts[2]);
        }
    }
    
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[][] counts = {
            {0, 0, 0},
            {0, 0, 1},
            {0, 0, 2},
            {0, 1, 0},
            {0, 1, 1},
            {0, 1, 2},
            {0, 2, 0},
            {0, 2, 1},
            {0, 2, 2},
            {0, 3, 0},
            {0, 3, 1},
            {0, 3, 2},
            {1, 0, 0},
            {1, 0, 1},
            {1, 0, 2},
            {1, 1, 0},
            {1, 1, 1},
            {1, 1, 2},
            {1, 2, 0},
            {1, 2, 1},
            {1, 2, 2},
            {1, 3, 0},
            {1, 3, 1},
            {1, 3, 2}
        };
        for (int i = 0; i < counts.length; i++) {
            assertEquals(i, counter.getCount(counts[i]));
        }
    }
    
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(24, counter.getSize());
    }
    
    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] sizes = counter.getSizes();
        assertEquals(2, sizes[0]);
        assertEquals(4, sizes[1]);
        assertEquals(3, sizes[2]);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testInvalidIndexGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCounts(25);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testInvalidDimensionGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(0, 0);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testInvalidCountGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(2, 4, 3);
    }
}