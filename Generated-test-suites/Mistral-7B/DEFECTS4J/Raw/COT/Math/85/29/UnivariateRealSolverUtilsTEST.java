package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.exceptions.MaxIterationsExceededException;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.BracketedBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolverFactory;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for {@link UnivariateRealSolverUtils} class.
 */
public class UnivariateRealSolverUtilsTest {

    /** Holder for the factory instance. */
    private static UnivariateRealSolverFactory factory;

    /** Function for testing. */
    private static UnivariateFunction function;

    /**
     * Initializes the test by getting the solver factory instance.
     */
    @BeforeClass
    public static void setUp() {
        factory = UnivariateRealSolverUtils.LazyHolder.FACTORY;
        function = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);
    }

    /**
     * Tests {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * method with default solver.
     */
    @Test
    public void testSolveDefaultSolver() {
        double root = UnivariateRealSolverUtils.solve(function, -2.0, 2.0);
        assertEquals(-1.0, root, Precision.EPSILON);
    }

    /**
     * Tests {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * method with default solver and absolute accuracy.
     */
    @Test
    public void testSolveDefaultSolverWithAbsoluteAccuracy() {
        double root = UnivariateRealSolverUtils.solve(function, -2.0, 2.0, 1.0e-6);
        assertEquals(-1.0, root, 1.0e-7);
    }

    /**
     * Tests {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * method with custom solver (BrentSolver).
     */
    @Test
    public void testSolveCustomSolver() {
        UnivariateRealSolver solver = new BrentSolver();
        double root = UnivariateRealSolverUtils.solve(function, -2.0, 2.0, solver);
        assertEquals(-1.0, root, Precision.EPSILON);
    }

    /**
     * Tests {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)}
     * method with default solver.
     */
    @Test
    public void testBracketDefaultSolver() throws ConvergenceException, FunctionEvaluationException {
        double[] brackets = UnivariateRealSolverUtils.bracket(function, -2.0, -1.0, 1.0);
        assertEquals(-1.0, brackets[0], Precision.EPSILON);
        assertEquals(1.0, brackets[1], Precision.EPSILON);
    }

    /**
     * Tests {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * method with default solver and maximum iterations.
     */
    @Test
    public void testBracketDefaultSolverWithMaxIterations() throws ConvergenceException, FunctionEvaluationException {
        double[] brackets = UnivariateRealSolverUtils.bracket(function, -2.0, -1.0, 1.0, 5);
        assertEquals(-1.0, brackets[0], Precision.EPSILON);
        assertEquals(1.0, brackets[1], Precision.EPSILON);
    }

    /**
     * Tests {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * method with custom solver (BracketedBrentSolver) and maximum iterations.
     */
    @Test
    public void testBracketCustomSolverWithMaxIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolver solver = new BracketedBrentSolver();
        double[] brackets = UnivariateRealSolverUtils.bracket(function, -2.0, -1.0, 1.0, 5, solver);
        assertEquals(-1.0, brackets[0], Precision.EPSILON);
        assertEquals(1.0, brackets[1], Precision.EPSILON);
    }

    /**
     * Tests {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * method with invalid arguments.
     */
    @Test
    public void testInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(null, 0.0, 0.0));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(function, 0.0, 0.0));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(function, 0.0, 0.0, -1.0));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(function, 0.0, 2.0, 1.0, -1));
    }

    /**
     * Tests {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)}
     * method with invalid arguments.
     */
    @Test
    public void testBracketInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(null, 0.0, 0.0));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(function, 0.0, 0.0));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(function, 0.0, 2.0, -1.0));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(function, 0.0, 2.0, 2.0, -1));
    }

    /**
     * Tests {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * method with invalid arguments.
     */
    @Test
    public void testBracketWithMaxIterationsInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(null, 0.0, 0.0, 5));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(function, 0.0, 0.0, 5));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(function, 0.0, 2.0, -1.0, 5));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(function, 0.0, 2.0, 2.0, -1));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(function, 0.0, 2.0, 5, -1));
    }

    /**
     * Tests {@link UnivariateRealSolverUtils#midpoint(double, double)}
     * method with valid and invalid arguments.
     */
    @Test
    public void testMidpoint() {
        assertEquals(0.5, UnivariateRealSolverUtils.midpoint(1.0, 2.0), Precision.EPSILON);
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.midpoint(0.0, 0.0));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.midpoint(1.0, 1.0));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.midpoint(-0.5, 0.5));
    }
}