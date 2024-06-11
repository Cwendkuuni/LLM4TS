package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link CholeskyDecompositionImpl}.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Tests the Cholesky decomposition of a positive definite matrix.
     */
    @Test
    public void testDecompositionOfPositiveDefiniteMatrix() {
        double[][] matrixData = {
            {1.0, 0.5, 0.5},
            {0.5, 1.5, 1.0},
            {0.5, 1.0, 1.5}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lt = choleskyDecomposition.getLT();

        Assert.assertEquals("L is not equal to LT transposed", l.transpose(), lt);
        SingularValueDecomposition svd = l.getSingularValueDecomposition();
        Assert.assertEquals("L is not invertible", 1.0, svd.getS().get(0), 1e-14);
        RealMatrix inverseL = l.getInverse();
        RealMatrix product = l.multiply(inverseL);
        Assert.assertEquals("L * L<sup>-1</sup> is not the identity matrix", MatrixUtils.createIdentityMatrix(3), product, 1e-14);
    }

    /**
     * Tests the Cholesky decomposition of a symmetric matrix.
     */
    @Test
    public void testDecompositionOfSymmetricMatrix() {
        double[][] matrixData = {
            {1.0, 1.0, 1.0},
            {1.0, 1.5, 1.5},
            {1.0, 1.5, 2.5}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        try {
            new CholeskyDecompositionImpl(matrix);
            Assert.fail("Should have thrown NotSymmetricMatrixException");
        } catch (NotSymmetricMatrixException e) {
            // Expected
        }
    }

    /**
     * Tests the Cholesky decomposition of a non-positive definite matrix.
     */
    @Test
    public void testDecompositionOfNonPositiveDefiniteMatrix() {
        double[][] matrixData = {
            {1.0, 1.0, -1.0},
            {1.0, 1.5, 1.5},
            {-1.0, 1.5, 2.5}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        try {
            new CholeskyDecompositionImpl(matrix);
            Assert.fail("Should have thrown NotPositiveDefiniteMatrixException");
        } catch (NotPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    /**
     * Tests the solve method.
     */
    @Test
    public void testSolve() {
        double[][] matrixData = {
            {1.0, 0.5, 0.5},
            {0.5, 1.5, 1.0},
            {0.5, 1.0, 1.5}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);

        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector x = choleskyDecomposition.getSolver().solve(b);

        RealVector expected = new ArrayRealVector(new double[]{3.0 / 2.0, 1.0, 1.0});
        Assert.assertEquals("The solution is not correct", expected, x, 1e-14);
    }

}