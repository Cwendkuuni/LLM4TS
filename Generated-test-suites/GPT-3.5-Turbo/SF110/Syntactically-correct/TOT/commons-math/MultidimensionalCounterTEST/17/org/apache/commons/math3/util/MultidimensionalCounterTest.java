import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultidimensionalCounterTest {
    
    private MultidimensionalCounter counter;
    
    @Before
    public void setUp() throws NotStrictlyPositiveException {
        counter = new MultidimensionalCounter(3, 3, 3);
    }
    
    @After
    public void tearDown() {
        counter = null;
    }
    
    @Test
    public void testGetDimension() {
        Assert.assertEquals(3, counter.getDimension());
    }
    
    @Test
    public void testGetCounts_validIndex() throws OutOfRangeException {
        int[] counts = counter.getCounts(13);
        Assert.assertArrayEquals(new int[]{1, 0, 1}, counts);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_invalidIndex() throws OutOfRangeException {
        counter.getCounts(27);
    }
    
    @Test
    public void testGetCount_validIndices() throws OutOfRangeException, DimensionMismatchException {
        int count = counter.getCount(1, 2, 1);
        Assert.assertEquals(21, count);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_invalidIndices() throws OutOfRangeException, DimensionMismatchException {
        counter.getCount(1, 2);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCount_outOfRangeIndex() throws OutOfRangeException, DimensionMismatchException {
        counter.getCount(1, 3, 1);
    }
    
    @Test
    public void testGetSize() {
        Assert.assertEquals(27, counter.getSize());
    }
    
    @Test
    public void testGetSizes() {
        int[] sizes = counter.getSizes();
        Assert.assertArrayEquals(new int[]{3, 3, 3}, sizes);
    }
    
    @Test
    public void testIterator_hasNext() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        Assert.assertTrue(iterator.hasNext());
    }
    
    @Test
    public void testIterator_next() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        Assert.assertEquals(0, iterator.next().intValue());
        Assert.assertEquals(1, iterator.next().intValue());
        Assert.assertEquals(2, iterator.next().intValue());
    }
    
    @Test
    public void testIterator_getCount() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        Assert.assertEquals(-1, iterator.getCount());
        iterator.next();
        Assert.assertEquals(0, iterator.getCount());
    }
    
    @Test
    public void testIterator_getCounts() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int[] counts = iterator.getCounts();
        Assert.assertArrayEquals(new int[]{0, 0, 0}, counts);
        iterator.next();
        counts = iterator.getCounts();
        Assert.assertArrayEquals(new int[]{0, 0, 1}, counts);
    }
    
    @Test
    public void testIterator_getCount_dim() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        Assert.assertEquals(-1, iterator.getCount(0));
        iterator.next();
        Assert.assertEquals(0, iterator.getCount(0));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_remove() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }
}