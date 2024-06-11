package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;

import org.apache.commons.math3.optimization.Weight;

/**
 * Test class for Weight class.
 */
public class WeightTest {

    /**
     * Test constructor with double array.
     */
    @Test
    public void testWeightConstructorWithDoubleArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightInstance = new Weight(weight);
        RealMatrix expectedWeight = RealMatrixUtils.createMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                                     {0.0, 2.0, 0.0},
                                                                                     {0.0, 0.0, 3.0}});
        RealMatrix actualWeight = weightInstance.getWeight();
        assertEquals("Weight matrix is not as expected", expectedWeight, actualWeight);
    }

    /**
     * Test constructor with RealMatrix.
     */
    @Test
    public void testWeightConstructorWithRealMatrix() {
        RealMatrix weight = RealMatrixUtils.createMatrix(new double[][]{{1.0, 0.0},
                                                                          {0.0, 2.0}});
        Weight weightInstance = new Weight(weight);
        RealMatrix expectedWeight = weight.copy();
        RealMatrix actualWeight = weightInstance.getWeight();
        assertEquals("Weight matrix is not as expected", expectedWeight, actualWeight);
    }

    /**
     * Test constructor with NonSquareMatrixException.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithNonSquareMatrixException() {
        RealMatrix weight = RealMatrixUtils.createMatrix(new double[][]{{1.0, 0.0},
                                                                          {0.0, 2.0, 3.0}});
        new Weight(weight);
    }
}