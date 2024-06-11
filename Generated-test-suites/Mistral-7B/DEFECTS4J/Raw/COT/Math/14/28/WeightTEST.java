package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstructor() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = RealMatrixUtils.diagonalMatrix(weight);
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    @Test
    public void testWeightSquareMatrixConstructor() {
        double[][] weight = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        Weight weightObj = new Weight(weightMatrix);
        RealMatrix expectedWeight = weightMatrix.copy();
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightNonSquareMatrixConstructor() {
        double[][] weight = {{1.0, 0.0}, {0.0, 2.0, 3.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        new Weight(weightMatrix);
    }
}