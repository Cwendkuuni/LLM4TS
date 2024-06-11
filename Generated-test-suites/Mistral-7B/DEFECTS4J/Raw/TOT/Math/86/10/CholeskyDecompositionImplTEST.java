package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CholeskyDecompositionImpl.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test the Cholesky decomposition of a positive definite matrix.
     */
    @Test
    public void testCholeskyDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix L = cholesky.getL();
        RealMatrix LT = cholesky.getLT();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.707106781186548, 0.408248290463863, 0.0},
            {-0.408248290463863, 0.408248290463863, 0.316227766016838},
        });
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.707106781186548, -0.408248290463863},
            {0.0, 0.408248290463863, 0.408248290463863},
            {0.0, 0.0, 0.316227766016838}
        });
        assertEquals(expectedL, L);
        assertEquals(expectedLT, LT);
    }

    /**
     * Test the Cholesky decomposition of a singular matrix.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.0},
            {0.0, 0.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the Cholesky decomposition of a non-symmetric matrix.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNonSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the Cholesky decomposition of a matrix with incorrect input thresholds.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testIncorrectThresholds() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0},
            {2.0, 1.0}
        });
        new CholeskyDecompositionImpl(matrix, 0.0, 0.0);
    }

    /**
     * Test the solving of a linear equation using the Cholesky decomposition.
     */
    @Test
    public void testSolveLinearEquation() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0},
            {2.0, 5.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix L = cholesky.getL();
        RealVector b = new ArrayRealVector(new double[]{1.0, 3.0});
        RealVector x = cholesky.getSolver().solve(b);
        RealVector expectedX = new ArrayRealVector(new double[]{1.5, 0.5});
        assertEquals(expectedX, x);
    }

    /**
     * Test the inverse of a matrix using the Cholesky decomposition.
     */
    @Test
    public void testInverseMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0},
            {2.0, 5.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix L = cholesky.getL();
        RealMatrix expectedInv = new Array2DRowRealMatrix(new double[][]{
            {0.5, -0.2},
            {-0.2, 0.2}
        });
        RealMatrix inv = cholesky.getSolver().getInverse();
        assertEquals(expectedInv, inv);
    }
}