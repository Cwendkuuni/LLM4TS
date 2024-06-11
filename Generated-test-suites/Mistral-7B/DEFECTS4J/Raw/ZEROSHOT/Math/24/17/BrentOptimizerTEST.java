package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateFunction;

public class BrentOptimizerTest {

    private UnivariateOptimizer optimizer;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-6, 1e-6);
        function = new SimpleFunction();
    }

    @After
    public void tearDown() {
        optimizer = null;
        function = null;
    }

    @Test
    public void testOptimize_minimum() throws ConvergenceException,
            MaxCountExceededException, NotStrictlyPositiveException {
        SimpleBounds bounds = new SimpleBounds(0, 2);
        PointValuePair result = optimizer.optimize(function, bounds, null);
        assertEquals(0.75, result.getPoint(), 1e-6);
        assertEquals(-0.2164671276281514, result.getValue(), 1e-6);
    }

    @Test
    public void testOptimize_maximum() throws ConvergenceException,
            MaxCountExceededException, NotStrictlyPositiveException {
        SimpleBounds bounds = new SimpleBounds(0, 2);
        optimizer = new BrentOptimizer(1e-6, 1e-6, new GoalType(GoalType.MAXIMIZE));
        PointValuePair result = optimizer.optimize(function, bounds, null);
        assertEquals(1.25, result.getPoint(), 1e-6);
        assertEquals(2.574232872858664, result.getValue(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimize_invalidAbsoluteThreshold() throws ConvergenceException,
            MaxCountExceededException {
        optimizer = new BrentOptimizer(-1e-6, 1e-6, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testOptimize_invalidRelativeThreshold() throws ConvergenceException,
            MaxCountExceededException {
        optimizer = new BrentOptimizer(1e-7, 1e-6, null);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimize_functionWithZeroDerivative() throws ConvergenceException,
            MaxCountExceededException, NotStrictlyPositiveException {
        SimpleBounds bounds = new SimpleBounds(0, 2);
        optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        UnivariateFunction function = new SimpleFunctionWithZeroDerivative();
        PointValuePair result = optimizer.optimize(function, bounds, null);
        fail("Optimization should fail with ConvergenceException");
    }

    @Test(expected = MaxCountExceededException.class)
    public void testOptimize_maxCountExceeded() throws ConvergenceException,
            MaxCountExceededException, NotStrictlyPositiveException {
        SimpleFunction function = new SimpleFunctionWithMaxCountExceeded();
        SimpleBounds bounds = new SimpleBounds(0, 2);
        optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        PointValuePair result = optimizer.optimize(function, bounds, null);
        fail("Optimization should fail with MaxCountExceededException");
    }

    private static class SimpleFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return FastMath.sin(x) / x;
        }

        @Override
        public double derivative(double x) {
            return (FastMath.cos(x) / x - FastMath.sin(x) / (x * x)) * x;
        }
    }

    private static class SimpleFunctionWithZeroDerivative implements UnivariateFunction {

        @Override
        public double value(double x) {
            return x * x * x - 1;
        }

        @Override
        public double derivative(double x) {
            return 0;
        }
    }

    private static class SimpleFunctionWithMaxCountExceeded implements UnivariateFunction {

        private int count = 0;

        @Override
        public double value(double x) {
            count++;
            if (count > 1000) {
                throw new MaxCountExceededException(1000, count);
            }
            return FastMath.sin(x) / x;
        }

        @Override
        public double derivative(double x) {
            count++;
            if (count > 1000) {
                throw new MaxCountExceededException(1000, count);
            }
            return (FastMath.cos(x) / x - FastMath.sin(x) / (x * x)) * x;
        }
    }
}