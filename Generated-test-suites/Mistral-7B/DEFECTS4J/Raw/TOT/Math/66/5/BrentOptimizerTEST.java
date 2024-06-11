package org.apache.commons.math.optimization.univariate.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.TestException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar. ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.special.E;
import org.junit.Test;

/**
 * Tests for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    @Test
    public void testSimpleMin() throws TestException {
        double a = 1.0;
        double b = 3.0;
        double x = 2.0;
        UnivariateFunction f = new PolynomialFunction(new double[] { 1, -2.0 }, 1);
        GoalType goalType = GoalType.MINIMIZE;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(10);

        PointValuePair result = optimizer.optimize(f, goalType, a, b, x);

        assertEquals(E.EULER, result.getValue(), 1e-5);
        assertEquals(x, result.getPoint(), 1e-5);
    }

    @Test
    public void testSimpleMax() throws TestException {
        double a = 1.0;
        double b = 3.0;
        double x = 2.0;
        UnivariateFunction f = new PolynomialFunction(new double[] { -1, 2.0 }, 1);
        GoalType goalType = GoalType.MAXIMIZE;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(10);

        PointValuePair result = optimizer.optimize(f, goalType, a, b, x);

        assertEquals(E.EULER, result.getValue(), 1e-5);
        assertEquals(x, result.getPoint(), 1e-5);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidArgument() {
        UnivariateFunction f = new PolynomialFunction(new double[] { 1 }, 1);
        double min = 1.0;
        double max = 1.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, GoalType.MINIMIZE, min, max);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testMaxIterations() throws TestException {
        double a = 1.0;
        double b = 3.0;
        double x = 2.0;
        UnivariateFunction f = new PolynomialFunction(new double[] { 1, -2.0 }, 1);
        GoalType goalType = GoalType.MINIMIZE;

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(0);

        optimizer.optimize(f, goalType, a, b, x);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws TestException {
        double a = 1.0;
        double b = 3.0;
        double x = 2.0;
        UnivariateFunction f = new FunctionUtils()::sqrt;
        GoalType goalType = GoalType.MINIMIZE;

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(10);

        optimizer.optimize(f, goalType, a, b, x);
    }

    @Test
    public void testCustomBounds() throws TestException {
        double a = 1.0;
        double b = 3.0;
        double x = 2.0;
        UnivariateFunction f = new PolynomialFunction(new double[] { 1, -2.0 }, 1);
        GoalType goalType = GoalType.MINIMIZE;

        SimpleBounds bounds = new SimpleBounds(1.1, 2.9);

        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(10);
        optimizer.optimize(f, goalType, bounds, x);

        assertEquals(E.EULER, optimizer.getValue(), 1e-5);
        assertEquals(x, optimizer.getPoint(), 1e-5);
    }

    @Test
    public void testCustomInitialGuess() throws TestException {
        double a = 1.0;
        double b = 3.0;
        double x = 2.0;
        UnivariateFunction f = new PolynomialFunction(new double[] { 1, -2.0 }, 1);
        GoalType goalType = GoalType.MINIMIZE;

        InitialGuess initialGuess = new InitialGuess(2.1);

        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(10);
        PointValuePair result = optimizer.optimize(f, goalType, a, b, initialGuess);

        assertEquals(E.EULER, result.getValue(), 1e-5);
        assertEquals(x, result.getPoint(), 1e-5);
    }

}