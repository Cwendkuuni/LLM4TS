package org.apache.commons.math.optimization.direct;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealVector;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMultidim;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearch;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Test cases for MultiDirectional class.
 */
public class MultiDirectionalTest {

    /**
     * Test for MultiDirectional optimizer with default coefficients.
     */
    @Test
    public void testMultiDirectionalDefault() {
        MultiDirectional optimizer = new MultiDirectional();
        ObjectiveFunction function = new FibonacciSearch(new MyFunction(), new SimpleBounds(1.0, 5.0),
                GoalTypeMultidim.MINIMIZE, new MaxEval(100), new InitialGuess(new Double[]{2.5}));
        optimizer.optimize(function);
        PointValuePair solution = function.getSolution();
        Assert.assertNotNull(solution);
        Assert.assertEquals(1.61803398875, solution.getPoint()[0], 1e-6);
    }

    /**
     * Test for MultiDirectional optimizer with specified coefficients.
     */
    @Test
    public void testMultiDirectionalCustom() {
        MultiDirectional optimizer = new MultiDirectional(1.5, 0.7);
        ObjectiveFunction function = new MyFunction();
        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        PointValuePair solution = optimizer.optimize(function, bounds, GoalType.MINIMIZE, new MaxEval(100));
        Assert.assertNotNull(solution);
        Assert.assertEquals(3.0, solution.getPoint()[0], 1e-6);
    }

    /**
     * Test for MultiDirectional optimizer with unbounded domain.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMultiDirectionalUnbounded() {
        MultiDirectional optimizer = new MultiDirectional();
        ObjectiveFunction function = new MyFunction();
        optimizer.optimize(function, new SimpleBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
                GoalType.MINIMIZE, new MaxEval(100));
    }

    /**
     * Test for MultiDirectional optimizer withNoBracketing.
     */
    @Test(expected = NoBracketingException.class)
    public void testMultiDirectionalNoBracketing() {
        MultiDirectional optimizer = new MultiDirectional();
        ObjectiveFunction function = new MyFunctionWithNoBracketing();
        optimizer.optimize(function, new SimpleBounds(0.0, 1.0), GoalType.MINIMIZE, new MaxEval(100));
    }

    /**
     * Test for MultiDirectional optimizer withMaxEvalExceeded.
     */
    @Test(expected = MaxEvalExceededException.class)
    public void testMultiDirectionalMaxEvalExceeded() {
        MultiDirectional optimizer = new MultiDirectional();
        ObjectiveFunction function = new MyFunction();
        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        optimizer.optimize(function, bounds, GoalType.MINIMIZE, new MaxEval(1));
    }

    /**
     * Test for MultiDirectional optimizer withNullFunction.
     */
    @Test(expected = NullPointerException.class)
    public void testMultiDirectionalNullFunction() {
        MultiDirectional optimizer = new MultiDirectional();
        optimizer.optimize(null, new SimpleBounds(0.0, 10.0), GoalType.MINIMIZE, new MaxEval(100));
    }

    /**
     * Objective function for testing.
     */
    private static class MyFunction implements ObjectiveFunction {

        @Override
        public double value(RealVector x) {
            return Math.pow(Math.sin(Math.PI * x.getEntry(0)), 2.0);
        }

        @Override
        public RealVector gradient(RealVector x) {
            double[] gradient = new double[1];
            gradient[0] = -2.0 * Math.sin(Math.PI * x.getEntry(0)) * Math.cos(Math.PI * x.getEntry(0));
            return new ArrayRealVector(gradient);
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    }

    /**
     * Objective function for testing withNoBracketing.
     */
    private static class MyFunctionWithNoBracketing implements ObjectiveFunction {

        @Override
        public double value(RealVector x) {
            return Math.sin(Math.PI * x.getEntry(0));
        }
    }
}