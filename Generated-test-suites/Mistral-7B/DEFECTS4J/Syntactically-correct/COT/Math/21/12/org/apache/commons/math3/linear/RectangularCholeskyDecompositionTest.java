package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import org.junit.Test;

/**
 * JUnit test class for {@link RectangularCholeskyDecomposition} class.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test the constructor with a valid positive semidefinite matrix.
     */
    @Test
    public void testValidConstruction() {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 6.0},
                {3.0, 6.0, 9.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        assertNotNull(decomposition);
        assertEquals(3, decomposition.getRank());
        assertNotNull(decomposition.getRootMatrix());
    }

    /**
     * Test the constructor with a positive semidefinite matrix with zero diagonal.
     */
    @Test
    public void testZeroDiagonalConstruction() {
        double[][] matrixData = {
                {1.0, 2.0, 0.0},
                {2.0, 4.0, 0.0},
                {0.0, 0.0, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;

        assertThrows(NonPositiveDefiniteMatrixException.class, () -> {
            new RectangularCholeskyDecomposition(matrix, small);
        });
    }

    /**
     * Test the constructor with a non-positive semidefinite matrix.
     */
    @Test
    public void testNonPositiveConstruction() {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, -1.0, 6.0},
                {3.0, 6.0, 9.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;

        assertThrows(NonPositiveDefiniteMatrixException.class, () -> {
            new RectangularCholeskyDecomposition(matrix, small);
        });
    }

    /**
     * Test the constructor with a matrix with a negative diagonal element.
     */
    @Test
    public void testNegativeDiagonalConstruction() {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 6.0},
                {3.0, 6.0, -1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;

        assertThrows(NonPositiveDefiniteMatrixException.class, () -> {
            new RectangularCholeskyDecomposition(matrix, small);
        });
    }

    /**
     * Test the constructor with a matrix with a large enough threshold value.
     */
    @Test
    public void testLargeThresholdConstruction() {
        double[][] matrixData = {
                {1.0, 2.0, 0.0},
                {2.0, 4.0, 0.0},
                {0.0, 0.0, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 1.0; // large threshold value

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        assertNotNull(decomposition);
        assertEquals(3, decomposition.getRank());
        assertNotNull(decomposition.getRootMatrix());
    }

}