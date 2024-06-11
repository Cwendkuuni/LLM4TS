package org.apache.commons.math.linear.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SolverCondition;
import org.apache.commons.math3.special.Erf;

/**
 * Test for CholeskyDecompositionImpl class.
 */
public class CholeskyDecompositionImplTest extends TestCase {

    /**
     * Test Cholesky decomposition of a symmetric positive definite matrix.
     */
    public void testCholeskyDecomposition() {
        RealMatrix A = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0, 1.0},
            {1.0, 3.0, 2.0},
            {1.0, 2.0, 5.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(A);
        RealMatrix L = cholesky.getL();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][] {
            {Math.sqrt(2.0), 0.0, 0.0},
            {0.5, Math.sqrt(3.0), 0.0},
            {0.5, 1.0, Math.sqrt(5.0)}
        });
        assertEquals(expectedL, L);
        RealMatrix LT = cholesky.getLT();
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][] {
            {Math.sqrt(2.0), 0.5, 0.5},
            {0.5 * 0.5, Math.sqrt(3.0), 1.0},
            {0.5 * 0.5, 1.0, Math.sqrt(5.0)}
        });
        assertEquals(expectedLT, LT);
    }

    /**
     * Test Cholesky decomposition of a square non-symmetric matrix.
     */
    public void testCholeskyDecompositionNonSymmetric() {
        RealMatrix A = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0},
            {1.0, 3.0}
        });
        try {
            new CholeskyDecompositionImpl(A);
            fail("Expected NotSymmetricMatrixException");
        } catch (NotSymmetricMatrixException e) {
            // expected
        }
    }

    /**
     * Test Cholesky decomposition of a non-square matrix.
     */
    public void testCholeskyDecompositionNonSquare() {
        RealMatrix A = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0},
            {1.0, 3.0},
            {1.0, 2.0}
        });
        try {
            new CholeskyDecompositionImpl(A);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // expected
        }
    }

    /**
     * Test Cholesky decomposition of a singular matrix.
     */
    public void testCholeskyDecompositionSingular() {
        RealMatrix A = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0},
            {1.0, 2.0}
        });
        try {
            new CholeskyDecompositionImpl(A);
            fail("Expected NotPositiveDefiniteMatrixException");
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    /**
     * Test Cholesky decomposition with custom thresholds.
     */
    public void testCholeskyDecompositionCustomThresholds() {
        RealMatrix A = new Array2DRowRealMatrix(new double[][] {
            {1e-6, 1e-6},
            {1e-6, 1e-6}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(A, 1.0e-10, 1.0e-15);
        RealMatrix L = cholesky.getL();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.0},
            {0.0, 1.0}
        });
        assertEquals(expectedL, L);
        RealMatrix LT = cholesky.getLT();
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.0},
            {0.0, 1.0}
        });
        assertEquals(expectedLT, LT);
    }

    /**
     * Test Cholesky decomposition with large matrix.
     */
    public void testCholeskyDecompositionLargeMatrix() {
        RealMatrix A = MatrixUtils.randomRealMatrix(1000, 1000);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(A);
        RealMatrix L = cholesky.getL();
        RealMatrix LT = cholesky.getLT();
        Solver solver = cholesky.getSolver();
        RealVector b = new ArrayRealVector(new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0});
        RealVector x = solver.solve(b);
        RealVector y = MatrixUtils.createColumnRealVector(new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0});
        RealVector z = L.operate(x);
        RealVector w = LT.operate(z);
        RealVector v = MatrixUtils.createColumnRealVector(new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0});
        double tol = 1.0e-6;
        for (int i = 0; i < 1000; i++) {
            double diff = Math.abs(w.getEntry(i) - v.getEntry(i));
            Assert.assertTrue("Expected equal for index " + i, diff <= tol);
        }
    }

    /**
     * Test Cholesky decomposition with large matrix using SingularValueDecomposition instead.
     */
    public void testCholeskyDecompositionLargeMatrixSingularValueDecomposition() {
        RealMatrix A = MatrixUtils.randomRealMatrix(1000, 1000);
        SingularValueDecomposition svd = new SingularValueDecomposition(A, SolverCondition.LU);
        RealMatrix L = svd.getL();
        RealMatrix LT = L.transpose();
        Solver solver = new CholeskyDecompositionImpl(A.getData()).getSolver();
        RealVector b = new ArrayRealVector(new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0});
        RealVector x = solver.solve(b);
        RealVector y = MatrixUtils.createColumnRealVector(new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0});
        RealVector z = L.operate(x);
        RealVector w = LT.operate(z);
        RealVector v = y;
        double tol = 1.0e-6;
        for (int i = 0; i < 1000; i++) {
            double diff = Math.abs(w.getEntry(i) - v.getEntry(i));
            Assert.assertTrue("Expected equal for index " + i, diff <= tol);
        }
    }

}