package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolverFactory;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.util.Precision;

import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;

/**
 * Test class for UnivariateRealSolverUtils.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}.
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @org.junit.Test
    public void testSolve() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double result = UnivariateRealSolverUtils.solve(func, 1.0, 2.0);
        double expected = Precision.round(Math.sqrt(2.0), 15);
        assertEquals(expected, result, 1e-15);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * with default solver.
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @org.junit.Test
    public void testSolveWithDefaultSolver() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };
        double result = UnivariateRealSolverUtils.solve(func, 0.0, 1.0);
        double expected = Precision.round(Math.PI / 2.0, 15);
        assertEquals(expected, result, 1e-15);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double, double)}
     * with default solver.
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @org.junit.Test
    public void testSolveWithDefaultSolverAndAccuracy() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };
        double result = UnivariateRealSolverUtils.solve(func, 0.0, 1.0, 1e-7, Double.POSITIVE_INFINITY);
        double expected = Precision.round(Math.PI / 2.0, 15);
        assertEquals(expected, result, 1e-7);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)}
     * with default solver.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @org.junit.Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double[] brackets = UnivariateRealSolverUtils.bracket(func, 1.0, -1.0, 2.0);
        double a = brackets[0];
        double b = brackets[1];
        double expectedA = Precision.round(-Math.sqrt(2.0), 15);
        double expectedB = Precision.round(Math.sqrt(2.0), 15);
        assertEquals(expectedA, a, 1e-15);
        assertEquals(expectedB, b, 1e-15);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with default solver.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @org.junit.Test
    public void testBracketWithMaxIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        double[] brackets = UnivariateRealSolverUtils.bracket(func, 0.0, 1.0, 1.0, 100);
        double a = brackets[0];
        double b = brackets[1];
        double expectedA = Precision.round(Math.PI / 2.0, 15);
        double expectedB = Precision.round(3.0, 15);
        assertEquals(expectedA, a, 1e-15);
        assertEquals(expectedB, b, 1e-15);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#setup(UnivariateRealFunction)}
     * @throws IllegalArgumentException
     */
    @org.junit.Test(expected = MathRuntimeException.class)
    public void testSetupWithNullFunction() {
        UnivariateRealFunction func = null;
        UnivariateRealSolverUtils.setup(func);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#midpoint(double, double)}
     * @throws IllegalArgumentException
     */
    @org.junit.Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 2.0;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        double expected = (a + b) / 2.0;
        assertEquals(expected, result, 1e-15);
    }
}