package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.initializer.AverageStartingPointInitializer;
import org.apache.commons.math3.optim.maximizer.BrentMaximizer;
import org.apache.commons.math3.optim.point.PointValuePairImpl;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link org.apache.commons.math3.optimization.BaseOptimizer}.
 *
 * @author Iulian Agapie (iulianagapie@gmail.com)
 */
public class BaseOptimizerTest<PAIR extends PointValuePair<PAIR, Number>> {

    private static final int MAX_EVALUATIONS = 10;

    /**
     * Test getMaxEvaluations method.
     */
    @Test
    public void testGetMaxEvaluations() {
        final BaseOptimizer<PAIR> optimizer = new BrentMaximizer<>(new UnivariateFunction() {
            @Override
            public Number value(Number point) {
                return point.doubleValue() * point.doubleValue();
            }

            @Override
            public Number derivative(Number point) {
                return 2 * point.doubleValue();
            }
        }, new AverageStartingPointInitializer<>(), new MaxEval(MAX_EVALUATIONS), new ConvergenceChecker<PAIR>() {
            @Override
            public boolean checkConvergence(PAIR current) {
                return current.getValue().doubleValue() > 4.0;
            }
        });
        assertEquals(MAX_EVALUATIONS, optimizer.getMaxEvaluations());
    }

    /**
     * Test getEvaluations method.
     */
    @Test
    public void testGetEvaluations() {
        final BaseOptimizer<PAIR> optimizer = new BrentMaximizer<>(new UnivariateFunction() {
            @Override
            public Number value(Number point) {
                return point.doubleValue() * point.doubleValue();
            }

            @Override
            public Number derivative(Number point) {
                return 2 * point.doubleValue();
            }
        }, new AverageStartingPointInitializer<>(), new MaxEval(MAX_EVALUATIONS), new ConvergenceChecker<PAIR>() {
            @Override
            public boolean checkConvergence(PAIR current) {
                return current.getValue().doubleValue() > 4.0;
            }
        });
        assertEquals(0, optimizer.getEvaluations());

        final PointValuePair<PAIR, Number> result = optimizer.optimize(new PointValuePairImpl<>(new Double(1.0), new Double(1.0)));
        assertEquals(1, optimizer.getEvaluations());
        assertEquals(result.getValue().doubleValue(), 1.0, 0.01);
    }

    /**
     * Test getConvergenceChecker method.
     */
    @Test
    public void testGetConvergenceChecker() {
        final BaseOptimizer<PAIR> optimizer = new BrentMaximizer<>(new UnivariateFunction() {
            @Override
            public Number value(Number point) {
                return point.doubleValue() * point.doubleValue();
            }

            @Override
            public Number derivative(Number point) {
                return 2 * point.doubleValue();
            }
        }, new AverageStartingPointInitializer<>(), new MaxEval(MAX_EVALUATIONS), new ConvergenceChecker<PAIR>() {
            @Override
            public boolean checkConvergence(PAIR current) {
                return current.getValue().doubleValue() > 4.0;
            }
        });
        final ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        Assert.assertNotNull(convergenceChecker);
        assertFalse(convergenceChecker.isConverged(new PointValuePairImpl<>(new Double(1.0), new Double(1.0))));
        assertTrue(convergenceChecker.isConverged(new PointValuePairImpl<>(new Double(5.0), new Double(25.0))));
    }
}