package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEval;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the BaseOptimizer interface.
 */
public class BaseOptimizerTest {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = getMockOptimizer();
        int maxEvaluations = optimizer.getMaxEvaluations();
        Assert.assertNotNull(maxEvaluations);
        Assert.assertEquals(-1, maxEvaluations); // default value is undefined
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Object> optimizer = getMockOptimizer();
        optimizer.optimize(new MaxEval(1));
        int evaluations = optimizer.getEvaluations();
        Assert.assertEquals(1, evaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = getMockOptimizer();
        ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
        Assert.assertNotNull(convergenceChecker);
    }

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()} with a defined value.
     */
    @Test
    public void testGetMaxEvaluationsDefined() {
        BaseOptimizer<Object> optimizer = getMockOptimizerWithMaxEvaluations(100);
        int maxEvaluations = optimizer.getMaxEvaluations();
        Assert.assertEquals(100, maxEvaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()} when no optimization has been performed.
     */
    @Test
    public void testGetEvaluationsBeforeOptimize() {
        BaseOptimizer<Object> optimizer = getMockOptimizer();
        int evaluations = optimizer.getEvaluations();
        Assert.assertEquals(0, evaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()} after multiple optimizations.
     */
    @Test
    public void testGetEvaluationsAfterMultipleOptimize() {
        BaseOptimizer<Object> optimizer = getMockOptimizer();
        optimizer.optimize(new MaxEval(1));
        optimizer.optimize(new MaxEval(1));
        optimizer.optimize(new MaxEval(1));
        int evaluations = optimizer.getEvaluations();
        Assert.assertEquals(3, evaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()} with a custom ConvergenceChecker.
     */
    @Test
    public void testGetConvergenceCheckerCustom() {
        BaseOptimizer<Object> optimizer = getMockOptimizerWithCustomConvergenceChecker();
        ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
        Assert.assertNotNull(convergenceChecker);
        Assert.assertTrue(convergenceChecker instanceof MyCustomConvergenceChecker);
    }

    private BaseOptimizer<Object> getMockOptimizer() {
        return new MockBaseOptimizer<>();
    }

    private BaseOptimizer<Object> getMockOptimizerWithMaxEvaluations(int maxEvaluations) {
        return new MockBaseOptimizerWithMaxEvaluations(maxEvaluations);
    }

    private BaseOptimizer<Object> getMockOptimizerWithCustomConvergenceChecker() {
        return new MockBaseOptimizerWithCustomConvergenceChecker();
    }

    /**
     * Mock implementation of BaseOptimizer with no specific behavior.
     */
    private static class MockBaseOptimizer<PAIR> implements BaseOptimizer<PAIR> {
        // Empty implementation
    }

    /**
     * Mock implementation of BaseOptimizer with a defined maximum number of evaluations.
     */
    private static class MockBaseOptimizerWithMaxEvaluations<PAIR> extends MockBaseOptimizer<PAIR> {
        private final int maxEvaluations;

        public MockBaseOptimizerWithMaxEvaluations(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        @Override
        public int getMaxEvaluations() {
            return maxEvaluations;
        }
    }

    /**
     * Mock implementation of BaseOptimizer with a custom ConvergenceChecker.
     */
    private static class MockBaseOptimizerWithCustomConvergenceChecker<PAIR> extends MockBaseOptimizer<PAIR> {
        // Empty implementation, but with a custom ConvergenceChecker
    }

    /**
     * Custom ConvergenceChecker for testing purposes.
     */
    private static class MyCustomConvergenceChecker<PAIR> implements ConvergenceChecker<PAIR> {
        // Empty implementation for testing
    }
}