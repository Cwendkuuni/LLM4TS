import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {
    
    @Test
    public void testConstructor_ValidSize() {
        try {
            MultidimensionalCounter m = new MultidimensionalCounter(2, 3, 4);
            assertNotNull(m);
            assertEquals(3, m.getDimension());
            int[] expectedSizes = {2, 3, 4};
            assertArrayEquals(expectedSizes, m.getSizes());
            assertEquals(24, m.getSize());
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException should not be thrown");
        }
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_InvalidSize() throws NotStrictlyPositiveException {
        MultidimensionalCounter m = new MultidimensionalCounter(-1, 0, 1);
    }
    
    @Test
    public void testGetCounts_ValidIndex() {
        try {
            MultidimensionalCounter m = new MultidimensionalCounter(2, 3, 4);
            int[] expectedCounts = {1, 2, 3};
            assertArrayEquals(expectedCounts, m.getCounts(21));
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException should not be thrown");
        } catch (OutOfRangeException e) {
            fail("OutOfRangeException should not be thrown");
        }
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_InvalidIndex_LowerBound() throws OutOfRangeException {
        MultidimensionalCounter m = new MultidimensionalCounter(2, 3, 4);
        int[] counts = m.getCounts(-1);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_InvalidIndex_UpperBound() throws OutOfRangeException {
        MultidimensionalCounter m = new MultidimensionalCounter(2, 3, 4);
        int[] counts = m.getCounts(24);
    }
    
    @Test
    public void testGetCount_ValidCount() {
        try {
            MultidimensionalCounter m = new MultidimensionalCounter(2, 3, 4);
            int count = m.getCount(1, 2, 3);
            assertEquals(23, count);
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException should not be thrown");
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException should not be thrown");
        } catch (OutOfRangeException e) {
            fail("OutOfRangeException should not be thrown");
        }
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_InvalidCount_DimensionMismatch() throws DimensionMismatchException, NotStrictlyPositiveException, OutOfRangeException {
        MultidimensionalCounter m = new MultidimensionalCounter(2, 3, 4);
        int count = m.getCount(1, 2);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCount_InvalidCount_OutOfRange() throws DimensionMismatchException, NotStrictlyPositiveException, OutOfRangeException {
        MultidimensionalCounter m = new MultidimensionalCounter(2, 3, 4);
        int count = m.getCount(1, 2, 5);
    }
    
    @Test
    public void testToString() {
        try {
            MultidimensionalCounter m = new MultidimensionalCounter(2, 3);
            assertEquals("[0][1][2]", m.toString());
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException should not be thrown");
        }
    }
    
    @Test
    public void testIterator() {
        try {
            MultidimensionalCounter m = new MultidimensionalCounter(2, 3);
            
            java.util.Iterator<Integer> iterator = m.iterator();
            assertTrue(iterator.hasNext());
            assertEquals(Integer.valueOf(0), iterator.next());
            assertTrue(iterator.hasNext());
            assertEquals(Integer.valueOf(1), iterator.next());
            assertTrue(iterator.hasNext());
            assertEquals(Integer.valueOf(2), iterator.next());
            assertFalse(iterator.hasNext());
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException should not be thrown");
        }
    }
    
}