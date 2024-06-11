// CholeskyDecompositionImplTest.java
package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CholeskyDecompositionImpl.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test the constructor that takes a square matrix as an argument and
     * uses default thresholds.
     */
    @Test
    public void testDefaultConstructor() {
        RealMatrix matrix = RealMatrixFactory.identity(3);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        assertEquals(l, lt.transpose());
    }

    /**
     * Test the constructor that takes a square matrix, a relative symmetry threshold,
     * and an absolute positivity threshold as arguments.
     */
    @Test
    public void testConstructorWithThresholds() {
        RealMatrix matrix = RealMatrixFactory.identity(3);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1e-12, 1e-12);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        assertEquals(l, lt.transpose());
    }

    /**
     * Test the constructor with a non-square matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0}, {2.0, 3.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the constructor with a non-symmetric matrix.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testConstructorWithNonSymmetricMatrix() {
        RealMatrix matrix = RealMatrixFactory.random(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < i; j++) {
                matrix.setEntry(i, j, matrix.getEntry(j, i) + 1.0);
            }
        }
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the constructor with a non-positive definite matrix.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, -1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the getL() method.
     */
    @Test
    public void testGetL() {
        RealMatrix matrix = RealMatrixFactory.identity(3);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        assertEquals(l, lt.transpose());
    }

    /**
     * Test the getLT() method.
     */
    @Test
    public void testGetLT() {
        RealMatrix matrix = RealMatrixFactory.identity(3);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        assertEquals(l, lt.transpose());
    }

    /**
     * Test the getDeterminant() method.
     */
    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = RealMatrixFactory.identity(3);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        assertEquals(1.0, cholesky.getDeterminant(), 1e-15);
    }

    /**
     * Test the getSolver() method.
     */
    @Test
    public void testGetSolver() {
        RealMatrix matrix = RealMatrixFactory.identity(3);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealVector b = new ArrayRealVector(new double[]{-1.0, 2.0, 3.0});
        RealVector x = cholesky.getSolver().solve(b);
        assertEquals(new ArrayRealVector(new double[]{1.0/3.0, -1.0/3.0, 1.0/3.0}), x);
    }

    /**
     * Test the inverse() method.
     */
    @Test
    public void testInverse() throws InvalidMatrixException {
        RealMatrix matrix = RealMatrixFactory.identity(3);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix inverse = cholesky.getInverse();
        RealMatrix product = new CholeskyDecompositionImpl(matrix).getL().multiply(inverse);
        assertEquals(MatrixUtils.createRealIdentityMatrix(3), product);
    }

}