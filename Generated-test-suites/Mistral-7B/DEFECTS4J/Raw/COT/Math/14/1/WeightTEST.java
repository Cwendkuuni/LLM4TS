package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optim.initializestrassen.InitialGuess;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.optimization.linear.GoalType;
import org.apache.commons.math3.optimization.nonlinear.LinearConstraint;
import org.apache.commons.math3.optimization.nonlinear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.nonlinear.LinearConstraintSet;
import org.apache.commons.math3.optimization.nonlinear.linear.LinearConstraintSetFactory;
import org.apache.commons.math3.optimization.nonlinear.linear.LsqLinearConstraints;
import org.apache.commons.math3.optimization.nonlinear.linear.LsqLinearObjectiveFunction;
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
    public void testConstructorWithDoubleArray() {
        // Given
        double[] weight = new double[]{1.0, 2.0, 3.0};
        Weight weightInstance = new Weight(weight);

        // When
        RealMatrix actualWeight = weightInstance.getWeight();

        // Then
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3)
                .setEntry(0, 0, 1.0)
                .setEntry(1, 1, 2.0)
                .setEntry(2, 2, 3.0);
        Assert.assertArrayEquals(expectedWeight.getData(), actualWeight.getData(), 0.0);
    }

    /**
     * Test constructor with RealMatrix.
     */
    @Test
    public void testConstructorWithRealMatrix() {
        // Given
        RealMatrix weight = new Array2DRowRealMatrix(3, 3)
                .setEntry(0, 0, 1.0)
                .setEntry(1, 1, 2.0)
                .setEntry(2, 2, 3.0);
        Weight weightInstance = new Weight(weight);

        // When
        RealMatrix actualWeight = weightInstance.getWeight();

        // Then
        Assert.assertEquals(weight, actualWeight);
    }

    /**
     * Test exception when constructing with non-square matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testExceptionWithNonSquareMatrix() {
        // Given
        RealMatrix weight = new Array2DRowRealMatrix(3, 2)
                .setEntry(0, 0, 1.0)
                .setEntry(1, 0, 2.0)
                .setEntry(2, 0, 3.0)
                .setEntry(0, 1, 4.0)
                .setEntry(1, 1, 5.0)
                .setEntry(2, 1, 6.0);

        // When
        new Weight(weight);

        // Then (exception is thrown)
    }

    /**
     * Test getWeight method.
     */
    @Test
    public void testGetWeight() {
        // Given
        double[] weight = new double[]{1.0, 2.0, 3.0};
        Weight weightInstance = new Weight(weight);

        // When
        RealMatrix actualWeight = weightInstance.getWeight();

        // Then
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3)
                .setEntry(0, 0, 1.0)
                .setEntry(1, 1, 2.0)
                .setEntry(2, 2, 3.0);
        Assert.assertArrayEquals(expectedWeight.getData(), actualWeight.getData(), 0.0);
    }

    /**
     * Test getWeight method with pre-existing RealMatrix.
     */
    @Test
    public void testGetWeightWithPreExistingRealMatrix() {
        // Given
        RealMatrix weight = new Array2DRowRealMatrix(3, 3)
                .setEntry(0, 0, 1.0)
                .setEntry(1, 1, 2.0)
                .setEntry(2, 2, 3.0);
        Weight weightInstance = new Weight(weight);

        // When
        RealMatrix actualWeight = weightInstance.getWeight();

        // Then
        Assert.assertEquals(weight, actualWeight);
    }
}