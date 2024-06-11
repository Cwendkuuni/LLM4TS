package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.InvalidMatrixException;

public class CholeskyDecompositionImplTest {

    @org.junit.Test
    public void testConstructorWithMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.2}, {0.1, 1.0, 0.3}, {0.2, 0.3, 1.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        RealMatrix identity = MatrixUtils.createRealIdentityMatrix(3);
        RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.5, 0.3}, {0.5, 1.0, 0.2}, {0.3, 0.2, 1.0}});
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.5, 0.3}, {0.5, 1.0, 0.2}, {0.3, 0.2, 1.0}});
        assertEquals(expectedL, l);
        assertEquals(expectedLT, lt);
        assertEquals(identity, cholesky.getSolver().solve(identity));
    }

    @org.junit.Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1}, {0.1, 1.0}, {0.1, 0.2}});
        new CholeskyDecompositionImpl(matrix);
    }

    @org.junit.Test(expected = NotSymmetricMatrixException.class)
    public void testConstructorWithNonSymmetricMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @org.junit.Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 0.1, -1.0}, {0.1, 0.1, 1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @org.junit.Test
    public void testGetDeterminant() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.2}, {0.1, 1.0, 0.3}, {0.2, 0.3, 1.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        assertEquals(1.0 * 1.0 * 1.0, cholesky.getDeterminant(), 1e-15);
    }

    @org.junit.Test
    public void testSolveVector() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.2}, {0.1, 1.0, 0.3}, {0.2, 0.3, 1.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        double[] b = new double[]{1.0, 2.0, 3.0};
        RealVector x = cholesky.getSolver().solve(new RealVectorImpl(b, false));
        RealVector expected = new RealVectorImpl(new double[]{0.5, -0.5, 0.5}, false);
        assertEquals(expected, x);
    }

    @org.junit.Test
    public void testSolveMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.2}, {0.1, 1.0, 0.3}, {0.2, 0.3, 1.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix b = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        RealMatrix x = cholesky.getSolver().solve(b);
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{1.5, -0.3}, {-0.3, 0.6}});
        assertEquals(expected, x);
    }

    @org.junit.Test
    public void testGetInverse() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.2}, {0.1, 1.0, 0.3}, {0.2, 0.3, 1.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix inverse = cholesky.getInverse();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.6, 0.2, -0.2}, {-0.2, 0.5, 0.1}, {0.2, -0.1, 0.4}});
        assertEquals(expected, inverse);
    }

}