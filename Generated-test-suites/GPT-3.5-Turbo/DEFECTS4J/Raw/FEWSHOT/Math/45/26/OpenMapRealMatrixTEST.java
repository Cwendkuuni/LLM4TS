package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OpenMapRealMatrixTest {
    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNull(matrix.getEntry(0, 0));
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copy = matrix.copy();
        assertEquals(matrix.getRowDimension(), copy.getRowDimension());
        assertEquals(matrix.getColumnDimension(), copy.getColumnDimension());
        assertNull(copy.getEntry(0, 0));
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix createdMatrix = matrix.createMatrix(5, 5);
        assertEquals(5, createdMatrix.getRowDimension());
        assertEquals(5, createdMatrix.getColumnDimension());
        assertNull(createdMatrix.getEntry(0, 0));
    }

    @Test
    public void testGetColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        assertEquals(4, matrix.getColumnDimension());
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 2.0);
        OpenMapRealMatrix matrixToAdd = new OpenMapRealMatrix(3, 3);
        matrixToAdd.setEntry(0, 0, 3.0);
        OpenMapRealMatrix result = matrix.add(matrixToAdd);
        assertEquals(5.0, result.getEntry(0, 0), 0.0);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 5.0);
        OpenMapRealMatrix matrixToSubtract = new OpenMapRealMatrix(3, 3);
        matrixToSubtract.setEntry(0, 0, 3.0);
        OpenMapRealMatrix result = matrix.subtract(matrixToSubtract);
        assertEquals(2.0, result.getEntry(0, 0), 0.0);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 2.0);
        OpenMapRealMatrix matrixToMultiply = new OpenMapRealMatrix(2, 2);
        matrixToMultiply.setEntry(0, 0, 3.0);
        OpenMapRealMatrix result = matrix.multiply(matrixToMultiply);
        assertEquals(6.0, result.getEntry(0, 0), 0.0);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 2.0);
        matrix.addToEntry(0, 0, 3.0);
        assertEquals(5.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 2.0);
        matrix.multiplyEntry(0, 0, 3.0);
        assertEquals(6.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test
    public void testComputeKey() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        int key = matrix.computeKey(1, 1);
        assertEquals(4, key);
    }
}