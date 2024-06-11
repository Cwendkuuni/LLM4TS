import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Before
    public void setUp() {
        counter = new MultidimensionalCounter(3, 4, 2);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        // Verify that hasNext() returns true for the first element
        assertTrue(iterator.hasNext());
        
        // Verify that next() returns 0 for the first element
        assertEquals(0, iterator.next().intValue());
        
        // Verify that getCount() returns 0 for the first element
        assertEquals(0, iterator.getCount());
        
        // Verify that getCounts() returns [0, 0, 0] for the first element
        assertArrayEquals(new int[]{0, 0, 0}, iterator.getCounts());
        
        // Verify that getCount(0) returns 0 for the first element
        assertEquals(0, iterator.getCount(0));
        
        // Verify that getCount(1) returns 0 for the first element
        assertEquals(0, iterator.getCount(1));
        
        // Verify that getCount(2) returns 0 for the first element
        assertEquals(0, iterator.getCount(2));
        
        // Verify that hasNext() returns true for the second element
        assertTrue(iterator.hasNext());
        
        // Verify that next() returns 1 for the second element
        assertEquals(1, iterator.next().intValue());
        
        // Verify that getCount() returns 1 for the second element
        assertEquals(1, iterator.getCount());
        
        // Verify that getCounts() returns [0, 0, 1] for the second element
        assertArrayEquals(new int[]{0, 0, 1}, iterator.getCounts());
        
        // Verify that getCount(0) returns 0 for the second element
        assertEquals(0, iterator.getCount(0));
        
        // Verify that getCount(1) returns 0 for the second element
        assertEquals(0, iterator.getCount(1));
        
        // Verify that getCount(2) returns 1 for the second element
        assertEquals(1, iterator.getCount(2));
        
        //....
        // Continue verifying other elements
        
        // Verify that hasNext() returns false after iterating over all elements
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetCounts() {
        // Verify that getCounts(0) returns [0, 0, 0] for index 0
        assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        
        // Verify that getCounts(4) returns [0, 1, 0] for index 4
        assertArrayEquals(new int[]{0, 1, 0}, counter.getCounts(4));
        
        // Verify that getCounts(23) returns [1, 3, 2] for index 23
        assertArrayEquals(new int[]{1, 3, 2}, counter.getCounts(23));
        
        //....
        // Continue verifying other indices
    }

    @Test
    public void testGetCount() {
        // Verify that getCount(0, 0, 0) returns 0 for indices (0, 0, 0)
        assertEquals(0, counter.getCount(0, 0, 0));
        
        // Verify that getCount(0, 0, 1) returns 1 for indices (0, 0, 1)
        assertEquals(1, counter.getCount(0, 0, 1));
        
        // Verify that getCount(1, 3, 2) returns 23 for indices (1, 3, 2)
        assertEquals(23, counter.getCount(1, 3, 2));
        
        //....
        // Continue verifying other indices
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsIndexOutOfRange() {
        // Verify that getCounts(-1) throws OutOfRangeException
        counter.getCounts(-1);
        
        // Verify that getCounts(24) throws OutOfRangeException
        counter.getCounts(24);
        
        //....
        // Continue verifying other out of range indices
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        // Verify that getCount(0, 0, 0, 0) throws DimensionMismatchException
        counter.getCount(0, 0, 0, 0);
        
        // Verify that getCount(1, 2) throws DimensionMismatchException
        counter.getCount(1, 2);
        
        //....
        // Continue verifying other dimension mismatch cases
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        // Verify that getCount(0, 0, 4) throws OutOfRangeException
        counter.getCount(0, 0, 4);
        
        // Verify that getCount(1, -1, 2) throws OutOfRangeException
        counter.getCount(1, -1, 2);
        
        //....
        // Continue verifying other out of range cases
    }

    @Test
    public void testGetSize() {
        // Verify that getSize() returns 24 for a counter with sizes (3, 4, 2)
        assertEquals(24, counter.getSize());
        
        //....
        // Continue verifying other counter sizes
    }

    @Test
    public void testGetSizes() {
        // Verify that getSizes() returns [3, 4, 2] for a counter with sizes (3, 4, 2)
        assertArrayEquals(new int[]{3, 4, 2}, counter.getSizes());
        
        //....
        // Continue verifying other counter sizes
    }

    @Test
    public void testToString() {
        // Verify that toString() returns the expected string representation of the counter
        // for example "[0][0][0]" for the first element
        assertEquals("[0][0][0]", counter.toString());
        
        //....
        // Continue verifying other elements
    }

}