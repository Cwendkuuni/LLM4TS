package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.commons.math.linear.OpenMapRealMatrix;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixCreation() {
        final int rows = 2;
        final int columns = 3;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rows, columns);
        assertEquals(rows, matrix.getRowDimension());
        assertEquals(columns, matrix.getColumnDimension());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixNegativeDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(-1, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixZeroColumns() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 0);
    }

    @Test
    public void testOpenMapRealMatrixCopy() {
        final int rows = 2;
        final int columns = 3;
        OpenMapRealMatrix original = new OpenMapRealMatrix(rows, columns);
        OpenMapRealMatrix copy = original.copy();
        assertEquals(original, copy);
    }

    @Test
    public void testOpenMapRealMatrixAdd() {
        final int rows = 2;
        final int columns = 3;
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(rows, columns);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix1.setEntry(i, j, i + j);
                matrix2.setEntry(i, j, i - j);
            }
        }
        OpenMapRealMatrix sum = matrix1.add(matrix2);
        RealMatrix expectedSum = new Array2DRowRealMatrix(new double[][]{{1, 1, 1}, {2, 2, 2}});
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                assertEquals(expectedSum.getEntry(i, j), sum.getEntry(i, j), 1e-10);
            }
        }
    }

    @Test
    public void testOpenMapRealMatrixSubtract() {
        final int rows = 2;
        final int columns = 3;
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(rows, columns);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix1.setEntry(i, j, i + j);
                matrix2.setEntry(i, j, i - j);
            }
        }
        OpenMapRealMatrix difference = matrix1.subtract(matrix2);
        RealMatrix expectedDifference = new Array2DRowRealMatrix(new double[][]{{2, 2, 2}, {2, 2, 2}});
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                assertEquals(expectedDifference.getEntry(i, j), difference.getEntry(i, j), 1e-10);
            }
        }
    }

    @Test
    public void testOpenMapRealMatrixMultiply() {
        final int rows1 = 2;
        final int columns1 = 3;
        final int rows2 = 3;
        final int columns2 = 4;
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(rows1, columns1);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(rows2, columns2);
        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < columns1; j++) {
                matrix1.setEntry(i, j, i + j);
            }
        }
        for (int i = 0; i < rows2; i++) {
            for (int j = 0; j < columns2; j++) {
                matrix2.setEntry(i, j, j + i);
            }
        }
        OpenMapRealMatrix product = matrix1.multiply(matrix2);
        RealMatrix expectedProduct = new Array2DRowRealMatrix(new double[][]{{3*2, 4*2, 5*2}, {4*2, 5*2, 6*2}});
        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < columns2; j++) {
                assertEquals(expectedProduct.getEntry(i, j), product.getEntry(i, j), 1e-10);
            }
        }
    }

    @Test
    public void testOpenMapRealMatrixSetEntry() {
        final int rows = 2;
        final int columns = 3;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix.setEntry(i, j, i + j);
            }
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                assertEquals(i + j, matrix.getEntry(i, j), 1e-10);
            }
        }
    }

    @Test
    public void testOpenMapRealMatrixAddToEntry() {
        final int rows = 2;
        final int columns = 3;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix.setEntry(i, j, i + j);
            }
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix.addToEntry(i, j, 1.0);
                assertEquals(i + j + 1.0, matrix.getEntry(i, j), 1e-10);
            }
        }
    }

    @Test
    public void testOpenMapRealMatrixMultiplyEntry() {
        final int rows = 2;
        final int columns = 3;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix.setEntry(i, j, i + j);
            }
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix.multiplyEntry(i, j, 2.0);
                assertEquals(2 * (i + j), matrix.getEntry(i, j), 1e-10);
            }
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testOpenMapRealMatrixSetEntryOutOfBounds() {
        final int rows = 2;
        final int columns = 3;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rows, columns);
        matrix.setEntry(rows, columns, Double.POSITIVE_INFINITY);
    }

    @Test
    public void testOpenMapRealMatrixGetEntryNull() {
        final int rows = 2;
        final int columns = 3;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rows, columns);
        assertNull(matrix.getEntry(rows, columns));
    }

    @Test
    public void testOpenMapRealMatrixComputeKey() {
        final int rows = 2;
        final int columns = 3;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int key = OpenMapRealMatrix.this.computeKey(i, j);
                int expectedKey = i * columns + j;
                assertEquals(expectedKey, key);
            }
        }
    }
}