package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.WeightedSumFunction;
import org.apache.commons.math3.optim.linear.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.nonlinear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.LinearConstraints;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    /**
     * Test constructor with a diagonal weight matrix.
     */
    @Test
    public void testDiagonalWeightMatrix() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3, 1.0);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);

        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }

    /**
     * Test constructor with a non-diagonal weight matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonDiagonalWeightMatrix() {
        double[][] weights = {{1.0, 2.0}, {3.0, 4.0}};
        Weight weight = new Weight(new Array2DRowRealMatrix(weights));
    }

    /**
     * Test method getWeight().
     */
    @Test
    public void testGetWeight() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3, 1.0);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);

        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);

        Weight weight2 = new Weight(new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}}));
        RealMatrix actual2 = weight2.getWeight();
        RealArray2D expected2 = new Array2DRowRealMatrix(2, 2, 1.0);
        expected2.setEntry(0, 0, 1.0);
        expected2.setEntry(0, 1, 0.0);
        expected2.setEntry(1, 0, 0.0);
        expected2.setEntry(1, 1, 1.0);
        assertEquals(expected2, actual2.transpose());
    }

    /**
     * Test the optimization process with a simple weight matrix.
     */
    @Test
    public void testOptimizationWithWeight() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        double[] expectedSolution = {1.0, 1.0, 1.0};
        Weight weight = new Weight(new Array2DRowRealMatrix(data));

        WeightedSumFunction f = new WeightedSumFunction(data);
        RealVector initialGuess = new Array2DRowRealVector(new double[]{0.0, 0.0, 0.0});
        LevenbergMarquardtOptimizer optimizer = new LevenbergMarquardtOptimizer();

        RealVector solution = optimizer.optimize(f, new InitialGuess(initialGuess), weight);

        assertEquals(expectedSolution, solution.toArray(), 0.001);
    }

    /**
     * Test the optimization process with a non-square weight matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testOptimizationWithNonSquareWeight() {
        double[][] data = {{1.0, 2.0}, {4.0, 5.0}};
        Weight weight = new Weight(new Array2DRowRealMatrix(data));

        WeightedSumFunction f = new WeightedSumFunction(data);
        RealVector initialGuess = new Array2DRowRealVector(new double[]{0.0, 0.0});
        LevenbergMarquardtOptimizer optimizer = new LevenbergMarquardtOptimizer();

        optimizer.optimize(f, new InitialGuess(initialGuess), weight);
    }

    /**
     * Test the optimization process with a weight matrix with constraints.
     */
    @Test
    public void testOptimizationWithWeightAndConstraint() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        double[] expectedSolution = {1.0, 1.0, 1.0};
        double[] lowerBounds = {-1.0, -1.0, -1.0};
        double[] upperBounds = {1.0, 1.0, 1.0};
        Weight weight = new Weight(new Array2DRowRealMatrix(data));

        WeightedSumFunction f = new WeightedSumFunction(data);
        RealVector initialGuess = new Array2DRowRealVector(new double[]{0.0, 0.0, 0.0});
        LinearConstraints constraints = new LinearConstraints(new LinearConstraint[]{
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}}), 1.0, lowerBounds, upperBounds)
        });

        LevenbergMarquardtOptimizer optimizer = new LevenbergMarquardtOptimizer(constraints);

        RealVector solution = optimizer.optimize(f, new InitialGuess(initialGuess), weight);

        assertEquals(expectedSolution, solution.toArray(), 0.001);
    }
}