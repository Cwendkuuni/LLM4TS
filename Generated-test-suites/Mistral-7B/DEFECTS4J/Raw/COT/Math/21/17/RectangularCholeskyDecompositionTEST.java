package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecomposition.UException;
import org.apache.commons.math3.linear.SingularValueDecomposition.VException;
import org.apache.commons.math3.linear.SingularValueDecomposition.ValueException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Pair;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for {@link RectangularCholeskyDecomposition}.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test method for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with positive definite matrix.
     */
    @org.junit.Test
    public void testPositiveDefiniteMatrix() {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 6.0},
                {3.0, 6.0, 10.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();

        assertEquals(3, rank);
        assertEquals(3, root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());

        RealMatrix expectedMatrix = new SingularValueDecomposition(new Array2DRowRealMatrix(matrixData))
                .getU();
        for (int i = 0; i < rank; ++i) {
            for (int j = 0; j < rank; ++j) {
                assertEquals(Precision.SAFE_Double.tolerance() * expectedMatrix.getEntry(i, j),
                        root.getEntry(i, j), 0.0);
            }
        }
    }

    /**
     * Test method for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with positive semidefinite matrix.
     */
    @org.junit.Test
    public void testPositiveSemidefiniteMatrix() {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 0.0},
                {3.0, 0.0, 10.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;

        assertFalse("Matrix is not symmetric.", matrix.isSymmetric());

        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        assertNull(decomposition);
    }

    /**
     * Test method for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with negative diagonal elements.
     */
    @org.junit.Test
    public void testNegativeDiagonalElements() {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, -5.0, 6.0},
                {3.0, 6.0, -10.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;

        assertFalse("Matrix is not symmetric.", matrix.isSymmetric());

        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        assertNull(decomposition);
    }

    /**
     * Test method for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with very small diagonal elements.
     */
    @org.junit.Test
    public void testVerySmallDiagonalElements() {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 1e-10},
                {3.0, 1e-10, 10.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 1e-16;

        Assertions.assertThrows(NonPositiveDefiniteMatrixException.class, () -> {
            new RectangularCholeskyDecomposition(matrix, small);
        });
    }

    /**
     * Test method for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with non-symmetric matrix.
     */
    @org.junit.Test
    public void testNonSymmetricMatrix() {
        double[][] matrixData = {
                {1.0, 2.0},
                {3.0, 4.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;

        assertFalse("Matrix is not symmetric.", matrix.isSymmetric());

        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        assertNull(decomposition);
    }

    /**
     * Test method for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with identity matrix.
     */
    @org.junit.Test
    public void testIdentityMatrix() {
        double[][] matrixData = {
                {1.0, 0.0},
                {0.0, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();

        assertEquals(2, rank);
        assertEquals(2, root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());

        RealVector expectedVector = new ArrayRealVector(new double[] {1.0, 1.0});
        for (int i = 0; i < rank; ++i) {
            RealVector vector = root.columnVector(i);
            assertEquals(expectedVector, vector);
        }
    }

    /**
     * Test method for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with singular matrix.
     */
    @org.junit.Test
    public void testSingularMatrix() {
        double[][] matrixData = {
                {1.0, 2.0},
                {2.0, 0.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;

        assertFalse("Matrix is not symmetric.", matrix.isSymmetric());

        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        assertNull(decomposition);
    }

    /**
     * Test method for {@link RectangularCholeskyDecomposition#getRank()}.
     */
    @org.junit.Test
    public void testGetRank() {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 6.0},
                {3.0, 6.0, 10.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        int rank = decomposition.getRank();

        assertEquals(3, rank);
    }

    /**
     * Test method for {@link RectangularCholeskyDecomposition#getRootMatrix()}.
     */
    @org.junit.Test
    public void testGetRootMatrix() {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 6.0},
                {3.0, 6.0, 10.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomposition.getRootMatrix();

        assertEquals(3, root.getRowDimension());
        assertEquals(3, root.getColumnDimension());
    }
}