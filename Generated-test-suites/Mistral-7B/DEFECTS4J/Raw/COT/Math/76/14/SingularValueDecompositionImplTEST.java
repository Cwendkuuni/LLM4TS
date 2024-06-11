package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.EigenDecompositionImpl;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testConstructorWithValidMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            // Should not reach here
        }
        assertTrue("SingularValueDecompositionImpl was not constructed", svd != null);
    }

    @Test(expected = InvalidMatrixException.class)
    public void testConstructorWithSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 1}, {1, 1}});
        new SingularValueDecompositionImpl(matrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1}});
        new SingularValueDecompositionImpl(matrix);
    }

    @Test
    public void testGetUForIdentityMatrix() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix result = svd.getU();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.70710678, 0.70710678},
                {-0.70710678, 0.70710678}});
        assertMatrixEquals(expected, result);
    }

    @Test
    public void testGetUTForIdentityMatrix() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix result = svd.getUT();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.70710678, -0.70710678},
                {0.70710678, 0.70710678}});
        assertMatrixEquals(expected, result);
    }

    @Test
    public void testGetSForIdentityMatrix() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix result = svd.getS();
        RealMatrix expected = MatrixUtils.createRealDiagonalMatrix(new double[]{1, 1});
        assertMatrixEquals(expected, result);
    }

    @Test
    public void testGetSingularValuesForIdentityMatrix() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double[] result = svd.getSingularValues();
        double[] expected = new double[]{1, 1};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetVForIdentityMatrix() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix result = svd.getV();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.70710678, -0.70710678},
                {0.70710678, 0.70710678}});
        assertMatrixEquals(expected, result);
    }

    @Test
    public void testGetVTForIdentityMatrix() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix result = svd.getVT();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.70710678, 0.70710678},
                {-0.70710678, 0.70710678}});
        assertMatrixEquals(expected, result);
    }

    @Test
    public void testGetCovarianceWithFullMatrix() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 1}, {1, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix result = svd.getCovariance(0);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1, 1}, {1, 1}});
        assertMatrixEquals(expected, result);
    }

    @Test
    public void testGetCovarianceWithSingularMatrix() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 1}, {1, 1}, {1, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix result = svd.getCovariance(1);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.57735027, 0.57735027},
                {0.57735027, 0.57735027}, {0.57735027, 0.57735027}});
        assertMatrixEquals(expected, result);
    }

    @Test
    public void testGetNormWithIdentityMatrix() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double result = svd.getNorm();
        double expected = 1;
        assertEquals(expected, result, 1e-14);
    }

    @Test
    public void testGetConditionNumberWithIdentityMatrix() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double result = svd.getConditionNumber();
        double expected = 1;
        assertEquals(expected, result, 1e-14);
    }

    @Test
    public void testGetRankWithIdentityMatrix() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        int result = svd.getRank();
        int expected = 2;
        assertEquals(expected, result);
    }

    @Test
    public void testGetSolverWithIdentityMatrix() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        // A simple test case for the solver.
        RealVector b = new ArrayRealVector(new double[]{1, 1});
        RealVector x = solver.solve(b);
        RealVector expected = new ArrayRealVector(new double[]{1, 1});
        assertArrayEquals(expected.toArray(), x.toArray(), 1e-14);
    }

    @Test
    public void testGetSolverWithSingularMatrix() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 1}, {1, 1}, {1, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        // A simple test case for the solver.
        RealVector b = new ArrayRealVector(new double[]{1, 1, 1});
        RealVector x = solver.solve(b);
        RealVector expected = new ArrayRealVector(new double[]{0.5773502687258757, 0.5773502687258757,
                0.5773502687258757});
        assertArrayEquals(expected.toArray(), x.toArray(), 1e-14);
    }

    @Test
    public void testIsNonSingularWithIdentityMatrix() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        assertTrue(solver.isNonSingular());
    }

    @Test
    public void testIsNonSingularWithSingularMatrix() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 1}, {1, 1}, {1, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        assertFalse(solver.isNonSingular());
    }

    private void assertMatrixEquals(RealMatrix expected, RealMatrix actual) {
        for (int i = 0; i < expected.getRowDimension(); ++i) {
            for (int j = 0; j < expected.getColumnDimension(); ++j) {
                assertEquals(expected.getEntry(i, j), actual.getEntry(i, j), 1e-14);
            }
        }
    }

    private void assertEquals(double expected, double actual, double tolerance) {
        assertTrue("Expected: " + expected + ", Actual: " + actual,
                Precision.isClose(expected, actual, tolerance));
    }
}