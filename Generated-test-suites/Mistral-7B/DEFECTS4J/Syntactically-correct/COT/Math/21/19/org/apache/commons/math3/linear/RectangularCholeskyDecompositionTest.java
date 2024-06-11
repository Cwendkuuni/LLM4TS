package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor_validInput_success() {
        double small = 0.001;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.5, 1.5, 0.5},
                {1.5, 2.5, 1.5},
                {0.5, 1.5, 1.5}
        });

        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomp.getRootMatrix();
            int rank = decomp.getRank();

            Assert.assertEquals(3, rank);
            Assert.assertArrayEquals(new double[][]{
                    {FastMath.sqrt(2.5), 0.0, -0.5 * FastMath.sqrt(2.5 / 3.0)},
                    {0.0, FastMath.sqrt(2.5), 0.5 * FastMath.sqrt(2.5 / 3.0)},
                    {-0.5 * FastMath.sqrt(2.5 / 3.0), 0.5 * FastMath.sqrt(2.5 / 3.0), FastMath.sqrt(1.5)}
            }, root.getData(), 0.001);

        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_nonPositiveDefiniteMatrix_exceptionThrown() {
        double small = 0.001;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.5, 1.5, 0.5},
                {1.5, 2.5, 1.5},
                {0.5, 1.5, 0.5}
        });

        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.assertEquals(1.5, Precision.abs(e.getDiagonalValue()), 0.0);
            Assert.assertEquals(2, e.getRowIndex());
            Assert.assertEquals(small, e.getThreshold());
            throw e;
        }
    }

}