package org.apache.commons.math3.optimization;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleConvergenceChecker;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.noderiv.BrentMinimizer;
import org.apache.commons.math3.optim.univariate.noderiv.BrentMinimizer.BrentMinimizerConfig;
import org.junit.Test;

/**
 * Test class for the {@link BaseOptimizer} interface.
 *
 * @author Iulian Ştefan
 */
public class BaseOptimizerTest {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        MaxEval maxEvaluations = new MaxEval(100);
        BaseOptimizer<Object> optimizer = new BrentMinimizer(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.cos(x);
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x) * (Math.sin(x) - Math.cos(x));
            }
        }, maxEvaluations, new BrentMinimizerConfig());

        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(100, maxEvaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        MaxEval maxEvaluations = new MaxEval(100);
        BaseOptimizer<Object> optimizer = new BrentMinimizer(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.cos(x);
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x) * (Math.sin(x) - Math.cos(x));
            }
        }, maxEvaluations, new BrentMinimizerConfig());

        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);

        optimizer.optimize(0.0);

        evaluations = optimizer.getEvaluations();
        assertEquals(1, evaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @Test
    public void testGetConvergenceChecker() {
        MaxEval maxEvaluations = new MaxEval(100);
        BaseOptimizer<Object> optimizer = new BrentMinimizer(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.cos(x);
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x) * (Math.sin(x) - Math.cos(x));
            }
        }, maxEvaluations, new BrentMinimizerConfig());

        ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull(convergenceChecker);

        assertTrue(convergenceChecker instanceof SimpleConvergenceChecker);
    }
}