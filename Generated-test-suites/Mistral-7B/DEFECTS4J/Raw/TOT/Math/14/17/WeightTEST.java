package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixTestCase;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnconstrainedMinimizer;
import org.apache.commons.math3.optim.nonlinear.LeastSquaresOptimizer;
import org.apache.commons.math3.optim.nonlinear.linear.LevenbergMarquardtOptimizer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the {@link Weight} class.
 */
public class WeightTest extends RealMatrixTestCase {

    /**
     * Tests constructor with a diagonal weight matrix.
     */
    @Test
    public void testDiagonalWeightMatrix() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 1.0);
        Assert.assertArrayEquals(expected.getData(), weightObj.getWeight().getData(), 0.0);
    }

    /**
     * Tests constructor with a non-diagonal weight matrix.
     */
    @Test
    public void testNonDiagonalWeightMatrix() {
        double[][] weight = {{1.0, 0.5}, {0.5, 2.0}};
        RealMatrix realMatrix = new Array2DRowRealMatrix(weight);
        Weight weightObj = new Weight(realMatrix);
        Assert.assertEquals(realMatrix, weightObj.getWeight());
    }

    /**
     * Tests method getWeight() with a diagonal weight matrix.
     */
    @Test
    public void testGetWeightDiagonal() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0);
        RealMatrix actual = weightObj.getWeight();
        Assert.assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }

    /**
     * Tests method getWeight() with a non-diagonal weight matrix.
     */
    @Test
    public void testGetWeightNonDiagonal() {
        double[][] weight = {{1.0, 0.5}, {0.5, 2.0}};
        Weight weightObj = new Weight(new Array2DRowRealMatrix(weight));
        RealMatrix expected = new Array2DRowRealMatrix(weight);
        RealMatrix actual = weightObj.getWeight();
        Assert.assertEquals(expected, actual);
    }

    /**
     * Tests initialization of a LeastSquaresOptimizer with a non-square weight matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testInitializeLeastSquaresWithNonSquareWeight() {
        double[][] weight = {{1.0, 0.5}, {0.5, 2.0}};
        RealMatrix realMatrix = new Array2DRowRealMatrix(weight);
        Weight weightObj = new Weight(realMatrix);
        SimpleBounds bounds = new SimpleBounds(new double[0], new double[0]);
        InitialGuess initialGuess = new InitialGuess() {
            @Override
            public RealVector getInitialGuess(int dimension) {
                return new ArrayRealVector(new double[dimension]);
            }
        };
        UnconstrainedMinimizer minimizer = new LeastSquaresOptimizer();
        minimizer.initialize(new Weight(new Array2DRowRealMatrix(weight)), new PointValuePair(new DenseVector(new double[0]), 0.0), bounds, initialGuess, new MaxEval(100));
    }

    /**
     * Tests initialization of a LevenbergMarquardtOptimizer with a non-square weight matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testInitializeLevenbergMarquardtWithNonSquareWeight() {
        double[][] weight = {{1.0, 0.5}, {0.5, 2.0}};
        RealMatrix realMatrix = new Array2DRowRealMatrix(weight);
        Weight weightObj = new Weight(realMatrix);
        SimpleBounds bounds = new SimpleBounds(new double[0], new double[0]);
        InitialGuess initialGuess = new InitialGuess() {
            @Override
            public RealVector getInitialGuess(int dimension) {
                return new ArrayRealVector(new double[dimension]);
            }
        };
        UnconstrainedMinimizer minimizer = new LevenbergMarquardtOptimizer();
        minimizer.initialize(new Weight(new Array2DRowRealMatrix(weight)), new PointValuePair(new DenseVector(new double[0]), 0.0), bounds, initialGuess, new MaxEval(100));
    }
}