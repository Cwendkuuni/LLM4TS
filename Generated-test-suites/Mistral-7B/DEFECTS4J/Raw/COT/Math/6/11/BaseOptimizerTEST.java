package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.univariate.ConvergenceChecker;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.junit.Test;

import java.util.function.Function;

/**
 * Test file for BaseOptimizer interface.
 */
public class BaseOptimizerTest {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new DummyBaseOptimizer();
        assertEquals(MaxEval.UNLIMITED, optimizer.getMaxEvaluations());

        optimizer = new LimitedBaseOptimizer(10);
        assertEquals(10, optimizer.getMaxEvaluations());
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new DummyBaseOptimizer();
        assertNull(optimizer.getEvaluations());

        optimizer = new OptimizingBaseOptimizer(() -> 0, new SimpleValueChecker());
        optimizer.optimize(new DummyFunction(), new DummyInitialGuess());
        assertEquals(1, optimizer.getEvaluations());
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new BaseOptimizer<PointValuePair>() {
            @Override
            public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
                return null;
            }
        };
        assertNull(optimizer.getConvergenceChecker());

        optimizer = new OptimizingBaseOptimizer(() -> 0, new SimpleValueChecker());
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull(convergenceChecker);
        assertEquals(UnivariateOptimizer.DEFAULT_CONVERGENCE_CHECKER, convergenceChecker);
    }

    /**
     * Dummy BaseOptimizer implementation for testing.
     */
    private static class DummyBaseOptimizer implements BaseOptimizer<PointValuePair> {
        // Empty implementation
    }

    /**
     * Limited BaseOptimizer implementation for testing.
     */
    private static class LimitedBaseOptimizer extends DummyBaseOptimizer implements BaseOptimizer<PointValuePair> {
        private final int maxEvaluations;

        public LimitedBaseOptimizer(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        @Override
        public int getMaxEvaluations() {
            return maxEvaluations;
        }
    }

    /**
     * Optimizing BaseOptimizer implementation for testing.
     */
    private static class OptimizingBaseOptimizer<PAIR> implements BaseOptimizer<PAIR> {
        private final Function<Double, PAIR> objectiveFunction;
        private final ConvergenceChecker<PAIR> convergenceChecker;

        public OptimizingBaseOptimizer(Function<Double, PAIR> objectiveFunction, ConvergenceChecker<PAIR> convergenceChecker) {
            this.objectiveFunction = objectiveFunction;
            this.convergenceChecker = convergenceChecker;
        }

        @Override
        public PAIR optimize(Function<Double, PAIR> objectiveFunction, Double initialGuess) {
            // Optimization logic here
            return new PointValuePair(initialGuess, objectiveFunction.apply(initialGuess));
        }

        @Override
        public ConvergenceChecker<PAIR> getConvergenceChecker() {
            return convergenceChecker;
        }
    }

    /**
     * Dummy Function implementation for testing.
     */
    private static class DummyFunction implements Function<Double, PointValuePair> {
        @Override
        public PointValuePair apply(Double input) {
            return new PointValuePair(input, Math.pow(input, 2));
        }
    }

    /**
     * Dummy InitialGuess implementation for testing.
     */
    private static class DummyInitialGuess implements Double {
        @Override
        public double doubleValue() {
            return 0.0;
        }
    }
}