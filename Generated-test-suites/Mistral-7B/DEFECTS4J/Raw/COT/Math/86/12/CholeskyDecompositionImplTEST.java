package org.apache.commons.math.linear.test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.special.MatrixFunctions;
import org.junit.Assert;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    /**
     * Test the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0},
                                                                       {2.0, 5.0, 4.0},
                                                                       {2.0, 4.0, 6.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix expectedL = MatrixFunctions.cholesky(matrix);
        RealMatrix actualL = choleskyDecomposition.getL();
        Assert.assertArrayEquals(expectedL.getData(), actualL.getData(), 0.0);
    }

    /**
     * Test the constructor with custom thresholds.
     */
    @Test
    public void testCustomConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0e-5, 2.0e-5, 2.0e-5},
                                                                       {2.0e-5, 5.0, 4.0},
                                                                       {2.0e-5, 4.0, 6.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix, 1.0e-11, 1.0e-11);
        RealMatrix expectedL = MatrixFunctions.cholesky(matrix);
        RealMatrix actualL = choleskyDecomposition.getL();
        Assert.assertArrayEquals(expectedL.getData(), actualL.getData(), 0.0);
    }

    /**
     * Test exception when given a non-square matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {1.0, 3.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test exception when given a not symmetric matrix.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0},
                                                                       {2.0, 5.0, 4.0},
                                                                       {2.0, 4.0, 6.0},
                                                                       {2.0, 4.0, 6.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test exception when given a not positive definite matrix.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0},
                                                                       {2.0, -1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test getting the L matrix.
     */
    @Test
    public void testGetL() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0},
                                                                       {2.0, 5.0, 4.0},
                                                                       {2.0, 4.0, 6.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix expectedL = MatrixFunctions.cholesky(matrix);
        RealMatrix actualL = choleskyDecomposition.getL();
        Assert.assertArrayEquals(expectedL.getData(), actualL.getData(), 0.0);
    }

    /**
     * Test getting the LT matrix.
     */
    @Test
    public void testGetLT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0},
                                                                       {2.0, 5.0, 4.0},
                                                                       {2.0, 4.0, 6.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix expectedLT = MatrixFunctions.cholesky(matrix).transpose();
        RealMatrix actualLT = choleskyDecomposition.getLT();
        Assert.assertArrayEquals(expectedLT.getData(), actualLT.getData(), 0.0);
    }

    /**
     * Test getting the determinant.
     */
    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0},
                                                                       {2.0, 2.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        double expectedDeterminant = MatrixFunctions.determinant(matrix.getData());
        double actualDeterminant = choleskyDecomposition.getDeterminant();
        Assert.assertEquals(expectedDeterminant, actualDeterminant, 0.0);
    }

    /**
     * Test the solver.
     */
    @Test
    public void testSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0},
                                                                       {2.0, 2.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealVector b = new RealVectorImpl(new double[]{1.0, 1.0});
        RealVector x = choleskyDecomposition.getSolver().solve(b);
        RealVector expectedX = new RealVectorImpl(new double[]{3.0 / 3.0, 1.0 / 3.0});
        Assert.assertArrayEquals(expectedX.getData(), x.getData(), 0.0);
    }

}