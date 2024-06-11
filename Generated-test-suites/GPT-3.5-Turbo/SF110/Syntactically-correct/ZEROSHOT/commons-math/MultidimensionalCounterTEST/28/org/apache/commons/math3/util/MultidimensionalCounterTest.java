import org.apache.commons.math3.exception.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_negativeSize_throwsException() throws NotStrictlyPositiveException {
        new MultidimensionalCounter(-1, 2, 3);
    }

    @Test
    public void testConstructor_validSize_setsDimension() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testIterator_hasNext_returnsTrue() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        Iterator<Integer> iterator = counter.iterator();
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testIterator_hasNext_returnsFalse() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2);
        Iterator<Integer> iterator = counter.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterator_next_returnsNextValue() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        Iterator<Integer> iterator = counter.iterator();
        assertEquals(Integer.valueOf(0), iterator.next());
    }

    @Test
    public void testIterator_next_returnsAllValues() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        Iterator<Integer> iterator = counter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            assertEquals(Integer.valueOf(count), iterator.next());
            count++;
        }
        assertEquals(6, count);
    }

    @Test
    public void testGetCounts_validIndex_returnsCounts() throws NotStrictlyPositiveException, OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int[] counts = counter.getCounts(4);
        assertArrayEquals(new int[]{0, 1}, counts);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_invalidIndex_throwsException() throws NotStrictlyPositiveException, OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2);
        counter.getCounts(2);
    }

    @Test
    public void testGetCount_validArray_returnsCount() throws NotStrictlyPositiveException, OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int count = counter.getCount(1, 2);
        assertEquals(5, count);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_invalidArray_throwsException() throws NotStrictlyPositiveException, OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2);
        counter.getCount(1, 2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_outOfRange_throwsException() throws NotStrictlyPositiveException, OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.getCount(1, 4);
    }

    @Test
    public void testGetSize_returnsSize() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int size = counter.getSize();
        assertEquals(6, size);
    }

    @Test
    public void testGetSizes_returnsSizes() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] sizes = counter.getSizes();
        assertArrayEquals(new int[]{2, 3, 4}, sizes);
    }

    @Test
    public void testToString_returnsString() throws NotStrictlyPositiveException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        String str = counter.toString();
        assertEquals("[0][0]", str);
    }
}