package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEval;

import java.util.Random;

public class BaseOptimizerTest<PAIR> {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new DummyBaseOptimizer<>();
        assertEquals(0, optimizer.getMaxEvaluations());

        optimizer = new DummyBaseOptimizer<>(100);
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = new DummyBaseOptimizer<>();
        assertEquals(0, optimizer.getEvaluations());

        optimizer.optimize(new DummyObjectiveFunction(), new DummyPoint(), new DummyInitialGuess());
        assertEquals(1, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new DummyBaseOptimizer<>();
        assertNull(optimizer.getConvergenceChecker());

        ConvergenceChecker<PAIR> convergenceChecker = new MaxEvalConvergenceChecker(100);
        optimizer = new DummyBaseOptimizer<>(convergenceChecker);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    static class DummyBaseOptimizer<PAIR> implements BaseOptimizer<PAIR> {
        private ConvergenceChecker<PAIR> convergenceChecker;
        private int maxEvaluations;

        public DummyBaseOptimizer() {
            this.maxEvaluations = 0;
        }

        public DummyBaseOptimizer(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        public DummyBaseOptimizer(ConvergenceChecker<PAIR> convergenceChecker) {
            this.convergenceChecker = convergenceChecker;
        }

        @Override
        public int getMaxEvaluations() {
            return maxEvaluations;
        }

        @Override
        public int getEvaluations() {
            return maxEvaluations - maxEvaluations % 10; // for testing purposes
        }

        @Override
        public ConvergenceChecker<PAIR> getConvergenceChecker() {
            return convergenceChecker;
        }
    }

    static class DummyObjectiveFunction implements ObjectiveFunction<Double, Double> {
        @Override
        public Double value(Double point) {
            return new Random().nextDouble();
        }

        @Override
        public Double value(Double[] point) {
            return value(point[0]);
        }
    }

    static class DummyPoint implements Point<Double> {
        @Override
        public Double[] getCoordinates() {
            return new Double[]{0.0};
        }
    }

    static class DummyInitialGuess implements InitialGuess<Double> {
        @Override
        public Double getInitialGuess() {
            return 0.0;
        }
    }

    static class MaxEvalConvergenceChecker implements ConvergenceChecker<PAIR> {
        private final int maxEvaluations;

        public MaxEvalConvergenceChecker(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        @Override
        public boolean check(PAIR pointObjectivePair) {
            return getMaxEvaluations() >= maxEvaluations;
        }

        @Override
        public int getMaxIterations() {
            return maxEvaluations;
        }
    }
}