package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.MatrixUtils;

import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecompositionImplConstructorWithDefaultThreshold() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testCholeskyDecompositionImplConstructorWithCustomThreshold() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0e-5, 0.1, 0.1}, {0.1, 1.0e-5, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix, 1e-12, 1e-8);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testCholeskyDecompositionImplConstructorWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1}, {0.1, 0.1}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testCholeskyDecompositionImplConstructorWithNotSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.5}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testCholeskyDecompositionImplConstructorWithNotPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, -1.0, 0.1}, {0.1, 0.1, -1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testGetL() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        assertNotNull(l);
        SingularValueDecomposition svd = new SingularValueDecompositionImpl(l);
        assertEquals(3, svd.getN());
        assertEquals(3, svd.getM());
        assertEquals(1.0, svd.getS()[0], 1e-15);
        assertEquals(1.0, svd.getS()[1], 1e-15);
        assertEquals(1.0, svd.getS()[2], 1e-15);
    }

    @Test
    public void testGetLT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix lt = choleskyDecomposition.getLT();
        assertNotNull(lt);
        SingularValueDecomposition svd = new SingularValueDecompositionImpl(lt.transpose());
        assertEquals(3, svd.getN());
        assertEquals(3, svd.getM());
        assertEquals(1.0, svd.getS()[0], 1e-15);
        assertEquals(1.0, svd.getS()[1], 1e-15);
        assertEquals(1.0, svd.getS()[2], 1e-15);
    }

    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        double determinant = choleskyDecomposition.getDeterminant();
        assertEquals(1.0, determinant, 1e-15);
    }

    @Test
    public void testGetSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        assertNotNull(solver);
    }

    @Test
    public void testSolverWithVector() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        double[] b = {1.0, 2.0, 3.0};
        double[] x = choleskyDecomposition.getSolver().solve(b);
        assertArrayEquals(new double[]{1.0, 0.0, 0.0}, x, 1e-15);
    }

    @Test
    public void testSolverWithSingularVector() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 0.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        double[] b = {1.0, 0.0, 1.0};
        assertThrows(IllegalArgumentException.class, () -> choleskyDecomposition.getSolver().solve(b));
    }

    @Test
    public void testSolverWithRealVector() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealVector b = new RealVectorImpl(new double[]{1.0, 2.0, 3.0});
        RealVector x = choleskyDecomposition.getSolver().solve(b);
        assertEquals(b, x);
    }

    @Test
    public void testSolverWithSingularRealVector() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 0.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealVector b = new RealVectorImpl(new double[]{1.0, 0.0, 1.0});
        assertNull(choleskyDecomposition.getSolver().solve(b));
    }

    @Test
    public void testSolverMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix b = MatrixUtils.createRealIdentityMatrix(3);
        RealMatrix x = choleskyDecomposition.getSolver().solve(b);
        RealMatrix expected = MatrixUtils.createRealIdentityMatrix(3);
        assertArrayEquals(expected.getData(), x.getData(), 1e-15);
    }

    @Test
    public void testSolverWithSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 0.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix b = MatrixUtils.createRealIdentityMatrix(3).addScalar(1.0);
        assertNull(choleskyDecomposition.getSolver().solve(b));
    }

    @Test
    public void testGetInverse() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix inverse = choleskyDecomposition.getInverse();
        assertNotNull(inverse);
        assertFalse(inverse.isSymmetric());
    }
}