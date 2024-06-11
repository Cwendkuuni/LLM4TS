import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {

    // Test the creation and size of the multidimensional counter
    @Test
    public void testMultidimensionalCounter() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        Assert.assertEquals(3, counter.getDimension());
        Assert.assertEquals(24, counter.getSize());
        Assert.assertArrayEquals(new int[]{2, 4, 3}, counter.getSizes());
    }

    // Test the creation of an iterator
    @Test
    public void testIterator() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(-1, iterator.getCount());
        Assert.assertArrayEquals(new int[]{0, 0, 0}, iterator.getCounts());
        Assert.assertEquals(0, iterator.getCount(0));
        Assert.assertEquals(0, iterator.next().intValue());
        
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(0, iterator.getCount());
        Assert.assertArrayEquals(new int[]{0, 0, 1}, iterator.getCounts());
        Assert.assertEquals(0, iterator.getCount(1));
        Assert.assertEquals(1, iterator.next().intValue());
        
        // ... Continue testing the iterator and edge cases ...
    }

    // Test the conversion from unidimensional counter to multidimensional indices
    @Test
    public void testGetCounts() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        Assert.assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        Assert.assertArrayEquals(new int[]{0, 0, 1}, counter.getCounts(1));
        Assert.assertArrayEquals(new int[]{0, 0, 2}, counter.getCounts(2));
        Assert.assertArrayEquals(new int[]{0, 1, 0}, counter.getCounts(3));
        
        // ... Continue testing the conversion ...
    }

    // Test the conversion from multidimensional indices to unidimensional counter
    @Test
    public void testGetCount() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        Assert.assertEquals(0, counter.getCount(0, 0, 0));
        Assert.assertEquals(1, counter.getCount(0, 0, 1));
        Assert.assertEquals(2, counter.getCount(0, 0, 2));
        Assert.assertEquals(3, counter.getCount(0, 1, 0));
        
        // ... Continue testing the conversion ...
    }
}