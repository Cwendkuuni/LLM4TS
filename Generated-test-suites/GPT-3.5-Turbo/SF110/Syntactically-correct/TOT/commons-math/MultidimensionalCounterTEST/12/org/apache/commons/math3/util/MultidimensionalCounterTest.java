import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class MultidimensionalCounterTest {

    private final int[] sizes;

    public MultidimensionalCounterTest(int[] sizes) {
        this.sizes = sizes;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> sizes() {
        return Arrays.asList(new Object[][]{
                {new int[]{3, 4, 5}},
                {new int[]{1, 2, 3}},
                {new int[]{5, 5, 5}}
        });
    }

    private MultidimensionalCounter counter;

    @Before
    public void setup() throws NotStrictlyPositiveException {
        counter = new MultidimensionalCounter(sizes);
    }

    @Test
    public void testGetDimension() {
        Assert.assertEquals(sizes.length, counter.getDimension());
    }

    @Test
    public void testGetSize() {
        int expectedSize = 1;
        for (int size : sizes) {
            expectedSize *= size;
        }

        Assert.assertEquals(expectedSize, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        Assert.assertArrayEquals(sizes, counter.getSizes());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWithIncorrectDimensions() throws OutOfRangeException, DimensionMismatchException {
        counter.getCount(1, 2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithOutOfRangeIndex() throws OutOfRangeException, DimensionMismatchException {
        int[] invalidIndices = new int[sizes.length];
        Arrays.fill(invalidIndices, 0);
        invalidIndices[0] = -1;

        counter.getCount(invalidIndices);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsInvalidIndex() throws OutOfRangeException {
        counter.getCounts(counter.getSize() + 1);
    }

    @Test
    public void testGetCountsValidIndices() throws OutOfRangeException {
        int[] expectedCounts = new int[sizes.length];

        for (int i = 0; i < counter.getSize(); i++) {
            int[] indices = counter.getCounts(i);
            Assert.assertArrayEquals(expectedCounts, indices);

            expectedCounts[sizes.length - 1]++;
            for (int j = sizes.length - 1; j > 0; j--) {
                if (expectedCounts[j] >= sizes[j]) {
                    expectedCounts[j] = 0;
                    expectedCounts[j - 1]++;
                } else {
                    break;
                }
            }
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        counter.iterator().remove();
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = 0;

        while (iterator.hasNext()) {
            Assert.assertEquals(count, iterator.getCount());
            count++;
            iterator.next();
        }

        Assert.assertEquals(counter.getSize(), count);
    }
}