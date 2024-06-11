package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.PointValuePair.PairType;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.convergence.ConvergenceCriteria;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearchOptimizer;

import org.junit.Test;

/**
 * Test cases for {@link BaseOptimizer} interface.
 *
 * @param <PAIR> Type of the point/objective pair.
 */
public class BaseOptimizerTest<PAIR extends PointValuePair<Number, Number>> {

    /** Tested optimizer with no convergence checker. */
    private FibonacciSearchOptimizer<Number> optimizerNoChecker = new FibonacciSearchOptimizer<Number>(GoalType.MAXIMIZE);

    /** Tested optimizer with convergence checker. */
    private FibonacciSearchOptimizer<Number> optimizerWithChecker;

    /** Random number generator. */
    private final Random rand = new Random();

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        // No checker optimizer
        assertEquals(Integer.MAX_VALUE, optimizerNoChecker.getMaxIterations());

        // Optimizer with checker
        ConvergenceChecker<PAIR> checker = new ConvergenceCriteria(1.0E-6);
        optimizerWithChecker = new FibonacciSearchOptimizer<Number>(GoalType.MAXIMIZE, checker);
        assertEquals(10, optimizerWithChecker.getMaxIterations()); // Default value

        // Set max evaluations
        optimizerWithChecker.setMaxIterations(50);
        assertEquals(50, optimizerWithChecker.getMaxIterations());
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        // No checker optimizer
        int initialEvaluations = optimizerNoChecker.getIterations();
        optimizerNoChecker.optimize(new SimpleBounds(0.0, 1.0), new ObjectiveFunction() {
            @Override
            public Number value(Number x) {
                return Math.exp(x);
            }
        });
        int finalEvaluations = optimizerNoChecker.getIterations();
        assertEquals(initialEvaluations + 1, finalEvaluations);

        // Optimizer with checker
        optimizerWithChecker = new FibonacciSearchOptimizer<Number>(GoalType.MAXIMIZE, new ConvergenceCriteria(1.0E-6));
        initialEvaluations = optimizerWithChecker.getIterations();
        optimizerWithChecker.optimize(new SimpleBounds(0.0, 1.0), new ObjectiveFunction() {
            @Override
            public Number value(Number x) {
                return Math.exp(x);
            }
        });
        finalEvaluations = optimizerWithChecker.getIterations();
        assertEquals(initialEvaluations + 1, finalEvaluations);
        assertTrue(optimizerWithChecker.isConverged());
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetConvergenceCheckerNoChecker() {
        optimizerNoChecker.getConvergenceChecker();
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @Test
    public void testGetConvergenceCheckerWithChecker() {
        optimizerWithChecker = new FibonacciSearchOptimizer<Number>(GoalType.MAXIMIZE, new ConvergenceCriteria(1.0E-6));
        ConvergenceChecker<PAIR> checker = optimizerWithChecker.getConvergenceChecker();
        assertEquals(1.0E-6, checker.getAbsoluteTolerance(), 1.0E-8);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()} with no checker.
     */
    @Test
    public void testGetConvergenceCheckerNoCheck() {
        optimizerNoChecker.getConvergenceChecker(); // Should not throw an exception
        assertNull(optimizerNoChecker.getConvergenceChecker());
    }
}