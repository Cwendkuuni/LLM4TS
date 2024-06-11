import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.*;
import java.util.*;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounter_ValidSize() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertNotNull(counter);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounter_InvalidSize() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(0, 1, 2);
    }
    
    @Test
    public void testIterator_HasNext() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        assertTrue(iterator.hasNext());
    }
    
    @Test
    public void testIterator_Next_ValidCount() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        int count = iterator.next();
        assertEquals(0, count);
    }
    
    @Test
    public void testIterator_Next_CountsIncremented() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        int count1 = iterator.next();
        int count2 = iterator.next();
        
        assertEquals(count1 + 1, count2);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_Remove() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        iterator.remove();
    }
    
    @Test
    public void testGetDimension_Valid() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        int dimension = counter.getDimension();
        
        assertEquals(3, dimension);
    }
    
    @Test
    public void testGetSize_Valid() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        int size = counter.getSize();
        
        assertEquals(24, size);
    }
    
    @Test
    public void testGetSizes_Valid() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        int[] sizes = counter.getSizes();
        
        assertArrayEquals(new int[]{2, 3, 4}, sizes);
    }
    
    @Test
    public void testToString_Valid() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        String output = counter.toString();
        
        assertEquals("[0][0][0]", output);
    }
    
    @Test
    public void testGetCounts_ValidIndex() throws NotStrictlyPositiveException, OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        int[] counts = counter.getCounts(10);
        
        assertArrayEquals(new int[]{1, 2, 2}, counts);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_InvalidIndex() throws NotStrictlyPositiveException, OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        int[] counts = counter.getCounts(30);
    }
    
    @Test
    public void testGetCount_Valid() throws NotStrictlyPositiveException, DimensionMismatchException, OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        int count = counter.getCount(0, 2, 1);
        
        assertEquals(17, count);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_InvalidDimensionMismatch() throws NotStrictlyPositiveException, DimensionMismatchException, OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        int count = counter.getCount(0, 2);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCount_OutOfRange() throws NotStrictlyPositiveException, DimensionMismatchException, OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        int count = counter.getCount(0, 2, 5);
    }
    
}