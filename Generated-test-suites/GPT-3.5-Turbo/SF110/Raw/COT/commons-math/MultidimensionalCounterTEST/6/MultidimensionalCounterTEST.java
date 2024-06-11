import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.*;

public class MultidimensionalCounterTest {
    private MultidimensionalCounter counter;

    @Before
    public void setup() {
        counter = new MultidimensionalCounter(2, 3);
    }

    // Method: getDimension()
    @Test
    public void testGetDimension() {
        assertEquals(2, counter.getDimension());
    }

    // Method: getCounts(int index) - Basic test case
    @Test
    public void testGetCounts_Basic() throws OutOfRangeException {
        assertArrayEquals(new int[]{0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[]{0, 2}, counter.getCounts(2));
        assertArrayEquals(new int[]{1, 0}, counter.getCounts(3));
        assertArrayEquals(new int[]{1, 1}, counter.getCounts(4));
        assertArrayEquals(new int[]{1, 2}, counter.getCounts(5));
    }

    // Method: getCounts(int index) - Test for index out of range
    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_OutOfRange() throws OutOfRangeException {
        counter.getCounts(-1);
    }

    // Method: getCount(int... c) - Basic test case
    @Test
    public void testGetCount_Basic() throws OutOfRangeException, DimensionMismatchException {
        assertEquals(0, counter.getCount(0, 0));
        assertEquals(1, counter.getCount(0, 1));
        assertEquals(2, counter.getCount(0, 2));
        assertEquals(3, counter.getCount(1, 0));
        assertEquals(4, counter.getCount(1, 1));
        assertEquals(5, counter.getCount(1, 2));
    }

    // Method: getCount(int... c) - Test for dimension mismatch
    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_DimensionMismatch() throws OutOfRangeException, DimensionMismatchException {
        counter.getCount(0);
    }

    // Method: getCount(int... c) - Test for element c[i] out of range
    @Test(expected = OutOfRangeException.class)
    public void testGetCount_OutOfRange() throws OutOfRangeException, DimensionMismatchException {
        counter.getCount(0, -1);
    }

    // Method: getSize() - Basic test case
    @Test
    public void testGetSize_Basic() {
        assertEquals(6, counter.getSize());
    }

    // Method: getSizes() - Basic test case
    @Test
    public void testGetSizes_Basic() {
        assertArrayEquals(new int[]{2, 3}, counter.getSizes());
    }

    // Method: toString() - Basic test case
    @Test
    public void testToString_Basic() {
        assertEquals("[0][0]", counter.toString());
    }

    // Method: iterator() - Basic test case
    @Test
    public void testIterator_Basic() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        assertEquals(6, count);
    }

    // Method: iterator() - Test for remove() method
    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_Remove() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }

    // 

    // ###TEST START##

    /**
     * Edge cases and exception handling scenarios to be tested:
     * 1. Creating a MultidimensionalCounter object with non-positive size
     * 2. getCounts(int index) with index out of range
     * 3. getCount(int... c) with dimension mismatch
     * 4. getCount(int... c) with element c[i] out of range
     * 5. iterator() with remove() method
     */

    // Method: MultidimensionalCounter(int... size) - Test for non-positive size
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NonPositiveSize() throws NotStrictlyPositiveException {
        new MultidimensionalCounter(-1, 3);
    }

    // Method: getCounts(int index) - Test for index out of range
    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_OutOfRangeUpper() throws OutOfRangeException {
        counter.getCounts(6);
    }

    // Method: getCounts(int index) - Test for index out of range
    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_OutOfRangeLower() throws OutOfRangeException {
        counter.getCounts(-1);
    }

    // Method: getCount(int... c) - Test for dimension mismatch
    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_DimensionMismatchUpper() throws OutOfRangeException, DimensionMismatchException {
        counter.getCount(0, 0, 0);
    }

    // Method: getCount(int... c) - Test for dimension mismatch
    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_DimensionMismatchLower() throws OutOfRangeException, DimensionMismatchException {
        counter.getCount(0);
    }

    // Method: getCount(int... c) - Test for element c[i] out of range
    @Test(expected = OutOfRangeException.class)
    public void testGetCount_OutOfRangeUpper() throws OutOfRangeException, DimensionMismatchException {
        counter.getCount(0, 4);
    }

    // Method: getCount(int... c) - Test for element c[i] out of range
    @Test(expected = OutOfRangeException.class)
    public void testGetCount_OutOfRangeLower() throws OutOfRangeException, DimensionMismatchException {
        counter.getCount(0, -1);
    }

    // Method: iterator() - Test for remove() method
    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_RemoveUnsupported() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }

    // ###TEST END##

    // 
}