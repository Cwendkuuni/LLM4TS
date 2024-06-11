import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultidimensionalCounterTest {
    
    private MultidimensionalCounter counter;
    
    @Before
    public void setUp() throws Exception {
        counter = new MultidimensionalCounter(3, 4, 5);
    }
    
    @Test
    public void testConstructor_validSizeDimension_createCounter() {
        assertNotNull(counter);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_zeroSizeDimension_throwException() throws NotStrictlyPositiveException {
        new MultidimensionalCounter(0);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_negativeSizeDimension_throwException() throws NotStrictlyPositiveException {
        new MultidimensionalCounter(-1);
    }
    
    @Test
    public void testIterator_hasNext_returnTrue() {
        assertTrue(counter.iterator().hasNext());
    }
    
    @Test
    public void testIterator_next_returnNextCount() {
        Iterator iterator = counter.iterator();
        int expectedCount = 0;
        
        while (iterator.hasNext()) {
            int actualCount = iterator.next();
            assertEquals(expectedCount++, actualCount);
        }
    }
    
    @Test
    public void testIterator_remove_throwUnsupportedOperationException() {
        Iterator iterator = counter.iterator();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }
    
    @Test
    public void testGetDimension_validIndex_returnDimension() {
        int expectedDimension = 3;
        int actualDimension = counter.getDimension();
        assertEquals(expectedDimension, actualDimension);
    }
    
    @Test
    public void testGetSizes_returnArrayOfSizes() {
        int[] expectedSizes = {3, 4, 5};
        int[] actualSizes = counter.getSizes();
        assertArrayEquals(expectedSizes, actualSizes);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_invalidIndex_throwOutOfRangeException() throws OutOfRangeException {
        counter.getCounts(-1);
    }
    
    @Test
    public void testGetCounts_validIndex_returnArrayOfCounts() throws OutOfRangeException {
        int[] expectedCounts = {0, 1, 2};
        int[] actualCounts = counter.getCounts(2);
        assertArrayEquals(expectedCounts, actualCounts);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_invalidDimension_throwDimensionMismatchException() throws DimensionMismatchException, OutOfRangeException {
        counter.getCount(1, 2);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCount_invalidIndex_throwOutOfRangeException() throws DimensionMismatchException, OutOfRangeException {
        counter.getCount(-1, 0, 0);
    }
    
    @Test
    public void testGetCount_validArguments_returnCount() throws DimensionMismatchException, OutOfRangeException {
        int expectedCount = 7;
        int actualCount = counter.getCount(1, 1, 2);
        assertEquals(expectedCount, actualCount);
    }
    
    @Test
    public void testGetSize_returnTotalSize() {
        int expectedSize = 60;
        int actualSize = counter.getSize();
        assertEquals(expectedSize, actualSize);
    }
    
    @Test
    public void testToString_returnStringRepresentation() {
        String expectedString = "[0][1][2]";
        String actualString = counter.toString();
        assertEquals(expectedString, actualString);
    }
}