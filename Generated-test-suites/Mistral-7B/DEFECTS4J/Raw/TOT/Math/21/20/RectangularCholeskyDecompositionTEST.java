package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link RectangularCholeskyDecomposition} class.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test the decomposition of a positive definite matrix.
     */
    @Test
    public void testPositiveDefinite() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2, 1, 1}, {1, 2, 1}, {1, 1, 2}});
        RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1.4142135625}});
        assertEquals("Unexpected root matrix", expected, decompositon.getRootMatrix());
        assertEquals("Unexpected rank", 3, decompositon.getRank());
    }

    /**
     * Test the decomposition of a positive semidefinite matrix with small diagonal elements.
     */
    @Test
    public void testPositiveSemidefinite() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2, 1, 1}, {1, 2, 0.1}, {1, 0.1, 2}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected exception: NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals("Unexpected diagonal element index", 2, e.getRow());
            assertEquals("Unexpected diagonal element value", 0.1, Precision.abs(e.getValue()), 1e-12);
        }
    }

    /**
     * Test the decomposition of a non-symmetric matrix.
     */
    @Test
    public void testNonSymmetric() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, 1, 1}, {3, 1, 1}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected exception: NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected, matrix is not symmetric positive semidefinite
        }
    }

}