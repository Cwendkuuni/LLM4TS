package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SolverFormat;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.util.Precision;

import java.util.Random;

/**
 * Test cases for CholeskyDecompositionImpl class
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test constructor with default threshold values
     */
    @org.junit.Test
    public void testDefaultConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0}, {1.0, 2.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        DecompositionSolver solver = cholesky.getSolver();
        assertEquals(2.0, l.getEntry(0, 0), 1e-15);
        assertEquals(1.0, l.getEntry(1, 0), 1e-15);
        assertEquals(1.0, l.getEntry(0, 1), 1e-15);
        assertEquals(1.0, l.getEntry(1, 1), 1e-15);
        assertEquals(1.0 / Math.sqrt(2.0), lt.getEntry(0, 0), 1e-15);
        assertEquals(0.0, lt.getEntry(0, 1), 1e-15);
        assertEquals(1.0 / Math.sqrt(2.0), lt.getEntry(1, 0), 1e-15);
        assertEquals(-1.0 / (2.0 * Math.sqrt(2.0)), lt.getEntry(1, 1), 1e-15);
        RealVector b = new RealVectorImpl(new double[]{1.0, 1.0});
        RealVector x = solver.solve(b);
        assertEquals(0.5, x.getEntry(0), 1e-15);
        assertEquals(0.5, x.getEntry(1), 1e-15);
    }

    /**
     * Test constructor with custom threshold values
     */
    @org.junit.Test
    public void testCustomConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1e-5, 1.0}, {1.0, 1e-5}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1e-8, 1e-12);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        DecompositionSolver solver = cholesky.getSolver();
        assertEquals(1e-5, l.getEntry(0, 0), 1e-12);
        assertEquals(0.0, l.getEntry(0, 1), 1e-12);
        assertEquals(1e-5, l.getEntry(1, 1), 1e-12);
        assertEquals(0.0, l.getEntry(1, 0), 1e-12);
        assertEquals(Math.sqrt(1e-5), lt.getEntry(0, 0), 1e-12);
        assertEquals(0.0, lt.getEntry(0, 1), 1e-12);
        assertEquals(Math.sqrt(1e-5), lt.getEntry(1, 0), 1e-12);
        assertEquals(-1.0 / (2.0 * Math.sqrt(1e-5)), lt.getEntry(1, 1), 1e-12);
        RealVector b = new RealVectorImpl(new double[]{1.0, 1.0});
        RealVector x = solver.solve(b);
        assertEquals(0.5, x.getEntry(0), 1e-12);
        assertEquals(0.5, x.getEntry(1), 1e-12);
    }

    /**
     * Test exception handling when given a non-square matrix
     */
    @org.junit.Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0}, {1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test exception handling when given a non-symmetric matrix
     */
    @org.junit.Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0}, {1.0, 3.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test exception handling when given a non-positive definite matrix
     */
    @org.junit.Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {-1.0, 0.0}, {0.0, -1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test determinant calculation
     */
    @org.junit.Test
    public void testDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0}, {1.0, 2.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        double determinant = cholesky.getDeterminant();
        assertEquals(3.0, determinant, 1e-15);
    }

    /**
     * Test solving a system of linear equations
     */
    @org.junit.Test
    public void testSolve() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0}, {1.0, 2.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealVector b = new RealVectorImpl(new double[]{1.0, 1.0});
        RealVector x = cholesky.getSolver().solve(b);
        assertEquals(0.5, x.getEntry(0), 1e-15);
        assertEquals(0.5, x.getEntry(1), 1e-15);
    }

    /**
     * Test solving a system of linear equations with singular value decomposition (SVD)
     */
    @org.junit.Test
    public void testSolveWithSVD() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0}, {1.0, 2.0}});
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        RealMatrix V = svd.getV();
        RealVector b = new RealVectorImpl(new double[]{1.0, 1.0});
        RealVector x = V.transpose().multiply(svd.getU().multiply(new RealVectorImpl(new double[]{1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)})));
        RealVector xCholesky = new CholeskyDecompositionImpl(matrix).getSolver().solve(b);
        assertEquals(0.5, x.getEntry(0), 1e-15);
        assertEquals(0.5, x.getEntry(1), 1e-15);
        assertEquals(0.5, xCholesky.getEntry(0), 1e-15);
        assertEquals(0.5, xCholesky.getEntry(1), 1e-15);
    }

    /**
     * Test solving a system of linear equations with large off-diagonal elements
     */
    @org.junit.Test
    public void testSolveWithLargeOffDiagonalElements() {
        Random random = new Random(123);
        int n = 100;
        double epsilon = 1e-15;
        double[][] values = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                values[i][j] = random.nextGaussian() * 10.0;
                if (i == j) {
                    values[i][j] += 1.0;
                }
            }
        }
        RealMatrix matrix = new Array2DRowRealMatrix(values);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealVector b = new RealVectorImpl(new double[n]);
        for (int i = 0; i < n; i++) {
            b.setEntry(i, random.nextGaussian() * 10.0);
        }
        RealVector x = cholesky.getSolver().solve(b);
        for (int i = 0; i < n; i++) {
            double xI = x.getEntry(i);
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    double lIJ = cholesky.getL().getEntry(i, j);
                    double lJI = cholesky.getL().getEntry(j, i);
                    double maxDelta = Precision.safeEpsilon() * Math.max(Math.abs(lIJ), Math.abs(lJI));
                    assertFalse("Off-diagonal elements are too close: i=" + i + ", j=" + j +
                            ", lIJ=" + lIJ + ", lJI=" + lJI + ", maxDelta=" + maxDelta +
                            ", xI=" + xI + ", xJ=" + x.getEntry(j), Math.abs(lIJ - lJI) > maxDelta);
                }
                assertTrue("Diagonal elements are not positive: i=" + i + ", xI=" + xI, xI > epsilon);
            }
        }
    }

    /**
     * Test solving a system of linear equations with small diagonal elements
     */
    @org.junit.Test
    public void testSolveWithSmallDiagonalElements() {
        Random random = new Random(123);
        int n = 100;
        double epsilon = 1e-15;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[n][n]);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    matrix.setEntry(i, j, random.nextDouble() * 1e-5);
                } else {
                    matrix.setEntry(i, j, random.nextGaussian() * 10.0);
                }
            }
        }
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealVector b = new RealVectorImpl(new double[n]);
        for (int i = 0; i < n; i++) {
            b.setEntry(i, random.nextGaussian() * 10.0);
        }
        RealVector x = cholesky.getSolver().solve(b);
        for (int i = 0; i < n; i++) {
            double xI = x.getEntry(i);
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    double lIJ = cholesky.getL().getEntry(i, j);
                    double lJI = cholesky.getL().getEntry(j, i);
                    double maxDelta = Precision.safeEpsilon() * Math.max(Math.abs(lIJ), Math.abs(lJI));
                    assertFalse("Off-diagonal elements are too close: i=" + i + ", j=" + j +
                            ", lIJ=" + lIJ + ", lJI=" + lJI + ", maxDelta=" + maxDelta +
                            ", xI=" + xI + ", xJ=" + x.getEntry(j), Math.abs(lIJ - lJI) > maxDelta);
                }
                assertTrue("Diagonal elements are not positive: i=" + i + ", xI=" + xI, xI > epsilon);
            }
        }
    }

}