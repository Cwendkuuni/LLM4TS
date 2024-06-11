package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstruction() {
        double[] weight = new double[]{1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);
        RealMatrix actual = w.getWeight();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testWeightSquareMatrixConstruction() {
        double[][] weight = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        Weight w = new Weight(weightMatrix);
        RealMatrix expected = weightMatrix.copy();
        RealMatrix actual = w.getWeight();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightNonSquareMatrixConstruction() {
        double[][] weight = new double[][]{{1.0, 0.5}, {0.5, 1.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        new Weight(weightMatrix);
    }

    @Test
    public void testGetWeight() {
        double[][] weight = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        Weight w = new Weight(weightMatrix);
        RealMatrix expected = weightMatrix.copy();
        RealMatrix actual = w.getWeight();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}