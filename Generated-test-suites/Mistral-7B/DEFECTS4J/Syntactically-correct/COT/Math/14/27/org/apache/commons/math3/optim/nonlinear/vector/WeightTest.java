package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.optimization.Weight;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for Weight class.
 */
public class WeightTest {

    /**
     * Test constructor with double array.
     */
    @Test
    public void testConstructorWithDoubleArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = RealMatrixUtils.diagonalMatrix(1.0, weight.length);
        RealMatrix actual = weightObj.getWeight();
        Assert.assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }

    /**
     * Test constructor with RealMatrix.
     */
    @Test
    public void testConstructorWithRealMatrix() {
        RealMatrix weight = RealMatrixUtils.denseMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}});
        Weight weightObj = new Weight(weight);
        RealMatrix expected = weight.copy();
        RealMatrix actual = weightObj.getWeight();
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test constructor with non-square RealMatrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareRealMatrix() {
        RealMatrix weight = RealMatrixUtils.denseMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0, 3.0}});
        new Weight(weight);
    }

    /**
     * Test getWeight method with diagonal matrix.
     */
    @Test
    public void testGetWeightWithDiagonalMatrix() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = RealMatrixUtils.diagonalMatrix(1.0, weight.length);
        RealMatrix actual = weightObj.getWeight();
        Assert.assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }

    /**
     * Test getWeight method with non-diagonal matrix.
     */
    @Test
    public void testGetWeightWithNonDiagonalMatrix() {
        RealMatrix weight = RealMatrixUtils.denseMatrix(new double[][]{{1.0, 0.5}, {0.5, 2.0}});
        Weight weightObj = new Weight(weight);
        RealMatrix expected = weight.copy();
        RealMatrix actual = weightObj.getWeight();
        Assert.assertEquals(expected, actual);
    }
}