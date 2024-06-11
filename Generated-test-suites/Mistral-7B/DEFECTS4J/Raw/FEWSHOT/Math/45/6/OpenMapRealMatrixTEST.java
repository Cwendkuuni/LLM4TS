package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math.linear.*;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixConstruction() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertTrue(matrix.isEmpty());
    }

    @Test
    public void testOpenMapRealMatrixCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        OpenMapRealMatrix copy = matrix.copy();

        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                assertEquals(matrix.getEntry(i, j), copy.getEntry(i, j), 0.001);
            }
        }
    }

    @Test
    public void testOpenMapRealMatrixEntryAccess() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.001);
        assertEquals(3.0, matrix.getEntry(2, 2), 0.001);
    }

    @Test
    public void testOpenMapRealMatrixEntryModification() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixEntryAccessOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(3, 3, 1.0);
    }

    @Test
    public void testOpenMapRealMatrixAdd() {
        OpenMapRealMatrix A = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix B = new OpenMapRealMatrix(3, 3);

        A.setEntry(0, 0, 1.0);
        A.setEntry(1, 1, 2.0);
        A.setEntry(2, 2, 3.0);

        B.setEntry(0, 0, 1.0);
        B.setEntry(1, 1, 2.0);
        B.setEntry(2, 2, 3.0);

        OpenMapRealMatrix C = A.add(B);

        for (int i = 0; i < A.getRowDimension(); i++) {
            for (int j = 0; j < A.getColumnDimension(); j++) {
                assertEquals(A.getEntry(i, j) + B.getEntry(i, j), C.getEntry(i, j), 0.001);
            }
        }
    }

    @Test
    public void testOpenMapRealMatrixSubtract() {
        OpenMapRealMatrix A = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix B = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix C = new OpenMapRealMatrix(3, 3);

        A.setEntry(0, 0, 1.0);
        A.setEntry(1, 1, 2.0);
        A.setEntry(2, 2, 3.0);

        B.setEntry(0, 0, 1.0);
        B.setEntry(1, 1, 2.0);
        B.setEntry(2, 2, -3.0);

        C = A.subtract(B);

        for (int i = 0; i < A.getRowDimension(); i++) {
            for (int j = 0; j < A.getColumnDimension(); j++) {
                assertEquals(A.getEntry(i, j) -
                        B.getEntry(i, j), C.getEntry(i, j), 0.001);
            }
        }
    }

    @Test
    public void testOpenMapRealMatrixMultiply() {
        OpenMapRealMatrix A = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix B = new OpenMapRealMatrix(3, 3);
        RealMatrix C = new Array2DRowRealMatrix(new double[3][3]);

        A.setEntry(0, 0, 1.0);
        A.setEntry(1, 1, 2.0);
        A.setEntry(2, 2, 3.0);

        B.setEntry(0, 0, 1.0);
        B.setEntry(0, 1, 2.0);
        B.setEntry(0, 2, 3.0);
        B.setEntry(1,

0, 4.0);
        B.setEntry(1, 1, 5.0);
        B.setEntry(1, 2, 6.0);
        B.setEntry(2, 0, 7.0);
        B.setEntry(2, 1, 8.0);
        B.setEntry(2, 2, 9.0);

        C = A.multiply(B);

        for (int i = 0; i < A.getRowDimension(); i++) {
            for (int j = 0; j < B.getColumnDimension(); j++) {
                int key = A.computeKey(i, j);
                int keyB = B.computeKey(j, i);
                double sum = 0.0;
                for (int k = 0; k < A.getColumnDimension(); k++) {
                    sum += A.getEntry(i, k) * B.getEntry(k, j);
                }
                assertEquals(sum, C.getEntry(i, j), 0.001);
            }
        }
    }

    @Test
    public void testOpenMapRealMatrixMultiplyByScalar() {
        OpenMapRealMatrix A = new OpenMapRealMatrix(3, 3);
        double scalar = 2.0;

        A.setEntry(0, 0, 1.0);
        A.setEntry(1, 1, 2.0);
        A.setEntry(2, 2, 3.0);

        for (int i = 0; i < A.getRowDimension(); i++) {
            for (int j = 0; j < A.getColumnDimension(); j++) {
                A.multiplyEntry(i, j, scalar);
            }
        }

        for (int i = 0; i < A.getRowDimension(); i++) {
            for (int j = 0; j < A.getColumnDimension(); j++) {
                assertEquals(A.getEntry(i, j) * scalar,
                        A.getEntry(i, j), 0.001);
            }
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testOpenMapRealMatrixMultiplyByTooLargeScalar() {
        OpenMapRealMatrix A = new OpenMapRealMatrix(3, 3);
        double scalar = 2.0E308;

        A.setEntry(0, 0, 1.0);
        A.setEntry(1, 1, 2.0);
        A.setEntry(2, 2, 3.0);

        for (int i = 0; i < A.getRowDimension(); i++) {
            for (int j = 0; j < A.getColumnDimension(); j++) {
                A.multiplyEntry(i, j, scalar);
            }
        }
    }

}