import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultidimensionalCounterTest {
    
    private MultidimensionalCounter counter;
    
    @Before
    public void setUp() {
        counter = new MultidimensionalCounter(2, 4, 3);
    }
    
    @Test
    public void testGetDimension() {
        Assert.assertEquals(3, counter.getDimension());
    }
    
    @Test
    public void testGetSize() {
        Assert.assertEquals(24, counter.getSize());
    }
    
    @Test
    public void testGetCounts() {
        int[] counts = counter.getCounts(12);
        Assert.assertArrayEquals(new int[]{1, 0, 0}, counts);
    }
    
    @Test
    public void testGetCountsOutOfRange() {
        try {
            int[] counts = counter.getCounts(24);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Do nothing, test passed
        }
    }
    
    @Test
    public void testGetCount() {
        int count = counter.getCount(1, 3, 2);
        Assert.assertEquals(23, count);
    }
    
    @Test
    public void testGetCountDimensionMismatch() {
        try {
            int count = counter.getCount(1, 3);
            Assert.fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Do nothing, test passed
        }
    }
    
    @Test
    public void testGetCountOutOfRange() {
        try {
            int count = counter.getCount(1, 4, 2);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Do nothing, test passed
        }
    }
    
    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(0, iterator.next().intValue());
        Assert.assertEquals(1, iterator.next().intValue());
        Assert.assertEquals(2, iterator.next().intValue());
        Assert.assertEquals(3, iterator.next().intValue());
        Assert.assertEquals(4, iterator.next().intValue());
        Assert.assertEquals(5, iterator.next().intValue());
        Assert.assertEquals(6, iterator.next().intValue());
        Assert.assertEquals(7, iterator.next().intValue());
        Assert.assertEquals(8, iterator.next().intValue());
        Assert.assertEquals(9, iterator.next().intValue());
        Assert.assertEquals(10, iterator.next().intValue());
        Assert.assertEquals(11, iterator.next().intValue());
        Assert.assertEquals(12, iterator.next().intValue());
        Assert.assertEquals(13, iterator.next().intValue());
        Assert.assertEquals(14, iterator.next().intValue());
        Assert.assertEquals(15, iterator.next().intValue());
        Assert.assertEquals(16, iterator.next().intValue());
        Assert.assertEquals(17, iterator.next().intValue());
        Assert.assertEquals(18, iterator.next().intValue());
        Assert.assertEquals(19, iterator.next().intValue());
        Assert.assertEquals(20, iterator.next().intValue());
        Assert.assertEquals(21, iterator.next().intValue());
        Assert.assertEquals(22, iterator.next().intValue());
        Assert.assertEquals(23, iterator.next().intValue());
        Assert.assertFalse(iterator.hasNext());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }

}