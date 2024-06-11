import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.*;
import java.util.*;

public class MultidimensionalCounterTest {
    
    private MultidimensionalCounter mc;
    
    @Before
    public void setUp() throws Exception {
        mc = new MultidimensionalCounter(3, 4, 2);
    }
    
    @Test
    public void testGetDimension() {
        assertEquals(3, mc.getDimension());
    }
    
    @Test
    public void testGetSizes() {
        int[] sizes = mc.getSizes();
        assertEquals(3, sizes.length);
        assertEquals(3, sizes[0]);
        assertEquals(4, sizes[1]);
        assertEquals(2, sizes[2]);
    }
    
    @Test
    public void testGetSize() {
        assertEquals(24, mc.getSize());
    }
    
    @Test
    public void testGetCounts() {
        int[] counts = mc.getCounts(0);
        assertEquals(3, counts.length);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(0, counts[2]);
        
        counts = mc.getCounts(23);
        assertEquals(3, counts.length);
        assertEquals(2, counts[0]);
        assertEquals(3, counts[1]);
        assertEquals(1, counts[2]);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountsInvalidIndex() {
        mc.getCounts(30);
    }
    
    @Test
    public void testGetCount() {
        int count = mc.getCount(2, 3, 1);
        assertEquals(23, count);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountInvalidValue() {
        mc.getCount(5, 3, 1);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountInvalidDimension() {
        mc.getCount(2, 3);
    }
    
    @Test
    public void testIterator() {
        Iterator<Integer> iterator = mc.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(0), iterator.next());
        assertEquals(Integer.valueOf(1), iterator.next());
        assertEquals(Integer.valueOf(2), iterator.next());
        // Skipped some values to simplify the test
        assertEquals(Integer.valueOf(21), iterator.next());
        assertEquals(Integer.valueOf(22), iterator.next());
        assertEquals(Integer.valueOf(23), iterator.next());
        assertFalse(iterator.hasNext());
    }
}