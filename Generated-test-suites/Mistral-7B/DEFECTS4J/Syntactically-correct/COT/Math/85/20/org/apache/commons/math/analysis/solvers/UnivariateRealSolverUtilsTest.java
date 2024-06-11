package org.apache.commons.math.analysis.solvers; // make sure the package is correct

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.BracketedBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.PrecisionModifiedException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.special.BesselFunctions;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.special.HypergeometricFunctions;
import org.apache.commons.math3.special.LogBesselI;
import org.apache.commons.math3.special.LogBesselK;
import org.junit.BeforeClass;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    /**
     * Setup class level variables.
     */
    @BeforeClass
    public static void setup() {
        // No setup needed for this test class
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}.
     */
    @Test
    public void testSolve() {
        UnivariateFunction f = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);// root is 2.0
        double x0 = 0.0;
        double x1 = 4.0;

        // Test normal case
        double root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(2.0, root, 1e-6); // allow small error due to floating point precision

        // Test edge case with x0 == x1
        x1 = x0;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(f, x0, x1));

        // Test case where function does not have a root in the interval
        f = new PolynomialFunction(x -> Math.pow(x, 2) + 1.0);
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(f, x0, x1));

        // Test exception handling
        f = new PolynomialFunction(x -> Math.log(x)); // log function is not defined for non-positive x
        x0 = -1.0;
        x1 = 0.0;
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(f, x0, x1));

        f = new PolynomialFunction(x -> 1.0 / x); // division by zero
        x0 = 0.0;
        x1 = 1.0;
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(f, x0, x1));
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolveWithAbsoluteAccuracy() {
        UnivariateFunction f = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);// root is 2.0
        double x0 = 0.0;
        double x1 = 4.0;
        double absoluteAccuracy = 1e-6;

        // Test normal case
        double root = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        assertEquals(2.0, root, absoluteAccuracy);

        // Test edge case with x0 == x1
        x1 = x0;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy));

        // Test case where function does not have a root in the interval
        f = new PolynomialFunction(x -> Math.pow(x, 2) + 1.0);
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy));

        // Test exception handling
        f = new PolynomialFunction(x -> Math.log(x)); // log function is not defined for non-positive x
        x0 = -1.0;
        x1 = 0.0;
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy));

        f = new PolynomialFunction(x -> 1.0 / x); // division by zero
        x0 = 0.0;
        x1 = 1.0;
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy));
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)}
     */
    @Test
    public void testBracket() {
        UnivariateFunction f = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);// root is 2.0
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 3.0;

        // Test normal case
        double[] bracketedValues = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        assertArrayEquals(new double[]{1.0, 2.0}, bracketedValues);

        // Test edge case with x0 == x1
        initial = 0.0;
        lowerBound = 0.0;
        upperBound = 0.0;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound));

        // Test case where function does not have a root in the interval
        f = new PolynomialFunction(x -> Math.pow(x, 2) + 1.0);
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound));

        // Test exception handling
        f = new PolynomialFunction(x -> Math.log(x)); // log function is not defined for non-positive x
        initial = -1.0;
        lowerBound = 0.0;
        upperBound = 0.0;
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound));

        f = new PolynomialFunction(x -> 1.0 / x); // division by zero
        initial = 0.0;
        lowerBound = 0.0;
        upperBound = 1.0;
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound));
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     */
    @Test
    public void testBracketWithMaxIterations() {
        UnivariateFunction f = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);// root is 2.0
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 3.0;
        int maxIterations = 1; // set max iterations to a very small number to force an exception

        // Test normal case
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maxIterations));

        // Test exception handling
        f = new PolynomialFunction(x -> Math.log(x)); // log function is not defined for non-positive x
        initial = -1.0;
        lowerBound = 0.0;
        upperBound = 0.0;
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maxIterations));

        f = new PolynomialFunction(x -> 1.0 / x); // division by zero
        initial = 0.0;
        lowerBound = 0.0;
        upperBound = 1.0;
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maxIterations));

        // Test case where function does not have a root in the interval
        f = new PolynomialFunction(x -> Math.pow(x, 2) + 1.0);
        assertThrows(TooManyIterationsException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maxIterations));
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#midpoint(double, double)}
     */
    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 2.0;

        // Test normal case
        double midpoint = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(1.5, midpoint, 1e-6);

        // Test edge case with a == b
        a = 2.0;
        b = 2.0;
        midpoint = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(2.0, midpoint);

        // Test exceptional cases
        a = Double.NaN;
        assertFalse(Double.isNaN(UnivariateRealSolverUtils.midpoint(a, b)));
        b = Double.NaN;
        assertFalse(Double.isNaN(UnivariateRealSolverUtils.midpoint(a, b)));

        a = Double.POSITIVE_INFINITY;
        assertFalse(Double.isNaN(UnivariateRealSolverUtils.midpoint(a, b)));
        b = Double.POSITIVE_INFINITY;
        assertFalse(Double.isNaN(UnivariateRealSolverUtils.midpoint(a, b)));

        a = Double.NEGATIVE_INFINITY;
        assertFalse(Double.isNaN(UnivariateRealSolverUtils.midpoint(a, b)));
        b = Double.NEGATIVE_INFINITY;
        assertFalse(Double.isNaN(UnivariateRealSolverUtils.midpoint(a, b)));

        a = Long.MAX_VALUE;
        assertFalse(Double.isNaN(UnivariateRealSolverUtils.midpoint(a, b)));
        b = Long.MAX_VALUE;
        assertFalse(Double.isNaN(UnivariateRealSolverUtils.midpoint(a, b)));
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#setup(UnivariateRealFunction)}
     */
    @Test
    public void testSetup() {
        UnivariateFunction f = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);// root is 2.0

        // Test normal case
        UnivariateRealSolverUtils.setup(f);

        // Test edge case with null function
        assertThrows(NullArgumentException.class, () -> UnivariateRealSolverUtils.setup(null));
    }
}