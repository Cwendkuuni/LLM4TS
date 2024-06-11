package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.exception.SingularValueDecompositionException;
import org.apache.commons.math3.exception.NotSquareMatrixException;
import org.apache.commons.math3.exception.RealMatrixSizeMismatchException;
import org.apache.commons.math3.exception.InvalidMatrixException;
import org.junit.Test;

/**
 * Test for CholeskyDecompositionImpl class
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test for constructor with default thresholds
     */
    @Test
    public void testDefaultThresholds() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.1, 0.1},
            {0.1, 1.0, 0.1},
            {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();
        assertEquals(l, lt.transpose());
        assertEquals(decomposition.getDeterminant(), 1.0, 1e-15);

        DecompositionSolver solver = decomposition.getSolver();
        RealVector b = new RealVectorImpl(new double[]{1.0, 2.0, 3.0});
        RealVector x = solver.solve(b);
        assertEquals(x.getDimension(), 3);
        assertEquals(x.getEntry(0), 1.0 / 1.4142135625, 1e-15);
        assertEquals(x.getEntry(1), (1.0 - 1.0 / 1.4142135625) / 0.70710678118654755, 1e-15);
        assertEquals(x.getEntry(2), -(1.0 / 1.4142135625 * (1.0 - 1.0 / 1.4142135625) - 1.0) / 0.70710678118654755, 1e-15);

        // test inverse
        RealMatrix inverse = decomposition.getInverse();
        RealVector b2 = new RealVectorImpl(new double[]{1.0, 1.0, 1.0});
        RealVector x2 = inverse.operate(b2);
        assertEquals(x2.getEntry(0), 1.0 / 1.4142135625, 1e-15);
        assertEquals(x2.getEntry(1), (1.0 - 1.0 / 1.4142135625) / 0.70710678118654755, 1e-15);
        assertEquals(x2.getEntry(2), -(1.0 / 1.4142135625 * (1.0 - 1.0 / 1.4142135625) - 1.0) / 0.70710678118654755, 1e-15);
    }

    /**
     * Test for constructor with custom thresholds
     */
    @Test
    public void testCustomThresholds() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 1e-13, 1e-13},
            {1e-13, 1.0, 1e-13},
            {1e-13, 1e-13, 1.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix, 1e-24, 1e-24);
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();
        assertEquals(l, lt.transpose());
        assertEquals(decomposition.getDeterminant(), 1.0, 1e-24);

        DecompositionSolver solver = decomposition.getSolver();
        RealVector b = new RealVectorImpl(new double[]{1.0, 2.0, 3.0});
        RealVector x = solver.solve(b);
        assertEquals(x.getDimension(), 3);
        assertEquals(x.getEntry(0), 1.0 / Math.sqrt(2.0), 1e-24);
        assertEquals(x.getEntry(1), (1.0 - 1.0 / Math.sqrt(2.0)) / (Math.sqrt(2.0) / 2.0), 1e-24);
        assertEquals(x.getEntry(2), -(1.0 / Math.sqrt(2.0) * (1.0 - 1.0 / Math.sqrt(2.0)) - 1.0) / (Math.sqrt(2.0) / 2.0), 1e-24);

        // test inverse
        RealMatrix inverse = decomposition.getInverse();
        RealVector b2 = new RealVectorImpl(new double[]{1.0, 1.0, 1.0});
        RealVector x2 = inverse.operate(b2);
        assertEquals(x2.getEntry(0), 1.0 / Math.sqrt(2.0), 1e-24);
        assertEquals(x2.getEntry(1), (1.0 - 1.0 / Math.sqrt(2.0)) / (Math.sqrt(2.0) / 2.0), 1e-24);
        assertEquals(x2.getEntry(2), -(1.0 / Math.sqrt(2.0) * (1.0 - 1.0 / Math.sqrt(2.0)) - 1.0) / (Math.sqrt(2.0) / 2.0), 1e-24);
    }

    /**
     * Test for NotSquareMatrixException
     */
    @Test(expected = NotSquareMatrixException.class)
    public void testNotSquareMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.1},
            {0.1, 1.0},
            {0.1, 1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test for NotSymmetricMatrixException
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.1, 0.1},
            {0.1, 1.0, 0.1},
            {0.1, 0.1, 1.0},
            {0.1, 0.1, 1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test for NotPositiveDefiniteMatrixException
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.1, 0.1},
            {0.1, -1.0, 0.1},
            {0.1, 0.1, 1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test for LUDecompositionException when the matrix is singular
     */
    @Test(expected = SingularValueDecompositionException.class)
    public void testSingularValueDecompositionException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {0.0, 1.0, 0.0},
            {1.0, 0.0, 0.0},
            {0.0, 0.0, 0.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test for RealMatrixSizeMismatchException when the right-hand side vector has a different size than the matrix
     */
    @Test(expected = RealMatrixSizeMismatchException.class)
    public void testRealMatrixSizeMismatchException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.1},
            {0.1, 1.0}});
        RealVector b = new RealVectorImpl(new double[]{1.0, 2.0, 3.0});
        new CholeskyDecompositionImpl(matrix).getSolver().solve(b);
    }
}