import static org.junit.Assert.*;
import org.junit.*;

public class MultidimensionalCounterTest {
    
    private MultidimensionalCounter counter;
    
    @Before
    public void setUp() {
        try {
            counter = new MultidimensionalCounter(2, 3, 4);
        } catch (Exception e) {
            fail("Exception thrown in setUp method");
        }
    }
    
    @Test
    public void testGetDimension() {
        assertEquals(3, counter.getDimension());
    }
    
    @Test
    public void testGetCounts() {
        try {
            int[] counts = counter.getCounts(10);
            assertArrayEquals(new int[] {2, 0, 2}, counts);
        } catch (Exception e) {
            fail("Exception thrown in testGetCounts method");
        }
    }
    
    @Test
    public void testGetCountsOutOfRange() {
        try {
            counter.getCounts(30);
            fail("No exception thrown");
        } catch (OutOfRangeException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }
    
    @Test
    public void testGetCount() {
        try {
            int[] counts = {1, 2, 3};
            int count = counter.getCount(counts);
            assertEquals(47, count);
        } catch (Exception e) {
            fail("Exception thrown in testGetCount method");
        }
    }
    
    @Test
    public void testGetCountInvalidDimension() {
        try {
            int[] counts = {1, 2};
            counter.getCount(counts);
            fail("No exception thrown");
        } catch (DimensionMismatchException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }
    
    @Test
    public void testGetCountOutOfRange() {
        try {
            int[] counts = {1, 2, 5};
            counter.getCount(counts);
            fail("No exception thrown");
        } catch (OutOfRangeException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }
    
    @Test
    public void testGetSize() {
        assertEquals(24, counter.getSize());
    }
    
    @Test
    public void testGetSizes() {
        assertArrayEquals(new int[] {2, 3, 4}, counter.getSizes());
    }
    
    @Test
    public void testIterator() {
        assertEquals(0, counter.iterator().getCount());
        assertArrayEquals(new int[] {0, 0, 0}, counter.iterator().getCounts());
        
        while (counter.iterator().hasNext()) {
            counter.iterator().next();
        }
        
        assertEquals(23, counter.iterator().getCount());
        assertArrayEquals(new int[] {1, 2, 3}, counter.iterator().getCounts());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        counter.iterator().remove();
    }
}