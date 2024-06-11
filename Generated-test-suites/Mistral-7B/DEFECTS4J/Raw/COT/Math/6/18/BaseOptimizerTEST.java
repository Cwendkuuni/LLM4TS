package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.StoppingCondition;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.convergence.AbstractConvergenceChecker;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.maximization.Maximizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer.Finding;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizerException;
import org.apache.commons.math3.optim.univariate.valuefunction.GoalTypeValueFunction;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test class for {@link BaseOptimizer} interface.
 *
 * @param <PAIR> Type of the point/objective pair.
 */
public class BaseOptimizerTest<PAIR extends PointValuePair<Number, Number>> {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new BrentOptimizer(new GoalTypeValueFunction(GoalType.MAXIMIZE, new SimpleBounds(0.0, 1.0)));
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(MaxEval.MAX_EVALUATIONS, maxEvaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() throws UnivariateOptimizerException {
        BaseOptimizer<PAIR> optimizer = new BrentOptimizer(new GoalTypeValueFunction(GoalType.MAXIMIZE, new SimpleBounds(0.0, 1.0)));
        optimizer.optimize();
        int evaluations = optimizer.getEvaluations();
        assertEquals(1, evaluations);

        optimizer.optimize();
        evaluations = optimizer.getEvaluations();
        assertEquals(2, evaluations);

        optimizer.optimize();
        evaluations = optimizer.getEvaluations();
        assertEquals(3, evaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new BrentOptimizer(new GoalTypeValueFunction(GoalType.MAXIMIZE, new SimpleBounds(0.0, 1.0)));
        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        assertNull(convergenceChecker);

        optimizer = new BrentOptimizer(new AbstractConvergenceChecker<PAIR>() {
            @Override
            public Finding checkConvergence(UnivariateOptimizer<Number> optimizer, double fx, PointValuePair currentPoint) {
                return Finding.CONVERGED;
            }
        });
        convergenceChecker = optimizer.getConvergenceChecker();
        assertNull(convergenceChecker);

        optimizer = new BrentOptimizer(new ConvergenceChecker<PAIR>() {
            @Override
            public Finding checkConvergence(UnivariateOptimizer<Number> optimizer, double fx, PointValuePair currentPoint) {
                return Finding.DIVERGED;
            }
        });
        convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull(convergenceChecker);
    }

    /**
     * Test methods for edge cases and exceptions.
     */
    @Test(expected = UnivariateOptimizerException.class)
    public void testExceptionHandling() {
        BaseOptimizer<PAIR> optimizer = new BrentOptimizer(new GoalTypeValueFunction(GoalType.MAXIMIZE, new SimpleBounds(Double.NEGATIVE_INFINITY, Double.NaN)));
        optimizer.optimize();
    }

    @Test
    public void testGetMaxEvaluationsWithCustomMaximum() {
        BaseOptimizer<PAIR> optimizer = new BrentOptimizer(new GoalTypeValueFunction(GoalType.MAXIMIZE, new SimpleBounds(0.0, 1.0)), 10);
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(10, maxEvaluations);
    }
}