package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEval;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the BaseOptimizer interface.
 */
public class BaseOptimizerTest {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer();
        int maxEvaluations = optimizer.getMaxEvaluations();
        Assert.assertNotNull(maxEvaluations);
        Assert.assertTrue(maxEvaluations > 0);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer();
        int evaluations = optimizer.getEvaluations();
        Assert.assertEquals(0, evaluations);

        optimizer.optimize(new Object[]{}, new Object[]{});
        evaluations = optimizer.getEvaluations();
        Assert.assertNotEquals(0, evaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer();
        ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
        Assert.assertNotNull(convergenceChecker);
    }

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()} with negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxEvaluations() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer();
        optimizer.setMaxEvaluations(-1);
    }

    /**
     * Test method for {@link BaseOptimizer#optimize()} with null arguments.
     */
    @Test(expected = NullPointerException.class)
    public void testNullArguments() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer();
        optimizer.optimize(null, null);
    }

    /**
     * Test method for {@link BaseOptimizer#optimize()} with null point array.
     */
    @Test(expected = NullPointerException.class)
    public void testNullPointArray() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer();
        optimizer.optimize(null, new Object[]{});
    }

    /**
     * Test method for {@link BaseOptimizer#optimize()} with null objective array.
     */
    @Test(expected = NullPointerException.class)
    public void testNullObjectiveArray() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer();
        optimizer.optimize(new Object[]{}, null);
    }

    /**
     * Test method for {@link BaseOptimizer#optimize()} with illegal number of arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalNumberOfArguments() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer();
        optimizer.optimize(new Object[]{new Object[]{}});
    }

    /**
     * Test method for {@link BaseOptimizer#optimize()} with no specified convergence checker.
     */
    @Test
    public void testNoSpecifiedConvergenceChecker() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer();
        optimizer.optimize(new Object[]{}, new Object[]{});
        ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
        Assert.assertNotNull(convergenceChecker);
    }

    /**
     * Test method for {@link BaseOptimizer#optimize()} with specified convergence checker.
     */
    @Test
    public void testSpecifiedConvergenceChecker() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer();
        ConvergenceChecker<Object> customConvergenceChecker = new MaxEval(5);
        optimizer.setConvergenceChecker(customConvergenceChecker);
        optimizer.optimize(new Object[]{}, new Object[]{});
        ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
        Assert.assertEquals(customConvergenceChecker, convergenceChecker);
    }
}

class DummyBaseOptimizer implements BaseOptimizer<Object> {

    private int maxEvaluations;
    private ConvergenceChecker<Object> convergenceChecker;

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
        return 0;
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
    public PAIR optimize(PAIR[] startingPoints, PAIR[] objectiveValues) {
        throw new UnsupportedOperationException();
    }
}