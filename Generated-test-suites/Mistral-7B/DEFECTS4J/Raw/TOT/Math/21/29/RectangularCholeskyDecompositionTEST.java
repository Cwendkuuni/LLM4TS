package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link RectangularCholeskyDecomposition} class.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test the decomposition of a positive definite matrix.
     */
    @Test
    public void testPositiveDefiniteMatrix() {
        double[][] matrix = {
            {2.0, 1.0},
            {1.0, 2.0}
        };
        RealMatrix input = new Array2DRowRealMatrix(matrix);
        try {
            RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(input, 0.0);
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)},
                                                                                {-1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)}});
            RealMatrix expectedResult = expectedRoot.transpose().multiply(expectedRoot);
            RealMatrix result = decomposer.getRootMatrix().transpose().multiply(decomposer.getRootMatrix());
            assertEquals(expectedResult, result);
            assertEquals(2, decomposer.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the decomposition of a positive semidefinite matrix with small diagonal elements.
     */
    @Test
    public void testPositiveSemidefiniteMatrix() {
        double[][] matrix = {
            {2.0, 1.0, 0.1},
            {1.0, 2.0, 0.1},
            {0.1, 0.1, 0.2}
        };
        RealMatrix input = new Array2DRowRealMatrix(matrix);
        try {
            RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(input, 0.1);
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0), 0.0},
                                                                                {-1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0), 0.0},
                                                                                {0.0, 0.0, Math.sqrt(0.2)}});
            RealMatrix expectedResult = expectedRoot.transpose().multiply(expectedRoot);
            RealMatrix result = decomposer.getRootMatrix().transpose().multiply(decomposer.getRootMatrix());
            assertEquals(expectedResult, result);
            assertEquals(3, decomposer.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the decomposition with a non-positive definite matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() {
        double[][] matrix = {
            {-1.0, 1.0},
            {1.0, -1.0}
        };
        RealMatrix input = new Array2DRowRealMatrix(matrix);
        new RectangularCholeskyDecomposition(input, 0.0);
    }

    /**
     * Test the decomposition with a singular matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testSingularMatrix() {
        double[][] matrix = {
            {0.0, 1.0},
            {1.0, 0.0}
        };
        RealMatrix input = new Array2DRowRealMatrix(matrix);
        new RectangularCholeskyDecomposition(input, 0.0);
    }
}