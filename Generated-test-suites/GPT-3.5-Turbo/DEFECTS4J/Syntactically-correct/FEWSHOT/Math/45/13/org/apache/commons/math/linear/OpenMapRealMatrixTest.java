
package org.apache.commons.math.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    private OpenMapRealMatrix matrix;

    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(3, 3);
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix copy = matrix.copy();
        assertEquals(matrix.getRowDimension(), copy.getRowDimension());
        assertEquals(matrix.getColumnDimension(), copy.getColumnDimension());
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                assertEquals(matrix.getEntry(i, j), copy.getEntry(i, j), 0.0);
            }
        }
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix newMatrix = matrix.createMatrix(2, 2);
        assertEquals(2, newMatrix.getRowDimension());
        assertEquals(2, newMatrix.getColumnDimension());
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        m.setEntry(0, 0, 1.0);
        m.setEntry(1, 1, 2.0);
        m.setEntry(2, 2, 3.0);

        OpenMapRealMatrix result = matrix.add(m);
        assertEquals(3.0, result.getEntry(0, 0), 0.0);
        assertEquals(2.0, result.getEntry(1, 1), 0.0);
        assertEquals(3.0, result.getEntry(2, 2), 0.0);

        // Test with different dimensions
        try {
            matrix.add(new OpenMapRealMatrix(2, 2));
        } catch (MatrixDimensionMismatchException e) {
            assertEquals("Matrix dimensions must agree", e.getMessage());
        }
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

        // Test with different dimensions
        try {
            matrix.subtract(new OpenMapRealMatrix(2, 2));
        } catch (MatrixDimensionMismatchException e) {
            assertEquals("Matrix dimensions must agree", e.getMessage());
        }
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

        // Test with different dimensions
        try {
            matrix.multiply(new OpenMapRealMatrix(2, 3));
        } catch (MatrixDimensionMismatchException e) {
            assertEquals("Matrix dimensions must agree", e.getMessage());
        }
    }

    @Test
    public void testGetEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);

        // Test with invalid row and column indices
        try {
            matrix.getEntry(3, 3);
        } catch (MatrixIndexOutOfBoundsException e) {
            assertEquals("Invalid matrix index", e.getMessage());
        }
    }

    @Test
    public void testSetEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);

        // Test with invalid row and column indices
        try {
            matrix.setEntry(3, 3, 1.0);
        } catch (MatrixIndexOutOfBoundsException e) {
            assertEquals("Invalid matrix index", e.getMessage());
        }
    }

    @Test
    public void testAddToEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 0.0);

        // Test with invalid row and column indices
        try {
            matrix.addToEntry(3, 3, 1.0);
        } catch (MatrixIndexOutOfBoundsException e) {
            assertEquals("Invalid matrix index", e.getMessage());
        }
    }

    @Test
    public void testMultiplyEntry() {
        matrix.setEntry(0, 0, 2.0);
        matrix.multiplyEntry(0, 0, 3.0);
        assertEquals(6.0, matrix.getEntry(0, 0), 0.0);

        // Test with invalid row and column indices
        try {
            matrix.multiplyEntry(3, 3, 1.0);
        } catch (MatrixIndexOutOfBoundsException e) {
            assertEquals("Invalid matrix index", e.getMessage());
        }
    }
}
