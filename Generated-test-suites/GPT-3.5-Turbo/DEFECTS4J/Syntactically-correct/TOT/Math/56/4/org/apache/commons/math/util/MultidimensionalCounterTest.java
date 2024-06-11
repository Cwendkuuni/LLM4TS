import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Before
    public void setUp() {
        counter = new MultidimensionalCounter(2, 3, 4);
    }

    @Test
    public void testIteration() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        // Test hasNext() and next() methods
        int[] expectedCounts = new int[]{0, 0, 0};
        int count = 0;
        while (iterator.hasNext()) {
            Assert.assertArrayEquals(expectedCounts, iterator.getCounts());
            Assert.assertEquals(count, iterator.getCount());
            count++;
            expectedCounts = getNextCounts(expectedCounts);
            iterator.next();
        }
        Assert.assertEquals(23, iterator.getCount());
        Assert.assertArrayEquals(new int[]{1, 3, 2}, iterator.getCounts());

        // Test getCount(int) method
        Assert.assertEquals(1, iterator.getCount(0));
        Assert.assertEquals(3, iterator.getCount(1));
        Assert.assertEquals(2, iterator.getCount(2));
        
        // Test getCounts(int) method
        Assert.assertArrayEquals(new int[]{1, 3, 2}, counter.getCounts(23));
    }

    @Test
    public void testGetCount() {
        // Typical use cases
        Assert.assertEquals(0, counter.getCount(0, 0, 0));
        Assert.assertEquals(1, counter.getCount(0, 0, 1));
        Assert.assertEquals(2, counter.getCount(0, 0, 2));
        Assert.assertEquals(3, counter.getCount(0, 1, 0));
        Assert.assertEquals(12, counter.getCount(1, 0, 0));
        Assert.assertEquals(23, counter.getCount(1, 3, 2));

        // Edge cases
        Assert.assertEquals(11, counter.getCount(1, 2, 3));
        Assert.assertEquals(7, counter.getCount(1, 1, 3));
        
        // Error scenarios
        try {
            counter.getCount(0, 0, 3);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }

        try {
            counter.getCount(0, 3, 2);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
        
        try {
            counter.getCount(2, 0, 0);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
        
        try {
            counter.getCount(2, 3, 4);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
        
        try {
            counter.getCount(2);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
        
        try {
            counter.getCount(0, 0);
            Assert.fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetSize() {
        Assert.assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        Assert.assertArrayEquals(new int[]{2, 3, 4}, counter.getSizes());
    }

    private int[] getNextCounts(int[] counts) {
        int[] nextCounts = new int[counts.length];
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] < counter.getSizes()[i] - 1) {
                nextCounts[i] = counts[i] + 1;
                break;
            } else {
                nextCounts[i] = 0;
            }
        }
        return nextCounts;
    }

}