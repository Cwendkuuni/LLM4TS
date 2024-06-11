package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.special.Constant;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    @Test
    public void testDecomposeIdentityMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-14);
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(2, root.getRowDimension());
        assertEquals(2, root.getColumnDimension());
        assertArrayEquals(new double[][]{{1.0, 0.0}, {0.0, 1.0}}, root.getData(), 1e-14);
        assertEquals(2, decomposition.getRank());
    }

    @Test
    public void testDecomposeDiagonalMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{Constant.PI * Constant.PI, 0.0}, {0.0, Constant.E * Constant.E}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 1e-14);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
        assertNull(decomposition);
    }

    @Test
    public void testDecomposeNegativeDiagonalMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 0.0}, {0.0, -1.0}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 1e-14);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
        assertNull(decomposition);
    }

    @Test
    public void testDecomposeSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-14);
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(2, root.getRowDimension());
        assertEquals(2, root.getColumnDimension());
        assertArrayEquals(new double[][]{{1.0, 1.0}, {1.0, 1.0}}, root.getData(), 1e-14);
        assertEquals(2, decomposition.getRank());
    }

    @Test
    public void testDecomposeSymmetricMatrixWithSmallDiagonalElements() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1e-8}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 1e-14);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
        assertNull(decomposition);
    }

    @Test
    public void testDecomposeSymmetricMatrixWithNegativeDiagonalElements() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, -1.0}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 1e-14);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
        assertNull(decomposition);
    }

    @Test
    public void testDecomposeSymmetricMatrixWithNegativeSubDiagonalElements() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, -1.1}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 1e-14);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
        assertNull(decomposition);
    }

    @Test
    public void testDecomposeSymmetricMatrixWithSmallSubDiagonalElements() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1e-8}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-14);
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(2, root.getRowDimension());
        assertEquals(2, root.getColumnDimension());
        assertArrayEquals(new double[][]{{1.0, 1.0}, {1.0, 1.0}}, root.getData(), 1e-14);
        assertEquals(2, decomposition.getRank());
    }

    @Test
    public void testDecomposeSymmetricMatrixWithZeroSubDiagonalElements() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-14);
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(2, root.getRowDimension());
        assertEquals(2, root.getColumnDimension());
        assertArrayEquals(new double[][]{{1.0, 0.0}, {0.0, 1.0}}, root.getData(), 1e-14);
        assertEquals(2, decomposition.getRank());
    }
}