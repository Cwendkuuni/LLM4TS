package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction function = x -> x * x - 4;
        double[] bracketing = UnivariateRealSolverUtils.bracket(function, 2.0, -2.0, 2.0);
        double root = UnivariateRealSolverUtils.solve(function, bracketing[0], bracketing[1]);
        assertEquals(0.0, root, 1e-6);

        function = x -> Math.sin(x) / x;
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(function, -1.0, 1.0));
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.solve(function, -1.0, 1.0, 0.0));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(null, 0.0, 0.0));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(function, Double.NaN, Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(function, -1.0, 1.0, -1.0));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(function, 0.0, 0.0, Double.POSITIVE_INFINITY));

        function = x -> x;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(function, Double.NEGATIVE_INFINITY, 0.0));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(function, 0.0, Double.NEGATIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(function, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction function = x -> x * x - 4;
        double[] bracketing = UnivariateRealSolverUtils.bracket(function, 2.0, -2.0, 2.0);
        assertArrayEquals(new double[]{1.5, 2.5}, bracketing, 0.0);

        function = x -> Math.sin(x) / x;
        assertNull(UnivariateRealSolverUtils.bracket(function, -1.0, 1.0));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracketing(null, 0.0, 0.0));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracketing(function, Double.NaN, Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracketing(function, -1.0, 1.0, -1.0));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracketing(function, 0.0, 0.0, Double.POSITIVE_INFINITY));

        function = x -> x;
        assertNull(UnivivariateRealSolverUtils.bracketing(function, Double.NEGATIVE_INFINITY, 0.0));
        assertNull(UnivariateRealSolverUtils.bracketing(function, 0.0, Double.NEGATIVE_INFINITY));
        assertNull(UnivariateRealSolverUtils.bracketing(function, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));

        function = x -> Math.exp(-x) - 0.5;
        double[] bracketing2 = UnivariateRealSolverUtils.bracket(function, 0.0, 2.0, 1000);
        assertArrayEquals(new double[]{1.0, 1.5}, bracketing2, 1e-6);
    }

    @Test
    public void testMidpoint() {
        assertEquals(3.5, UnivariateRealSolverUtils.midpoint(2.0, 4.0));
    }
}