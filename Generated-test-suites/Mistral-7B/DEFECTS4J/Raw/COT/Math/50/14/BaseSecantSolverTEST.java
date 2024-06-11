package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.RootFindFailureException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.util.Precision;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for BaseSecantSolver.
 */
public class BaseSecantSolverTest {

    /**
     * Tested instance.
     */
    private BaseSecantSolver solver;

    /**
     * Tested function.
     */
    private UnivariateFunction function;

    /**
     * Sets up each test.
     */
    @Before
    public void setUp() {
        solver = new BaseSecantSolver.IllinoisSolver(1e-4, new IllinoisSolver(1e-4, 1000));
        function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{0.0});
    }

    /**
     * Tears down each test.
     */
    @After
    public void tearDown() {
        solver = null;
        function = null;
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)}.
     */
    @Test
    public void testBaseSecantSolverConstructor() {
        assertEquals("Default constructor with default method.", BaseSecantSolver.Method.REGULA_FALSI,
                new BaseSecantSolver().getMethod());
        assertEquals("Constructor with default accuracy and Illinois method.", BaseSecantSolver.Method.ILLINOIS,
                new BaseSecantSolver(1e-4, BaseSecantSolver.Method.ILLINOIS).getMethod());
        assertEquals("Constructor with absolute and relative accuracies and Illinois method.", BaseSecantSolver.Method.ILLINOIS,
                new BaseSecantSolver(1e-4, 1e-6, 1e-4, BaseSecantSolver.Method.ILLINOIS).getMethod());
        assertEquals("Constructor with absolute, relative, and function value accuracies and Illinois method.", BaseSecantSolver.Method.ILLINOIS,
                new BaseSecantSolver(1e-4, 1e-6, 1e-8, BaseSecantSolver.Method.ILLINOIS).getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, Method)}.
     */
    @Test
    public void testBaseSecantSolverConstructorWithAccuracy() {
        assertEquals("Constructor with default method and given accuracy.", BaseSecantSolver.Method.REGULA_FALSI,
                new BaseSecantSolver(1e-4).getMethod());
        assertEquals("Constructor with Illinois method and given accuracy.", BaseSecantSolver.Method.ILLINOIS,
                new BaseSecantSolver(1e-4, BaseSecantSolver.Method.ILLINOIS).getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, Method)}.
     */
    @Test
    public void testBaseSecantSolverConstructorWithAllAccuracies() {
        assertEquals("Constructor with Illinois method and given accuracies.", BaseSecantSolver.Method.ILLINOIS,
                new BaseSecantSolver(1e-4, 1e-6, 1e-8, BaseSecantSolver.Method.ILLINOIS).getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#getMethod()}.
     */
    @Test
    public void testGetMethod() {
        assertEquals("Get method for default constructor.", BaseSecantSolver.Method.REGULA_FALSI,
                new BaseSecantSolver().getMethod());
        assertEquals("Get method for constructor with Illinois method.", BaseSecantSolver.Method.ILLINOIS,
                new BaseSecantSolver(1e-4, BaseSecantSolver.Method.ILLINOIS).getMethod());
        assertEquals("Get method for constructor with absolute, relative, and function value accuracies and Illinois method.", BaseSecantSolver.Method.ILLINOIS,
                new BaseSecantSolver(1e-4, 1e-6, 1e-8, BaseSecantSolver.Method.ILLINOIS).getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     *
     * @throws MaxEvalException       max evaluation exception
     * @throws RootFindFailureException root find failure exception
     * @throws TooManyIterationsException too many iterations exception
     */
    @Test
    public void testSolve() throws MaxEvalException, RootFindFailureException, TooManyIterationsException {
        double expected = 0.0;

        // Test with the default function.
        assertEquals("Solve using the default function and default bounds.", expected,
                solver.solve(100, function, -1.0, 1.0), Precision.SAFE_EPSILON);

        // Test with the default function and given bounds.
        assertEquals("Solve using the default function and given bounds.", expected,
                solver.solve(100, function, -0.5, 0.5), Precision.SAFE_EPSILON);

        // Test with the default function, given bounds, and given starting point.
        assertEquals("Solve using the default function, given bounds, and given starting point.", expected,
                solver.solve(100, function, -0.5, 0.5, -0.1), Precision.SAFE_EPSILON);

        // Test with a custom function.
        UnivariateFunction customFunction = new PolynomialFunction(new double[]{-1.0, 1.0, 2.0}, new double[]{0.0});
        assertEquals("Solve using a custom function and default bounds.", expected,
                solver.solve(100, customFunction, -1.0, 1.0), Precision.SAFE_EPSILON);

        // Test with a custom function and given bounds.
        assertEquals("Solve using a custom function and given bounds.", expected,
                solver.solve(100, customFunction, -0.5, 0.5), Precision.SAFE_EPSILON);

        // Test with a custom function, given bounds, and given starting point.
        assertEquals("Solve using a custom function, given bounds, and given starting point.", expected,
                solver.solve(100, customFunction, -0.5, 0.5, -0.1), Precision.SAFE_EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}.
     *
     * @throws MaxEvalException       max evaluation exception
     * @throws RootFindFailureException root find failure exception
     * @throws TooManyIterationsException too many iterations exception
     */
    @Test
    public void testSolveWithBracketing() throws MaxEvalException, RootFindFailureException, TooManyIterationsException {
        double expected = 0.0;

        // Test with the default function and default bounds.
        assertEquals("Solve using the default function and default bounds with bracketing.", expected,
                solver.solve(100, function, -1.0, 1.0), Precision.SAFE_EPSILON);

        // Test with the default function and given bounds.
        assertEquals("Solve using the default function and given bounds with bracketing.", expected,
                solver.solve(100, function, -0.5, 0.5), Precision.SAFE_EPSILON);

        // Test with a custom function and default bounds.
        UnivariateFunction customFunction = new PolynomialFunction(new double[]{-1.0, 1.0, 2.0}, new double[]{0.0});
        assertEquals("Solve using a custom function and default bounds with bracketing.", expected,
                solver.solve(100, customFunction, -1.0, 1.0), Precision.SAFE_EPSILON);

        // Test with a custom function and given bounds.
        assertEquals("Solve using a custom function and given bounds with bracketing.", expected,
                solver.solve(100, customFunction, -0.5, 0.5), Precision.SAFE_EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     *
     * @throws MaxEvalException       max evaluation exception
     * @throws RootFindFailureException root find failure exception
     * @throws TooManyIterationsException too many iterations exception
     */
    @Test
    public void testSolveWithStartingValue() throws MaxEvalException, RootFindFailureException, TooManyIterationsException {
        double expected = 0.0;

        // Test with the default function and default bounds.
        assertEquals("Solve using the default function, default bounds, and given starting value.", expected,
                solver.solve(100, function, -1.0, 1.0, -0.1), Precision.SAFE_EPSILON);

        // Test with the default function and given bounds.
        assertEquals("Solve using the default function, given bounds, and given starting value.", expected,
                solver.solve(100, function, -0.5, 0.5, -0.1), Precision.SAFE_EPSILON);

        // Test with a custom function and default bounds.
        UnivariateFunction customFunction = new PolynomialFunction(new double[]{-1.0, 1.0, 2.0}, new double[]{0.0});
        assertEquals("Solve using a custom function, default bounds, and given starting value.", expected,
                solver.solve(100, customFunction, -1.0, 1.0, -0.1), Precision.SAFE_EPSILON);

        // Test with a custom function and given bounds.
        assertEquals("Solve using a custom function, given bounds, and given starting value.", expected,
                solver.solve(100, customFunction, -0.5, 0.5, -0.1), Precision.SAFE_EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}.
     *
     * @throws MaxEvalException       max evaluation exception
     * @throws RootFindFailureException root find failure exception
     * @throws TooManyIterationsException too many iterations exception
     */
    @Test(expected = RootFindFailureException.class)
    public void testFailedSolve() throws MaxEvalException, RootFindFailureException, TooManyIterationsException {
        UnivariateFunction customFunction = new PolynomialFunction(new double[]{1.0, -1.0}, new double[]{0.0});
        solver.solve(100, customFunction, -1.0, 1.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     *
     * @throws MaxEvalException       max evaluation exception
     * @throws RootFindFailureException root find failure exception
     * @throws TooManyIterationsException too many iterations exception
     */
    @Test(expected = RootFindFailureException.class)
    public void testFailedSolveWithBracketing() throws MaxEvalException, RootFindFailureException, TooManyIterationsException {
        UnivariateFunction customFunction = new PolynomialFunction(new double[]{1.0, -1.0}, new double[]{0.0});
        solver.solve(100, customFunction, -1.0, 1.0, -0.1);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double)}.
     *
     * @throws MaxEvalException       max evaluation exception
     * @throws RootFindFailureException root find failure exception
     * @throws TooManyIterationsException too many iterations exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullFunction() throws MaxEvalException, RootFindFailureException, TooManyIterationsException {
        solver.solve(100, null, -1.0, 1.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}.
     *
     * @throws MaxEvalException       max evaluation exception
     * @throws RootFindFailureException root find failure exception
     * @throws TooManyIterationsException too many iterations exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullStartingValue() throws MaxEvalException, RootFindFailureException, TooManyIterationsException {
        solver.solve(100, function, null, -0.5, 0.5);
    }

    /**
     * Test method for {@link BaseSecantSolver#getAllowedSolution()}.
     */
    @Test
    public void testGetAllowedSolution() {
        assertEquals("Get allowed solution for default constructor.", AllowedSolution.ANY_SIDE, solver.getAllowedSolution());
        assertEquals("Get allowed solution for constructor with Illinois method.", AllowedSolution.ANY_SIDE,
                new BaseSecantSolver(1e-4, BaseSecantSolver.Method.ILLINOIS).getAllowedSolution());
        assertEquals("Get allowed solution for constructor with absolute, relative, and function value accuracies and Illinois method.", AllowedSolution.ANY_SIDE,
                new BaseSecantSolver(1e-4, 1e-6, 1e-8, BaseSecantSolver.Method.ILLINOIS).getAllowedSolution());
    }

    /**
     * Test method for {@link BaseSecantSolver#setAllowedSolution(AllowedSolution)}.
     */
    @Test
    public void testSetAllowedSolution() {
        solver.setAllowedSolution(AllowedSolution.LEFT_SIDE);
        assertEquals("Get allowed solution after setting.", AllowedSolution.LEFT_SIDE, solver.getAllowedSolution());
    }

    /**
     * Test method for {@link BaseSecantSolver#computeObjectiveValue(double)}.
     */
    @Test
    public void testComputeObjectiveValue() {
        double x = 0.5;
        assertEquals("Compute objective value for default function and given point.", FunctionUtils.value(function, x),
                solver.computeObjectiveValue(x), Precision.SAFE_EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#verifyBracketing(double, double)}.
     */
    @Test
    public void testVerifyBracketing() {
        double x0 = -0.5;
        double x1 = 0.5;
        assertTrue("Verify bracketing for points x0 < x1.", solver.verifyBracketing(x0, x1));

        double x2 = 0.5;
        assertFalse("Verify bracketing for points x0 > x1.", solver.verifyBracketing(x1, x2));
    }
}