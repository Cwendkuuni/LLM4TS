package org.apache.commons.math.linear.test;

import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear. travail.CholeskyDecomposition;
import org.apache.commons.math3.linear.traval.CholeskyDecompositionImpl;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link CholeskyDecompositionImpl} class.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test Cholesky decomposition for 2x2 identity matrix.
     */
    @Test
    public void testIdentityMatrix() {
        final double[][] identityMatrix = {{1.0, 0.0}, {0.0, 1.0}};
        final RealMatrix matrix = new Array2DRowRealMatrix(identityMatrix);
        CholeskyDecomposition choleskyDecomposition;
        try {
            choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        } catch (NotPositiveDefiniteMatrixException | NotSymmetricMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
            return;
        }

        final RealMatrix l = choleskyDecomposition.getL();
        final RealMatrix lT = choleskyDecomposition.getLT();
        Assert.assertEquals("L is not equal to the expected L",
                MatrixUtils.createRealIdentityMatrix(2), l);
        Assert.assertEquals("LT is not equal to the expected LT",
                lT, l.transpose());

        final RealVector b = new RealVectorImpl(new double[]{1.0, 2.0});
        final RealVector x = choleskyDecomposition.getSolver().solve(b);
        Assert.assertEquals("Solution is not equal to the expected solution",
                new double[]{1.0, 1.0}, x.toArray());
    }

    /**
     * Test Cholesky decomposition for 2x2 non-symmetric matrix.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNonSymmetricMatrix() {
        final double[][] matrix = {{1.0, 1.0}, {0.0, 1.0}};
        final RealMatrix matrixInstance = new Array2DRowRealMatrix(matrix);
        new CholeskyDecompositionImpl(matrixInstance);
    }

    /**
     * Test Cholesky decomposition for 2x2 non-positive definite matrix.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() {
        final double[][] matrix = {{1.0, 2.0}, {2.0, -1.0}};
        final RealMatrix matrixInstance = new Array2DRowRealMatrix(matrix);
        new CholeskyDecompositionImpl(matrixInstance);
    }

    /**
     * Test Cholesky decomposition for 3x3 identity matrix.
     */
    @Test
    public void testThreeByThreeIdentityMatrix() {
        final double[][] identityMatrix = {{1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        final RealMatrix matrix = new Array2DRowRealMatrix(identityMatrix);
        CholeskyDecomposition choleskyDecomposition;
        try {
            choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        } catch (NotPositiveDefiniteMatrixException | NotSymmetricMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
            return;
        }

        final RealMatrix l = choleskyDecomposition.getL();
        final RealMatrix lT = choleskyDecomposition.getLT();
        Assert.assertEquals("L is not equal
    to the expected L",
                MatrixUtils.createRealIdentityMatrix(3), l);
        Assert.assertEquals("LT is not equal to the expected LT",
                lT, l.transpose());

        final RealVector b = new RealVectorImpl(new double[]{1.0, 2.0, 3.0});
        final RealVector x = choleskyDecomposition.getSolver().solve(b);
        Assert.assertEquals("Solution is not equal to the expected solution",
                new double[]{1.0, 0.0, 1.0}, x.toArray());
    }
}