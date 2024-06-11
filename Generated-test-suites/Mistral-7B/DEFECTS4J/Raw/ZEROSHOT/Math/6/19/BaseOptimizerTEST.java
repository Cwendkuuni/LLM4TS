package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleConvergenceChecker;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for {@link BaseOptimizer}.
 *
 * @author <a href="mailto:johan.boodts@gmail.com">Johan Boodts</a>
 */
public class BaseOptimizerTest<PAIR extends PointValuePair<Point, Number>> {

    /** The optimizer under test. */
    private BaseOptimizer<PAIR> optimizer;

    /** The convergence checker under test. */
    private ConvergenceChecker<PAIR> convergenceChecker;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        optimizer = new TestOptimizer();
        convergenceChecker = new SimpleConvergenceChecker(1e-6);
        optimizer.setConvergenceChecker(convergenceChecker);
    }

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals("Maximum number of function evaluations should be set by default", MaxEval.DEFAULT, maxEvaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        int evaluations = optimizer.getEvaluations();
        assertNull("Evaluations should be zero by default", evaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        assertNull("Convergence checker should be null by default", convergenceChecker);
    }

    /**
     * Test class for {@link BaseOptimizer}.
     */
    public static class TestOptimizer implements BaseOptimizer<PAIR> {

        /** The number of evaluations. */
        private int evaluations;

        @Override
        public int getMaxEvaluations() {
            return MaxEval.DEFAULT;
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
        public PAIR optimize(UnivariateFunction f) {
            evaluations++;
            return new PointValuePair(new Double[1], new Double(0.0));
        }

        @Override
        public void setConvergenceChecker(ConvergenceChecker<PAIR> convergenceChecker) {
            this.convergenceChecker = convergenceChecker;
        }

        private ConvergenceChecker<PAIR> convergenceChecker;
    }
}