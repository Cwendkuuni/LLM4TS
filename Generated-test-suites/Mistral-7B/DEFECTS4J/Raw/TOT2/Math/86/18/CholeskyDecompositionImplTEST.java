package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the CholeskyDecompositionImpl class.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test the Cholesky decomposition of a trivial 1x1 matrix.
     */
    @Test
    public void testTrivialDecomposition() {
        double[][] lTData = new double[][]{{1.0}};
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(
                new Array2DRowRealMatrix(lTData));
        RealMatrix l = decomp.getL();
        assertNotNull(l);
        assertEquals(1.0, l.getEntry(0, 0), 1e-14);
    }

    /**
     * Test the Cholesky decomposition of a diagonal matrix.
     */
    @Test
    public void testDiagonalDecomposition() {
        double[][] lTData = new double[][]{{1.0, 0.0}, {0.0, 2.0}};
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(
                new Array2DRowRealMatrix(lTData));
        RealMatrix l = decomp.getL();
        assertNotNull(l);
        assertEquals(1.0, l.getEntry(0, 0), 1e-14);
        assertEquals(Math.sqrt(2.0), l.getEntry(1, 1), 1e-14);
    }

    /**
     * Test the Cholesky decomposition of a symmetric positive definite matrix.
     */
    @Test
    public void testSymmetricPositiveDefiniteDecomposition() {
        double[][] lTData = new double[][]{{3.0, 1.5, 1.5},
                                            {1.5, 2.0, 1.5},
                                            {1.5, 1.5, 2.0}};
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(
                new Array2DRowRealMatrix(lTData));
        RealMatrix l = decomp.getL();
        assertNotNull(l);
        RealMatrix lt = decomp.getLT();
        SingularValueDecomposition svd = new SingularValueDecompositionImpl(
                new Array2DRowRealMatrix(lTData));
        SingularValueDecomposition svdLT = new SingularValueDecompositionImpl(
                lt.transpose());
        RealMatrix expectedLt = new Array2DRowRealMatrix(svdLT.getV());
        assertEquals(expectedLt, lt, 1e-14);
    }

    /**
     * Test the Cholesky decomposition of a non-symmetric matrix.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNonSymmetricDecomposition() {
        double[][] lTData = new double[][]{{1.0, 2.0},
                                            {2.0, 1.0}};
        new CholeskyDecompositionImpl(new Array2DRowRealMatrix(lTData));
    }

    /**
     * Test the Cholesky decomposition of a singular matrix.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testSingularDecomposition() {
        double[][] lTData = new double[][]{{1.0, 1.0},
                                            {1.0, -1.0}};
        new CholeskyDecompositionImpl(new Array2DRowRealMatrix(lTData));
    }

    /**
     * Test the Cholesky decomposition of a non-square matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareDecomposition() {
        double[][] lTData = new double[][]{{1.0, 2.0},
                                            {2.0}};
        new CholeskyDecompositionImpl(new Array2DRowRealMatrix(lTData));
    }

    /**
     * Test the computation of the determinant.
     */
    @Test
    public void testDeterminant() {
        double[][] lTData = new double[][]{{3.0, 1.5, 1.5},
                                            {1.5, 2.0, 1.5},
                                            {1.5, 1.5, 2.0}};
        double determinant = new CholeskyDecompositionImpl(
                new Array2DRowRealMatrix(lTData)).getDeterminant();
        assertEquals(2.0 * Math.sqrt(3.0), determinant, 1e-14);
    }

    /**
     * Test the solution of a linear system.
     */
    @Test
    public void testSolver() {
        double[][] lTData = new double[][]{{3.0, 1.5, 1.5},
                                            {1.5, 2.0, 1.5},
                                            {1.5, 1.5, 2.0}};
        double[] b = {1.0, 1.0, 1.0};
        double[] x = new CholeskyDecompositionImpl(
                new Array2DRowRealMatrix(lTData)).getSolver().solve(b);
        RealVector expectedX = new RealVectorImpl(new double[]{0.1428571428571429,
                                                              0.4285714285714285,
                                                              0.857142857142857});
        assertEquals(expectedX, new RealVectorImpl(x), 1e-14);
    }

}