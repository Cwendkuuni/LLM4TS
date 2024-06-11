package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RuntimeConfiguration;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.AllowedSolution;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver.Method;

/**
 * Test class for BaseSecantSolver.
 */
public class BaseSecantSolverTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        assertEquals("Expected Illinois method", Method.ILLINOIS, solver.getMethod());
    }

    /**
     * Test constructor with relative accuracy.
     */
    @Test
    public void testConstructorRelative() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-3, 1e-6, Method.PEGASUS);
        assertEquals("Expected Pegasus method", Method.PEGASUS, solver.getMethod());
    }

    /**
     * Test constructor with absolute and relative accuracy.
     */
    @Test
    public void testConstructorAbsoluteAndRelative() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-3, 1e-6, 1e-8, Method.REGULA_FALSI);
        assertEquals("Expected Regula Falsi method", Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test solve method with default values.
     */
    @Test
    public void testSolveDefault() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(Method.ILLINOIS);
        try {
            double root = solver.solve(10, f, -10, 10);
            fail("Expected IllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
        root = solver.solve(10, f, -5, 5);
        assertEquals("Expected root -1.41421356", -1.4142135624953853, Precision.round(root, 12), 1e-12);
    }

    /**
     * Test solve method with given accuracy.
     */
    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-7, Method.ILLINOIS);
        double root = solver.solve(10, f, -5, 5);
        assertEquals("Expected root -1.41421356", -1.4142135624953853, Precision.round(root, 12), 1e-12);
    }

    /**
     * Test solve method with given function value accuracy.
     */
    @Test
    public void testSolveWithFunctionValueAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, 1e-6, Method.PEGASUS);
        double root = solver.solve(10, f, -RuntimeConfiguration.longValue("math.PI") / 2, RuntimeConfiguration.longValue("math.PI") / 2);
        assertEquals("Expected root -1.41371587", -1.4137158708286645, Precision.round(root, 12), 1e-12);
    }

    /**
     * Test solve method with allowed solution.
     */
    @Test
    public void testSolveWithAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-3, Method.REGULA_FALSI);
        double root = solver.solve(10, f, -5, 5, AllowedSolution.LEFT_SIDE);
        assertEquals("Expected root -1.41421356", -1.4142135624953853, Precision.round(root, 12), 1e-12);
    }

    /**
     * Test solve method with allowed solution and inverted interval.
     */
    @Test
    public void testSolveWithAllowedSolutionAndInvertedInterval() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-3, Method.REGULA_FALSI);
        double root = solver.solve(10, f, 5, -5, AllowedSolution.RIGHT_SIDE);
        assertEquals("Expected root 1.41421356", 1.4142135624953853, Precision.round(root, 12), 1e-12);
    }

    /**
     * Test verifyBracketing method.
     */
    @Test
    public void testVerifyBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-3, Method.PEGASUS);
        try {
            solver.verifyBracketing(1, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        solver.verifyBracketing(1, 2);
        solver.verifyBracketing(0, 2);
        solver.verifyBracketing(-1, 2);
        solver.verifyBracketing(1, -2);
        solver.verifyBracketing(0, -2);
        solver.verifyBracketing(-1, -2);
    }
}