import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.IntStream;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructorWithValidSizes() {
        int[] sizes = new int[]{3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
        assertEquals(1440, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeSizes() {
        int[] sizes = new int[]{-1, 4, 5};
        try {
            new MultidimensionalCounter(sizes);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithSizeZeroDimensions() {
        int[] sizes = new int[]{0, 4, 5};
        try {
            new MultidimensionalCounter(sizes);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testMultidimensionalCounterIterator() {
        int[] sizes = new int[]{3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();

        int count = 0;
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            assertEquals(next.intValue(), count);
            ++count;
        }

        // Check that there are no more elements
        assertFalse(iterator.hasNext());

        // Check that getCount returns the correct value
        for (int i = 0; i < counter.getDimension(); i++) {
            int dimCount = iterator.getCount(i);
            assertEquals(size(i + 1), dimCount);
        }
    }

    @Test
    public void testGetCounts() {
        int[] sizes = new int[]{3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        int[] expected = new int[]{0, 0, 0};
        int[] actual = counter.getCounts(0);
        assertArrayEquals(expected, actual);

        int[] expectedLast = new int[]{size(sizes.length - 1), size(sizes.length - 1) - 1, size(sizes.length - 1) - 1};
        int[] actualLast = counter.getCounts(counter.getSize() - 1);
        assertArrayEquals(expectedLast, actualLast);

        // Test getCounts for different indices
        for (int i = 0; i < counter.getSize() - 2; i++) {
            int[] expectedI = new int[counter.getDimension()];
            int index = i;
            for (int j = 0; j < counter.getDimension(); j++) {
                expectedI[j] = index % size(j + 1);
                index /= size(j + 1);
            }
            int[] actualI = counter.getCounts(i);
            assertArrayEquals(expectedI, actualI);
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = new int[]{3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int index = counter.getSize() + 1;
        try {
            counter.getCount(index);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithInvalidDimensions() {
        int[] sizes = new int[]{3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int invalidDimension = 5;
        try {
            counter.getCount(invalidDimension);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    @Test
    public void testToString() {
        int[] sizes = new int[]{3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        String expected = "[0][0][0]";
        String actual = counter.toString();
        assertEquals(expected, actual);

        counter.next();
        expected = "[0][0][1]";
        actual = counter.toString();
        assertEquals(expected, actual);

        // Test toString for all elements
        for (int i = 0; i < counter.getSize(); i++) {
            counter.next();
            expected = Arrays.toString(counter.getCounts());
            actual = counter.toString();
            assertEquals(expected, actual);
        }

        // Test toString after reset
        counter.reset();
        expected = "[0][0][0]";
        actual = counter.toString();
        assertEquals(expected, actual);
    }

    private int size(int i) {
        return sizes[i - 1];
    }

    private int[] sizes;
}