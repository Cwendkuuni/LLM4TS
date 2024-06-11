package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /** A test function for root finding. */
    private UnivariateRealFunction testFunction;

    /** A solver instance. */
    private BaseSecantSolver solver;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        solver = new RegulaFalsiSolver(1.0e-6, RegulaFalsiSolver.Method.REGULA_FALSI);
    }

    /**
     * Tests the solver with a simple function where the roots are known.
     */
    @Test
    public void testSimpleFunction() {
        double root = solver.solve(10, testFunction, -2.0, 2.0, 0.0);
        assertEquals(0.0, root, 1.0e-6);
    }

    /**
     * Tests the solver with a function that has a multiple root.
     */
    @Test
    public void testMultipleRoot() {
        double root = solver.solve(10, testFunction, -2.0, 2.0, 0.0);
        assertEquals(0.0, root, 1.0e-6);
        assertEquals(0.0, solver.solve(10, testFunction, -2.0, 2.0, root), 1.0e-6);
    }

    /**
     * Tests the solver with a function that does not bracket a root.
     */
    @Test(expected = MaxEvalException.class)
    public void testFunctionDoesNotBracketRoot() {
        solver = new RegulaFalsiSolver(1.0e-6, RegulaFalsiSolver.Method.REGULA_FALSI);
        solver.solve(10, testFunction, 0.0, 0.0, 0.0);
    }

    /**
     * Tests the solver with a function that has no roots in the given interval.
     */
    @Test
    public void testFunctionHasNoRoots() {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        solver = new RegulaFalsiSolver(1.0e-6, RegulaFalsiSolver.Method.REGULA_FALSI);
        solver.solve(10, testFunction, -3.0, 3.0, 0.0);
    }

    /**
     * Tests the solver with a function that has a root very close to the lower
     * bound.
     */
    @Test
    public void testRootVeryCloseToLowerBound() {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 0.01;
            }
        };
        solver = new RegulaFalsiSolver(1.0e-8, RegulaFalsiSolver.Method.REGULA_FALSI);
        double root = solver.solve(10, testFunction, -0.5, 0.5, 0.0);
        assertEquals(-0.5, root, 1.0e-8);
    }

    /**
     * Tests the solver with a function that has a very small root.
     */
    @Test
    public void testVerySmallRoot() {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1.0e-12;
            }
        };
        solver = new RegulaFalsiSolver(1.0e-18, RegulaFalsiSolver.Method.REGULA_FALSI);
        double root = solver.solve(10, testFunction, -1.0, 1.0, 0.0);
        assertTrue(Precision.isClose(0.0, root, 1.0e-18));
    }

    /**
     * Tests the solver with a function that has a root very close to the upper
     * bound.
     */
    @Test
    public void testRootVeryCloseToUpperBound() {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1.0;
            }
        };
        solver = new RegulaFalsiSolver(1.0e-6, RegulaFalsiSolver.Method.REGULA_FALSI);
        double root = solver.solve(10, testFunction, -1.0, 1.0, 0.0);
        assertEquals(0.0, root, 1.0e-6);
    }
}