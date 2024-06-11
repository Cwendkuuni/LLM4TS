package org.apache.commons.math3.optimization;

import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class WeightTest {
    @Test
    public void testWeightWithArray() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3, new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        });
        RealMatrix actualWeight = weight.getWeight();

        assertNotNull(actualWeight);
        assertEquals(expectedWeight, actualWeight);
    }

    @Test
    public void testWeightWithMatrix() {
        RealMatrix weights = new Array2DRowRealMatrix(3, 3, new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        });
        Weight weight = new Weight(weights);
        RealMatrix expectedWeight = weights.copy();
        RealMatrix actualWeight = weight.getWeight();

        assertNotNull(actualWeight);
        assertEquals(expectedWeight, actualWeight);
    }

    @Test(expected = NullPointerException.class)
    public void testWeightWithNullArray() {
        double[] weights = null;
        Weight weight = new Weight(weights);
    }

    @Test(expected = NullPointerException.class)
    public void testWeightWithNullMatrix() {
        RealMatrix weights = null;
        Weight weight = new Weight(weights);
    }

    @Test(expected = org.apache.commons.math3.linear.NonSquareMatrixException.class)
    public void testWeightWithNonSquareMatrix() {
        RealMatrix weights = new Array2DRowRealMatrix(2, 3);
        Weight weight = new Weight(weights);
    }

    @Test
    public void testWeightWithEmptyArray() {
        double[] weights = {};
        Weight weight = new Weight(weights);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(0, 0);
        RealMatrix actualWeight = weight.getWeight();

        assertNotNull(actualWeight);
        assertEquals(expectedWeight, actualWeight);
    }
}