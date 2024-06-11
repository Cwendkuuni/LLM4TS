package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstruction() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3);
        expectedWeight.setEntry(0, 0, weight[0]);
        expectedWeight.setEntry(1, 1, weight[1]);
        expectedWeight.setEntry(2, 2, weight[2]);

        assertEquals(expectedWeight, weightObj.getWeight());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightSquareMatrixException() {
        double[][] weight = {{1.0, 2.0}, {3.0, 4.0}};
        Weight weightObj = new Weight(new Array2DRowRealMatrix(weight));
    }

    @Test
    public void testWeightSquareMatrix() {
        double[][] weight = {{1.0, 0.0}, {0.0, 1.0}};
        Weight weightObj = new Weight(new Array2DRowRealMatrix(weight));
        RealMatrix expectedWeight = new Array2DRowRealMatrix(weight);

        assertEquals(expectedWeight, weightObj.getWeight());
    }

    @Test(expected = NullPointerException.class)
    public void testWeightNullInput() {
        new Weight(null);
    }
}