package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearchOptimizer;
import org.junit.Assert;
import org.junit.Test;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optimization.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optimization.nonlinear.scalar.noderiv.FibonacciSearchOptimizer;

/**
 * Testing {@link BaseOptimizer} interface.
 *
 * @since 3.0
 */
public class BaseOptimizerTest {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new FibonacciSearchOptimizer(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, Math.sin(x));
            }
        }, new SimpleBounds(0, 10), GoalType.MINIMIZE, MaxEval.ITERATIONS, 100);
        int maxEvaluations = optimizer.getMaxEvaluations();
        Assert.assertEquals(100, maxEvaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new FibonacciSearchOptimizer(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, Math.sin(x));
            }
        }, new SimpleBounds(0, 10), GoalType.MINIMIZE, MaxEval.ITERATIONS, 100);
        optimizer.optimize();
        int evaluations = optimizer.getEvaluations();
        Assert.assertGreaterThan(0, evaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new FibonacciSearchOptimizer(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, Math.sin(x));
            }
        }, new SimpleBounds(0, 10), GoalType.MINIMIZE, MaxEval.ITERATIONS, 100);
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        Assert.assertNotNull(convergenceChecker);
    }

    /**
     * Test method for {@link BaseOptimizer#optimize()} with a simple objective function.
     */
    @Test
    public void testOptimize_SimpleFunction() {
        BaseOptimizer<PointValuePair> optimizer = new FibonacciSearchOptimizer(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        }, new SimpleBounds(-5, 5), GoalType.MINIMIZE, MaxEval.ITERATIONS, 100);
        PointValuePair result = optimizer.optimize();
        Assert.assertNotNull(result);
        Assert.assertEquals(-2.414213771118531E9D, result.getValue(), 1.0E-10);
        Assert.assertEquals(-4.0, result.getPoint(), 1.0E-10);
    }

    /**
     * Test method for {@link BaseOptimizer#optimize()} with a complex objective function.
     */
    @Test
    public void testOptimize_ComplexFunction() {
        BaseOptimizer<PointValuePair> optimizer = new FibonacciSearchOptimizer(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.log(Math.sin(Math.PI * x) + Math.cos(Math.PI * x) / (2D * Math.PI));
            }
        }, new SimpleBounds(-5, 5), GoalType.MINIMIZE, MaxEval.ITERATIONS, 100);
        PointValuePair result = optimizer.optimize();
        Assert.assertNotNull(result);
        Assert.assertEquals(-1.134592583226423D, result.getValue(), 1.0E-10);
        Assert.assertEquals(0.317558212213546, result.getPoint(), 1.0E-10);
    }

    /**
     * Test method for {@link BaseOptimizer#optimize()} with an error scenario.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testOptimize_ErrorScenario() {
        BaseOptimizer<PointValuePair> optimizer = new FibonacciSearchOptimizer(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException("Function is not supported");
            }
        }, new SimpleBounds(-5, 5), GoalType.MINIMIZE, MaxEval.ITERATIONS, 100);
        optimizer.optimize();
    }
}