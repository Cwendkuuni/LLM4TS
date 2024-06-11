import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        int count = 0;
        while(iterator.hasNext()){
            iterator.next();
            count++;
        }
        
        assertEquals(24, count);
    }
    
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        int[] counts = counter.getCounts(0);
        assertArrayEquals(new int[]{0, 0, 0}, counts);
        
        counts = counter.getCounts(6);
        assertArrayEquals(new int[]{0, 2, 0}, counts);
        
        counts = counter.getCounts(23);
        assertArrayEquals(new int[]{1, 2, 3}, counts);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        counter.getCounts(24);
    }
    
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        int count = counter.getCount(0, 0, 0);
        assertEquals(0, count);
        
        count = counter.getCount(0, 2, 0);
        assertEquals(6, count);
        
        count = counter.getCount(1, 2, 3);
        assertEquals(23, count);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        counter.getCount(0, 0);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        counter.getCount(2, 0, 0);
    }
    
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        int size = counter.getSize();
        assertEquals(24, size);
    }
    
    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        int[] sizes = counter.getSizes();
        assertArrayEquals(new int[]{2, 3, 4}, sizes);
    }
    
    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        String str = counter.toString();
        assertEquals("[0][0][0]", str);
    }
}