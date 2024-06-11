package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValuesChecker;
import org.apache.commons.math3.optim.UnconvergedException;
import org.junit.Test;

public class BaseOptimizerTest<PAIR extends PointValuePair> {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new MockBaseOptimizer<>();
        assertEquals(0, optimizer.getMaxEvaluations());

        optimizer = new MaxEval(3);
        assertEquals(3, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = new MockBaseOptimizer<>();
        assertNull(optimizer.getEvaluations());

        optimizer.optimize(new MockObjectiveFunction(), new MockStartPoint());
        assertEquals(1, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new MockBaseOptimizer<>();
        assertNull(optimizer.getConvergenceChecker());

        optimizer = new MockBaseOptimizerWithChecker();
        assertEquals(new SimpleValuesChecker(1e-6), optimizer.getConvergenceChecker());
    }

    private static class MockBaseOptimizer<PAIR extends PointValuePair> implements BaseOptimizer<PAIR> {
        @Override
        public int getMaxEvaluations() {
            return 0;
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

    private static class MockBaseOptimizerWithChecker<PAIR extends PointValuePair> implements BaseOptimizer<PAIR> {
        private final ConvergenceChecker<PAIR> checker;

        public MockBaseOptimizerWithChecker() {
            checker = new SimpleValuesChecker(1e-6);
        }

        @Override
        public int getMaxEvaluations() {
            return 3;
        }

        @Override
        public int getEvaluations() {
            throw new UnconvergedException();
        }

        @Override
        public ConvergenceChecker<PAIR> getConvergenceChecker() {
            return checker;
        }
    }

    private static class MockObjectiveFunction implements org.apache.commons.math3.optim.ObjectiveFunction<Double> {
        @Override
        public Double value(Double x) {
            return x * x;
        }

        @Override
        public Double gradient(Double x) {
            return 2 * x;
        }
    }

    private static class MockStartPoint implements org.apache.commons.math3.optim.PointValuePair<Double> {
        private final Double point;
        private final Double value;

        public MockStartPoint(Double point, Double value) {
            this.point = point;
            this.value = value;
        }

        @Override
        public Double getPoint() {
            return point;
        }

        @Override
        public Double getValue() {
            return value;
        }
    }
}