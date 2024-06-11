package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.special.FastMath;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {-1.0},
            {0.0},
            {0.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
            throw e;
        }
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0}
        });
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decomposition.getRootMatrix();
            assertArrayEquals(new double[][] {
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
            }, root.getData());
            assertTrue(decomposition.getRank() == matrix.getRowDimension());
        } catch (NonPositiveDefiniteMatrixException e) {
            // should not reach here
        }
    }

    @Test
    public void testPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0}
        });
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decomposition.getRootMatrix();
            assertArrayEquals(new double[][] {
                {FastMath.SQRT(1.0), 0.0, 0.0},
                {0.5 * FastMath.SQRT(2.0), FastMath.SQRT(5.0), 0.0},
                {0.5 * FastMath.SQRT(3.0), 0.5 * FastMath.SQRT(3.0), FastMath.SQRT(10.0)}
            }, root.getData());
            assertTrue(decomposition.getRank() == matrix.getRowDimension());
        } catch (NonPositiveDefiniteMatrixException e) {
            // should not reach here
        }
    }

    @Test
    public void testSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0},
            {2.0, 1.0}
        });
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decomposition.getRootMatrix();
            assertArrayEquals(new double[][] {
                {FastMath.SQRT(1.0), 0.0},
                {0.5 * FastMath.SQRT(2.0), FastMath.SQRT(2.0)}
            }, root.getData());
            assertTrue(decomposition.getRank() == matrix.getRowDimension());
        } catch (NonPositiveDefiniteMatrixException e) {
            // should not reach here
        }
    }

    @Test
    public void testZeroDiagonalMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {0.0, 0.0},
            {0.0, 0.0}
        });
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decomposition.getRootMatrix();
            assertNotNull(root);
            assertTrue(decomposition.getRank() == matrix.getRowDimension());
        } catch (NonPositiveDefiniteMatrixException e) {
            // should not reach here
        }
    }

    @Test
    public void testSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {0.0, 1.0},
            {0.0, 0.0}
        });
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            assertFalse(true);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testNonSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0},
            {3.0, 4.0}
        });
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            assertFalse(true);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testSmallDiagonalElements() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.0, 0.0},
            {0.0, 0.1, 0.0},
            {0.0, 0.0, 0.01}
        });
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.1);
            RealMatrix root = decomposition.getRootMatrix();
            assertArrayEquals(new double[][] {
                {FastMath.SQRT(1.0), 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, FastMath.SQRT(0.01)}
            }, root.getData());
            assertTrue(decomposition.getRank() == matrix.getRowDimension());
        } catch (NonPositiveDefiniteMatrixException e) {
            // should not reach here
        }
    }

}