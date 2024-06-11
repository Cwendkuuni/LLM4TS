package org.apache.commons.math.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;

public class CholeskyDecompositionImplTest {

    @Test
    public void testDefaultConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        CholeskyDecompositionImpl decomposition = null;
        try {
            decomposition = new CholeskyDecompositionImpl(matrix);
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected
        }
        assertNull(decomposition);
    }

    @Test
    public void testSymmetricMatrixDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        CholeskyDecompositionImpl decomposition = null;
        try {
            decomposition = new CholeskyDecompositionImpl(matrix, 1.0e-14, 1.0e-14);
        } catch (NotSquareMatrixException | NotSymmetricMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.0, 0.86602540378444},
                {0.0, 0.0}});
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 0.5},
                {0.5, 0.86602540378444},
                {0.5, 0.43301270189222},
                {0.0, 0.0}});
        assertEquals(expectedL.toString(), l.toString());
        assertEquals(expectedLT.toString(), lt.toString());
    }

    @Test
    public void testNonSymmetricMatrixDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.5}});
        CholeskyDecompositionImpl decomposition = null;
        try {
            decomposition = new CholeskyDecompositionImpl(matrix, 1.0e-14, 1.0e-14);
            fail("Unexpected exception: decomposition of non-symmetric matrix");
        } catch (NotSymmetricMatrixException e) {
            // expected
        }
        assertNull(decomposition);
    }

    @Test
    public void testPositiveDefiniteMatrixDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4.0, 1.0}, {1.0, 3.0}});
        CholeskyDecompositionImpl decomposition = null;
        try {
            decomposition = new CholeskyDecompositionImpl(matrix, 1.0e-14, 1.0e-14);
        } catch (NotPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{{0.5, 1.0 / 2.0}, {0.0, Math.sqrt(3.0 / 2.0)}});
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][]{
                {0.5, 1.0 / 2.0},
                {0.5, 1.0 / 2.0},
                {0.5, 1.0 / 2.0},
                {0.0, Math.sqrt(3.0 / 2.0)}});
        assertEquals(expectedL.toString(), l.toString());
        assertEquals(expectedLT.toString(), lt.toString());
    }

    @Test
    public void testSingularMatrixDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        CholeskyDecompositionImpl decomposition = null;
        try {
            decomposition = new CholeskyDecompositionImpl(matrix, 1.0e-14, 1.0e-14);
            fail("Unexpected exception: decomposition of singular matrix");
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected
        }
        assertNull(decomposition);
    }

    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        CholeskyDecompositionImpl decomposition = null;
        try {
            decomposition = new CholeskyDecompositionImpl(matrix, 1.0e-14, 1.0e-14);
        } catch (NotPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        double determinant = decomposition.getDeterminant();
        assertEquals(1.0, determinant, 1.0e-14);
    }

    @Test
    public void testGetSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        CholeskyDecompositionImpl decomposition = null;
        try {
            decomposition = new CholeskyDecompositionImpl(matrix, 1.0e-14, 1.0e-14);
        } catch (NotPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        DecompositionSolver solver = decomposition.getSolver();
        RealVector x = solver.solve(new Array2DRowRealMatrix(new double[][]{{1.0}, {1.0}}).transpose()).getDataRef();
        RealVector expectedX = new RealVectorImpl(new double[]{0.5, -0.5}, false);
        assertArrayEquals(expectedX.toArray(), x, 1.0e-14);
    }

    @Test
    public void testGetSolverWithSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        CholeskyDecompositionImpl decomposition = null;
        try {
            decomposition = new CholeskyDecompositionImpl(matrix, 1.0e-14, 1.0e-14);
        } catch (NotPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        DecompositionSolver solver = decomposition.getSolver();
        RealVector x = solver.solve(new Array2DRowRealMatrix(new double[][]{{1.0}, {1.0}}).transpose()).getDataRef();
        assertEquals(0.0, x[0], 1.0e-14);
    }

    @Test
    public void testGetSolverWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}, {3.0, 4.0}});
        CholeskyDecompositionImpl decomposition = null;
        try {
            decomposition = new CholeskyDecompositionImpl(matrix, 1.0e-14, 1.0e-14);
        } catch (NotPositiveDefiniteMatrixException | NotSquareMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        DecompositionSolver solver = decomposition.getSolver();
        RealVector x = null;
        try {
            x = solver.solve(new Array2DRowRealMatrix(new double[][]{{1.0}, {1.0}, {1.0}}).transpose()).getDataRef();
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertNull(x);
    }

    @Test
    public void testInverse() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        CholeskyDecompositionImpl decomposition = null;
        try {
            decomposition = new CholeskyDecompositionImpl(matrix, 1.0e-14, 1.0e-14);
        } catch (NotPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        RealMatrix inverse = decomposition.getInverse();
        RealMatrix expectedInverse = new SingularValueDecomposition(MatrixUtils.createRealMatrix(matrix.getData())).getInverse();
        assertEquals(expectedInverse.toString(), inverse.toString());
    }
}