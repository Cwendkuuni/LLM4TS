package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNullMatrix() {
        new CholeskyDecompositionImpl(null);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrix() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0, 4.0}};
        new CholeskyDecompositionImpl(new Array2DRowRealMatrix(data, false));
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrix() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}};
        new CholeskyDecompositionImpl(new Array2DRowRealMatrix(data, false), 0.0, 0.0);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrix() {
        double[][] data = {{-1.0, 0.0}, {0.0, -1.0}};
        new CholeskyDecompositionImpl(new Array2DRowRealMatrix(data, false), 0.0, 0.0);
    }

    @Test
    public void testIdentity() {
        double[][] identityData = {{1.0}, {0.0}};
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(new Array2DRowRealMatrix(identityData, false));
        RealMatrix L = decomposition.getL();
        assertEquals(L.getRowDimension(), L.getColumnDimension());
        assertEquals(L.getRowDimension(), 1);
        assertEquals(L.getColumnDimension(), 1);
        assertArrayEquals(new double[][]{{1.0, 0.0}}, L.getData());
    }

    @Test
    public void testDecomposition() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 5.0, 8.0}, {3.0, 8.0, 13.0}};
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(new Array2DRowRealMatrix(data, false), 0.0, 0.0);
        RealMatrix L = decomposition.getL();
        RealMatrix LT = decomposition.getLT();
        assertEquals(L.getRowDimension(), L.getColumnDimension(), "L is not square");
        assertEquals(LT.getRowDimension(), L.getColumnDimension(), "LT is not square");
        assertEquals(L.getRowDimension(), LT.getRowDimension(), "L and LT have different dimensions");

        assertNotNull(L);
        assertNotNull(LT);

        assertArrayEquals(new double[][]{{3.0, 2.0, 1.0}, {1.5, 2.5, 1.0}, {1.0, 1.5, 2.5}}, L.getData());
        assertArrayEquals(new double[][]{{3.0, 1.5, 1.0}, {2.0, 2.5, 1.5}, {1.0, 1.0, 2.5}}, LT.getData());

        RealMatrix expectedInvL = new Array2DRowRealMatrix(new double[][]{{1.0 / 3.0, -2.0 / 3.0, 1.0 / 6.0},
                                                                            {-1.0 / 6.0, (1.0 / 3.0) * (5.0 / 3.0), -2.0 / 3.0},
                                                                            {-1.0 / 6.0, -2.0 / 3.0, (1.0 / 3.0) * (8.0 / 3.0)}}, false);
        RealMatrix invL = decomposition.getSolver().getInverse();
        assertArrayEquals(expectedInvL.getData(), invL.getData());

        RealVector expectedSolution = new Array2DRowRealMatrix(new double[][]{{1.0}, {2.0}, {3.0}}, false).transpose().getColumnVector(0);
        RealVector solution = decomposition.getSolver().solve(new Array2DRowRealMatrix(new double[][]{{1.0}, {2.0}, {3.0}}, false).transpose());
        assertArrayEquals(expectedSolution.toArray(), solution.toArray());
    }

    @Test
    public void testDeterminant() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 5.0, 8.0}, {3.0, 8.0, 13.0}};
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(new Array2DRowRealMatrix(data, false), 0.0, 0.0);
        assertEquals(Math.pow(1.0, 3.0) * (1.0 * 5.0 * (3.0 * 8.0 - 2.0 * 2.0)) * (13.0 - 3.0 * 3.0),
                     decomposition.getDeterminant(), 1e-10);
    }

    @Test
    public void testSolverNonSingular() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 5.0, 8.0}, {3.0, 8.0, 13.0}};
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(new Array2DRowRealMatrix(data, false), 0.0, 0.0);

        double[] b = new double[]{1.0, 2.0, 3.0};
        RealVector solution = decomposition.getSolver().solve(new RealVectorImpl(b, false));
        assertFalse(solution.isZeroVector());
        assertEquals(solution.getDimension(), 3);

        RealVector x = new RealVectorImpl(new double[]{1.0, 2.0, 3.0}, false);
        RealVector expected = new RealVectorImpl(new double[]{1.5, 2.0, 1.0}, false);
        assertArrayEquals(expected.toArray(), solution.toArray());

        RealMatrix A = new Array2DRowRealMatrix(data, false);
        RealVector y = new RealVectorImpl(A.transpose().operate(solution), false);
        assertTrue(A.multiply(solution).checkRowSagittalEquality(y, 1e-10));
    }

    @Test(expected = InvalidMatrixException.class)
    public void testSolverSingular() {
        double[][] data = {{1.0, 0.0, 3.0}, {0.0, 0.0, 8.0}, {3.0, 8.0, 0.0}};
        new CholeskyDecompositionImpl(new Array2DRowRealMatrix(data, false), 0.0, 0.0).getSolver().solve(new RealVectorImpl(new double[]{1.0, 2.0, 3.0}, false));
    }

}