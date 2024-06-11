import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        // Typical case
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(24, counter.getSize());
        
        // Edge case: Single dimension counter
        MultidimensionalCounter singleCounter = new MultidimensionalCounter(5);
        assertEquals(5, singleCounter.getSize());
        
        // Error scenario: Negative size
        try {
            MultidimensionalCounter negativeCounter = new MultidimensionalCounter(-2, 3, 4);
            fail("Expected NotStrictlyPositiveException was not thrown");
        } catch (NotStrictlyPositiveException e) {
            // Exception expected
        }
    }

    @Test
    public void testCountsToIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        // Typical case
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(23, counter.getCount(1, 2, 3));
        
        // Edge case: Minimum values
        assertEquals(0, counter.getCount(0, 0, 0));

        // Edge case: Maximum values
        assertEquals(23, counter.getCount(1, 2, 3));
        
        // Error scenario: Out of range index
        try {
            counter.getCount(2, 3, 4);
            fail("Expected OutOfRangeException was not thrown");
        } catch (OutOfRangeException e) {
            // Exception expected
        }

        // Error scenario: Dimension mismatch
        try {
            counter.getCount(0, 0);
            fail("Expected DimensionMismatchException was not thrown");
        } catch (DimensionMismatchException e) {
            // Exception expected
        }
    }

    @Test
    public void testIndexToCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        // Typical case
        assertArrayEquals(new int[]{1, 2, 3}, counter.getCounts(23));

        // Edge case: Minimum index
        assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));

        // Edge case: Maximum index
        assertArrayEquals(new int[]{1, 2, 3}, counter.getCounts(23));

        // Error scenario: Out of range index
        try {
            counter.getCounts(30);
            fail("Expected OutOfRangeException was not thrown");
        } catch (OutOfRangeException e) {
            // Exception expected
        }
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        // Typical case
        assertTrue(iterator.hasNext());
        assertEquals(0, (int) iterator.next());
        assertEquals(1, iterator.getCount());
        assertArrayEquals(new int[]{0, 0, 1}, iterator.getCounts());

        // Edge case: Last element
        while (iterator.hasNext()) {
            iterator.next();
        }
        assertFalse(iterator.hasNext());

        // Error scenario: Remove operation not supported
        try {
            iterator.remove();
            fail("Expected UnsupportedOperationException was not thrown");
        } catch (UnsupportedOperationException e) {
            // Exception expected
        }
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals("[0][0][0]", counter.toString());
    }
}