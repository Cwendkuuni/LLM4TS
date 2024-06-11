package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.NonSquareMatrixException;

import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightDiagonal() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix weightMatrix = weightObj.getWeight();

        assertArrayEquals(weight, weightMatrix.getData(), 0.001);
    }

    @Test
    public void testWeightSquare() {
        double[][] weight = {{1.0, 0.0}, {0.0, 2.0}};
        Weight weightObj = new Weight(new Array2DRowRealMatrix(weight));
        RealMatrix weightMatrix = weightObj.getWeight();

        SingularValueDecomposition svd = weightMatrix.getSingularValueDecomposition();
        assertArrayEquals(weight, svd.getV().getData(), 0.001);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightNonSquare() {
        double[][] weight = {{1.0, 0.0}, {0.0, 1.0, 2.0}};
        new Weight(new Array2DRowRealMatrix(weight));
    }

    @Test
    public void testWeightCopy() {
        double[][] weight = {{1.0, 0.0}, {0.0, 2.0}};
        Weight weightObj = new Weight(new Array2DRowRealMatrix(weight));
        RealMatrix weightMatrix = weightObj.getWeight();

        RealMatrix weightMatrixCopy = weightMatrix.copy();
        assertNotNull(weightMatrixCopy);
        assertArrayEquals(weightMatrix.getData(), weightMatrixCopy.getData(), 0.001);
    }

    @Test
    public void testWeightIsNull() {
        Weight weightObj = new Weight(null);
        assertNull(weightObj.getWeight());
    }
}