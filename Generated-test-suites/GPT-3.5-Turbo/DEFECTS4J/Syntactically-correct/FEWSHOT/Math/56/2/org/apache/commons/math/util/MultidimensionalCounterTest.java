import static org.junit.Assert.*;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;

public class MultidimensionalCounterTest {
    
    @Test
    public void testConstructor() {
        try {
            new MultidimensionalCounter();
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        try {
            new MultidimensionalCounter(2, 0, 3);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
        
        try {
            new MultidimensionalCounter(-1, 2, 3);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
        
        new MultidimensionalCounter(2, 3, 4);
    }
    
    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter.getDimension());
    }
    
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(2 * 3 * 4, counter.getSize());
    }
    
    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] sizes = counter.getSizes();
        assertEquals(3, sizes.length);
        assertEquals(2, sizes[0]);
        assertEquals(3, sizes[1]);
        assertEquals(4, sizes[2]);
    }
    
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        try {
            counter.getCount(2, 2);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // expected
        }
        
        try {
            counter.getCount(0, 2, 4);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // expected
        }
        
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(2, counter.getCount(0, 0, 2));
        assertEquals(3, counter.getCount(0, 1, 0));
        assertEquals(4, counter.getCount(0, 1, 1));
        assertEquals(5, counter.getCount(0, 1, 2));
        assertEquals(6, counter.getCount(1, 0, 0));
        assertEquals(7, counter.getCount(1, 0, 1));
        assertEquals(8, counter.getCount(1, 0, 2));
        assertEquals(9, counter.getCount(1, 1, 0));
        assertEquals(10, counter.getCount(1, 1, 1));
        assertEquals(11, counter.getCount(1, 1, 2));
    }
    
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        
        try {
            counter.getCounts(-1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // expected
        }
        
        try {
            counter.getCounts(24);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // expected
        }
        
        assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{0, 0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[]{0, 0, 2}, counter.getCounts(2));
        assertArrayEquals(new int[]{0, 1, 0}, counter.getCounts(3));
        assertArrayEquals(new int[]{0, 1, 1}, counter.getCounts(4));
        assertArrayEquals(new int[]{0, 1, 2}, counter.getCounts(5));
        assertArrayEquals(new int[]{1, 0, 0}, counter.getCounts(6));
        assertArrayEquals(new int[]{1, 0, 1}, counter.getCounts(7));
        assertArrayEquals(new int[]{1, 0, 2}, counter.getCounts(8));
        assertArrayEquals(new int[]{1, 1, 0}, counter.getCounts(9));
        assertArrayEquals(new int[]{1, 1, 1}, counter.getCounts(10));
        assertArrayEquals(new int[]{1, 1, 2}, counter.getCounts(11));
    }
    
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(0, (int) iterator.next());
        assertArrayEquals(new int[]{0, 0, 0}, iterator.getCounts());
        
        assertTrue(iterator.hasNext());
        assertEquals(1, (int) iterator.next());
        assertArrayEquals(new int[]{0, 0, 1}, iterator.getCounts());
        
        assertTrue(iterator.hasNext());
        assertEquals(2, (int) iterator.next());
        assertArrayEquals(new int[]{0, 0, 2}, iterator.getCounts());
        
        assertTrue(iterator.hasNext());
        assertEquals(3, (int) iterator.next());
        assertArrayEquals(new int[]{0, 1, 0}, iterator.getCounts());
        
        assertTrue(iterator.hasNext());
        assertEquals(4, (int) iterator.next());
        assertArrayEquals(new int[]{0, 1, 1}, iterator.getCounts());
        
        assertTrue(iterator.hasNext());
        assertEquals(5, (int) iterator.next());
        assertArrayEquals(new int[]{0, 1, 2}, iterator.getCounts());
        
        assertTrue(iterator.hasNext());
        assertEquals(6, (int) iterator.next());
        assertArrayEquals(new int[]{1, 0, 0}, iterator.getCounts());
        
        assertTrue(iterator.hasNext());
        assertEquals(7, (int) iterator.next());
        assertArrayEquals(new int[]{1, 0, 1}, iterator.getCounts());
        
        assertTrue(iterator.hasNext());
        assertEquals(8, (int) iterator.next());
        assertArrayEquals(new int[]{1, 0, 2}, iterator.getCounts());
        
        assertTrue(iterator.hasNext());
        assertEquals(9, (int) iterator.next());
        assertArrayEquals(new int[]{1, 1, 0}, iterator.getCounts());
        
        assertTrue(iterator.hasNext());
        assertEquals(10, (int) iterator.next());
        assertArrayEquals(new int[]{1, 1, 1}, iterator.getCounts());
        
        assertTrue(iterator.hasNext());
        assertEquals(11, (int) iterator.next());
        assertArrayEquals(new int[]{1, 1, 2}, iterator.getCounts());
        
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }
}