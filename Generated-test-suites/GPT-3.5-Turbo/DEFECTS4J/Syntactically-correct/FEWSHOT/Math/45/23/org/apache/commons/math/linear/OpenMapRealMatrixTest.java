package org.apache.commons.math.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.util.OpenIntToDoubleHashMap;
import org.junit.Before;
import org.junit.Test;

public class OpenMapRealMatrixTest {
    private OpenMapRealMatrix matrix;

    @Before
    public void setup() {
        matrix = new OpenMapRealMatrix(3, 3);
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix copyMatrix = matrix.copy();
        
        // Check if the copied matrix is not the same reference as the original matrix
        assertNotSame(matrix, copyMatrix);
        
        // Check if the dimensions of the copied matrix are the same as the original matrix
        assertEquals(matrix.getRowDimension(), copyMatrix.getRowDimension());
        assertEquals(matrix.getColumnDimension(), copyMatrix.getColumnDimension());
    }
    
    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix newMatrix = matrix.createMatrix(4, 4);
        
        // Check if the dimensions of the new matrix are correct
        assertEquals(4, newMatrix.getRowDimension());
        assertEquals(4, newMatrix.getColumnDimension());
    }
    
    @Test
    public void testGetColumnDimension() {
        assertEquals(3, matrix.getColumnDimension());
    }
    
    @Test
    public void testAdd() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        
        // Add matrices
        OpenMapRealMatrix result = matrix.add(m);
        
        // Check if the dimensions of the result matrix are correct
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testAddDimensionMismatchException() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(4, 4);
        
        // Try to add matrices with different dimensions, should throw DimensionMismatchException
        matrix.add(m);
    }
    
    @Test
    public void testSubtract() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        
        // Subtract matrices
        OpenMapRealMatrix result = matrix.subtract(m);
        
        // Check if the dimensions of the result matrix are correct
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testSubtractDimensionMismatchException() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(4, 4);
        
        // Try to subtract matrices with different dimensions, should throw DimensionMismatchException
        matrix.subtract(m);
    }
    
    @Test
    public void testMultiplyWithOpenMapRealMatrix() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        
        // Multiply matrices
        OpenMapRealMatrix result = matrix.multiply(m);
        
        // Check if the dimensions of the result matrix are correct
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testMultiplyWithOpenMapRealMatrixDimensionMismatchException() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(4, 4);
        
        // Try to multiply matrices with incompatible dimensions, should throw DimensionMismatchException
        matrix.multiply(m);
    }
    
    @Test
    public void testMultiplyWithRealMatrix() {
        RealMatrix m = new OpenMapRealMatrix(3, 3);
        
        // Multiply matrices
        RealMatrix result = matrix.multiply(m);
        
        // Check if the dimensions of the result matrix are correct
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testMultiplyWithRealMatrixDimensionMismatchException() {
        RealMatrix m = new OpenMapRealMatrix(4, 4);
        
        // Try to multiply matrices with incompatible dimensions, should throw DimensionMismatchException
        matrix.multiply(m);
    }
    
    @Test
    public void testGetEntry() {
        double value = matrix.getEntry(0, 0);
        
        // Check if the retrieved value is correct
        assertEquals(0.0, value, 0.0);
    }
    
    @Test
    public void testSetEntry() {
        matrix.setEntry(0, 0, 1.0);
        double value = matrix.getEntry(0, 0);
        
        // Check if the value is correctly set
        assertEquals(1.0, value, 0.0);
    }
    
    @Test
    public void testAddToEntry() {
        matrix.addToEntry(0, 0, 1.0);
        double value = matrix.getEntry(0, 0);
        
        // Check if the value is correctly added to the existing entry
        assertEquals(1.0, value, 0.0);
    }
    
    @Test
    public void testMultiplyEntry() {
        matrix.setEntry(0, 0, 2.0);
        matrix.multiplyEntry(0, 0, 3.0);
        double value = matrix.getEntry(0, 0);
        
        // Check if the value is correctly multiplied with the existing entry
        assertEquals(6.0, value, 0.0);
    }
    
    @Test
    public void testComputeKey() {
        OpenMapRealMatrix keyMatrix = new OpenMapRealMatrix(4, 4);
        
        int key = keyMatrix.computeKey(2, 2);
        
        // Check if the computed key is correct
        assertEquals(10, key);
    }
    
    @Test
    public void testGetRowDimension() {
        assertEquals(3, matrix.getRowDimension());
    }
}