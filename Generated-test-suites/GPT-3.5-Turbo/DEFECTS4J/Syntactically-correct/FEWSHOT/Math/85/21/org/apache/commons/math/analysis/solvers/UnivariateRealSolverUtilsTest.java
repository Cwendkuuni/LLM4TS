package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class UnivariateRealSolverUtilsTest {

    // Test case for solve(UnivariateRealFunction f, double x0, double x1)
    @Test
    public void testSolve1() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x*x - 4;
            }
        };

        double result = UnivariateRealSolverUtils.solve(f, 0, 3);

        assertEquals(2.0, result, 0.0001);
    }

    // Test case for solve(UnivariateRealFunction f, double x0, double x1, double absoluteAccuracy)
    @Test
    public void testSolve2() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x*x - 4;
            }
        };

        double result = UnivariateRealSolverUtils.solve(f, 0, 3, 0.0001);

        assertEquals(2.0, result, 0.0001);
    }

    // Test case for bracket(UnivariateRealFunction function, double initial, double lowerBound, double upperBound)
    @Test
    public void testBracket1() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x*x - 4;
            }
        };

        double[] result = UnivariateRealSolverUtils.bracket(f, 0, -1, 3);

        assertEquals(-1.0, result[0], 0.0001);
        assertEquals(3.0, result[1], 0.0001);
    }

    // Test case for bracket(UnivariateRealFunction function, double initial, double lowerBound, double upperBound, int maximumIterations)
    @Test
    public void testBracket2() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x*x - 4;
            }
        };

        double[] result = UnivariateRealSolverUtils.bracket(f, 0, -1, 3, 100);

        assertEquals(-1.0, result[0], 0.0001);
        assertEquals(3.0, result[1], 0.0001);
    }

    // Test case for midpoint(double a, double b)
    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(1, 3);

        assertEquals(2.0, result, 0.0001);
    }
}