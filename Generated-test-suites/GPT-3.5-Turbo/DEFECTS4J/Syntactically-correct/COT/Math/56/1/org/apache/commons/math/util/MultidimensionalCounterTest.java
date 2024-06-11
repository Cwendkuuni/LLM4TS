
import org.junit.Test;
import org.junit.Assert;

public class MultidimensionalCounterTest {
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(0, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(1, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(2, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(3, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(4, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(5, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(6, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(7, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(8, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(9, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(10, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(11, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(12, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(13, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(14, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(15, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(16, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(17, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(18, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(19, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(20, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(21, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(22, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(23, iterator.next().intValue());
        
        Assert.assertFalse(iterator.hasNext());
        Assert.assertEquals(23, iterator.getCount());
        Assert.assertArrayEquals(new int[]{1, 3, 2}, iterator.getCounts());
        Assert.assertEquals(2, iterator.getCount(0));
        Assert.assertEquals(3, iterator.getCount(1));
        Assert.assertEquals(2, iterator.getCount(2));
    }
    
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        
        // Check conversion from unidimensional counter to multidimensional counts
        Assert.assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        Assert.assertArrayEquals(new int[]{0, 0, 1}, counter.getCounts(1));
        Assert.assertArrayEquals(new int[]{0, 0, 2}, counter.getCounts(2));
        Assert.assertArrayEquals(new int[]{0, 1, 0}, counter.getCounts(3));
        //...
        Assert.assertArrayEquals(new int[]{1, 0, 0}, counter.getCounts(12));
        //...
        Assert.assertArrayEquals(new int[]{1, 3, 2}, counter.getCounts(23));
        
        // Check conversion from multidimensional counts to unidimensional counter
        Assert.assertEquals(0, counter.getCount(0, 0, 0));
        Assert.assertEquals(1, counter.getCount(0, 0, 1));
        Assert.assertEquals(2, counter.getCount(0, 0, 2));
        Assert.assertEquals(3, counter.getCount(0, 1, 0));
        //...
        Assert.assertEquals(12, counter.getCount(1, 0, 0));
        //...
        Assert.assertEquals(23, counter.getCount(1, 3, 2));
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCounts(24);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(2, 4, 3);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(2, 4);
    }
    
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        Assert.assertEquals(24, counter.getSize());
    }
    
    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        Assert.assertArrayEquals(new int[]{2, 4, 3}, counter.getSizes());
    }
}
