package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for the {@link RectangularCholeskyDecomposition} class.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test decomposition of a 2x2 positive semidefinite matrix with no small diagonal elements.
     */
    @Test
    public void testDecomposition2x2() {
        double[][] matrixData = {
            {1.0, 2.0},
            {2.0, 1.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 1e-16;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomposition.getRootMatrix();

        assertArrayEquals(new double[][]{{0.44720943, 0.89442758}, {0.89442758, 1.33986866}},
                root.getData(), 0.00001);

        assertEquals(2, decomposition.getRank());
    }

    /**
     * Test decomposition of a 2x2 positive semidefinite matrix with small diagonal elements.
     */
    @Test
    public void testDecomposition2x2WithSmallDiagonal() {
        double[][] matrixData = {
            {1.0e-5, 2.0},
            {2.0, 1.0e-5}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 1e-16;

        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(1.0e-5, e.getValue(), 0.0);
            assertEquals(0, e.getRow());
            assertEquals(0, e.getSmall());
        }
    }

    /**
     * Test decomposition of a 3x3 positive semidefinite matrix with no small diagonal elements.
     */
    @Test
    public void testDecomposition3x3() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 1.0, 2.0},
            {3.0, 2.0, 5.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 1e-16;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomposition.getRootMatrix();

        assertArrayEquals(new double[][]{{0.35355339, 0.52915225, 0.78128176},
                {0.52915225, 0.76224757, 0.89442758},
                {0.78128176, 0.89442758, 1.33986866}},
                root.getData(), 0.00001);

        assertEquals(3, decomposition.getRank());
    }

    /**
     * Test decomposition of a 3x3 positive semidefinite matrix with small diagonal elements.
     */
    @Test
    public void testDecomposition3x3WithSmallDiagonal() {
        double[][] matrixData = {
            {1.0e-5, 2.0, 3.0},
            {2.0, 1.0e-5, 2.0},
            {3.0, 2.0, 1.0e-5}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 1e-16;

        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(1.0e-5, e.getValue(), 0.0);
            assertEquals(0, e.getRow());
            assertEquals(0, e.getSmall());
        }
    }

    /**
     * Test decomposition of a 3x3 positive semidefinite matrix with repeated small diagonal elements.
     */
    @Test
    public void testDecomposition3x3WithRepeatedSmallDiagonal() {
        double[][] matrixData = {
            {1.0e-5, 2.0, 3.0},
            {2.0, 1.0e-5, 2.0},
            {3.0, 2.0, 1.0e-5}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 1e-15;

        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        assertEquals(2, decomposition.getRank());
    }

    /**
     * Test decomposition of a non-positive definite matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testDecompositionNonPositiveDefinite() {
        double[][] matrixData = {
            {1.0, -1.0},
            {-1.0, 1.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 1e-16;

        new RectangularCholeskyDecomposition(matrix, small);
    }
}