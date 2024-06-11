import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.*;

public class MultidimensionalCounterTest {
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_withInvalidSize_throwsNotStrictlyPositiveException() throws NotStrictlyPositiveException {
        int[] size = {0, 5, 10};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
    }
    
    @Test
    public void testIterator_hasNext_returnsTrue() throws NotStrictlyPositiveException {
        int[] size = {2, 2, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator<Integer> iterator = counter.iterator();
        assertTrue(iterator.hasNext());
    }
    
    @Test
    public void testIterator_hasNext_returnsFalse() throws NotStrictlyPositiveException {
        int[] size = {1, 1, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator<Integer> iterator = counter.iterator();
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testIterator_next_incrementCounter() throws NotStrictlyPositiveException {
        int[] size = {2, 2, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator<Integer> iterator = counter.iterator();
        assertEquals(0, (int) iterator.next());
        assertEquals(1, counter.getCount(0));
        assertEquals(2, counter.getCount(1, 0));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_remove_throwsUnsupportedOperationException() throws NotStrictlyPositiveException {
        int[] size = {1, 1, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator<Integer> iterator = counter.iterator();
        iterator.remove();
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_withInvalidIndex_throwsOutOfRangeException() throws NotStrictlyPositiveException, OutOfRangeException {
        int[] size = {2, 2, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCounts(8);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_withInvalidDimension_throwsDimensionMismatchException() throws NotStrictlyPositiveException, DimensionMismatchException {
        int[] size = {2, 2, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(1, 1);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCount_withInvalidIndex_throwsOutOfRangeException() throws NotStrictlyPositiveException, OutOfRangeException {
        int[] size = {2, 2, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(3, 1, 0);
    }
    
    @Test
    public void testGetCount_returnsCorrectCount() throws NotStrictlyPositiveException, OutOfRangeException, DimensionMismatchException {
        int[] size = {2, 2, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(3, counter.getCount(1, 1, 0));
        assertEquals(7, counter.getCount(1, 1, 1));
    }
    
    @Test
    public void testGetSize_returnsCorrectSize() throws NotStrictlyPositiveException {
        int[] size = {2, 2, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(8, counter.getSize());
    }
    
    @Test
    public void testGetSizes_returnsCorrectSizes() throws NotStrictlyPositiveException {
        int[] size = {2, 2, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] sizes = counter.getSizes();
        assertEquals(2, sizes.length);
        assertEquals(2, sizes[0]);
        assertEquals(2, sizes[1]);
    }
    
    @Test
    public void testToString_returnsCorrectStringRepresentation() throws NotStrictlyPositiveException {
        int[] size = {2, 2, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals("[0][0][0]", counter.toString());
    }
}