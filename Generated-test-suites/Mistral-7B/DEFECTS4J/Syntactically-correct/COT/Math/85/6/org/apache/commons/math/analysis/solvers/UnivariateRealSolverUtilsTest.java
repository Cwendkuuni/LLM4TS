package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.solvers.BrentSolver;
import org.apache.commons.math3.solvers.LBFGSBrentSolver;
import org.apache.commons.math3.solvers.UnivariateSolver;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test for UnivariateRealSolverUtils class.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Initialize the solvers.
     */
    @BeforeClass
    public static void setUp() {
        LazyHolder.FACTORY.setDefaultSolver(new BrentSolver());
        LazyHolder.FACTORY.setConvergenceThreshold(1.0e-10);
    }

    /**
     * Test for solve method with a valid function and interval.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolve() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);
        double root = UnivariateRealSolverUtils.solve(function, -2.0, 2.0);
        assertEquals(0.0, root, 1e-8);
    }

    /**
     * Test for solve method with a function that has no roots in the interval.
     * @throws Exception if an error occurs
     */
    @Test(expected = NoRootsException.class)
    public void testSolveNoRoots() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) + 1.0);
        UnivariateRealSolverUtils.solve(function, -1.0, 1.0);
    }

    /**
     * Test for solve method with a function that throws an exception during evaluation.
     * @throws Exception if an error occurs
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testSolveException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("An error occurred");
            }
        };
        UnivariateRealSolverUtils.solve(function, 0.0, 0.0);
    }

    /**
     * Test for solve method with an interval that is not valid.
     * @throws Exception if an error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidInterval() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1.0);
        UnivariateRealSolverUtils.solve(function, 2.0, 1.0);
    }

    /**
     * Test for solve method with a specified accuracy.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolveSpecifiedAccuracy() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);
        double root = UnivariateRealSolverUtils.solve(function, -2.0, 2.0, 1e-12);
        assertEquals(0.0, root, 1e-13);
    }

    /**
     * Test for bracket method with a valid function and initial point.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBracket() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);
        double[] bracketingPoints = UnivariateRealSolverUtils.bracket(function, -2.0, -1.0, 3.0);
        assertTrue(bracketingPoints[0] < bracketingPoints[1]);
        assertEquals(-1.5, bracketingPoints[0], 1e-8);
        assertEquals(-0.5, bracketingPoints[1], 1e-8);
    }

    /**
     * Test for bracket method with a function that has no roots in the interval.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBracketNoRoots() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) + 1.0);
        try {
            UnivariateRealSolverUtils.bracket(function, 0.0, 1.0, 2.0);
            fail();
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    /**
     * Test for bracket method with a function that throws an exception during evaluation.
     * @throws Exception if an error occurs
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testBracketException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("An error occurred");
            }
        };
        try {
            UnivariateRealSolverUtils.bracket(function, 0.0, 0.0, 0.0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for bracket method with an interval that is not valid.
     * @throws Exception if an error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBracketInvalidInterval() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1.0);
        try {
            UnivariateRealSolverUtils.bracket(function, 2.0, 1.0, 2.0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for bracket method with a specified maximum number of iterations.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBracketSpecifiedMaxIterations() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);
        double[] bracketingPoints = UnivariateRealSolverUtils.bracket(function, -2.0, -1.0, 3.0, 10);
        assertTrue(bracketingPoints[0] < bracketingPoints[1]);
        assertEquals(-1.5, bracketingPoints[0], 1e-8);
        assertEquals(-0.5, bracketingPoints[1], 1e-8);
    }

    /**
     * Test for midpoint method.
     */
    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 2.0;
        double midpoint = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(1.5, midpoint, 1e-8);
    }

    /**
     * Test for setup method.
     */
    @Test
    public void testSetup() {
        UnivariateRealFunction function = null;
        try {
            UnivariateRealSolverUtils.setup(function);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}