import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;

public class MultidimensionalCounterTest {
    
    @Test
    public void testConstructor() {
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(-1, 2, 3);
            fail("NotStrictlyPositiveException not thrown");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }
    
    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(3, counter.getDimension());
    }
    
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(2 * 4 * 3, counter.getSize());
    }
    
    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] expectedSizes = {2, 4, 3};
        assertArrayEquals(expectedSizes, counter.getSizes());
    }
    
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int index = 23;
        int[] expectedCounts = {1, 3, 2};
        assertArrayEquals(expectedCounts, counter.getCounts(index));
        
        try {
            counter.getCounts(-1);
            fail("OutOfRangeException not thrown");
        } catch (OutOfRangeException e) {
            // expected
        }
        
        try {
            counter.getCounts(2 * 4 * 3);
            fail("OutOfRangeException not thrown");
        } catch (OutOfRangeException e) {
            // expected
        }
    }
    
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] counts = {1, 3, 2};
        int expectedCount = 23;
        assertEquals(expectedCount, counter.getCount(counts));
        
        try {
            counter.getCount(1, 5, 2);
            fail("OutOfRangeException not thrown");
        } catch (OutOfRangeException e) {
            // expected
        }
        
        try {
            counter.getCount(1, 3, 3);
            fail("OutOfRangeException not thrown");
        } catch (OutOfRangeException e) {
            // expected
        }
        
        try {
            counter.getCount(1, 3);
            fail("DimensionMismatchException not thrown");
        } catch (DimensionMismatchException e) {
            // expected
        }
    }
    
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        assertEquals(-1, iterator.getCount());
        assertArrayEquals(new int[] {0, 0, 0}, iterator.getCounts());
        assertEquals(0, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        assertEquals(0, iterator.getCount(2));
        
        int count = 0;
        while (iterator.hasNext()) {
            int[] counts = iterator.getCounts();
            assertEquals(count, iterator.getCount());
            assertEquals(count, counter.getCount(counts));
            assertEquals(counts[0], iterator.getCount(0));
            assertEquals(counts[1], iterator.getCount(1));
            assertEquals(counts[2], iterator.getCount(2));
            count++;
            iterator.next();
        }
        
        assertEquals(7, iterator.getCount());
        assertArrayEquals(new int[] {1, 1, 1}, iterator.getCounts());
        assertEquals(1, iterator.getCount(0));
        assertEquals(1, iterator.getCount(1));
        assertEquals(1, iterator.getCount(2));
        
        try {
            iterator.remove();
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

}