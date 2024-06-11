package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.initializer.AverageInitializer;
import org.apache.commons.math3.optim.initializer.GreedyInitializer;
import org.apache.commons.math3.optim.initializer.RandomInitializer;
import org.apache.commons.math3.optim.simple.SimpleConvergenceChecker;
import org.apache.commons.math3.optim.simple.SimpleLineSearch;
import org.apache.commons.math3.optim.univariate.BrentMinimizer;
import org.junit.Test;

/**
 * Test cases for the BaseOptimizer interface.
 */
public class BaseOptimizerTest<PAIR extends PointValuePair<Double, Double>> {

    /**
     * Test for getMaxEvaluations() method.
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new BrentMinimizer(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        }, new SimpleBounds(0.0, 10.0), new SimpleConvergenceChecker(), new SimpleLineSearch(), MaxEval.DEFAULT);
        assertEquals(MaxEval.DEFAULT, optimizer.getMaxEvaluations());
    }

    /**
     * Test for getEvaluations() method.
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = new BrentMinimizer(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        }, new SimpleBounds(0.0, 10.0), new SimpleConvergenceChecker(), new SimpleLineSearch(), MaxEval.DEFAULT);
        optimizer.optimize(new AverageInitializer(), null);
        assertEquals(1, optimizer.getEvaluations());

        optimizer.optimize(new GreedyInitializer(), null);
        assertEquals(2, optimizer.getEvaluations());

        optimizer.optimize(new RandomInitializer(), null);
        assertEquals(3, optimizer.getEvaluations());
    }

    /**
     * Test for getConvergenceChecker() method.
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new BrentMinimizer(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        }, new SimpleBounds(0.0, 10.0), new SimpleConvergenceChecker(), new SimpleLineSearch(), MaxEval.DEFAULT);
        assertSame(new SimpleConvergenceChecker(), optimizer.getConvergenceChecker());
    }

    /**
     * Test for getConvergenceChecker() with a custom ConvergenceChecker.
     */
    @Test
    public void testGetConvergenceCheckerCustom() {
        BaseOptimizer<PAIR> optimizer = new BrentMinimizer(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        }, new SimpleBounds(0.0, 10.0), new CustomConvergenceChecker(), new SimpleLineSearch(), MaxEval.DEFAULT);
        assertSame(new CustomConvergenceChecker(), optimizer.getConvergenceChecker());
    }

    /**
     * Custom ConvergenceChecker for testing purposes.
     */
    private static class CustomConvergenceChecker extends SimpleConvergenceChecker {
    }
}