package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for the BaseSecantSolver class.
 */
public class BaseSecantSolverTest {

    /**
     * Tests the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        assertNotNull(solver);
    }

    /**
     * Tests the constructor with absolute accuracy.
     */
    @Test
    public void testConstructorAbsoluteAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-9, BaseSecantSolver.Method.ILLINOIS);
        assertNotNull(solver);
    }

    /**
     * Tests the constructor with relative accuracy and absolute accuracy.
     */
    @Test
    public void testConstructorRelativeAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-4, 1e-9, BaseSecantSolver.Method.ILLINOIS);
        assertNotNull(solver);
    }

    /**
     * Tests the constructor with relative accuracy, absolute accuracy,
     * function value accuracy, and method.
     */
    @Test
    public void testConstructorWithAllArguments() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-4, 1e-9, 1e-6, BaseSecantSolver.Method.ILLINOIS);
        assertNotNull(solver);
    }

    /**
     * Tests the solve method with a simple function.
     */
    @Test
    public void testSolveSimpleFunction() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(10, f, -2, 2);
        assertEquals(0, root, 1e-5);
    }

    /**
     * Tests the solve method with a function that has no roots in the given interval.
     */
    @Test(expected = NoBracketingException.class)
    public void testSolveNoBracketing() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        solver.solve(10, f, -5, 5);
    }

    /**
     * Tests the solve method with a function that has multiple roots in the given interval.
     */
    @Test(expected = MaxEvalException.class)
    public void testSolveMultipleRoots() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x - 1;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        solver.solve(10, f, -1, 1);
    }

    /**
     * Tests the solve method with a function that is not defined for certain values in the interval.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveUndefinedFunction() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x < 0) {
                    throw new UnsupportedOperationException("Function not defined for negative x.");
                }
                return Math.pow(x, 0.5);
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        solver.solve(10, f, -1, 1);
    }

    /**
     * Tests the solve method with a function that is not monotonic in the interval.
     */
    @Test
    public void testSolveNonMonotonicFunction() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) * (x < 0 ? -1 : 1);
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(10, f, -1, 1);
        assertTrue(root < 0);
    }
}