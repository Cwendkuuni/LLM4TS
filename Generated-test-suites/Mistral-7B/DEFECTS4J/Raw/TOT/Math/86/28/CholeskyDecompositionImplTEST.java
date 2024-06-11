package org.apache.commons.math.linear.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.linear.NotSymmetricMatrixException;
import org.apache.commons.math3.linear.NotPositiveDefiniteMatrixException;

import org.apache.commons.math3.exception.MathRuntimeException;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    /**
     * Test the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        try {
            RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                { 1, 2 },
                { 2, 1 }
            });
            CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
            assertNotNull(cholesky.getL());
            assertNotNull(cholesky.getLT());
        } catch (NonSquareMatrixException | NotSymmetricMatrixException e) {
            // expected
        }
    }

    /**
     * Test the constructor with a non-square matrix.
     */
    @Test
    public void testNonSquareConstructor() {
        try {
            RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                { 1, 2 },
                { 2 },
                { 3 }
            });
            CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
            assertThrows(NonSquareMatrixException.class, () -> cholesky.getL());
            assertThrows(NonSquareMatrixException.class, () -> cholesky.getLT());
        } catch (NotSymmetricMatrixException | NotPositiveDefiniteMatrixException e) {
            // should not happen
        }
    }

    /**
     * Test the constructor with a non-symmetric matrix.
     */
    @Test
    public void testNonSymmetricConstructor() {
        try {
            RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                { 1, 2 },
                { 3, 1 }
            });
            CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
            assertThrows(NotSymmetricMatrixException.class, () -> cholesky.getL());
            assertThrows(NotSymmetricMatrixException.class, () -> cholesky.getLT());
        } catch (NonSquareMatrixException | NotPositiveDefiniteMatrixException e) {
            // should not happen
        }
    }

    /**
     * Test the constructor with a non-positive definite matrix.
     */
    @Test
    public void testNonPositiveDefiniteConstructor() {
        try {
            RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                { 1, 2 },
                { 2, -1 }
            });
            CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
            assertThrows(NotPositiveDefiniteMatrixException.class, () -> cholesky.getL());
            assertThrows(NotPositiveDefiniteMatrixException.class, () -> cholesky.getLT());
        } catch (NonSquareMatrixException | NotSymmetricMatrixException e) {
            // should not happen
        }
    }

    /**
     * Test the constructor with a positive definite matrix and default thresholds.
     */
    @Test
    public void testPositiveDefiniteConstructorDefaultThresholds() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            { 1, 2, 3 },
            { 2, 1, 4 },
            { 3, 4, 1 }
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        assertEquals(3, l.getRowDimension());
        assertEquals(3, l.getColumnDimension());
        RealMatrix lt = cholesky.getLT();
        assertEquals(3, lt.getRowDimension());
        assertEquals(3, lt.getColumnDimension());
        assertEquals(l, lt.transpose());
    }

    /**
     * Test the constructor with a positive definite matrix and custom thresholds.
     */
    @Test
    public void testPositiveDefiniteConstructorCustomThresholds() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            { 1e-5, 2e-5, 3e-5 },
            { 2e-5, 1e-5, 4e-5 },
            { 3e-5, 4e-5, 1e-5 }
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1e-11, 1e-16);
        RealMatrix l = cholesky.getL();
        assertEquals(3, l.getRowDimension());
        assertEquals(3, l.getColumnDimension());
        RealMatrix lt = cholesky.getLT();
        assertEquals(3, lt.getRowDimension());
        assertEquals(3, lt.getColumnDimension());
        assertEquals(l, lt.transpose());
    }

    /**
     * Test the getDeterminant method.
     */
    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            { 1, 2, 3 },
            { 2, 1, 4 },
            { 3, 4, 1 }
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        assertEquals(3.0 * 2.0 * 1.0, cholesky.getDeterminant(), 1e-15);
    }

    /**
     * Test the getSolver method with a vector.
     */
    @Test
    public void testGetSolverVector() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            { 1, 2, 3 },
            { 2, 1, 4 },
            { 3, 4, 1 }
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);

        RealVector vector = new ArrayRealVector(new double[] { 1, 2, 3 });
        RealVector solution = cholesky.getSolver().solve(vector);

        assertEquals(new double[] { 0.53846068, -0.47140452, 0.0 }, solution.getData(), 1e-15);
    }

    /**
     * Test the getSolver method with a matrix.
     */
    @Test
    public void testGetSolverMatrix() {
        RealMatrix matrixA = new Array2DRowRealMatrix(new double[][] {
            { 1, 2, 3 },
            { 2, 1, 4 },
            { 3, 4, 1 }
        });
        RealMatrix matrixB = new Array2DRowRealMatrix(new double[][] {
            { 1, 2 },
            { 3, 1 }
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrixA);

        RealMatrix solution = cholesky.getSolver().solve(matrixB);

        RealMatrix expectedSolution = new Array2DRowRealMatrix(new double[][] {
            { 0.63212055, 0.21557303 },
            { -0.04114182, 0.84785863 }
        });
        assertEquals(expectedSolution, solution);
    }

}