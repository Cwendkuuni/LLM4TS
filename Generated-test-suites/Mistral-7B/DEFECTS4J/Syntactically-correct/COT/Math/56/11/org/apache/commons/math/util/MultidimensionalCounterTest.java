import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test class for MultidimensionalCounter.
 */
public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertEquals(3, mdc.getDimension());
        assertEquals(2 * 4 * 3, mdc.getSize());
        assertEquals(size, mdc.getSizes());
    }

    @Test
    public void testIteratorHasNext() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iter = mdc.iterator();

        // check the initial state
        assertFalse(iter.hasNext());

        // test valid next calls
        for (int i = 0; i < mdc.getSize(); i++) {
            assertTrue(iter.hasNext());
            Integer next = iter.next();
            assertEquals(i, next.intValue());
        }

        // test end of iteration
        assertFalse(iter.hasNext());
    }

    @Test
    public void testIteratorGetCount() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iter = mdc.iterator();

        for (int i = 0; i < mdc.getSize(); i++) {
            Integer next = iter.next();
            int count = iter.getCount();
            assertEquals(i, next.intValue());
            assertEquals(i, count);
        }

        assertFalse(iter.hasNext());
    }

    @Test
    public void testIteratorGetCounts() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);

        MultidimensionalCounter.Iterator iter = mdc.iterator();

        for (int i = 0; i < mdc.getSize(); i++) {
            int[] counts = iter.getCounts();
            Integer next = iter.next();
            int expectedCount = i;
            assertEquals(expectedCount, next.intValue());
            int[] expectedMultidimensionalCounts = new int[]{expectedCount / size[last], expectedCount % size[last], expectedCount % size[last - 1], expectedCount % size[last - 2]};
            assertArrayEquals(expectedMultidimensionalCounts, counts);
        }

        assertFalse(iter.hasNext());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountNegativeIndex() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCount(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeIndex() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCount(mdc.getSize());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountInvalidDimension() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCount(mdc.getDimension() + 1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNotStrictlyPositiveSizes() {
        int[] size = {-1, 2, 3};
        assertThrows(NotStrictlyPositiveException.class, () -> new MultidimensionalCounter(size));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroSizes() {
        int[] size = {0, 2, 3};
        assertThrows(NotStrictlyPositiveException.class, () -> new MultidimensionalCounter(size));
    }
}