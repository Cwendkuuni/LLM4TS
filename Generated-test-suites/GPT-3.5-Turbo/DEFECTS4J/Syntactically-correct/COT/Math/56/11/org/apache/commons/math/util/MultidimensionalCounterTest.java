import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {

    // Test getCounts method
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);

        // Basic test cases
        Assert.assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        Assert.assertArrayEquals(new int[]{0, 0, 1}, counter.getCounts(1));
        Assert.assertArrayEquals(new int[]{0, 0, 2}, counter.getCounts(2));
        Assert.assertArrayEquals(new int[]{0, 1, 0}, counter.getCounts(3));
        Assert.assertArrayEquals(new int[]{1, 3, 2}, counter.getCounts(23));

        // Edge case: index out of range
        try {
            counter.getCounts(24);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Exception expected
        }
    }

    // Test getCount method
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);

        // Basic test cases
        Assert.assertEquals(0, counter.getCount(0, 0, 0));
        Assert.assertEquals(1, counter.getCount(0, 0, 1));
        Assert.assertEquals(2, counter.getCount(0, 0, 2));
        Assert.assertEquals(3, counter.getCount(0, 1, 0));
        Assert.assertEquals(23, counter.getCount(1, 3, 2));

        // Edge case: index out of range
        try {
            counter.getCount(2, 0, 0);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Exception expected
        }

        // Edge case: dimension mismatch
        try {
            counter.getCount(0, 0);
            Assert.fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Exception expected
        }
    }

    // Test getSize method
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);

        // Basic test case
        Assert.assertEquals(24, counter.getSize());
    }

    // Test getSizes method
    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);

        // Basic test case
        Assert.assertArrayEquals(new int[]{2, 4, 3}, counter.getSizes());
    }

    // Test iterator
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        // Basic test case
        Assert.assertEquals(0, (int) iterator.next());
        Assert.assertEquals(1, (int) iterator.next());
        Assert.assertEquals(2, (int) iterator.next());
        Assert.assertEquals(3, (int) iterator.next());
        Assert.assertEquals(23, (int) iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    // Test toString method
    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);

        // Basic test case
        Assert.assertEquals("[0][0][0]", counter.toString());
    }
}