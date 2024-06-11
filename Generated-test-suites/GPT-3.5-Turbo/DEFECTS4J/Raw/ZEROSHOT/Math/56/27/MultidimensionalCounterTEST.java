import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.util.MathUtils;
import org.apache.commons.math.util.MultidimensionalCounter;

public class MultidimensionalCounterTest {

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        int[] expectedCount = {0, 0, 0};
        int expectedUniCount = 0;
        int[] expectedMultiCount = {0, 0, 0};
        Assert.assertEquals(expectedUniCount, iterator.getCount());
        Assert.assertArrayEquals(expectedCount, iterator.getCounts());

        while (iterator.hasNext()) {
            int[] currentMultiCount = iterator.getCounts();
            int currentUniCount = iterator.next();

            for (int i = 0; i < expectedMultiCount.length; i++) {
                Assert.assertEquals(expectedMultiCount[i], currentMultiCount[i]);
            }
            Assert.assertEquals(expectedUniCount + 1, currentUniCount);

            // Update expected values
            expectedUniCount++;
            if (expectedCount[2] == 3) {
                expectedCount[2] = 0;
                if (expectedCount[1] == 2) {
                    expectedCount[1] = 0;
                    expectedCount[0] = 1;
                } else {
                    expectedCount[1]++;
                }
            } else {
                expectedCount[2]++;
            }

            for (int i = 0; i < expectedMultiCount.length; i++) {
                expectedMultiCount[i] = expectedCount[i];
            }
        }
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        int[] expectedCounts0 = {0, 0, 0};
        int[] expectedCounts1 = {0, 0, 1};
        int[] expectedCounts2 = {0, 0, 2};
        int[] expectedCounts3 = {0, 1, 0};
        int[] expectedCounts12 = {1, 0, 0};
        int[] expectedCounts23 = {1, 3, 2};

        Assert.assertArrayEquals(expectedCounts0, counter.getCounts(0));
        Assert.assertArrayEquals(expectedCounts1, counter.getCounts(1));
        Assert.assertArrayEquals(expectedCounts2, counter.getCounts(2));
        Assert.assertArrayEquals(expectedCounts3, counter.getCounts(3));
        Assert.assertArrayEquals(expectedCounts12, counter.getCounts(12));
        Assert.assertArrayEquals(expectedCounts23, counter.getCounts(23));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCounts(25);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        int[] counts0 = {0, 0, 0};
        int[] counts1 = {0, 0, 1};
        int[] counts12 = {1, 0, 0};
        int[] counts23 = {1, 3, 2};

        int expectedCount0 = 0;
        int expectedCount1 = 1;
        int expectedCount12 = 12;
        int expectedCount23 = 23;

        Assert.assertEquals(expectedCount0, counter.getCount(counts0));
        Assert.assertEquals(expectedCount1, counter.getCount(counts1));
        Assert.assertEquals(expectedCount12, counter.getCount(counts12));
        Assert.assertEquals(expectedCount23, counter.getCount(counts23));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] counts = {2, 1};
        counter.getCount(counts);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] counts = {0, 1, 5};
        counter.getCount(counts);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int expectedSize = 24;
        Assert.assertEquals(expectedSize, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] expectedSizes = {2, 3, 4};
        Assert.assertArrayEquals(expectedSizes, counter.getSizes());
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        String expectedString = "[0][0][0]";
        Assert.assertEquals(expectedString, counter.toString());
    }

}