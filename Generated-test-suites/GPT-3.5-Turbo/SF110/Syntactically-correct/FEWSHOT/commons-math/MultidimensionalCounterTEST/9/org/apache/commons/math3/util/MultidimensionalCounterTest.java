import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.*;
import java.util.*;

public class MultidimensionalCounterTest {
    @Test
    public void testConstructor() throws NotStrictlyPositiveException {
        try {
            MultidimensionalCounter counter1 = new MultidimensionalCounter(-1, 2, 3);
            fail("Exception not thrown for negative size");
        } catch (NotStrictlyPositiveException e) {
            // exception expected
        }
        
        MultidimensionalCounter counter2 = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter2.getDimension());
        assertEquals(2, counter2.getCounts(0).length);
        assertEquals(2, counter2.getCounts(counter2.getSize() - 1).length);
    }
    
    @Test
    public void testIterator() {
        MultidimensionalCounter counter1 = new MultidimensionalCounter(2, 2);
        Iterator<Integer> iterator1 = counter1.iterator();
        assertTrue(iterator1.hasNext());
        assertEquals(Integer.valueOf(0), iterator1.next());
        assertEquals(Integer.valueOf(1), iterator1.next());
        assertEquals(Integer.valueOf(2), iterator1.next());
        assertEquals(Integer.valueOf(3), iterator1.next());
        assertFalse(iterator1.hasNext());
        
        MultidimensionalCounter counter2 = new MultidimensionalCounter(3, 3, 3);
        Iterator<Integer> iterator2 = counter2.iterator();
        assertEquals(Integer.valueOf(0), iterator2.next());
        assertEquals(Integer.valueOf(1), iterator2.next());
        // ...
        assertEquals(Integer.valueOf(26), iterator2.next());
        assertFalse(iterator2.hasNext());
    }
    
    @Test
    public void testGetCounts() throws OutOfRangeException {
        MultidimensionalCounter counter1 = new MultidimensionalCounter(2, 2);
        
        assertArrayEquals(new int[]{0, 0}, counter1.getCounts(0));
        assertArrayEquals(new int[]{1, 0}, counter1.getCounts(1));
        assertArrayEquals(new int[]{0, 1}, counter1.getCounts(2));
        assertArrayEquals(new int[]{1, 1}, counter1.getCounts(3));
        
        MultidimensionalCounter counter2 = new MultidimensionalCounter(3, 3, 3);
        
        assertArrayEquals(new int[]{0, 0, 0}, counter2.getCounts(0));
        assertArrayEquals(new int[]{1, 0, 0}, counter2.getCounts(1));
        // ...
        assertArrayEquals(new int[]{2, 2, 2}, counter2.getCounts(counter2.getSize() - 1));
    }
    
    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter1 = new MultidimensionalCounter(2, 2);
        
        assertEquals(0, counter1.getCount(0, 0));
        assertEquals(1, counter1.getCount(1, 0));
        assertEquals(2, counter1.getCount(0, 1));
        assertEquals(3, counter1.getCount(1, 1));
        
        MultidimensionalCounter counter2 = new MultidimensionalCounter(3, 3, 3);
        
        assertEquals(0, counter2.getCount(0, 0, 0));
        assertEquals(1, counter2.getCount(1, 0, 0));
        // ...
        assertEquals(26, counter2.getCount(2, 2, 2));
    }
    
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter1 = new MultidimensionalCounter(2, 2);
        assertEquals(4, counter1.getSize());
        
        MultidimensionalCounter counter2 = new MultidimensionalCounter(3, 3, 3);
        assertEquals(27, counter2.getSize());
    }
    
    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter1 = new MultidimensionalCounter(2, 3, 4);
        assertArrayEquals(new int[]{2, 3, 4}, counter1.getSizes());
        
        MultidimensionalCounter counter2 = new MultidimensionalCounter(3, 5);
        assertArrayEquals(new int[]{3, 5}, counter2.getSizes());
    }
    
    @Test
    public void testToString() {
        MultidimensionalCounter counter1 = new MultidimensionalCounter(2, 2);
        assertEquals("[0][0]", counter1.toString());
        
        MultidimensionalCounter counter2 = new MultidimensionalCounter(3, 3, 3);
        assertEquals("[0][0][0]", counter2.toString());
    }
}