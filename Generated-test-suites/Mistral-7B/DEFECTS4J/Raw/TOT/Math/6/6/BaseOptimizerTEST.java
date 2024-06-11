package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.convergers.Fibonacci;
import org.apache.commons.math3.optim.univariate.convergers.SqrtBiHalving;
import org.junit.Test;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

/**
 * Test class for {@link BaseOptimizer} interface.
 */
public class BaseOptimizerTest<PAIR extends PointValuePair<Double, Double>> {

    private final UnivariateFunction function = f -> Math.pow(f, 2);
    private final BaseOptimizer<PAIR> optimizer = new Fibonacci(new BrentOptimizer(function), new SimpleBounds(1.0, 10.0), MaxEval.REACH_THE_MINIMUM);

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        assertEquals(MaxEval.REACH_THE_MINIMUM.getMaxIterations(), optimizer.getMaxEvaluations());
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        int initialEvaluations = optimizer.getEvaluations();
        optimizer.optimize();
        int finalEvaluations = optimizer.getEvaluations();
        assertEquals(initialEvaluations + 1, finalEvaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        assertFalse(convergenceChecker instanceof SqrtBiHalving); // BrentOptimizer uses Fibonacci by default
        ConvergenceChecker<PAIR> sqrtBiHalvingChecker = new SqrtBiHalving(function, new SimpleBounds(1.0, 10.0), MaxEval.REACH_THE_MINIMUM);
        optimizer = new BrentOptimizer(function, new SimpleBounds(1.0, 10.0), MaxEval.REACH_THE_MINIMUM, sqrtBiHalvingChecker);
        convergenceChecker = optimizer.getConvergenceChecker();
        assertTrue(convergenceChecker instanceof SqrtBiHalving);
    }

    /**
     * Test optimize method with a simple univariate function.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        double solution = optimizer.optimize().getValue();
        assertEquals(Math.sqrt(2.0), solution, 0.01);
    }

    /**
     * Test optimize method with an invalid univariate function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeInvalidFunction() {
        BaseOptimizer<PAIR> invalidOptimizer = new BrentOptimizer(null, new SimpleBounds(1.0, 10.0), MaxEval.REACH_THE_MINIMUM);
        invalidOptimizer.optimize();
    }
}