package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for CholeskyDecompositionImpl
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test the constructor with default threshold values
     */
    @Test
    public void testDefaultConstructor() {
        RealMatrix matrix = Array2DRandomGenerator.getRandomMatrix(new int[]{3, 3}, 1.0);
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        // Check decomposed matrix and its inverse
        RealMatrix L = choleskyDecomposition.getL();
        RealMatrix LT = choleskyDecomposition.getLT();
        RealMatrix inverseL = L.inverse();
        RealMatrix expectedInverseL = LT;
        assertTrue(L.isSymmetric());
        assertTrue(L.isPositiveDefinite());
        assertArrayEquals(expectedInverseL.getData(), inverseL.getData(), 1.0e-9);
    }

    /**
     * Test the constructor with custom threshold values
     */
    @Test
    public void testCustomConstructor() {
        RealMatrix matrix = Array2DRandomGenerator.getRandomMatrix(new int[]{3, 3}, 1.0);
        double relativeSymmetryThreshold = 1.0e-8;
        double absolutePositivityThreshold = 1.0e-5;
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix, relativeSymmetryThreshold,
                absolutePositivityThreshold);
        // Check decomposed matrix and its inverse
        RealMatrix L = choleskyDecomposition.getL();
        RealMatrix LT = choleskyDecomposition.getLT();
        RealMatrix inverseL = L.inverse();
        RealMatrix expectedInverseL = LT;
        assertTrue(L.isSymmetric());
        assertTrue(L.isPositiveDefinite());
        assertArrayEquals(expectedInverseL.getData(), inverseL.getData(), 1.0e-8);
    }

    /**
     * Test the getDeterminant method
     */
    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = Array2DRandomGenerator.getRandomMatrix(new int[]{3, 3}, 1.0);
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        double determinant = choleskyDecomposition.getDeterminant();
        assertNotNull(determinant);
    }

    /**
     * Test the getSolver method
     */
    @Test
    public void testGetSolver() {
        RealMatrix matrix = Array2DRandomGenerator.getRandomMatrix(new int[]{3, 3}, 1.0);
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        assertNotNull(solver);
    }

    /**
     * Test the solve method with a RealVector
     */
    @Test
    public void testSolveRealVector() {
        RealMatrix matrix = Array2DRandomGenerator.getRandomMatrix(new int[]{3, 3}, 1.0);
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        double[] b = new double[]{1.0, 2.0, 3.0};
        double[] expectedResult = new double[]{0.632498676016262, -0.463928604147682, 0.201626039477276};
        double[] result = choleskyDecomposition.getSolver().solve(new ArrayRealVector(b));
        assertArrayEquals(expectedResult, result, 1.0e-6);
    }

    /**
     * Test the solve method with a RealMatrix
     */
    @Test
    public void testSolveRealMatrix() {
        RealMatrix matrix = Array2DRandomGenerator.getRandomMatrix(new int[]{3, 3}, 1.0);
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix b = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0}
        });
        RealMatrix expectedResult = MatrixUtils.createRealMatrix(new double[][]{
                {-0.508896587980023, -0.377401796162166},
                {1.164011418280815, 0.662386325601285}
        });
        RealMatrix result = choleskyDecomposition.getSolver().solve(b);
        assertMatrixEquals(expectedResult, result, 1.0e-6);
    }

    /**
     * Test for non-square matrix
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix matrix = Array2DRandomGenerator.getRandomMatrix(new int[]{3, 2}, 1.0);
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test for non-symmetric matrix
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        RealMatrix matrix = Array2DRandomGenerator.getRandomMatrix(new int[]{3, 3}, 1.0);
        double[][] lTData = matrix.getData();
        for (int i = 0; i < 3; i++) {
            for (int j = i + 1; j < 3; j++) {
                lTData[i][j] += 1.0;
            }
        }
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(new Array2DRowRealMatrix(lTData));
    }

    /**
     * Test for not positive definite matrix
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {2.0, -1.0}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test edge case: diagonal elements close to zero
     */
    @Test
    public void testDiagonalElementsCloseToZero() {
        double relativeSymmetryThreshold = 1.0e-16;
        double absolutePositivityThreshold = 1.0e-16;
        RealMatrix matrix = Array2DRandomGenerator.getRandomMatrix(new int[]{3, 3}, 1.0);
        for (int i = 0; i < 3; i++) {
            matrix.setEntry(i, i, absolutePositivityThreshold);
        }
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix, relativeSymmetryThreshold,
                absolutePositivityThreshold);
        // Check decomposed matrix and its inverse
        RealMatrix L = choleskyDecomposition.getL();
        RealMatrix LT = choleskyDecomposition.getLT();
        RealMatrix inverseL = L.inverse();
        RealMatrix expectedInverseL = LT;
        assertTrue(L.isSymmetric());
        assertTrue(L.isPositiveDefinite());
        assertArrayEquals(expectedInverseL.getData(), inverseL.getData(), 1.0e-16);
    }

    /**
     * Test edge case: off-diagonal elements close to diagonal elements
     */
    @Test
    public void testOffDiagonalElementsCloseToDiagonalElements() {
        double relativeSymmetryThreshold = 1.0e-9;
        double absolutePositivityThreshold = 1.0e-9;
        RealMatrix matrix = Array2DRandomGenerator.getRandomMatrix(new int[]{3, 3}, 1.0);
        for (int i = 0; i < 3; i++) {
            for (int j = i + 1; j < 3; j++) {
                matrix.setEntry(i, j, relativeSymmetryThreshold);
                matrix.setEntry(j, i, relativeSymmetryThreshold);
            }
        }
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix, relativeSymmetryThreshold,
                absolutePositivityThreshold);
        // Check decomposed matrix and its inverse
        RealMatrix L = choleskyDecomposition.getL();
        RealMatrix LT = choleskyDecomposition.getLT();
        RealMatrix inverseL = L.inverse();
        RealMatrix expectedInverseL = LT;
        assertTrue(L.isSymmetric());
        assertTrue(L.isPositiveDefinite());
        assertArrayEquals(expectedInverseL.getData(), inverseL.getData(), 1.0e-9);
    }

}