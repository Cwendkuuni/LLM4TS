package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math.analysis.solvers.IllinoisSolver;
import org.apache.commons.math.analysis.solvers.PegasusSolver;
import org.apache.commons.math.analysis.solvers.AllowedSolution;

/**
 * Tests for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** Function for testing. */
    private UnivariateFunction function;

    /** Regula Falsi Solver. */
    private BracketedUnivariateRealSolver<UnivariateFunction> solverRegulaFalsi;

    /** Illinois Solver. */
    private BracketedUnivariateRealSolver<UnivariateFunction> solverIllinois;

    /** Pegasus Solver. */
    private BracketedUnivariateRealSolver<UnivariateFunction> solverPegasus;

    /**
     * Sets up the test by creating a function and solvers.
     */
    @BeforeClass
    public static void setUpClass() {
        // No set up code for this test.
    }

    /**
     * Sets up the test by creating a function and solvers.
     * @throws UnsupportedOperationException
     */
    @Before
    public void setUp() throws UnsupportedOperationException {
        function = new PolynomialFunction(2.0, 3.0, 1.0);
        solverRegulaFalsi = new RegulaFalsiSolver(1e-6, 2.0);
        solverIllinois = new IllinoisSolver(1e-6, 2.0);
        solverPegasus = new PegasusSolver(1e-6, 1e-6, 1e-6, 2.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)}.
     */
    @Test
    public void testBaseSecantSolverConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(BaseSecantSolver.Method.ILLINOIS, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, Method)}.
     */
    @Test
    public void testBaseSecantSolverConstructorWithRelativeAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(0.01, 1e-6, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(0.01, solver.getRelativeAccuracy(), 1e-9);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(BaseSecantSolver.Method.ILLINOIS, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, Method)}.
     */
    @Test
    public void testBaseSecantSolverConstructorWithAllAccuracies() {
        BaseSecantSolver solver = new BaseSecantSolver(0.01, 1e-6, 1e-6, 1e-6, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(0.01, solver.getRelativeAccuracy(), 1e-9);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(1e-6, solver.getFunctionValueAccuracy(), 1e-9);
        assertEquals(BaseSecantSolver.Method.ILLINOIS, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}.
     */
    @Test
    public void testSolve() {
        double resultRegulaFalsi = solverRegulaFalsi.solve(function, 0.0, 2.0, 1.0);
        double resultIllinois = solverIllinois.solve(function, 0.0, 2.0, 1.0);
        double resultPegasus = solverPegasus.solve(function, 0.0, 2.0, 1.0);

        assertEquals(resultRegulaFalsi, -1.0, 1e-9);
        assertEquals(resultIllinois, -1.0, 1e-9);
        assertEquals(resultPegasus, -1.0, 1e-9);

        try {
            solverRegulaFalsi.solve(function, 0.0, 2.0, 2.0);
            assertTrue(false);
        } catch (MaxEvalException ex) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithAllowedSolution() {
        double resultRegulaFalsiLeft = solverRegulaFalsi.solve(function, 0.0, 2.0, 1.0, 1.0, AllowedSolution.LEFT_SIDE);
        double resultRegulaFalsiRight = solverRegulaFalsi.solve(function, 0.0, 2.0, 1.0, 2.0, AllowedSolution.RIGHT_SIDE);
        double resultIllinoisBelow = solverIllinois.solve(function, 0.0, 2.0, 1.0, -1.0, AllowedSolution.BELOW_SIDE);
        double resultIllinoisAbove = solverIllinois.solve(function, 0.0, 2.0, 1.0, 1.0, AllowedSolution.ABOVE_SIDE);

        assertEquals(-1.0, resultRegulaFalsiLeft, 1e-9);
        assertEquals(1.0, resultRegulaFalsiRight, 1e-9);
        assertEquals(-1.0, resultIllinoisBelow, 1e-9);
        assertEquals(1.0, resultIllinoisAbove, 1e-9);

        try {
            solverRegulaFalsi.solve(function, 0.0, 2.0, 2.0, 2.0, AllowedSolution.LEFT_SIDE);
            assertTrue(false);
        } catch (MaxEvalException ex) {
            // Expected
        }

        try {
            solverRegulaFalsi.solve(function, 0.0, 2.0, 1.0, 3.0, AllowedSolution.RIGHT_SIDE);
            assertTrue(false);
        } catch (NumberIsTooLargeException ex) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#verifyBracketing(double, double)}.
     */
    @Test
    public void testVerifyBracketing() {
        // Test with bracketing
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double x0 = 1.0, x1 = 2.0, x = 1.5;
        double f0 = function.value(x0), f1 = function.value(x1);
        assertTrue(solver.verifyBracketing(x0, x1));

        // Test without bracketing
        x = x0;
        f0 = function.value(x);
        assertFalse(solver.verifyBracketing(x0, x));
    }
}