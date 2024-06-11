import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.util.MultidimensionalCounter;
import org.junit.Assert;
import org.junit.Test;


public class MultidimensionalCounterTest {

    // Test cases for the constructor
    @Test
    public void testConstructor_typicalCase() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        Assert.assertEquals(3, counter.getDimension());
        Assert.assertArrayEquals(new int[]{2, 4, 3}, counter.getSizes());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_zeroSize() {
        new MultidimensionalCounter(2, 0, 3);
    }

    // Test cases for the iterator
    @Test
    public void testIterator_hasNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        Assert.assertTrue(iterator.hasNext());
        for (int i = 0; i < counter.getSize() - 1; i++) {
            iterator.next();
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterator_next() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        Assert.assertEquals(new Integer(0), iterator.next());
        Assert.assertEquals(new Integer(1), iterator.next());
        Assert.assertEquals(new Integer(3), iterator.next());
        Assert.assertEquals(new Integer(4), iterator.next());
        Assert.assertEquals(new Integer(23), iterator.next());
    }

    // Test cases for getCounts(int index)
    @Test
    public void testGetCounts_typicalCase() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        Assert.assertArrayEquals(new int[]{1, 3, 2}, counter.getCounts(23));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_outOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCounts(27);
    }

    // Test cases for getCount(int ... c)
    @Test
    public void testGetCount_typicalCase() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        Assert.assertEquals(23, counter.getCount(1, 3, 2));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_dimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(1, 3);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_outOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(1, 3, 6);
    }

    // Test cases for getSize()
    @Test
    public void testGetSize_typicalCase() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        Assert.assertEquals(24, counter.getSize());
    }

    // Test cases for toString()
    @Test
    public void testToString_typicalCase() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        Assert.assertEquals("[0][0][0]", counter.toString());
    }

}