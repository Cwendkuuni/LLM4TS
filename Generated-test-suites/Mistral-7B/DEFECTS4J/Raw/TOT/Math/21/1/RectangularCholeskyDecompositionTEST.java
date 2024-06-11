package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for {@link RectangularCholeskyDecomposition}.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test the decomposition of a positive definite matrix.
     */
    @Test
    public void testDecompositionPositiveDefinite() {
        double[][] data = { { 1.0, 2.0, 2.0 }, { 2.0, 4.0, 4.0 }, { 2.0, 4.0, 8.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data);

        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decomp.getRootMatrix();
            RealMatrix expected = RealMatrixUtils.createColumnRealMatrix(new double[][] {
                    { FastMath.sqrt(1.0), 0.0, 0.0 },
                    { FastMath.sqrt(2.0), FastMath.sqrt(2.0), 0.0 },
                    { FastMath.sqrt(2.0), 0.0, FastMath.sqrt(2.0) }
            });
            assertEquals(expected, root);
            assertEquals(3, decomp.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the decomposition of a positive semidefinite matrix with rank 2.
     */
    @Test
    public void testDecompositionPositiveSemidefiniteRank2() {
        double[][] data = { { 1.0, 2.0, 2.0 }, { 2.0, 4.0, 4.0 }, { 2.0, 4.0, 8.0 }, { 2.0, 4.0, 4.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data);

        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decomp.getRootMatrix();
            RealMatrix expected = RealMatrixUtils.createColumnRealMatrix(new double[][] {
                    { FastMath.sqrt(1.0), 0.0, 0.0 },
                    { FastMath.sqrt(2.0), FastMath.sqrt(2.0), 0.0 },
                    { FastMath.sqrt(2.0), 0.0, FastMath.sqrt(2.0) },
                    { 0.0, 0.0, 0.0 }
            });
            assertEquals(expected, root);
            assertEquals(3, decomp.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the decomposition of a positive semidefinite matrix with rank 1.
     */
    @Test
    public void testDecompositionPositiveSemidefiniteRank1() {
        double[][] data = { { 1.0, 1.0, 1.0 }, { 1.0, 4.0, 4.0 }, { 1.0, 4.0, 8.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data);

        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decomp.getRootMatrix();
            RealMatrix expected = RealMatrixUtils.createColumnRealMatrix(new double[][] {
                    { FastMath.sqrt(1.0), 0.0, 0.0 },
                    { 0.0, 0.0, 0.0 },
                    { 0.0, 0.0, FastMath.sqrt(1.0) }
            });
            assertEquals(expected, root);
            assertEquals(2, decomp.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the decomposition of a non positive definite matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testDecompositionNonPositiveDefinite() {
        double[][] data = { { 1.0, 2.0, 2.0 }, { 2.0, 4.0, 3.0 }, { 2.0, 4.0, 8.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data);

        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException but no exception was thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
    }

    /**
     * Test the decomposition with a matrix that is not symmetric.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDecompositionNotSymmetric() {
        double[][] data = { { 1.0, 2.0 }, { 2.0, 3.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data);

        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected IllegalArgumentException but no exception was thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}