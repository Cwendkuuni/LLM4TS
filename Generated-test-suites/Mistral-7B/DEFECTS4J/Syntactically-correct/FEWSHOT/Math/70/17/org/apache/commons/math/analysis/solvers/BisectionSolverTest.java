package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BisectionSolverTest {

    @Test(expected = NullArgumentException.class)
    public void testConstructorNullFunction() {
        new BisectionSolver(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveNullFunction() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = null;
        solver.solve(function, 0.0, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveEmptyInterval() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = f -> Math.sqrt(Math.abs(f)) * 2;
        solver.solve(function, 0.0, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveOverlappingInterval() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = f -> Math.PI - f;
        solver.solve(function, 3.0, 2.0);
    }

    @Test
    public void testSolveFunctionWithZeroDerivative() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = f -> f * f - 1;
        double root = solver.solve(function, -2.0, 2.0);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testSolveFunctionWithOneRoot() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = f -> Math.pow(Math.E, f) - 2;
        double root = solver.solve(function, -5.0, 5.0);
        assertEquals(Math.log(2.0) / Math.E, root, 1e-6);
    }

    @Test
    public void testSolveFunctionWithTwoRoots() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = f -> Math.pow(Math.E, f) - 3;
        try {
            solver.solve(function, -5.0, 5.0);
            assertFalse(true);
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }

    @Test
    public void testSolveFunctionWithNegativeRoot() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = f -> Math.pow(Math.E, f) + 2;
        double root = solver.solve(function, -5.0, 5.0);
        assertEquals(-Math.log(2.0) / Math.E, root, 1e-6);
    }

    @Test
    public void testSolveFunctionWithNoRoots() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = f -> Math.sin(f);
        try {
            solver.solve(function, -5.0, 5.0);
            assertFalse(true);
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }

    @Test
    public void testSolveFunctionWithInfiniteRoot() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = f -> 1.0 / f;
        try {
            solver.solve(function, 0.0, 1.0);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSolveFunctionWithNegativeInfiniteRoot() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = f -> 1.0 / f;
        try {
            solver.solve(function, -1.0, 0.0);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSolveFunctionWithTwoCloseRoots() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = f -> f * f - 0.01;
        double root = solver.solve(function, -1.0, 1.0);
        assertEquals(-0.5, root, 1e-6);
    }

    @Test
    public void testSolveFunctionWithInitialGuessCloseToRoot() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = f -> Math.pow(Math.E, f) - 2;
        double root = solver.solve(function, 1.5, 2.0);
        assertEquals(Math.log(2.0) / Math.E, root, 1e-6);
    }

    @Test
    public void testSolveFunctionWithLargeInterval() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = f -> Math.sin(f);
        try {
            solver.solve(function, -1000.0, 1000.0);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSolveFunctionWithMaxIterations() {
        BisectionSolver solver = new BisectionSolver(5);
        UnivariateRealFunction function = f -> Math.pow(Math.E, f) - 2;
        try {
            solver.solve(function, 0.0, 1.0);
            assertFalse(true);
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }

    @Test
    public void testSolveFunctionWithRelativeAccuracy() {
        BisectionSolver solver = new BisectionSolver(100, 1e-3);
        UnivariateRealFunction function = f -> Math.pow(Math.E, f) - 2;
        double root = solver.solve(function, 0.0, 1.0);
        assertTrue(Precision.isClose(Math.log(2.0) / Math.E, root, 1e-3));
    }

    @Test
    public void testSolveFunctionWithAbsoluteAccuracy() {
        BisectionSolver solver = new BisectionSolver(100, 1e-6);
        UnivariateRealFunction function = f -> Math.pow(Math.E, f) - 2;
        double root = solver.solve(function, 0.0, 1.0);
        assertEquals(Math.log(2.0) / Math.E, root, 1e-6);
    }

    @Test
    public void testSolveFunctionWithFunctionValuesArray() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = f -> Math.sin(f);
        double[] x = {-1.0, 1.0};
        double[] y = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            y[i] = solver.solve(function, x[i], x[(i + 1) % x.length]);
        }
        assertArrayEquals(new double[]{-1.0, 1.0}, x, 1e-6);
        assertArrayEquals(new double[]{-1.0, 1.0}, x, 0.01);
        RealArray2D expectedMatrix = new Array2DRowRealMatrix(x.length, 2);
        expectedMatrix.setRow(0, 0, -1.0);
        expectedMatrix.setRow(0, 1, -1.0);
        expectedMatrix.setRow(1, 0, 1.0);
        expectedMatrix.setRow(1, 1, 1.0);
        RealVector expectedResult = new ArrayRealVector(new double[]{0.0, 0.0});
        assertEquals(expectedMatrix, solver.getIterationData());
        assertEquals(expectedResult, solver.getSolutionVector());
    }

    private UnivariateRealFunction f(double x) {
        return () -> Math.cos(x) * Math.sin(x) - 0.1;
    }
}