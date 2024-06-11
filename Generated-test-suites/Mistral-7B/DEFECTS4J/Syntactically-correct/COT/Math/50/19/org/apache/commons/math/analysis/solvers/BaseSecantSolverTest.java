package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketException;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.AllowedSolution;
import org.apache.commons.math.analysis.solvers.Methods;

/**
 * JUnit test file for the BaseSecantSolver class.
 */
public class BaseSecantSolverTest {

    private BracketedUnivariateRealSolver<UnivariateFunction> solver;

    @Before
    public void setUp() {
        double absoluteAccuracy = 1e-6;
        double relativeAccuracy = 1e-4;
        double functionValueAccuracy = 1e-6;
        solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy,  functionValueAccuracy, Methods.REGULA_FALSI);
    }

    /**
     * Test solver with a simple function.
     */
    @Test
    public void testSimpleFunction() throws MaxEvalException, NoBracketException {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double root = solver.solve(50, f, -2.5, 2.5, 0.0);
        assertEquals("Wrong root found", 0.0, root, 1e-6);
    }

    /**
     * Test solver with a function that has a root at the minimum bound.
     */
    @Test
    public void testMinimumBoundRoot() throws MaxEvalException, NoBracketException {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        solver = new BaseSecantSolver(Methods.ILLINOIS);
        try {
            solver.solve(50, f, -2.0, 2.0, 0.0);
            fail("Expected NoBracketException");
        } catch (NoBracketException e) {
            // expected
        }
    }

    /**
     * Test solver with a function that has a root at the maximum bound.
     */
    @Test
    public void testMaximumBoundRoot() throws MaxEvalException, NoBracketException {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        solver = new BaseSecantSolver(Methods.ILLINOIS);
        try {
            solver.solve(50, f, -2.0, 2.0, 2.0);
            fail("Expected NoBracketException");
        } catch (NoBracketException e) {
            // expected
        }
    }

    /**
     * Test solver with a function that has multiple roots.
     */
    @Test
    public void testMultipleRoots() throws MaxEvalException, NoBracketException {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return (x - 1) * (x - 3);
            }
        };
        double root1 = solver.solve(50, f, -3.0, 5.0, 0.0);
        assertEquals("Wrong root found", 1.0, root1, 1e-6);

        solver.reset();
        double root2 = solver.solve(50, f, -3.0, 5.0, 3.0);
        assertEquals("Wrong root found", 3.0, root2, 1e-6);
    }

    /**
     * Test solver with a function that has a vertical asymptote.
     */
    @Test
    public void testVerticalAsymptote() throws MaxEvalException, NoBracketException {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 1 / (x - 2);
            }
        };
        solver = new BaseSecantSolver(Methods.PEGASUS);
        try {
            solver.solve(50, f, -3.0, 3.0, 2.0);
            fail("Expected MaxEvalException");
        } catch (MaxEvalException e) {
            // expected
        }
    }

    /**
     * Test solver with a function that has an infinite root.
     */
    @Test
    public void testInfiniteRoot() throws MaxEvalException, NoBracketException {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * Math.log(x) - 1;
            }
        };
        solver = new BaseSecantSolver(Methods.PEGASUS);
        try {
            solver.solve(50, f, 0.0, Double.POSITIVE_INFINITY, 1.0);
            fail("Expected MaxEvalException");
        } catch (MaxEvalException e) {
            // expected
        }
    }

    /**
     * Test solver with a function that has a root at the midpoint of the interval.
     */
    @Test
    public void testMidpointRoot() throws MaxEvalException, NoBracketException {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double root = solver.solve(50, f, -1.5, 1.5, 0.0);
        assertEquals("Wrong root found", 0.0, root, 1e-6);
    }

    /**
     * Test solver with a function that has a very large root.
     */
    @Test
    public void testLargeRoot() throws MaxEvalException, NoBracketException {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x * x - 100000;
            }
        };
        double root = solver.solve(50, f, -1000, 1000, 0.0);
        assertEquals("Wrong root found", 1000, root, 1e-6);
    }

    /**
     * Test solver with a function that has a very small root.
     */
    @Test
    public void testSmallRoot() throws MaxEvalException, NoBracketException {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x * x - 1;
            }
        };
        double root = solver.solve(50, f, -1, 1, 0.0);
        assertEquals("Wrong root found", 0, root, 1e-12);
    }

    /**
     * Test solver with a function that has a root between the minimum and maximum bounds.
     */
    @Test
    public void testRootInInterval() throws MaxEvalException, NoBracketException {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double root = solver.solve(50, f, -2.5, 2.5, 0.0);
        assertEquals("Wrong root found", 0.0, root, 1e-6);
    }
}