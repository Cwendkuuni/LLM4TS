package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.Precision;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecomposition() throws NonPositiveDefiniteMatrixException {
        // Test with a positive definite matrix
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 2.0, 2.0 }, { 2.0, 4.0, 4.0 }, { 2.0, 4.0, 5.0 } });
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.0, 0.0 }, { 1.0, 2.0, 0.0 }, { 1.0, 2.0, 2.3162277660168376 } });
        RealMatrix expectedBTranspose = new Array2DRowRealMatrix(new double[][] { { 1.0, 1.0, 1.0 }, { 0.0, 2.0, 2.0 }, { 0.0, 0.0, 2.3162277660168376 } });

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);

        RealMatrix root = decomposition.getRootMatrix();
        RealMatrix bTranspose = root.transpose();

        assertArrayEquals(expectedRoot.getData(), root.getData(), 1e-14);
        assertArrayEquals(expectedBTranspose.getData(), bTranspose.getData(), 1e-14);

        // Test with a positive semidefinite matrix
        matrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 2.0, 2.0 }, { 2.0, 0.0, 1.0 }, { 2.0, 1.0, 0.0 } });
        expectedRoot = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.0, 0.0 }, { 1.0, 1.0, 0.0 }, { 1.0, 0.5, 1.0 } });
        expectedBTranspose = new Array2DRowRealMatrix(new double[][] { { 1.0, 1.0, 1.0 }, { 1.0, 1.0, 0.5 }, { 0.0, 0.5, 1.0 } });

        decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);

        root = decomposition.getRootMatrix();
        bTranspose = root.transpose();

        assertArrayEquals(expectedRoot.getData(), root.getData(), 1e-14);
        assertArrayEquals(expectedBTranspose.getData(), bTranspose.getData(), 1e-14);

        // Test with a rank deficient matrix
        matrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 2.0, 2.0 }, { 2.0, 2.0, 2.0 }, { 2.0, 2.0, 0.0 } });
        expectedRoot = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.0, 0.0 }, { 1.0, 1.0, 0.0 }, { 1.0, 1.0, 0.0 } });
        expectedBTranspose = new Array2DRowRealMatrix(new double[][] { { 1.0, 1.0, 1.0 }, { 1.0, 1.0, 1.0 }, { 0.0, 1.0, 1.0 } });

        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }

        // Test with a non-symmetric matrix
        matrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 2.0 }, { 1.0, 3.0 } });
        expectedRoot = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.0 }, { 0.0, 1.6487212707001282 } });
        expectedBTranspose = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.6109385257573154 }, { 0.0, 1.6487212707001282 } });

        decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);

        root = decomposition.getRootMatrix();
        bTranspose = root.transpose();

        assertArrayEquals(expectedRoot.getData(), root.getData(), 1e-14);
        assertArrayEquals(expectedBTranspose.getData(), bTranspose.getData(), 1e-14);
    }

    @Test
    public void testRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 2.0, 2.0, 0.0 }, { 2.0, 4.0, 4.0, 2.0 }, { 2.0, 4.0, 5.0, 0.0 } });
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            assertEquals(3, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }

        matrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 2.0, 2.0 }, { 2.0, 4.0, 4.0 }, { 2.0, 4.0, 5.0 } });
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
        assertNull(decomposition);

        matrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 2.0 }, { 2.0, 3.0 } });
        RectangularCholeskyDecomposition decomposition2 = null;
        try {
            decomposition2 = new RectangularCholeskyDecomposition(matrix, 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
        assertNull(decomposition2);

        matrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 2.0, 2.0 }, { 2.0, 2.0, 2.0 }, { 2.0, 2.0, 2.0 } });
        decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        assertEquals(3, decomposition.getRank());

        matrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 2.0, 2.0 }, { 2.0, 2.0, 2.0 }, { 2.0, 2.0, 0.0 } });
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            assertEquals(2, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 2.0, 2.0 }, { 2.0, 4.0, 4.0 }, { 2.0, 4.0, 5.0 } });
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decomposition.getRootMatrix();
            assertNotNull(root);
            assertEquals(3, root.getRowDimension());
            assertEquals(2, root.getColumnDimension());
            RealVector firstRow = root.getColumnVector(0);
            assertEquals(1.0, firstRow.getEntry(0), 1e-14);
            assertEquals(1.0, firstRow.getEntry(1), 1e-14);
            assertEquals(1.0, firstRow.getEntry(2), 1e-14);

            RealVector secondRow = root.getColumnVector(1);
            assertEquals(0.0, secondRow.getEntry(0), 1e-14);
            assertEquals(2.3162277660168376, secondRow.getEntry(1), 1e-14);
            assertEquals(2.3162277660168376, secondRow.getEntry(2), 1e-14);

            RealVector thirdRow = root.getColumnVector(2);
            assertEquals(0.0, thirdRow.getEntry(0), 1e-14);
            assertEquals(1.3661923187692048, thirdRow.getEntry(1), 1e-14);
            assertEquals(1.3661923187692048, thirdRow.getEntry(2), 1e-14);
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }

        matrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 2.0, 2.0 }, { 2.0, 2.0, 2.0 }, { 2.0, 2.0, 0.0 } });
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decomposition.getRootMatrix();
            assertNotNull(root);
            assertEquals(3, root.getRowDimension());
            assertEquals(2, root.getColumnDimension());
            RealVector firstRow = root.getColumnVector(0);
            assertEquals(1.0, firstRow.getEntry(0), 1e-14);
            assertEquals(1.0, firstRow.getEntry(1), 1e-14);
            assertEquals(1.0, firstRow.getEntry(2), 1e-14);

            RealVector secondRow = root.getColumnVector(1);
            assertEquals(0.0, secondRow.getEntry(0), 1e-14);
            assertEquals(1.0, secondRow.getEntry(1), 1e-14);
            assertEquals(0.0, secondRow.getEntry(2), 1e-14);

            RealVector thirdRow = root.getColumnVector(2);
            assertEquals(0.0, thirdRow.getEntry(0), 1e-14);
            assertEquals(1.0, thirdRow.getEntry(1), 1e-14);
            assertEquals(0.0, thirdRow.getEntry(2), 1e-14);
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }
}