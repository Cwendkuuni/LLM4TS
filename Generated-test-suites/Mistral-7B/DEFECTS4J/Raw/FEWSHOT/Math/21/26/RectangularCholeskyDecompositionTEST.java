package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        double small = 1e-8;

        // Test with a positive definite matrix
        double[][] matrixData = {
            {1.0d, 2.0d, 3.0d, 4.0d},
            {2.0d, 5.0d, 6.0d, 8.0d},
            {3.0d, 6.0d, 10.0d, 12.0d},
            {4.0d, 8.0d, 12.0d, 15.0d}
        };
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomp.getRootMatrix();

        assertNotNull(root);
        assertTrue(root.getRowDimension() == root.getColumnDimension());
        assertTrue(root.getRowDimension() == matrix.getRowDimension());

        double[][] expectedResult = {
            {1.0d, 0.0d, 0.0d, 0.0d},
            {0.0d, 2.5d, 1.0d, 0.0d},
            {0.0d, 1.0d, 3.0d, 1.0d},
            {0.0d, 0.0d, 1.0d, 3.5d}
        };
        assertArrayEquals(expectedResult, root.getData(), 0.0);
        assertEquals(4, decomp.getRank());

        // Test with a matrix that is not positive semidefinite
        double[][] notPositiveData = {
            {1.0d, 2.0d, 3.0d, 4.0d},
            {2.0d, -1.0d, 6.0d, 8.0d},
            {3.0d, 6.0d, -5.0d, 12.0d},
            {4.0d, 8.0d, 12.0d, -15.0d}
        };
        RealMatrix notPositiveMatrix = RealMatrixUtils.createRealMatrix(notPositiveData);
        try {
            new RectangularCholeskyDecomposition(notPositiveMatrix, small);
            assertFalse("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testGetRank() {
        double small = 1e-8;

        double[][] matrixData = {
            {1.0d, 2.0d, 3.0d, 4.0d},
            {2.0d, 5.0d, 6.0d, 8.0d},
            {3.0d, 6.0d, 10.0d, 12.0d},
            {4.0d, 8.0d, 12.0d, 15.0d}
        };
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);

        int rank = decomp.getRank();
        assertEquals(4, rank);

        double[][] notPositiveData = {
            {1.0d, 2.0d, 3.0d, 4.0d},
            {2.0d, -1.0d, 6.0d, 8.0d},
            {3.0d, 6.0d, -5.0d, 12.0d},
            {4.0d, 8.0d, 12.0d, -15.0d}
        };
        RealMatrix notPositiveMatrix = RealMatrixUtils.createRealMatrix(notPositiveData);
        try {
            new RectangularCholeskyDecomposition(notPositiveMatrix, small);
            assertNull("Expected null rank");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testGetRootMatrix() {
        double small = 1e-8;

        double[][] matrixData = {
            {1.0d, 2.0d, 3.0d, 4.0d},
            {2.0d, 5.0d, 6.0d, 8.0d},
            {3.0d, 6.0d, 10.0d, 12.0d},
            {4.0d, 8.0d, 12.0d, 15.0d}
        };
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);

        RealMatrix root = decomp.getRootMatrix();
        assertNotNull(root);
        assertEquals(4, root.getRowDimension());
        assertEquals(4, root.getColumnDimension());

        double[][] expectedResult = {
            {1.0d, 0.0d, 0.0d, 0.0d},
            {0.0d, 2.5d, 1.0d, 0.0d},
            {0.0d, 1.0d, 3.0d, 1.0d},
            {0.0d, 0.0d, 1.0d, 3.5d}
        };
        assertArrayEquals(expectedResult, root.getData(), 0.0);

        double[][] notPositiveData = {
            {1.0d, 2.0d, 3.0d, 4.0d},
            {2.0d, -1.0d, 6.0d, 8.0d},
            {3.0d, 6.0d, -5.0d, 12.0d},
            {4.0d, 8.0d, 12.0d, -15.0d}
        };
        RealMatrix notPositiveMatrix = RealMatrixUtils.createRealMatrix(notPositiveData);
        try {
            new RectangularCholeskyDecomposition(notPositiveMatrix, small);
            assertNull("Expected null root");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }
}