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
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = matrix1.copy();
        assertEquals(matrix1.getRowDimension(), matrix2.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), matrix2.getColumnDimension());
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
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getColumnDimension());
    }
    
    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
    }
    
    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
    }
    
    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
    }
    
    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(0.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(0.0, matrix.getEntry(2, 2), 0.0);
    }
    
    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
    }
    
    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
    }
    
    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 2.0);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(4.0, matrix.getEntry(0, 0), 0.0);
    }
    
    @Test
    public void testGetRowDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
    }

}