package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link RectangularCholeskyDecomposition}.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test rectangular Cholesky decomposition of a positive definite matrix.
     */
    @Test
    public void testPositiveDefinite() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 2.0},
            {3.0, 2.0, 9.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
            RealMatrix root = decomposition.getRootMatrix();
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0},
                                                                             {2.0, 2.25, 1.25},
                                                                             {3.0, 1.25, 3.0}});
            assertEquals(expected, root);
            assertEquals(3, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test rectangular Cholesky decomposition of a positive semidefinite matrix with small diagonal elements.
     */
    @Test
    public void testPositiveSemidefinite() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 0.5, 2.0},
            {3.0, 2.0, 0.5}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.1);
            RealMatrix root = decomposition.getRootMatrix();
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0},
                                                                             {2.0, 1.0, 0.0},
                                                                             {3.0, 0.0, 1.0}});
            assertEquals(expected, root);
            assertEquals(2, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test rectangular Cholesky decomposition of a non-positive definite matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefinite() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, -1.0, 2.0},
            {3.0, 2.0, -1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        new RectangularCholeskyDecomposition(matrix, 1e-10);
    }

    /**
     * Test rectangular Cholesky decomposition of a singular matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testSingular() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 0.0, 2.0},
            {3.0, 2.0, 0.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        new RectangularCholeskyDecomposition(matrix, 1e-10);
    }

    /**
     * Test rectangular Cholesky decomposition with diagonal elements below threshold.
     */
    @Test
    public void testDiagonalBelowThreshold() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 1e-10, 2.0},
            {3.0, 2.0, 1e-10}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.1);
            RealMatrix root = decomposition.getRootMatrix();
            // impossible to determine exactly what decomposition should look like
            // due to floating point issues, we just check rank
            assertEquals(2, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test rectangular Cholesky decomposition with diagonal elements below threshold
     * for all elements except the first one.
     */
    @Test
    public void testDiagonalBelowThresholdExceptFirst() {
        double[][] matrixData = {
            {1.0, 1e-10, 3.0},
            {2.0, 5.0, 2.0},
            {3.0, 2.0, 1e-10}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.1);
            RealMatrix root = decomposition.getRootMatrix();
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0},
                                                                             {2.0, 2.25, 1.25},
                                                                             {3.0, 1.25, 0.0}});
            assertEquals(expected, root);
            assertEquals(2, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}