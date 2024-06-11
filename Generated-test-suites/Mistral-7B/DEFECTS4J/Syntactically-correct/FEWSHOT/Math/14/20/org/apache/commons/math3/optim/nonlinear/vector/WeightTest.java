package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.NonSquareMatrixException;

import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstructor() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = RealMatrixUtils.diagonalMatrix(weights);
        RealMatrix actual = weight.getWeight();
        assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }

    @Test
    public void testWeightSquareMatrixConstructor() {
        double[][] weights = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weights);
        Weight weight = new Weight(weightMatrix);
        RealMatrix expected = weightMatrix;
        RealMatrix actual = weight.getWeight();
        assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightNonSquareMatrixConstructor() {
        double[][] weights = {{1.0, 0.0}, {0.0, 1.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weights);
        new Weight(weightMatrix);
    }

    @Test
    public void testWeightGetNull() {
        Weight weight = new Weight(null);
        assertNull(weight.getWeight());
    }

    @Test
    public void testWeightCopy() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = weight.getWeight();
        weight = new Weight(weight.getWeight());
        RealMatrix actual = weight.getWeight();
        assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }

    @Test
    public void testWeightEquals() {
        double[] weights1 = {1.0, 2.0, 3.0};
        double[] weights2 = {1.0, 2.0, 3.0};
        Weight weight1 = new Weight(weights1);
        Weight weight2 = new Weight(weights2);
        assertTrue(weight1.equals(weight2));
        Weight weight3 = new Weight(weights1);
        weight3.weightMatrix.setEntry(1, 1, 4.0);
        assertFalse(weight1.equals(weight3));
    }
}