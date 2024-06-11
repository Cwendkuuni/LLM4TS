import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {
    
    // Test the constructor
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NegativeSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(-1);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_ZeroSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(0);
    }
    
    @Test
    public void testConstructor_ValidSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
    }
    
    // Test the getDimension method
    
    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(3, counter.getDimension());
    }
    
    // Test the iterator method
    
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            count++;
            iterator.next();
        }
        assertEquals(counter.getSize(), count);
    }
    
    // Test the getCounts method
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_IndexTooLow() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCounts(-1);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_IndexTooHigh() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCounts(counter.getSize());
    }
    
    @Test
    public void testGetCounts_ValidIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] counts = counter.getCounts(12);
        assertArrayEquals(new int[] {1, 0, 0}, counts);
    }
    
    // Test the getCount method
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_DifferentSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(1, 2);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCount_IndexOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(0, 5, 2);
    }
    
    @Test
    public void testGetCount_ValidIndices() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int count = counter.getCount(1, 3, 2);
        assertEquals(23, count);
    }
    
    // Test the getSize method
    
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(24, counter.getSize());
    }
    
    // Test the getSizes method
    
    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] sizes = counter.getSizes();
        assertArrayEquals(new int[] {2, 4, 3}, sizes);
    }
    
    // Test the toString method
    
    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        String str = counter.toString();
        assertEquals("[0][0][0]", str);
    }
}