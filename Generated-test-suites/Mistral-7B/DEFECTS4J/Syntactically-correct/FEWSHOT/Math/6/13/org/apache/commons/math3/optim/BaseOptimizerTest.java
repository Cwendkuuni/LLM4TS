package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleConvergenceChecker;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.junit.Test;

/**
 * Test cases for {@link BaseOptimizer} interface.
 */
public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new DummyBaseOptimizer();
        assertEquals(0, optimizer.getMaxEvaluations());

        optimizer = new DummyBaseOptimizer(100);
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new DummyBaseOptimizer();
        assertEquals(0, optimizer.getEvaluations());

        optimizer.optimize(new DummyUnivariateFunction(), new DummyInitialGuess(), new DummyConvergenceChecker());
        assertEquals(1, optimizer.getEvaluations());

        optimizer.optimize(new DummyUnivariateFunction(), new DummyInitialGuess(), new DummyConvergenceChecker());
        assertEquals(2, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new DummyBaseOptimizer();
        assertNull(optimizer.getConvergenceChecker());

        optimizer = new DummyBaseOptimizer(new SimpleConvergenceChecker(MaxEval.CONVERGED));
        assertEquals(optimizer.getConvergenceChecker(), optimizer.getConvergenceChecker());
    }
}

/**
 * Dummy implementation of {@link BaseOptimizer}.
 */
class DummyBaseOptimizer<PAIR> implements BaseOptimizer<PAIR> {

    private int maxEvaluations = 0;
    private int evaluations = 0;
    private ConvergenceChecker<PAIR> convergenceChecker;

    public DummyBaseOptimizer() {
    }

    public DummyBaseOptimizer(int maxEvaluations) {
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

    public void optimize(UnivariateFunction function, double initialGuess, ConvergenceChecker<PAIR> checker) {
        evaluations++;
        convergenceChecker = checker;
    }
}

/**
 * Dummy implementation of {@link UnivariateFunction}.
 */
class DummyUnivariateFunction implements UnivariateFunction {

    @Override
    public double value(double x) {
        return x * x;
    }

    @Override
    public double derivative(double x) {
        return 2 * x;
    }
}

/**
 * Dummy implementation of {@link InitialGuess} and {@link ConvergenceChecker}.
 */
class DummyInitialGuess implements InitialGuess<Double> {

    @Override
    public Double getInitialGuess() {
        return 0.0;
    }
}

class DummyConvergenceChecker extends SimpleConvergenceChecker<PointValuePair> {

    public DummyConvergenceChecker(int maxIterations) {
        super(maxIterations);
    }
}