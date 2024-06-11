import org.apache.commons.math.util.MultidimensionalCounter;
import org.apache.commons.math.util.MathUtils;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * Test cases for MultidimensionalCounter class.
 */
public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertNotNull(mdc);
        assertEquals(3, mdc.getDimension());
        assertArrayEquals(size, mdc.getSizes());
        assertEquals(24, mdc.getSize());
    }

    @Test
    public void testIterator() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator iterator = mdc.iterator();
        assertNotNull(iterator);
        assertEquals(0, iterator.getCount());
        int[] expectedCounts = {0, 0, 0};
        int[] actualCounts = iterator.getCounts();
        assertArrayEquals(expectedCounts, actualCounts);
        for (int i = 0; i < 24; i++) {
            assertEquals(i, iterator.next());
            expectedCounts = MathUtils.increment(expectedCounts);
            actualCounts = iterator.getCounts();
            assertArrayEquals(expectedCounts, actualCounts);
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetCount() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        for (int i = 0; i < 24; i++) {
            int index = mdc.getCount();
            int[] expectedCounts = MathUtils.getOneBasedIndex(i, size);
            int[] actualCounts = mdc.getCounts(index);
            assertArrayEquals(expectedCounts, actualCounts);
        }
    }

    @Test
    public void testGetCount_OutOfRangeException() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertThrows(OutOfRangeException.class, () -> mdc.getCount(-1));
        assertThrows(OutOfRangeException.class, () -> mdc.getCount(25));
    }

    @Test
    public void testGetCount_DimensionMismatchException() {
        int[] size1 = {2, 4};
        int[] size2 = {2, 4, 3};
        MultidimensionalCounter mdc1 = new MultidimensionalCounter(size1);
        MultidimensionalCounter mdc2 = new MultidimensionalCounter(size2);
        int[] c1 = {0, 0};
        int[] c2 = {0, 0, 0};
        assertThrows(DimensionMismatchException.class, () -> mdc1.getCount(c2));
    }

    @Test
    public void testGetSizes() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertArrayEquals(size, mdc.getSizes());
    }

    @Test
    public void testGetSize() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertEquals(24, mdc.getSize());
    }

    @Test
    public void testToString() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        for (int i = 0; i < 24; i++) {
            String expected = "[[" + i / size[size.length - 1] + "][" +
                    (i % size[size.length - 1]) + "]]";
            assertEquals(expected, mdc.toString());
            int[] counts = mdc.getCounts(i);
            String actual = "[";
            for (int j = 0; j < size.length; j++) {
                actual += "[" + counts[j] + "]";
                if (j < size.length - 1) {
                    actual += ", ";
                }
            }
            actual += "]";
            assertEquals(actual, mdc.toString());
            mdc.next();
        }
        assertFalse(mdc.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator iterator = mdc.iterator();
        iterator.remove();
    }
}