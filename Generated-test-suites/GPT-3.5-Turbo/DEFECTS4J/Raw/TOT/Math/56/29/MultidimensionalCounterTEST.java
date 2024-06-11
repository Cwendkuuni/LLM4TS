import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Before
    public void setUp() {
        counter = new MultidimensionalCounter(2, 3, 4);
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSize() {
        assertEquals(24, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroSizeThrowsException() {
        MultidimensionalCounter invalidCounter = new MultidimensionalCounter(0, 1, 2);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeSizeThrowsException() {
        MultidimensionalCounter invalidCounter = new MultidimensionalCounter(-1, 1, 2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsIndexOutOfBoundsThrowsException() {
        int[] counts = counter.getCounts(25);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWithInvalidDimensionThrowsException() {
        int count = counter.getCount(1, 2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithOutOfRangeDimensionThrowsException() {
        int count = counter.getCount(0, 4, 2);
    }

    @Test
    public void testGetCounts() {
        int[] counts = counter.getCounts(13);
        assertArrayEquals(new int[]{0, 1, 2}, counts);
    }

    @Test
    public void testGetCount() {
        int count = counter.getCount(0, 2, 3);
        assertEquals(17, count);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(0), iterator.next());
        assertEquals(0, iterator.getCount());

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(1), iterator.next());
        assertEquals(1, iterator.getCount());

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(2), iterator.next());
        assertEquals(2, iterator.getCount());

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(3), iterator.next());
        assertEquals(3, iterator.getCount());

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(4), iterator.next());
        assertEquals(4, iterator.getCount());

        // Skip the remaining iterations...

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithEmptyCounter() {
        MultidimensionalCounter emptyCounter = new MultidimensionalCounter();
        MultidimensionalCounter.Iterator iterator = emptyCounter.iterator();

        assertFalse(iterator.hasNext());
    }

}