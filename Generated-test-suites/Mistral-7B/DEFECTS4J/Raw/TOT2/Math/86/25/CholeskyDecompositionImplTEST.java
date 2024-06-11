package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for {@link CholeskyDecompositionImpl}.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test the constructor with a square matrix.
     */
    @Test
    public void testConstructorWithSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        assertNotNull(choleskyDecomposition.getL());
        assertNotNull(choleskyDecomposition.getLT());
    }

    /**
     * Test the constructor with a non-square matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}, {1.0, 1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the constructor with a non-symmetric matrix.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testConstructorWithNonSymmetricMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 2.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the constructor with a non-positive definite matrix.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, -1.0}, {-1.0, 1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the getDeterminant() method.
     */
    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        double determinant = choleskyDecomposition.getDeterminant();
        Assert.assertEquals(1.1666666666666667E-13, determinant, 0.0); // Determinant is approximately 1, but not exactly due to floating point precision
    }

    /**
     * Test the getSolver() method.
     */
    @Test
    public void testGetSolver() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 4.0, 1.0}, {1.0, 1.0, 4.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealVector b = VectorUtils.createRealVector(new double[]{3.0, 1.0, 1.0});
        RealVector x = choleskyDecomposition.getSolver().solve(b);
        assertEquals(new double[]{1.125, -0.25, 0.25}, x.toArray(), 0.001);
    }

    /**
     * Test the solver with a singular matrix.
     */
    @Test(expected = InvalidMatrixException.class)
    public void testSolverWithSingularMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 0.5}, {0.5, 0.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealVector b = VectorUtils.createRealVector(new double[]{1.0, 1.0});
        choleskyDecomposition.getSolver().solve(b);
    }

    /**
     * Test the getL() method.
     */
    @Test
    public void testGetL() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.0, 1.0 - 0.25, -0.25}, {0.0, -0.25, 1.0 - 0.25}});
        Assert.assertEquals(expectedL, l);
    }

    /**
     * Test the getLT() method.
     */
    @Test
    public void testGetLT() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix lt = choleskyDecomposition.getLT();
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
        Assert.assertEquals(expectedLT, lt.transpose());
    }

}