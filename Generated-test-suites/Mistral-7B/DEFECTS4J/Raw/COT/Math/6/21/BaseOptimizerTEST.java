package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for BaseOptimizer interface
 */
public class BaseOptimizerTest {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new StubBaseOptimizer(10);
        assertEquals(10, optimizer.getMaxEvaluations());
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new StubBaseOptimizer(10);
        optimizer.optimize(new PointValuePair(0.0, 0.0), new MaxEval(5));
        assertEquals(5, optimizer.getEvaluations());
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new StubBaseOptimizer(10);
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull(convergenceChecker);
    }

    /**
     * Test case for getMaxEvaluations() with a BaseOptimizer instance that does not have a max evaluation limit
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMaxEvaluationsUnsupported() {
        BaseOptimizer<PointValuePair> optimizer = new UnsupportedBaseOptimizer();
        optimizer.getMaxEvaluations();
    }

    /**
     * Test case for getEvaluations() with a BaseOptimizer instance that has never been called before
     */
    @Test
    public void testGetEvaluationsBeforeCall() {
        BaseOptimizer<PointValuePair> optimizer = new StubBaseOptimizer(10);
        assertEquals(0, optimizer.getEvaluations());
    }

    /**
     * Test case for getEvaluations() with a BaseOptimizer instance that has been called with an empty convergence checker
     */
    @Test
    public void testGetEvaluationsEmptyConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new StubBaseOptimizer(10);
        ConvergenceChecker<PointValuePair> convergenceChecker = new EmptyConvergenceChecker<>();
        optimizer.setConvergenceChecker(convergenceChecker);
        optimizer.optimize(new PointValuePair(0.0, 0.0), new MaxEval(5));
        assertEquals(5, optimizer.getEvaluations());
    }

    /**
     * Test case for getEvaluations() with a BaseOptimizer instance that has been called multiple times
     */
    @Test
    public void testGetEvaluationsMultipleCalls() {
        BaseOptimizer<PointValuePair> optimizer = new StubBaseOptimizer(10);
        ConvergenceChecker<PointValuePair> convergenceChecker = new EmptyConvergenceChecker<>();
        optimizer.setConvergenceChecker(convergenceChecker);
        optimizer.optimize(new PointValuePair(0.0, 0.0), new MaxEval(5));
        optimizer.optimize(new PointValuePair(1.0, 1.0), new MaxEval(3));
        optimizer.optimize(new PointValuePair(2.0, 2.0), new MaxEval(7));
        assertEquals(15, optimizer.getEvaluations());
    }

    /**
     * Test case for getConvergenceChecker() with a BaseOptimizer instance that does not have a convergence checker set
     */
    @Test
    public void testGetConvergenceCheckerUnset() {
        BaseOptimizer<PointValuePair> optimizer = new StubBaseOptimizer(10);
        assertNull(optimizer.getConvergenceChecker());
    }
}

class StubBaseOptimizer<PAIR> implements BaseOptimizer<PAIR> {
    private int maxEvaluations;
    private int evaluations;
    private ConvergenceChecker<PAIR> convergenceChecker;

    public StubBaseOptimizer(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    @Override
    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    @Override
    public int getEvaluations() {
        return evaluations;
    }

    @Override
    public ConvergenceChecker<PAIR> getConvergenceChecker() {
        return convergenceChecker;
    }

    @Override
    public void setConvergenceChecker(ConvergenceChecker<PAIR> convergenceChecker) {
        this.convergenceChecker = convergenceChecker;
    }

    @Override
    public PAIR optimize(PAIR initialGuess, MaxEval maxEvaluations) {
        this.evaluations += maxEvaluations.getMaxEvaluations();
        return new PointValuePair(0.0, 0.0);
    }
}

class UnsupportedBaseOptimizer implements BaseOptimizer<PointValuePair> {
    @Override
    public int getMaxEvaluations() {
        throw new UnsupportedOperationException("getMaxEvaluations is not supported");
    }

    @Override
    public int getEvaluations() {
        throw new UnsupportedOperationException("getEvaluations is not supported");
    }

    @Override
    public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
        throw new UnsupportedOperationException("getConvergenceChecker is not supported");
    }

    @Override
    public void setConvergenceChecker(ConvergenceChecker<PointValuePair> convergenceChecker) {
        throw new UnsupportedOperationException("setConvergenceChecker is not supported");
    }

    @Override
    public PointValuePair optimize(PointValuePair initialGuess, MaxEval maxEvaluations) {
        throw new UnsupportedOperationException("optimize is not supported");
    }
}

class EmptyConvergenceChecker<PAIR> implements ConvergenceChecker<PAIR> {
    @Override
    public boolean isConverged(PAIR currentValue, PAIR previousValue) {
        return false;
    }
}