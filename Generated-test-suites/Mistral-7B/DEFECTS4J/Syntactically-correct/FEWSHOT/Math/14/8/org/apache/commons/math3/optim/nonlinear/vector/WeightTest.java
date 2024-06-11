package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class WeightTest {
    @Test
    public void testWeightDiagonalConstruction() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);

        RealMatrix actual = weightObj.getWeight();
        assertNotNull(actual);
        assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }

    @Test
    public void testWeightSquareMatrixConstruction() {
        double[][] weight = {{1.0, 0.0}, {0.0, 2.0}};
        Weight weightObj = new Weight(new Array2DRowRealMatrix(weight));
        RealMatrix expected = new Array2DRowRealMatrix(2, 2);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(0, 1, 0.0);
        expected.setEntry(1, 0, 0.0);
        expected.setEntry(1, 1, 2.0);

        RealMatrix actual = weightObj.getWeight();
        assertNotNull(actual);
        assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightNonSquareMatrixConstruction() {
        double[][] weight = {{1.0, 0.0}, {0.0, 2.0, 3.0}};
        new Weight(new Array2DRowRealMatrix(weight));
    }

    @Test
    public void testWeightGetNull() {
        Weight weightObj = new Weight(null);
        assertNull(weightObj.getWeight());
    }
}