import org.junit.Test;
import org.apache.commons.math3.exception.*;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {
    
    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(5, 10, 15);
        assertEquals(3, counter.getDimension());
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidSize() {
        new MultidimensionalCounter(-5, 0, 10);
    }
    
    @Test
    public void testGetCountsWithValidIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4);
        int[] expected = {1, 2};
        assertArrayEquals(expected, counter.getCounts(6));
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountsWithInvalidIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4);
        counter.getCounts(10);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWithInvalidLength() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.getCount(1, 2, 3);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithInvalidIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.getCount(1, 4);
    }
    
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4);
        assertEquals(6, counter.getCount(1, 2));
    }
    
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5, 6);
        assertEquals(360, counter.getSize());
    }
    
    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        int[] expected = {3, 4, 5};
        assertArrayEquals(expected, counter.getSizes());
    }
    
    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals("[0][1][2]", counter.toString());
    }
    
    @Test
    public void testIteratorHasNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
    }
    
    @Test
    public void testIteratorNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        int[] expectedFirst = {0, 0};
        assertArrayEquals(expectedFirst, iterator.getCounts());
        assertEquals(0, (int) iterator.next());
        
        int[] expectedSecond = {0, 1};
        assertArrayEquals(expectedSecond, iterator.getCounts());
        assertEquals(1, (int) iterator.next());
        
        int[] expectedLast = {1, 2};
        assertArrayEquals(expectedLast, iterator.getCounts());
        assertEquals(2, (int) iterator.next());
        
        assertFalse(iterator.hasNext());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }
}