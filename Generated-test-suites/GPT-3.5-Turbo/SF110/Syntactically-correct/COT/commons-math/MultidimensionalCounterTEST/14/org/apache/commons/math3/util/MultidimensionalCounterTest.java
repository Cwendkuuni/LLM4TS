import org.apache.commons.math3.exception.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {
    @Test
    public void testConstructor() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        assertEquals(3, counter.getDimension());
        assertArrayEquals(size, counter.getSizes());
        assertEquals(24, counter.getSize());
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidSize() {
        int[] size = {0, 1, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
    }
    
    @Test
    public void testGetCounts() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{1, 0, 0}, counter.getCounts(1));
        assertArrayEquals(new int[]{0, 1, 0}, counter.getCounts(6));
        assertArrayEquals(new int[]{1, 2, 3}, counter.getCounts(23));
        
        try {
            counter.getCounts(-1);
            fail("Should throw an OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Exception expected
        }
        
        try {
            counter.getCounts(24);
            fail("Should throw an OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Exception expected
        }
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        counter.getCount(0, 1);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        counter.getCount(0, 5, 3);
    }
    
    @Test
    public void testGetCount() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(5, counter.getCount(1, 0, 1));
        assertEquals(17, counter.getCount(1, 2, 1));
        
    }
    
    @Test
    public void testIterator() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            assertTrue(iterator.next() == count + 1);
            assertArrayEquals(iterator.getCounts(), counter.getCounts(count));
            assertTrue(iterator.getCount() == count);
            count++;
        }
        
        assertEquals(counter.getSize(), count);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        iterator.remove();
    }
    
}