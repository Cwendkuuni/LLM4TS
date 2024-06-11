package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Constants;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testExceptionThrownWhenMatrixIsNotPositiveSemidefinite() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, -1}});
        new RectangularCholeskyDecomposition(matrix, 1.0E-15);
    }

    @Test
    public void testBasicFunctionality() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, 4, 3}, {3, 3, 4}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1.0E-14);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.70710678118654755, 0.70710678118654755, 0.0}, {-0.70710678118654755, 0.70710678118654755, 0.70710678118654755}});
        RealMatrix expectedRootTranspose = new Array2DRowRealMatrix(new double[][]{{1.0, -0.70710678118654755, -0.70710678118654755}, {0.0, 0.70710678118654755, 0.70710678118654755}, {0.0, 0.0, 0.70710678118654755}});
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(expectedRoot, root);
        RealMatrix rootTranspose = root.transpose();
        assertEquals(expectedRootTranspose, rootTranspose);
        assertEquals(3, decomposition.getRank());
    }

    @Test
    public void testEdgeCaseSmallDiagonalElements() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0.1, 0.2, 0.3}, {0.2, 0.4, 0.5}, {0.3, 0.5, 0.1}});
        try {
            new RectangularCholeskyDecomposition(matrix, 1.0E-14);
            fail("Exception expected");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(0.1, e.getDiagonal(), Precision.SAFE_EPSILON);
            assertEquals(0, e.getRow());
        }
    }

    @Test
    public void testEdgeCaseNegativeDiagonalElements() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, -4, 3}, {3, 3, -4}});
        try {
            new RectangularCholeskyDecomposition(matrix, 1.0E-14);
            fail("Exception expected");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(-4.0, e.getDiagonal(), Precision.SAFE_EPSILON);
            assertEquals(1, e.getRow());
        }
    }

    @Test
    public void testExceptionThrownWhenDiagonalElementsAreBothSmallAndNegative() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-0.1, 0.2}, {0.2, -0.1}});
        try {
            new RectangularCholeskyDecomposition(matrix, 1.0E-14);
            fail("Exception expected");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testGetRankWhenAllElementsAreZero() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0, 0}, {0, 0}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 1.0E-14);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
        assertNull(decomposition);
        assertNull(decomposition.getRootMatrix());
        assertEquals(0, decomposition.getRank());
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, 4, 3}, {3, 3, 4}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1.0E-14);
        assertEquals(3, decomposition.getRank());
    }
}