package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.MatrixUtils;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        double small = 1e-8;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {2.01, 1.01, 0.01},
            {1.01, 1.02, 1.01},
            {0.01, 1.01, 0.5},
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();

            assertEquals("Unexpected rank", 3, rank);
            assertNotNull("Root matrix is null", root);
            assertEquals("Unexpected dimension of root matrix", 3, root.getRowDimension());
            assertEquals("Unexpected number of columns of root matrix", rank, root.getColumnDimension());
            assertEquals("Unexpected decomposition result", new Array2DRowRealMatrix(new double[][]{
                    {FastMath.sqrt(2.01), FastMath.sqrt(2.01) * 0.422618633791876},
                    {0, FastMath.sqrt(1.02) * 0.712582562683752},
                    {-FastMath.sqrt(2.01) * 0.422618633791876, 0, FastMath.sqrt(0.5)},
            }), root);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected exception
        }

        RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0},
        });
        try {
            RectangularCholeskyDecomposition decomposition2 = new RectangularCholeskyDecomposition(matrix2, small);
            RealMatrix root2 = decomposition2.getRootMatrix();
            int rank2 = decomposition2.getRank();

            assertEquals("Unexpected rank", 3, rank2);
            assertNotNull("Root matrix is null", root2);
            assertEquals("Unexpected dimension of root matrix", 3, root2.getRowDimension());
            assertEquals("Unexpected number of columns of root matrix", rank2, root2.getColumnDimension());
            assertEquals("Unexpected decomposition result", new Array2DRowRealMatrix(new double[][]{
                    {FastMath.sqrt(1.0), 0, 0},
                    {0, FastMath.sqrt(1.0), 0},
                    {0, 0, FastMath.sqrt(1.0)},
            }), root2);
        } catch (NonPositiveDefiniteMatrixException e) {
            // unexpected exception
            assertNull("Unexpected exception", e);
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {-1.0, 1.0},
            {1.0, -1.0},
        });

        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            assertNull("Unexpected result", true);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected exception
        }

        RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{
            {1.0, 1.0, 1.0},
            {1.0, -1.0, 0.0},
            {1.0, 0.0, -1.0},
        });

        try {
            new RectangularCholeskyDecomposition(matrix2, 0.0);
            assertNull("Unexpected result", true);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected exception
        }
    }
}