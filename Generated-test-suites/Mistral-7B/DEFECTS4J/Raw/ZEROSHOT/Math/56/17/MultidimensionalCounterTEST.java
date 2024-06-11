import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterCreation() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertEquals(3, mdc.getDimension());
        assertEquals(2 * 4 * 3, mdc.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterCreationWithNegativeSizes() {
        int[] sizes = {-1, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testMultidimensionalCounterGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertEquals(0, mdc.getCount());
        for (int i = 0; i < mdc.getSize(); i++) {
            int[] counts = mdc.getCounts(i);
            int count = mdc.getCount(counts);
            assertEquals(i, count);
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterGetCountOutOfRange() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCount(-1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultidimensionalCounterGetCountWithWrongDimension() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        int[] wrongDimensionCounts = {1, 1, 1};
        mdc.getCount(wrongDimensionCounts);
    }

    @Test
    public void testMultidimensionalCounterIterator() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator it = mdc.iterator();
        int count = 0;
        for (Integer next : it) {
            int[] counts = it.getCounts();
            int multidimensionalCount = mdc.getCount(counts);
            int unidimensionalCount = it.getCount();
            assertEquals(multidimensionalCount, unidimensionalCount);
            count++;
        }
        assertEquals(mdc.getSize(), count);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMultidimensionalCounterIteratorRemove() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator it = mdc.iterator();
        it.remove();
    }
}