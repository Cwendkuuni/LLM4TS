package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** The minimum value for testing. */
    private static final double MIN = 0.0;

    /** The maximum value for testing. */
    private static final double MAX = 1.0;

    /** The function to be tested. */
    private UnivariateRealFunction function;

    /** The solver under test. */
    private BaseSecantSolver solver;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        function = mock(UnivariateRealFunction.class);
        solver = new RegulaFalsiSolver(1.0E-6, new RegulaFalsiSolver.RegulaFalsiMethod());
    }

    /**
     * Test constructor with no arguments.
     */
    @Test
    public void testConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        assertNotNull(solver);
    }

    /**
     * Test constructor with absolute accuracy.
     */
    @Test
    public void testConstructorWithAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, BaseSecantSolver.Method.REGULA_FALSI);
        assertNotNull(solver);
    }

    /**
     * Test constructor with relative and absolute accuracies.
     */
    @Test
    public void testConstructorWithAccuracies() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, 1.0E-6, BaseSecantSolver.Method.REGULA_FALSI);
        assertNotNull(solver);
    }

    /**
     * Test constructor with relative, absolute accuracies, and function value
     * accuracy.
     */
    @Test
    public void testConstructorWithAllAccuracies() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0E-6, 1.0E-6, 1.0E-6, BaseSecantSolver.Method.REGULA_FALSI);
        assertNotNull(solver);
    }

    /**
     * Test solver with a simple function.
     */
    @Test
    public void testSolverWithSimpleFunction() throws MaxEvalException, NumberIsTooSmallException {
        when(function.value(MIN)).thenReturn(1.0);
        when(function.value(MAX)).thenReturn(-1.0);

        double solution = solver.solve(1, function, MIN, MAX, MIN + 0.5 * (MAX - MIN));
        assertEquals(0.5, solution, 1.0E-6);
    }

    /**
     * Test solver with a function having roots on the left side.
     */
    @Test
    public void testSolverWithFunctionLeftRoot() throws MaxEvalException, NumberIsTooSmallException {
        when(function.value(MIN)).thenReturn(1.0);
        when(function.value(MAX)).thenReturn(-1.0);

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        double solution = solver.solve(1, function, MIN, MAX, MIN + 0.5 * (MAX - MIN));
        assertEquals(MIN, solution, 1.0E-6);
    }

    /**
     * Test solver with a function having roots on the right side.
     */
    @Test
    public void testSolverWithFunctionRightRoot() throws MaxEvalException, NumberIsTooSmallException {
        when(function.value(MIN)).thenReturn(-1.0);
        when(function.value(MAX)).thenReturn(1.0);

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        double solution = solver.solve(1, function, MIN, MAX, MIN + 0.5 * (MAX - MIN));
        assertEquals(MAX, solution, 1.0E-6);
    }

    /**
     * Test solver with a function that has a root below the search interval.
     */
    @Test
    public void testSolverWithFunctionBelowRoot() throws MaxEvalException, NumberIsTooSmallException {
        when(function.value(MIN)).thenReturn(1.0);
        when(function.value(MAX)).thenReturn(-1.0);

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        double solution = solver.solve(1, function, MIN, MAX, MIN + 0.5 * (MAX - MIN));
        assertEquals(PrecisionUtils.round(Precision.round(MIN + 0.25), 6), solution, 1.0E-6);
    }

    /**
     * Test solver with a function that has a root above the search interval.
     */
    @Test
    public void testSolverWithFunctionAboveRoot() throws MaxEvalException, NumberIsTooSmallException {
        when(function.value(MIN)).thenReturn(-1.0);
        when(function.value(MAX)).thenReturn(1.0);

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        double solution = solver.solve(1, function, MIN, MAX, MIN + 0.5 * (MAX - MIN));
        assertEquals(PrecisionUtils.round(Precision.round(MIN + 0.75), 6), solution, 1.0E-6);
    }

    /**
     * Test solver with a function that doesn't bracket the root.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolverWithFunctionNotBracketing() throws MaxEvalException, NumberIsTooSmallException {
        when(function.value(MIN)).thenReturn(1.0);
        when(function.value(MAX)).thenReturn(1.0);

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(1, function, MIN, MAX, MIN + 0.5 * (MAX - MIN));
    }

    /**
     * Test solver with a function that has roots at the endpoints.
     */
    @Test
    public void testSolverWithFunctionRootsAtEndpoints() throws MaxEvalException, NumberIsTooSmallException {
        when(function.value(MIN)).thenReturn(0.0);
        when(function.value(MAX)).thenReturn(0.0);

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        double solution = solver.solve(1, function, MIN, MAX, MIN + 0.5 * (MAX - MIN));
        assertEquals(MIN, solution, 1.0E-6);
    }

    /**
     * Test solver with a function that has roots at the midpoint.
     */
    @Test
    public void testSolverWithFunctionRootsAtMidpoint() throws MaxEvalException, NumberIsTooSmallException {
        when(function.value(MIN)).thenReturn(1.0);
        when(function.value(MAX)).thenReturn(1.0);

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        solver.setMaxEval(10000);
        double solution = solver.solve(1, function, MIN, MAX, MIN + 0.5 * (MAX - MIN));
        assertEquals(PrecisionUtils.round(MIN + 0.5, 6), solution, 1.0E-6);
    }

    /**
     * Test solver with a function that has roots at the midpoint with different
     * accuracies.
     */
    @Test
    public void testSolverWithFunctionRootsAtMidpointWithDifferentAccuracies()
            throws MaxEvalException, NumberIsTooSmallException {
        when(function.value(MIN)).thenReturn(1.0);
        when(function.value(MAX)).thenReturn(1.0);

        BaseSecantSolver solver = new BaseSecantSolver(1.0E-8, BaseSecantSolver.Method.REGULA_FALSI);
        solver.setMaxEval(10000);
        double solution = solver.solve(1, function, MIN, MAX, MIN + 0.5 * (MAX - MIN));
        assertEquals(PrecisionUtils.round(MIN + 0.5, 8), solution, 1.0E-8);
    }
}