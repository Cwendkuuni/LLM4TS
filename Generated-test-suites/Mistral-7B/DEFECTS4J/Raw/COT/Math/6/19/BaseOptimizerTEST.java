package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.convergence.ConvergenceCriteria;
import org.apache.commons.math3.optim.MaxEval;

/**
 * Test class for the BaseOptimizer interface.
 */
@Deprecated
public class BaseOptimizerTest {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new DummyBaseOptimizer();
        int maxEvaluations = optimizer.getMaxEvaluations();
        int expectedMaxEvaluations = 100;
        assertEquals("Incorrect maximal number of function evaluations.", expectedMaxEvaluations, maxEvaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new DummyBaseOptimizer();
        int evaluations = optimizer.getEvaluations();
        int expectedEvaluations = 0;
        assertEquals("Incorrect number of function evaluations.", expectedEvaluations, evaluations);

        optimizer = new DummyBaseOptimizer();
        optimizer.optimize(new AroundMinimumInitializer<>(1.0), (x) -> 0.0, new ConvergenceCriteria(1e-6));
        evaluations = optimizer.getEvaluations();
        expectedEvaluations = 10; // Assuming 10 evaluations in the optimize method
        assertEquals("Incorrect number of function evaluations.", expectedEvaluations, evaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new DummyBaseOptimizer();
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        assertTrue("Convergence checker is null.", convergenceChecker != null);
    }

    /**
     * Test case for maximum number of function evaluations limit.
     */
    @Test(expected = MaxEval.CountReachedException.class)
    public void testMaxEvaluationsLimit() {
        BaseOptimizer<PointValuePair> optimizer = new DummyBaseOptimizer();
        MaxEval maxEvaluations = new MaxEval(5);
        optimizer = new DummyBaseOptimizer(maxEvaluations);
        ConvergenceCriteria convergenceCriteria = new ConvergenceCriteria(1e-6);
        optimizer.optimize(new AroundMinimumInitializer<>(1.0), (x) -> Math.sin(x), convergenceCriteria);
    }

    /**
     * Dummy implementation of BaseOptimizer.
     */
    private static class DummyBaseOptimizer implements BaseOptimizer<PointValuePair> {

        private int maxEvaluations;

        public DummyBaseOptimizer() {
            this.maxEvaluations = 100;
        }

        public DummyBaseOptimizer(MaxEval maxEvaluations) {
            this.maxEvaluations = maxEvaluations.getMaxEvaluations();
        }

        @Override
        public int getMaxEvaluations() {
            return this.maxEvaluations;
        }

        @Override
        public int getEvaluations() {
            return 0;
        }

        @Override
        public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
            return new ConvergenceChecker<PointValuePair>() {
                @Override
                public boolean isConverged(PointValuePair pointValuePair) {
                    return false;
                }
            };
        }

        @Override
        public PointValuePair optimize(InitialVector initialVector, FunctionEvaluator<PointValuePair> function, ConvergenceChecker<PointValuePair> convergenceChecker) {
            return null;
        }
    }
}