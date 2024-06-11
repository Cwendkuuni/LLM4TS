import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {
    
    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        Assert.assertEquals(3, counter.getDimension());
    }
    
    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] expectedSizes = {2, 4, 3};
        Assert.assertArrayEquals(expectedSizes, counter.getSizes());
    }
    
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        Assert.assertEquals(24, counter.getSize());
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSize() {
        new MultidimensionalCounter(-2, 4, 3);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroSize() {
        new MultidimensionalCounter(0, 4, 3);
    }
    
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(0, (int) iterator.next());
        Assert.assertEquals(1, (int) iterator.next());
        
        Assert.assertTrue(iterator.hasNext());
        Assert.assertArrayEquals(new int[]{0, 1, 0}, iterator.getCounts());
        Assert.assertEquals(2, (int) iterator.next());
        
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(3, (int) iterator.next());
        
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(4, (int) iterator.next());
        
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(5, (int) iterator.next());
        
        Assert.assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        
        int[] counts1 = counter.getCounts(0);
        int[] expectedCounts1 = {0, 0, 0};
        Assert.assertArrayEquals(expectedCounts1, counts1);
        
        int[] counts2 = counter.getCounts(12);
        int[] expectedCounts2 = {1, 0, 0};
        Assert.assertArrayEquals(expectedCounts2, counts2);
        
        int[] counts3 = counter.getCounts(23);
        int[] expectedCounts3 = {1, 3, 2};
        Assert.assertArrayEquals(expectedCounts3, counts3);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCounts(24);
    }
    
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        
        int count1 = counter.getCount(0, 0, 0);
        Assert.assertEquals(0, count1);
        
        int count2 = counter.getCount(0, 0, 1);
        Assert.assertEquals(1, count2);
        
        int count3 = counter.getCount(0, 0, 2);
        Assert.assertEquals(2, count3);
        
        int count4 = counter.getCount(0, 1, 0);
        Assert.assertEquals(3, count4);
        
        int count5 = counter.getCount(1, 0, 0);
        Assert.assertEquals(12, count5);
        
        int count6 = counter.getCount(1, 3, 2);
        Assert.assertEquals(23, count6);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(0, 0);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(0, 0, 3);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }
    
    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        Assert.assertEquals("[0][0][0]", counter.toString());
    }
    
}