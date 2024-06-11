
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.util.MultidimensionalCounter;
import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        MultidimensionalCounter.Iterator iterator = counter.iterator();

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(-1, iterator.getCount());
        Assert.assertArrayEquals(new int[]{0, 0, 0}, iterator.getCounts());

        for (int i = 0; i < 23; i++) {
            iterator.next();
        }

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(23, iterator.getCount());
        Assert.assertArrayEquals(new int[]{1, 3, 2}, iterator.getCounts());

        iterator.next();

        Assert.assertFalse(iterator.hasNext());
        Assert.assertEquals(24, iterator.getCount());
        Assert.assertArrayEquals(new int[]{1, 3, 2}, iterator.getCounts());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        Assert.assertArrayEquals(new int[] {0, 0, 0}, counter.getCounts(0));
        Assert.assertArrayEquals(new int[] {1, 3, 2}, counter.getCounts(23));

        try {
            counter.getCounts(-1);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // expected
        }

        try {
            counter.getCounts(24);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // expected
        }
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        Assert.assertEquals(0, counter.getCount(0, 0, 0));
        Assert.assertEquals(23, counter.getCount(1, 3, 2));

        Assert.assertEquals(1, counter.getCount(0, 0, 1));
        Assert.assertEquals(24, counter.getCount(1, 3, 3));

        try {
            counter.getCount(2, 0, 0);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // expected
        }

        try {
            counter.getCount(-1, 0, 0);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // expected
        }
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        Assert.assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        Assert.assertArrayEquals(new int[]{2, 3, 4}, counter.getSizes());
    }

    @Test
    public void testNotStrictlyPositiveException() {
        try {
            new MultidimensionalCounter(-1, 3, 4);
            Assert.fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testDimensionMismatchException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        try {
            counter.getCount(0, 1);
            Assert.fail("Expected DimensionMismatchException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            counter.getCount(0, 1, 2, 3);
            Assert.fail("Expected DimensionMismatchException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testOutOfRangeExceptions() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        try {
            counter.getCount(-1, 0, 0);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // expected
        }

        try {
            counter.getCount(2, 0, 0);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // expected
        }
    }
}

