package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathRuntimeException;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws Exception {
        UnivariateRealFunction function = new MockFunction();
        double[] expected = {1.0, 2.0};
        double result = UnivariateRealSolverUtils.solve(function, 0.0, 3.0);
        assertArrayEquals(expected, new double[]{1.0, 2.0}, new double[]{result, function.getValue(result)});

        UnivariateRealFunction functionNegative = (x) -> -function.value(x);
        double solution = UnivariateRealSolverUtils.solve(functionNegative, 0.0, 3.0);
        assertTrue(function.value(solution) * function.value(result) < 0);
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction function = new MockFunction();
        double[] expected = {1.5, 2.5};
        double absoluteAccuracy = 0.5;
        double result = UnivariateRealSolverUtils.solve(function, 0.0, 4.0, absoluteAccuracy);
        assertArrayEquals(expected, new double[]{result, function.getValue(result)});
    }

    @Test
    public void testSolveWithConvergenceException() throws Exception {
        UnivariateRealFunction function = (x) -> {
            if (x == 1.0) {
                throw new ConvergenceException("Test ConvergenceException");
            }
            return Math.sin(x);
        };
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.solve(function, 0.0, 3.0));
    }

    @Test
    public void testSolveWithFunctionEvaluationException() throws Exception {
        UnivariateRealFunction function = (x) -> {
            throw new FunctionEvaluationException("Test FunctionEvaluationException");
        };
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(function, 0.0, 3.0));
    }

    @Test
    public void testSolveWithIllegalArgumentException() throws Exception {
        UnivariateRealFunction function = null;
        assertThrows(MathRuntimeException.class, () -> UnivariateRealSolverUtils.solve(function, 0.0, 3.0));
    }

    @Test
    public void testSolveWithInvalidInterval() throws Exception {
        UnivariateRealFunction function = new MockFunction();
        assertThrows(MathRuntimeException.class, () -> UnivariateRealSolverUtils.solve(function, 3.0, 0.0));
    }

    private static class MockFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return x;
        }
    }
}