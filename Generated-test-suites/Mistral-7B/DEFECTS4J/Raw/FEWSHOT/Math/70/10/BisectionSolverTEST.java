package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolverWithDefaults() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new PolynomialFunction(x -> x * x - 4.0);
        double root = 2.0;

        assertEquals(root, solver.solve(f, -3.0, 3.0), 1E-6);
    }

    @Test
    public void testBisectionSolverWithCustomFunctionAndAccuracy() {
        BisectionSolver solver = new BisectionSolver(100, 1E-8);
        UnivariateRealFunction f = FunctionUtils.pow(x -> x, 2.0);
        double root = 2.0;

        assertEquals(root, solver.solve(f, -3.0, 3.0), 1E-8);
    }

    @Test
    public void testBisectionSolverWithCustomFunctionAndIterations() {
        BisectionSolver solver = new BisectionSolver(50);
        UnivariateRealFunction f = x -> Math.sin(x) - x;
        double root = 0.0;

        assertEquals(root, solver.solve(f, -3.0, 3.0), 1E-6);
    }

    @Test
    public void testBisectionSolverWithCustomFunctionAndInitialGuess() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = x -> Math.sin(x) - x;
        double root = 0.0;
        double initialGuess = 1.0;

        assertEquals(root, solver.solve(f, initialGuess, 3.0), 1E-6);
    }

    @Test
    public void testBisectionSolverWithCustomFunctionAndInvalidInterval() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = x -> Math.pow(x, 2.0) - 4.0;

        assertThrows(FunctionEvaluationException.class, () -> solver.solve(f, -2.0, 2.0));
    }

    @Test
    public void testBisectionSolverWithCustomFunctionAndMaxIterationsExceeded() {
        BisectionSolver solver = new BisectionSolver(1);
        UnivariateRealFunction f = x -> Math.sin(x) - x;

        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(f, -3.0, 3.0));
    }

    @Test
    public void testBisectionSolverWithCustomFunctionAndNoRoots() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = x -> Math.pow(x, 2.0) + 1.0;

        assertThrows(NoRootsException.class, () -> solver.solve(f, -3.0, 3.0));
    }
}