package org.apache.commons.math.linear;

import org.junit.Test;

import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {
    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(4, matrix.getColumnDimension());
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        OpenMapRealMatrix copiedMatrix = matrix.copy();
        assertEquals(3, copiedMatrix.getRowDimension());
        assertEquals(4, copiedMatrix.getColumnDimension());
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        OpenMapRealMatrix createdMatrix = matrix.createMatrix(2, 2);
        assertEquals(2, createdMatrix.getRowDimension());
        assertEquals(2, createdMatrix.getColumnDimension());
    }

    @Test
    public void testGetColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        assertEquals(4, matrix.getColumnDimension());
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(1, 1, 3.0);

        OpenMapRealMatrix sum = matrix1.add(matrix2);
        assertEquals(3.0, sum.getEntry(0, 0), 0.0);
        assertEquals(5.0, sum.getEntry(1, 1), 0.0);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(1, 1, 3.0);

        OpenMapRealMatrix difference = matrix1.subtract(matrix2);
        assertEquals(-1.0, difference.getEntry(0, 0), 0.0);
        assertEquals(-1.0, difference.getEntry(1, 1), 0.0);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(1, 1, 3.0);

        OpenMapRealMatrix product = matrix1.multiply(matrix2);
        assertEquals(2.0, product.getEntry(0, 0), 0.0);
        assertEquals(6.0, product.getEntry(1, 1), 0.0);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);

        matrix.setEntry(0, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(4.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.addToEntry(0, 0, 3.0);
        matrix.addToEntry(1, 1, 4.0);
        assertEquals(4.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(6.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.multiplyEntry(0, 0, 3.0);
        matrix.multiplyEntry(1, 1, 4.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(8.0, matrix.getEntry(1, 1), 0.0);
    }
}