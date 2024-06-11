package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

/**
 * Test cases for {@link UnivariateRealSolverUtils}.
 */
public class UnivariateRealSolverUtilsTest {
    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}.
     */
    @Test
    public void testSolve() {
        UnivariateRealFunction f = new PolynomialFunction(2.0, -1.0, 1.0, 1.0);
        double x0 = 1.0;
        double x1 = 2.0;

        double root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(1.0, root, 1e-12);

        f = new PolynomialFunction(2.0, -1.0, 1.0, -1.0);
        x0 = 1.0;
        x1 = 2.0;

        root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(-1.0, root, 1e-12);

        f = new PolynomialFunction(2.0, -1.0, 1.0, 0.0);
        x0 = 1.0;
        x1 = 2.0;

        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(f, x0, x1));

        f = null;
        assertThrows(NullArgumentException.class, () -> UnivariateRealSolverUtils.solve(f, x0, x1));
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new PolynomialFunction(2.0, -1.0, 1.0, 1.0);
        double x0 = 1.0;
        double x1 = 2.0;
        double absoluteAccuracy = 1e-10;

        double root = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        assertEquals(1.0, root, absoluteAccuracy);

        f = new PolynomialFunction(2.0, -1.0, 1.0, -1.0);
        x0 = 1.0;
        x1 = 2.0;
        absoluteAccuracy = 1e-10;

        root = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        assertEquals(-1.0, root, absoluteAccuracy);

        f = new PolynomialFunction(2.0, -1.0, 1.0, 0.0);
        x0 = 1.0;
        x1 = 2.0;
        absoluteAccuracy = 1e-10;

        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy));

        f = null;
        assertThrows(NullArgumentException.class, () -> UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy));
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testBracket() {
        UnivariateRealFunction f = new PolynomialFunction(2.0, -1.0, 1.0, 1.0);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;

        double[] bracketingValues = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        assertEquals(1.0, bracketingValues[0], 1e-12);
        assertEquals(2.0, bracketingValues[1], 1e-12);

        f = new PolynomialFunction(2.0, -1.0, 1.0, -1.0);
        initial = 1.0;
        lowerBound = 0.0;
        upperBound = 2.0;

        bracketingValues = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        assertEquals(-1.0, bracketingValues[0], 1e-12);
        assertEquals(1.0, bracketingValues[1], 1e-12);

        f = new PolynomialFunction(2.0, -1.0, 1.0, 0.0);
        initial = 1.0;
        lowerBound = 0.0;
        upperBound = 2.0;

        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound));

        f = null;
        assertThrows(NullArgumentException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound));
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}.
     */
    @Test
    public void testBracketWithMaxIterations() {
        UnivariateRealFunction f = new PolynomialFunction(2.0, -1.0, 1.0, 1.0);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maximumIterations = 1;

        double[] bracketingValues = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
        assertEquals(Double.NaN, bracketingValues[0], 0.0);
        assertEquals(Double.NaN, bracketingValues[1], 0.0);

        f = new PolynomialFunction(2.0, -1.0, 1.0, -1.0);
        initial = 1.0;
        lowerBound = 0.0;
        upperBound = 2.0;
        maximumIterations = 1;

        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations));

        f = null;
        assertThrows(NullArgumentException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations));
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#midpoint(double, double)}.
     */
    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 2.0;

        double midpoint = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(1.5, midpoint, 1e-12);

        a = -1.0;
        b = 1.0;

        midpoint = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(0.0, midpoint, 1e-12);
    }
}