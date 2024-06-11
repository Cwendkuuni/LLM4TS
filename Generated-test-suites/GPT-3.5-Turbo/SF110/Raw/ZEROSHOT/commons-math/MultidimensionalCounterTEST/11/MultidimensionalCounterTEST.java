import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            Assert.assertEquals(3, counter.getDimension());
            Assert.assertArrayEquals(size, counter.getSizes());
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeSize() throws NotStrictlyPositiveException {
        int[] size = {-1, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
    }
    
    @Test
    public void testIterator() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
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
        Assert.assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testGetCounts() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        try {
            int[] counts = counter.getCounts(0);
            Assert.assertArrayEquals(new int[]{0, 0}, counts);
            counts = counter.getCounts(1);
            Assert.assertArrayEquals(new int[]{0, 1}, counts);
            counts = counter.getCounts(4);
            Assert.assertArrayEquals(new int[]{1, 1}, counts);
            counts = counter.getCounts(5);
            Assert.assertArrayEquals(new int[]{1, 2}, counts);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountsWithInvalidIndex() throws OutOfRangeException {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCounts(6);
    }
    
    @Test
    public void testGetCount() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        try {
            int count = counter.getCount(0, 0);
            Assert.assertEquals(0, count);
            count = counter.getCount(0, 1);
            Assert.assertEquals(1, count);
            count = counter.getCount(1, 1);
            Assert.assertEquals(4, count);
            count = counter.getCount(1, 2);
            Assert.assertEquals(5, count);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWithInvalidNumberOfArguments() throws DimensionMismatchException {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(0, 1, 2);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithInvalidIndex() throws OutOfRangeException {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(2, 1);
    }
    
    @Test
    public void testGetSize() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int totalSize = counter.getSize();
        Assert.assertEquals(6, totalSize);
    }
    
    @Test
    public void testToString() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        String str = counter.toString();
        Assert.assertEquals("[0][0]", str);
    }
    
    @Test
    public void testIteratorGetCount() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        Assert.assertEquals(-1, iterator.getCount());
        Assert.assertArrayEquals(new int[]{0, 0}, iterator.getCounts());
        Assert.assertEquals(0, iterator.getCount(0));
        Assert.assertEquals(0, iterator.getCount(1));
        
        iterator.next();
        Assert.assertEquals(0, iterator.getCount());
        Assert.assertArrayEquals(new int[]{0, 1}, iterator.getCounts());
        Assert.assertEquals(0, iterator.getCount(0));
        Assert.assertEquals(1, iterator.getCount(1));
        
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        Assert.assertEquals(4, iterator.getCount());
        Assert.assertArrayEquals(new int[]{1, 1}, iterator.getCounts());
        Assert.assertEquals(1, iterator.getCount(0));
        Assert.assertEquals(1, iterator.getCount(1));
        
        iterator.next();
        Assert.assertEquals(5, iterator.getCount());
        Assert.assertArrayEquals(new int[]{1, 2}, iterator.getCounts());
        Assert.assertEquals(1, iterator.getCount(0));
        Assert.assertEquals(2, iterator.getCount(1));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }
}