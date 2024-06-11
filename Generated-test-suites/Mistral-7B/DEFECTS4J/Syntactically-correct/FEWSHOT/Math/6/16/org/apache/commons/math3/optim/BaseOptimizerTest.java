package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.junit.Test;

/**
 * Test cases for BaseOptimizer interface.
 */
public class BaseOptimizerTest<PAIR extends PointValuePair<Double, Double>> {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new DummyBaseOptimizer<>();
        assertEquals(0, optimizer.getMaxEvaluations());

        optimizer = new DummyBaseOptimizer<>(50);
        assertEquals(50, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = new DummyBaseOptimizer<>();
        assertEquals(0, optimizer.getEvaluations());

        optimizer.optimize(new DummyObjectiveFunction(), new DummyStartPoint(), new MaxEval(10));
        assertEquals(10, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new DummyBaseOptimizer<>();
        assertNull(optimizer.getConvergenceChecker());

        ConvergenceChecker<PAIR> convergenceChecker = new SimpleValueChecker(1e-6);
        optimizer = new DummyBaseOptimizer<>(convergenceChecker);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    private static class DummyBaseOptimizer<PAIR extends PointValuePair<Double, Double>> implements BaseOptimizer<PAIR> {
        private ConvergenceChecker<PAIR> convergenceChecker;
        private int maxEvaluations;

        public DummyBaseOptimizer() {
            this(null);
        }

        public DummyBaseOptimizer(ConvergenceChecker<PAIR> convergenceChecker) {
            this(convergenceChecker, 0);
        }

        public DummyBaseOptimizer(int maxEvaluations) {
            this(null, maxEvaluations);
        }

        public DummyBaseOptimizer(ConvergenceChecker<PAIR> convergenceChecker, int maxEvaluations) {
            this.convergenceChecker = convergenceChecker;
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
            return convergenceChecker;
        }
    }

    private static class DummyObjectiveFunction implements ObjectiveFunction<Double> {
        @Override
        public Double value(Double point) {
            return null;
        }
    }

    private static class DummyStartPoint implements PointValuePair<Double, Double> {
        @Override
        public Double getPoint() {
            return null;
        }

        @Override
        public Double getValue() {
            return null;
        }
    }
}