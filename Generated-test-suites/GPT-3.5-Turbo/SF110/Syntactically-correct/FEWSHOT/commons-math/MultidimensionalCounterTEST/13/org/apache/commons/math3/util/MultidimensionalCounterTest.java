package org.apache.commons.math3.util;
import org.apache.commons.math3.exception.*;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {
    @Test
    public void testConstructor() throws NotStrictlyPositiveException {
        int[] size1 = {2, 3};
        MultidimensionalCounter m1 = new MultidimensionalCounter(size1);
        assertEquals(2, m1.getDimension());
        
        int[] size2 = {4, 5, 6};
        MultidimensionalCounter m2 = new MultidimensionalCounter(size2);
        assertEquals(3, m2.getDimension());
        
        int[] size3 = {1, 1, 1, 1};
        MultidimensionalCounter m3 = new MultidimensionalCounter(size3);
        assertEquals(4, m3.getDimension());
        
        int[] size4 = {};
        NotStrictlyPositiveException exception = assertThrows(NotStrictlyPositiveException.class, () -> {
            MultidimensionalCounter m4 = new MultidimensionalCounter(size4);
        });
        assertEquals("Number of elements must be positive: 0", exception.getMessage());
    }
    
    @Test
    public void testIterator() {
        int[] size1 = {2, 3};
        MultidimensionalCounter m1 = new MultidimensionalCounter(size1);
        Iterator iterator1 = m1.iterator();
        int count1 = 0;
        while(iterator1.hasNext()) {
            iterator1.next();
            count1++;
        }
        assertEquals(6, count1);
        
        int[] size2 = {4, 4};
        MultidimensionalCounter m2 = new MultidimensionalCounter(size2);
        Iterator iterator2 = m2.iterator();
        int count2 = 0;
        while(iterator2.hasNext()) {
            iterator2.next();
            count2++;
        }
        assertEquals(16, count2);
        
        int[] size3 = {2, 2, 2};
        MultidimensionalCounter m3 = new MultidimensionalCounter(size3);
        Iterator iterator3 = m3.iterator();
        int count3 = 0;
        while(iterator3.hasNext()) {
            iterator3.next();
            count3++;
        }
        assertEquals(8, count3);
    }
    
    @Test
    public void testGetCounts() {
        int[] size1 = {2, 3};
        MultidimensionalCounter m1 = new MultidimensionalCounter(size1);
        
        int[] counts1 = m1.getCounts(0);
        int[] expectedCounts1 = {0, 0};
        assertArrayEquals(expectedCounts1, counts1);
        
        int[] counts2 = m1.getCounts(5);
        int[] expectedCounts2 = {1, 2};
        assertArrayEquals(expectedCounts2, counts2);
        
        int[] counts3 = m1.getCounts(1);
        int[] expectedCounts3 = {0, 1};
        assertArrayEquals(expectedCounts3, counts3);
        
        int[] counts4 = m1.getCounts(3);
        int[] expectedCounts4 = {1, 0};
        assertArrayEquals(expectedCounts4, counts4);
    }
    
    @Test
    public void testGetCountsOutOfRange() {
        int[] size = {2, 3};
        MultidimensionalCounter m = new MultidimensionalCounter(size);
        
        OutOfRangeException exception1 = assertThrows(OutOfRangeException.class, () -> {
            int[] counts1 = m.getCounts(-1);
        });
        assertEquals("Specified index is out of range: -1 is not in [0, 5]", exception1.getMessage());
        
        OutOfRangeException exception2 = assertThrows(OutOfRangeException.class, () -> {
            int[] counts2 = m.getCounts(6);
        });
        assertEquals("Specified index is out of range: 6 is not in [0, 5]", exception2.getMessage());
    }
    
    @Test
    public void testGetCount() {
        int[] size1 = {2, 3};
        MultidimensionalCounter m1 = new MultidimensionalCounter(size1);
        
        int count1 = m1.getCount(0, 0);
        assertEquals(0, count1);
        
        int count2 = m1.getCount(1, 2);
        assertEquals(5, count2);
        
        int count3 = m1.getCount(0, 2);
        assertEquals(2, count3);
        
        int count4 = m1.getCount(1, 0);
        assertEquals(3, count4);
    }
    
    @Test
    public void testGetCountWithInvalidDimension() {
        int[] size = {2, 3};
        MultidimensionalCounter m = new MultidimensionalCounter(size);
        
        int[] counts1 = {1};
        DimensionMismatchException exception1 = assertThrows(DimensionMismatchException.class, () -> {
            int count1 = m.getCount(counts1);
        });
        assertEquals("Dimension mismatch: 1", exception1.getMessage());
        
        int[] counts2 = {1, 2, 3};
        DimensionMismatchException exception2 = assertThrows(DimensionMismatchException.class, () -> {
            int count2 = m.getCount(counts2);
        });
        assertEquals("Dimension mismatch: 3", exception2.getMessage());
    }
    
    @Test
    public void testGetCountOutOfRange() {
        int[] size = {2, 3};
        MultidimensionalCounter m = new MultidimensionalCounter(size);
        
        OutOfRangeException exception1 = assertThrows(OutOfRangeException.class, () -> {
            int count1 = m.getCount(-1, 0);
        });
        assertEquals("Specified index is out of range: -1 is not in [0, 1]", exception1.getMessage());
        
        OutOfRangeException exception2 = assertThrows(OutOfRangeException.class, () -> {
            int count2 = m.getCount(2, 0);
        });
        assertEquals("Specified index is out of range: 2 is not in [0, 1]", exception2.getMessage());
        
        OutOfRangeException exception3 = assertThrows(OutOfRangeException.class, () -> {
            int count3 = m.getCount(0, 3);
        });
        assertEquals("Specified index is out of range: 3 is not in [0, 2]", exception3.getMessage());
    }
    
    @Test
    public void testGetSize() {
        int[] size1 = {2, 3};
        MultidimensionalCounter m1 = new MultidimensionalCounter(size1);
        assertEquals(6, m1.getSize());
        
        int[] size2 = {4, 4};
        MultidimensionalCounter m2 = new MultidimensionalCounter(size2);
        assertEquals(16, m2.getSize());
        
        int[] size3 = {2, 2, 2};
        MultidimensionalCounter m3 = new MultidimensionalCounter(size3);
        assertEquals(8, m3.getSize());
    }
    
    @Test
    public void testGetSizes() {
        int[] size1 = {2, 3};
        MultidimensionalCounter m1 = new MultidimensionalCounter(size1);
        int[] expectedSizes1 = {2, 3};
        assertArrayEquals(expectedSizes1, m1.getSizes());
        
        int[] size2 = {4, 4};
        MultidimensionalCounter m2 = new MultidimensionalCounter(size2);
        int[] expectedSizes2 = {4, 4};
        assertArrayEquals(expectedSizes2, m2.getSizes());
        
        int[] size3 = {2, 2, 2};
        MultidimensionalCounter m3 = new MultidimensionalCounter(size3);
        int[] expectedSizes3 = {2, 2, 2};
        assertArrayEquals(expectedSizes3, m3.getSizes());
    }
}