package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SolverType;
import org.apache.commons.math3.linear.MatrixUtils;

import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecompositionImpl_Default() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            { 2.0, 1.0, 1.0 },
            { 1.0, 1.0, 1.0 },
            { 1.0, 1.0, 2.0 }
        });

        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][] {
            { 1.41421356, 0.0, 0.0 },
            { 0.70710678, 1.41421356, 0.0 },
            { 0.0, 0.70710678, 1.41421356 }
        });
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][] {
            { 1.41421356, 0.70710678, 0.0 },
            { 0.0, 1.41421356, 0.70710678 },
            { 0.0, 0.0, 1.41421356 }
        });

        assertEquals("L is not correct", expectedL, l);
        assertEquals("LT is not correct", expectedLT, lt);
    }

    @Test
    public void testCholeskyDecompositionImpl_CustomThresholds() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            { 0.0001, 1.0, 1.0 },
            { 1.0, 0.0001, 1.0 },
            { 1.0, 1.0, 0.0001 }
        });

        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1e-8, 1e-15);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][] {
            { 0.001, 0.0, 0.0 },
            { 0.0, 0.001, 0.0 },
            { 0.0, 0.0, 0.001 }
        });
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][] {
            { 0.001, 0.0, 0.0 },
            { 0.0, 0.001, 0.0 },
            { 0.0, 0.0, 0.001 }
        });

        assertEquals("L is not correct", expectedL, l);
        assertEquals("LT is not correct", expectedLT, lt);
    }

    @Test(expected = NotSquareMatrixException.class)
    public void testCholeskyDecompositionImpl_NonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            { 2.0, 1.0 },
            { 1.0, 1.0 },
            { 1.0, 1.0 }
        });

        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testCholeskyDecompositionImpl_NotSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            { 2.0, 1.0 },
            { 1.0, 2.0 }
        });

        try {
            new CholeskyDecompositionImpl(matrix);
            fail("Should have thrown exception");
        } catch (NotSymmetricMatrixException e) {
            // expected
        }
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testCholeskyDecompositionImpl_NotPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            { 1.0, 1.0 },
            { 1.0, 0.0 }
        });

        try {
            new CholeskyDecompositionImpl(matrix);
            fail("Should have thrown exception");
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            { 2.0, 1.0, 1.0 },
            { 1.0, 1.0, 1.0 },
            { 1.0, 1.0, 2.0 }
        });

        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        double determinant = cholesky.getDeterminant();
        assertEquals("Determinant is not correct", 2.0 * 1.0 * 1.0 * 1.0 * 1.0 * 1.0 * 1.0 * 1.0, determinant, 1e-14);
    }

    @Test
    public void testGetSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            { 2.0, 1.0, 1.0 },
            { 1.0, 1.0, 1.0 },
            { 1.0, 1.0, 2.0 }
        });

        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = cholesky.getSolver();

        RealVector b = new ArrayRealVector(new double[] { 1.0, 2.0, 3.0 });
        RealVector x = solver.solve(b);

        RealVector expectedX = new ArrayRealVector(new double[] { 1.0 / 2.0, 1.0 / 2.0, 1.0 / 2.0 });
        assertEquals("Solution is not correct", expectedX, x);

        RealMatrix A = new Array2DRowRealMatrix(new double[][] {
            { 2.0, 1.0, 1.0 },
            { 1.0, 1.0, 1.0 },
            { 1.0, 1.0, 2.0 }
        });
        RealVector y = new ArrayRealVector(new double[] { 1.0, 0.0, -1.0 });
        RealVector expectedY = new ArrayRealVector(new double[] { 1.0, 0.0, -1.0 });
        RealVector xPrime = solver.solve(y);
        assertEquals("Solution of A'x = y is not correct", expectedY, xPrime);

        RealMatrix AInv = new SingularValueDecomposition(A, SolverType.DIAGONAL_DECOMPOSITION).getSolver().getInverse();
        RealVector yPrime = AInv.operate(xPrime);
        assertEquals("Solution of Ax = x' is not correct", b, yPrime);
    }
}