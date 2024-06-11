import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {
  
    private MultidimensionalCounter counter;
  
    @Before
    public void setUp() {
        counter = new MultidimensionalCounter(2, 4, 3);
    }
  
    @Test
    public void testGetDimension() {
        assertEquals(3, counter.getDimension());
    }
  
    @Test
    public void testGetSize() {
        assertEquals(24, counter.getSize());
    }
  
    @Test
    public void testGetSizes() {
        int[] expectedSizes = {2, 4, 3};
        assertArrayEquals(expectedSizes, counter.getSizes());
    }
  
    @Test
    public void testGetCounts() {
        int[] counts = counter.getCounts(12);
        int[] expectedCounts = {1, 0, 0};
        assertArrayEquals(expectedCounts, counts);
    }
  
    @Test
    public void testGetCountsOutOfRange() {
        try {
            int[] counts = counter.getCounts(24);
            fail("Expected OutOfRangeException");
        } catch (Exception e) {
            assertEquals(OutOfRangeException.class, e.getClass());
        }
    }
  
    @Test
    public void testGetCount() {
        int count = counter.getCount(1, 3, 2);
        assertEquals(23, count);
    }
  
    @Test
    public void testGetCountWithInvalidDimensionSize() {
        try {
            int count = counter.getCount(2, 3, 1);
            fail("Expected DimensionMismatchException");
        } catch (Exception e) {
            assertEquals(DimensionMismatchException.class, e.getClass());
        }
    }
  
    @Test
    public void testGetCountWithOutOfRangeIndex() {
        try {
            int count = counter.getCount(0, 5, 2);
            fail("Expected OutOfRangeException");
        } catch (Exception e) {
            assertEquals(OutOfRangeException.class, e.getClass());
        }
    }
  
    @Test
    public void testIteratorNext() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(Integer.valueOf(0), iterator.next());
    }
  
    @Test
    public void testIteratorHasNext() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
    }
  
    @Test
    public void testIteratorRemove() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        try {
            iterator.remove();
            fail("Expected UnsupportedOperationException");
        } catch (Exception e) {
            assertEquals(UnsupportedOperationException.class, e.getClass());
        }
    }
  
    @Test
    public void testIteratorGetCount() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(0, iterator.getCount());
    }
  
    @Test
    public void testIteratorGetCounts() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int[] counts = iterator.getCounts();
        int[] expectedCounts = {0, 0, 0};
        assertArrayEquals(expectedCounts, counts);
    }
  
    @Test
    public void testIteratorGetCountWithValidDimension() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(0, iterator.getCount(0));
    }
  
    @Test
    public void testIteratorGetCountWithInvalidDimension() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        try {
            int count = iterator.getCount(3);
            fail("Expected IndexOutOfBoundsException");
        } catch (Exception e) {
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }
    }
}