package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        double small = 1e-5;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {2.0, 1.0},
            {1.0, 1.0},
            {1.0, 0.5}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            assertEquals(3, rank);
            assertEquals(2, root.getRowDimension());
            assertEquals(2, root.getColumnDimension());

            double[][] expectedRoot = {
                    {1.4142135625, 1.4142135625},
                    {-0.7071067812, 0.7071067812}
            };

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    assertEquals(expectedRoot[i][j], root.getEntry(i, j), Precision.SAFE_EPSILON);
                }
            }

            RealVector vector = new ArrayRealVector(new double[]{0.1, 0.2, 0.3});
            RealMatrix product = root.transpose().multiply(root).multiply(vector);
            RealVector expectedProduct = new ArrayRealVector(new double[]{0.2000000000001, 0.2000000000001});
            assertArrayEquals(expectedProduct.toArray(), product.getColumnVector(0).toArray(), 0.0);

            matrix = new Array2DRowRealMatrix(new double[][]{
                    {1.0, 1.0},
                    {1.0, 1.0}
            });

            try {
                new RectangularCholeskyDecomposition(matrix, small);
                fail("Expected NonPositiveDefiniteMatrixException");
            } catch (NonPositiveDefiniteMatrixException e) {
                // Expected
            }

            matrix = new Array2DRowRealMatrix(new double[][]{
                    {1.0, 1.0},
                    {1.0, -1.0}
            });

            try {
                new RectangularCholeskyDecomposition(matrix, small);
                fail("Expected NonPositiveDefiniteMatrixException");
            } catch (NonPositiveDefiniteMatrixException e) {
                // Expected
            }

            matrix = new Array2DRowRealMatrix(new double[][]{
                    {1.0, 1.0},
                    {1.0, 0.0}
            });

            try {
                new RectangularCholeskyDecomposition(matrix, small);
                RealMatrix root2 = decomposition.getRootMatrix();
                assertEquals(2, root2.getRowDimension());
                assertEquals(1, root2.getColumnDimension());
                assertNotNull(root2.getEntry(1, 0));
                fail("Expected NonPositiveDefiniteMatrixException");
            } catch (NonPositiveDefiniteMatrixException e) {
                // Expected
            }
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException");
        }
    }
}