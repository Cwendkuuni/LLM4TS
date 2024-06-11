package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Fibonacci1D;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentMinimizer.IterationData;
import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.optimization.Weight;

/**
 * Test class for Weight.
 */
public class WeightTest {

    /**
     * Test constructor with double array.
     */
    @Test
    public void testConstructorWithDoubleArray() {
        double[] weight = new double[] {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix weightMatrix = weightObj.getWeight();
        Assert.assertArrayEquals(new double[][] {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}}, weightMatrix.getData());
    }

    /**
     * Test constructor with RealMatrix.
     */
    @Test
    public void testConstructorWithRealMatrix() {
        double[][] weightData = new double[][] {{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}};
        RealMatrix weight = new Array2DRowRealMatrix(weightData);
        Weight weightObj = new Weight(weight);
        RealMatrix weightMatrix = weightObj.getWeight();
        Assert.assertArrayEquals(weightData, weightMatrix.getData());
    }

    /**
     * Test getWeight method.
     */
    @Test
    public void testGetWeight() {
        double[] weight = new double[] {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix weightMatrix = weightObj.getWeight();
        RealMatrix copyWeightMatrix = weightMatrix.copy();
        Assert.assertArrayEquals(weight, copyWeightMatrix.getData()[0]);
    }

    /**
     * Test exception in constructor with non-square matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testExceptionInConstructorWithNonSquareMatrix() {
        double[][] weightData = new double[][] {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix weight = new Array2DRowRealMatrix(weightData);
        new Weight(weight);
    }
}