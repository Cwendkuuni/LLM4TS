package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.MathRuntimeException;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolveWithDefaultSolver() {
        UnivariateRealFunction function = new MockFunction();
        double x0 = 0.0;
        double x1 = 1.0;

        // Test with default solver and no accuracy specified
        double result = UnivariateRealSolverUtils.solve(function, x0, x1);
        assertNotNull(result);
        assertEquals(0.5, result, 0.01);

        // Test with default solver and absolute accuracy specified
        double absoluteAccuracy = 0.01;
        result = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
        assertNotNull(result);
        assertEquals(0.5, result, absoluteAccuracy);
    }

    @Test
    public void testSolveWithCustomSolver() {
        UnivariateRealFunction function = new MockFunction();
        UnivariateRealSolver customSolver = new BisectionSolver();
        double x0 = 0.0;
        double x1 = 1.0;

        // Test with custom solver and no accuracy specified
        double result = UnivariateRealSolverUtils.solve(function, x0, x1, customSolver);
        assertNotNull(result);
        assertEquals(0.5, result, 0.01);

        // Test with custom solver and absolute accuracy specified
        double absoluteAccuracy = 0.01;
        result = UnivariateRealSolverUtils.solve(function, x0, x1, customSolver, absoluteAccuracy);
        assertNotNull(result);
        assertEquals(0.5, result, absoluteAccuracy);
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction function = new MockFunction();
        double initial = 0.5;
        double lowerBound = 0.0;
        double upperBound = 1.0;

        // Test with default solver and no maximum iterations specified
        double[] bracketingPoints = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        assertArrayEquals(new double[]{0.0, 0.5}, bracketingPoints);

        // Test with default solver and maximum iterations specified
        int maximumIterations = 10;
        bracketingPoints = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);
        assertArrayEquals(new double[]{0.0, 0.5}, bracketingPoints);

        // Test with custom solver and no maximum iterations specified
        UnivariateRealSolver customSolver = new SecantSolver();
        bracketingPoints = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, customSolver);
        assertArrayEquals(new double[]{0.0, 0.5}, bracketingPoints);

        // Test with custom solver and maximum iterations specified
        maximumIterations = 5;
        bracketingPoints = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, customSolver, maximumIterations);
        assertArrayEquals(new double[]{0.0, 0.5}, bracketingPoints);
    }

    @Test
    public void testBracketFailure() {
        UnivariateRealFunction function = new MockFunctionWithNoRoots();
        double initial = 0.5;
        double lowerBound = 0.0;
        double upperBound = 1.0;

        assertThrows(ConvergenceException.class, () -> {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        });

        assertThrows(ConvergenceException.class, () -> {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, new FailedSolver());
        });
    }
}

class MockFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.pow(x, 2) - 0.25;
    }
}

class MockFunctionWithNoRoots implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.pow(x, 2) + 0.25;
    }
}

class FailedSolver implements UnivariateRealSolver {
    @Override
    public double solve(UnivariateRealFunction function, double x0, double x1)
            throws ConvergenceException, FunctionEvaluationException {
        throw new ConvergenceException("Failed solver");
    }
}

class BisectionSolver implements UnivariateRealSolver {
    @Override
    public double solve(UnivariateRealFunction function, double x0, double x1)
            throws ConvergenceException, FunctionEvaluationException {
        double a = x0;
        double b = x1;
        double fa;
        double fb;

        do {
            fa = function.value(a);
            fb = function.value(b);
            if (fa * fb < 0) {
                return midpoint(a, b);
            }
            if (Math.abs(fa) < 0.001) {
                return a;
            }

            if (fa > 0) {
                b = a;
                a = x0;
            } else {
                x0 = b;
            }
        } while (Math.abs(fa) > 0.001 && Math.abs(fb) > 0.001);

        throw new ConvergenceException("Convergence failed");
    }
}

class SecantSolver implements UnivariateRealSolver {
    @Override
    public double solve(UnivariateRealFunction function, double x0, double x1)
            throws ConvergenceException, FunctionEvaluationException {
        double x2;
        double fx0;
        double fx1;

        do {
            fx0 = function.value(x0);
            x2 = x1 - (x1 - x0) * fx0 / (function.value(x1) - fx0);
            fx1 = function.value(x1);
            x1 = x2;
        } while (Math.abs(fx1) > 0.001);

        return x1;
    }
}