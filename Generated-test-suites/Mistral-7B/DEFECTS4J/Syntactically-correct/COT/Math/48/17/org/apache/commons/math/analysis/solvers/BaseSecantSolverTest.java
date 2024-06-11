package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** A simple test function. */
    private static final UnivariateRealFunction TEST_FUNCTION = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return Math.pow(x - 2.0, 2.0);
        }
    };

    /** A test function with a singularity at the root. */
    private static final UnivariateRealFunction TEST_FUNCTION_SINGULAR = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            if (x > 0) {
                return Math.pow(x - 1.0, 0.5);
            } else {
                throw new ArithmeticException("Cannot compute the square root of negative number.");
            }
        }
    };

    /** An instance of BaseSecantSolver with default parameters. */
    private BaseSecantSolver solver;

    /** An instance of BaseSecantSolver with custom parameters. */
    private BaseSecantSolver customSolver;

    /**
     * Set up before each test case.
     */
    @Before
    public void setUp() {
        solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.Method.REGULA_FALSI);
        customSolver = new BaseSecantSolver(TestConstants.EPSILON,
                BaseSecantSolver.Method.ILLINOIS);
    }

    /**
     * Test the constructor with default parameters.
     */
    @Test
    public void testDefaultConstructor() {
        assertNotNull(solver);
        assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test the constructor with custom parameters.
     */
    @Test
    public void testCustomConstructor() {
        assertNotNull(customSolver);
        assertEquals(TestConstants.EPSILON, customSolver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(BaseSecantSolver.Method.ILLINOIS, customSolver.getMethod());
    }

    /**
     * Test the solve method with a simple test function.
     * @throws ConvergenceException if the algorithm fails to converge.
     * @throws NoBracketingException if the method cannot determine bracketing.
     * @throws MaxIterationsExceededException if the algorithm exceeds the maximum number of iterations.
     * @throws MathIllegalArgumentException if the input function is null or contains a singularity in the interval.
     */
    @Test
    public void testSolveSimpleFunction()
            throws ConvergenceException, NoBracketingException,
            MaxIterationsExceededException, MathIllegalArgumentException {
        final double root = 2.0;
        final double expectedRoot = root;
        final double min = root - 1.0;
        final double max = root + 1.0;

        double solvedRoot = solver.solve(TEST_FUNCTION, min, max,
                BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(expectedRoot, solvedRoot, 1e-5);
    }

    /**
     * Test the solve method with a simple test function and custom parameters.
     * @throws ConvergenceException if the algorithm fails to converge.
     * @throws NoBracketingException if the method cannot determine bracketing.
     * @throws MaxIterationsExceededException if the algorithm exceeds the maximum number of iterations.
     * @throws MathIllegalArgumentException if the input function is null or contains a singularity in the interval.
     */
    @Test
    public void testSolveCustomFunction()
            throws ConvergenceException, NoBracketingException,
            MaxIterationsExceededException, MathIllegalArgumentException {
        final double root = 2.0;
        final double expectedRoot = root;
        final double min = root - 1.0;
        final double max = root + 1.0;

        double solvedRoot = customSolver.solve(TEST_FUNCTION, min, max,
                BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(expectedRoot, solvedRoot, 1e-5);
    }

    /**
     * Test the solve method with a test function having a singularity at the root.
     * @throws ConvergenceException if the algorithm fails to converge.
     * @throws MathIllegalArgumentException if the input function is null or contains a singularity in the interval.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveFunctionWithSingularity()
            throws ConvergenceException {
        final double min = 0.0;
        final double max = 1.0;

        solver.solve(TEST_FUNCTION_SINGULAR, min, max,
                BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }

    /**
     * Test the solve method with a test function having a singularity at the root and custom parameters.
     * @throws ConvergenceException if the algorithm fails to converge.
     * @throws MathIllegalArgumentException if the input function is null or contains a singularity in the interval.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveCustomFunctionWithSingularity()
            throws ConvergenceException {
        final double min = 0.0;
        final double max = 1.0;

        customSolver.solve(TEST_FUNCTION_SINGULAR, min, max,
                BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }

    /**
     * Test the allowed solution flag.
     */
    @Test
    public void testAllowedSolution() {
        // Test the default allowed solution flag.
        assertEquals(BaseSecantSolver.AllowedSolution.ANY_SIDE, solver.getAllowedSolution());

        // Test the custom allowed solution flag.
        customSolver.setAllowedSolution(BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(BaseSecantSolver.AllowedSolution.LEFT_SIDE, customSolver.getAllowedSolution());
    }
}