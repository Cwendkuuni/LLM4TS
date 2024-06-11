package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.Method;

/**
 * Test cases for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test that the solver correctly finds a simple root.
     */
    @Test
    public void testSimpleRoot() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };
        final double absTol = 1e-9;
        final double relTol = 1e-9;
        final BaseSecantSolver solver = new BaseSecantSolver(relTol, absTol, Method.ILLINOIS);
        assertEquals("Solver should find square root of 2", 1.41421356237, solver.solve(100, f, -1.0, 2.0, 1.0), absTol);
    }

    /**
     * Test that the solver correctly handles a root at a boundary.
     */
    @Test
    public void testRootAtBoundary() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI / 2.0) * (x - 1.0);
            }
        };
        final double absTol = 1e-9;
        final double relTol = 1e-9;
        final BaseSecantSolver solver = new BaseSecantSolver(relTol, absTol, Method.ILLINOIS);
        assertEquals("Solver should find square root of -1", -1.0, solver.solve(100, f, -1.5, 1.5, 0.0), absTol);
    }

    /**
     * Test that the solver correctly handles a root between boundaries.
     */
    @Test
    public void testRootBetweenBoundaries() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return (x - 1.0) * (x - 3.0);
            }
        };
        final double absTol = 1e-9;
        final double relTol = 1e-9;
        final BaseSecantSolver solver = new BaseSecantSolver(relTol, absTol, Method.ILLINOIS);
        assertEquals("Solver should find root between boundaries", 2.0, solver.solve(100, f, 0.0, 4.0, 1.5), absTol);
    }

    /**
     * Test that the solver correctly handles a function with a root at an
     * endpoint.
     */
    @Test
    public void testRootAtEndpoint() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.cos(x) - 1.0;
            }
        };
        final double absTol = 1e-9;
        final double relTol = 1e-9;
        final BaseSecantSolver solver = new BaseSecantSolver(relTol, absTol, Method.ILLINOIS);
        try {
            solver.solve(100, f, -Math.PI, Math.PI, 0.0);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }
        assertEquals("Solver should find root at an endpoint", 0.0, solver.solve(100, f, 0.0, 0.0, 0.0), absTol);
    }

    /**
     * Test that the solver correctly handles a function with no roots.
     */
    @Test
    public void testNoRoots() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        final double absTol = 1e-9;
        final double relTol = 1e-9;
        final BaseSecantSolver solver = new BaseSecantSolver(relTol, absTol, Method.ILLINOIS);
        try {
            solver.solve(100, f, -10.0, 10.0, 0.0);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }
    }

    /**
     * Test that the solver correctly handles a function that always returns
     * NaN.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testNaNFunction() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Double.NaN;
            }
        };
        final double absTol = 1e-9;
        final double relTol = 1e-9;
        final BaseSecantSolver solver = new BaseSecantSolver(relTol, absTol, Method.ILLINOIS);
        solver.solve(100, f, -10.0, 10.0, 0.0);
    }

    /**
     * Test that the solver correctly handles a function that throws an
     * exception.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testExceptionFunction() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        final double absTol = 1e-9;
        final double relTol = 1e-9;
        final BaseSecantSolver solver = new BaseSecantSolver(relTol, absTol, Method.ILLINOIS);
        solver.solve(100, f, -10.0, 10.0, 0.0);
    }

    /**
     * Test that the solver correctly handles a function with very large roots.
     */
    @Test
    public void testLargeRoots() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 100.0;
            }
        };
        final double absTol = 1e12;
        final double relTol = 1e-12;
        final BaseSecantSolver solver = new BaseSecantSolver(relTol, absTol, Method.ILLINOIS);
        assertEquals("Solver should find very large root", 10.0, solver.solve(100, f, -50.0, 50.0, 0.0), Precision.epsmax(1.0));
    }

    /**
     * Test that the solver correctly handles a function with roots of very
     * different signs.
     */
    @Test
    public void testRootsOfDifferentSigns() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        final double absTol = 1e-9;
        final double relTol = 1e-9;
        final BaseSecantSolver solver = new BaseSecantSolver(relTol, absTol, Method.ILLINOIS);
        assertEquals("Solver should find negative root", -2.0, solver.solve(100, f, -5.0, 5.0, 0.0), absTol);
        assertEquals("Solver should find positive root", 2.0, solver.solve(100, f, -5.0, 5.0, 0.0), absTol);
    }

    /**
     * Test that the solver correctly handles a function with roots at infinity.
     */
    @Test
    public void testRootsAtInfinity() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1.0 / x;
            }
        };
        final double absTol = 1e-9;
        final double relTol = 1e-9;
        final BaseSecantSolver solver = new BaseSecantSolver(relTol, absTol, Method.ILLINOIS);
        assertEquals("Solver should find negative root at infinity", Double.NEGATIVE_INFINITY, solver.solve(100, f, -1000.0, 1000.0, -1000.0), Double.NEGATIVE_INFINITY);
        assertEquals("Solver should find positive root at infinity", Double.POSITIVE_INFINITY, solver.solve(100, f, -1000.0, 1000.0, 1000.0), Double.POSITIVE_INFINITY);
    }
}