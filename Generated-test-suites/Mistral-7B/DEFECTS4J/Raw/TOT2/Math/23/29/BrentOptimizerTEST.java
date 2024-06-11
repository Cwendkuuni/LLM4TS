package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.GoalType;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

/**
 * Test cases for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {
    /**
     * Test for minimization with given initial guess.
     */
    @Test
    public void testMinimizationWithInitialGuess() {
        double function = Math::pow;
        double min = Math.pow(2.0, 0.5);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateOptimizer result = optimizer.optimize(
            new UnivariateFunctionWrapper(function),
            0.0,
            new InitialGuess(1.5),
            GoalType.MINIMIZE);
        PointValuePair expected = new PointValuePair(
            Math.sqrt(2.0), min);
        assertEquals(expected.getPoint(), result.getPoint(), 1e-6);
        assertEquals(expected.getValue(), result.getValue(), 1e-6);
    }

    /**
     * Test for minimization without initial guess.
     */
    @Test
    public void testMinimizationWithoutInitialGuess() {
        double function = Math::pow;
        double min = Math.pow(2.0, 0.5);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateOptimizer result = optimizer.optimize(
            new UnivariateFunctionWrapper(function),
            0.0,
            null,
            GoalType.MINIMIZE);
        PointValuePair expected = new PointValuePair(
            Math.sqrt(2.0), min);
        assertEquals(expected.getPoint(), result.getPoint(), 1e-6);
        assertEquals(expected.getValue(), result.getValue(), 1e-6);
    }

    /**
     * Test for minimization with invalid relative tolerance.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testMinimizationWithInvalidRelativeTolerance() {
        double function = Math::pow;
        double min = Math.pow(2.0, 0.5);
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-6);
    }

    /**
     * Test for minimization with invalid absolute tolerance.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testMinimizationWithInvalidAbsoluteTolerance() {
        double function = Math::pow;
        double min = Math.pow(2.0, 0.5);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, -1e-6);
    }

    /**
     * Test for minimization with invalid goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMinimizationWithInvalidGoalType() {
        double function = Math::pow;
        double min = Math.pow(2.0, 0.5);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.optimize(
            new UnivariateFunctionWrapper(function),
            0.0,
            null,
            GoalType.MAXIMIZE);
    }

    /**
     * Test for minimization with a function that returns NaN.
     */
    @Test(expected = ArithmeticException.class)
    public void testMinimizationWithNaNFunction() {
        double function = x -> Math.sqrt(- x);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.optimize(
            new UnivariateFunctionWrapper(function),
            0.0,
            null,
            GoalType.MINIMIZE);
    }

    /**
     * Test for minimization with a function that is not monotonous.
     */
    @Test
    public void testMinimizationWithNonMonotonousFunction() {
        double function = x -> Math.pow(x, 2) * Math.sin(x);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateOptimizer result = optimizer.optimize(
            new UnivariateFunctionWrapper(function),
            -5.0,
            null,
            GoalType.MINIMIZE);
        double expectedMin = -3.141592653589793;
        double expectedPoint = Math.PI;
        assertEquals(expectedPoint, result.getPoint(), 1e-6);
        assertEquals(expectedMin, result.getValue(), 1e-6);
    }

    /**
     * Test for minimization with a function that is not differentiable.
     */
    @Test
    public void testMinimizationWithNonDifferentiableFunction() {
        double function = x -> Math.abs(x);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateOptimizer result = optimizer.optimize(
            new UnivariateFunctionWrapper(function),
            0.0,
            null,
            GoalType.MINIMIZE);
        double expectedMin = 0.0;
        double expectedPoint = 0.0;
        assertEquals(expectedPoint, result.getPoint(), 1e-6);
        assertEquals(expectedMin, result.getValue(), 1e-6);
    }

    /**
     * Test for maximization with given initial guess.
     */
    @Test
    public void testMaximizationWithInitialGuess() {
        double function = Math::pow;
        double max = Math.pow(2.0, 0.5);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateOptimizer result = optimizer.optimize(
            new UnivariateFunctionWrapper(function),
            0.0,
            new InitialGuess(1.5),
            GoalType.MAXIMIZE);
        PointValuePair expected = new PointValuePair(
            Math.sqrt(2.0), max);
        assertEquals(expected.getPoint(), result.getPoint(), 1e-6);
        assertEquals(expected.getValue(), result.getValue(), 1e-6);
    }

    /**
     * Test for maximization without initial guess.
     */
    @Test
    public void testMaximizationWithoutInitialGuess() {
        double function = Math::pow;
        double max = Math.pow(2.0, 0.5);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateOptimizer result = optimizer.optimize(
            new UnivariateFunctionWrapper(function),
            0.0,
            null,
            GoalType.MAXIMIZE);
        PointValuePair expected = new PointValuePair(
            Math.sqrt(2.0), max);
        assertEquals(expected.getPoint(), result.getPoint(), 1e-6);
        assertEquals(expected.getValue(), result.getValue(), 1e-6);
    }

    /**
     * Test for maximization with invalid relative tolerance.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testMaximizationWithInvalidRelativeTolerance() {
        double function = Math::pow;
        double max = Math.pow(2.0, 0.5);
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-6);
    }

    /**
     * Test for maximization with invalid absolute tolerance.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testMaximizationWithInvalidAbsoluteTolerance() {
        double function = Math::pow;
        double max = Math.pow(2.0, 0.5);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, -1e-6);
    }

    /**
     * Test for maximization with invalid goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMaximizationWithInvalidGoalType() {
        double function = Math::pow;
        double max = Math.pow(2.0, 0.5);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.optimize(
            new UnivariateFunctionWrapper(function),
            0.0,
            null,
            GoalType.MINIMIZE);
    }

    /**
     * Test for maximization with a function that returns NaN.
     */
    @Test(expected = ArithmeticException.class)
    public void testMaximizationWithNaNFunction() {
        double function = x -> Math.sqrt(- x);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.optimize(
            new UnivariateFunctionWrapper(function),
            0.0,
            null,
            GoalType.MAXIMIZE);
    }

    /**
     * Test for maximization with a function that is not monotonous.
     */
    @Test
    public void testMaximizationWithNonMonotonousFunction() {
        double function = x -> Math.pow(x, 2) * Math.sin(x);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateOptimizer result = optimizer.optimize(
            new UnivariateFunctionWrapper(function),
            -5.0,
            null,
            GoalType.MAXIMIZE);
        double expectedMax = 3.141592653589793;
        double expectedPoint = -Math.PI;
        assertEquals(expectedPoint, result.getPoint(), 1e-6);
        assertEquals(expectedMax, result.getValue(), 1e-6);
    }

    /**
     * Test for maximization with a function that is not differentiable.
     */
    @Test
    public void testMaximizationWithNonDifferentiableFunction() {
        double function = x -> Math.abs(x);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateOptimizer result = optimizer.optimize(
            new UnivariateFunctionWrapper(function),
            0.0,
            null,
            GoalType.MAXIMIZE);
        double expectedMax = 0.0;
        double expectedPoint = 0.0;
        assertEquals(expectedPoint, result.getPoint(), 1e-6);
        assertEquals(expectedMax, result.getValue(), 1e-6);
    }
}