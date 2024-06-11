package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.NonUnivariateOptimizationException;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.PointValuePairChecker;
import org.apache.commons.math3.optim.Minimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunctionAdapter;
import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    /**
     * Test function f(x) = 1 - x^2.
     */
    @Test
    public void testRosenbrockFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-8, 1e-8);
        UnivariateFunction f = new UnivariateFunctionAdapter(new RosenbrockFunction());

        PointValuePair solution = optimizer.optimize(f, new InitialGuess(0.5), GoalType.MINIMIZE);
        PointValuePairChecker checker = new PointValuePairChecker(new PointValuePair(0.0, 1.0), 1e-8);
        assertTrue(checker.check(solution, "Rosenbrock function"));
    }

    /**
     * Test function f(x) = |x|.
     */
    @Test
    public void testAbsoluteValueFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-8, 1e-8, new SimpleValueChecker(1e-8));
        UnivariateFunction f = new UnivariateFunctionAdapter(new AbsoluteValueFunction());

        PointValuePair solution = optimizer.optimize(f, new InitialGuess(1), GoalType.MINIMIZE);
        assertEquals(0.0, solution.getPoint(), 1e-8);
    }

    /**
     * Test function f(x) = x^2 for x < 0 and f(x) = x for x >= 0.
     */
    @Test
    public void testPieceWiseFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        UnivariateFunction f = new UnivariateFunctionAdapter(new PieceWiseFunction());

        try {
            PointValuePair solution = optimizer.optimize(f, new InitialGuess(-1), GoalType.MINIMIZE);
            fail("Expected NonUnivariateOptimizationException for piecewise function.");
        } catch (NonUnivariateOptimizationException e) {
            // Expected
        }

        PointValuePair solution = optimizer.optimize(f, new InitialGuess(0), GoalType.MINIMIZE);
        assertEquals(0.0, solution.getPoint(), 1e-6);
    }

    /**
     * Test function f(x,y) = x^2 + y^2.
     */
    @Test
    public void testTwoVariablesFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-8, 1e-8, new SimpleValueChecker(1e-8));
        MultivariateFunction f = new MultivariateFunctionAdapter(new TwoVariablesFunction());

        PointValuePair solution = optimizer.optimize(f, new InitialGuess[]{new InitialGuess(1.0), new InitialGuess(1.0)}, GoalType.MINIMIZE);
        PointValuePairChecker checker = new PointValuePairChecker(new PointValuePair(0.0, 0.0), 1e-8);
        assertTrue(checker.check(solution, "Two variables function"));
    }

    /**
     * Test that the optimizer throws an exception when the initial guess is not an array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInitialGuessType() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-8, 1e-8);
        optimizer.optimize(new UnivariateFunction() {
            public double value(double x) {
                return x * x;
            }
        }, new Object(), GoalType.MINIMIZE);
    }

    /**
     * Test that the optimizer throws an exception when the initial guess array has an incorrect length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInitialGuessLength() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-8, 1e-8);
        optimizer.optimize(new UnivariateFunction() {
            public double value(double x) {
                return x * x;
            }
        }, new InitialGuess[]{new InitialGuess(1.0)}, GoalType.MINIMIZE);
    }

    /**
     * Test that the optimizer throws an exception when the goal type is incorrect.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-8, 1e-8);
        optimizer.optimize(new UnivariateFunction() {
            public double value(double x) {
                return x * x;
            }
        }, new InitialGuess[]{new InitialGuess(1.0)}, GoalType.MAXIMIZE);
    }

    /**
     * Test function f(x) = x^2.
     */
    private static class RosenbrockFunction implements UnivariateFunction {
        public double value(double x) {
            return 1.0 - x * x;
        }

        public double value(double[] x) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Test function f(x) = |x|.
     */
    private static class AbsoluteValueFunction implements UnivariateFunction {
        public double value(double x) {
            return Math.abs(x);
        }

        public double value(double[] x) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Test function f(x) = x^2 for x < 0 and f(x) = x for x >= 0.
     */
    private static class PieceWiseFunction implements UnivariateFunction {
        public double value(double x) {
            if (x < 0) {
                return x * x;
            } else {
                return x;
            }
        }

        public double value(double[] x) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Test function f(x, y) = x^2 + y^2.
     */
    private static class TwoVariablesFunction implements MultivariateFunction {
        public double value(double[] x) {
            double xVal = x[0];
            double yVal = x[1];
            return xVal * xVal + yVal * yVal;
        }

        public double value(double x, double y) {
            return value(new double[]{x, y});
        }
    }
}