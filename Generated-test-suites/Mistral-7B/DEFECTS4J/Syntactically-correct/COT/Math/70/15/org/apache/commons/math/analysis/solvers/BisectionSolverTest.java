package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testConstructorWithFunction() {
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 4);
        BisectionSolver solver = new BisectionSolver(function);
        assertNotNull(solver.getFunction());
        assertArrayEquals(new double[]{2.0, 3.0}, solver.getInitialBracket(), 0.0);
        assertArrayEquals(new double[]{0.1, 100.0}, solver.getMaxIterations(), 0.0);
        assertArrayEquals(new double[]{1.0e-6, 1.0e-10}, solver.getRelativeAccuracy(), 0.0);
        assertArrayEquals(new double[]{1.0e-6, 1.0e-10}, solver.getAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testConstructorWithoutFunction() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver.getFunction());
        assertArrayEquals(new double[]{2.0, 3.0}, solver.getInitialBracket(), 0.0);
        assertArrayEquals(new double[]{100.0, 100.0}, solver.getMaxIterations(), 0.0);
        assertArrayEquals(new double[]{1.0e-6, 1.0e-10}, solver.getRelativeAccuracy(), 0.0);
        assertArrayEquals(new double[]{1.0e-6, 1.0e-10}, solver.getAbsoluteAccuracy(), 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithFunctionAndInvalidMinAndMax() {
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1);
        BisectionSolver solver = new BisectionSolver(function);
        double min = 0.0;
        double max = 1.0;
        solver.solve(function, min, max);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveWithFunctionAndMaxIterationsExceeded() {
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1);
        BisectionSolver solver = new BisectionSolver();
        double min = 0.0;
        double max = 0.1;
        solver.solve(function, min, max, 0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveWithFunctionAndFunctionEvaluationException() {
        UnivariateRealFunction function = f -> Math.sqrt(Math.abs(f));
        BisectionSolver solver = new BisectionSolver();
        double min = -1.0;
        double max = 1.0;
        solver.solve(function, min, max);
    }

    @Test
    public void testSolveWithFunctionAndSmallInterval() throws Exception {
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1);
        BisectionSolver solver = new BisectionSolver();
        double min = 0.0;
        double max = 0.1;
        double result = solver.solve(function, min, max);
        assertTrue(Precision.isClose(result, -0.5, 1e-5));
    }

    @Test
    public void testSolveWithFunctionAndLargeInterval() throws Exception {
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1);
        BisectionSolver solver = new BisectionSolver();
        double min = -10.0;
        double max = 10.0;
        double result = solver.solve(function, min, max);
        assertTrue(Precision.isClose(result, 0.0, 1e-5));
    }

    @Test
    public void testSolveWithFunctionAndInitialBracket() throws Exception {
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1);
        BisectionSolver solver = new BisectionSolver();
        double min = 0.0;
        double max = 2.0;
        double result = solver.solve(function, min, max);
        assertTrue(Precision.isClose(result, -1.0, 1e-5));
    }

    @Test
    public void testSolveWithFunctionAndLargeIterations() throws Exception {
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1);
        BisectionSolver solver = new BisectionSolver();
        double min = 0.0;
        double max = 0.1;
        solver.setMaxIterations(10000);
        double result = solver.solve(function, min, max);
        assertTrue(Precision.isClose(result, -0.5, 1e-5));
    }

    @Test
    public void testSolveWithFunctionAndSmallAccuracy() throws Exception {
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1);
        BisectionSolver solver = new BisectionSolver();
        double min = 0.0;
        double max = 0.1;
        solver.setAbsoluteAccuracy(1e-3);
        solver.setRelativeAccuracy(1e-3);
        double result = solver.solve(function, min, max);
        assertTrue(MathUtils.isClose(result, -0.5, 1e-3));
    }

    @Test
    public void testSolveWithFunctionAndLargeAccuracy() throws Exception {
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1);
        BisectionSolver solver = new BisectionSolver();
        double min = 0.0;
        double max = 0.1;
        solver.setAbsoluteAccuracy(1e-15);
        solver.setRelativeAccuracy(1e-15);
        double result = solver.solve(function, min, max);
        assertTrue(Precision.isClose(result, -0.5, 1e-15));
    }
}