package org.apache.commons.math3.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for the RectangularCholeskyDecomposition class
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test the constructor with valid inputs
     * @throws NonPositiveDefiniteMatrixException
     */
    @Test
    public void testValidConstructor() throws NonPositiveDefiniteMatrixException {
        double[][] matrixData = {
                {3.0, 2.0, 1.0},
                {2.0, 2.0, 2.0},
                {1.0, 2.0, 3.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                {FastMath.sqrt(2.0), FastMath.sqrt(2.0)/2.0, 0.0},
                {FastMath.sqrt(3.0)/2.0, FastMath.sqrt(3.0)/2.0, 1.0}}, false);
        RealMatrix actualRoot = rcd.getRootMatrix();
        assertTrue(MathUtils.isArrayEquals(expectedRoot.getData(), actualRoot.getData(), 0.0001));
        assertEquals(expectedRoot.getRowDimension(), actualRoot.getRowDimension());
        assertEquals(expectedRoot.getColumnDimension(), actualRoot.getColumnDimension());
        assertEquals(expectedRoot.getRank(), actualRoot.getRank());
    }

    /**
     * Test the constructor with a non-symmetric matrix
     * @throws NonPositiveDefiniteMatrixException
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonSymmetricMatrix() throws NonPositiveDefiniteMatrixException {
        double[][] matrixData = {
                {3.0, 2.0, 1.0},
                {2.0, 2.0, 2.0},
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;
        new RectangularCholeskyDecomposition(matrix, small);
    }

    /**
     * Test the constructor with a non-positive definite matrix
     * @throws NonPositiveDefiniteMatrixException
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        double[][] matrixData = {
                {3.0, 2.0, 1.0},
                {2.0, -1.0, 2.0},
                {1.0, 2.0, 3.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;
        new RectangularCholeskyDecomposition(matrix, small);
    }

    /**
     * Test the constructor with a singular matrix
     * @throws NonPositiveDefiniteMatrixException
     */
    @Test
    public void testSingularMatrix() throws NonPositiveDefiniteMatrixException {
        double[][] matrixData = {
                {3.0, 2.0, 1.0},
                {2.0, 2.0, 2.0},
                {1.0, 2.0, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                {FastMath.sqrt(2.0)/2.0, FastMath.sqrt(2.0), 0.0},
                {FastMath.sqrt(3.0)/2.0, 0.0, 1.0}}, false);
        RealMatrix actualRoot = rcd.getRootMatrix();
        assertTrue(MathUtils.isArrayEquals(expectedRoot.getData(), actualRoot.getData(), 0.0001));
        assertEquals(expectedRoot.getRowDimension(), actualRoot.getRowDimension());
        assertEquals(expectedRoot.getColumnDimension(), actualRoot.getColumnDimension());
        assertEquals(expectedRoot.getRank(), actualRoot.getRank());
        assertEquals(2, actualRoot.getColumnDimension());
    }

    /**
     * Test the getRank method
     */
    @Test
    public void testGetRank() {
        double[][] matrixData = {
                {3.0, 2.0, 1.0},
                {2.0, 2.0, 2.0},
                {1.0, 2.0, 3.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        int expectedRank = 3;
        int actualRank = rcd.getRank();
        assertEquals(expectedRank, actualRank);
    }

    /**
     * Test the getRank method with a singular matrix
     */
    @Test
    public void testGetRankSingularMatrix() {
        double[][] matrixData = {
                {3.0, 2.0, 1.0},
                {2.0, 2.0, 2.0},
                {1.0, 2.0, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        int expectedRank = 2;
        int actualRank = rcd.getRank();
        assertEquals(expectedRank, actualRank);
    }

    /**
     * Test the getRootMatrix method with valid inputs
     */
    @Test
    public void testGetRootMatrix() {
        double[][] matrixData = {
                {3.0, 2.0, 1.0},
                {2.0, 2.0, 2.0},
                {1.0, 2.0, 3.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                {FastMath.sqrt(2.0), FastMath.sqrt(2.0)/2.0, 0.0},
                {FastMath.sqrt(3.0)/2.0, FastMath.sqrt(3.0)/2.0, 1.0}}, false);
        RealMatrix actualRoot = rcd.getRootMatrix();
        assertTrue(MathUtils.isArrayEquals(expectedRoot.getData(), actualRoot.getData(), 0.0001));
        assertEquals(expectedRoot.getRowDimension(), actualRoot.getRowDimension());
        assertEquals(expectedRoot.getColumnDimension(), actualRoot.getColumnDimension());
        assertEquals(expectedRoot.getRank(), actualRoot.getRank());
    }

    /**
     * Test the getRootMatrix method with small diagonal elements
     */
    @Test
    public void testGetRootMatrixSmallDiagonalElements() {
        double[][] matrixData = {
                {3.0, 2.0, 1.0},
                {2.0, 2.0, 2.0},
                {1.0, 2.0, 0.5}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.5;
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                {FastMath.sqrt(2.0), FastMath.sqrt(2.0)/2.0, 0.0},
                {FastMath.sqrt(3.0)/3.0, FastMath.sqrt(3.0)/3.0, 1.0}}, false);
        RealMatrix actualRoot = rcd.getRootMatrix();
        assertTrue(MathUtils.isArrayEquals(expectedRoot.getData(), actualRoot.getData(), 0.001));
        assertEquals(expectedRoot.getRowDimension(), actualRoot.getRowDimension());
        assertEquals(expectedRoot.getColumnDimension(), actualRoot.getColumnDimension());
        assertEquals(expectedRoot.getRank(), actualRoot.getRank());
    }

    /**
     * Test the getRootMatrix method with a diagonal matrix
     */
    @Test
    public void testGetRootMatrixDiagonalMatrix() {
        double[][] matrixData = {
                {3.0, 0.0, 0.0},
                {0.0, 2.0, 0.0},
                {0.0, 0.0, 3.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}}, false);
        RealMatrix actualRoot = rcd.getRootMatrix();
        assertTrue(MathUtils.isArrayEquals(expectedRoot.getData(), actualRoot.getData(), 0.0001));
        assertEquals(expectedRoot.getRowDimension(), actualRoot.getRowDimension());
        assertEquals(expectedRoot.getColumnDimension(), actualRoot.getColumnDimension());
        assertEquals(expectedRoot.getRank(), actualRoot.getRank());
    }
}