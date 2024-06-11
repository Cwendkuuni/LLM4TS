package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    /**
     * Test constructor CholeskyDecompositionImpl(RealMatrix) with valid input.
     * @throws Exception if any error occurs
     */
    @Test
    public void testValidInput() throws Exception {
        double[][] matrixData = {
            {4.0, 2.0},
            {2.0, 4.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test constructor CholeskyDecompositionImpl(RealMatrix, double, double) with valid input.
     * @throws Exception if any error occurs
     */
    @Test
    public void testValidInputWithThresholds() throws Exception {
        double[][] matrixData = {
            {0.1, 0.01},
            {0.01, 0.2}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1e-6, 1e-7);
    }

    /**
     * Test CholeskyDecompositionImpl(RealMatrix) with invalid input (non-square matrix).
     * @throws Exception if any error occurs
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testInvalidInputNonSquareMatrix() throws Exception {
        double[][] matrixData = {
            {4.0, 2.0},
            {2.0, 4.0},
            {1.0, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test CholeskyDecompositionImpl(RealMatrix, double, double) with invalid input (non-symmetric matrix).
     * @throws Exception if any error occurs
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testInvalidInputNonSymmetricMatrix() throws Exception {
        double[][] matrixData = {
            {4.0, 2.0},
            {2.0, 3.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix, 1e-15, 1e-15);
    }

    /**
     * Test CholeskyDecompositionImpl(RealMatrix, double, double) with invalid input (non-positive definite matrix).
     * @throws Exception if any error occurs
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testInvalidInputNonPositiveDefiniteMatrix() throws Exception {
        double[][] matrixData = {
            {1.0, 0.0},
            {0.0, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix, 1e-15, 1e-15);
    }

    /**
     * Test getL() method.
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetL() throws Exception {
        double[][] matrixData = {
            {4.0, 2.0},
            {2.0, 4.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1e-15, 1e-15);
        RealMatrix expectedL = new CholeskyDecomposition(matrix).getL();
        RealMatrix l = cholesky.getL();
        assertArrayEquals(expectedL.getData(), l.getData(), 1e-15);
    }

    /**
     * Test getLT() method.
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetLT() throws Exception {
        double[][] matrixData = {
            {4.0, 2.0},
            {2.0, 4.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1e-15, 1e-15);
        RealMatrix expectedLT = new CholeskyDecomposition(matrix).getLT();
        RealMatrix lt = cholesky.getLT();
        assertArrayEquals(expectedLT.getData(), lt.getData(), 1e-15);
    }

    /**
     * Test getDeterminant() method.
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetDeterminant() throws Exception {
        double[][] matrixData = {
            {4.0, 2.0},
            {2.0, 4.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1e-15, 1e-15);
        double expectedDeterminant = 16.0;
        double determinant = cholesky.getDeterminant();
        assertEquals(expectedDeterminant, determinant, 1e-15);
    }

    /**
     * Test getSolver() method.
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetSolver() throws Exception {
        double[][] matrixData = {
            {4.0, 2.0},
            {2.0, 4.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1e-15, 1e-15);
        RealVector expectedSolution = new Array2DRowRealVector(new double[] {1.0, 1.0});
        RealVector solution = cholesky.getSolver().solve(expectedSolution);
        assertArrayEquals(expectedSolution.getData(), solution.getData(), 1e-15);
    }

    /**
     * Test CholeskyDecompositionImpl(RealMatrix) solver with invalid input.
     * @throws Exception if any error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolverInvalidInput() throws Exception {
        double[][] matrixData = {
            {4.0, 2.0},
            {2.0, 4.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1e-15, 1e-15);
        RealVector invalidVector = new Array2DRowRealVector(new double[] {1.0, 2.0, 3.0});
        cholesky.getSolver().solve(invalidVector);
    }

    /**
     * Test CholeskyDecompositionImpl(RealMatrix) solver with valid input.
     * @throws Exception if any error occurs
     */
    @Test
    public void testSolverValidInput() throws Exception {
        double[][] matrixData = {
            {4.0, 2.0},
            {2.0, 4.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1e-15, 1e-15);
        RealVector expectedSolution = new Array2DRowRealVector(new double[] {1.0 / 2.0, 1.0});
        RealVector solution = cholesky.getSolver().solve(new Array2DRowRealVector(new double[] {1.0, 1.0}));
        assertArrayEquals(expectedSolution.getData(), solution.getData(), 1e-15);
    }

    /**
     * Test CholeskyDecompositionImpl(RealMatrix) solver with invalid matrix.
     * @throws Exception if any error occurs
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testSolverInvalidMatrix() throws Exception {
        double[][] matrixData = {
            {1.0, 0.0},
            {0.0, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1e-15, 1e-15);
        RealVector vector = new Array2DRowRealVector(new double[] {1.0, 1.0});
        cholesky.getSolver().solve(vector);
    }

}