package org.apache.commons.math.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.MatrixUtils;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixConstruction() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(5, 5);
        assertEquals(5, matrix.getRowDimension());
        assertEquals(5, matrix.getColumnDimension());
        assertTrue(matrix.entries.isEmpty());
    }

    @Test
    public void testOpenMapRealMatrixCopyConstruction() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(3, 3);
        original.setEntry(1, 1, 1.0);
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
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

    @Test
    public void testOpenMapRealMatrixAddition() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 3);
        m1.setEntry(1, 1, 1.0);
        m1.setEntry(1, 2, 2.0);
        m2.setEntry(1, 1, 3.0);
        m2.setEntry(1, 2, 4.0);

        OpenMapRealMatrix sum = m1.add(m2);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{4.0, 6.0}, {1.0, 6.0}, {1.0, 6.0}});

        assertEquals(expected.getRowDimension(), sum.getRowDimension());
        assertEquals(expected.getColumnDimension(), sum.getColumnDimension());
        for (int row = 0; row < sum.getRowDimension(); row++) {
            for (int column = 0; column < sum.getColumnDimension(); column++) {
                assertEquals(expected.getEntry(row, column), sum.getEntry(row, column), 0.0);
            }
        }
    }

    @Test
    public void testOpenMapRealMatrixSubtraction() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 3);
        m1.setEntry(1, 1, 1.0);
        m1.setEntry(1, 2, 2.0);
        m2.setEntry(1, 1, 3.0);
        m2.setEntry(1, 2, 4.0);

        OpenMapRealMatrix difference = m1.subtract(m2);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{-2.0, -2.0}, {0.0, -2.0}, {0.0, -2.0}});

        assertEquals(expected.getRowDimension(), difference.getRowDimension());
        assertEquals(expected.getColumnDimension(), difference.getColumnDimension());
        for (int row = 0; row < difference.getRowDimension(); row++) {
            for (int column = 0; column < difference.getColumnDimension(); column++) {
                assertEquals(expected.getEntry(row, column), difference.getEntry(row, column), 0.0);
            }
        }
    }

    @Test
    public void testOpenMapRealMatrixMultiplication() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 3);
        m1.setEntry(1, 1, 1.0);
        m1.setEntry(1, 2, 2.0);
        m1.setEntry(2, 1, 3.0);
        m1.setEntry(2, 2, 4.0);
        m2.setEntry(1, 1, 1.0);
        m2.setEntry(1, 2, 2.0);
        m2.setEntry(2, 1, 3.0);
        m2.setEntry(2, 2, 4.0);

        RealMatrix product = m1.multiply(m2);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {6.0, 10.0},
            {15.0, 22.0},
            {15.0, 22.0}});

        assertEquals(expected.getRowDimension(), product.getRowDimension());
        assertEquals(expected.getColumnDimension(), product.getColumnDimension());
        for (int row = 0; row < product.getRowDimension(); row++) {
            for (int column = 0; column < product.getColumnDimension(); column++) {
                assertEquals(expected.getEntry(row, column), product.getEntry(row, column), 0.0);
            }
        }
    }

    @Test
    public void testOpenMapRealMatrixSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(5, 5);
        matrix.setEntry(1, 1, 1.0);
        assertEquals(1.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixSetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(5, 5);
        matrix.setEntry(5, 5, 1.0);
    }

    @Test
    public void testOpenMapRealMatrixGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(5, 5);
        matrix.setEntry(1, 1, 1.0);
        assertEquals(1.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixGetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(5, 5);
        matrix.setEntry(1, 1, 1.0);
        matrix.getEntry(5, 5);
    }

    @Test
    public void testOpenMapRealMatrixAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(5, 5);
        matrix.addToEntry(1, 1, 1.0);
        assertEquals(1.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testOpenMapRealMatrixMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(5, 5);
        matrix.multiplyEntry(1, 1, 2.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testOpenMapRealMatrixGetRow() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(5, 5);
        RealVector row = matrix.getColumnVector(1);
        row.setEntry(2, 1.0);
        assertEquals(1.0, matrix.getEntry(2, 1), 0.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixGetRowOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(5, 5);
        matrix.getRow(5);
    }

    @Test
    public void testOpenMapRealMatrixGetColumn() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(5, 5);
        RealVector column = matrix.getColumnVector(1);
        column.setEntry(2, 1.0);
        assertEquals(1.0, matrix.getEntry(1, 2), 0.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixGetColumnOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(5, 5);
        matrix.getColumn(5);
    }

    @Test
    public void testOpenMapRealMatrixTranspose() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 1.0);
        matrix.setEntry(1, 2, 2.0);
        matrix.setEntry(2, 1, 3.0);
        matrix.setEntry(2, 2, 4.0);

        OpenMapRealMatrix transpose = matrix.transpose();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0, 3.0},
            {2.0, 4.0},
            {1.0, 3.0}});

        assertEquals(expected.getRowDimension(), transpose.getRowDimension());
        assertEquals(expected.getColumnDimension(), transpose.getColumnDimension());
        for (int row = 0; row < transpose.getRowDimension(); row++) {
            for (int column = 0; column < transpose.getColumnDimension(); column++) {
                assertEquals(expected.getEntry(row, column), transpose.getEntry(column, row), 0.0);
            }
        }
    }

    @Test
    public void testOpenMapRealMatrixEquals() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 3);
        m1.setEntry(1, 1, 1.0);
        m1.setEntry(1, 2, 2.0);
        m1.setEntry(2, 1, 3.0);
        m1.setEntry(2, 2, 4.0);
        m2.setEntry(1, 1, 1.0);
        m2.setEntry(1, 2, 2.0);
        m2.setEntry(2, 1, 3.0);
        m2.setEntry(2, 2, 4.0);

        assertEquals(m1, m2);
    }

    @Test
    public void testOpenMapRealMatrixNotEquals() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 3);
        m1.setEntry(1, 1, 1.0);
        m1.setEntry(1, 2, 2.0);
        m1.setEntry(2, 1, 3.0);
        m1.setEntry(2, 2, 4.0);
        m2.setEntry(1, 1, 2.0);
        m2.setEntry(1, 2, 3.0);
        m2.setEntry(2, 1, 4.0);
        m2.setEntry(2, 2, 5.0);

        assertNotEquals(m1, m2);
    }

    @Test
    public void testOpenMapRealMatrixToString() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 1.0);
        matrix.setEntry(1, 2, 2.0);
        matrix.setEntry(2, 1, 3.0);
        matrix.setEntry(2, 2, 4.0);

        String output = matrix.toString();
        String expected = "OpenMapRealMatrix[\n" +
                "  [1.0, 2.0],\n" +
                "  [3.0, 4.0],\n" +
                "  [1.0, 2.0]],\n" +
                "rows = 3,\n" +
                "columns = 3\n" +
                "]";

        assertEquals(expected, output);
    }
}