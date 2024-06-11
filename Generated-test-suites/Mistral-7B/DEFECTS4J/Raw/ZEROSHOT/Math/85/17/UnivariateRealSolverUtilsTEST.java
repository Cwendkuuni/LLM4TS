package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.exception.ZeroException;

public class UnivariateRealSolverUtilsTest {

    @SuppressWarnings("unchecked")
    private UnivariateRealFunction createFunction(double[] coefficients) {
        return (UnivariateRealFunction) new PolynomialFunction(coefficients);
    }

    @org.junit.Test
    public void testSolveDefaultSolver() throws Exception {
        double[] coefficients = {1.0, -2.0, 2.0, -1.0};
        UnivariateRealFunction function = createFunction(coefficients);
        double x0 = 1.0;
        double x1 = 0.0;
        double root = -1.0;
        double expected = UnivariateRealSolverUtils.solve(function, x0, x1);
        assertEquals(root, expected, 1e-12);
    }

    @org.junit.Test(expected = ConvergenceException.class)
    public void testSolveDefaultSolverFail() throws Exception {
        double[] coefficients = {1.0, -2.0, 2.0, -1.0};
        UnivariateRealFunction function = createFunction(coefficients);
        double x0 = 1.0;
        double x1 = 1.0;
        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @org.junit.Test
    public void testSolveDefaultSolverAbsoluteAccuracy() throws Exception {
        double[] coefficients = {1.0, -2.0, 2.0, -1.0};
        UnivariateRealFunction function = createFunction(coefficients);
        double x0 = 1.0;
        double x1 = 0.0;
        double absoluteAccuracy = 1e-6;
        double root = -1.0;
        double expected = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
        assertEquals(root, expected, 1e-12 + absoluteAccuracy);
    }

    @org.junit.Test(expected = ConvergenceException.class)
    public void testSolveDefaultSolverAbsoluteAccuracyFail() throws Exception {
        double[] coefficients = {1.0, -2.0, 2.0, -1.0};
        UnivariateRealFunction function = createFunction(coefficients);
        double x0 = 1.0;
        double x1 = 0.0;
        double absoluteAccuracy = 0.0;
        UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSolveFunctionNull() throws Exception {
        UnivariateRealFunction function = null;
        double x0 = 1.0;
        double x1 = 0.0;
        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidInterval() throws Exception {
        double[] coefficients = {1.0, -2.0, 2.0, -1.0};
        UnivariateRealFunction function = createFunction(coefficients);
        double x0 = -1.0;
        double x1 = 1.0;
        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @org.junit.Test
    public void testBracket() throws Exception {
        double[] coefficients = {1.0, -2.0, 2.0, -1.0};
        UnivariateRealFunction function = createFunction(coefficients);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        assertEquals(2.0, result[0], 1e-12);
        assertEquals(1.0, result[1], 1e-12);
    }

    @org.junit.Test(expected = ConvergenceException.class)
    public void testBracketFail() throws Exception {
        double[] coefficients = {1.0, -2.0, 2.0, -1.0};
        UnivariateRealFunction function = createFunction(coefficients);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 1.0;
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @org.junit.Test
    public void testBracketMaxIterations() throws Exception {
        double[] coefficients = {1.0, -2.0, 2.0, -1.0};
        UnivariateRealFunction function = createFunction(coefficients);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maximumIterations = 1;
        double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);
        assertEquals(initial, result[0], 1e-12);
        assertEquals(initial, result[1], 1e-12);
    }

    @org.junit.Test
    public void testMidpoint() throws Exception {
        double a = 1.0;
        double b = 2.0;
        double expected = 1.5;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(expected, result, 1e-12);
    }

    @org.junit.Test
    public void testSetupFunctionNull() throws Exception {
        UnivariateRealFunction function = null;
        UnivariateRealSolverUtils.setup(function);
    }

    @org.junit.Test
    public void testFactory() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(new double[] {1.0, -2.0, 0.0});
        UnivariateSolver solver = UnivariateRealSolverUtils.solve(function, 1.0, 1.0, 1e-6);
        assertEquals(0.0, solver.root, 1e-12 + 1e-6);
    }

    @org.junit.Test
    public void testBrentSolver() throws Exception {
        double[] coefficients = {1.0, -2.0, 2.0, -1.0};
        UnivariateRealFunction function = createFunction(coefficients);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        UnivariateSolver solver = new BrentSolver();
        double root = UnivariateRealSolverUtils.solve(function, solver, initial, lowerBound, upperBound);
        assertEquals(-1.0, root, 1e-12);
    }

    @org.junit.Test
    public void testBrentSolverAbsoluteAccuracy() throws Exception {
        double[] coefficients = {1.0, -2.0, 2.0, -1.0};
        UnivariateRealFunction function = createFunction(coefficients);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        UnivariateSolver solver = new BrentSolver();
        double absoluteAccuracy = 1e-6;
        double root = UnivariateRealSolverUtils.solve(function, solver, initial, lowerBound, upperBound, absoluteAccuracy);
        assertEquals(-1.0, root, 1e-12 + absoluteAccuracy);
    }

    @org.junit.Test(expected = PrecisionException.class)
    public void testBrentSolverZero() throws Exception {
        double[] coefficients = {1.0, 0.0, 0.0, 0.0};
        UnivariateRealFunction function = createFunction(coefficients);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        UnivariateSolver solver = new BrentSolver();
        UnivariateRealSolverUtils.solve(function, solver, initial, lowerBound, upperBound);
    }

    @org.junit.Test(expected = MaxIterationsExceededException.class)
    public void testBrentSolverMaxIterations() throws Exception {
        double[] coefficients = {1.0, -2.0, 2.0, -1.0};
        UnivariateRealFunction function = createFunction(coefficients);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        UnivariateSolver solver = new BrentSolver();
        solver.setMaxIterations(0);
        UnivariateRealSolverUtils.solve(function, solver, initial, lowerBound, upperBound);
    }
}