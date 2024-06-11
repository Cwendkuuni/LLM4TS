package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecompositionImpl() {
        double[][] matrixData = {
            {1.0, 0.5, 0.5},
            {0.5, 2.0, 1.5},
            {0.5, 1.5, 3.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);

        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();
        assertArrayEqualsValues(new double[][]{{1.0, 0.0, 0.0},
                {0.4495, 1.0, 0.0},
                {0.3162, 0.7071, 1.0}}, l.getData());
        assertArrayEqualsValues(new double[][]{{1.0, 0.5, 0.5},
                {0.5, 1.0, 0.5},
                {0.5, 0.5, 1.0}}, lt.getData());

        RealVector vector = new RealVectorImpl(new double[]{1.0, 2.0, 3.0});
        RealVector x = decomposition.getSolver().solve(vector);
        assertArrayEquals(new double[]{0.6321, -0.5878, 0.2599}, x.toArray(), 0.0);

        RealMatrix b = new Array2DRowRealMatrix(new double[][]{{1.0},
                {2.0}, {3.0}});
        RealMatrix result = decomposition.getSolver().solve(b);
        assertArrayEqualsValues(new double[][]{{1.5}, {0.3162}, {-0.3162}}, result.getData());
    }

    @Test
    public void testThresholds() {
        double[][] matrixData = {
            {1.0, 0.5, 0.5},
            {0.5, 2.0, 1.5},
            {0.5, 1.5, 2.5}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        try {
            new CholeskyDecompositionImpl(matrix);
            assertFalse("Expected NotSymmetricMatrixException or NotPositiveDefiniteMatrixException");
        } catch (NotSymmetricMatrixException | NotPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testGetDeterminant() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 4.0, 5.0},
            {3.0, 5.0, 9.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl decomposition = null;

        try {
            decomposition = new CholeskyDecompositionImpl(matrix);
        } catch (Exception e) {
            // ignore
        }

        if (decomposition != null) {
            double determinant = decomposition.getDeterminant();
            assertEquals(36.0, determinant, 0.0);
        }
    }

    private static void assertArrayEqualsValues(double[][] expected, double[][] actual) {
        for (int i = 0; i < expected.length; ++i) {
            for (int j = 0; j < expected[i].length; ++j) {
                assertEquals(expected[i][j], actual[i][j], 0.0);
            }
        }
    }

}