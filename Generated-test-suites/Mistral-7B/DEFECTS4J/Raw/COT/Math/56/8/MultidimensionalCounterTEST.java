import org.apache.commons.math.util.MultidimensionalCounter;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    // Test constructor
    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(size);
        assertEquals(3, mdCounter.getDimension());
        assertEquals(2 * 4 * 3, mdCounter.getSize());
    }

    // Test getCounts() method
    @Test
    public void testGetCounts() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(size);

        int[] expectedValues = {0, 1, 0};
        int[] actualValues = mdCounter.getCounts(0);
        assertArrayEquals(expectedValues, actualValues);

        expectedValues = new int[]{1, 1, 1};
        actualValues = mdCounter.getCounts(12);
        assertArrayEquals(expectedValues, actualValues);

        try {
            mdCounter.getCounts(23);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }

    // Test getCount() method
    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(size);

        int expectedValue = 0;
        int actualValue = mdCounter.getCount(0, 0, 0);
        assertEquals(expectedValue, actualValue);

        expectedValue = 1;
        actualValue = mdCounter.getCount(1, 1, 1);
        assertEquals(expectedValue, actualValue);

        try {
            mdCounter.getCount(-1, 1, 1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }

        try {
            mdCounter.getCount(1, -1, 1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }

        try {
            mdCounter.getCount(1, 1, -1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }

        try {
            mdCounter.getCount(2, 2, 2);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    // Test iterator methods
    @Test
    public void testIterator() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iter = mdCounter.iterator();

        assertEquals(Integer.MIN_VALUE, iter.getCount());
        int[] expectedValues = {0, 0, 0};
        int[] actualValues = iter.getCounts();
        assertArrayEquals(expectedValues, actualValues);

        expectedValues = new int[]{0, 0, 1};
        actualValues = iter.next().getCounts();
        assertArrayEquals(expectedValues, actualValues);

        expectedValues = new int[]{0, 1, 0};
        actualValues = iter.next().getCounts();
        assertArrayEquals(expectedValues, actualValues);

        expectedValues = new int[]{1, 0, 0};
        actualValues = iter.next().getCounts();
        assertArrayEquals(expectedValues, actualValues);

        try {
            iter.next();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            // Expected exception
        }
    }

    // Test getSize() method
    @Test
    public void testGetSize() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(size);
        assertEquals(2 * 4 * 3, mdCounter.getSize());
    }

    // Test getSizes() method
    @Test
    public void testGetSizes() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(size);
        int[] expectedValues = {2, 4, 3};
        int[] actualValues = mdCounter.getSizes();
        assertArrayEquals(expectedValues, actualValues);
    }

    // Test toString() method
    @Test
    public void testToString() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(size);
        String expectedValue = "[0][0][0]";
        String actualValue = mdCounter.toString();
        assertEquals(expectedValue, actualValue);

        mdCounter.next();
        expectedValue = "[0][0][1]";
        actualValue = mdCounter.toString();
        assertEquals(expectedValue, actualValue);

        mdCounter.next();
        expectedValue = "[0][1][0]";
        actualValue = mdCounter.toString();
        assertEquals(expectedValue, actualValue);
    }
}