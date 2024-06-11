package org.apache.commons.math.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OpenMapRealMatrixTest {
    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }
    
    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copy = matrix.copy();
        assertEquals(3, copy.getRowDimension());
        assertEquals(3, copy.getColumnDimension());
    }
    
    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix newMatrix = matrix.createMatrix(2, 2);
        assertEquals(2, newMatrix.getRowDimension());
        assertEquals(2, newMatrix.getColumnDimension());
    }
    
    @Test
    public void testGetColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        assertEquals(4, matrix.getColumnDimension());
    }
    
    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(0.0, result.getEntry(0, 0), 0.0);
        assertEquals(0.0, result.getEntry(0, 1), 0.0);
        assertEquals(0.0, result.getEntry(1, 0), 0.0);
        assertEquals(0.0, result.getEntry(1, 1), 0.0);
    }
    
    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(0.0, result.getEntry(0, 0), 0.0);
        assertEquals(0.0, result.getEntry(0, 1), 0.0);
        assertEquals(0.0, result.getEntry(1, 0), 0.0);
        assertEquals(0.0, result.getEntry(1, 1), 0.0);
    }
    
    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        RealMatrix result = matrix1.multiply(matrix2);
        assertEquals(0.0, result.getEntry(0, 0), 0.0);
        assertEquals(0.0, result.getEntry(0, 1), 0.0);
        assertEquals(0.0, result.getEntry(1, 0), 0.0);
        assertEquals(0.0, result.getEntry(1, 1), 0.0);
    }
    
    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        assertEquals(0.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(0.0, matrix.getEntry(0, 1), 0.0);
        assertEquals(0.0, matrix.getEntry(1, 0), 0.0);
        assertEquals(0.0, matrix.getEntry(1, 1), 0.0);
    }
    
    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix.getEntry(0, 1), 0.0);
        assertEquals(3.0, matrix.getEntry(1, 0), 0.0);
        assertEquals(4.0, matrix.getEntry(1, 1), 0.0);
    }
    
    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 0.0);
    }
    
    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 2.0);
        matrix.multiplyEntry(0, 0, 3.0);
        assertEquals(6.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test
    public void testGetRowDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        assertEquals(3, matrix.getRowDimension());
    }
}