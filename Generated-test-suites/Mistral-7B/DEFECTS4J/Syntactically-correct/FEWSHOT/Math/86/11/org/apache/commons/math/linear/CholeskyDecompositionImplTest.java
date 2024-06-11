package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecomposition() {
        double[][] data = {
            {1.0, 2.0, 3.0, 4.0},
            {2.0, 3.0, 4.0, 5.0},
            {3.0, 4.0, 5.0, 6.0},
            {4.0, 5.0, 6.0, 7.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();

        assertNotNull(l);
        assertNotNull(lt);
        assertEquals(matrix.getRowDimension(), l.getRowDimension());
        assertEquals(matrix.getColumnDimension(), l.getColumnDimension());

        assertEquals(0.0, l.getEntry(0, 0), 1e-14);
        assertEquals(1.0 / Math.sqrt(1.0), l.getEntry(0, 1), 1e-14);
        assertEquals(1.0 / Math.sqrt(2.0), l.getEntry(1, 1), 1e-14);
        assertEquals(1.0 / Math.sqrt(3.0), l.getEntry(2, 1), 1e-14);
        assertEquals(1.0 / Math.sqrt(4.0), l.getEntry(3, 1), 1e-14);
        assertEquals(1.0 / Math.sqrt(5.0), l.getEntry(3, 2), 1e-14);
        assertEquals(1.0 / Math.sqrt(6.0), l.getEntry(3, 3), 1e-14);

        assertArrayEquals(new double[][]{{1.0 / Math.sqrt(1.0), 0.0, 0.0, 0.0},
                {-1.0 * l.getEntry(1, 1) / Math.sqrt(1.0), 1.0 / Math.sqrt(2.0), 0.0, 0.0},
                {-1.0 * l.getEntry(2, 1), -1.0 * l.getEntry(2, 2) / Math.sqrt(2.0), 1.0 / Math.sqrt(3.0), 0.0},
                {-1.0 * l.getEntry(3, 1), -1.0 * l.getEntry(3, 2), -1.0 * l.getEntry(3, 3) / Math.sqrt(3.0), 1.0 / Math.sqrt(4.0)}},
                lTData, 1e-14);

        RealMatrix expectedLt = new Array2DRowRealMatrix(new double[][]{{1.0 / Math.sqrt(1.0), 0.0, 0.0, 0.0},
                {-1.0 * l.getEntry(1, 1) / Math.sqrt(1.0), 1.0 / Math.sqrt(2.0), 0.0, 0.0},
                {-1.0 * l.getEntry(2, 1), -1.0 * l.getEntry(2, 2) / Math.sqrt(2.0), 1.0 / Math.sqrt(3.0), 0.0},
                {-1.0 * l.getEntry(3, 1), -1.0 * l.getEntry(3, 2), -1.0 * l.getEntry(3, 3) / Math.sqrt(3.0), 1.0 / Math.sqrt(4.0)}}, false);

        assertEquals(matrix, expectedLt.transpose().multiply(l));

        RealMatrix inverse = decomposition.getSolver().getInverse();
        RealMatrix expectedInverse = MatrixUtils.createRealIdentityMatrix(matrix.getRowDimension());
        assertEquals(expectedInverse, inverse);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        double[][] data = {
            {1.0, 2.0},
            {3.0, 4.0, 5.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        double[][] data = {
            {1.0, 2.0},
            {2.0, 3.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        new CholeskyDecompositionImpl(matrix, 1e-15, 1e-10);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        double[][] data = {
            {-1.0, 2.0},
            {2.0, 3.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        new CholeskyDecompositionImpl(matrix, 1e-15, 1e-10);
    }

    @Test
    public void testSolve() {
        double[][] data = {
            {1.0, 2.0},
            {3.0, 4.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealVector b = new RealVectorImpl(new double[]{1.0, 2.0}, false);
        RealVector x = decomposition.getSolver().solve(b);

        double[] expectedX = new double[]{1.0 / (Math.sqrt(1.0) + Math.sqrt(2.0)),
                1.0 / (Math.sqrt(1.0) - Math.sqrt(2.0))};
        assertArrayEquals(expectedX, x.toArray(), 1e-14);
    }

    @Test
    public void testSolveWithIdentityMatrix() {
        double[][] data = {
            {1.0, 0.0},
            {0.0, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealVector b = new RealVectorImpl(new double[]{1.0, 0.0}, false);
        RealVector x = decomposition.getSolver().solve(b);

        double[] expectedX = new double[]{1.0, 0.0};
        assertArrayEquals(expectedX, x.toArray(), 1e-14);
    }

}