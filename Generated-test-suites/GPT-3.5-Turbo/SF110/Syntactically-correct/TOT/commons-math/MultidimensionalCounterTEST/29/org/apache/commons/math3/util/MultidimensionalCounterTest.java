import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.*;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Before
    public void setUp() throws Exception {
        counter = new MultidimensionalCounter(2, 3, 4);
    }

    @Test
    public void testConstructor_validSize() {
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception thrown for valid size");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_invalidSize() throws Exception {
        MultidimensionalCounter counter = new MultidimensionalCounter(-2, 3, 4);
    }

    @Test
    public void testGetDimension() {
        int dimension = counter.getDimension();
        assertEquals(3, dimension);
    }

    @Test
    public void testGetCounts_validIndex() {
        try {
            int[] counts = counter.getCounts(15);
            int[] expectedCounts = {1, 0, 3};
            assertArrayEquals(expectedCounts, counts);
        } catch (Exception e) {
            fail("Exception thrown for valid index");
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_invalidIndex() throws Exception {
        int[] counts = counter.getCounts(30);
    }

    @Test
    public void testGetCount_validCounts() {
        try {
            int count = counter.getCount(1, 0, 3);
            assertEquals(15, count);
        } catch (Exception e) {
            fail("Exception thrown for valid counts");
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_invalidCounts() throws Exception {
        int count = counter.getCount(1, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_invalidIndexInCounts() throws Exception {
        int count = counter.getCount(1, 0, 4);
    }

    @Test
    public void testGetSize() {
        int size = counter.getSize();
        assertEquals(24, size);
    }

    @Test
    public void testGetSizes() {
        int[] sizes = counter.getSizes();
        int[] expectedSizes = {2, 3, 4};
        assertArrayEquals(expectedSizes, sizes);
    }

    @Test
    public void testToString() {
        String result = counter.toString();
        String expected = "[0][0][0]";
        assertEquals(expected, result);
    }

    @Test
    public void testIterator_hasNext() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testIterator_next_withHasNext() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int[] expectedCounts = {0, 0, 0};
        assertEquals(0, (int) iterator.next());
        assertArrayEquals(expectedCounts, iterator.getCounts());
    }

    @Test
    public void testIterator_next_withoutHasNext() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        for (int i = 0; i < counter.getSize() - 1; i++) {
            iterator.next();
        }
        int[] expectedCounts = {1, 2, 3};
        assertEquals(counter.getSize() - 1, (int) iterator.next());
        assertArrayEquals(expectedCounts, iterator.getCounts());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_remove() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }

}