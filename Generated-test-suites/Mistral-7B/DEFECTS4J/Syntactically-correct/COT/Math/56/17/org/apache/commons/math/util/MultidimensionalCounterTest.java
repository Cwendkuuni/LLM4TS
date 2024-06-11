import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(sizes.length, counter.getDimension());
        assertEquals(MathUtils.product(sizes), counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSizes() {
        int[] sizes = {-1, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroSizes() {
        int[] sizes = {0, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testIteratorHasNext() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes.clone());
        Iterator iterator = counter.iterator();
        int[] counts;

        for (int i = 0; i < MathUtils.product(sizes); i++) {
            assertTrue(iterator.hasNext());
            counts = iterator.getCounts();
            assertEquals(i, iterator.getCount());
            assertEquals(counts.length, sizes.length);
            for (int j = 0; j < sizes.length; j++) {
                assertEquals(sizes[j], counts[j]);
            }
            iterator.next();
        }
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes.clone());
        Iterator iterator = counter.iterator();
        iterator.remove();
    }

    @Test
    public void testGetCounts() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes.clone());
        int[] counts;

        for (int i = 0; i < MathUtils.product(sizes); i++) {
            counts = counter.getCounts(i);
            assertEquals(sizes.length, counts.length);
            for (int j = 0; j < sizes.length; j++) {
                assertEquals(i / sizes[j] % sizes[j + 1], counts[j]);
            }
            counter.next();
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes.clone());
        counter.getCount(-1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountInvalidDimension() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes.clone());
        counter.getCount(5);
    }

}