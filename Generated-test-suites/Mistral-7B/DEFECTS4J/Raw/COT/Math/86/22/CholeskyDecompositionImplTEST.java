package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.junit.Test;

/**
 * JUnit test cases for CholeskyDecompositionImpl class
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test constructor with default thresholds
     */
    @Test
    public void testCholeskyDecompositionDefault() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][] {{3, 2}, {2, 3}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        assertEquals(l.getRowDimension(), lt.getColumnDimension());
        assertEquals(l.getRowDimension(), l.getColumnDimension());
    }

    /**
     * Test constructor with custom thresholds
     */
    @Test
    public void testCholeskyDecompositionCustom() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][] {{2.1, 1.0}, {1.0, 2.1}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1.0e-5, 1.0e-12);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        assertEquals(l.getRowDimension(), lt.getColumnDimension());
        assertEquals(l.getRowDimension(), l.getColumnDimension());
    }

    /**
     * Test CholeskyDecompositionImpl with non-square matrix
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {{1, 2}, {3, 4}, {5, 6}});
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test CholeskyDecompositionImpl with non-symmetric matrix
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {{1, 1}, {0, 2}});
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test CholeskyDecompositionImpl with non-positive definite matrix
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {{1, -1}, {-1, 1}});
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test getL() method
     */
    @Test
    public void testGetL() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][] {{3, 2}, {2, 3}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealVector lRow0 = l.getColumnVector(0);
        RealVector lRow1 = l.getColumnVector(1);
        assertArrayEquals(new double[]{-0.577350269189626, 0}, lRow0.toArray(), 0.0);
        assertArrayEquals(new double[]{0.577350269189626, -0.577350269189626}, lRow1.toArray(), 0.0);
    }

    /**
     * Test getLT() method
     */
    @Test
    public void testGetLT() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][] {{3, 2}, {2, 3}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix lt = cholesky.getLT();
        assertTrue(lt.isSymmetric());
        assertArrayEquals(new double[]{-0.577350269189626, 0.577350269189626}, lt.getEntryData(0, 0), 0.0);
        assertArrayEquals(new double[]{0.577350269189626, -0.577350269189626}, lt.getEntryData(1, 1), 0.0);
    }

    /**
     * Test getDeterminant() method
     */
    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][] {{3, 2}, {2, 3}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        assertEquals(3.0 * 3.0, cholesky.getDeterminant(), 0.0);
    }

    /**
     * Test getSolver() method
     */
    @Test
    public void testGetSolver() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][] {{3, 2}, {2, 3}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = cholesky.getSolver();
        RealVector b = new ArrayRealVector(new double[]{-1, 1});
        RealVector x = solver.solve(b);
        assertArrayEquals(new double[]{-0.577350269189626, 0.577350269189626}, x.toArray(), 0.0);
    }

    /**
     * Test getSolver() method with non-square matrix
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testGetSolverNonSquareMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {{1, 2}, {3, 4}, {5, 6}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = cholesky.getSolver();
    }

    /**
     * Test CholeskyDecompositionImpl with singular matrix
     */
    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        RealMatrix matrix = MatrixUtils.createSingularMatrix(3);
        try {
            new CholeskyDecompositionImpl(matrix);
        } catch (NotPositiveDefiniteMatrixException e) {
            throw new SingularMatrixException("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test Solver class
     */
    @Test
    public void testSolver() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][] {{3, 2}, {2, 3}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = cholesky.getSolver();
        RealVector b = new ArrayRealVector(new double[]{1, 2});
        RealVector x = solver.solve(b);
        assertArrayEquals(new double[]{-0.577350269189626, 0.577350269189626}, x.toArray(), 0.0);
    }

    /**
     * Test Solver class with non-square matrix
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testSolverNonSquareMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {{1, 2}, {3, 4}, {5, 6}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = cholesky.getSolver();
        RealVector b = new ArrayRealVector(new double[]{-1, 1, 2});
        solver.solve(b);
    }
}