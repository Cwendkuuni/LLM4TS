package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Tests for CholeskyDecompositionImpl.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test the constructor with a given positive definite matrix.
     */
    @Test
    public void testCholeskyDecompositionPositiveDefinite() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0, 1.0},
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 2.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][] {
            {Math.sqrt(2.0), 0.0, 0.0},
            {-0.5 * Math.sqrt(2.0), Math.sqrt(1.0), 0.0},
            {-0.5 * Math.sqrt(2.0), -0.5, Math.sqrt(2.0)}
        });
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][] {
            {Math.sqrt(2.0), -0.5 * Math.sqrt(2.0), -0.5 * Math.sqrt(2.0)},
            {0.0, Math.sqrt(1.0), -0.5},
            {0.0, 0.0, Math.sqrt(2.0)}
        });
        assertEquals("L is not correct.", expectedL, l);
        assertEquals("LT is not correct.", expectedLT, lt);

        RealVector b = new RealVectorImpl(new double[] {1.0, 0.5, 0.5});
        RealVector x = cholesky.getSolver().solve(b);
        RealVector expectedX = new RealVectorImpl(new double[] {-0.5, 1.0, 0.0});
        assertArrayEquals("X is not correct.", expectedX.getDataRef(), x.getDataRef());
    }

    /**
     * Test the constructor with a given non-positive definite matrix.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testCholeskyDecompositionNonPositiveDefinite() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0, 1.0},
            {1.0, -1.0, 1.0},
            {1.0, 1.0, 2.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the constructor with a given non-symmetric matrix.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testCholeskyDecompositionNonSymmetric() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0},
            {1.0, 3.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the determinant method with a given positive definite matrix.
     */
    @Test
    public void testDeterminantPositiveDefinite() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0, 1.0},
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 2.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        assertEquals("Determinant is not correct.", 2.0 * Math.pow(2.0, (double) (3.0 * (3.0 + 1.0) / 2.0)), cholesky.getDeterminant(), 1.0E-15);
    }

    /**
     * Test the determinant method with a given non-positive definite matrix.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testDeterminantNonPositiveDefinite() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0, 1.0},
            {1.0, -1.0, 1.0},
            {1.0, 1.0, 2.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the solve method with a given positive definite matrix and a vector.
     */
    @Test
    public void testSolvePositiveDefinite() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0, 1.0},
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 2.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealVector b = new RealVectorImpl(new double[] {1.0, 0.5, 0.5});
        RealVector x = cholesky.getSolver().solve(b);
        RealVector expectedX = new RealVectorImpl(new double[] {-0.5, 1.0, 0.0});
        assertArrayEquals("X is not correct.", expectedX.getDataRef(), x.getDataRef());
    }

    /**
     * Test the solve method with a given non-positive definite matrix and a vector.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testSolveNonPositiveDefinite() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0, 1.0},
            {1.0, -1.0, 1.0},
            {1.0, 1.0, 2.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealVector b = new RealVectorImpl(new double[] {1.0, 0.5, 0.5});
        cholesky.getSolver().solve(b);
    }

    /**
     * Test the solve method with a given non-symmetric matrix and a vector.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testSolveNonSymmetric() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0},
            {1.0, 3.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealVector b = new RealVectorImpl(new double[] {1.0, 0.5});
        cholesky.getSolver().solve(b);
    }

    /**
     * Test the Singular Value Decomposition method with a given positive definite matrix.
     */
    @Test
    public void testSingularValueDecompositionPositiveDefinite() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0, 1.0},
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 2.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        SingularValueDecomposition svd = cholesky.getSolver().getSvd();
        assertEquals("Singular values are not correct.", 2.0, svd.getSingularValues()[0], 1.0E-15);
        assertEquals("Singular values are not correct.", Math.sqrt(2.0), svd.getSingularValues()[1], 1.0E-15);
        assertEquals("Singular values are not correct.", Math.sqrt(2.0), svd.getSingularValues()[2], 1.0E-15);
    }

    /**
     * Test the Singular Value Decomposition method with a given non-positive definite matrix.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testSingularValueDecompositionNonPositiveDefinite() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0, 1.0},
            {1.0, -1.0, 1.0},
            {1.0, 1.0, 2.0}
        });
        new CholeskyDecompositionImpl(matrix).getSolver().getSvd();
    }

    /**
     * Test the Singular Value Decomposition method with a given non-symmetric matrix.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testSingularValueDecompositionNonSymmetric() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0},
            {1.0, 3.0}
        });
        new CholeskyDecompositionImpl(matrix).getSolver().getSvd();
    }

}