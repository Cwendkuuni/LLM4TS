
package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        // Test with a simple linear function f(x) = x
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        };
        double x0 = 0.0;
        double x1 = 10.0;
        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1);
            assertTrue(Math.abs(result - 0.0) < 1e-10);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSolveWithAccuracy() {
        // Test with a simple linear function f(x) = x
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        };
        double x0 = 0.0;
        double x1 = 10.0;
        double absoluteAccuracy = 1e-5;
        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
            assertTrue(Math.abs(result - 0.0) < absoluteAccuracy);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBracket() {
        // Test with a simple quadratic function f(x) = x^2 - 4
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        double initial = 0.0;
        double lowerBound = -10.0;
        double upperBound = 10.0;
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
            assertNotNull(result);
            assertEquals(2, result.length);
            assertTrue(result[0] < result[1]);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBracketWithMaximumIterations() {
        // Test with a simple quadratic function f(x) = x^2 - 4
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        double initial = 0.0;
        double lowerBound = -10.0;
        double upperBound = 10.0;
        int maximumIterations = 100;
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
            assertNotNull(result);
            assertEquals(2, result.length);
            assertTrue(result[0] < result[1]);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 2.0;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(1.5, result, 1e-10);
    }
}
