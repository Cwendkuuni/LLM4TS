import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.math3.util.MathUtils.copyOf;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
        assertEquals(sizes, counter.getSizes());
        assertEquals(24 * 4 * 3, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSizes() {
        int[] sizes = {-1, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroSizes() {
        int[] sizes = {0, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testIteratorBasic() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        int count = 0;
        int[] counts = new int[3];
        int[] countsMultidimensional = new int[3];
        while (iterator.hasNext()) {
            counts[0] = iterator.getCount();
            countsMultidimensional = iterator.getCounts();
            assertEquals(count, counts[0]);
            assertEquals(countsMultidimensional.length, 3);
            for (int i = 0; i < countsMultidimensional.length; i++) {
                assertEquals(i, countsMultidimensional[i]);
            }
            count++;
        }
        assertEquals(24 * 4 * 3, count);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        iterator.remove();
    }

    @Test
    public void testGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int index = 0;
        int[] multidimensionalCounts = counter.getCounts(index);
        int count = counter.getCount(multidimensionalCounts);
        assertEquals(index, count);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsDimensionMismatch() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] multidimensionalCounts = new int[4];
        counter.getCounts(multidimensionalCounts);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int index = 25 * 4 * 3;
        counter.getCount(index);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] multidimensionalCounts = counter.getCounts(-1);
    }

    @Test
    public void testToString() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        String expected = "[0][0][0], [0][0][1], [0][0][2], [0][1][0], ..., [1][2][2]";
        assertEquals(expected, counter.toString());
    }
}