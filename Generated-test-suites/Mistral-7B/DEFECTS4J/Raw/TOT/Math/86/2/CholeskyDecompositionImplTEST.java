package org.apache.commons.math.linear.test;

import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for CholeskyDecompositionImpl class.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test Cholesky decomposition of a positive definite matrix.
     */
    @Test
    public void testPositiveDefinite() {
        final double[][] input = {
            {3.0, 2.0, 0.0},
            {2.0, 5.0, 2.0},
            {0.0, 2.0, 4.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(input);
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix L = choleskyDecomposition.getL();
        RealMatrix LT = choleskyDecomposition.getLT();
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][]{{1.0/Math.sqrt(3.0), 0.0, -1.0/Math.sqrt(3.0)},
                                                                           {-1.0/Math.sqrt(5.0), 1.0/Math.sqrt(5.0), 0.0},
                                                                           {-1.0/Math.sqrt(4.0), -1.0/2.0, 1.0/Math.sqrt(4.0)}});
        assertEquals("L^T does not match expected value", expectedLT, LT);
        assertEquals("Determinant does not match expected value", 24.0, choleskyDecomposition.getDeterminant(), 0.0);
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        RealVector b = new RealVectorImpl(new double[]{1.0, 2.0, 3.0});
        RealVector x = solver.solve(b);
        assertEquals("Solution does not match expected value", new double[]{-0.154948974278317, 0.432494422781247, 0.841619366812594}, x.toArray(), 0.001);
    }

    /**
     * Test Cholesky decomposition of a negative definite matrix.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNegativeDefinite() {
        final double[][] input = {
            {3.0, 2.0, 0.0},
            {2.0, -5.0, 2.0},
            {0.0, 2.0, 4.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(input);
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test Cholesky decomposition of a non-symmetric matrix.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNonSymmetric() {
        final double[][] input = {
            {3.0, 2.0},
            {2.0, 5.0},
            {0.0, 2.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(input);
        new CholeskyDecompositionImpl(matrix, 0.0, 0.0);
    }

    /**
     * Test Cholesky decomposition of a non-square matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquare() {
        final double[][] input = {
            {3.0},
            {2.0, 5.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(input);
        new CholeskyDecompositionImpl(matrix);
    }

}