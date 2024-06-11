package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.util.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link RectangularCholeskyDecomposition} class.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test decomposition of positive definite matrix.
     */
    @Test
    public void testDecompositionOfPositiveDefiniteMatrix() {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 8.0},
                {3.0, 8.0, 13.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 1e-10;
        try {
            RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposer.getRootMatrix();
            int rank = decomposer.getRank();
            Assert.assertEquals(3, rank);
            Assert.assertArrayEquals(new double[][]{
                    {1.0, 0.0, 0.0},
                    {0.5, 1.5, 0.0},
                    {0.1666666666666666, 0.8, 1.0}
            }, root.getData(), 1e-6);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test decomposition of positive semidefinite matrix.
     */
    @Test
    public void testDecompositionOfPositiveSemidefiniteMatrix() {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 8.0},
                {3.0, 8.0, 13.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 1e-10;
        try {
            RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposer.getRootMatrix();
            int rank = decomposer.getRank();
            Assert.assertEquals(3, rank);
            Assert.assertArrayEquals(new double[][]{
                    {1.0, 0.0, 0.0},
                    {0.5, 2.0, 0.0},
                    {0.1666666666666666, 0.8, 1.0}
            }, root.getData(), 1e-6);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.assertFalse(Precision.isClose(e.getValue(), 0.0, 1e-6));
            Assert.assertEquals(1, e.getIndex());
            Assert.assertTrue(Precision.isClose(e.getSmall(), 2.0, 1e-6));
        }
    }

    /**
     * Test decomposition with negative diagonal element.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testDecompositionWithNegativeDiagonalElement() {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, -1.0, 8.0},
                {3.0, 8.0, 13.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 1e-10;
        try {
            new RectangularCholeskyDecomposition(matrix, small);
            Assert.fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.assertEquals(1, e.getIndex());
            Assert.assertTrue(Precision.isClose(e.getValue(), -1.0, 1e-6));
        }
    }

    /**
     * Test decomposition with all negative diagonal elements.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testDecompositionWithAllNegativeDiagonalElements() {
        double[][] matrixData = {
                {-1.0, 2.0, 3.0},
                {2.0, -1.0, 8.0},
                {3.0, 8.0, -13.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 1e-10;
        try {
            new RectangularCholeskyDecomposition(matrix, small);
            Assert.fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    /**
     * Test exception message.
     */
    @Test
    public void testExceptionMessage() {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 8.0},
                {3.0, 8.0, 13.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 1e-15;
        try {
            new RectangularCholeskyDecomposition(matrix, small);
            Assert.fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.assertEquals("The diagonal element at index 2 is less than the given tolerance of " + small +
                    ", which is not acceptable for a symmetric positive semidefinite matrix.", e.getMessage());
        }
    }
}