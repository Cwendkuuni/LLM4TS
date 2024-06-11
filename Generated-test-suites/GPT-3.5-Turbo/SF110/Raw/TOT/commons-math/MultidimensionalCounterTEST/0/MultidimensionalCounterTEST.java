import static org.junit.Assert.*;
import org.junit.*;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor_withValidInputArray() {
        int[] size = {2, 3};
        
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        assertNotNull(counter);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_withInvalidInputArray() {
        int[] size = {-1, 3};
        
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
    }
    
    @Test
    public void testGetDimension() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        int dimension = counter.getDimension();
        
        assertEquals(3, dimension);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_withInvalidIndex() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        int[] indices = counter.getCounts(6);
    }
    
    @Test
    public void testGetCounts_withValidIndex() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        int[] indices = counter.getCounts(4);
        
        assertEquals(1, indices[0]);
        assertEquals(1, indices[1]);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_withInvalidInputArray() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        int[] c = {1, 2, 3};
        int count = counter.getCount(c);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCount_withInvalidIndex() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        int[] c = {1, 4};
        int count = counter.getCount(c);
    }
    
    @Test
    public void testGetCount_withValidInputArray() {
        int[] size = {3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        int[] c = {2, 3};
        int count = counter.getCount(c);
        
        assertEquals(11, count);
    }
    
    @Test
    public void testGetSize() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        int totalSize = counter.getSize();
        
        assertEquals(24, totalSize);
    }
    
    @Test
    public void testGetSizes() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        int[] sizes = counter.getSizes();
        
        assertEquals(2, sizes[0]);
        assertEquals(3, sizes[1]);
        assertEquals(4, sizes[2]);
    }
    
    @Test
    public void testToString() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        String result = counter.toString();
        
        assertEquals("[0][0]", result);
    }
    
    @Test
    public void testIterator_hasNext() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        assertTrue(iterator.hasNext());
        iterator.next();
        iterator.next();
        iterator.next();
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testIterator_next() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        int nextCount = iterator.next();
        
        assertEquals(0, nextCount);
    }
    
    @Test
    public void testIterator_getCount() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        int count = iterator.getCount();
        
        assertEquals(-1, count);
    }
    
    @Test
    public void testIterator_getCounts() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        int[] counts = iterator.getCounts();
        
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
    }
    
    @Test
    public void testIterator_getCount_withValidDimension() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        int count = iterator.getCount(1);
        
        assertEquals(0, count);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_remove() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        iterator.remove();
    }

}