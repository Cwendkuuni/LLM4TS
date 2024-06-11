
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {

    // Test constructor with different sizes
    @Test
    public void testConstructor() {
        // Typical use case
        MultidimensionalCounter counter1 = new MultidimensionalCounter(2, 4, 3);
        Assert.assertEquals(3, counter1.getDimension());
        int[] expectedSizes1 = {2, 4, 3};
        Assert.assertArrayEquals(expectedSizes1, counter1.getSizes());

        // Edge case: zero sizes
        try {
            MultidimensionalCounter counter2 = new MultidimensionalCounter(0, 0, 0);
            Assert.fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    // Test iterator for different scenarios
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        // Typical use case: Test iterator hasNext()
        Assert.assertTrue(iterator.hasNext());

        // Typical use case: Test iterator next()
        Assert.assertEquals(Integer.valueOf(0), iterator.next());
        Assert.assertEquals(Integer.valueOf(1), iterator.next());
        Assert.assertEquals(Integer.valueOf(2), iterator.next());
        Assert.assertEquals(Integer.valueOf(3), iterator.next());
        Assert.assertEquals(Integer.valueOf(4), iterator.next());
        Assert.assertEquals(Integer.valueOf(5), iterator.next());

        // Typical use case: Test iterator getCount()
        Assert.assertEquals(6, iterator.getCount());

        // Typical use case: Test iterator getCounts()
        int[] expectedCounts = {1, 0, 1};
        Assert.assertArrayEquals(expectedCounts, iterator.getCounts());
    }

    // Test getCounts() for different scenarios
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);

        // Typical use case
        int[] counts1 = counter.getCounts(5);
        int[] expectedCounts1 = {1, 1, 2};
        Assert.assertArrayEquals(expectedCounts1, counts1);

        // Edge case: Index out of range
        try {
            int[] counts2 = counter.getCounts(30);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }

    // Test getCount() for different scenarios
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);

        // Typical use case
        int[] indices1 = {1, 1, 2};
        int count1 = counter.getCount(indices1);
        Assert.assertEquals(23, count1);

        // Dimension mismatch
        try {
            int[] indices2 = {1, 1};
            int count2 = counter.getCount(indices2);
            Assert.fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }

        // Value out of range
        try {
            int[] indices3 = {2, 0, 1};
            int count3 = counter.getCount(indices3);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }

    // Test getSize()
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        Assert.assertEquals(24, counter.getSize());
    }

}

