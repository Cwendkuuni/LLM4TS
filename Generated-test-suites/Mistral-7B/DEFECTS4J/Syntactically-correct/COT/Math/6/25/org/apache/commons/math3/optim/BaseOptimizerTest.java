package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.UnivariateRealFunction;
import org.apache.commons.math3.optimization.convergence.ConvergenceChecker;
import org.apache.commons.math3.optimization.convergence.ConvergenceCheckerImpl;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for BaseOptimizer interface.
 */
public class BaseOptimizerTest {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new NoOpOptimizer<>();
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertNotNull("Max evaluations should not be null", maxEvaluations);
        assertEquals("Max evaluations should be set by constructor", MaxEval.DEFAULT, maxEvaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new NoOpOptimizer<>();
        int evaluations = optimizer.getEvaluations();
        assertEquals("Evaluations should be 0 before optimization", 0, evaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new NoOpOptimizer<>();
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull("Convergence checker should not be null", convergenceChecker);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()} with custom convergence checker.
     */
    @Test
    public void testGetCustomConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new NoOpOptimizer<>();
        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceCheckerImpl();
        optimizer = new BaseOptimizer<PointValuePair>() {
            @Override
            public ConvergenceChecker<PAIR> getConvergenceChecker() {
                return customChecker;
            }
        };
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull("Custom convergence checker should not be null", convergenceChecker);
        assertEquals("Custom convergence checker should be the one set", customChecker, convergenceChecker);
    }

    /**
     * Test edge case for {@link BaseOptimizer#getMaxEvaluations()} with minimum value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetMaxEvaluationsMinValue() {
        BaseOptimizer<PointValuePair> optimizer = new NoOpOptimizer<>();
        optimizer.setMaxEvaluations(-1);
    }

    /**
     * Test edge case for {@link BaseOptimizer#getConvergenceChecker()} with null value.
     */
    @Test(expected = NullPointerException.class)
    public void testGetConvergenceCheckerNull() {
        BaseOptimizer<PointValuePair> optimizer = new NoOpOptimizer<>();
        optimizer.setConvergenceChecker(null);
    }

    /**
     * Test exception handling in {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMaxEvaluationsUnsupported() {
        AbstractBaseOptimizer<PointValuePair> optimizer = new AbstractBaseOptimizer<PointValuePair>() {};
        int maxEvaluations = optimizer.getMaxEvaluations();
    }

    /**
     * Test exception handling in {@link BaseOptimizer#getEvaluations()}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetEvaluationsUnsupported() {
        AbstractBaseOptimizer<PointValuePair> optimizer = new AbstractBaseBaseOptimizer<PointValuePair>() {};
        int evaluations = optimizer.getEvaluations();
    }

    /**
     * Test exception handling in {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetConvergenceCheckerUnsupported() {
        AbstractBaseOptimizer<PointValuePair> optimizer = new AbstractBaseOptimizer<PointValuePair>() {};
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
    }
}

class AbstractBaseBaseOptimizer<PAIR> extends BaseOptimizer<PAIR> {}
class NoOpOptimizer<PAIR> extends BaseOptimizer<PAIR> {
    @Override
    public int getMaxEvaluations() {
        return MaxEval.DEFAULT;
    }

    @Override
    public int getEvaluations() {
        return 0;
    }

    @Override
    public ConvergenceChecker<PAIR> getConvergenceChecker() {
        return new ConvergenceCheckerImpl();
    }
}