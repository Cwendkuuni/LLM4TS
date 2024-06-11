package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.SingularValueDecomposition;

import org.junit.Test;

public class CholeskyDecompositionImplTest {
    @Test
    public void testCreate() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { {1.0, 2.0}, {2.0, 3.0}});
        CholeskyDecompositionImpl decomp = null;
        try {
            decomp = new CholeskyDecompositionImpl(matrix);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
        assertNotNull(decomp);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { {1.0, 2.0}, {2.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { {1.0, 2.0}, {2.0, 3.0}, {1.0, 2.0}});
        new CholeskyDecompositionImpl(matrix, DEFAULT_RELATIVE_SYMMETRY_THRESHOLD, DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { {-1.0, 2.0}, {2.0, -3.0}});
        new CholeskyDecompositionImpl(matrix, DEFAULT_RELATIVE_SYMMETRY_THRESHOLD, DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
    }

    @Test
    public void testGetL() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { {1.0, 2.0}, {2.0, 3.0}});
        CholeskyDecompositionImpl decomp = null;
        try {
            decomp = new CholeskyDecompositionImpl(matrix);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
        RealMatrix l = decomp.getL();
        assertNotNull(l);
        assertEquals(matrix.getRowDimension(), l.getRowDimension());
        assertEquals(matrix.getColumnDimension(), l.getColumnDimension());
    }

    @Test
    public void testGetLT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { {1.0, 2.0}, {2.0, 3.0}});
        CholeskyDecompositionImpl decomp = null;
        try {
            decomp = new CholeskyDecompositionImpl(matrix);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
        RealMatrix lt = decomp.getLT();
        assertNotNull(lt);
        assertEquals(matrix.getRowDimension(), lt.getRowDimension());
        assertEquals(matrix.getColumnDimension(), lt.getColumnDimension());
    }

    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { {1.0, 2.0}, {2.0, 3.0}});
        CholeskyDecompositionImpl decomp = null;
        try {
            decomp = new CholeskyDecompositionImpl(matrix);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
        double determinant = decomp.getDeterminant();
        assertEquals(1.0 * 3.0, determinant, 1e-14);
    }

    @Test
    public void testSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { {1.0, 2.0}, {2.0, 3.0}});
        CholeskyDecompositionImpl decomp = null;
        try {
            decomp = new CholeskyDecompositionImpl(matrix);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        double[] b = new double[] { 1.0, 1.0 };
        RealVector x = decomp.getSolver().solve(b);
        assertNotNull(x);
        assertEquals(2, x.getDimension());

        double[][] xMat = new double[1][matrix.getColumnDimension()];
        RealMatrix xVec = new Array2DRowRealMatrix(xMat);
        RealVector bVec = new ArrayRealVector(b);
        RealMatrix A = matrix.transpose().multiply(matrix);
        RealMatrix AInv = decomp.getSolver().getInverse();
        RealMatrix xExpected = AInv.multiply(bVec);

        RealMatrix xActual = decomp.getSolver().solve(bVec);
        assertEquals(xExpected, xActual);
    }

    @Test
    public void testSolver_RealVector() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { {1.0, 2.0}, {2.0, 3.0}});
        CholeskyDecompositionImpl decomp = null;
        try {
            decomp = new CholeskyDecompositionImpl(matrix);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        RealVector b = new ArrayRealVector(new double[] { 1.0, 1.0 });
        RealVector x = decomp.getSolver().solve(b);
        assertNotNull(x);
        assertEquals(2, x.getDimension());

        double[][] xMat = new double[1][matrix.getColumnDimension()];
        RealMatrix xVec = new Array2DRowRealMatrix(xMat);
        RealVector bVec = b;
        RealMatrix A = matrix.transpose().multiply(matrix);
        RealMatrix AInv = decomp.getSolver().getInverse();
        RealMatrix xExpected = AInv.multiply(bVec);

        RealMatrix xActual = decomp.getSolver().solve(bVec);
        assertEquals(xExpected, xActual);
    }

    @Test
    public void testSolver_RealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { {1.0, 2.0}, {2.0, 3.0}});
        CholeskyDecompositionImpl decomp = null;
        try {
            decomp = new CholeskyDecompositionImpl(matrix);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        RealMatrix b = new Array2DRowRealMatrix(new double[][] { {1.0, 1.0}, {1.0, 1.0} });
        RealMatrix x = decomp.getSolver().solve(b);
        assertNotNull(x);
        assertEquals(matrix.getRowDimension(), x.getRowDimension());
        assertEquals(matrix.getColumnDimension(), x.getColumnDimension());

        double[][] xMat = new double[matrix.getRowDimension()][matrix.getColumnDimension()];
        RealMatrix xVec = new Array2DRowRealMatrix(xMat);
        RealMatrix bVec = b;
        RealMatrix A = matrix.transpose().multiply(matrix);
        RealMatrix AInv = decomp.getSolver().getInverse();
        RealMatrix xExpected = AInv.multiply(bVec);

        RealMatrix xActual = decomp.getSolver().solve(bVec);
        assertEquals(xExpected, xActual);
    }

    @Test
    public void testSolver_SingularValueDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { {1.0, 2.0}, {2.0, 3.0}});
        CholeskyDecompositionImpl decomp = null;
        try {
            decomp = new CholeskyDecompositionImpl(matrix);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        RealMatrix b = new Array2DRowRealMatrix(new double[][] { {1.0, 1.0}, {1.0, 1.0} });

        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        RealMatrix x = svd.getSolve(b);

        RealMatrix xLU = LUDecomposition.getDecomposition(matrix).getSolve(b);

        RealMatrix xActual = decomp.getSolver().solve(b);

        assertEquals(x, xActual);
    }
}