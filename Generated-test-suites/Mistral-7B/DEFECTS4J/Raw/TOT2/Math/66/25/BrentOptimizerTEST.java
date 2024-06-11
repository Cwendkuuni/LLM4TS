package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeWeightedSum;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMinimizing;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMaximizing;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunctionGrad;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleBoundingMethod;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer.BracketingStrategy;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer.BracketingStrategyType;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.junit.Test;

/**
 * Test class for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    /**
     * Test optimization of a simple function.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        UnivariateFunction f = new PolynomialFunction(new double[] { 1, -2, 1 }, 3);
        BrentOptimizer optimizer = new BrentOptimizer();
        double x;

        // Minimization
        x = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0, 0.0).getPoint();
        assertEquals(0.0, x, 1e-8);

        // Maximization
        optimizer = new BrentOptimizer();
        optimizer.setGoalType(GoalType.MAXIMIZE);
        x = optimizer.optimize(f.negate(), GoalType.MAXIMIZE, -10.0, 10.0, 0.0).getPoint();
        assertEquals(2.0, x, 1e-8);
    }

    /**
     * Test optimization of a function with no bracketing.
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizeFunctionNoBracketing() {
        UnivariateFunction f = new PolynomialFunction(new double[] { 1, 0, -1 }, 3);
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
    }

    /**
     * Test optimization of a function with infinite bracketing.
     */
    @Test(expected = MaxEvalException.class)
    public void testOptimizeFunctionInfiniteBracketing() {
        UnivariateFunction f = new PolynomialFunction(new double[] { 1.0 / 2.0 }, 0);
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0, 0.0, 1, 1e-6);
    }

    /**
     * Test optimization of a function with zero gradient.
     */
    @Test
    public void testOptimizeFunctionZeroGradient() {
        UnivariateFunction f = new PolynomialFunction(new double[] { 1, 0, 0 }, 2);
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(100);
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
        assertEquals(0.0, result.getPoint(), 1e-8);
    }

    /**
     * Test optimization of a function with a singularity.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testOptimizeFunctionSingularity() {
        UnivariateFunction f = new PolynomialFunction(new double[] { 1, 0, 0 }, 2);
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(1e-16);
        optimizer.optimize(f.multiply(new PolynomialFunction(new double[] { 1, 0, 1 }, 3)), GoalType.MINIMIZE, -10.0, 10.0, 0.0);
    }

    /**
     * Test optimization of a function with bounds.
     */
    @Test
    public void testOptimizeFunctionBounds() {
        UnivariateFunction f = new PolynomialFunction(new double[] { 1, -2, 1 }, 3);
        BrentOptimizer optimizer = new BrentOptimizer();
        double x;
        double min = 0.1;
        double max = 5.1;

        x = optimizer.optimize(f, GoalType.MINIMIZE, min, max, 0.0).getPoint();
        assertEquals(0.0, x, 1e-8);
        assertTrue(min <= x && x <= max);

        optimizer = new BrentOptimizer();
        optimizer.setGoalType(GoalType.MAXIMIZE);
        x = optimizer.optimize(f.negate(), GoalType.MAXIMIZE, min, max, 0.0).getPoint();
        assertEquals(2.0, x, 1e-8);
        assertTrue(min <= x && x <= max);
    }

    /**
     * Test optimization of a function with a custom goal type.
     */
    @Test
    public void testOptimizeFunctionCustomGoalType() {
        UnivariateFunction f = new PolynomialFunction(new double[] { 1, -2, 1 }, 3);
        BrentOptimizer optimizer = new BrentOptimizer();
        double x;
        double min = 0.1;
        double max = 5.1;

        ObjectiveFunctionWeightedSum goalFunction = new ObjectiveFunctionWeightedSum(new ObjectiveFunction[] { f, f.negate() }, new double[] { 0.5, 0.5 });
        GoalType customGoalType = new GoalTypeWeightedSum(new GoalType[] { GoalType.MINIMIZE, GoalType.MAXIMIZE }, new double[] { 0.5, 0.5 });
        x = optimizer.optimize(goalFunction, customGoalType, min, max, 0.0).getPoint();
        assertEquals(1.0, x, 1e-8);
        assertTrue(min <= x && x <= max);
    }

    /**
     * Test optimization of a function with a custom bracketing strategy.
     */
    @Test
    public void testOptimizeFunctionCustomBracketingStrategy() {
        UnivariateFunction f = new PolynomialFunction(new double[] { 1, -2, 1 }, 3);
        BrentOptimizer optimizer = new BrentOptimizer();
        double x;
        double min = 0.1;
        double max = 5.1;

        optimizer.setBracketingStrategy(BracketingStrategyType.BACKTRACKING11POINT);
        x = optimizer.optimize(f, GoalType.MINIMIZE, min, max, 0.0).getPoint();
        assertEquals(0.0, x, 1e-8);
        assertTrue(min <= x && x <= max);

        optimizer.setGoalType(GoalType.MAXIMIZE);
        x = optimizer.optimize(f.negate(), GoalType.MAXIMIZE, min, max, 0.0).getPoint();
        assertEquals(2.0, x, 1e-8);
        assertTrue(min <= x && x <= max);
    }

    /**
     * Test optimization of a function with a custom bracketing strategy.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeFunctionInvalidBracketingStrategy() {
        UnivariateFunction f = new PolynomialFunction(new double[] { 1, -2, 1 }, 3);
        BrentOptimizer optimizer = new BrentOptimizer();

        optimizer.setBracketingStrategy(new BracketingStrategy() {
            @Override
            public double bracketingStep(double a, double b, double fA, double fB, double tolerance) {
                throw new UnsupportedOperationException();
            }
        });
        optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0, 0.0);
    }
}