import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.*;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        // Typical use case
        int[] sizes1 = {2, 3, 4};
        MultidimensionalCounter counter1 = new MultidimensionalCounter(sizes1);
        assertEquals(2, counter1.getDimension());
        
        // Edge cases
        int[] sizes2 = {0, 0};
        try {
            MultidimensionalCounter counter2 = new MultidimensionalCounter(sizes2);
            fail("Expected NotStrictlyPositiveException to be thrown");
        } catch (NotStrictlyPositiveException e) {
            // Exception expected
        }
        
        int[] sizes3 = {1, Integer.MAX_VALUE};
        MultidimensionalCounter counter3 = new MultidimensionalCounter(sizes3);
        assertEquals(Integer.MAX_VALUE, counter3.getDimension());
    }

    @Test
    public void testGetCounts() {
        // Typical use case
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] counts1 = counter.getCounts(0);
        assertArrayEquals(new int[] {0, 0}, counts1);

        // Edge cases
        try {
            int[] counts2 = counter.getCounts(3);
            fail("Expected OutOfRangeException to be thrown");
        } catch (OutOfRangeException e) {
            // Exception expected
        }
        
        int[] counts3 = counter.getCounts(5);
        assertArrayEquals(new int[] {1, 2}, counts3);
    }

    @Test
    public void testGetCount() {
        // Typical use case
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int count1 = counter.getCount(1, 2);
        assertEquals(5, count1);
        
        // Edge cases
        try {
            int count2 = counter.getCount(-1, 0);
            fail("Expected OutOfRangeException to be thrown");
        } catch (OutOfRangeException e) {
            // Exception expected
        }
        
        try {
            int count3 = counter.getCount(2, 2);
            fail("Expected OutOfRangeException to be thrown");
        } catch (OutOfRangeException e) {
            // Exception expected
        }

        try {
            int count4 = counter.getCount(0, 0, 0);
            fail("Expected DimensionMismatchException to be thrown");
        } catch (DimensionMismatchException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testGetSize() {
        // Typical use case
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(24, counter.getSize());
        
        // Edge cases
        int[] sizes2 = {0};
        try {
            MultidimensionalCounter counter2 = new MultidimensionalCounter(sizes2);
            fail("Expected NotStrictlyPositiveException to be thrown");
        } catch (NotStrictlyPositiveException e) {
            // Exception expected
        }
        
        int[] sizes3 = {1, Integer.MAX_VALUE};
        MultidimensionalCounter counter3 = new MultidimensionalCounter(sizes3);
        assertEquals(Integer.MAX_VALUE, counter3.getSize());
    }

    @Test
    public void testGetSizes() {
        // Typical use case
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertArrayEquals(new int[] {2, 3, 4}, counter.getSizes());
    }

    @Test
    public void testIterator() {
        // Typical use case
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next().intValue());
        assertEquals(1, iterator.next().intValue());
        assertEquals(2, iterator.next().intValue());
        assertEquals(3, iterator.next().intValue());
        assertEquals(4, iterator.next().intValue());
        assertEquals(5, iterator.next().intValue());
        assertFalse(iterator.hasNext());
    }

}