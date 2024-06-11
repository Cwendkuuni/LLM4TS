package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.Weight;
import org.junit.Test;

/**
 * Test cases for Weight class.
 */
public class WeightTest {

    /**
     * Test constructor with a diagonal weight matrix.
     */
    @Test
    public void testDiagonalWeightMatrixConstruction() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(weight);
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals("Weight matrices are not equal.", expectedWeight, actualWeight);
    }

    /**
     * Test constructor with a non-diagonal weight matrix.
     */
    @Test
    public void testNonDiagonalWeightMatrixConstruction() {
        double[][] weight = {{1.0, 0.5}, {0.5, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        Weight weightObj = new Weight(weightMatrix);
        RealMatrix expectedWeight = weightMatrix.copy();
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals("Weight matrices are not equal.", expectedWeight, actualWeight);
    }

    /**
     * Test constructor with a non-square weight matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareWeightMatrixConstruction() {
        double[][] weight = {{1.0, 0.5}, {0.5}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        new Weight(weightMatrix);
    }

    /**
     * Test getWeight method.
     */
    @Test
    public void testGetWeightMethod() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(weight);
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals("Weight matrices are not equal.", expectedWeight, actualWeight);
    }
}