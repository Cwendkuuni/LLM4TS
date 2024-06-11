import org.junit.Test;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    // Test for the constructor
    @Test
    public void testConstructor() {
        // Typical use case
        MultidimensionalCounter counter1 = new MultidimensionalCounter(2, 4, 3);
        assertEquals(3, counter1.getDimension());
        
        // Edge case: Size of 1 dimension is zero
        try {
            MultidimensionalCounter counter2 = new MultidimensionalCounter(2, 0, 3);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Exception expected
        }
        
        // Edge case: Size of 1 dimension is negative
        try {
            MultidimensionalCounter counter3 = new MultidimensionalCounter(2, -4, 3);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Exception expected
        }
        
        // Edge case: Size of all dimensions is negative
        try {
            MultidimensionalCounter counter4 = new MultidimensionalCounter(-2, -4, -3);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Exception expected
        }
    }
    
    // Test for iterator()
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        // Typical use case
        assertEquals(true, iterator.hasNext());
        assertEquals(0, (int) iterator.next());
        assertEquals(true, iterator.hasNext());
        
        int count = 1;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        assertEquals(24, count);
        
        // Test for remove()
        try {
            iterator.remove();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Exception expected
        }
    }
    
    // Test for getCounts(int index)
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        
        // Typical use case
        assertArrayEquals(new int[] {0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[] {0, 0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[] {1, 2, 2}, counter.getCounts(23));
        
        // Edge case: index is negative
        try {
            counter.getCounts(-1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Exception expected
        }
        
        // Edge case: index is greater than totalSize
        try {
            counter.getCounts(24);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Exception expected
        }
    }
    
    // Test for getCount(int ... c)
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        
        // Typical use case
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(23, counter.getCount(1, 3, 2));
        
        // Edge case: Value in one dimension is negative
        try {
            counter.getCount(0, -1, 0);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Exception expected
        }
        
        // Edge case: Value in one dimension is greater than size
        try {
            counter.getCount(3, 2, 1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Exception expected
        }
        
        // Edge case: Size of c does not match size of array given in the constructor
        try {
            counter.getCount(0, 0);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Exception expected
        }
    }
    
    // Test for getSize()
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        
        // Typical use case
        assertEquals(24, counter.getSize());
    }
    
    // Test for getSizes()
    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        
        // Typical use case
        assertArrayEquals(new int[] {2, 4, 3}, counter.getSizes());
    }
    
    // Test for toString()
    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        
        // Typical use case
        assertEquals("[0][0][0]", counter.toString());
    }
}