package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixCreation() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNull(matrix.getEntry(0, 0));
        assertEquals(0, matrix.size());
    }

    @Test
    public void testOpenMapRealMatrixCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        assertEquals(original, copy);
    }

    @Test
    public void testOpenMapRealMatrixAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix sum = matrix1.add(matrix2);
        assertEquals(matrix1, sum);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrix1.setEntry(i, j, (double) i + j);
                matrix2.setEntry(i, j, (double) i - j);
            }
        }
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{3, 0, 3}, {0, -3, 0}, {3, 3, 3}});
        assertEquals(expected, sum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixAdditionDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.add(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix difference = matrix1.subtract(matrix2);
        assertEquals(matrix1, difference);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrix1.setEntry(i, j, (double) i + j);
                matrix2.setEntry(i, j, (double) i - j);
            }
        }
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{6, 0, 6}, {0, 0, 0}, {6, 6, 6}});
        assertEquals(expected, difference);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixSubtractionDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.subtract(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        RealMatrix product = matrix1.multiply(matrix2);
        assertEquals(product.getColumnDimension(), 3);
        assertEquals(product.getRowDimension(), 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrix1.setEntry(i, j, (double) i + j);
                matrix2.setEntry(j, i, (double) j + i);
            }
        }
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{15, 15, 15}, {15, 15, 15}, {15, 15, 15}});
        assertEquals(expected, product);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixMultiplicationDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.multiply(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(1, 1, 2);
        assertEquals(2, matrix.getEntry(1, 1), 0.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(3, 3, 2);
    }

    @Test
    public void testOpenMapRealMatrixAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(1, 1, 2);
        assertEquals(2, matrix.getEntry(1, 1), 0.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixAddToEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(3, 3, 2);
    }

}