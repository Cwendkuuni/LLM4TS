import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.math3.util.MathUtils.copyOf;
import static org.apache.commons.math3.util.Precision.equals;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] dimensions = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(dimensions);
        Assert.assertEquals(3, counter.getDimension());
        Assert.assertArrayEquals(copyOf(dimensions), counter.getSizes());
        Assert.assertEquals(24 * 4 * 3, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSizes() {
        int[] dimensions = {-5, 4, 3};
        new MultidimensionalCounter(dimensions);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroSizes() {
        int[] dimensions = {0, 4, 3};
        new MultidimensionalCounter(dimensions);
    }

    @Test
    public void testIteratorHasNext() {
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 4, 3});
        Iterator iterator = multidimensionalCounter.iterator();
        for (int i = 0; i < multidimensionalCounter.getSize(); i++) {
            assertTrue(iterator.hasNext());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 4, 3});
        Iterator iterator = multidimensionalCounter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            assertEquals(count, next);
            count++;
        }
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 4, 3});
        int[] count = multidimensionalCounter.getCounts(12);
        assertEquals(2, count[0]);
        assertEquals(3, count[1]);
        assertEquals(0, count[2]);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 4, 3});
        multidimensionalCounter.getCounts(24 * 4 * 3 + 1);
    }

    @Test
    public void testGetCountLastDimension() {
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 4, 3});
        int lastCount = multidimensionalCounter.getCount(2, 3, 0);
        assertEquals(0, lastCount);
    }

    @Test
    public void testGetCountLastDimensionMax() {
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 4, 3});
        int lastCount = multidimensionalCounter.getCount(2, 4, size(multidimensionalCounter) - 1);
        assertEquals(3, lastCount);
    }

    @Test
    public void testGetCountFirstDimension() {
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 4, 3});
        int firstCount = multidimensionalCounter.getCount(0, 0, 0);
        assertEquals(0, firstCount);
    }

    @Test
    public void testGetCountFirstDimensionMax() {
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 4, 3});
        int firstCount = multidimensionalCounter.getCount(size(multidimensionalCounter) - 1, 0, 0);
        assertEquals(23, firstCount);
    }

    @Test
    public void testGetCountMiddleDimension() {
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 4, 3});
        int middleCount = multidimensionalCounter.getCount(1, 2, 1);
        assertEquals(1, middleCount);
    }

    @Test
    public void testGetCountMiddleDimensionMax() {
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 4, 3});
        int middleCount = multidimensionalCounter.getCount(1, size(multidimensionalCounter, 1), size(multidimensionalCounter, 2) - 1);
        assertEquals(3, middleCount);
    }

    private int size(MultidimensionalCounter multidimensionalCounter) {
        return multidimensionalCounter.getSize();
    }

    private int size(MultidimensionalCounter multidimensionalCounter, int dimension) {
        return multidimensionalCounter.getSizes()[dimension];
    }
}