package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceCheckerFactory;
import org.apache.commons.math3.optim.nonlinear.convergence.StoppingCondition;
import org.junit.Test;

/**
 * Test cases for BaseOptimizer interface.
 */
public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new DummyOptimizer();
        assertEquals(0, optimizer.getMaxEvaluations());
        optimizer = new DummyOptimizer(250);
        assertEquals(250, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new DummyOptimizer();
        assertEquals(0, optimizer.getEvaluations());
        optimizer.optimize(new DummyFunction(), new SimpleBounds(-1, 1), new MaxEval(10));
        assertEquals(10, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new DummyOptimizer();
        assertNull(optimizer.getConvergenceChecker());
        optimizer = new DummyOptimizer(new ConvergenceCheckerFactory() {
            @Override
            public ConvergenceChecker<PointValuePair> createChecker() {
                return new ConvergenceChecker<PointValuePair>() {
                    @Override
                    public boolean isConverged(PointValuePair currentValue, PointValuePair previousValue, int iteration) {
                        return currentValue.getValue() < 0.001;
                    }
                };
            }
        });
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        ConvergenceChecker<PointValuePair> expectedChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean isConverged(PointValuePair currentValue, PointValuePair previousValue, int iteration) {
                return currentValue.getValue() < 0.001;
            }
        };
        assertEquals(expectedChecker, convergenceChecker);
    }
}

class DummyFunction implements UnivariateFunction {
    @Override
    public double value(double x) {
        return Math.pow(x, 2);
    }
}

class DummyOptimizer<PAIR> implements BaseOptimizer<PAIR> {
    private int maxEvaluations = 0;

    public DummyOptimizer() {
        this(0);
    }

    public DummyOptimizer(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    @Override
    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    @Override
    public int getEvaluations() {
        return 0;
    }

    @Override
    public ConvergenceChecker<PAIR> getConvergenceChecker() {
        return null;
    }
}