package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.goaltype.GoalType;
import org.apache.commons.math3.optim.nonlinear.function.Axes;
import org.apache.commons.math3.optim.nonlinear.function.PolynomialFunction;
import org.apache.commons.math3.optim.nonlinear.function.UnivariateFunction;
import org.apache.commons.math3.optim.point.MaxEval;
import org.apache.commons.math3.optim.point.PointValuePairCollection;
import org.apache.commons.math3.optim.initialguess.InitialGuess;
import org.apache.commons.math3.optim.nonlinear.simple.SquaredErrorFunction;
import org.apache.commons.math3.optim.nonlinear.simple.UnivariateSquaredErrorFunction;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.convergence.IterationCounter;
import org.junit.Test;

public class BrentOptimizerTest {

    // Test method: testBrentOptimizer_FunctionMinimum
    @Test
    public void testBrentOptimizer_FunctionMinimum() {
        UnivariateFunction testFunction = new PolynomialFunction(2, new double[]{-1.0, 1.0, 1.0});
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new IterationCounter(100));
        PointValuePair minimum = optimizer.optimize(testFunction, new InitialGuess(0.5), new GoalType(GoalType.MINIMIZE), new MaxEval(1000));
        assertEquals(minimum.getX(), -1.0, 1e-6);
        assertEquals(minimum.getValue(), 2.0, 1e-6);
    }

    // Test method: testBrentOptimizer_FunctionMaximum
    @Test
    public void testBrentOptimizer_FunctionMaximum() {
        UnivariateFunction testFunction = new PolynomialFunction(2, new double[]{-1.0, 1.0, -1.0});
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new IterationCounter(100));
        PointValuePair maximum = optimizer.optimize(testFunction, new InitialGuess(0.5), new GoalType(GoalType.MAXIMIZE), new MaxEval(1000));
        assertEquals(maximum.getX(), 1.0, 1e-6);
        assertEquals(maximum.getValue(), 2.0, 1e-6);
    }

    // Test method: testBrentOptimizer_FunctionWithZeroGradient
    @Test
    public void testBrentOptimizer_FunctionWithZeroGradient() {
        UnivariateFunction testFunction = new PolynomialFunction(3, new double[]{-1.0, 1.0, 1.0, 0.0});
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new IterationCounter(100));
        PointValuePair minimum = optimizer.optimize(testFunction, new InitialGuess(0.5), new GoalType(GoalType.MINIMIZE), new MaxEval(1000));
        assertEquals(minimum.getX(), 0.0, 1e-6);
        assertEquals(minimum.getValue(), 0.0, 1e-6);
    }

    // Test method: testBrentOptimizer_FunctionWithVerySmallGradient
    @Test
    public void testBrentOptimizer_FunctionWithVerySmallGradient() {
        UnivariateFunction testFunction = new PolynomialFunction(3, new double[]{-1.0, 1.0, 1.0e-8, 1.0});
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new IterationCounter(100));
        PointValuePair minimum = optimizer.optimize(testFunction, new InitialGuess(0.5), new GoalType(GoalType.MINIMIZE), new MaxEval(1000));
        assertEquals(minimum.getX(), 0.5 + 1e-6, 1e-6);
        assertEquals(minimum.getValue(), 1.0, 1e-6);
    }

    // Test method: testBrentOptimizer_FunctionWithVeryLargeGradient
    @Test
    public void testBrentOptimizer_FunctionWithVeryLargeGradient() {
        UnivariateFunction testFunction = new PolynomialFunction(3, new double[]{1.0e10, 1.0, 1.0, 1.0e-10});
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new IterationCounter(100));
        PointValuePair minimum = optimizer.optimize(testFunction, new InitialGuess(0.5), new GoalType(GoalType.MINIMIZE), new MaxEval(1000));
        assertEquals(minimum.getX(), 0.5, 1e-6);
        assertEquals(minimum.getValue(), 0.5, 1e-6);
    }

    // Test method: testBrentOptimizer_FunctionWithNegativeAbsoluteThreshold
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testBrentOptimizer_FunctionWithNegativeAbsoluteThreshold() {
        UnivariateOptimizer optimizer = new BrentOptimizer(-1e-6, 1e-6, new IterationCounter(100));
    }

    // Test method: testBrentOptimizer_FunctionWithSmallRelativeThreshold
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testBrentOptimizer_FunctionWithSmallRelativeThreshold() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-8, 1e-6, new IterationCounter(100));
    }

    // Test method: testBrentOptimizer_FunctionWithNullGoalType
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testBrentOptimizer_FunctionWithNullGoalType() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new IterationCounter(100), null);
    }

    // Test method: testBrentOptimizer_FunctionWithNullInitialGuess
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testBrentOptimizer_FunctionWithNullInitialGuess() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new IterationCounter(100), new GoalType(GoalType.MINIMIZE), null);
    }

    // Test method: testBrentOptimizer_FunctionWithNullConvergenceChecker
    @Test
    public void testBrentOptimizer_FunctionWithNullConvergenceChecker() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        PointValuePair minimum = optimizer.optimize(new Axes(), new InitialGuess(0.5), new GoalType(GoalType.MINIMIZE), new MaxEval(1000));
        assertEquals(minimum.getX(), 0.5, 1e-6);
        assertEquals(minimum.getValue(), 0.0, 1e-6);
    }

    // Test method: testBrentOptimizer_FunctionWithCustomConvergenceChecker
    @Test
    public void testBrentOptimizer_FunctionWithCustomConvergenceChecker() {
        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return (previous != null && current != null && previous.getValue() - current.getValue() < 1e-6);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, customChecker);
        PointValuePair minimum = optimizer.optimize(new Axes(), new InitialGuess(0.5), new GoalType(GoalType.MINIMIZE), new MaxEval(1000));
        assertEquals(minimum.getX(), 0.5, 1e-6);
        assertEquals(minimum.getValue(), 0.0, 1e-6);
    }

    // Test method: testBrentOptimizer_FunctionWithCustomErrorFunction
    @Test
    public void testBrentOptimizer_FunctionWithCustomErrorFunction() {
        UnivariateFunction testFunction = new UnivariateSquaredErrorFunction(new PolynomialFunction(2, new double[]{-1.0, 1.0, 1.0}));
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new IterationCounter(100));
        PointValuePair minimum = optimizer.optimize(testFunction, new InitialGuess(0.5), new GoalType(GoalType.MINIMIZE), new MaxEval(1000));
        assertEquals(minimum.getX(), -1.0, 1e-6);
        assertEquals(minimum.getValue(), 2.0, 1e-6);
    }
}