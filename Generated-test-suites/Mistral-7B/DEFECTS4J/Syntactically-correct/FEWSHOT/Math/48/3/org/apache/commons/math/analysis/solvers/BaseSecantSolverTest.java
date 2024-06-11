package org.apache.commons.cli.test; // Replace with the correct package name if needed

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.commons.math.analysis.FunctionUtils;
import org.apache.commons.commons.math.analysis.PolynomialFunction;
import org.apache.commons.commons.math.analysis.UnivariateFunction;
import org.junit.Test;

import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math3.analysis.solvers.BrentSolver; // Add this import if needed for testing BrentSolver method

public class BaseSecantSolverTest {

    @Test
    public void testBaseSecantSolver() {
        // Test constructor with default arguments
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        assertNotNull(solver);

        // Test constructor with custom arguments
        double relativeAccuracy = 0.01;
        double absoluteAccuracy = 1e-5;
        double functionValueAccuracy = 1e-6;
        BaseSecantSolver solverWithCustomArgs = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, BaseSecantSolver.Method.PEGASUS);
        assertNotNull(solverWithCustomArgs);
        assertTrue(solverWithCustomArgs.getRelativeAccuracy() == relativeAccuracy);
        assertTrue(solverWithCustomArgs.getAbsoluteAccuracy() == absoluteAccuracy);
        assertTrue(solverWithCustomArgs.getFunctionValueAccuracy() == functionValueAccuracy);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMethod() {
        new BaseSecantSolver(1.0, BaseSecantSolver.Method.INVALID);
    }

    @Test
    public void testSolveRegulaFalsi() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);

        double root = solver.solve(function, -3.0, 3.0, 100);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testSolveIllinois() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);

        double root = solver.solve(function, -3.0, 3.0, 100);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testSolvePegasus() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);

        double root = solver.solve(function, -3.0, 3.0, 100);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testSolveBracketedFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);

        double root = solver.solve(function, -3.0, 3.0, 100, AllowedSolution.BELOW_SIDE);
        assertEquals(0.0, root, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);

        // Set the function value accuracy to be too small, causing convergence exception
        BaseSecantSolver solverWithCustomAccuracy = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS, 1e-12, 1e-6, 1e-12);
        double root = solverWithCustomAccuracy.solve(function, -3.0, 3.0, 100);
    }

    @Test
    public void testAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);

        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);

        double rootBelow = solver.solve(function, -3.0, 3.0, 100, AllowedSolution.BELOW_SIDE);
        assertEquals(0.0, rootBelow, 1e-6);

        double rootAbove = solver.solve(function, -3.0, 3.0, 100, AllowedSolution.ABOVE_SIDE);
        assertEquals(0.0, rootAbove, 1e-6);

        double rootLeft = solver.solve(function, -3.0, 3.0, 100, AllowedSolution.LEFT_SIDE);
        assertFalse(rootLeft < 0.0);

        double rootRight = solver.solve(function, -3.0, 3.0, 100, AllowedSolution.RIGHT_SIDE);
        assertFalse(rootRight > 0.0);

        double rootAnySide = solver.solve(function, -3.0, 3.0, 100, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, rootAnySide, 1e-6);
    }

    @Test
    public void testAllowedSolutionWithCustomAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);

        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);

        double rootBelow = solver.solve(function, -3.0, 3.0, 100, AllowedSolution.BELOW_SIDE, 1e-8);
        assertEquals(0.0, rootBelow, 1e-8);

        double rootAbove = solver.solve(function, -3.0, 3.0, 100, AllowedSolution.ABOVE_SIDE, 1e-8);
        assertEquals(0.0, rootAbove, 1e-8);

        double rootLeft = solver.solve(function, -3.0, 3.0, 100, AllowedSolution.LEFT_SIDE, 1e-8);
        assertFalse(rootLeft < 0.0);

        double rootRight = solver.solve(function, -3.0, 3.0, 100, AllowedSolution.RIGHT_SIDE, 1e-8);
        assertFalse(rootRight > 0.0);

        double rootAnySide = solver.solve(function, -3.0, 3.0, 100, AllowedSolution.ANY_SIDE, 1e-8);
        assertEquals(0.0, rootAnySide, 1e-8);
    }

    @Test
    public void testBrentSolver() {
        BaseSecantSolver brentSolver = new BaseSecantSolver(BaseSecantSolver.Method.BRENT);
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);

        double root = brentSolver.solve(function, -3.0, 3.0, 100);
        assertEquals(0.0, root, 1e-6);
    }
}