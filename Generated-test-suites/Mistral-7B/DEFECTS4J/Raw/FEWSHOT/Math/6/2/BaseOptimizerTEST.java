package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePair;
import org.junit.Test;

/**
 * Test cases for the BaseOptimizer interface.
 */
public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new DummyBaseOptimizer(10);
        assertEquals(10, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new DummyBaseOptimizer(10);
        optimizer.optimize(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public PointValuePair gradient(double x) {
                return new PointValuePair(x, 2 * x);
            }
        }, new SimpleBounds(0, 1), MaxEval.UNLIMITED, GoalType.MINIMIZE);
        assertEquals(1, optimizer.getEvaluations());

        optimizer.optimize(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public PointValuePair gradient(double x) {
                return new PointValuePair(x, 2 * x);
            }
        }, new SimpleBounds(0, 1), MaxEval.UNLIMITED, GoalType.MINIMIZE);
        assertEquals(2, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new DummyBaseOptimizer(10);
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Dummy implementation of BaseOptimizer for testing.
     */
    private static class DummyBaseOptimizer<PAIR> implements BaseOptimizer<PAIR> {

        private final int maxEvaluations;

        public DummyBaseOptimizer(int maxEvaluations) {
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

        @Override
        public PointValuePair optimize(ObjectiveFunction f, SimpleBounds xlower, SimpleBounds xupper, MaxEval maxEvals, GoalType goal) {
            return new PointValuePair(0, 0);
        }
    }
}