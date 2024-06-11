import org.apache.commons.math3.exception.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor_positiveDimension() {
        try{
            MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
            assertNotNull(counter);
        }catch(Exception e){
            fail("Exception thrown");
        }
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_negativeDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(-2, 3, 4);
    }
    
    @Test
    public void testIterator_hasNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testIterator_next() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(Integer.valueOf(0), iterator.next());
        assertEquals(Integer.valueOf(1), iterator.next());
        assertEquals(Integer.valueOf(2), iterator.next());
        assertEquals(Integer.valueOf(3), iterator.next());
        assertEquals(Integer.valueOf(4), iterator.next());
        assertEquals(Integer.valueOf(5), iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testIterator_getCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(0, iterator.getCount());
        iterator.next();
        assertEquals(1, iterator.getCount());
    }
    
    @Test
    public void testIterator_getCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertArrayEquals(new int[]{0, 0}, iterator.getCounts());
        iterator.next();
        assertArrayEquals(new int[]{0, 1}, iterator.getCounts());
    }
    
    @Test
    public void testIterator_getCountByDim() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(0, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        iterator.next();
        assertEquals(0, iterator.getCount(0));
        assertEquals(1, iterator.getCount(1));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_remove() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }
    
    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter.getDimension());
    }
    
    @Test
    public void testGetCounts_inRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        try {
            assertArrayEquals(new int[]{0, 0}, counter.getCounts(0));
            assertArrayEquals(new int[]{0, 1}, counter.getCounts(1));
            assertArrayEquals(new int[]{1, 0}, counter.getCounts(2));
            assertArrayEquals(new int[]{1, 1}, counter.getCounts(3));
            assertArrayEquals(new int[]{0, 2}, counter.getCounts(4));
            assertArrayEquals(new int[]{1, 2}, counter.getCounts(5));
        } catch (OutOfRangeException e) {
            fail("Exception thrown");
        }
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_outOfRange() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.getCounts(6);
    }
    
    @Test
    public void testGetCount_inRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        try {
            assertEquals(0, counter.getCount(0, 0));
            assertEquals(1, counter.getCount(0, 1));
            assertEquals(2, counter.getCount(1, 0));
            assertEquals(3, counter.getCount(1, 1));
            assertEquals(4, counter.getCount(0, 2));
            assertEquals(5, counter.getCount(1, 2));
        } catch (OutOfRangeException | DimensionMismatchException e) {
            fail("Exception thrown");
        }
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCount_outOfRange() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.getCount(0, 3);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_dimensionMismatch() throws DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.getCount(0);
    }
    
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertEquals(6, counter.getSize());
    }
    
    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertArrayEquals(new int[]{2, 3}, counter.getSizes());
    }
    
    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertEquals("[0][0]", counter.toString());
    }
}