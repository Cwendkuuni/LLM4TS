package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.special.Constant;

import java.util.Random;

/**
 * Test cases for the {@link RectangularCholeskyDecomposition} class.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test constructor with positive definite matrix.
     */
    @org.junit.Test
    public void testPositiveDefiniteMatrix() {
        double[][] matrixData = {
            {2.0, 1.0, 1.0},
            {1.0, 2.0, 1.0},
            {1.0, 1.0, 1.5}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        double small = 0.0;
        RectangularCholeskyDecomposition decomposedMatrix = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomposedMatrix.getRootMatrix();

        assertEquals(matrix, root.transpose().multiply(root));
        assertNotNull(decomposedMatrix.getRootMatrix());
        assertEquals(3, decomposedMatrix.getRank());
    }

    /**
     * Test constructor with positive semidefinite matrix.
     */
    @org.junit.Test
    public void testPositiveSemidefiniteMatrix() {
        double[][] matrixData = {
            {2.0, 1.0, 1.0},
            {1.0, 2.0, 0.5},
            {1.0, 0.5, 1.5}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        double small = 0.0;
        RectangularCholeskyDecomposition decomposedMatrix = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomposedMatrix.getRootMatrix();

        assertEquals(matrix, root.transpose().multiply(root), 0.001); // tolerate small errors
        assertNotNull(decomposedMatrix.getRootMatrix());
        assertEquals(3, decomposedMatrix.getRank());
    }

    /**
     * Test constructor with non-symmetric matrix.
     */
    @org.junit.Test
    public void testNonSymmetricMatrix() {
        double[][] matrixData = {
            {2.0, 1.0},
            {1.0, 3.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        double small = 0.0;
        assertThrows(NonPositiveDefiniteMatrixException.class,
                () -> new RectangularCholeskyDecomposition(matrix, small));
    }

    /**
     * Test constructor with negative definite matrix.
     */
    @org.junit.Test
    public void testNegativeDefiniteMatrix() {
        double[][] matrixData = {
            {-2.0, 1.0},
            {1.0, -2.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        double small = 0.0;
        assertThrows(NonPositiveDefiniteMatrixException.class,
                () -> new RectangularCholeskyDecomposition(matrix, small));
    }

    /**
     * Test constructor with very small diagonal elements.
     */
    @org.junit.Test
    public void testVerySmallDiagonalElements() {
        double[][] matrixData = {
            {1.0e-10, 1.0, 1.0},
            {1.0, 1.0e-10, 1.0},
            {1.0, 1.0, 1.0e-10}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        double small = 1.0e-12;
        assertThrows(NonPositiveDefiniteMatrixException.class,
                () -> new RectangularCholeskyDecomposition(matrix, small));
    }

    /**
     * Test constructor with small diagonal elements.
     */
    @org.junit.Test
    public void testSmallDiagonalElements() {
        double[][] matrixData = {
            {1.0, 1.0, 1.0},
            {1.0, 1.0e-6, 1.0},
            {1.0, 1.0, 1.0e-6}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        double small = 1.0e-8;
        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail("expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    /**
     * Test getRank method.
     */
    @org.junit.Test
    public void testGetRank() {
        double[][] matrixData = {
            {2.0, 1.0, 1.0},
            {1.0, 2.0, 0.5},
            {1.0, 0.5, 1.5}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        double small = 0.0;
        RectangularCholeskyDecomposition decomposedMatrix = new RectangularCholeskyDecomposition(matrix, small);

        assertEquals(3, decomposedMatrix.getRank());
    }

    /**
     * Test getRootMatrix method.
     */
    @org.junit.Test
    public void testGetRootMatrix() {
        double[][] matrixData = {
            {2.0, 1.0, 1.0},
            {1.0, 2.0, 0.5},
            {1.0, 0.5, 1.5}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        double small = 0.0;
        RectangularCholeskyDecomposition decomposedMatrix = new RectangularCholeskyDecomposition(matrix, small);

        RealMatrix root = decomposedMatrix.getRootMatrix();
        RealVector vector1 = root.getColumnVector(0);
        RealVector vector2 = root.getColumnVector(1);
        RealVector vector3 = root.getColumnVector(2);

        assertEquals(3, root.getRowDimension());
        assertEquals(3, root.getColumnDimension());
        assertTrue(vector1.getEntry(0) > 0.0);
        assertTrue(vector2.getEntry(1) > 0.0);
        assertTrue(vector3.getEntry(2) > 0.0);
    }

    /**
     * Test constructor with identity matrix.
     */
    @org.junit.Test
    public void testIdentityMatrix() {
        double[][] matrixData = {
            {1.0, 0.0},
            {0.0, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        double small = 0.0;
        RectangularCholeskyDecomposition decomposedMatrix = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomposedMatrix.getRootMatrix();

        assertEquals(matrix, root.transpose().multiply(root));
        assertNotNull(decomposedMatrix.getRootMatrix());
        assertEquals(2, decomposedMatrix.getRank());
    }

    /**
     * Test constructor with orthogonal matrix.
     */
    @org.junit.Test
    public void testOrthogonalMatrix() {
        double[][] matrixData = {
            {Constant.SQRT_2_2, -Constant.SQRT_2_2},
            {Constant.SQRT_2_2, Constant.SQRT_2_2}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        double small = 0.0;
        RectangularCholeskyDecomposition decomposedMatrix = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomposedMatrix.getRootMatrix();

        assertEquals(matrix, root.transpose().multiply(root));
        assertNotNull(decomposedMatrix.getRootMatrix());
        assertEquals(2, decomposedMatrix.getRank());
    }

    /**
     * Test constructor with random matrix.
     */
    @org.junit.Test
    public void testRandomMatrix() {
        int rows = 3;
        int columns = 3;
        double[][] matrixData = new double[rows][columns];
        Random random = new Random();

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                matrixData[i][j] = random.nextDouble();
            }
        }

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;

        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail("expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }
}