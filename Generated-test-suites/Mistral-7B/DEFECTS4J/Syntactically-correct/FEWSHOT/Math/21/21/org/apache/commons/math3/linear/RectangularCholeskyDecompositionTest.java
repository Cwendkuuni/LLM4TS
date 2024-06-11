package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertMatricesAlmostEqual;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test cases for {@link RectangularCholeskyDecomposition}.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test Cholesky decomposition of a 2x2 matrix.
     */
    @Test
    public void test2x2() {
        double[][] matrix = { { 1.0, 0.5 }, { 0.5, 0.6 } };
        RealMatrix reference = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.5 }, { 0.0, 0.80635389 } });

        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(RealMatrixUtils.createRealMatrix(matrix), 0.0);
            RealMatrix root = decomp.getRootMatrix();
            assertMatricesAlmostEqual(reference, root, 1e-10);
            assertEquals(2, decomp.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test Cholesky decomposition of a 2x2 matrix with small diagonal elements.
     */
    @Test
    public void test2x2SmallDiagonal() {
        double[][] matrix = { { 0.1, 0.5 }, { 0.5, 0.6 } };
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(RealMatrixUtils.createRealMatrix(matrix), 0.0);
            assertNull(decomp.getRootMatrix());
            assertEquals(0, decomp.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    /**
     * Test Cholesky decomposition of a 3x3 matrix.
     */
    @Test
    public void test3x3() {
        double[][] matrix = { { 1.0, 0.5, 0.5 }, { 0.5, 1.0, 0.25 }, { 0.5, 0.25, 1.0 } };
        RealMatrix reference = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.5, 0.5 }, { 0.0, 0.81649658, 0.40824829 }, { 0.0, 0.0, 0.86602540 } });

        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(RealMatrixUtils.createRealMatrix(matrix), 0.0);
            RealMatrix root = decomp.getRootMatrix();
            assertMatricesAlmostEqual(reference, root, 1e-10);
            assertEquals(3, decomp.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test Cholesky decomposition of a 3x3 matrix with small diagonal elements.
     */
    @Test
    public void test3x3SmallDiagonal() {
        double[][] matrix = { { 0.1, 0.5, 0.5 }, { 0.5, 0.1, 0.25 }, { 0.5, 0.25, 0.1 } };
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(RealMatrixUtils.createRealMatrix(matrix), 0.0);
            assertNull(decomp.getRootMatrix());
            assertEquals(0, decomp.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    /**
     * Test Cholesky decomposition of a 3x3 matrix with negative diagonal elements.
     */
    @Test
    public void test3x3NegativeDiagonal() {
        double[][] matrix = { { -1.0, 0.5, 0.5 }, { 0.5, -1.0, 0.25 }, { 0.5, 0.25, -1.0 } };
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(RealMatrixUtils.createRealMatrix(matrix), 0.0);
            assertNull(decomp.getRootMatrix());
            assertEquals(0, decomp.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    /**
     * Test Cholesky decomposition of a 4x4 matrix.
     */
    @Test
    public void test4x4() {
        double[][] matrix = { { 1.0, 0.5, 0.5, 0.5 }, { 0.5, 1.0, 0.25, 0.25 }, { 0.5, 0.25, 1.0, 0.25 }, { 0.5, 0.25, 0.25, 1.0 } };
        RealMatrix reference = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.5, 0.5, 0.5 }, { 0.0, 0.81649658, 0.40824829, 0.20411361 }, { 0.0, 0.0, 0.76754911, 0.45027167 }, { 0.0, 0.0, 0.0, 0.80308561 } });

        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(RealMatrixUtils.createRealMatrix(matrix), 0.0);
            RealMatrix root = decomp.getRootMatrix();
            assertMatricesAlmostEqual(reference, root, 1e-10);
            assertEquals(4, decomp.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test Cholesky decomposition of a 4x4 matrix with small diagonal elements.
     */
    @Test
    public void test4x4SmallDiagonal() {
        double[][] matrix = { { 0.1, 0.5, 0.5, 0.5 }, { 0.5, 0.1, 0.25, 0.25 }, { 0.5, 0.25, 0.1, 0.1 }, { 0.5, 0.25, 0.1, 0.1 } };
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(RealMatrixUtils.createRealMatrix(matrix), 0.0);
            assertNull(decomp.getRootMatrix());
            assertEquals(0, decomp.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    /**
     * Test Cholesky decomposition of a 4x4 matrix with negative diagonal elements.
     */
    @Test
    public void test4x4NegativeDiagonal() {
        double[][] matrix = { { -1.0, 0.5, 0.5, 0.5 }, { 0.5, -1.0, 0.25, 0.25 }, { 0.5, 0.25, -1.0, 0.25 }, { 0.5, 0.25, 0.25, -1.0 } };
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(RealMatrixUtils.createRealMatrix(matrix), 0.0);
            assertNull(decomp.getRootMatrix());
            assertEquals(0, decomp.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    /**
     * Test Cholesky decomposition of a 5x5 matrix.
     */
    @Test
    public void test5x5() {
        double[][] matrix = { { 1.0, 0.5, 0.5, 0.5, 0.5 }, { 0.5, 1.0, 0.25, 0.25, 0.25 }, { 0.5, 0.25, 1.0, 0.25, 0.25 }, { 0.5, 0.25, 0.25, 1.0, 0.25 }, { 0.5, 0.25, 0.25, 0.25, 1.0 } };
        RealMatrix reference = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.5, 0.5, 0.5, 0.5 }, { 0.0, 0.81649658, 0.40824829, 0.20411361, 0.10205664 }, { 0.0, 0.0, 0.76754911, 0.45027167, 0.22516667 }, { 0.0, 0.0, 0.0, 0.72887784, 0.41615385 }, { 0.0, 0.0, 0.0, 0.0, 0.80234726 } });

        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(RealMatrixUtils.createRealMatrix(matrix), 0.0);
            RealMatrix root = decomp.getRootMatrix();
            assertMatricesAlmostEqual(reference, root, 1e-10);
            assertEquals(5, decomp.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test Cholesky decomposition of a 5x5 matrix with small diagonal elements.
     */
    @Test
    public void test5x5SmallDiagonal() {
        double[][] matrix = { { 0.1, 0.5, 0.5, 0.5, 0.5 }, { 0.5, 0.1, 0.25, 0.25, 0.25 }, { 0.5, 0.25, 0.1, 0.1, 0.1 }, { 0.5, 0.25, 0.1, 0.1, 0.1 }, { 0.5, 0.25, 0.1, 0.1, 0.1 } };
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(RealMatrixUtils.createRealMatrix(matrix), 0.0);
            assertNull(decomp.getRootMatrix());
            assertEquals(0, decomp.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    /**
     * Test Cholesky decomposition of a 5x5 matrix with negative diagonal elements.
     */
    @Test
    public void test5x5NegativeDiagonal() {
        double[][] matrix = { { -1.0, 0.5, 0.5, 0.5, 0.5 }, { 0.5, -1.0, 0.25, 0.25, 0.25 }, { 0.5, 0.25, -1.0, 0.25, 0.25 }, { 0.5, 0.25, 0.25, -1.0, 0.25 }, { 0.5, 0.25, 0.25, 0.25, -1.0 } };
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(RealMatrixUtils.createRealMatrix(matrix), 0.0);
            assertNull(decomp.getRootMatrix());
            assertEquals(0, decomp.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    /**
     * Test Cholesky decomposition of a non positive definite matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefinite() {
        double[][] matrix = { { -1.0, 0.5 }, { 0.5, -1.0 } };
        new RectangularCholeskyDecomposition(RealMatrixUtils.createRealMatrix(matrix), 0.0);
    }

    /**
     * Test Cholesky decomposition of a null matrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullMatrix() {
        new RectangularCholeskyDecomposition(null, 0.0);
    }

}