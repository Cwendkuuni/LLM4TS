package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstructor() {
        double[] weights = new double[]{1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);

        RealMatrix expected = RealMatrixUtils.createIdentityMatrix(3);
        for (int i = 0; i < 3; i++) {
            expected.setEntry(i, i, weights[i]);
        }

        RealMatrix actual = weight.getWeight();
        assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightSquareMatrixConstructorWithNonSquareMatrix() {
        double[][] weightMatrixData = new double[][]{
            {1.0, 2.0},
            {3.0}
        };
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightMatrixData);

        new Weight(weightMatrix);
    }

    @Test
    public void testWeightSquareMatrixConstructor() {
        double[][] weightMatrixData = new double[][]{
            {1.0, 0.0},
            {0.0, 2.0}
        };
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightMatrixData);

        Weight weight = new Weight(weightMatrix);
        RealMatrix actual = weight.getWeight();

        assertArrayEquals(weightMatrix.getData(), actual.getData(), 0.0);
    }

    @Test
    public void testWeightGetNull() {
        Weight weight = new Weight(null);
        assertNull(weight.getWeight());
    }

    @Test
    public void testWeightGetCopy() {
        double[] weights = new double[]{1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);

        RealMatrix actual = weight.getWeight();
        RealMatrix expected = RealMatrixUtils.createIdentityMatrix(3);
        for (int i = 0; i < 3; i++) {
            expected.setEntry(i, i, weights[i]);
        }

        assertArrayEquals(expected.getData(), actual.getData(), 0.0);

        // Modifying the expected matrix should not change the actual one
        expected.setEntry(1, 1, 4.0);
        assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }
}