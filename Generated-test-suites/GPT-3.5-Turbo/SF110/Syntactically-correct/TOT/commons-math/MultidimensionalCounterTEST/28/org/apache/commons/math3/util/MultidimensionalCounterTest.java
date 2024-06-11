import static org.junit.Assert.*;
import org.junit.*;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter mCounter;

    @Before
    public void setUp() throws Exception {
        // Initialize the MultidimensionalCounter object with size [2, 3, 4]
        mCounter = new MultidimensionalCounter(2, 3, 4);
    }

    @Test
    public void testGetDimension() {
        // Test the getDimension() method
        assertEquals(3, mCounter.getDimension());
    }

    @Test
    public void testGetSizes() {
        // Test the getSizes() method
        int[] expectedSizes = {2, 3, 4};
        assertArrayEquals(expectedSizes, mCounter.getSizes());
    }

    @Test
    public void testGetSize() {
        // Test the getSize() method
        assertEquals(24, mCounter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidSize() throws NotStrictlyPositiveException {
        // Test when size is not strictly positive
        mCounter = new MultidimensionalCounter(-1, 5, 8);
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        // Test the getCounts() method for various indices

        // Typical use case
        int[] expectedCounts1 = {0, 0, 0};
        assertArrayEquals(expectedCounts1, mCounter.getCounts(0));

        // Typical use case
        int[] expectedCounts2 = {1, 0, 2};
        assertArrayEquals(expectedCounts2, mCounter.getCounts(8));

        // Edge case
        int[] expectedCounts3 = {1, 2, 3};
        assertArrayEquals(expectedCounts3, mCounter.getCounts(23));

        // Error scenario
        try {
            // Index out of range
            mCounter.getCounts(24);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Exception expected
        }
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        // Test the getCount() method for various input arrays

        // Typical use case
        assertEquals(0, mCounter.getCount(0, 0, 0));

        // Typical use case
        assertEquals(8, mCounter.getCount(1, 0, 2));

        // Edge case
        assertEquals(23, mCounter.getCount(1, 2, 3));

        // Error scenario
        try {
            // Invalid dimension
            mCounter.getCount(1, 2);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Exception expected
        }

        try {
            // Out of range index
            mCounter.getCount(1, 2, 4);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Exception expected
        }
    }

    @Test
    public void testIterator() {
        // Test the Iterator's methods

        MultidimensionalCounter.Iterator iterator = mCounter.iterator();

        // Test hasNext() method
        assertTrue(iterator.hasNext());

        // Test next() method
        assertEquals(Integer.valueOf(0), iterator.next());
        assertEquals(Integer.valueOf(1), iterator.next());
        // ...
        assertEquals(Integer.valueOf(23), iterator.next());

        // Test getCount() method
        assertEquals(24, iterator.getCount());

        // Test getCounts() method
        int[] expectedCounts = {1, 2, 3};
        assertArrayEquals(expectedCounts, iterator.getCounts());

        // Test getCount(dim) method
        assertEquals(1, iterator.getCount(0));
        assertEquals(2, iterator.getCount(1));
        assertEquals(3, iterator.getCount(2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        // Test the remove() method, expecting UnsupportedOperationException
        MultidimensionalCounter.Iterator iterator = mCounter.iterator();
        iterator.remove();
    }

}