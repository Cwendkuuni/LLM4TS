package org.apache.commons.math3.linear;

import static org.apache.commons.math3.linear.MatrixUtils.createRealMatrix;
import static org.apache.commons.math3.linear.RealMatrixoperations.add;
import static org.apache.commons.math3.linear.RealMatrixoperations.getEntry;
import static org.apache.commons.math3.linear.RealMatrixoperations.multiply;
import static org.apache.commons.math3.linear.RealMatrixoperations.subtract;
import static org.apache.commons.math3.linear.RealMatrixoperations.transpose;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixoperations;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Test;

/**
 * Test class for {@link RectangularCholeskyDecomposition}.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Check that the constructor works correctly.
     */
    @Test
    public void testConstructor() {
        double[][] a = {
            { 1, 2, 3 },
            { 2, 1, 1 },
            { 3, 1, 1 }
        };
        RealMatrix matrix = createRealMatrix(a);
        double small = 0.001;
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
        assertNotNull(decomp.getRootMatrix());
        assertEquals(3, decomp.getRank());
    }

    /**
     * Test that an exception is thrown if the input matrix is not positive semidefinite.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() {
        double[][] a = {
            { 1, 2, 3 },
            { 2, -1, 1 },
            { 3, 1, 1 }
        };
        RealMatrix matrix = createRealMatrix(a);
        double small = 0.001;
        new RectangularCholeskyDecomposition(matrix, small);
    }

    /**
     * Test that an exception is thrown if the input matrix is not symmetric.
     */
    @Test(expected = AssertionError.class)
    public void testNotSymmetricMatrix() {
        double[][] a = {
            { 1, 2 },
            { 3, 4 }
        };
        RealMatrix matrix = createRealMatrix(a);
        double small = 0.001;
        new RectangularCholeskyDecomposition(matrix, small);
    }

    /**
     * Test that the root matrix is correctly calculated for a simple 2x2 case.
     */
    @Test
    public void testRootMatrix2x2() {
        double[][] a = {
            { 2, 1 },
            { 1, 2 }
        };
        RealMatrix matrix = createRealMatrix(a);
        double small = 0.01;
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomp.getRootMatrix();
        assertEquals(2, root.getRowDimension());
        assertEquals(2, root.getColumnDimension());
        assertEquals(1.4142135625, getEntry(root, 0, 0), 0.001);
        assertEquals(0.7071067811, getEntry(root, 0, 1), 0.001);
        assertEquals(0.7071067811, getEntry(root, 1, 0), 0.001);
        assertEquals(1.4142135625, getEntry(root, 1, 1), 0.001);
    }

    /**
     * Test that the root matrix is correctly calculated for a
     * 3x3 case with some small diagonal elements.
     */
    @Test
    public void testRootMatrix3x3() {
        double[][] a = {
            { 2, 1, 1 },
            { 1, 2, 1 },
            { 1, 1, 1e-5 }
        };
        RealMatrix matrix = createRealMatrix(a);
        double small = 1e-3;
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomp.getRootMatrix();
        assertEquals(3, root.getRowDimension());
        assertEquals(3, root.getColumnDimension());
        assertArrayEquals(new double[][] {
            { 2.0, 1.0, 1.0 },
            { 1.0, 2.0, 1.0 },
            { 0.0, 0.0, 0.7071067811 }
        }, root.getData(), 0.001);
    }

    /**
     * Test that the rank is correctly calculated.
     */
    @Test
    public void testRank() {
        double[][] a = {
            { 2, 1, 1 },
            { 1, 2, 1 },
            { 1, 1, 1e-5 }
        };
        RealMatrix matrix = createRealMatrix(a);
        double small = 1e-3;
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
        assertEquals(3, decomp.getRank());
    }

    /**
     * Test that the rank is correctly calculated when all diagonal elements are 0.
     */
    @Test
    public void testRankAllZeroDiagonal() {
        double[][] a = {
            { 0, 0 },
            { 0, 0 }
        };
        RealMatrix matrix = createRealMatrix(a);
        double small = 0.0;
        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail();
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    /**
     * Test that the root matrix is correctly multiplied by its transpose.
     */
    @Test
    public void testRootMatrixMultipliedByTranspose() {
        double[][] a = {
            { 2, 1, 1 },
            { 1, 2, 1 },
            { 1, 1, 1e-5 }
        };
        RealMatrix matrix = createRealMatrix(a);
        double small = 1e-3;
        RealMatrix root;
        try {
            root = new RectangularCholeskyDecomposition(matrix, small).getRootMatrix();
        } catch (NonPositiveDefiniteMatrixException e) {
            fail();
        }
        RealMatrix expected = multiply(root, transpose(root));
        RealMatrix result = multiply(root, transpose(root));
        assertArrayEquals(expected.getData(), result.getData(), 1e-8);
    }

    /**
     * Test that the root matrix is correctly multiplied by a vector.
     */
    @Test
    public void testRootMatrixMultipliedByVector() {
        double[][] a = {
            { 2, 1, 1 },
            { 1, 2, 1 },
            { 1, 1, 1e-5 }
        };
        RealMatrix matrix = createRealMatrix(a);
        double small = 1e-3;
        RealMatrix root;
        try {
            root = new RectangularCholeskyDecomposition(matrix, small).getRootMatrix();
        } catch (NonPositiveDefiniteMatrixException e) {
            fail();
        }
        RealMatrix expected = multiply(createRealMatrix(new double[][] { { 1, 0, 0 } }), root);
        RealMatrix result = multiply(createRealMatrix(new double[][] { { 1, 0, 0 } }), root);
        assertArrayEquals(expected.getData(), result.getData(), 1e-8);
    }

    /**
     * Test that the root matrix is correctly multiplied by a matrix.
     */
    @Test
    public void testRootMatrixMultipliedByMatrix() {
        double[][] a = {
            { 2, 1, 1 },
            { 1, 2, 1 },
            { 1, 1, 1e-5 }
        };
        double[][] b = {
            { 1, 2, 3 },
            { 2, 1, 2 },
            { 3, 2, 1 }
        };
        RealMatrix matrixA = createRealMatrix(a);
        RealMatrix matrixB = createRealMatrix(b);
        double small = 1e-3;
        RealMatrix root;
        try {
            root = new RectangularCholeskyDecomposition(matrixA, small).getRootMatrix();
        } catch (NonPositiveDefiniteMatrixException e) {
            fail();
        }
        RealMatrix result = multiply(matrixB, root);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
            { 3.0, 2.0, 2.0 },
            { 6.0, 6.0, 4.0 },
            { 9.0, 8.0, 3.0 }
        });
        assertArrayEquals(expected.getData(), result.getData(), 1e-8);
    }

}