import org.apache.commons.math3.exception.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() throws NotStrictlyPositiveException {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertNotNull(counter);
        assertEquals(2, counter.getDimension());
        assertEquals(6, counter.getSize());
        int[] expectedSize = {2, 3};
        assertArrayEquals(expectedSize, counter.getSizes());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSize() throws NotStrictlyPositiveException {
        int[] size = {2, -3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
    }

    @Test
    public void testIterator() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator<Integer> iterator = counter.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next().intValue());
        assertEquals(1, iterator.next().intValue());
        assertEquals(2, iterator.next().intValue());
        assertEquals(3, iterator.next().intValue());
        assertEquals(4, iterator.next().intValue());
        assertEquals(5, iterator.next().intValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] counts = counter.getCounts(0);
        int[] expectedCounts = {0, 0};
        assertArrayEquals(expectedCounts, counts);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsInvalidIndex() throws OutOfRangeException {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCounts(6);
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int count = counter.getCount(0, 1);
        assertEquals(1, count);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountInvalidDimensions() throws OutOfRangeException, DimensionMismatchException {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountInvalidIndex() throws OutOfRangeException, DimensionMismatchException {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(-1, 1);
    }

    @Test
    public void testToString() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        String expectedString = "[0][0]";
        assertEquals(expectedString, counter.toString());
    }
}