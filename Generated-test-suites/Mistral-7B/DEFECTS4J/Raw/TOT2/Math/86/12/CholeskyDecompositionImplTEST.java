package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.VectorUtils;

public class CholeskyDecompositionImplTest {

    /**
     * Test Cholesky decomposition of a symmetric positive definite matrix.
     */
    @Test
    public void testCholeskyDecomposition() {
        // create test matrix
        final double[][] testMatrixData = {
            {2.0, 1.0, 1.0},
            {1.0, 2.0, 1.0},
            {1.0, 1.0, 3.0}
        };
        final RealMatrix testMatrix = new Array2DRowRealMatrix(testMatrixData, false);

        // create CholeskyDecompositionImpl instance
        final CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(testMatrix);

        // check if L is correct
        final RealMatrix l = choleskyDecomposition.getL();
        assertEquals(testMatrix, l.transpose().multiply(l));

        // check if determinant is correct
        assertEquals(VectorUtils.dotProduct(new RealVectorImpl(new double[] {2.0, 1.0, 1.0}), l.getColumnVector(0)), choleskyDecomposition.getDeterminant());

        // check if solver is working
        final double[] b = {1.0, 2.0, 3.0};
        final RealVector x = choleskyDecomposition.getSolver().solve(new RealVectorImpl(b));
        assertEquals(new double[] {1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(3.0)}, x.toArray());
    }

    /**
     * Test Cholesky decomposition of a non-symmetric matrix
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrix() {
        // create test matrix
        final double[][] testMatrixData = {
            {2.0, 1.0},
            {1.0, 2.0}
        };
        final RealMatrix testMatrix = new Array2DRowRealMatrix(testMatrixData, false);

        // create CholeskyDecompositionImpl instance
        new CholeskyDecompositionImpl(testMatrix);
    }

    /**
     * Test Cholesky decomposition of a non-positive definite matrix
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrix() {
        // create test matrix
        final double[][] testMatrixData = {
            {1.0, 1.0},
            {1.0, 0.0}
        };
        final RealMatrix testMatrix = new Array2DRowRealMatrix(testMatrixData, false);

        // create CholeskyDecompositionImpl instance
        new CholeskyDecompositionImpl(testMatrix);
    }

    /**
     * Test Cholesky decomposition of an identity matrix
     */
    @Test
    public void testIdentityMatrix() {
        // create test matrix
        final double[][] testMatrixData = {
            {1.0, 0.0},
            {0.0, 1.0}
        };
        final RealMatrix testMatrix = new Array2DRowRealMatrix(testMatrixData, false);

        // create CholeskyDecompositionImpl instance
        final CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(testMatrix);

        // check if L is correct
        final RealMatrix l = choleskyDecomposition.getL();
        assertEquals(testMatrix, l.transpose().multiply(l));

        // check if determinant is correct
        assertEquals(1.0, choleskyDecomposition.getDeterminant());

        // check if solver is working
        final double[] b = {1.0, 1.0};
        final RealVector x = choleskyDecomposition.getSolver().solve(new RealVectorImpl(b));
        assertEquals(new double[] {1.0, 1.0}, x.toArray());
    }

}