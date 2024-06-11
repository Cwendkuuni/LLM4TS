package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.VectorUtils;
import org.apache.commons.math3.util.Precision;

import java.util.Random;

public class RectangularCholeskyDecompositionTest {

    @org.junit.Test
    public void testRectangularCholeskyDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.2}, {0.1, 1.0, 0.3}, {0.2, 0.3, 1.0}});
        double small = 1e-7;

        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = rcd.getRootMatrix();
        int rank = rcd.getRank();
        assertEquals(rank, 3);
        assertEquals(root.getRowDimension(), 3);
        assertEquals(root.getColumnDimension(), rank);

        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{0.99999999999999997, -0.04263308909092956, 0.0},
                {-0.04263308909092956, 1.0000000000000001, -0.0772973002210583},
                {0.0, -0.0772973002210583, 1.0}});
        assertArrayEquals(expectedRoot.getData(), root.getData(), 1e-10);
    }

    @org.junit.Test
    public void testRectangularCholeskyDecompositionSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.2}, {0.1, 1.0, 0.3}, {0.2, 0.3, 0.0}});
        double small = 1e-7;

        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, small));
    }

    @org.junit.Test
    public void testRectangularCholeskyDecompositionNegativeMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.2}, {0.1, 1.0, 0.3}, {0.2, 0.3, -1.0}});
        double small = 1e-7;

        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, small));
    }

    @org.junit.Test
    public void testRectangularCholeskyDecompositionIdentityMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        double small = 1e-14;

        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = rcd.getRootMatrix();
        int rank = rcd.getRank();
        assertEquals(rank, 2);
        assertEquals(root.getRowDimension(), 2);
        assertEquals(root.getColumnDimension(), rank);

        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        assertArrayEquals(expectedRoot.getData(), root.getData(), 1e-14);
    }

    @org.junit.Test
    public void testRectangularCholeskyDecompositionEmptyMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{}});
        double small = 1e-7;

        assertThrows(IllegalArgumentException.class, () -> new RectangularCholeskyDecomposition(matrix, small));
    }

    @org.junit.Test
    public void testRectangularCholeskyDecompositionDiagonalMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}, {0.0, 0.0}});
        double small = 1e-14;

        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = rcd.getRootMatrix();
        int rank = rcd.getRank();
        assertEquals(rank, 3);
        assertEquals(root.getRowDimension(), 3);
        assertEquals(root.getColumnDimension(), rank);

        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        assertArrayEquals(expectedRoot.getData(), root.getData(), 1e-14);
    }

    @org.junit.Test
    public void testRectangularCholeskyDecompositionSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1}, {0.1, 1.0}});
        double small = 1e-14;

        assertThrows(IllegalArgumentException.class, () -> new RectangularCholeskyDecomposition(matrix, small));
    }

    @org.junit.Test
    public void testRectangularCholeskyDecompositionRankDeficientMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.2}, {0.1, 1.0, 0.3}, {0.2, 0.3, 0.0}});
        double small = 1e-7;

        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, small));

        // Checking the SVD of the matrix against the Cholesky decomposition
        RealMatrix originalMatrix = matrix.copy();
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix vt = svd.getVT();

        RealMatrix expectedCholesky = new Array2DRowRealMatrix(new double[][]{{1.0 / Precision.sqrt(s.getEntry(0, 0)), -s.getEntry(1, 0) / (2 * s.getEntry(0, 0)), s.getEntry(2, 0) / (2 * s.getEntry(0, 0))},
                {-s.getEntry(1, 0) / (2 * s.getEntry(0, 0)), 1.0 / Precision.sqrt(s.getEntry(1, 1)), -s.getEntry(2, 1) / (2 * s.getEntry(1, 1))},
                {s.getEntry(2, 0) / (2 * s.getEntry(0, 0)), -s.getEntry(2, 1) / (2 * s.getEntry(1, 1)), 1.0 / Precision.sqrt(s.getEntry(2, 2))}});

        RealMatrix cholesky = new RectangularCholeskyDecomposition(originalMatrix, 1e-14).getRootMatrix();
        assertArrayEquals(expectedCholesky.getData(), cholesky.getData(), 1e-10);
    }

    @org.junit.Test
    public void testRectangularCholeskyDecompositionRandomMatrix() {
        Random random = new Random();
        int rows = 10;
        int columns = 5;
        int maxIterations = 100;
        double epsilon = 1e-6;

        for (int i = 0; i < 10; ++i) {
            double[][] randomMatrix = new double[rows][columns];
            for (int j = 0; j < rows; ++j) {
                for (int k = 0; k < columns; ++k) {
                    randomMatrix[j][k] = random.nextGaussian();
                }
            }

            RealMatrix matrix = new Array2DRowRealMatrix(randomMatrix);
            double small = Precision.sqrt(epsilon);

            try {
                RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
                RealMatrix root = rcd.getRootMatrix();
                int rank = rcd.getRank();

                RealVector sv = VectorUtils.createRealVector(VectorUtils.getColumnVector(MatrixUtils.transpose(rcd.getRootMatrix()), 0));
                RealVector svFromSVD = new SingularValueDecomposition(matrix).getS().getColumnVector(0);

                for (int j = 0; j < rank; ++j) {
                    assertEquals(Precision.round(sv.getEntry(j), epsilon), Precision.round(svFromSVD.getEntry(j), epsilon), epsilon);
                }

                for (int j = rank; j < sv.getDimension(); ++j) {
                    assertEquals(0.0, sv.getEntry(j), epsilon);
                }

                assertTrue(isClose(rcd.getRootMatrix().multiply(MatrixUtils.transpose(rcd.getRootMatrix())), matrix, epsilon));

            } catch (NonPositiveDefiniteMatrixException e) {
                // Matrix is not positive semidefinite, try another one
            }

        }
    }

    private boolean isClose(RealMatrix a, RealMatrix b, double epsilon) {
        for (int i = 0; i < a.getRowDimension(); ++i) {
            for (int j = 0; j < a.getColumnDimension(); ++j) {
                if (Math.abs(a.getEntry(i, j) - b.getEntry(i, j)) > epsilon) {
                    return false;
                }
            }
        }
        return true;
    }
}