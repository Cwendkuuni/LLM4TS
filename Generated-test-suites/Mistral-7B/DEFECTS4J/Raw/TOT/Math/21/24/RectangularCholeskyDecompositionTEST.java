package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link RectangularCholeskyDecomposition} class.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Tests {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * method with positive definite matrix.
     */
    @Test
    public void testPositiveDefiniteMatrix() {
        double[][] matrixData = {
            {1.0, 2.0, 2.0},
            {2.0, 4.0, 4.0},
            {2.0, 4.0, 6.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        RealMatrix root;
        try {
            root = new RectangularCholeskyDecomposition(matrix, 0.0).getRootMatrix();
        } catch (NonPositiveDefiniteMatrixException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{
            {1.0, 1.0, 1.0},
            {0.0, 2.0, 1.0},
            {0.0, 0.0, 1.84775995449865},
        }, false);

        assertEquals(expectedRoot, root);
    }

    /**
     * Tests {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * method with positive semidefinite matrix.
     */
    @Test
    public void testPositiveSemidefiniteMatrix() {
        double[][] matrixData = {
            {1.0, 2.0, 2.0},
            {2.0, 4.0, 4.0},
            {2.0, 4.0, 5.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        RealMatrix root;
        try {
            root = new RectangularCholeskyDecomposition(matrix, 0.0).getRootMatrix();
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected, matrix is only positive semidefinite
        }
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{
            {1.0, 1.0, 1.0},
            {0.0, 2.0, 1.0},
            {0.0, 0.0, Math.sqrt(5.0)}
        }, false);

        assertNotNull(root);
        assertEquals(expectedRoot, root);
    }

    /**
     * Tests {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * method with non-positive definite matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() {
        double[][] matrixData = {
            {1.0, 2.0, 2.0},
            {2.0, 3.0, 4.0},
            {2.0, 4.0, 2.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    /**
     * Tests {@link RectangularCholeskyDecomposition#getRank()} method.
     */
    @Test
    public void testGetRank() {
        double[][] matrixData = {
            {1.0, 2.0, 2.0},
            {2.0, 4.0, 4.0},
            {2.0, 4.0, 5.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        RealMatrix root;
        try {
            root = new RectangularCholeskyDecomposition(matrix, 0.0).getRootMatrix();
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected, matrix is only positive semidefinite
        }
        assertEquals(3, root.getRowDimension());
        assertEquals(3, root.getColumnDimension());
        assertEquals(3, new RectangularCholeskyDecomposition(matrix, 0.0).getRank());
    }

    /**
     * Tests {@link RectangularCholeskyDecomposition#getRootMatrix()} method.
     */
    @Test
    public void testGetRootMatrix() {
        double[][] matrixData = {
            {1.0, 2.0, 2.0},
            {2.0, 4.0, 4.0},
            {2.0, 4.0, 5.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        RealMatrix root;
        try {
            root = new RectangularCholeskyDecomposition(matrix, 0.0).getRootMatrix();
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected, matrix is only positive semidefinite
        }
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{
            {1.0, 1.0, 1.0},
            {0.0, 2.0, 1.0},
            {0.0, 0.0, Math.sqrt(5.0)}
        }, false);
        assertEquals(expectedRoot, root);
    }

    /**
     * Tests {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * method with empty matrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyMatrix() {
        new RectangularCholeskyDecomposition(MatrixUtils.createRealMatrix(0, 0), 0.0);
    }

    /**
     * Tests {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * method with matrix with different number of rows and columns.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDifferentNumberOfRowsAndColumns() {
        double[][] matrixData = {
            {1.0, 2.0},
            {3.0, 4.0, 5.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

}