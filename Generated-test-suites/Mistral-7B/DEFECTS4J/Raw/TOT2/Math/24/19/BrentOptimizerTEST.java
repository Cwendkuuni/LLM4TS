package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.nonlinear.simple.WeierstrassFunction;
import org.apache.commons.math3.optim.nonlinear.simple.SineFunction;
import org.apache.commons.math3.optim.nonlinear.simple.AbsFunction;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer_minimization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new WeierstrassFunction(2, 1);

        PointValuePair solution = optimizer.optimize(function, new InitialGuess(1d), new MaxEval(100));
        assertEquals(0d, solution.getPoint(), 1e-6);
        assertEquals(-1.414213741250416e-4, solution.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizer_minimization_error() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new AbsFunction(new SineFunction());

        try {
            optimizer.optimize(function, new InitialGuess(1d), new MaxEval(100));
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testBrentOptimizer_minimization_convergence_checker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new MyConvergenceChecker());
        UnivariateFunction function = new WeierstrassFunction(2, 1);

        PointValuePair solution = optimizer.optimize(function, new InitialGuess(1d), new MaxEval(100));
        assertEquals(0d, solution.getPoint(), 1e-6);
        assertEquals(-1.414213741250416e-4, solution.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizer_minimization_custom_initial_guess() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new WeierstrassFunction(2, 1);

        PointValuePair solution = optimizer.optimize(function, new InitialGuess(2d), new MaxEval(100));
        assertEquals(-1.414213741250416e-4, solution.getPoint(), 1e-6);
        assertEquals(1.414213741250416e-4, solution.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizer_minimization_no_convergence() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new WeierstrassFunction(3, 1);

        PointValuePair solution = optimizer.optimize(function, new InitialGuess(1d), new MaxEval(100));
        // No solution is found within the specified tolerance
        assertNull(solution);
    }

    @Test
    public void testBrentOptimizer_minimization_max_evaluations() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new WeierstrassFunction(2, 1);

        try {
            optimizer.optimize(function, new InitialGuess(1d), new MaxEval(1));
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testBrentOptimizer_maximization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, GoalType.MAXIMIZE);
        UnivariateFunction function = new WeierstrassFunction(2, 1);

        PointValuePair solution = optimizer.optimize(function, new InitialGuess(1d), new MaxEval(100));
        assertEquals(0d, solution.getPoint(), 1e-6);
        assertEquals(1.414213741250416e-4, solution.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizer_maximization_error() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, GoalType.MAXIMIZE);
        UnivariateFunction function = new AbsFunction(new SineFunction());

        try {
            optimizer.optimize(function, new InitialGuess(1d), new MaxEval(100));
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testBrentOptimizer_maximization_convergence_checker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, GoalType.MAXIMIZE, new MyConvergenceChecker());
        UnivariateFunction function = new WeierstrassFunction(2, 1);

        PointValuePair solution = optimizer.optimize(function, new InitialGuess(1d), new MaxEval(100));
        assertEquals(0d, solution.getPoint(), 1e-6);
        assertEquals(1.414213741250416e-4, solution.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizer_maximization_custom_initial_guess() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, GoalType.MAXIMIZE);
        UnivariateFunction function = new WeierstrassFunction(2, 1);

        PointValuePair solution = optimizer.optimize(function, new InitialGuess(2d), new MaxEval(100));
        assertEquals(-1.414213741250416e-4, solution.getPoint(), 1e-6);
        assertEquals(-1.414213741250416e-4, solution.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizer_maximization_no_convergence() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, GoalType.MAXIMIZE);
        UnivariateFunction function = new WeierstrassFunction(3, 1);

        PointValuePair solution = optimizer.optimize(function, new InitialGuess(1d), new MaxEval(100));
        // No solution is found within the specified tolerance
        assertNull(solution);
    }

    @Test
    public void testBrentOptimizer_maximization_max_evaluations() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, GoalType.MAXIMIZE);
        UnivariateFunction function = new WeierstrassFunction(2, 1);

        try {
            optimizer.optimize(function, new InitialGuess(1d), new MaxEval(1));
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testBrentOptimizer_minimax() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new WeierstrassFunction(2, 1);
        UnivariatePointValuePair solution = optimizer.optimize(function, new InitialGuess(1d), new MaxEval(100), GoalType.MINIMMAX);

        // It should find the minimum, since the Weierstrass function goes to
        // negative values for x < 0
        assertEquals(0d, solution.getPoint(), 1e-6);
        assertEquals(-1.414213741250416e-4, solution.getValue(), 1e-7);

        // Now test the maximization
        optimizer.setGoalType(GoalType.MAXIMIZE);
        solution = optimizer.optimize(function, new InitialGuess(1d), new MaxEval(100), GoalType.MINIMMAX);

        assertEquals(0d, solution.getPoint(), 1e-6);
        assertEquals(1.414213741250416e-4, solution.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizer_minimax_error() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new AbsFunction(new SineFunction());

        try {
            optimizer.optimize(function, new InitialGuess(1d), new MaxEval(100), GoalType.MINIMMAX);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            if (iteration > 100) {
                return true;
            }
            return false;
        }
    }
}