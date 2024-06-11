import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
        assertEquals(2 * 4 * 3, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSizes() {
        int[] sizes = {-2, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroSizes() {
        int[] sizes = {0, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        for (int i = 0; i < counter.getSize(); i++) {
            int[] counts = counter.getCounts(i);
            int count = counter.getCount(counts);
            assertEquals(i, count);
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMismatchedCountsDimension() {
        int[] sizes = {2, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] wrongCounts = new int[3];
        counter.getCount(wrongCounts);
    }

    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeCounts() {
        int[] sizes = {2, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] counts = new int[2];
        counts[0] = 5;
        counts[1] = 0;
        counter.getCount(counts);
    }

    @Test
    public void testIterator() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();

        int index = 0;
        while (iterator.hasNext()) {
            int counterIndex = iterator.next();
            int[] counts = iterator.getCounts();
            int count = counter.getCount(counts);
            assertEquals(index, count);
            index++;
        }
    }

    @Test
    public void testToString() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();

        int index = 0;
        while (iterator.hasNext()) {
            int counterIndex = iterator.next();
            String expectedString = "[";
            for (int i = 0; i < counter.getDimension(); i++) {
                int count = counter.getCount(i, counterIndex);
                expectedString += count + "]";
                if (i < counter.getDimension() - 1) {
                    expectedString += ", ";
                }
            }
            String actualString = counter.toString();
            assertEquals(expectedString, actualString);
            index++;
        }
    }
}