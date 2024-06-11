package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.goal.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeConvergenceChecker;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

/**
 * Test cases for {@link BrentOptimizer} class
 */
public class BrentOptimizerTest {

    @org.junit.Test
    public void testBrentOptimizerConstructor() {
        // Test the constructor that takes two arguments (relative and absolute threshold)
        try {
            new BrentOptimizer(-0.1, 0.1);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }

        new BrentOptimizer(0.1, 0.1);
        new BrentOptimizer(2.0, 0.1);
    }

    @org.junit.Test
    public void testBrentOptimizerConstructorWithChecker() {
        // Test the constructor that takes three arguments (relative, absolute threshold, and ConvergenceChecker)
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.1, new GoalTypeConvergenceChecker(GoalType.MINIMIZE, 10));
        assertNotNull(optimizer);
    }

    @org.junit.Test
    public void testBrentOptimizerMinimizeSimpleFunction() {
        UnivariateFunction function = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.1);
        PointValuePair result = optimizer.optimize(function, new InitialGuess(2.0));
        assertEquals(1.0, result.getPoint(), 0.001);
        assertEquals(-E.EulerNumber.doubleValue(), result.getValue(), 0.001);
    }

    @org.junit.Test
    public void testBrentOptimizerMinimizeFunctionWithInitialGuessCloseToMinimum() {
        UnivariateFunction function = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.1);
        PointValuePair result = optimizer.optimize(function, new InitialGuess(0.99));
        assertEquals(1.0, result.getPoint(), 0.001);
        assertEquals(-E.EulerNumber.doubleValue(), result.getValue(), 0.001);
    }

    @org.junit.Test
    public void testBrentOptimizerMinimizeFunctionWithInitialGuessFarFromMinimum() {
        UnivariateFunction function = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.1);
        PointValuePair result = optimizer.optimize(function, new InitialGuess(3.0));
        assertEquals(1.0, result.getPoint(), 0.001);
        assertEquals(-E.EulerNumber.doubleValue(), result.getValue(), 0.001);
    }

    @org.junit.Test
    public void testBrentOptimizerMinimizeFunctionWithLargeRelTolerance() {
        UnivariateFunction function = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.1);
        PointValuePair result = optimizer.optimize(function, new InitialGuess(2.0));
        assertEquals(1.0, result.getPoint(), 0.1);
        assertEquals(-E.EulerNumber.doubleValue(), result.getValue(), 0.1);
    }

    @org.junit.Test
    public void testBrentOptimizerMinimizeFunctionWithSmallAbsTolerance() {
        UnivariateFunction function = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0001);
        PointValuePair result = optimizer.optimize(function, new InitialGuess(2.0));
        assertEquals(1.0, result.getPoint(), 0.0001);
        assertEquals(-E.EulerNumber.doubleValue(), result.getValue(), 0.0001);
    }

    @org.junit.Test
    public void testBrentOptimizerMinimizeFunctionWithCustomConvergenceChecker() {
        UnivariateFunction function = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.1, new MyConvergenceChecker());
        PointValuePair result = optimizer.optimize(function, new InitialGuess(2.0));
        assertEquals(1.0, result.getPoint(), 0.001);
        assertEquals(-E.EulerNumber.doubleValue(), result.getValue(), 0.001);
    }

    @org.junit.Test
    public void testBrentOptimizerMinimizeFunctionWithInitialGuessOutsideFunctionDomain() {
        UnivariateFunction function = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.1);
        try {
            optimizer.optimize(function, new InitialGuess(-1.0));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @org.junit.Test
    public void testBrentOptimizerMinimizeFunctionWithFunctionThrowsException() {
        UnivariateFunction function = new MyExceptionThrowingFunction();
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.1);
        try {
            optimizer.optimize(function, new InitialGuess(0.0));
            fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            // Expected exception
        }
    }

    private static class MyFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.exp(-x) - E.EulerNumber.doubleValue();
        }
    }

    private static class MyConvergenceChecker implements GoalTypeConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current, GoalType goalType) {
            return current.getValue() <= previous.getValue() - 0.001;
        }
    }
}

class MyExceptionThrowingFunction implements UnivariateFunction {
    @Override
    public double value(double x) throws Exception {
        throw new Exception("Exception thrown for testing purposes.");
    }
}