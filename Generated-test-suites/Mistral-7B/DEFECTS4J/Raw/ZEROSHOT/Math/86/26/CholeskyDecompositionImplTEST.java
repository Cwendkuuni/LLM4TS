package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;

import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 2.0},
            {2.0, 3.0, 4.0},
            {2.0, 4.0, 5.0}
        });

        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);

        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        double det = cholesky.getDeterminant();

        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][] {
            {3.013217997, 0.0, 0.0},
            {1.322580646, 3.013217997, 0.0},
            {0.0, 1.322580646, 3.013217997}
        });

        assertArrayEquals(expectedL.getData(), l.getData(), 0.0);
        assertArrayEquals(lt.transpose().getData(), l.getData(), 0.0);
        assertEquals(det, 3.013217997 * det, 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 2.0},
            {2.0, 3.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 2.0},
            {2.0, 3.0, 4.0},
            {2.0, 4.0, 5.0},
            {2.0, 4.0, 5.0}
        });
        new CholeskyDecompositionImpl(matrix, DEFAULT_RELATIVE_SYMMETRY_THRESHOLD, DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 2.0},
            {2.0, 0.0, 4.0},
            {2.0, 4.0, 5.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 2.0},
            {2.0, 3.0, 4.0},
            {2.0, 4.0, 5.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);

        double[] b = {1.0, 2.0, 3.0};
        double[] x = cholesky.getSolver().solve(b);

        RealVector expectedX = new ArrayRealVector(new double[] {1.0, 0.5, 0.25});
        assertArrayEquals(expectedX.toArray(), x, 0.0);

        RealVector bVector = new ArrayRealVector(b);
        RealVector expectedXVector = cholesky.getSolver().solve(bVector);
        assertArrayEquals(expectedXVector.toArray(), x, 0.0);

        RealMatrix bMatrix = MatrixUtils.createRealMatrix(b);
        RealMatrix expectedXMatrix = cholesky.getSolver().solve(bMatrix);
        assertArrayEquals(expectedXMatrix.getData(), MatrixUtils.createIdentityMatrix(3).transpose().getData(), 0.0);

        RealMatrix inverse = cholesky.getSolver().getInverse();
        RealVector xVector = new ArrayRealVector(new double[]{1.0, 0.0, 0.0});
        RealVector expectedXVectorInverse = cholesky.getSolver().solve(xVector);
        assertArrayEquals(x.clone(), expectedXVectorInverse.toArray(), 0.0);
    }

}