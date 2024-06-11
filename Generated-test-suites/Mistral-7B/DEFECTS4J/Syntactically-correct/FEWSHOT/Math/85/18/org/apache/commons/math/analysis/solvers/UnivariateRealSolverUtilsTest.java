package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };

        double[] xy = UnivariateRealSolverUtils.bracket(f, 0, -2, 2);
        double root = UnivariateRealSolverUtils.solve(f, xy[0], xy[1]);

        assertTrue(Math.abs(root - 1) < 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveFailedToBracket() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        UnivariateRealSolverUtils.solve(f, -1, 1);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFailedToEvaluateFunction() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Failed to evaluate function");
            }
        };

        UnivariateRealSolverUtils.solve(f, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveNullFunction() throws Exception {
        UnivariateRealFunction f = null;

        UnivariateRealSolverUtils.solve(f, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidInterval() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        };

        UnivariateRealSolverUtils.solve(f, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    @Test
    public void testBracket() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };

        double[] xy = UnivariateRealSolverUtils.bracket(f, 0, -2, 2);

        assertArrayEquals(new double[]{0.0, 1.0}, xy, 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketFailedToBracket() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.cos(x);
            }
        };

        UnivariateRealSolverUtils.bracket(f, 0, 0, 1);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracketFailedToEvaluateFunction() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Failed to evaluate function");
            }
        };

        UnivariateRealSolverUtils.bracket(f, 0, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketNullFunction() throws Exception {
        UnivariateRealFunction f = null;

        UnivariateRealSolverUtils.bracket(f, 0, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketInvalidFunctionValue() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Double.NaN;
            }
        };

        UnivariateRealSolverUtils.bracket(f, 0, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketInvalidInterval() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        };

        UnivariateRealSolverUtils.bracket(f, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
    }

    @Test
    public void testMidpoint() throws Exception {
        double a = 1.0;
        double b = 3.0;

        double mid = UnivariateRealSolverUtils.midpoint(a, b);

        assertEquals(2.0, mid, 0.0);
    }
}