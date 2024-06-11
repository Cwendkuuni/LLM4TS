package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.convergence.SimpleConvergenceChecker;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.GoalType;

public class BaseOptimizerTest<PAIR extends PointValuePair<Double, Double>> {

    private BaseOptimizer<PAIR> optimizer;
    private UnivariateFunction testFunction;

    @Before
    public void setUp() {
        optimizer = new org.apache.commons.math3.optim.univariate.UnivariateFibonacciOptimizer(new SimpleBounds(0.0, 2.0), 10, GoalType.MAXIMIZE);
        testFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2.0) + Math.pow(Math.cos(x), 2.0);
            }

            @Override
            public double derivative(double x) {
                return 2.0 * (Math.sin(x) * Math.cos(x) - Math.cos(x) * Math.sin(x));
            }
        };
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(10, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        optimizer.optimize(testFunction, new MaxEval(10), new SimpleConvergenceChecker(1.0E-6, 1.0E-6));
        assertEquals(10, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        assertEquals(new SimpleConvergenceChecker(1.0E-6, 1.0E-6), optimizer.getConvergenceChecker());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedMethod() {
        optimizer.setMaxEvaluations(5);
        fail("Should throw UnsupportedOperationException");
    }

    @Test
    public void testOptimizeSuccess() {
        PAIR result = optimizer.optimize(testFunction, new MaxEval(1), new SimpleConvergenceChecker(1.0E-6, 1.0E-6));
        assertEquals(Math.PI, result.getPoint(), 1.0E-6);
        assertEquals(Math.PI * Math.PI, result.getValue(), 1.0E-6);
    }

    @Test(expected = IllegalStateException.class)
    public void testOptimizeFailure() {
        optimizer.optimize(testFunction, new MaxEval(0), new SimpleConvergenceChecker(1.0E-6, 1.0E-6));
        fail("Should throw IllegalStateException");
    }

}