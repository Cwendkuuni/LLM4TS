package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math.linear.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixCreation() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNull(matrix.getEntry(0, 0));
        assertNull(matrix.getEntry(1, 1));
    }

    @Test
    public void testOpenMapRealMatrixCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix copy = original.copy();
        assertEquals(original, copy);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixNegativeDimension() {
        new OpenMapRealMatrix(-1, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixZeroDimension() {
        new OpenMapRealMatrix(0, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixDifferentDimension() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        assertNotEquals(matrix1, matrix2);
    }

    @Test
    public void testOpenMapRealMatrixAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);
        matrix2.setEntry(1, 2, 5.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(2, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
        assertEquals(4.0, result.getEntry(0, 0), 0.001);
        assertEquals(6.0, result.getEntry(1, 1), 0.001);
        assertEquals(5.0, result.getEntry(1, 2), 0.001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testOpenMapRealMatrixAddDifferentDimension() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.add(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(0, 0, -1.0);
        matrix2.setEntry(1, 1, -2.0);

        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(2, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
        assertEquals(0.0, result.getEntry(0, 0), 0.001);
        assertEquals(0.0, result.getEntry(1, 1), 0.001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testOpenMapRealMatrixSubtractDifferentDimension() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.subtract(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(0, 2, 3.0);
        matrix2.setEntry(0, 0, 4.0);
        matrix2.setEntry(1, 0, 5.0);
        matrix2.setEntry(1, 1, 6.0);

        RealMatrix result = matrix1.multiply(matrix2);
        assertEquals(2, result.getRowDimension());
        assertEquals(2, result.getColumnDimension());
        assertEquals(22.0, result.getEntry(0, 0), 0.001);
        assertEquals(30.0, result.getEntry(0, 1), 0.001);
        assertEquals(36.0, result.getEntry(1, 0), 0.001);
        assertEquals(42.0, result.getEntry(1, 1), 0.001);
    }

    @Test(expected = MatrixDimensionMismatchException.class)
    public void testOpenMapRealMatrixMultiplyIncompatible() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix1.multiply(matrix2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testOpenMapRealMatrixSetEntryTooLarge() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(1, 1);
        long maxLong = Long.MAX_VALUE;
        matrix.setEntry(0, 0, maxLong + 1);
    }

    @Test
    public void testOpenMapRealMatrixSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(1, 1);
        double value = 123.45;
        matrix.setEntry(0, 0, value);
        assertEquals(value, matrix.getEntry(0, 0), 0.001);
    }

    @Test
    public void testOpenMapRealMatrixAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(1, 1);
        double value = 123.45;
        matrix.addToEntry(0, 0, value);
        assertEquals(value, matrix.getEntry(0, 0), 0.001);
    }

    @Test
    public void testOpenMapRealMatrixMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(1, 1);
        double value = 123.45;
        matrix.multiplyEntry(0, 0, value);
        assertEquals(value * matrix.getEntry(0, 0), matrix.getEntry(0, 0), 0.001);
    }
}