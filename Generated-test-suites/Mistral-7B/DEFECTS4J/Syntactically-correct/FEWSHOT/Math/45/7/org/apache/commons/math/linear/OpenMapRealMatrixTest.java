package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixCreation() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertEquals(0, matrix.getSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDimensionOpenMapRealMatrixCreation() {
        new OpenMapRealMatrix(-1, -1);
    }

    @Test
    public void testCopyMatrixOpenMapRealMatrix() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix copy = original.copy();
        assertEquals(original, copy);
    }

    @Test
    public void testCreateMatrixOpenMapRealMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3));
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testGetColumnDimensionOpenMapRealMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testAddEmptyOpenMapRealMatrix() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(1, 1);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(1, 1);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(1, result.getRowDimension());
        assertEquals(1, result.getColumnDimension());
        assertEquals(0, result.getSize());
    }

    @Test
    public void testAddSparseMatrixOpenMapRealMatrix() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 3);
        expected.setEntry(0, 0, 0.0 + 0.0);
        expected.setEntry(0, 1, 0.0 + 0.0);
        expected.setEntry(0, 2, 0.0 + 0.0);
        expected.setEntry(1, 0, 0.0 + 0.0);
        expected.setEntry(1, 1, 0.0 + 0.0);
        expected.setEntry(1, 2, 0.0 + 0.0);
        assertArrayEquals(expected.toString().split("\n"), result.toString().split("\n"));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAddIncompatibleOpenMapRealMatrix() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.add(matrix2);
    }

    @Test
    public void testSubtractEmptyOpenMapRealMatrix() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(1, 1);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(1, 1);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(1, result.getRowDimension());
        assertEquals(1, result.getColumnDimension());
        assertEquals(0, result.getSize());
    }

    @Test
    public void testSubtractSparseMatrixOpenMapRealMatrix() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 3);
        expected.setEntry(0, 0, 0.0 - 0.0);
        expected.setEntry(0, 1, 0.0 - 0.0);
        expected.setEntry(0, 2, 0.0 - 0.0);
        expected.setEntry(1, 0, 0.0 - 0.0);
        expected.setEntry(1, 1, 0.0 - 0.0);
        expected.setEntry(1, 2, 0.0 - 0.0);
        assertArrayEquals(expected.toString().split("\n"), result.toString().split("\n"));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSubtractIncompatibleOpenMapRealMatrix() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.subtract(matrix2);
    }

    @Test
    public void testMultiplyByZeroOpenMapRealMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix result = matrix.multiply(Array2DRowRealMatrix.identity(3));
        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 3);
        expected.setEntry(0, 0, 0.0);
        expected.setEntry(0, 1, 0.0);
        expected.setEntry(0, 2, 0.0);
        expected.setEntry(1, 0, 0.0);
        expected.setEntry(1, 1, 0.0);
        expected.setEntry(1, 2, 0.0);
        assertArrayEquals(expected.toString().split("\n"), result.toString().split("\n"));
    }

    @Test
    public void testMultiplySparseMatrixOpenMapRealMatrix() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 2);
        expected.setEntry(0, 0, 0.0 * 0.0 + 0.0 * 0.0 + 0.0 * 0.0);
        expected.setEntry(0, 1, 0.0 * 0.0 + 0.0 * 0.0 + 0.0 * 0.0);
        expected.setEntry(1, 0, 0.0 * 0.0 + 0.0 * 0.0 + 0.0 * 0.0);
        expected.setEntry(1, 1, 0.0 * 0.0 + 0.0 * 0.0 + 0.0 * 0.0);
        assertArrayEquals(expected.toString().split("\n"), result.toString().split("\n"));
    }

    @Test(expected = MatrixDimensionMismatchException.class)
    public void testMultiplyIncompatibleOpenMapRealMatrix() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        RealMatrix matrix2 = new Array2DRowRealMatrix(2, 3);
        matrix1.multiply(matrix2);
    }

    @Test
    public void testMultiplyDenseMatrixOpenMapRealMatrix() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        RealMatrix matrix2 = new Array2DRowRealMatrix(3, 2);
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        RealMatrix expected = matrix1.multiply(matrix2);
        assertArrayEquals(expected.toString().split("\n"), result.toString().split("\n"));
    }

    @Test
    public void testSetEntryOpenMapRealMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-8);
        assertEquals(2.0, matrix.getEntry(1, 1), 1e-8);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryOutOfBoundsOpenMapRealMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(3, 3, 3.0);
    }

    @Test
    public void testAddToEntryOpenMapRealMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.addToEntry(0, 0, 1.0);
        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 1e-8);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryOutOfBoundsOpenMapRealMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.addToEntry(3, 3, 3.0);
    }

    @Test
    public void testMultiplyEntryOpenMapRealMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0 * 0.0, matrix.getEntry(0, 0), 1e-8);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryOutOfBoundsOpenMapRealMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.multiplyEntry(3, 3, 2.0);
    }

    @Test
    public void testGetEntryOpenMapRealMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-8);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryOutOfBoundsOpenMapRealMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.getEntry(3, 3);
    }
}