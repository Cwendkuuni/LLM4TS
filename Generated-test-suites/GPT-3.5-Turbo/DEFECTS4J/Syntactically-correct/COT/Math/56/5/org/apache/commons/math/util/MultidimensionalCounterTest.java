
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.util.MultidimensionalCounter;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        // Test valid sizes
        MultidimensionalCounter counter1 = new MultidimensionalCounter(2, 4, 3);
        Assert.assertEquals(3, counter1.getDimension());
        Assert.assertArrayEquals(new int[]{2, 4, 3}, counter1.getSizes());

        // Test invalid sizes
        try {
            MultidimensionalCounter counter2 = new MultidimensionalCounter(0, 4, 3);
            Assert.fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        
        // Test iteration
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            int next = iterator.next();
            Assert.assertEquals(count, next);
            count++;
        }
        
        // Test unidimensional count
        Assert.assertEquals(counter.getSize(), iterator.getCount());

        // Test reset
        iterator = counter.iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        Assert.assertEquals(-1, iterator.getCount());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        
        // Test valid indices
        Assert.assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        Assert.assertArrayEquals(new int[]{0, 0, 1}, counter.getCounts(1));
        Assert.assertArrayEquals(new int[]{1, 3, 2}, counter.getCounts(23));

        // Test invalid indices
        try {
            counter.getCounts(-1);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
        
        try {
            counter.getCounts(100);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        
        // Test valid counts
        Assert.assertEquals(0, counter.getCount(0, 0, 0));
        Assert.assertEquals(1, counter.getCount(0, 0, 1));
        Assert.assertEquals(23, counter.getCount(1, 3, 2));

        // Test invalid counts
        try {
            counter.getCount(-1, 0, 0);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
        
        try {
            counter.getCount(0, 0, 10);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }

        try {
            counter.getCount(0, 5, 0);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
        
        try {
            counter.getCount(0, 3, 4);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }

        try {
            counter.getCount(0, 3);
            Assert.fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        Assert.assertEquals(24, counter.getSize());
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        Assert.assertEquals("[0][0][0]", counter.toString());
    }
}
