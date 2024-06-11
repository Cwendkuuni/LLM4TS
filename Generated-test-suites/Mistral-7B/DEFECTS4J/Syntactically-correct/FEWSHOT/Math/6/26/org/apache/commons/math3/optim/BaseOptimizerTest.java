package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleValuesChecker;
import org.junit.Test;

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

        optimizer.optimize(new DummyObjectiveFunction(), new DummyInitialGuess(), new DummyConvergenceChecker());
        assertEquals(1, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new DummyBaseOptimizer<>();
        assertNull(optimizer.getConvergenceChecker());

        optimizer = new DummyBaseOptimizer<>(new SimpleValuesChecker(1e-6));
        assertSame(optimizer.getConvergenceChecker(), new SimpleValuesChecker(1e-6));
    }

    private static class DummyBaseOptimizer<PAIR> implements BaseOptimizer<PAIR> {
        private int maxEvaluations;
        private ConvergenceChecker<PAIR> convergenceChecker;

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
            return MaxEval.getNumberOfEvaluations();
        }

        @Override
        public ConvergenceChecker<PAIR> getConvergenceChecker() {
            return convergenceChecker;
        }
    }

    private static class DummyObjectiveFunction implements org.apache.commons.math3.optim.ObjectiveFunction<Double> {
        @Override
        public Double value(Double x) {
            return x * x;
        }

        @Override
        public Double value(org.apache.commons.math3.optim.PointValues pointValues) {
            return null;
        }

        @Override
        public void gradient(Double x, org.apache.commons.math3.optim.PointValues gradient, org.apache.commons.math3.optim.MaxEval maxEval) {
        }

        @Override
        public void gradient(org.apache.commons.math3.optim.PointValues pointValues) {
        }

        @Override
        public void value(org.apache.commons.math3.optim.PointValues pointValues, org.apache.commons.math3.optim.MaxEval maxEval) {
            pointValues.setValue(0, value(pointValues.getPoint().getComponent(0)));
        }
    }

    private static class DummyInitialGuess implements org.apache.commons.math3.optim.PointValuePair {
        @Override
        public org.apache.commons.math3.optim.Point getPoint() {
            return new org.apache.commons.math3.optim.PointValuePair.DoublePointValue(0.0);
        }

        @Override
        public double getValue() {
            return 0.0;
        }
    }

    private static class DummyConvergenceChecker implements ConvergenceChecker<PAIR> {
        private double tolerance;

        public DummyConvergenceChecker(double tolerance) {
            this.tolerance = tolerance;
        }

        @Override
        public boolean isConverged(PAIR pair) {
            return Math.abs(pair.getValue()) < tolerance;
        }
    }
}