package org.apache.commons.math3.optimization;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstruction() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);

        RealMatrix expectedWeight = RealMatrixUtils.diagonalMatrix(weight);
        RealMatrix actualWeight = weightObj.getWeight();

        assertEquals(expectedWeight, actualWeight);
    }

    @Test
    public void testWeightSquareMatrixConstruction() {
        RealMatrix weight = RealMatrixUtils.createMatrix(3, 3, i -> i * i);
        Weight weightObj = new Weight(weight);

        RealMatrix expectedWeight = weight.copy();
        RealMatrix actualWeight = weightObj.getWeight();

        assertEquals(expectedWeight, actualWeight);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightNonSquareMatrixConstruction() {
        RealMatrix weight = RealMatrixUtils.createMatrix(3, 2, i -> i * i);
        new Weight(weight);
    }
}