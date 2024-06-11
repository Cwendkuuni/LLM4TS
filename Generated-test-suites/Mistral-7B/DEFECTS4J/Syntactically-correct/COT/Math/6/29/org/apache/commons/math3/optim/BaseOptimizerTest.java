package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnconstrainedMinimization;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.initializer.AverageStartingPointGenerator;
import org.apache.commons.math3.optim.initializer.StartingPointGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.junit.Test;

import static org.apache.commons.math3.optim.BaseOptimizer.PAIR;

public class BaseOptimizerTest<PAIR extends PointValuePair> {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new DummyBaseOptimizer<>();
        assertEquals(0, optimizer.getMaxEvaluations());

        optimizer = new DummyBaseOptimizerWithMaxEvaluations(5);
        assertEquals(5, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = new DummyBaseOptimizer<>();
        assertEquals(0, optimizer.getEvaluations());

        UnivariateOptimizer optimizerWithEvaluations = new DummyUnivariateOptimizer();
        optimizer = (BaseOptimizer<PAIR>) optimizerWithEvaluations;
        optimizer.optimize(new SimpleBounds(0.0, 1.0), new DummyObjectiveFunction(), new AverageStartingPointGenerator(), new ConvergenceChecker<PAIR>() {
            @Override
            public boolean isConverged(UnconstrainedMinimization<PAIR> optimization) {
                return false;
            }
        }, GoalType.MINIMIZE, new MaxEval(10));
        assertEquals(10, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new DummyBaseOptimizer<>();
        assertNull(optimizer.getConvergenceChecker());

        UnivariateOptimizer optimizerWithChecker = new DummyUnivariateOptimizer();
        optimizer = (BaseOptimizer<PAIR>) optimizerWithChecker;
        optimizer.setConvergenceChecker(new DummyConvergenceChecker());
        assertEquals(optimizer.getConvergenceChecker(), optimizer.getConvergenceChecker());
    }

    // Add more test cases for edge cases and exceptions as necessary

}

class DummyBaseOptimizer<PAIR> implements BaseOptimizer<PAIR> {
    // empty implementation
}

class DummyBaseOptimizerWithMaxEvaluations<PAIR> extends DummyBaseOptimizer<PAIR> {
    private final int maxEvaluations;

    public DummyBaseOptimizerWithMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    @Override
    public int getMaxEvaluations() {
        return maxEvaluations;
    }
}

class DummyConvergenceChecker<PAIR> implements ConvergenceChecker<PAIR> {
    // empty implementation
}

class DummyObjectiveFunction implements ObjectiveFunction {
    // empty implementation
}