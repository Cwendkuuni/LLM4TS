import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Before
    public void setUp() {
        // Instantiate MultidimensionalCounter with sizes 2, 4, and 3
        counter = new MultidimensionalCounter(2, 4, 3);
    }

    @Test
    public void testConstructor() {
        // Test that the dimension is set correctly
        assertEquals(3, counter.getDimension());

        // Test that the size array is set correctly
        assertArrayEquals(new int[]{2, 4, 3}, counter.getSizes());

        // Test that the totalSize is calculated correctly
        assertEquals(24, counter.getSize());

        // Test that the uniCounterOffset array is calculated correctly
        assertArrayEquals(new int[]{12, 3, 0}, counter.uniCounterOffset);

        // Test that the last index is set correctly
        assertEquals(2, counter.last);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NegativeSize() {
        // Instantiate MultidimensionalCounter with negative size (-1)
        MultidimensionalCounter counter = new MultidimensionalCounter(-1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_ZeroSize() {
        // Instantiate MultidimensionalCounter with zero size (0)
        MultidimensionalCounter counter = new MultidimensionalCounter(0);
    }

    @Test
    public void testIterator() {
        // Test that the iterator is correctly instantiated
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        // Test that the hasNext() method returns false when there are no more elements
        for (int i = 0; i < counter.getSize(); i++) {
            assertTrue(iterator.hasNext());
            iterator.next();
        }
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testGetCounts() {
        // Test that the counts are calculated correctly for a given index
        assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{0, 0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[]{1, 0, 0}, counter.getCounts(12));
        assertArrayEquals(new int[]{1, 3, 2}, counter.getCounts(23));
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_OutOfRange() {
        // Test that an OutOfRangeException is thrown when the index is greater than totalSize
        counter.getCounts(24);
    }
    
    @Test
    public void testGetCount() {
        // Test that the index within the unidimensional counter is calculated correctly for given counts
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(12, counter.getCount(1, 0, 0));
        assertEquals(23, counter.getCount(1, 3, 2));
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_DimensionMismatch() {
        // Test that a DimensionMismatchException is thrown when the size of counts does not match the size of the array given in the constructor
        counter.getCount(1, 0);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCount_OutOfRange() {
        // Test that an OutOfRangeException is thrown when a value of counts is not in the range of the corresponding dimension
        counter.getCount(0, 0, 3);
    }

    @Test
    public void testToString() {
        // Test that the toString() method returns the expected string representation
        assertEquals("[0][0][0]", counter.toString());
    }

}