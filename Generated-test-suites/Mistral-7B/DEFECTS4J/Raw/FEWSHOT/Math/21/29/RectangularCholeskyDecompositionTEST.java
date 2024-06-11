package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;

public class RectangularCholeskyDecompositionTest {

    /**
     * Test of Cholesky decomposition with small matrix
     */
    @Test
    public void testSmallMatrix() {
        double small = 1e-10;
        double[][] matrixData = {
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, 1.5}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomp.getRootMatrix();
            assertEquals(3, root.getRowDimension());
            assertEquals(3, root.getColumnDimension());
            assertEquals(2.0, Precision.round(root.getEntry(0, 0), 4));
            assertEquals(1.0, Precision.round(root.getEntry(1, 0), 4));
            assertEquals(-1.0, Precision.round(root.getEntry(2, 0), 4));
            assertEquals(1.0, Precision.round(root.getEntry(0, 1), 4));
            assertEquals(1.0, Precision.round(root.getEntry(1, 1), 4));
            assertEquals(1.0, Precision.round(root.getEntry(1, 2), 4));
            assertEquals(0.0, Precision.round(root.getEntry(2, 1), 4));
            assertEquals(-0.5, Precision.round(root.getEntry(2, 2), 4));
            int rank = decomp.getRank();
            assertEquals(3, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception should not be thrown");
        }
    }

    /**
     * Test of Cholesky decomposition with a singular matrix
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testSingularMatrix() {
        double small = 1e-10;
        double[][] matrixData = {
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, 0.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
            decomp.getRootMatrix();
            fail("Exception should be thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    /**
     * Test of Cholesky decomposition with a negative semidefinite matrix
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNegativeSemidefiniteMatrix() {
        double small = 1e-10;
        double[][] matrixData = {
                {2.0, 1.0, 1.0},
                {1.0, -2.0, 1.0},
                {1.0, 1.0, -1.5}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
            decomp.getRootMatrix();
            fail("Exception should be thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

}