package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEval;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for BaseOptimizer interface.
 */
public class BaseOptimizerTest {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = getDummyBaseOptimizer();
        int maxEvaluations = optimizer.getMaxEvaluations();
        Assert.assertNotNull(maxEvaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Object> optimizer = getDummyBaseOptimizer();
        optimizer.optimize(new DummyPoint(), new DummyObjectiveFunction());
        int evaluations = optimizer.getEvaluations();
        Assert.assertTrue(evaluations > 0);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = getDummyBaseOptimizer();
        ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
        Assert.assertNotNull(convergenceChecker);
    }

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()} with a negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetMaxEvaluationsNegativeValue() {
        BaseOptimizer<Object> optimizer = getDummyBaseOptimizer();
        optimizer.setMaxEvaluations(-1);
        int maxEvaluations = optimizer.getMaxEvaluations();
    }

    /**
     * Test method for {@link BaseOptimizer#optimize()} with a null point.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeNullPoint() {
        BaseOptimizer<Object> optimizer = getDummyBaseOptimizer();
        optimizer.optimize(null, new DummyObjectiveFunction());
    }

    /**
     * Test method for {@link BaseOptimizer#optimize()} with a null objective function.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeNullObjectiveFunction() {
        BaseOptimizer<Object> optimizer = getDummyBaseOptimizer();
        optimizer.optimize(new DummyPoint(), null);
    }

    /**
     * Test method for {@link BaseOptimizer#optimize()} with a convergence checker that always returns false.
     */
    @Test
    public void testOptimizeWithConvergenceCheckerThatAlwaysReturnsFalse() {
        BaseOptimizer<Object> optimizer = getDummyBaseOptimizer();
        optimizer.setConvergenceChecker(new DummyConvergenceCheckerThatAlwaysReturnsFalse());
        optimizer.optimize(new DummyPoint(), new DummyObjectiveFunction());
    }

    /**
     * Test method for {@link BaseOptimizer#optimize()} with a convergence checker that throws an exception.
     */
    @Test(expected = RuntimeException.class)
    public void testOptimizeWithConvergenceCheckerThatThrowsException() {
        BaseOptimizer<Object> optimizer = getDummyBaseOptimizer();
        optimizer.setConvergenceChecker(new DummyConvergenceCheckerThatThrowsException());
        optimizer.optimize(new DummyPoint(), new DummyObjectiveFunction());
    }

    /**
     * Test method for {@link BaseOptimizer#optimize()} with a MaxEval exceeded.
     */
    @Test
    public void testOptimizeWithMaxEvalExceeded() {
        BaseOptimizer<Object> optimizer = getDummyBaseOptimizer();
        optimizer.setMaxEvaluations(1);
        optimizer.optimize(new DummyPoint(), new DummyObjectiveFunction());
        Assert.assertEquals(1, optimizer.getEvaluations());
    }

    /**
     * Helper method to create a dummy BaseOptimizer implementation.
     *
     * @return a new instance of a dummy BaseOptimizer implementation
     */
    private BaseOptimizer<Object> getDummyBaseOptimizer() {
        return new DummyBaseOptimizer();
    }

    /**
     * Dummy implementation of {@link BaseOptimizer}.
     */
    private static class DummyBaseOptimizer implements BaseOptimizer<Object> {
        private int evaluations = 0;
        private ConvergenceChecker<Object> convergenceChecker;
        private int maxEvaluations;

        @Override
        public int getMaxEvaluations() {
            return maxEvaluations;
        }

        @Override
        public void setMaxEvaluations(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        @Override
        public int getEvaluations() {
            return evaluations;
        }

        @Override
        public ConvergenceChecker<Object> getConvergenceChecker() {
            return convergenceChecker;
        }

        @Override
        public void setConvergenceChecker(ConvergenceChecker<Object> convergenceChecker) {
            this.convergenceChecker = convergenceChecker;
        }

        @Override
        public Object optimize(Object start, Object objectiveFunction) {
            optimizeImpl(start, objectiveFunction);
            return null;
        }
    }

    /**
     * Dummy ConvergenceChecker that always returns false.
     */
    private static class DummyConvergenceCheckerThatAlwaysReturnsFalse implements ConvergenceChecker<Object> {
        @Override
        public boolean isConverged(Object currentValue, Object bestValue, Object objectiveFunctionValue) {
            return false;
        }
    }

    /**
     * Dummy ConvergenceChecker that throws an exception.
     */
    private static class DummyConvergenceCheckerThatThrowsException implements ConvergenceChecker<Object> {
        @Override
        public boolean isConverged(Object currentValue, Object bestValue, Object objectiveFunctionValue) {
            throw new RuntimeException("Convergence checker exception");
        }
    }

    /**
     * Dummy Point implementation.
     */
    private static class DummyPoint {
        // empty implementation
    }

    /**
     * Dummy ObjectiveFunction implementation.
     */
    private static class DummyObjectiveFunction implements ObjectiveFunction<Object> {
        @Override
        public Object value(Object point) {
            return null;
        }
    }
}