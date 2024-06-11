package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolverConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
        assertEquals(100, solver.getMaxIterations());
        assertEquals(1.0E-6, solver.getRelativeAccuracy(), 1.0E-8);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedSolveConstructor() {
        BisectionSolver solver = new BisectionSolver(null);
    }

    @Test
    public void testSolveSimpleCase() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateFunction f = new PolynomialFunction(x -> x * x - 4.0);
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, -2.0, 2.0);
        assertEquals(0.0, result, 1.0E-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateFunction f = x -> Math.sin(Math.PI * x) / x;
        BisectionSolver solver = new BisectionSolver();
        double[] bounds = {-10.0, 10.0};
        solver.solve(f, bounds[0], bounds[1]);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateFunction f = x -> { throw new ArithmeticException("Divide by zero"); };
        BisectionSolver solver = new BisectionSolver();
        double[] bounds = {-1.0, 1.0};
        solver.solve(f, bounds[0], bounds[1]);
    }

    @Test
    public void testSolveRelativeAccuracy() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateFunction f = x -> Math.pow(x, 2.0) - 4.0;
        BisectionSolver solver = new BisectionSolver();
        double[] bounds = {-3.0, 3.0};
        double result = solver.solve(f, bounds[0], bounds[1]);
        assertEquals(0.0, result, 1.0E-5);
    }

    @Test
    public void testSolveMaxIterations() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateFunction f = x -> Math.pow(x, 2.0) - 4.0;
        BisectionSolver solver = new BisectionSolver(1);
        double[] bounds = {-3.0, 3.0};
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(f, bounds[0], bounds[1]));
    }

    @Test
    public void testSolveAbsoluteAccuracy() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateFunction f = x -> Math.pow(x, 2.0) - 4.0;
        BisectionSolver solver = new BisectionSolver(100, 1.0E-9);
        double[] bounds = {-3.0, 3.0};
        double result = solver.solve(f, bounds[0], bounds[1]);
        assertEquals(0.0, result, 1.0E-9);
    }

    @Test
    public void testSolveWithInitialGuess() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateFunction f = x -> Math.pow(x, 2.0) - 4.0;
        BisectionSolver solver = new BisectionSolver();
        double[] bounds = {-3.0, 3.0};
        double initialGuess = 2.0;
        double result = solver.solve(f, bounds[0], bounds[1], initialGuess);
        assertEquals(0.0, result, 1.0E-6);
    }

    @Test
    public void testVerifyInterval() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = x -> FunctionUtils.ONE;
        double min = Double.NaN;
        double max = 0.0;
        assertFalse(solver.verifyInterval(min, max, f));

        min = Double.NEGATIVE_INFINITY;
        max = 0.0;
        assertFalse(solver.verifyInterval(min, max, f));

        min = 0.0;
        max = Double.NEGATIVE_INFINITY;
        assertFalse(solver.verifyInterval(min, max, f));

        min = 0.0;
        max = 0.0;
        assertTrue(solver.verifyInterval(min, max, f));

        min = -1.0;
        max = 1.0;
        assertTrue(solver.verifyInterval(min, max, f));
    }

    @Test
    public void testMidpoint() {
        double min = 0.0;
        double max = 1.0;
        assertEquals(0.5, UnivariateRealSolverUtils.midpoint(min, max), 1.0E-6);
    }

    @Test
    public void testSetResult() {
        BisectionSolver solver = new BisectionSolver();
        double value = 0.0;
        int iterations = 0;
        solver.setResult(value, iterations);
        assertEquals(value, solver.getResult(), 1.0E-6);
        assertEquals(iterations, solver.getIterations());
    }

    @Test
    public void testClearResult() {
        BisectionSolver solver = new BisectionSolver();
        solver.clearResult();
        assertNull(solver.getResult());
        assertEquals(0, solver.getIterations());
    }

    @Test
    public void testSolveNumericalStability() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateFunction f = x -> Math.pow(x, 2.0) - 1.0;
        BisectionSolver solver = new BisectionSolver();
        double[] bounds = {1.0E-307, 1.0};
        double result = solver.solve(f, bounds[0], bounds[1]);
        assertTrue(Precision.abs(result) > 1.0E-307);
    }
}