package org.apache.commons.math.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.util.OpenIntToDoubleHashMap;
import org.junit.Before;
import org.junit.Test;

public class OpenMapRealMatrixTest {
    
    private OpenMapRealMatrix matrix;
    
    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(3, 3);
    }
    
    @Test
    public void testAdd() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        m.setEntry(0, 0, 1.0);
        m.setEntry(1, 1, 2.0);
        m.setEntry(2, 2, 3.0);
        
        OpenMapRealMatrix result = matrix.add(m);
        
        assertEquals(1.0, result.getEntry(0, 0), 0.0);
        assertEquals(2.0, result.getEntry(1, 1), 0.0);
        assertEquals(3.0, result.getEntry(2, 2), 0.0);
    }
    
    @Test
    public void testSubtract() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        m.setEntry(0, 0, 1.0);
        m.setEntry(1, 1, 2.0);
        m.setEntry(2, 2, 3.0);
        
        OpenMapRealMatrix result = matrix.subtract(m);
        
        assertEquals(-1.0, result.getEntry(0, 0), 0.0);
        assertEquals(-2.0, result.getEntry(1, 1), 0.0);
        assertEquals(-3.0, result.getEntry(2, 2), 0.0);
    }
    
    @Test
    public void testMultiply() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        m.setEntry(0, 0, 1.0);
        m.setEntry(1, 1, 2.0);
        m.setEntry(2, 2, 3.0);
        
        OpenMapRealMatrix result = matrix.multiply(m);
        
        assertEquals(0.0, result.getEntry(0, 0), 0.0);
        assertEquals(0.0, result.getEntry(1, 1), 0.0);
        assertEquals(0.0, result.getEntry(2, 2), 0.0);
    }
    
    @Test
    public void testGetEntry() {
        matrix.setEntry(0, 0, 1.0);
        double entry = matrix.getEntry(0, 0);
        assertEquals(1.0, entry, 0.0);
    }
    
    @Test
    public void testSetEntry() {
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
    }
    
    @Test
    public void testAddToEntry() {
        matrix.addToEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
    }
    
    @Test
    public void testMultiplyEntry() {
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(0.0, matrix.getEntry(0, 0), 0.0);
    }
    
    @Test
    public void testCopy() {
        OpenMapRealMatrix copy = matrix.copy();
        assertTrue(matrix != copy);
        assertTrue(matrix.getEntry(0, 0) == copy.getEntry(0, 0));
    }
    
    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix newMatrix = matrix.createMatrix(3, 3);
        assertEquals(3, newMatrix.getRowDimension());
        assertEquals(3, newMatrix.getColumnDimension());
    }
    
    @Test
    public void testGetColumnDimension() {
        assertEquals(3, matrix.getColumnDimension());
    }
    
    @Test
    public void testGetRowDimension() {
        assertEquals(3, matrix.getRowDimension());
    }
    
    @Test
    public void testComputeKey() {
        int key = matrix.computeKey(1, 1);
        assertEquals(4, key);
    }
    
}