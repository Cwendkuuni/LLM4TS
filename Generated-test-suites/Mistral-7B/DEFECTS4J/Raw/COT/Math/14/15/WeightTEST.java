package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.optimization.WeightImpl;
import org.junit.Test;

/**
 * Weight test cases.
 */
public class WeightTest {

    /**
     * Test constructor with double array.
     */
    @Test
    public void testConstructorWithDoubleArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = RealMatrixUtils.createIdentityMatrix(3);
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals(expectedWeight.toString(), actualWeight.toString());
    }

    /**
     * Test constructor with RealMatrix.
     */
    @Test
    public void testConstructorWithRealMatrix() {
        double[][] weight = { {1.0, 0.0}, {0.0, 2.0} };
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        Weight weightObj = new Weight(weightMatrix);
        RealMatrix expectedWeight = weightMatrix;
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals(expectedWeight.toString(), actualWeight.toString());
    }

    /**
     * Test constructor with non-square matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        double[][] weight = { {1.0, 0.0}, {0.0, 2.0}, {3.0, 4.0} };
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        new Weight(weightMatrix);
    }
}