import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.exception.*;

public class MultidimensionalCounterTest {

    // Test cases for MultidimensionalCounter constructor
    
    @Test
    public void testConstructor_PositiveSize_Success() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Assert.assertEquals(3, counter.getDimension());
        Assert.assertArrayEquals(size, counter.getSizes());
        Assert.assertEquals(24, counter.getSize());
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NegativeSize_ThrowsException() {
        int[] size = {2, -3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
    }
    
    // Test cases for getCounts method
    
    @Test
    public void testGetCounts_ValidIndex_ReturnsCorrectCounts() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] counts = counter.getCounts(10);
        int[] expectedCounts = {1, 1, 2};
        Assert.assertArrayEquals(expectedCounts, counts);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_InvalidIndex_ThrowsException() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCounts(30);
    }
    
    // Test cases for getCount method
    
    @Test
    public void testGetCount_ValidCounts_ReturnsCorrectCount() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] counts = {1, 1, 2};
        int count = counter.getCount(counts);
        Assert.assertEquals(10, count);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_InvalidCounts_ThrowsException() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] counts = {1, 1};
        counter.getCount(counts);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCount_OutOfRangeCounts_ThrowsException() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] counts = {1, 1, 5};
        counter.getCount(counts);
    }
    
    // Test cases for Iterator
    
    @Test
    public void testIterator_HasNext_ReturnsTrue() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        Assert.assertTrue(iterator.hasNext());
    }
    
    @Test
    public void testIterator_HasNext_ReturnsFalse() {
        int[] size = {2, 3, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        Assert.assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testIterator_Next_ReturnsNextCounts() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        int[] firstCounts = iterator.getCounts();
        int[] expectedFirstCounts = {0, 0, 0};
        Assert.assertArrayEquals(expectedFirstCounts, firstCounts);
        
        int firstCount = iterator.getCount();
        Assert.assertEquals(0, firstCount);
        
        iterator.next();
        
        int[] secondCounts = iterator.getCounts();
        int[] expectedSecondCounts = {0, 0, 1};
        Assert.assertArrayEquals(expectedSecondCounts, secondCounts);
        
        int secondCount = iterator.getCount();
        Assert.assertEquals(1, secondCount);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_Remove_ThrowsException() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        iterator.remove();
    }
}