package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class CholeskyDecompositionImplTest {

    @Test
    public void testIdentityMatrixDecomposition() {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(3);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        assertEquals(lt.transpose(), l);
        assertEquals(MatrixUtils.multiply(lt, l), matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testSingularMatrixDecomposition() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{2, 1}, {1, 2}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixDecomposition() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 1}, {1}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testSymmetryBreakingMatrixDecomposition() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 1}});
        new CholeskyDecompositionImpl(matrix, 0.0001, 0.0001);
    }

    @Test
    public void testSolveSimpleEquation() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, 2}});
        RealVector b = new ArrayRealVector(new double[]{1, 2});
        RealVector x = new CholeskyDecompositionImpl(matrix).getSolver().solve(b);
        RealVector expected = new ArrayRealVector(new double[]{0.5, 1.5});
        assertArrayEquals(expected.toArray(), x.toArray(), 0.0);
    }

    @Test
    public void testSolveComplexEquation() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {2, 4, 6}, {3, 6, 10}});
        RealVector b = new ArrayRealVector(new double[]{6, 14, 22});
        RealVector x = new CholeskyDecompositionImpl(matrix).getSolver().solve(b);
        RealVector expected = new ArrayRealVector(new double[]{2.5, 1.5, 1.0});
        assertArrayEquals(expected.toArray(), x.toArray(), 0.0);
    }

    @Test
    public void testSolveSingularEquation() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, 0}});
        RealVector b = new ArrayRealVector(new double[]{1, 0});
        RealVector x = new CholeskyDecompositionImpl(matrix).getSolver().solve(b);
        RealVector expected = new ArrayRealVector(new double[]{0, 0});
        assertArrayEquals(expected.toArray(), x.toArray(), 0.0);
    }

    @Test
    public void testSolveNonSquareEquation() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        RealVector b = new ArrayRealVector(new double[]{1, 2, 3});
        RealVector x = new CholeskyDecompositionImpl(matrix).getSolver().solve(b);
        RealVector expected = new RealVectorImpl(new double[]{1.5 / Math.sqrt(2.0), 0.5 / Math.sqrt(2.0)}, false);
        assertEquals(expected, x);
    }

    @Test
    public void testSolveDiagonalEquation() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{2, 0}, {0, 3}});
        RealVector b = new ArrayRealVector(new double[]{4, 6});
        RealVector x = new CholeskyDecompositionImpl(matrix).getSolver().solve(b);
        RealVector expected = new ArrayRealVector(new double[]{2.0, 1.5}, false);
        assertEquals(expected, x);
    }

    @Test
    public void testSolveComplexEquationWithVector() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {2, 4, 6}, {3, 6, 10}});
        RealVector b = new RealVectorImpl(new double[]{6, 14, 22}, false);
        RealVector x = new CholeskyDecompositionImpl(matrix).getSolver().solve(b);
        RealVector expected = new RealVectorImpl(new double[]{2.5, 1.5, 1.0}, false);
        assertEquals(expected, x);
    }

    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 3}});
        double determinant = new CholeskyDecompositionImpl(matrix).getDeterminant();
        assertEquals(determinant, 1.0 / 3.0, 1e-6);
    }
}