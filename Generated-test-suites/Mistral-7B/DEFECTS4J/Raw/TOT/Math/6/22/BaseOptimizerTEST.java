package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.ConvergenceCheckerError;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.StoppingCriteria;
import org.apache.commons.math3.optim.convergence.SimpleConvergenceChecker;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math3.optim.BaseOptimizer;
import org.apache.commons.math3.optim.MaxEvalStoppingCriteria;

/**
 * Test cases for {@link BaseOptimizer} interface.
 */
public class BaseOptimizerTest {

    /** The optimizer instance under test. */
    private BaseOptimizer<PointValuePair> optimizer;

    /** The convergence checker instance under test. */
    private ConvergenceChecker<PointValuePair> checker;

    /**
     * Set up the test instance.
     */
    @Before
    public void setUp() {
        optimizer = new SomeOptimizer();
        checker = new SimpleConvergenceChecker(1.0E-6, 10);
        optimizer.setConvergenceChecker(checker);
        optimizer.setMaxEvaluations(5);
    }

    /**
     * Test getMaxEvaluations method.
     */
    @Test
    public void testGetMaxEvaluations() {
        assertEquals(5, optimizer.getMaxEvaluations());
    }

    /**
     * Test getEvaluations method with no optimization call yet.
     */
    @Test
    public void testGetEvaluationsBeforeOptimization() {
        assertEquals(0, optimizer.getEvaluations());
    }

    /**
     * Test getEvaluations method after optimization.
     */
    @Test
    public void testGetEvaluationsAfterOptimization() {
        PointValuePair result = optimizer.optimize(new SimpleBounds(0.0, 1.0), new GoalType() {
            @Override
            public double value(double d) {
                return Math.sin(d);
            }
        });
        assertEquals(5, optimizer.getEvaluations());
        assertSame(result, optimizer.getMostRecentPointValuePair());
    }

    /**
     * Test getConvergenceChecker method.
     */
    @Test
    public void testGetConvergenceChecker() {
        assertSame(checker, optimizer.getConvergenceChecker());
    }

    /**
     * Test optimization with convergence.
     */
    @Test(expected = ConvergenceCheckerError.class)
    public void testOptimizationWithConvergence() {
        optimizer.optimize(new SimpleBounds(0.0, 1.0), new GoalType() {
            @Override
            public double value(double d) {
                return d * d;
            }
        });
    }

    /**
     * Test optimization with no convergence.
     */
    @Test(expected = MaxEvaluationsExceededException.class)
    public void testOptimizationWithMaxEvaluationsExceeded() {
        optimizer = new SomeOptimizer();
        optimizer.setMaxEvaluations(0);
        optimizer.optimize(new SimpleBounds(0.0, 1.0), new GoalType() {
            @Override
            public double value(double d) {
                return Math.sin(Math.PI * d);
            }
        });
    }

    /**
     * Test optimization with invalid convergence checker.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationWithInvalidConvergenceChecker() {
        optimizer = new SomeOptimizer();
        optimizer.setConvergenceChecker(null);
        optimizer.optimize(new SimpleBounds(0.0, 1.0), new GoalType() {
            @Override
            public double value(double d) {
                return Math.sin(Math.PI * d);
            }
        });
    }

    /**
     * Test optimization with null goal.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizationWithNullGoal() {
        optimizer = new SomeOptimizer();
        optimizer.optimize(new SimpleBounds(0.0, 1.0), null);
    }

    /**
     * Test optimization with invalid bounds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationWithInvalidBounds() {
        optimizer = new SomeOptimizer();
        optimizer.optimize(new SimpleBounds(-1.0, 0.0), new GoalType() {
            @Override
            public double value(double d) {
                return Math.sin(Math.PI * d);
            }
        });
    }
}

class SomeOptimizer extends BaseOptimizer<PointValuePair> {
    @Override
    public PointValuePair optimize(Bounds bounds, GoalType goal) {
        // Implementation for testing purposes.
        throw new UnsupportedOperationException();
    }

    @Override
    public StoppingCriteria getStoppingCriteria() {
        return new MaxEvalStoppingCriteria(MaxEval.MAX_EVAL);
    }

    @Override
    public void setInitialGuess(double[] initial) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaxEvaluations(int maxEvaluations) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setConvergenceChecker(ConvergenceChecker<PointValuePair> checker) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PointValuePair getMostRecentPointValuePair() {
        throw new UnsupportedOperationException();
    }
}