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
    public void testAdd() {
        OpenMapRealMatrix matrixToAdd = new OpenMapRealMatrix(3, 3);
        matrixToAdd.setEntry(0, 0, 1.0);
        matrixToAdd.setEntry(1, 1, 2.0);
        matrixToAdd.setEntry(2, 2, 3.0);

        OpenMapRealMatrix result = matrix.add(matrixToAdd);

        assertEquals(1.0, result.getEntry(0, 0), 0.0);
        assertEquals(2.0, result.getEntry(1, 1), 0.0);
        assertEquals(3.0, result.getEntry(2, 2), 0.0);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrixToSubtract = new OpenMapRealMatrix(3, 3);
        matrixToSubtract.setEntry(0, 0, 1.0);
        matrixToSubtract.setEntry(1, 1, 2.0);
        matrixToSubtract.setEntry(2, 2, 3.0);

        OpenMapRealMatrix result = matrix.subtract(matrixToSubtract);

        assertEquals(-1.0, result.getEntry(0, 0), 0.0);
        assertEquals(-2.0, result.getEntry(1, 1), 0.0);
        assertEquals(-3.0, result.getEntry(2, 2), 0.0);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrixToMultiply = new OpenMapRealMatrix(3, 3);
        matrixToMultiply.setEntry(0, 0, 1.0);
        matrixToMultiply.setEntry(1, 1, 2.0);
        matrixToMultiply.setEntry(2, 2, 3.0);

        OpenMapRealMatrix result = matrix.multiply(matrixToMultiply);

        assertEquals(0.0, result.getEntry(0, 0), 0.0);
        assertEquals(0.0, result.getEntry(1, 1), 0.0);
        assertEquals(0.0, result.getEntry(2, 2), 0.0);
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
    public void testAddToEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);

        matrix.addToEntry(0, 0, 1.0);
        matrix.addToEntry(1, 1, 2.0);
        matrix.addToEntry(2, 2, 3.0);

        assertEquals(2.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(4.0, matrix.getEntry(1, 1), 0.0);
        assertEquals(6.0, matrix.getEntry(2, 2), 0.0);
    }

    @Test
    public void testMultiplyEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);

        matrix.multiplyEntry(0, 0, 2.0);
        matrix.multiplyEntry(1, 1, 3.0);
        matrix.multiplyEntry(2, 2, 4.0);

        assertEquals(2.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(6.0, matrix.getEntry(1, 1), 0.0);
        assertEquals(12.0, matrix.getEntry(2, 2), 0.0);
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix newMatrix = matrix.createMatrix(2, 2);

        assertEquals(2, newMatrix.getRowDimension());
        assertEquals(2, newMatrix.getColumnDimension());
    }

    @Test
    public void testGetColumnDimension() {
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testGetRowDimension() {
        assertEquals(3, matrix.getRowDimension());
    }
}