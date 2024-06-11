package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.MathException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.util.MatrixUtils;
import org.apache.commons.math3.special.FastMath;
import org.junit.Test;

/**
 * Test cases for {@link RectangularCholeskyDecomposition}.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test case for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with a positive definite matrix.
     */
    @Test
    public void testPositiveDefiniteMatrix() {
        // Matrix from the class javadoc
        RealMatrix input = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 2.0},
            {2.0, 3.0, 1.0},
            {2.0, 1.0, 3.0}
        });

        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0, 1.0, 1.0},
            {1.0, 1.4142135625, 0.70710678118},
            {1.0, 0.70710678118, 1.4142135625}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(input, 0.0);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            assertArrayEquals(expected.getData(), root.getData(), 0.0);
            assertEquals(rank, 3);
        } catch (NonPositiveDefiniteMatrixException e) {
            throw new RuntimeException("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

    /**
     * Test case for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with a positive semidefinite matrix.
     */
    @Test
    public void testPositiveSemidefiniteMatrix() {
        // Matrix from the class javadoc
        RealMatrix input = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 2.0},
            {2.0, 1.0, 0.0},
            {2.0, 0.0, 1.0}
        });

        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 0.0},
            {1.0, 0.0, 1.0}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(input, 0.0);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            assertArrayEquals(expected.getData(), root.getData(), 0.0);
            assertEquals(rank, 3);
        } catch (NonPositiveDefiniteMatrixException e) {
            throw new RuntimeException("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

    /**
     * Test case for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with a non positive semidefinite matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() {
        // Matrix from the class javadoc
        RealMatrix input = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 2.0},
            {2.0, -1.0, 0.0},
            {2.0, 0.0, -1.0}
        });

        try {
            new RectangularCholeskyDecomposition(input, 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            // pass
        }
        throw new AssertionError("Unexpected exception not thrown");
    }

    /**
     * Test case for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with a null matrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullMatrix() {
        new RectangularCholeskyDecomposition(null, 0.0);
    }

    /**
     * Test case for {@link RectangularCholeskyDecomposition#getRank()}.
     */
    @Test
    public void testGetRank() {
        RealMatrix input = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 2.0},
            {2.0, 1.0, 0.0},
            {2.0, 0.0, 1.0}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(input, 0.0);
            int rank = decomposition.getRank();
            assertEquals(rank, 3);
        } catch (NonPositiveDefiniteMatrixException e) {
            throw new RuntimeException("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

    /**
     * Test case for {@link RectangularCholeskyDecomposition#getRootMatrix()}.
     */
    @Test
    public void testGetRootMatrix() {
        RealMatrix input = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 2.0},
            {2.0, 1.0, 0.0},
            {2.0, 0.0, 1.0}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(input, 0.0);
            RealMatrix root = decomposition.getRootMatrix();
            assertNotNull(root);
            assertEquals(root.getRowDimension(), 3);
            assertEquals(root.getColumnDimension(), 3);
        } catch (NonPositiveDefiniteMatrixException e) {
            throw new RuntimeException("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

    /**
     * Test case for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with a matrix with a small diagonal element.
     */
    @Test
    public void testSmallDiagonalElement() {
        RealMatrix input = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 2.0},
            {2.0, 1.0, 0.001},
            {2.0, 0.001, 1.0}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(input, 0.001);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            assertArrayEquals(new double[][]{
                    {1.0, 1.0, 1.0},
                    {1.0, 1.0, 0.0},
                    {1.0, 0.0, 1.0}
            }, root.getData(), 0.001);
            assertEquals(rank, 3);
        } catch (NonPositiveDefiniteMatrixException e) {
            throw new RuntimeException("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

    /**
     * Test case for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with a matrix with negative diagonal elements.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNegativeDiagonalElement() {
        RealMatrix input = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 2.0},
            {2.0, -1.0, 0.0},
            {2.0, 0.0, -1.0}
        });

        try {
            new RectangularCholeskyDecomposition(input, 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            // pass
        }
        throw new AssertionError("Unexpected exception not thrown");
    }

    /**
     * Test case for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with a matrix with small negative diagonal elements.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testSmallNegativeDiagonalElement() {
        RealMatrix input = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 2.0},
            {2.0, -1.0e-6, 0.0},
            {2.0, 0.0, -1.0e-6}
        });

        try {
            new RectangularCholeskyDecomposition(input, 1e-6);
        } catch (NonPositiveDefiniteMatrixException e) {
            // pass
        }
        throw new AssertionError("Unexpected exception not thrown");
    }

    /**
     * Test case for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with a matrix with null diagonal elements.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullDiagonalElement() {
        RealMatrix input = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 2.0},
            {2.0, null, 0.0},
            {2.0, 0.0, 1.0}
        });

        try {
            new RectangularCholeskyDecomposition(input, 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            throw new RuntimeException("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
        throw new AssertionError("Unexpected exception not thrown");
    }

    /**
     * Test case for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with a matrix with zero diagonal elements.
     */
    @Test
    public void testZeroDiagonalElement() {
        RealMatrix input = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 2.0},
            {2.0, 0.0, 0.0},
            {2.0, 0.0, 1.0}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(input, 0.0);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            assertArrayEquals(new double[][]{
                    {1.0, 1.0, 1.0},
                    {1.0, 0.0, 0.0},
                    {1.0, 0.0, 1.0}
            }, root.getData(), 0.0);
            assertEquals(rank, 3);
        } catch (NonPositiveDefiniteMatrixException e) {
            throw new RuntimeException("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

    /**
     * Test case for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with a matrix with diagonal elements equal to small.
     */
    @Test
    public void testDiagonalElementsEqualToSmall() {
        RealMatrix input = new Array2DRowRealMatrix(new double[][]{
            {0.0, 2.0, 2.0},
            {2.0, 0.0, 0.0},
            {2.0, 0.0, 0.0}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(input, 0.0);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            assertArrayEquals(new double[][]{
                    {1.0, 1.0, 1.0},
                    {1.0, 1.0, 0.0},
                    {1.0, 0.0, 1.0}
            }, root.getData(), 0.0);
            assertEquals(rank, 3);
        } catch (NonPositiveDefiniteMatrixException e) {
            throw new RuntimeException("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }
}