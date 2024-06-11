import static org.junit.Assert.assertEquals;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(3, counter.getDimension());
        assertEquals(24, counter.getSize());
        assertArrayEquals(new int[] {2, 4, 3}, counter.getSizes());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSize() {
        new MultidimensionalCounter(-2, 4, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroSize() {
        new MultidimensionalCounter(0, 4, 3);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(-1, iterator.getCount());

        assertTrue(iterator.hasNext());
        assertEquals(0, (int) iterator.next());
        assertEquals(0, iterator.getCount());
        assertArrayEquals(new int[] {0, 0, 0}, iterator.getCounts());

        assertTrue(iterator.hasNext());
        assertEquals(1, (int) iterator.next());
        assertEquals(1, iterator.getCount());
        assertArrayEquals(new int[] {0, 0, 1}, iterator.getCounts());

        // ... (more tests to cover all iterations)
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        
        assertArrayEquals(new int[] {0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[] {0, 0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[] {0, 0, 2}, counter.getCounts(2));
        assertArrayEquals(new int[] {0, 1, 0}, counter.getCounts(3));
        // ... (more tests to cover all indices)
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsNegativeIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCounts(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOverflowIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCounts(24);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(2, counter.getCount(0, 0, 2));
        assertEquals(3, counter.getCount(0, 1, 0));
        // ... (more tests to cover all indices)
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(0, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOverflowIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(2, 3, 4);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountNegativeDimensionIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(0, 0, -1);
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals("[0][0][0]", counter.toString());
        // ... (more tests to cover different multidimensional counts)
    }
}