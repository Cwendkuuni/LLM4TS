package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEvalException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new DummyOptimizer<>();
        int maxEvaluations = optimizer.getMaxEvaluations();
        int evaluations = getEvaluations(optimizer);

        assertEquals("Maximum number of function evaluations should be set", maxEvaluations, evaluations);
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new DummyOptimizer<>();
        int evaluations = optimizer.getEvaluations();

        assertEquals("Number of evaluations should be zero by default", 0, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new DummyOptimizer<>();
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();

        assertNotNull("Convergence checker should not be null", convergenceChecker);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetConvergenceCheckerUnsupported() {
        BaseOptimizer<Object> optimizer = new DummyOptimizer<>();
        optimizer.getConvergenceChecker();
    }

    @Test
    public void testOptimize() throws Exception {
        BaseOptimizer<PointValuePair> optimizer = new DummyOptimizer<>();
        ConvergenceChecker<PointValuePair> convergenceChecker = new DummyConvergenceChecker<>();
        optimizer.setConvergenceChecker(convergenceChecker);
        optimizer.setMaxEvaluations(10);
        PointValuePair result = optimizer.optimize(new DummyObjectiveFunction(), new PointValuePair(0.0, 0.0));
        int evaluations = getEvaluations(optimizer);

        assertNotNull("Optimization result should not be null", result);
        assertEquals("Number of function evaluations should match the max", 10, evaluations);
    }

    @Test(expected = MaxEvalException.class)
    public void testOptimizeMaxEval() throws Exception {
        BaseOptimizer<PointValuePair> optimizer = new DummyOptimizer<>();
        ConvergenceChecker<PointValuePair> convergenceChecker = new DummyConvergenceChecker<>();
        optimizer.setConvergenceChecker(convergenceChecker);
        optimizer.setMaxEvaluations(1);
        optimizer.optimize(new DummyObjectiveFunction(), new PointValuePair(0.0, 0.0));
    }

    private int getEvaluations(BaseOptimizer<PointValuePair> optimizer) {
        return optimizer.getEvaluations();
    }

    private static class DummyObjectiveFunction implements ObjectiveFunction<PointValuePair> {
        @Override
        public PointValuePair value(PointValuePair point) {
            return new PointValuePair(point.getPoint()[0], point.getPoint()[0] * point.getPoint()[0]);
        }
    }

    private static class DummyOptimizer<PAIR> implements BaseOptimizer<PAIR> {
        private int evaluations = 0;
        private ConvergenceChecker<PAIR> convergenceChecker;
        private int maxEvaluations;

        public void setConvergenceChecker(ConvergenceChecker<PAIR> convergenceChecker) {
            this.convergenceChecker = convergenceChecker;
        }

        public void setMaxEvaluations(int maxEvaluations) {
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
        public PAIR optimize(ObjectiveFunction<PAIR> objectiveFunction, PAIR initialGuess) throws Exception {
            for (int i = 0; i < maxEvaluations; i++) {
                PointValuePair pointValuePair = objectiveFunction.value(initialGuess);
                evaluations++;
            }
            return initialGuess;
        }
    }

    private static class DummyConvergenceChecker<PAIR> implements ConvergenceChecker<PAIR> {
    }
}