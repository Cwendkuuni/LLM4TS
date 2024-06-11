package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;

import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        });

        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);

        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();

        RealVector lDiagonal = l.getColumnVector(0);
        double[] lDiagonalData = lDiagonal.getDataRef();
        for (int i = 0; i < lDiagonalData.length; i++) {
            assertEquals("L[" + i + "]", Math.sqrt(matrix.getEntry(i, i)), lDiagonalData[i], 1e-14);
        }

        RealMatrix expectedLt = new SingularValueDecomposition(matrix.transpose()).getU();
        assertEquals("LT", expectedLt, lt);

        RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][]{
                {1.0 / lDiagonalData[0], 0.0, 0.0},
                {0.5 * (2.0 / lDiagonalData[0] - 1.0 / lDiagonalData[1]), 1.0 / lDiagonalData[1], 0.0},
                {0.5 * (3.0 / lDiagonalData[0] - 1.0 / lDiagonalData[1] - 1.0 / lDiagonalData[2]),
                        0.5 * (3.0 / lDiagonalData[1] - 1.0 / lDiagonalData[2]), 1.0 / lDiagonalData[2]}
        });
        assertEquals("L", expectedL, l);

        RealMatrix expectedLT = l.transpose();
        assertEquals("LT", expectedLT, lt);

        RealMatrix inverse = cholesky.getSolver().getInverse();
        RealMatrix expectedInverse = MatrixUtils.createRealMatrix(new double[][]{
                {1.0 / lDiagonalData[0], 0.0, 0.0},
                {-0.5 * (2.0 / lDiagonalData[0] - 1.0 / lDiagonalData[1]), 1.0 / lDiagonalData[1], 0.0},
                {-0.5 * (3.0 / lDiagonalData[0] - 1.0 / lDiagonalData[1] - 1.0 / lDiagonalData[2]),
                        -0.5 * (3.0 / lDiagonalData[1] - 1.0 / lDiagonalData[2]), 1.0 / lDiagonalData[2]}
        });
        assertEquals("Inverse", expectedInverse, inverse);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {2.0, 3.0},
                {3.0, 4.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {1.0, 1.0}
        });
        new CholeskyDecompositionImpl(matrix, 0.0, 0.0);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {2.0, -1.0}
        });
        new CholeskyDecompositionImpl(matrix, 0.0, 0.0);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixExceptionWithNegativeDiagonal() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {2.0, -1.0},
                {3.0, 3.0}
        });
        new CholeskyDecompositionImpl(matrix, 0.0, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {2.0, 3.0}
        });
        new CholeskyDecompositionImpl(matrix, 1.0, 1.0);
    }
}