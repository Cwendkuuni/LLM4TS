import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterInitialization() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        assertEquals(3, counter.getDimension());
        assertArrayEquals(new int[] {2, 3, 4}, counter.getSizes());
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testMultidimensionalCounterIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(0, (int) iterator.next());
        assertEquals(1, (int) iterator.next());
        assertEquals(2, (int) iterator.next());
        assertEquals(3, (int) iterator.next());
        
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMultidimensionalCounterConversion() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        
        assertArrayEquals(new int[] {0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[] {0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[] {1, 0}, counter.getCounts(2));
        assertArrayEquals(new int[] {1, 1}, counter.getCounts(3));
        
        assertEquals(0, counter.getCount(0, 0));
        assertEquals(1, counter.getCount(0, 1));
        assertEquals(2, counter.getCount(1, 0));
        assertEquals(3, counter.getCount(1, 1));
        
        try {
            counter.getCount(0, 2);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterInitializationInvalidSize() {
        new MultidimensionalCounter(2, 0, 4);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultidimensionalCounterInvalidCountArraySize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        counter.getCount(0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterInvalidIndexConversion() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        counter.getCounts(4);
    }
}