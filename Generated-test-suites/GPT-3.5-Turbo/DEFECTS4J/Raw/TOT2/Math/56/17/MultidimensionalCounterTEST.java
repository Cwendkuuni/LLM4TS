import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {
    
    @Test
    public void testGetDimension() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
    }
    
    @Test
    public void testGetSizes() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertArrayEquals(sizes, counter.getSizes());
    }
    
    @Test
    public void testGetSize() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(24, counter.getSize());
    }
    
    @Test
    public void testIterator() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertNotNull(counter.iterator());
    }
    
    @Test
    public void testGetCounts() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] expectedCounts = {0, 0, 0};
        assertArrayEquals(expectedCounts, counter.getCounts(0));
        
        int[] expectedCounts2 = {1, 0, 0};
        assertArrayEquals(expectedCounts2, counter.getCounts(1));
    }
    
    @Test
    public void testGetCount() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(0, counter.getCount(0, 0, 0));
        
        assertEquals(23, counter.getCount(1, 2, 3));
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountsException() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCounts(30);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountException() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(0, 0);
    }
}