package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.linear.*;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstruction() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3, weight, false);
        RealMatrix actualWeight = weightObj.getWeight();
        assertNotNull(actualWeight);
        assertEquals(expectedWeight, actualWeight);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightNonSquareConstruction() {
        double[][] weight = {{1.0, 2.0}, {3.0, 4.0}};
        Weight weightObj = new Weight(new Array2DRowRealMatrix(weight, false));
    }

    @Test
    public void testWeightSquareConstruction() {
        double[][] weight = {{1.0, 0.0}, {0.0, 1.0}};
        Weight weightObj = new Weight(new Array2DRowRealMatrix(weight, false));
        RealMatrix expectedWeight = new Array2DRowRealMatrix(weight, false);
        RealMatrix actualWeight = weightObj.getWeight();
        assertNotNull(actualWeight);
        assertEquals(expectedWeight, actualWeight);
    }
}