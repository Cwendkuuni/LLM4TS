package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link RectangularCholeskyDecomposition} class.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test the decomposition of a full rank positive semidefinite matrix.
     */
    @Test
    public void testDecompositionFullRank() {

        double[][] data = new double[][]{
            {3.0, 2.0, 1.0},
            {2.0, 3.0, 1.0},
            {1.0, 1.0, 2.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        try {
            RectangularCholeskyDecomposition decomposableMatrix = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root       = decomposableMatrix.getRootMatrix();
            int rank            = decomposableMatrix.getRank();

            assertNotNull(root);
            assertEquals(3, root.getRowDimension());
            assertEquals(rank, root.getColumnDimension());
            assertArrayEquals(new double[]{-1.0 / FastMath.SQRT(3.0), 0.0, 0.0}, root.getColumnVector(0).toArray(), 0.0);
            assertArrayEquals(new double[]{-1.0 / FastMath.SQRT(2.0), 0.5 / FastMath.SQRT(2.0), 0.0}, root.getColumnVector(1).toArray(), 0.0);
            assertArrayEquals(new double[]{0.0, 0.5 / FastMath.SQRT(2.0), -1.0 / FastMath.SQRT(2.0)}, root.getColumnVector(2).toArray(), 0.0);

        } catch (NonPositiveDefiniteMatrixException e) {
            // this decomposition should be successful
            throw new AssertionError("Unexpected exception: " + e.getMessage(), e);
        }
    }

    /**
     * Test the decomposition of a singular positive semidefinite matrix.
     */
    @Test
    public void testDecompositionSingular() {

        double[][] data = new double[][]{
            {3.0, 2.0, 1.0},
            {2.0, 3.0, 1.0},
            {1.0, 1.0, 0.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        try {
            RectangularCholeskyDecomposition decomposableMatrix = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root       = decomposableMatrix.getRootMatrix();
            int rank            = decomposableMatrix.getRank();

            assertNotNull(root);
            assertEquals(3, root.getRowDimension());
            assertEquals(rank, root.getColumnDimension());
            assertArrayEquals(new double[]{-1.0 / FastMath.SQRT(3.0), 0.0, 0.0}, root.getColumnVector(0).toArray(), 0.0);
            assertArrayEquals(new double[]{-1.0 / FastMath.SQRT(2.0), 0.5 / FastMath.SQRT(2.0), 0.0}, root.getColumnVector(1).toArray(), 0.0);
            assertArrayEquals(new double[]{0.0, 0.5 / FastMath.SQRT(2.0), 0.0}, root.getColumnVector(2).toArray(), 0.0);

            // check that we have 2 independent rows
            RealMatrix lud = new LUDecomposition(MatrixUtils.createRealMatrix(new double[][]{
                    {3.0, 2.0, 1.0},
                    {2.0, 3.0, 1.0}}));
            RealMatrix lu = lud.getL();
            RealMatrix inverseLu = lu.transpose().getInverse();
            RealMatrix inverseLuTimesMatrix = MatrixUtils.mult(inverseLu, matrix);
            RealMatrix identity = MatrixUtils.createIdentityMatrix(2);
            assertArrayEquals(identity.getData(), inverseLuTimesMatrix.getData(), 0.0);

        } catch (NonPositiveDefiniteMatrixException e) {
            // this decomposition should be successful
            throw new AssertionError("Unexpected exception: " + e.getMessage(), e);
        }
    }

    /**
     * Test the decomposition of a negative definite matrix, which should fail.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testDecompositionNegativeDefinite() {

        double[][] data = new double[][]{
            {3.0, 2.0, 1.0},
            {2.0, -3.0, 1.0},
            {1.0, 1.0, 2.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            assertFalse("The decomposition of a negative definite matrix should fail", true);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    /**
     * Test the decomposition of a negative semidefinite matrix, which should fail.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testDecompositionNegativeSemidefinite() {

        double[][] data = new double[][]{
            {3.0, 2.0, 1.0},
            {2.0, -3.0, 1.0},
            {1.0, 1.0, -2.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            assertFalse("The decomposition of a negative semidefinite matrix should fail", true);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    /**
     * Test the decomposition of a non-symmetric matrix, which should fail.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testDecompositionNonSymmetric() {

        double[][] data = new double[][]{
            {3.0, 2.0},
            {2.0, 3.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            assertFalse("The decomposition of a non-symmetric matrix should fail", true);
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    /**
     * Test the decomposition of a null matrix, which should fail.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDecompositionNull() {

        RealMatrix matrix = null;

        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            assertFalse("The decomposition of a null matrix should fail", true);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test the decomposition of a matrix with a small diagonal, which should fail.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testDecompositionSmallDiagonal() {

        double[][] data = new double[][]{
            {3.0, 2.0, 1.0},
            {2.0, 3.0, 0.0},
            {1.0, 1.0, 2.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        try {
            new RectangularCholeskyDecomposition(matrix, -1.0);
            assertFalse("The decomposition of a matrix with a small diagonal should fail", true);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }
}