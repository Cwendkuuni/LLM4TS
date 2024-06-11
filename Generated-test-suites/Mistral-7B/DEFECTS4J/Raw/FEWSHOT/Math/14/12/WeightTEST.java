package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstruction() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weights);

        RealMatrix expectedWeightMatrix = new Array2DRowRealMatrix(3, 3);
        expectedWeightMatrix.setEntry(0, 0, 1.0);
        expectedWeightMatrix.setEntry(1, 1, 2.0);
        expectedWeightMatrix.setEntry(2, 2, 3.0);

        RealMatrix actualWeightMatrix = weightObj.getWeight();
        assertNotNull(actualWeightMatrix);
        assertArrayEquals(expectedWeightMatrix.getData(), actualWeightMatrix.getData(), 0.0);
    }

    @Test
    public void testWeightMatrixConstruction() {
        RealMatrix weights = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}});
        Weight weightObj = new Weight(weights);

        RealMatrix expectedWeightMatrix = weights;

        RealMatrix actualWeightMatrix = weightObj.getWeight();
        assertNotNull(actualWeightMatrix);
        assertArrayEquals(expectedWeightMatrix.getData(), actualWeightMatrix.getData(), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightMatrixNonSquareException() {
        RealMatrix weights = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0, 2.0}});
        new Weight(weights);
    }

    @Test
    public void testGetWeightNull() {
        Weight weightObj = new Weight(null);
        assertNull(weightObj.getWeight());
    }
}