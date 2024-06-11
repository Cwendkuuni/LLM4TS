package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverImpl;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class BisectionSolverTest {

    @Test
    public void testConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertEquals(100, solver.getMaxIterations());
        assertEquals(1e-6, solver.getRelativeAccuracy(), 1e-10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedSolveWithFunction() {
        BisectionSolver solver = new BisectionSolver(functionXSquared());
        solver.solve(1.0, 3.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedSolveWithMinMax() {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(1.0, 3.0, 1.5);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedSolveWithMinMaxAndInitial() {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(1.0, 3.0, 1.5, 2.0);
    }

    @Test
    public void testSolveWithFunction() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction func = functionXSquared();
        double root = solver.solve(func, 1.0, 3.0);
        assertEquals(2.0, root, 1e-7);
    }

    @Test
    public void testSolveWithMinMax() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(functionXSquared(), 1.0, 3.0);
        assertEquals(2.0, root, 1e-7);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction func = functionXCubed();
        solver.solve(func, 1.0, 3.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvalException() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction func = functionXDividedByZero();
        solver.solve(func, 1.0, 3.0);
    }

    @Test
    public void testVerifyInterval() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction func = functionXSquared();
        double min = 1.0;
        double max = 0.0;
        solver.verifyInterval(min, max);
        assertEquals(0.0, min, 1e-10);
        assertEquals(1.0, max, 1e-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyIntervalInvalidMin() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction func = functionXSquared();
        double min = Double.NEGATIVE_INFINITY;
        double max = 1.0;
        solver.verifyInterval(min, max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyIntervalInvalidMax() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction func = functionXSquared();
        double min = 1.0;
        double max = Double.POSITIVE_INFINITY;
        solver.verifyInterval(min, max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyIntervalInvalidMinMax() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction func = functionXSquared();
        double min = Double.POSITIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;
        solver.verifyInterval(min, max);
    }

    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 3.0;
        double expected = (a + b) / 2.0;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(expected, result, 1e-10);
    }

    @Test
    public void testSetResult() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        double value = 1.0;
        int iteration = 1;
        solver.setResult(value, iteration);
        assertEquals(value, solver.getResult(), 1e-10);
        assertEquals(iteration, solver.getIterationCount());
    }

    @Test
    public void testClearResult() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        solver.clearResult();
        assertEquals(Double.NaN, solver.getResult(), 1e-10);
        assertEquals(0, solver.getIterationCount());
    }

    private UnivariateRealFunction functionXSquared() {
        return f -> Math.pow(f, 2.0);
    }

    private UnivariateRealFunction functionXCubed() {
        return f -> Math.pow(f, 3.0);
    }

    private UnivariateRealFunction functionXDividedByZero() {
        return f -> {
            if (f == 0.0) {
                throw new ArithmeticException("Division by zero");
            }
            return 1.0 / f;
        };
    }
}