package org.apache.commons.math.optimization.direct;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.SimpleGoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearch;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the MultiDirectional optimization algorithm from the
 * Apache Commons Math library.
 */
public class MultiDirectionalTest {

    private MultiDirectional optimizer;
    private ObjectiveFunction function;

    /**
     * Sets up the test environment by creating the MultiDirectional optimizer
     * and the test function.
     */
    @Before
    public void setUp() {
        function = new MyTestFunction();
        optimizer = new MultiDirectional();
    }

    /**
     * Tests the MultiDirectional optimizer with a simple test function and
     * the default options.
     * @throws Exception if an error occurs during the optimization process
     */
    @Test
    public void testSimpleOptimization() throws Exception {
        final double[] lowerBounds = new double[] { -10.0, -10.0 };
        final double[] upperBounds = new double[] { 10.0, 10.0 };
        final SimpleBounds bounds = new SimpleBounds(lowerBounds, upperBounds);

        optimizer.optimize(function, bounds, new SimpleGoalType(GoalType.MINIMIZE), null);

        // check the result
        final PointValuePair result = optimizer.getBestSolution();
        final double expected = -Math.E;
        Assert.assertEquals("The optimization did not return the expected minimum", expected, result.getPoint()[0], 0.001);
    }

    /**
     * Tests the MultiDirectional optimizer with a simple test function and
     * custom coefficients.
     * @throws Exception if an error occurs during the optimization process
     */
    @Test
    public void testCustomCoefficientsOptimization() throws Exception {
        final double[] lowerBounds = new double[] { -10.0, -10.0 };
        final double[] upperBounds = new double[] { 10.0, 10.0 };
        final SimpleBounds bounds = new SimpleBounds(lowerBounds, upperBounds);

        final MultiDirectional optimizer = new MultiDirectional(1.5, 0.7);

        optimizer.optimize(function, bounds, new SimpleGoalType(GoalType.MINIMIZE), null);

        // check the result
        final PointValuePair result = optimizer.getBestSolution();
        final double expected = -Math.E * 1.5;
        Assert.assertEquals("The optimization did not return the expected minimum", expected, result.getPoint()[0], 0.001);
    }

    /**
     * Tests the MultiDirectional optimizer with a test function that has no
     * solution.
     * @throws Exception if an error occurs during the optimization process
     */
    @Test(expected = NoSolutionException.class)
    public void testNoSolution() throws Exception {
        final double[] lowerBounds = new double[] { -10.0, -10.0 };
        final double[] upperBounds = new double[] { 10.0, 10.0 };
        final SimpleBounds bounds = new SimpleBounds(lowerBounds, upperBounds);

        optimizer.optimize(new NoSolutionFunction(), bounds, new SimpleGoalType(GoalType.MINIMIZE), null);
    }

    /**
     * Test the MultiDirectional optimizer when the maximum number of function
     * evaluations is exceeded.
     * @throws Exception if an error occurs during the optimization process
     */
    @Test(expected = MaxEvalExceededException.class)
    public void testMaxEvalExceeded() throws Exception {
        final double[] lowerBounds = new double[] { -10.0, -10.0 };
        final double[] upperBounds = new double[] { 10.0, 10.0 };
        final SimpleBounds bounds = new SimpleBounds(lowerBounds, upperBounds);

        optimizer.setMaxEvaluations(5);
        optimizer.optimize(function, bounds, new SimpleGoalType(GoalType.MINIMIZE), null);
    }

    /**
     * Test the MultiDirectional optimizer with a test function that throws an
     * exception.
     * @throws Exception if an error occurs during the optimization process
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionException() throws Exception {
        final double[] lowerBounds = new double[] { -10.0, -10.0 };
        final double[] upperBounds = new double[] { 10.0, 10.0 };
        final SimpleBounds bounds = new SimpleBounds(lowerBounds, upperBounds);

        function = new ThrowingFunction();

        optimizer.optimize(function, bounds, new SimpleGoalType(GoalType.MINIMIZE), null);
    }

}

/**
 * A simple test function used to test the MultiDirectional optimizer.
 */
class MyTestFunction implements ObjectiveFunction {

    @Override
    public double value(double[] x) {
        return Math.exp(-x[0] * x[0]) + Math.exp(-x[1] * x[1]);
    }

    @Override
    public int getNumberOfVariables() {
        return 2;
    }

}

/**
 * A test function that has no solution.
 */
class NoSolutionFunction implements ObjectiveFunction {

    @Override
    public double value(double[] x) {
        return Math.sqrt(-1.0) * Math.sin(Math.PI);
    }

    @Override
    public int getNumberOfVariables() {
        return 1;
    }

}

/**
 * A test function that throws an exception when evaluated.
 */
class ThrowingFunction implements ObjectiveFunction {

    @Override
    public double value(double[] x) throws FunctionEvaluationException {
        throw new FunctionEvaluationException("Function evaluation error!");
    }

    @Override
    public int getNumberOfVariables() {
        return 1;
    }

}