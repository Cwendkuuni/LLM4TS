package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.initializer.Weight;
import org.junit.Assert;
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
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3, new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 2.0, 0.0},
                {0.0, 0.0, 3.0}
        });
        RealMatrix actualWeight = weight.getWeight();
        Assert.assertEquals(expectedWeight, actualWeight);
    }

    /**
     * Test constructor with RealMatrix.
     */
    @Test
    public void testWeightConstructorWithRealMatrix() {
        double[][] weights = {{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}, {0.0, 0.0}, {3.0, 0.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(5, 5, weights);
        Weight weight = new Weight(weightMatrix);
        RealMatrix expectedWeight = weightMatrix.copy();
        RealMatrix actualWeight = weight.getWeight();
        Assert.assertEquals(expectedWeight, actualWeight);

        try {
            new Weight(new Array2DRowRealMatrix(3, 4, new double[][]{
                    {1.0, 0.0, 0.0, 0.0},
                    {0.0, 2.0, 0.0, 0.0},
                    {0.0, 0.0, 3.0, 0.0}
            }));
            Assert.fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Expected
        }
    }
}