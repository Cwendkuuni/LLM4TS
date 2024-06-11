package org.apache.commons.math3.util;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.util.*;

public class MultidimensionalCounterTest {
    
    @Test
    public void testGetDimension() {
        MultidimensionalCounter mc = new MultidimensionalCounter(2, 3);
        assertEquals(2, mc.getDimension());
    }
    
    @Test
    public void testGetCounts() {
        MultidimensionalCounter mc = new MultidimensionalCounter(2, 3);
        assertArrayEquals(new int[]{1, 2}, mc.getCounts(5));
    }
    
    @Test
    public void testGetCount() {
        MultidimensionalCounter mc = new MultidimensionalCounter(2, 3);
        assertEquals(5, mc.getCount(1, 2));
    }
    
    @Test
    public void testGetSize() {
        MultidimensionalCounter mc = new MultidimensionalCounter(2, 3);
        assertEquals(6, mc.getSize());
    }
    
    @Test
    public void testGetSizes() {
        MultidimensionalCounter mc = new MultidimensionalCounter(2, 3);
        assertArrayEquals(new int[]{2, 3}, mc.getSizes());
    }
}