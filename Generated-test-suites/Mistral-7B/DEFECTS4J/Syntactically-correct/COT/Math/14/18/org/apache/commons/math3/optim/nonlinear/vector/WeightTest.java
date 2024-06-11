package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.InitialGuess;
import org.junit.Test;

/**
 * Test class for Weight.
 */
public class WeightTest {

    /**
     * Test constructor with double array.
     */
    @Test
    public void testWeightConstructorWithDoubleArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3, weight);
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    /**
     * Test constructor with RealMatrix.
     */
    @Test
    public void testWeightConstructorWithRealMatrix() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}});
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = weight.copy();
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    /**
     * Test constructor with invalid RealMatrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithInvalidRealMatrix() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}, {0.0, 3.0}});
        new Weight(weight);
    }

    /**
     * Test getWeight method.
     */
    @Test
    public void testGetWeight() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3, weight);
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    /**
     * Test getWeight method with RealMatrix constructor.
     */
    @Test
    public void testGetWeightWithRealMatrixConstructor() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}});
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = weight.copy();
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    /**
     * Test getWeight method with Singular Value Decomposition.
     */
    @Test
    public void testGetWeightWithSingularValueDecomposition() {
        double[][] input = {{1.0, 0.0}, {0.0, 2.0}, {0.0, 1.0}};
        RealMatrix inputMatrix = new Array2DRowRealMatrix(input);
        SingularValueDecomposition svd = new SingularValueDecomposition(inputMatrix);
        RealMatrix expectedWeight = svd.getU().transpose().multiply(svd.getDiagonalMatrix().multiply(svd.getU()));
        Weight weightObj = new Weight(inputMatrix);
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    /**
     * Test getWeight method with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetWeightWithNullArgument() {
        Weight weightObj = new Weight(null);
        weightObj.getWeight();
    }
}