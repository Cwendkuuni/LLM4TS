package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.optimization.Weight.WeightBuilder;
import org.junit.Test;

/**
 * Test cases for the Weight class.
 */
public class WeightTest {

    /**
     * Test constructor with double array.
     */
    @Test
    public void testConstructorWithDoubleArray() {
        double[] weight = new double[] {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][] {
                {1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    /**
     * Test constructor with RealMatrix.
     */
    @Test
    public void testConstructorWithRealMatrix() throws NonSquareMatrixException {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][] {
                {1.0, 0.0}, {0.0, 2.0}});
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = weight.copy();
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    /**
     * Test getWeight method.
     */
    @Test
    public void testGetWeight() {
        double[] weight = new double[] {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][] {
                {1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    /**
     * Test constructor with null weight array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullWeightArray() {
        double[] weight = null;
        new Weight(weight);
    }

    /**
     * Test constructor with empty weight array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyWeightArray() {
        double[] weight = {};
        new Weight(weight);
    }

    /**
     * Test constructor with non-square RealMatrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareRealMatrix() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][] {
                {1.0, 0.0}, {0.0, 2.0}, {0.0, 3.0}});
        new Weight(weight);
    }

    /**
     * Test constructor with null RealMatrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullRealMatrix() {
        RealMatrix weight = null;
        new Weight(weight);
    }
}