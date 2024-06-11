package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolverConstruction() {
        BisectionSolver solver = new BisectionSolver();
        assertEquals(100, solver.getMaxIterations());
        assertEquals(1e-6, solver.getPrecision(), 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedConstructor() {
        new BisectionSolver(new PolynomialFunction(new double[]{-1, 1, 0}));
    }

    @Test
    public void testSolveSimpleFunction() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 0});
        double root = solver.solve(f, -1.5, 1.5);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testSolveFunctionWithInitialGuess() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 1});
        double root = solver.solve(f, -1.5, 1.5, 0.1);
        assertEquals(0.3, root, 1e-6);
    }

    @Test
    public void testSolveFunctionWithNegativeInitialGuess() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, -1});
        double root = solver.solve(f, -1.5, 1.5, -0.1);
        assertEquals(0.7, root, 1e-6);
    }

    @Test
    public void testSolveFunctionWithMaxIterationsExceeded() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 0});
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(f, -1.5, 1.5, 0.0));
    }

    @Test
    public void testSolveFunctionWithFunctionEvaluationException() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 1 / Math.sqrt(128)});
        assertThrows(FunctionEvaluationException.class, () -> solver.solve(f, -1.5, 1.5));
    }

    @Test
    public void testVerifyInterval() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 0});
        assertThrows(IllegalArgumentException.class, () -> solver.solve(f, 1.5, 1.5));
        assertThrows(IllegalArgumentException.class, () -> solver.solve(f, -1.5, 1.5));
        assertThrows(IllegalArgumentException.class, () -> solver.solve(f, 1.5, -1.5));
    }

    @Test
    public void testMidpoint() {
        assertEquals(0.25, UnivariateRealSolverUtils.midpoint(0.5, 1.5), 1e-6);
        assertEquals(0.75, UnivariateRealSolverUtils.midpoint(0.5, 0.5), 1e-6);
        assertEquals(1.0, UnivariateRealSolverUtils.midpoint(1.5, 1.5), 1e-6);
    }

    @Test
    public void testSolveFunctionWithSmallDistance() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 0.0001});
        double root = solver.solve(f, -1.5, 1.5);
        assertEquals(0.0, root, 1e-9);
    }

    @Test
    public void testSolveFunctionWithLargeMinimum() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 0});
        double root = solver.solve(f, 1e10, 1.5);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testSolveFunctionWithLargeMaximum() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 0});
        double root = solver.solve(f, -1.5, 1e10);
        assertEquals(0.0, root, 1e-6);
    }
}