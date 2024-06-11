package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnconstrainedMinimizationProblem;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for BaseOptimizer interface.
 *
 * @author <a href="mailto:kvisco@apache.org">Kevin Slemon</a>
 */
public class BaseOptimizerTest {
    private ConvergenceChecker<Object> convergenceChecker;
    private BaseOptimizer<PointValuePair> optimizer;

    @Before
    public void setUp() {
        convergenceChecker = mock(ConvergenceChecker.class);
        optimizer = new BaseMockOptimizer<>(convergenceChecker);
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(MaxEval.UNLIMITED, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    static class BaseMockOptimizer<PAIR> implements BaseOptimizer<PAIR> {
        private final ConvergenceChecker<PAIR> convergenceChecker;

        public BaseMockOptimizer(ConvergenceChecker<PAIR> convergenceChecker) {
            this.convergenceChecker = convergenceChecker;
        }

        @Override
        public int getMaxEvaluations() {
            return MaxEval.UNLIMITED;
        }

        @Override
        public int getEvaluations() {
            return 0;
        }

        @Override
        public ConvergenceChecker<PAIR> getConvergenceChecker() {
            return convergenceChecker;
        }

        @Override
        public PointValuePair optimize(UnconstrainedMinimizationProblem<PAIR> problem) {
            throw new UnsupportedOperationException("This is a mock optimizer.");
        }

        @Override
        public PointValuePair optimize(UnconstrainedMinimizationProblem<PAIR> problem, double stepMax) {
            throw new UnsupportedOperationException("This is a mock optimizer.");
        }

        @Override
        public PointValuePair optimize(UnconstrainedMinimizationProblem<PAIR> problem, double stepSize) {
            throw new UnsupportedOperationException("This is a mock optimizer.");
        }

        @Override
        public PointValuePair optimize(UnconstrainedMinimizationProblem<PAIR> problem, double stepSize,
                SimpleValueChecker<? super PAIR> valueChecker) {
            throw new UnsupportedOperationException("This is a mock optimizer.");
        }

        @Override
        public PointValuePair optimize(UnconstrainedMinimizationProblem<PAIR> problem,
                SimpleValueChecker<? super PAIR> valueChecker, double stepMax) {
            throw new UnsupportedOperationException("This is a mock optimizer.");
        }

        @Override
        public PointValuePair optimize(UnconstrainedMinimizationProblem<PAIR> problem,
                SimpleValueChecker<? super PAIR> valueChecker, double stepSize) {
            throw new UnsupportedOperationException("This is a mock optimizer.");
        }

        @Override
        public PointValuePair optimize(UnconstrainedMinimizationProblem<PAIR> problem,
                SimpleValueChecker<? super PAIR> valueChecker, double stepSize, double stepMax) {
            throw new UnsupportedOperationException("This is a mock optimizer.");
        }
    }
}