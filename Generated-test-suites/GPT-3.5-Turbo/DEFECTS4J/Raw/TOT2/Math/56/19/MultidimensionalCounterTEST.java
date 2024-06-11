import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstructor() {
        // Typical use case
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertNotNull(counter);
        
        // Edge case: Negative size
        try {
            MultidimensionalCounter counter2 = new MultidimensionalCounter(-2, 3, 4);
            fail("Constructor should throw NotStrictlyPositiveException for negative size");
        } catch (NotStrictlyPositiveException e) {
            // Pass
        }
    }
    
    @Test
    public void testMultidimensionalCounterIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        // Typical use case
        assertTrue(iterator.hasNext());
        
        // Iterate and check values
        assertEquals(0, (int) iterator.next());
        assertEquals(1, (int) iterator.next());
        assertEquals(2, (int) iterator.next());
        assertEquals(3, (int) iterator.next());
        assertEquals(4, (int) iterator.next());
        assertEquals(5, (int) iterator.next());
    }
    
    @Test
    public void testMultidimensionalCounterGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        
        // Typical use case
        int[] counts = counter.getCounts(3);
        assertArrayEquals(new int[]{1, 1}, counts);
        
        // Edge case: OutOfRangeException
        try {
            counter.getCounts(5);
            fail("getCounts should throw OutOfRangeException for index out of range");
        } catch (OutOfRangeException e) {
            // Pass
        }
    }
    
    @Test
    public void testMultidimensionalCounterGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        
        // Typical use case
        int count = counter.getCount(1, 1);
        assertEquals(3, count);
        
        // Edge case: DimensionMismatchException
        try {
            counter.getCount(1);
            fail("getCount should throw DimensionMismatchException for incorrect dimension size");
        } catch (DimensionMismatchException e) {
            // Pass
        }
        
        // Edge case: OutOfRangeException
        try {
            counter.getCount(2, 2);
            fail("getCount should throw OutOfRangeException for index out of range");
        } catch (OutOfRangeException e) {
            // Pass
        }
    }
    
    @Test
    public void testMultidimensionalCounterGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 3);
        
        // Typical use case
        assertEquals(12, counter.getSize());
    }
}