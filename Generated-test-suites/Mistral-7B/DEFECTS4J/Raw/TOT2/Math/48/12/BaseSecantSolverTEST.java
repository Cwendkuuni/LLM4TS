package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.special.Erf;

import org.junit.Test;

/**
 * Test cases for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test solver with function f(x) = x^2 - 2.
     */
    @Test
    public void testSolveFunctionX2Minus2() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(
                1.0e-12, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(1.0, solver.solve(10, f, -2.5, 2.5, 0.0), 1.0e-13);
    }

    /**
     * Test solver with function f(x) = x^3 - 2.
     */
    @Test
    public void testSolveFunctionX3Minus2() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x * x - 2.0;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(
                1.0e-12, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(-1.0, solver.solve(10, f, -3.0, 3.0, 0.0), 1.0e-13);
    }

    /**
     * Test solver with function f(x) = Erf(x) * exp(x).
     */
    @Test
    public void testSolveFunctionErfExp() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Erf.erf(x) * Math.exp(x);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(
                1.0e-4, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(-0.5, solver.solve(10, f, -1.5, 1.5, 0.0), 1.0e-5);
    }

    /**
     * Test exception when function is null.
     */
    @Test(expected = NullArgumentException.class)
    public void testExceptionFunctionNull() {
        BaseSecantSolver solver = new BaseSecantSolver(
                1.0e-12, BaseSecantSolver.Method.ILLINOIS);
        solver.solve(10, null, 0.0, 0.0, 0.0);
    }

    /**
     * Test exception when relative accuracy is negative.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testExceptionRelativeAccuracyNegative() {
        BaseSecantSolver solver = new BaseSecantSolver(
                -1.0e-12, BaseSecantSolver.Method.ILLINOIS);
    }

    /**
     * Test exception when absolute accuracy is negative.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testExceptionAbsoluteAccuracyNegative() {
        BaseSecantSolver solver = new BaseSecantSolver(
                1.0e-12, -1.0e-12, BaseSecantSolver.Method.ILLINOIS);
    }

    /**
     * Test exception when function value accuracy is negative.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testExceptionFunctionValueAccuracyNegative() {
        BaseSecantSolver solver = new BaseSecantSolver(
                1.0e-12, 1.0e-12, -1.0e-12, BaseSecantSolver.Method.ILLINOIS);
    }

    /**
     * Test exception when bracketing is not satisfied.
     */
    @Test(expected = ConvergenceException.class)
    public void testExceptionBracketingNotSatisfied() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1.0;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(
                1.0e-12, BaseSecantSolver.Method.ILLINOIS);
        solver.solve(10, f, 0.0, 0.0, 0.0);
    }

    /**
     * Test exception when maxEval is exceeded.
     */
    @Test(expected = MaxEvalExceededException.class)
    public void testExceptionMaxEvalExceeded() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / (1.0 + x * x);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(
                1.0e-12, BaseSecantSolver.Method.ILLINOIS);
        solver.solve(0, f, -2.0, 2.0, 0.0);
    }

    /**
     * Test exception when x is too close to the root.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testExceptionNumberIsTooSmall() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1.0, 2.0);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(
                1.0e-15, BaseSecantSolver.Method.ILLINOIS);
        solver.solve(10, f, 0.9999, 1.0001, 0.0);
    }
}