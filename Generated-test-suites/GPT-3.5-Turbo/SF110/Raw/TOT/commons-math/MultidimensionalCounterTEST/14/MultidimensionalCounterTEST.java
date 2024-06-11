import static org.junit.Assert.*;
import org.junit.Test;


public class MultidimensionalCounterTest {
    
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iter = counter.iterator();
        
        assertTrue(iter.hasNext());
        assertEquals(Integer.valueOf(0), iter.next());
        assertEquals(0, iter.getCounts()[0]);
        assertEquals(0, iter.getCounts()[1]);
        assertEquals(0, iter.getCount());
        
        assertTrue(iter.hasNext());
        assertEquals(Integer.valueOf(1), iter.next());
        assertEquals(1, iter.getCounts()[0]);
        assertEquals(0, iter.getCounts()[1]);
        assertEquals(1, iter.getCount());
        
        assertTrue(iter.hasNext());
        assertEquals(Integer.valueOf(2), iter.next());
        assertEquals(0, iter.getCounts()[0]);
        assertEquals(1, iter.getCounts()[1]);
        assertEquals(2, iter.getCount());
        
        assertTrue(iter.hasNext());
        assertEquals(Integer.valueOf(3), iter.next());
        assertEquals(1, iter.getCounts()[0]);
        assertEquals(1, iter.getCounts()[1]);
        assertEquals(3, iter.getCount());
        
        assertFalse(iter.hasNext());
    }
    
    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter.getDimension());
    }
    
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        
        int[] counts = counter.getCounts(0);
        assertArrayEquals(new int[]{0, 0}, counts);
        
        counts = counter.getCounts(1);
        assertArrayEquals(new int[]{1, 0}, counts);
        
        counts = counter.getCounts(2);
        assertArrayEquals(new int[]{0, 1}, counts);
        
        counts = counter.getCounts(3);
        assertArrayEquals(new int[]{1, 1}, counts);
        
        try {
            counts = counter.getCounts(-1);
            fail("Exception not thrown");
        } catch (OutOfRangeException e) {
            // success
        }
        
        try {
            counts = counter.getCounts(4);
            fail("Exception not thrown");
        } catch (OutOfRangeException e) {
            // success
        }
    }
    
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        
        int count = counter.getCount(0, 0);
        assertEquals(0, count);
        
        count = counter.getCount(1, 0);
        assertEquals(1, count);
        
        count = counter.getCount(0, 1);
        assertEquals(2, count);
        
        count = counter.getCount(1, 1);
        assertEquals(3, count);
        
        try {
            count = counter.getCount(2, 0);
            fail("Exception not thrown");
        } catch (OutOfRangeException e) {
            // success
        }
        
        try {
            count = counter.getCount(1, 3);
            fail("Exception not thrown");
        } catch (OutOfRangeException e) {
            // success
        }
        
        try {
            count = counter.getCount(1);
            fail("Exception not thrown");
        } catch (DimensionMismatchException e) {
            // success
        }
    }
    
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(24, counter.getSize());
    }
    
    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int[] sizes = counter.getSizes();
        assertArrayEquals(new int[]{2, 3}, sizes);
    }
    
    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        String str = counter.toString();
        assertEquals("[0][0][0]", str);
    }
    
}