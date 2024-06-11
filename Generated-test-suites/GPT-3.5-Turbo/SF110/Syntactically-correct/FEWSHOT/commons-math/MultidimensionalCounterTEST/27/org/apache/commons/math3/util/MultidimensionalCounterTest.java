package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.*;
import java.util.*;

public class MultidimensionalCounterTest {
    @Test
    public void testConstructorWithPositiveSize() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
            assertArrayEquals(size, multidimensionalCounter.getSizes());
            assertEquals(24, multidimensionalCounter.getSize());
        } catch (NotStrictlyPositiveException e) {
            fail("Should not throw an exception");
        }
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroSize() throws NotStrictlyPositiveException {
        int[] size = {0, 3, 4};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeSize() throws NotStrictlyPositiveException {
        int[] size = {2, -3, 4};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
    }
    
    @Test
    public void testIterator() {
        int[] size = {2, 3};
        try {
            MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
            Iterator<Integer> iterator = multidimensionalCounter.iterator();
            
            assertTrue(iterator.hasNext());
            assertEquals(0, (int)iterator.next());
            assertEquals(1, (int)iterator.next());
            assertEquals(2, (int)iterator.next());
            assertEquals(3, (int)iterator.next());
            assertEquals(4, (int)iterator.next());
            assertEquals(5, (int)iterator.next());
            
            assertFalse(iterator.hasNext());
        } catch (NotStrictlyPositiveException e) {
            fail("Should not throw an exception");
        }
    }
    
    @Test
    public void testGetDimension() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
            assertEquals(3, multidimensionalCounter.getDimension());
        } catch (NotStrictlyPositiveException e) {
            fail("Should not throw an exception");
        }
    }
    
    @Test
    public void testGetCountsInRange() {
        int[] size = {2, 3};
        try {
            MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
            assertArrayEquals(new int[]{0, 0}, multidimensionalCounter.getCounts(0));
            assertArrayEquals(new int[]{0, 1}, multidimensionalCounter.getCounts(1));
            assertArrayEquals(new int[]{1, 0}, multidimensionalCounter.getCounts(2));
            assertArrayEquals(new int[]{1, 1}, multidimensionalCounter.getCounts(3));
            assertArrayEquals(new int[]{0, 2}, multidimensionalCounter.getCounts(4));
            assertArrayEquals(new int[]{1, 2}, multidimensionalCounter.getCounts(5));
        } catch (OutOfRangeExcpetion e) {
            fail("Should not throw an exception");
        }
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() throws OutOfRangeException {
        int[] size = {2, 3};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
        int[] counts = multidimensionalCounter.getCounts(6);
    }
    
    @Test
    public void testGetCount() {
        int[] size = {2, 3};
        try {
            MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
            assertEquals(0, multidimensionalCounter.getCount(0, 0));
            assertEquals(1, multidimensionalCounter.getCount(0, 1));
            assertEquals(2, multidimensionalCounter.getCount(1, 0));
            assertEquals(3, multidimensionalCounter.getCount(1, 1));
            assertEquals(4, multidimensionalCounter.getCount(0, 2));
            assertEquals(5, multidimensionalCounter.getCount(1, 2));
        } catch (OutOfRangeException | DimensionMismatchException e) {
            fail("Should not throw an exception");
        }
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWithInvalidDimension() throws OutOfRangeException, DimensionMismatchException {
        int[] size = {2, 3};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
        multidimensionalCounter.getCount(0);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithOutOfRangeIndex() throws OutOfRangeException, DimensionMismatchException {
        int[] size = {2, 3};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
        multidimensionalCounter.getCount(3, 0);
    }
    
    @Test
    public void testGetSize() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
            assertEquals(24, multidimensionalCounter.getSize());
        } catch (NotStrictlyPositiveException e) {
            fail("Should not throw an exception");
        }
    }
    
    @Test
    public void testGetSizes() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
            assertArrayEquals(size, multidimensionalCounter.getSizes());
        } catch (NotStrictlyPositiveException e) {
            fail("Should not throw an exception");
        }
    }
}