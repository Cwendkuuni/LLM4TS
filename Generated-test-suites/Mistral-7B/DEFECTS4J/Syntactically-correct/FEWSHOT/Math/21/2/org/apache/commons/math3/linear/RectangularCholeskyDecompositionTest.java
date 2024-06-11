package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecomposition() {
        double small = 1e-5;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {4.0, 1.0, 1.0},
                {1.0, 4.0, 1.0},
                {1.0, 1.0, 2.0}
        });
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            assertEquals(3, root.getRowDimension());
            assertEquals(3, root.getColumnDimension());
            RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
                    {1.0 / FastMath.sqrt(4.0), 0.0, -0.5 * FastMath.sqrt(2.0) / 2.0},
                    {0.0, 1.0 / FastMath.sqrt(4.0), -0.5 * FastMath.sqrt(2.0) / 2.0},
                    {-0.5, -0.5, 1.0}
            });
            assertEquals(expected, root);
            assertEquals(3, decomposition.getRank());

            RealVector v1 = matrix.operate(new Array2DRowRealMatrix(new double[][] {
                    {1.0, 1.0, 1.0}
            }));
            RealVector v2 = root.operate(v1);
            double epsilon = Precision.SAFE_EPSILON;
            assertEquals(1.0 + epsilon, v2.getEntry(0), epsilon);
            assertEquals(1.0 + epsilon, v2.getEntry(1), epsilon);
            assertEquals(Math.sqrt(2.0) + epsilon, v2.getEntry(2), epsilon);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrixException() {
        double small = 1e-5;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {1.0, 1.0, 1.0},
                {1.0, 0.0, 1.0},
                {1.0, 1.0, -1.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail("NonPositiveDefiniteMatrixException should be thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }
}