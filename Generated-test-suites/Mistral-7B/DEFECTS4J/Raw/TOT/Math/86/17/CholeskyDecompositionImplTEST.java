package org.apache.commons.math.linear;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class CholeskyDecompositionImplTest {

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(3);
        RealMatrix expectedDecomposition = matrix;
        try {
            CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            RealMatrix result = choleskyDecomposition.getL();
            assertEquals(expectedDecomposition, result);
        } catch (Exception ex) {
            // should not be thrown
            assertNull(ex);
        }
    }

    @Test
    public void testSymmetricMatrix() {
        double[] data = {1.0, 0.5, 0.5, 0.5, 1.0};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        try {
            CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            RealMatrix result = choleskyDecomposition.getL();
            RealMatrix expectedDecomposition = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
            assertEquals(expectedDecomposition, result);
        } catch (Exception ex) {
            // should not be thrown
            assertNull(ex);
        }
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrix() {
        double[] data = {1.0, 0.5, 1.5, 0.5, 1.0};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrix() {
        double[] data = {-1.0, 0.5, 0.5, 0.5, -1.0};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrix() {
        double[] data = {1.0, 0.5, 0.5, 1.0};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        new CholeskyDecompositionImpl(MatrixUtils.createRealMatrix(new double[][]{{1.0}, {2.0, 3.0}}));
    }

    @Test
    public void testDeterminant() {
        double[] data = {1.0, 2.0, 3.0, 2.0, 1.0};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        try {
            CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            double determinant = choleskyDecomposition.getDeterminant();
            assertEquals(1.0 / 6.0, determinant, 1e-10);
        } catch (Exception ex) {
            // should not be thrown
            assertNull(ex);
        }
    }

    @Test
    public void testSolveVector() {
        double[] data = {1.0, 2.0, 3.0, 2.0, 1.0};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        try {
            CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            RealVector x = choleskyDecomposition.getSolver().solve(b);
            RealVector expectedSolution = new ArrayRealVector(new double[]{3.0 / 2.0, 1.0, 1.0 / 3.0});
            assertEquals(expectedSolution, x);
        } catch (Exception ex) {
            // should not be thrown
            assertNull(ex);
        }
    }

    @Test
    public void testSolveMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RealMatrix b = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        try {
            CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            RealMatrix x = choleskyDecomposition.getSolver().solve(b);
            RealMatrix expectedSolution = new SingularValueDecomposition(MatrixUtils.createRealMatrix(new double[][]{{1.0 / 11.0, -2.0 / 11.0}, {-2.0 / 11.0, 1.0 / 11.0}})).getSolveX();
            assertEquals(expectedSolution, x);
        } catch (Exception ex) {
            // should not be thrown
            assertNull(ex);
        }
    }

    @Test
    public void testSolveComplexVector() {
        double[] data = {1.0, 2.0, 3.0, 2.0, 1.0};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RealVector b = new ArrayRealVector(new double[]{1.0, Complex.I});
        try {
            CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            RealVector x = choleskyDecomposition.getSolver().solve(b);
            RealVector expectedSolution = new ArrayRealVector(new double[]{1.0 / Math.sqrt(2.0), -Complex.I / Math.sqrt(2.0)});
            assertEquals(expectedSolution, x);
        } catch (Exception ex) {
            // should not be thrown
            assertNull(ex);
        }
    }

    @Test
    public void testSolveComplexMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RealMatrix b = MatrixUtils.createRealMatrix(new double[][]{{1.0, Complex.I}, {2.0, -Complex.I}});
        try {
            CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            RealMatrix x = choleskyDecomposition.getSolver().solve(b);
            RealMatrix expectedSolution = MatrixUtils.createRealMatrix(new double[][]{{1.0 / Math.sqrt(2.0), -Complex.I / Math.sqrt(2.0)}, {-Complex.I / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)}});
            assertEquals(expectedSolution, x);
        } catch (Exception ex) {
            // should not be thrown
            assertNull(ex);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidVectorB() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0, 4.0});
        new CholeskyDecompositionImpl(matrix).getSolver().solve(b);
    }

    @Test(expected = InvalidMatrixException.class)
    public void testSingularMatrix() {
        double[][] data = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 0.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        new CholeskyDecompositionImpl(matrix);
    }

}