package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Tests;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.Method;

/**
 * Test cases for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test case for {@link BaseSecantSolver#BaseSecantSolver(double, Method)}
     * constructor.
     */
    @Test
    public void testBaseSecantSolverConstructor() {
        final double absAccuracy = 1e-6;
        final double relAccuracy = 1e-8;
        final double funcValueAccuracy = 1e-10;
        final BaseSecantSolver solver = new BaseSecantSolver(relAccuracy, absAccuracy, funcValueAccuracy, Method.REGULA_FALSI);
        assertEquals(Method.REGULA_FALSI, solver.getMethod());
        assertEquals(relAccuracy, solver.getRelativeAccuracy(), 1e-12);
        assertEquals(absAccuracy, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals(funcValueAccuracy, solver.getFunctionValueAccuracy(), 1e-12);
    }

    /**
     * Test case for {@link BaseSecantSolver#BaseSecantSolver(double, double, Method)}
     * constructor.
     */
    @Test
    public void testBaseSecantSolverConstructor2() {
        final double absAccuracy = 1e-6;
        final double relAccuracy = 1e-8;
        final double funcValueAccuracy = 1e-10;
        final BaseSecantSolver solver = new BaseSecantSolver(relAccuracy, absAccuracy, funcValueAccuracy, Method.REGULA_FALSI);
        assertEquals(Method.REGULA_FALSI, solver.getMethod());
        assertEquals(relAccuracy, solver.getRelativeAccuracy(), 1e-12);
        assertEquals(absAccuracy, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals(funcValueAccuracy, solver.getFunctionValueAccuracy(), 1e-12);
    }

    /**
     * Test case for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, Method)}
     * constructor.
     */
    @Test
    public void testBaseSecantSolverConstructor3() {
        final double absAccuracy = 1e-6;
        final double relAccuracy = 1e-8;
        final double funcValueAccuracy = 1e-10;
        final BaseSecantSolver solver = new BaseSecantSolver(relAccuracy, absAccuracy, funcValueAccuracy, Method.REGULA_FALSI);
        assertEquals(Method.REGULA_FALSI, solver.getMethod());
        assertEquals(relAccuracy, solver.getRelativeAccuracy(), 1e-12);
        assertEquals(absAccuracy, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals(funcValueAccuracy, solver.getFunctionValueAccuracy(), 1e-12);
    }

    /**
     * Test case for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method.
     */
    @Test
    public void testSolve() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return FastMath.pow(x, 2) - 2;
            }
        };

        final double absAccuracy = 1e-6;
        final double relAccuracy = 1e-8;
        final double funcValueAccuracy = 1e-10;
        final BaseSecantSolver solver = new BaseSecantSolver(relAccuracy, absAccuracy, funcValueAccuracy, Method.REGULA_FALSI);

        // Test with default bounds
        final double root = solver.solve(100, f);
        assertEquals(0.0, root, 1e-12);

        // Test with custom bounds
        final double min = -5.0;
        final double max = 5.0;
        final double startValue = 2.0;
        final double result = solver.solve(100, f, min, max, startValue);
        assertEquals(0.0, result, 1e-12);
        Tests.assertEquals(min, solver.getMin(), 1e-12);
        Tests.assertEquals(max, solver.getMax(), 1e-12);
    }

    /**
     * Test case for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} method.
     */
    @Test
    public void testSolveAllowedSide() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return FastMath.pow(x, 2) - 2;
            }
        };

        final double absAccuracy = 1e-6;
        final double relAccuracy = 1e-8;
        final double funcValueAccuracy = 1e-10;
        final BaseSecantSolver solver = new BaseSecantSolver(relAccuracy, absAccuracy, funcValueAccuracy, Method.REGULA_FALSI);

        // Test with default bounds and LEFT_SIDE
        final AllowedSolution allowedSolution = AllowedSolution.LEFT_SIDE;
        final double result = solver.solve(100, f, 1.0, 2.0, 1.5, allowedSolution);
        assertEquals(1.0, result, 1e-12);

        // Test with custom bounds and LEFT_SIDE
        final double min = -5.0;
        final double max = 5.0;
        final double startValue = 2.0;
        final AllowedSolution allowedSolution2 = AllowedSolution.LEFT_SIDE;
        final double result2 = solver.solve(100, f, min, max, startValue, allowedSolution2);
        assertEquals(1.0, result2, 1e-12);
        Tests.assertEquals(min, solver.getMin(), 1e-12);
        Tests.assertEquals(max, solver.getMax(), 1e-12);

        // Test with default bounds and RIGHT_SIDE
        final AllowedSolution allowedSolution3 = AllowedSolution.RIGHT_SIDE;
        final double result3 = solver.solve(100, f, 1.0, 2.0, 1.5, allowedSolution3);
        assertEquals(-1.0, result3, 1e-12);

        // Test with custom bounds and RIGHT_SIDE
        final AllowedSolution allowedSolution4 = AllowedSolution.RIGHT_SIDE;
        final double result4 = solver.solve(100, f, min, max, startValue, allowedSolution4);
        assertEquals(-1.0, result4, 1e-12);
        Tests.assertEquals(min, solver.getMin(), 1e-12);
        Tests.assertEquals(max, solver.getMax(), 1e-12);
    }

    /**
     * Test case for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with
     * IllegalArgumentException.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveIllegalArgumentException() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1 / (x - 1) + 1 / (x + 1);
            }
        };

        final double absAccuracy = 1e-6;
        final double relAccuracy = 1e-8;
        final double funcValueAccuracy = 1e-10;
        final BaseSecantSolver solver = new BaseSecantSolver(relAccuracy, absAccuracy, funcValueAccuracy, Method.REGULA_FALSI);

        // Test with bounds that are not bracketing
        final double min = 1.5;
        final double max = 1.0;
        solver.solve(100, f, min, max);
    }

    /**
     * Test case for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with
     * ConvergenceException.
     */
    @Test(expected = ConvergenceException.class)
    public void testSolveConvergenceException() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, -Math.abs(x));
            }
        };

        final double absAccuracy = 1e-6;
        final double relAccuracy = 1e-8;
        final double funcValueAccuracy = 1e-10;
        final BaseSecantSolver solver = new BaseSecantSolver(relAccuracy, absAccuracy, funcValueAccuracy, Method.REGULA_FALSI);

        // Test with bounds where convergence is not guaranteed
        final double min = 0.5;
        final double max = 1.0;
        solver.solve(100, f, min, max);
    }

    /**
     * Test case for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with
     * MaxIterationsExceededException.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceededException() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        final double absAccuracy = 1e-6;
        final double relAccuracy = 1e-8;
        final double funcValueAccuracy = 1e-10;
        final BaseSecantSolver solver = new BaseSecantSolver(relAccuracy, absAccuracy, funcValueAccuracy, Method.REGULA_FALSI);

        // Test with bounds where convergence is unlikely and maximum number of iterations is reached
        final double min = 0.0;
        final double max = 2.0 * Math.PI;
        solver.setMaxIterations(10);
        solver.solve(10, f, min, max);
    }

    /**
     * Test case for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with
     * NoBracketingException.
     */
    @Test(expected = NoBracketingException.class)
    public void testSolveNoBracketingException() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        final double absAccuracy = 1e-6;
        final double relAccuracy = 1e-8;
        final double funcValueAccuracy = 1e-10;
        final BaseSecantSolver solver = new BaseSecantSolver(relAccuracy, absAccuracy, funcValueAccuracy, Method.REGULA_FALSI);

        // Test with bounds that do not bracket the root
        final double min = 2.0;
        final double max = 3.0;
        solver.solve(100, f, min, max);
    }

    /**
     * Test case for {@link BaseSecantSolver#getMethod()} method.
     */
    @Test
    public void testGetMethod() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-8, 1e-6, 1e-10, Method.PEGASUS);
        assertEquals(Method.PEGASUS, solver.getMethod());
    }
}