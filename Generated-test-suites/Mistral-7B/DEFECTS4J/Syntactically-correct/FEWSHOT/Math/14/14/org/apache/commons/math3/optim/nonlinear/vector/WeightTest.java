package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.junit.Test;

public class WeightTest {

    /**
     * Test constructor with array.
     */
    @Test
    public void testWeightConstructorWithArray() {
        double[] weights = new double[] {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedWeight = RealMatrixUtils.diagonalMatrix(weights);

        RealMatrix actualWeight = weight.getWeight();
        assertArrayEquals(expectedWeight.getData(), actualWeight.getData(), 0.0);
    }

    /**
     * Test constructor with RealMatrix.
     */
    @Test
    public void testWeightConstructorWithRealMatrix() {
        double[][] weightMatrixData = new double[][] {
                {1.0, 0.0},
                {0.0, 2.0}
        };
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightMatrixData);
        Weight weight = new Weight(weightMatrix);

        RealMatrix expectedWeight = weightMatrix;
        RealMatrix actualWeight = weight.getWeight();

        assertNotNull(expectedWeight);
        assertNotNull(actualWeight);
        assertTrue(expectedWeight.equals(actualWeight));
    }

    /**
     * Test constructor with non-square matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithNonSquareMatrix() {
        double[][] weightMatrixData = new double[][] {
                {1.0, 0.0},
                {0.0}
        };
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightMatrixData);
        new Weight(weightMatrix);
    }
}