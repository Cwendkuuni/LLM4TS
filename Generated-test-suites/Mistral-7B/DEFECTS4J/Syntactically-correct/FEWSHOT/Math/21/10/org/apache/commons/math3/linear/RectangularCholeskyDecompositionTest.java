package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;

import java.util.Random;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDelegation() {
        RealMatrix matrix = getRandomSymmetricPositiveSemidefiniteMatrix();
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            assertEquals(matrix.getRowDimension(), root.getRowDimension());
            assertEquals(matrix.getColumnDimension(), root.getColumnDimension());
            assertEquals(rank, root.getColumnDimension());
            for (int i = 0; i < rank; ++i) {
                for (int j = 0; j < rank; ++j) {
                    double expected = matrix.getEntry(i, i);
                    double actual = root.getEntry(i, j);
                    assertEquals(expected, actual, 1e-10);
                }
            }
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = Array2DRowRealMatrix.identity(10);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            assertEquals(matrix.getRowDimension(), root.getRowDimension());
            assertEquals(matrix.getColumnDimension(), root.getColumnDimension());
            assertEquals(matrix.getRowDimension(), rank);
            for (int i = 0; i < rank; ++i) {
                for (int j = 0; j < rank; ++j) {
                    double expected = 0.0;
                    if (i == j) {
                        expected = 1.0;
                    }
                    double actual = root.getEntry(i, j);
                    assertEquals(expected, actual, 1e-10);
                }
            }
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

    @Test
    public void testPositiveDefiniteMatrixThrowException() {
        Random random = new Random();
        double[][] matrixData = new double[5][5];
        for (int i = 0; i < matrixData.length; ++i) {
            for (int j = 0; j < matrixData[i].length; ++j) {
                matrixData[i][j] = random.nextDouble();
            }
        }
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testRandomMatrix() {
        Random random = new Random();
        double small = 1e-10;
        int order = 5;
        double[][] matrixData = new double[order][order];
        for (int i = 0; i < order; ++i) {
            for (int j = 0; j < order; ++j) {
                matrixData[i][j] = random.nextGaussian();
            }
        }
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            assertEquals(matrix.getRowDimension(), root.getRowDimension());
            assertEquals(matrix.getColumnDimension(), root.getColumnDimension());
            assertEquals(rank, root.getColumnDimension());
            for (int i = 0; i < rank; ++i) {
                for (int j = 0; j < rank; ++j) {
                    double expected = matrix.getEntry(i, i);
                    double actual = root.getEntry(i, j);
                    double error = Precision.abs(expected - actual);
                    assertTrue("Expected difference to be smaller than threshold: " + error + " > " + small, error < small);
                }
            }
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

}