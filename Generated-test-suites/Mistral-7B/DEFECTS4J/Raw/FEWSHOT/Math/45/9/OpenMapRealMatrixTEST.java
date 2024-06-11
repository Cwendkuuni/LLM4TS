package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MatrixDimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixUtils;
import org.apache.commons.math.linear.SparseRealMatrix;

public class OpenMapRealMatrixTest {
    @Test
    public void testOpenMapRealMatrixCreation() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 2);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(2, matrix.getColumnDimension());
        assertEquals(0, matrix.size());
    }

    @Test
    public void testOpenMapRealMatrixCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 2);
        OpenMapRealMatrix copy = matrix.copy();
        assertEquals(matrix, copy);
    }

    @Test (expected = NumberIsTooLargeException.class)
    public void testOpenMapRealMatrixWithNegativeDimension() {
        new OpenMapRealMatrix(-1, 2);
    }

    @Test
    public void testOpenMapRealMatrixAddition() throws DimensionMismatchException {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1, 1}, {2, 2}})));
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{2, 2}, {3, 3}})));
        OpenMapRealMatrix sum = m1.add(m2);
        RealMatrix expectedSum = new Array2DRowRealMatrix(new double[][]{{3, 3}, {5, 5}});
        assertArrayEquals(expectedSum.getData(), sum.getData(), 0.0);
    }

    @Test
    public void testOpenMapRealMatrixSubtraction() throws DimensionMismatchException {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1, 1}, {2, 2}})));
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{2, 2}, {3, 3}})));
        OpenMapRealMatrix difference = m1.subtract(m2);
        RealMatrix expectedDifference = new Array2DRowRealMatrix(new double[][]{{-1, -1}, {-1, -1}});
        assertArrayEquals(expectedDifference.getData(), difference.getData(), 0.0);
    }

    @Test (expected = MatrixDimensionMismatchException.class)
    public void testOpenMapRealMatrixMultiplicationWithDimensionMismatch() throws DimensionMismatchException {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 2);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(2, 3);
        m1.multiply(m2);
    }

    @Test
    public void testOpenMapRealMatrixMultiplication() throws DimensionMismatchException {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1, 1}, {2, 2}})));
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{3, 3}, {4, 4}})));
        OpenMapRealMatrix product = m1.multiply(m2);
        RealMatrix expectedProduct = new Array2DRowRealMatrix(new double[][]{{3*1+4*0, 3*1+4*1, 3*1+4*2, 3*1+4*3},
                                                                                {3*2+4*0, 3*2+4*1, 3*2+4*2, 3*2+4*3}});
        assertArrayEquals(expectedProduct.getData(), product.getData(), 0.0);
    }

    @Test
    public void testOpenMapRealMatrixSetEntry() throws DimensionMismatchException {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}});
        assertArrayEquals(expected.getData(), matrix.getData(), 0.0);
    }

    @Test
    public void testOpenMapRealMatrixGetEntry() throws DimensionMismatchException {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1, 1}, {2, 2}})));
        double value = matrix.getEntry(0, 0);
        assertTrue(value == 1.0 || value != 0.0);
        double value2 = matrix.getEntry(1, 1);
        assertTrue(value2 == 2.0 || value2 != 0.0);
        assertNull(matrix.getEntry(2, 2));
    }

    @Test
    public void testOpenMapRealMatrixAddToEntry() throws DimensionMismatchException {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1, 1}, {2, 2}})));
        matrix.addToEntry(0, 0, 2.0);
        matrix.addToEntry(1, 1, -1.0);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{3.0, 1.0}, {1.0, 1.0}});
        assertArrayEquals(expected.getData(), matrix.getData(), 0.0);
    }

    @Test
    public void testOpenMapRealMatrixMultiplyEntry() throws DimensionMismatchException {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1, 1}, {2, 2}})));
        matrix.multiplyEntry(0, 0, 2.0);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {2.0, 1.0}});
        assertArrayEquals(expected.getData(), matrix.getData(), 0.0);
    }

    @Test
    public void testOpenMapRealMatrixSize() throws DimensionMismatchException {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1, 1}, {2, 2}})));
        assertEquals(4, matrix.size());
    }

    @Test
    public void testOpenMapRealMatrixIsEmpty() throws DimensionMismatchException {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1, 1}, {2, 2}})));
        assertFalse(matrix.isEmpty());

        OpenMapRealMatrix emptyMatrix = new OpenMapRealMatrix(3, 2);
        assertTrue(emptyMatrix.isEmpty());
    }

    @Test
    public void testOpenMapRealMatrixAddToSparseMatrix() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        addToOpenMapRealMatrix(a, 0, 0, 1.0);
        addToOpenMapRealMatrix(a, 1, 1, 2.0);
        addToOpenMapRealMatrix(a, 2, 2, 3.0);

        SparseRealMatrix b = new OpenMapRealMatrix(3, 3);
        addToOpenMapRealMatrix(b, 0, 0, 1.0);
        addToOpenMapRealMatrix(b, 1, 1, 2.0);
        addToOpenMapRealMatrix(b, 2, 2, 3.0);

        OpenMapRealMatrix sum = a.add(b);
        assertEquals(a, sum);
    }

    @Test
    public void testOpenMapRealMatrixSubtractToSparseMatrix() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        addToOpenMapRealMatrix(a, 0, 0, 1.0);
        addToOpenMapRealMatrix(a, 1, 1, 2.0);
        addToOpenMapRealMatrix(a, 2, 2, 3.0);

        SparseRealMatrix b = new OpenMapRealMatrix(3, 3);
        addToOpenMapRealMatrix(b, 0, 0, 1.0);
        addToOpenMapRealMatrix(b, 1, 1, 2.0);
        addToOpenMapRealMatrix(b, 2, 2, 3.0);

        OpenMapRealMatrix difference = a.subtract(b);
        assertEquals(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{0.0, 0.0}, {0.0, 0.0}, {0.0, 0.0}})), difference);
    }

    private void addToOpenMapRealMatrix(OpenMapRealMatrix matrix, int row, int column, double value) {
        if (matrix.getEntry(row, column) == 0.0) {
            matrix.setEntry(row, column, value);
        } else {
            matrix.addToEntry(row, column, value);
        }
    }
}