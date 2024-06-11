import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;

public class MultidimensionalCounterTest {

    // Test constructor with valid input
    @Test
    public void testConstructor() throws NotStrictlyPositiveException {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(2, counter.getDimension());
        assertEquals(6, counter.getSize());
    }

    // Test constructor with invalid input
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidInput() throws NotStrictlyPositiveException {
        int[] size = {0};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
    }

    // Test iterator
    @Test
    public void testIterator() {
        int[] size = {2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(0), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(1), iterator.next());
        assertFalse(iterator.hasNext());
    }

    // Test getCounts method with valid index
    @Test
    public void testGetCounts() throws OutOfRangeException {
        int[] size = {2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] counts = counter.getCounts(0);
        assertEquals(1, counts.length);
        assertEquals(0, counts[0]);
    }

    // Test getCounts method with invalid index
    @Test(expected = OutOfRangeException.class)
    public void testGetCountsInvalidIndex() throws OutOfRangeException {
        int[] size = {2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] counts = counter.getCounts(1);
    }

    // Test getCount method with valid counts
    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        int[] size = {2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int count = counter.getCount(0);
        assertEquals(0, count);
    }

    // Test getCount method with invalid counts
    @Test(expected = OutOfRangeException.class)
    public void testGetCountInvalidCounts1() throws OutOfRangeException, DimensionMismatchException {
        int[] size = {2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int count = counter.getCount(1);
    }

    // Test getCount method with invalid counts
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountInvalidCounts2() throws OutOfRangeException, DimensionMismatchException {
        int[] size = {2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int count = counter.getCount(1, 2);
    }

    // Test getSize method
    @Test
    public void testGetSize() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(6, counter.getSize());
    }

    // Test getSizes method
    @Test
    public void testGetSizes() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] sizes = counter.getSizes();
        assertEquals(2, sizes.length);
        assertEquals(2, sizes[0]);
        assertEquals(3, sizes[1]);
    }

    // Test toString method
    @Test
    public void testToString() {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals("[0][0]", counter.toString());
    }

    // Test iterator getCount method
    @Test
    public void testIteratorGetCount() {
        int[] size = {2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(0, iterator.getCount());
    }

    // Test iterator getCounts method
    @Test
    public void testIteratorGetCounts() {
        int[] size = {2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int[] counts = iterator.getCounts();
        assertEquals(1, counts.length);
        assertEquals(0, counts[0]);
    }

    // Test iterator getCount method with valid dimension
    @Test
    public void testIteratorGetCountValidDimension() {
        int[] size = {2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = iterator.getCount(0);
        assertEquals(0, count);
    }

    // Test iterator getCount method with invalid dimension
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testIteratorGetCountInvalidDimension() {
        int[] size = {2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = iterator.getCount(1);
    }

    // Test iterator remove method
    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        int[] size = {2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }

    // Test iterator next method
    @Test
    public void testIteratorNext() {
        int[] size = {2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(Integer.valueOf(0), iterator.next());
    }
}
