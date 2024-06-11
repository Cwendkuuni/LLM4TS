package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightDiagonal() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix weightMatrix = weightObj.getWeight();

        RealVector v1 = weightMatrix.getColumnVector(0);
        RealVector v2 = weightMatrix.getColumnVector(1);
        RealVector v3 = weightMatrix.getColumnVector(2);

        assertEquals(weight[0], v1.getEntry(0), 0.0);
        assertEquals(weight[1], v2.getEntry(0), 0.0);
        assertEquals(weight[2], v3.getEntry(0), 0.0);
        assertNull(v1.getEntry(1));
        assertNull(v2.getEntry(1));
        assertNull(v3.getEntry(1));
    }

    @Test
    public void testWeightSquareMatrix() {
        double[][] weight = {{1.0, 2.0}, {2.0, 3.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        Weight weightObj = new Weight(weightMatrix);
        RealMatrix weightCopy = weightObj.getWeight();

        assertArrayEquals(weight, weightCopy.getData(), 0.0);
    }

    @Test(expected = org.apache.commons.math3.linear.NonSquareMatrixException.class)
    public void testWeightNonSquareMatrix() {
        double[][] weight = {{1.0, 2.0}, {2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        new Weight(weightMatrix);
    }
}