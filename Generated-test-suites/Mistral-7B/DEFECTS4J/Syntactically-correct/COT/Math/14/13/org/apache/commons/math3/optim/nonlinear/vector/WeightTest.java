package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Weight class.
 */
public class WeightTest {

    @Test
    public void testWeightDiagonalConstructor() {
        double[] weight = new double[]{1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);

        RealMatrix actual = weightObj.getWeight();
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWeightSquareMatrixConstructor() {
        double[][] weight = new double[][]{{1.0, 2.0}, {2.0, 3.0}};
        RealMatrix weightObj = new Array2DRowRealMatrix(weight);
        Weight weightInstance = new Weight(weightObj);
        RealMatrix expected = weightObj.copy();

        RealMatrix actual = weightInstance.getWeight();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightSquareMatrixConstructor_ThrowsException() {
        double[][] weight = new double[][]{{1.0, 2.0}, {2.0}};
        RealMatrix weightObj = new Array2DRowRealMatrix(weight);
        Weight weightInstance = new Weight(weightObj);
    }

    @Test
    public void testGetWeight_DiagonalMatrix() {
        double[] weight = new double[]{1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);

        RealMatrix actual = weightObj.getWeight();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetWeight_SquareMatrix() {
        double[][] weight = new double[][]{{1.0, 2.0}, {2.0, 3.0}};
        RealMatrix weightObj = new Array2DRowRealMatrix(weight);
        Weight weightInstance = new Weight(weightObj);
        RealMatrix expected = weightObj.copy();

        RealMatrix actual = weightInstance.getWeight();
        Assert.assertEquals(expected, actual);
    }
}