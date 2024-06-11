package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstruction() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);

        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3);
        expectedWeight.setEntry(0, 0, weights[0]);
        expectedWeight.setEntry(1, 1, weights[1]);
        expectedWeight.setEntry(2, 2, weights[2]);

        RealMatrix actualWeight = weight.getWeight();

        assertNotNull(actualWeight);
        assertArrayEquals(expectedWeight.getData(), actualWeight.getData(), 0.0);
    }

    @Test
    public void testWeightSquareMatrixConstruction() {
        double[][] weights = {{1.0}, {2.0, 2.0}, {3.0, 3.0, 3.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weights);

        Weight weight = new Weight(weightMatrix);

        RealMatrix expectedWeight = weightMatrix;

        RealMatrix actualWeight = weight.getWeight();

        assertNotNull(actualWeight);
        assertArrayEquals(expectedWeight.getData(), actualWeight.getData(), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightNonSquareMatrixConstruction() {
        double[][] weights = {{1.0}, {2.0, 2.0}, {3.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weights);

        Weight weight = new Weight(weightMatrix);
    }
}