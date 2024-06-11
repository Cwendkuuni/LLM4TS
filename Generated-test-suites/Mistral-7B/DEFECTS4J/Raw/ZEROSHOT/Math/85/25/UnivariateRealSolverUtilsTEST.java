package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

/**
 * JUnit 4 test file for UnivariateRealSolverUtils class.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)} method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolve() throws Exception {
        UnivariateRealFunction function = new Erf();
        double x0 = 1.0;
        double x1 = 2.0;

        double root = UnivariateRealSolverUtils.solve(function, x0, x1);
        assertNotEquals(x0, root);
        assertNotEquals(x1, root);
        assertTrue(Math.abs(function.value(root)) < 1e-10);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)} method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolveWithAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction function = new Erf();
        double x0 = 1.0;
        double x1 = 2.0;
        double absoluteAccuracy = 1e-10;

        double root = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
        assertNotEquals(x0, root);
        assertNotEquals(x1, root);
        assertTrue(Math.abs(function.value(root)) < absoluteAccuracy);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)} method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBracket() throws Exception {
        UnivariateRealFunction function = new Erf();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;

        double[] bracketingPoints = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        assertEquals(2, bracketingPoints.length);
        assertNotEquals(initial, bracketingPoints[0]);
        assertNotEquals(initial, bracketingPoints[1]);
        assertTrue(Math.abs(function.value(bracketingPoints[0])) > 0);
        assertTrue(Math.abs(function.value(bracketingPoints[1])) < 0);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)} method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBracketWithMaxIterations() throws Exception {
        UnivariateRealFunction function = new Erf();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maximumIterations = 1;

        try {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#setup(UnivariateRealFunction)} method.
     * @throws Exception if an error occurs
     */
    @Test(expected = MathRuntimeException.class)
    public void testSetupWithNullFunction() throws Exception {
        UnivariateRealFunction function = null;
        UnivariateRealSolverUtils.setup(function);
        fail("Unexpected success");
    }
}