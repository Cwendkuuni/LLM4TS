package org.apache.commons.math.linear;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.linear.OpenMapRealMatrix;

import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    private OpenMapRealMatrix matrix;

    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(3, 3);
    }

    @Test
    public void testConstructor() {
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix copy = matrix.copy();
        assertNotSame(matrix, copy);
        assertEquals(matrix.getRowDimension(), copy.getRowDimension());
        assertEquals(matrix.getColumnDimension(), copy.getColumnDimension());
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix newMatrix = matrix.createMatrix(4, 4);
        assertEquals(4, newMatrix.getRowDimension());
        assertEquals(4, newMatrix.getColumnDimension());
    }

    @Test
    public void testGetRowDimension() {
        assertEquals(3, matrix.getRowDimension());
    }

    @Test
    public void testGetColumnDimension() {
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testGetEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);
        assertEquals(3.0, matrix.getEntry(2, 2), 0.0);
    }

    @Test
    public void testSetEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);
        assertEquals(3.0, matrix.getEntry(2, 2), 0.0);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(1, 1, 2.0);
        matrix2.setEntry(2, 2, 3.0);

        OpenMapRealMatrix result = matrix.add(matrix2);

        assertEquals(2.0, result.getEntry(0, 0), 0.0);
        assertEquals(4.0, result.getEntry(1, 1), 0.0);
        assertEquals(6.0, result.getEntry(2, 2), 0.0);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(1, 1, 2.0);
        matrix2.setEntry(2, 2, 3.0);

        OpenMapRealMatrix result = matrix.subtract(matrix2);

        assertEquals(-1.0, result.getEntry(0, 0), 0.0);
        assertEquals(-2.0, result.getEntry(1, 1), 0.0);
        assertEquals(-3.0, result.getEntry(2, 2), 0.0);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(1, 1, 2.0);
        matrix2.setEntry(2, 2, 3.0);

        OpenMapRealMatrix result = matrix.multiply(matrix2);

        assertEquals(0.0, result.getEntry(0, 0), 0.0);
        assertEquals(0.0, result.getEntry(1, 1), 0.0);
        assertEquals(0.0, result.getEntry(2, 2), 0.0);
    }

    @Test
    public void testAddInvalidDimensions() {
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        try {
            matrix.add(matrix2);
            fail("Expected Exception");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("Matrix dimensions must be the same", e.getMessage());
        }
    }

    @Test
    public void testSubtractInvalidDimensions() {
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        try {
            matrix.subtract(matrix2);
            fail("Expected Exception");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("Matrix dimensions must be the same", e.getMessage());
        }
    }

    @Test
    public void testMultiplyInvalidDimensions() {
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        try {
            matrix.multiply(matrix2);
            fail("Expected Exception");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("Matrix dimensions incompatible for multiplication", e.getMessage());
        }
    }

    @Test
    public void testAddZeroValue() {
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(0, 0, 0.0);
        matrix2.setEntry(1, 1, 0.0);
        matrix2.setEntry(2, 2, 0.0);

        OpenMapRealMatrix result = matrix.add(matrix2);

        assertEquals(0.0, result.getEntry(0, 0), 0.0);
        assertEquals(0.0, result.getEntry(1, 1), 0.0);
        assertEquals(0.0, result.getEntry(2, 2), 0.0);
    }

    @Test
    public void testSetZeroValue() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 0.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);
        assertEquals(0.0, matrix.getEntry(2, 2), 0.0);
    }
}