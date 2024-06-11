package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.PrecisionRequiredException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.util.Precision;

import java.util.Arrays;

/**
 * Test file for {@link UnivariateRealSolverUtils} class.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testSolve() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(3.0, 1.0, -2.0, 1.0);
        double x0 = 0.0;
        double x1 = 2.0;
        double expectedResult = 1.0;
        double result;

        result = UnivariateRealSolverUtils.solve(function, x0, x1);
        assertEquals(expectedResult, Precision.round(result, 4), 0.0);

        UnivariateRealFunction function2 = new PolynomialFunction(1.0, 5.0, -3.0, 2.0);
        x0 = 0.0;
        x1 = 5.0;
        expectedResult = 1.5;
        result = UnivariateRealSolverUtils.solve(function2, x0, x1);
        assertEquals(expectedResult, Precision.round(result, 4), 0.0);

        UnivariateRealFunction function3 = new PolynomialFunction(1.0, 2.0, -2.0, 1.0);
        x0 = -5.0;
        x1 = -1.0;
        expectedResult = -1.0;
        result = UnivariateRealSolverUtils.solve(function3, x0, x1);
        assertEquals(expectedResult, Precision.round(result, 4), 0.0);

        UnivariateRealFunction function4 = new PolynomialFunction(1.0, 1.0, 0.0, 1.0);
        x0 = 0.0;
        x1 = 1.0;
        expectedResult = 0.0;
        result = UnivariateRealSolverUtils.solve(function4, x0, x1);
        assertEquals(expectedResult, Precision.round(result, 4), 0.0);

        UnivariateRealFunction function5 = new PolynomialFunction(1.0, 1.0, -1.0, 1.0);
        x0 = -2.0;
        x1 = 2.0;
        expectedResult = 0.0;
        result = UnivariateRealSolverUtils.solve(function5, x0, x1);
        assertEquals(expectedResult, Precision.round(result, 4), 0.0);

        UnivariateRealFunction function6 = new PolynomialFunction(0.0, 1.0, 1.0, 1.0);
        x0 = 0.0;
        x1 = 1.0;
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.solve(function6, x0, x1));

        UnivariateRealFunction function7 = new PolynomialFunction(1.0, 1.0, 0.0, 0.0);
        x0 = 0.0;
        x1 = 0.0;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(function7, x0, x1));
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testSolveWithAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(3.0, 1.0, -2.0, 1.0);
        double x0 = 0.0;
        double x1 = 2.0;
        double absoluteAccuracy = 1e-5;
        double expectedResult = 1.0;
        double result;

        result = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
        assertEquals(expectedResult, Precision.round(result, 4), 0.0);

        UnivariateRealFunction function2 = new PolynomialFunction(1.0, 5.0, -3.0, 2.0);
        x0 = 0.0;
        x1 = 5.0;
        absoluteAccuracy = 1e-3;
        expectedResult = 1.5;
        result = UnivariateRealSolverUtils.solve(function2, x0, x1, absoluteAccuracy);
        assertEquals(expectedResult, Precision.round(result, 4), 0.0);

        UnivariateRealFunction function3 = new PolynomialFunction(1.0, 2.0, -2.0, 1.0);
        x0 = -5.0;
        x1 = -1.0;
        absoluteAccuracy = 1e-5;
        expectedResult = -1.0;
        result = UnivariateRealSolverUtils.solve(function3, x0, x1, absoluteAccuracy);
        assertEquals(expectedResult, Precision.round(result, 4), 0.0);

        UnivariateRealFunction function4 = new PolynomialFunction(1.0, 1.0, 0.0, 1.0);
        x0 = 0.0;
        x1 = 1.0;
        absoluteAccuracy = 1e-10;
        expectedResult = 0.0;
        result = UnivariateRealSolverUtils.solve(function4, x0, x1, absoluteAccuracy);
        assertEquals(expectedResult, Precision.round(result, 4), 0.0);

        UnivariateRealFunction function5 = new PolynomialFunction(1.0, 1.0, -1.0, 1.0);
        x0 = -2.0;
        x1 = 2.0;
        absoluteAccuracy = 1e-5;
        expectedResult = 0.0;
        result = UnivariateRealSolverUtils.solve(function5, x0, x1, absoluteAccuracy);
        assertEquals(expectedResult, Precision.round(result, 4), 0.0);

        UnivariateRealFunction function6 = new PolynomialFunction(0.0, 1.0, 1.0, 1.0);
        x0 = 0.0;
        x1 = 1.0;
        absoluteAccuracy = 1e-11;
        assertThrows(PrecisionRequiredException.class, () -> UnivariateRealSolverUtils.solve(function6, x0, x1, absoluteAccuracy));

        UnivariateRealFunction function7 = new PolynomialFunction(1.0, 1.0, 0.0, 0.0);
        x0 = 0.0;
        x1 = 0.0;
        absoluteAccuracy = 1e-5;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(function7, x0, x1, absoluteAccuracy));
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testBracket() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(3.0, 1.0, -2.0, 1.0);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        double[] expectedResult = new double[]{1.0, 2.0};
        double[] result;

        result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        assertArrayEquals(expectedResult, result);

        UnivariateRealFunction function2 = new PolynomialFunction(1.0, 5.0, -3.0, 2.0);
        initial = 2.5;
        lowerBound = 0.0;
        upperBound = 5.0;
        expectedResult = new double[]{2.0, 3.0};
        result = UnivariateRealSolverUtils.bracket(function2, initial, lowerBound, upperBound);
        assertArrayEquals(expectedResult, result);

        UnivariateRealFunction function3 = new PolynomialFunction(1.0, 2.0, -2.0, 1.0);
        initial = -1.5;
        lowerBound = -5.0;
        upperBound = -1.0;
        expectedResult = new double[]{-2.0, -1.0};
        result = UnivariateRealSolverUtils.bracket(function3, initial, lowerBound, upperBound);
        assertArrayEquals(expectedResult, result);

        UnivariateRealFunction function4 = new PolynomialFunction(1.0, 1.0, 0.0, 1.0);
        initial = 0.5;
        lowerBound = 0.0;
        upperBound = 1.0;
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(function4, initial, lowerBound, upperBound, 1));

        UnivariateRealFunction function5 = new PolynomialFunction(1.0, 1.0, -1.0, 1.0);
        initial = -2.5;
        lowerBound = -2.0;
        upperBound = 2.0;
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(function5, initial, lowerBound, upperBound, 100_000));

        UnivariateRealFunction function6 = new PolynomialFunction(1.0, 1.0, 1.0, 1.0);
        initial = 0.5;
        lowerBound = 0.0;
        upperBound = 1.0;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(function6, initial, lowerBound, upperBound, -1));

        UnivariateRealFunction function7 = new PolynomialFunction(1.0, 1.0, 0.0, 0.0);
        initial = 0.5;
        lowerBound = 0.0;
        upperBound = 0.0;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(function7, initial, lowerBound, upperBound, 1));
    }

    /**
     * Polynomial function for testing.
     */
    private static class PolynomialFunction implements UnivariateRealFunction {

        private final double a;
        private final double b;
        private final double c;
        private final double d;

        public PolynomialFunction(double a, double b, double c, double d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }

        @Override
        public double value(double x) {
            return a * Math.pow(x, 3) + b * Math.pow(x, 2) + c * x + d;
        }
    }

    /**
     * Assert that arrays are equal.
     * @param expected expected array
     * @param actual actual array
     */
    private static void assertArrayEquals(double[] expected, double[] actual) {
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError("Expected " + Arrays.toString(expected) + ", but was " + Arrays.toString(actual));
        }
    }
}