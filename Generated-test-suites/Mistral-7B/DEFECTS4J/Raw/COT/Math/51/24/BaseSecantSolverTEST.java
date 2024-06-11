package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * JUnit test cases for the BaseSecantSolver class.
 */
public class BaseSecantSolverTest {

    /** A simple polynomial function for testing. */
    private static final UnivariateFunction FUNCTION = new PolynomialFunction(x -> x * x - 4.0);

    /**
     * Test constructor with default arguments.
     */
    @Test
    public void testDefaultConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        assertNotNull(solver);
    }

    /**
     * Test constructor with custom arguments.
     */
    @Test
    public void testCustomConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-8, BaseSecantSolver.Method.ILLINOIS);
        assertNotNull(solver);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}
     * with custom method and no bracketing.
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        solver.solve(10, FUNCTION, -1.0, 1.0);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}
     * with custom method and exact root.
     */
    @Test
    public void testExactRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        double solution = solver.solve(10, FUNCTION, -1.0, 1.0);
        assertEquals(0.0, solution, 1e-11);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}
     * with custom method and inexact root.
     */
    @Test
    public void testInexactRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        double solution = solver.solve(10, FUNCTION, -1.0, 1.0);
        double exactSolution = -2.0;
        assertEquals(exactSolution, solution, 1e-8);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}
     * with custom method and allowed solution on the left side.
     */
    @Test
    public void testAllowedSolutionLeft() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS,
                                                      AllowedSolution.LEFT_SIDE);
        double solution = solver.solve(10, FUNCTION, -1.0, 1.0);
        double exactSolution = -2.0;
        assertEquals(exactSolution, solution, 1e-8);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}
     * with custom method and allowed solution on the right side.
     */
    @Test
    public void testAllowedSolutionRight() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS,
                                                      AllowedSolution.RIGHT_SIDE);
        double solution = solver.solve(10, FUNCTION, -1.0, 1.0);
        double exactSolution = -2.0;
        assertEquals(exactSolution, solution, 1e-8);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}
     * with custom method and allowed solution below the root.
     */
    @Test
    public void testAllowedSolutionBelow() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS,
                                                      AllowedSolution.BELOW_SIDE);
        double solution = solver.solve(10, FUNCTION, -1.0, 1.0);
        double exactSolution = -2.0;
        assertEquals(exactSolution, solution, 1e-8);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}
     * with custom method and allowed solution above the root.
     */
    @Test
    public void testAllowedSolutionAbove() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS,
                                                      AllowedSolution.ABOVE_SIDE);
        double solution = solver.solve(10, FUNCTION, -1.0, 1.0);
        assertNull(solution);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}
     * with RegulaFalsi method.
     */
    @Test
    public void testRegulaFalsi() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        double solution = solver.solve(10, FUNCTION, -1.0, 1.0);
        double exactSolution = -2.0;
        assertEquals(exactSolution, solution, 1e-8);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}
     * with Illinois method.
     */
    @Test
    public void testIllinois() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        double solution = solver.solve(10, FUNCTION, -1.0, 1.0);
        double exactSolution = -2.0;
        assertEquals(exactSolution, solution, 1e-8);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}
     * with Pegasus method.
     */
    @Test
    public void testPegasus() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        double solution = solver.solve(10, FUNCTION, -1.0, 1.0);
        double exactSolution = -2.0;
        assertEquals(exactSolution, solution, 1e-8);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}
     * with maxEval exception.
     */
    @Test(expected = MaxEvalException.class)
    public void testMaxEvalException() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        solver.solve(0, FUNCTION, -1.0, 1.0);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}
     * with unsupported operation exception.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperationException() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        solver.solve(10, FUNCTION, -1.0, 1.0);
    }
}

/**
 * Allowed solution types.
 */
enum AllowedSolution {

    /** Allowed solutions on either side. */
    ANY_SIDE,

    /** Allowed solutions only on the left side. */
    LEFT_SIDE,

    /** Allowed solutions only on the right side. */
    RIGHT_SIDE,

    /** Allowed solutions below the root. */
    BELOW_SIDE,

    /** Allowed solutions above the root. */
    ABOVE_SIDE;
}