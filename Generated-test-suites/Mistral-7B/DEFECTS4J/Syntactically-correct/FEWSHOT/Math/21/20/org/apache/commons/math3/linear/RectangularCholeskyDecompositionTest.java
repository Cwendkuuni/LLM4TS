package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecomposition() {
        double small = 1e-6;

        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.1, 2.2, 3.3},
            {2.2, 4.4, 5.5},
            {3.3, 5.5, 6.6}
        });

        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);

        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {FastMath.sqrt(1.1), 0.0, 0.0},
            {0.0, FastMath.sqrt(4.4), 0.0},
            {0.0, 0.0, FastMath.sqrt(6.6)}
        });

        RealMatrix root = rcd.getRootMatrix();
        assertArrayEquals(expected.getData(), root.getData(), 1e-8);
        assertEquals(3, rcd.getRank());

        // Test with a diagonal matrix
        RealMatrix diagonalMatrix = new Array2DRowRealMatrix(new double[][]{
            {1.1, 0.0, 0.0},
            {0.0, 4.4, 0.0},
            {0.0, 0.0, 6.6}
        });
        RectangularCholeskyDecomposition diagonalRcd = new RectangularCholeskyDecomposition(diagonalMatrix, small);
        RealMatrix diagonalRoot = diagonalRcd.getRootMatrix();
        assertArrayEquals(diagonalMatrix.getData(), diagonalRoot.getData(), 1e-8);
        assertEquals(3, diagonalRcd.getRank());

        // Test with a negative diagonal
        RealMatrix negativeDiagonalMatrix = new Array2DRowRealMatrix(new double[][]{
            {1.1, 2.2, 3.3},
            {2.2, -4.4, 5.5},
            {3.3, 5.5, 6.6}
        });
        try {
            new RectangularCholeskyDecomposition(negativeDiagonalMatrix, small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        // Test with a singular matrix
        RealMatrix singularMatrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 1.0, 0.0},
            {1.0, 1.0, 0.0},
            {0.0, 0.0, 0.0}
        });
        try {
            new RectangularCholeskyDecomposition(singularMatrix, small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testRank() {
        double small = 1e-6;

        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.1, 2.2, 3.3},
            {2.2, 4.4, 5.5},
            {3.3, 5.5, 6.6}
        });

        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        assertEquals(3, rcd.getRank());

        RealMatrix singularMatrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 1.0, 0.0},
            {1.0, 1.0, 0.0},
            {0.0, 0.0, 0.0}
        });
        try {
            new RectangularCholeskyDecomposition(singularMatrix, small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        RealMatrix diagonalMatrix = new Array2DRowRealMatrix(new double[][]{
            {1.1, 0.0, 0.0},
            {0.0, 4.4, 0.0},
            {0.0, 0.0, 6.6}
        });
        RectangularCholeskyDecomposition diagonalRcd = new RectangularCholeskyDecomposition(diagonalMatrix, small);
        assertEquals(3, diagonalRcd.getRank());

        RealMatrix zeroMatrix = new Array2DRowRealMatrix(new double[][]{
            {},
            {}
        });
        try {
            new RectangularCholeskyDecomposition(zeroMatrix, small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        RealMatrix symmetricMatrix = new Array2DRowRealMatrix(new double[][]{
            {1.1, 2.2},
            {2.2, 1.1}
        });
        RectangularCholeskyDecomposition symmetricRcd = new RectangularCholeskyDecomposition(symmetricMatrix, small);
        assertEquals(2, symmetricRcd.getRank());
    }

    @Test
    public void testSolveWithRootMatrix() {
        double small = 1e-6;

        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.1, 2.2, 3.3},
            {2.2, 4.4, 5.5},
            {3.3, 5.5, 6.6}
        });

        RealMatrix root = new RectangularCholeskyDecomposition(matrix, small).getRootMatrix();

        RealVector x = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector b = root.operate(x);
        RealVector y = matrix.operate(x);

        assertEquals(Precision.safeEqual(b.getEntry(0), y.getEntry(0), 1e-8), true);
        assertEquals(Precision.safeEqual(b.getEntry(1), y.getEntry(1), 1e-8), true);
        assertEquals(Precision.safeEqual(b.getEntry(2), y.getEntry(2), 1e-8), true);
    }

    @Test
    public void testSolveWithRootMatrixNegativeDiagonal() {
        double small = 1e-6;

        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.1, 2.2, 3.3},
            {2.2, -4.4, 5.5},
            {3.3, 5.5, 6.6}
        });

        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        RealMatrix negativeRoot = new Array2DRowRealMatrix(new double[][]{
            {FastMath.sqrt(1.1), 0.0, 0.0},
            {0.0, FastMath.sqrt(-4.4), 0.0},
            {0.0, 0.0, FastMath.sqrt(6.6)}
        });

        RealVector x = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector b = negativeRoot.operate(x);

        RealVector y = matrix.operate(x);

        // The solution with a negative diagonal matrix is not defined
        // and we cannot check if it is correct or not
        assertFalse(Precision.safeEqual(b.getEntry(0), y.getEntry(0), 1e-8));
        assertFalse(Precision.safeEqual(b.getEntry(1), y.getEntry(1), 1e-8));
        assertFalse(Precision.safeEqual(b.getEntry(2), y.getEntry(2), 1e-8));
    }

    @Test
    public void testSolveWithRootMatrixSingular() {
        double small = 1e-6;

        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 1.0, 0.0},
            {1.0, 1.0, 0.0},
            {0.0, 0.0, 0.0}
        });

        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        RealMatrix singularRoot = new Array2DRowRealMatrix(new double[][]{
            {FastMath.sqrt(1.0), 0.0, 0.0},
            {0.0, FastMath.sqrt(1.0), 0.0},
            {0.0, 0.0, 0.0}
        });

        RealVector x = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector b = singularRoot.operate(x);

        RealVector y = matrix.operate(x);

        // The solution with a singular matrix is not defined
        // and we cannot check if it is correct or not
        assertFalse(Precision.safeEqual(b.getEntry(0), y.getEntry(0), 1e-8));
        assertFalse(Precision.safeEqual(b.getEntry(1), y.getEntry(1), 1e-8));
        assertFalse(Precision.safeEqual(b.getEntry(2), y.getEntry(2), 1e-8));
    }
}