package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.commons.math.analysis.FunctionUtils;
import org.apache.commons.commons.math.analysis.PolynomialFunction;
import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math.exception.MaxIterationsExceededException;
import org.apache.commons.commons.math.exception.PreconditionNotMetException;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testNullFunction() {
        assertThrows(NullPointerException.class, () -> new BaseSecantSolver(0.01, Method.REGULA_FALSI).solve(1, null, 0d, 1d, 0d));
    }

    @Test
    public void testEmptyInterval() {
        assertThrows(IllegalArgumentException.class, () -> new BaseSecantSolver(0.01, Method.REGULA_FALSI).solve(1, x -> 0d, 0d, 0d, AllowedSolution.ANY_SIDE));
    }

    @Test
    public void testSolverWithRegulaFalsiMethod() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 6.0, -11.0, 6.0}, new int[]{2, 1, 2});
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, Method.REGULA_FALSI);
        double solution = solver.solve(100, f, 1d, 2d, 1.5d);
        assertEquals(1.7320508075688772935, solution, 1e-5);
    }

    @Test
    public void testSolverWithIllinoisMethod() {
        UnivariateRealFunction f = x -> Math.pow(x, 3) - 2;
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, Method.ILLINOIS);
        double solution = solver.solve(100, f, 1d, 4d, 2d);
        assertEquals(2.0, solution, 1e-5);
    }

    @Test
    public void testSolverWithPegasusMethod() {
        UnivariateRealFunction f = x -> Math.pow(x, 3) - 2;
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, Method.PEGASUS);
        double solution = solver.solve(100, f, 1d, 4d, 2d);
        assertEquals(2.0, solution, 1e-5);
    }

    @Test
    public void testSolverWithAbsoluteAccuracy() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 2;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI);
        double solution = solver.solve(100, f, 1d, 2d, 1.5d);
        assertEquals(1.4142135623730955064, solution, 0.01);
    }

    @Test
    public void testSolverWithRelativeAccuracy() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 2;
        BaseSecantSolver solver = new BaseSecantSolver(0.001, 0.01, Method.REGULA_FALSI);
        double solution = solver.solve(100, f, 1d, 2d, 1.5d);
        assertEquals(1.4142135623730955064, solution, 0.001);
    }

    @Test
    public void testSolverWithFunctionValueAccuracy() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 2;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, 0.01, 0.001, Method.REGULA_FALSI);
        double solution = solver.solve(100, f, 1d, 2d, 1.5d);
        assertEquals(1.4142135623730955064, solution, 0.001);
    }

    @Test
    public void testSolverWithMaxEval() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 2;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI);
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(0, f, 1d, 2d, 1.5d));
    }

    @Test
    public void testSolverWithAllowedSolutionBelowSide() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 2;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI);
        double solution = solver.solve(100, f, 1d, 2d, 1.5d, AllowedSolution.BELOW_SIDE);
        assertEquals(1.4142135623730955064, solution, 0.01);
    }

    @Test
    public void testSolverWithAllowedSolutionAboveSide() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) + 2;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI);
        double solution = solver.solve(100, f, 1d, 2d, 1.5d, AllowedSolution.ABOVE_SIDE);
        assertEquals(-1.4142135623730955064, solution, 0.01);
    }

    @Test
    public void testSolverWithAllowedSolutionLeftSide() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 2;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI);
        double solution = solver.solve(100, f, 1d, 2d, 1.5d, AllowedSolution.LEFT_SIDE);
        assertEquals(1.4142135623730955064, solution, 0.01);
    }

    @Test
    public void testSolverWithAllowedSolutionRightSide() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 2;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI);
        double solution = solver.solve(100, f, 1d, 2d, 1.5d, AllowedSolution.RIGHT_SIDE);
        assertEquals(1.4142135623730955064, solution, 0.01);
    }

    @Test
    public void testSolverWithAllowedSolutionAnySide() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 2;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI);
        double solution = solver.solve(100, f, 1d, 2d, 1.5d);
        assertEquals(1.4142135623730955064, solution, 0.01);
    }

    @Test
    public void testSolverWithFunctionWithRootAtEnd() {
        UnivariateRealFunction f = x -> (x - 2) * (x - 3);
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI);
        double solution = solver.solve(100, f, 0d, 5d);
        assertEquals(3.0, solution, 0.01);
    }

    @Test
    public void testSolverWithFunctionWithRootAtStart() {
        UnivariateRealFunction f = x -> (x - 2) * (x - 3);
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI);
        double solution = solver.solve(100, f, -5d, 0d);
        assertEquals(2.0, solution, 0.01);
    }

    @Test
    public void testSolverWithFunctionWithRootInside() {
        UnivariateRealFunction f = x -> (x - 2) * (x - 3);
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI);
        double solution = solver.solve(100, f, 1.5d, 4d);
        assertEquals(2.5, solution, 0.01);
    }

    @Test
    public void testSolverWithFunctionWithRootBetweenEvaluatedPoints() {
        UnivariateRealFunction f = x -> FunctionUtils.piecewise(x, new double[]{-5.0, 0.0, 5.0},
            new double[]{-FunctionUtils.SQUARE, FunctionUtils.IDENTITY, FunctionUtils.SQUARE});
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI);
        double solution = solver.solve(100, f, -3d, 1d);
        assertEquals(0.0, solution, 0.01);
    }

    @Test
    public void testSolverWithFunctionWithDifferentRoots() {
        UnivariateRealFunction f = x -> (x - 1) * (x - 3) * (x - 5);
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI);
        double[] solutions = new double[3];
        for (int i = 0; i < 3; i++) {
            solutions[i] = solver.solve(100, f, 0d, 6d);
        }
        assertArrayEquals(new double[]{1.0, 3.0, 5.0}, solutions, 0.01);
    }

    @Test
    public void testSolverWithFunctionWithDifferentRootsDifferentAllowedSolution() {
        UnivariateRealFunction f = x -> (x - 1) * (x - 3) * (x - 5);
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI);
        double[] solutions = new double[3];
        for (int i = 0; i < 3; i++) {
            solutions[i] = solver.solve(100, f, 0d, 6d, AllowedSolution.values()[i % 3]);
        }
        assertArrayEquals(new double[]{1.0, 3.0, 5.0}, solutions, 0.01);
    }

    @Test
    public void testSolverWithFunctionWithDifferentRootsDifferentAllowedSolutionAndMethods() {
        UnivariateRealFunction f = x -> (x - 1) * (x - 3) * (x - 5);
        BaseSecantSolver solver1 = new BaseSecantSolver(0.01, Method.REGULA_FALSI);
        BaseSecantSolver solver2 = new BaseSecantSolver(0.01, Method.ILLINOIS);
        BaseSecantSolver solver3 = new BaseSecantSolver(0.01, Method.PEGASUS);
        double[] solutions1 = new double[3];
        double[] solutions2 = new double[3];
        double[] solutions3 = new double[3];
        for (int i = 0; i < 3; i++) {
            solutions1[i] = solver1.solve(100, f, 0d, 6d, AllowedSolution.values()[i % 3]);
            solutions2[i] = solver2.solve(100, f, 0d, 6d, AllowedSolution.values()[i % 3]);
            solutions3[i] = solver3.solve(100, f, 0d, 6d, AllowedSolution.values()[i % 3]);
        }
        assertArrayEquals(new double[]{1.0, 3.0, 5.0}, solutions1, 0.01);
        assertArrayEquals(new double[]{1.0, 3.0, 5.0}, solutions2, 0.01);
        assertArrayEquals(new double[]{1.0, 3.0, 5.0}, solutions3, 0.01);
    }

    @Test
    public void testSolverWithMaxIterationsExceededException() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 2;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI);
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(0, f, 1d, 2d, 1.5d));
    }

    @Test
    public void testSolverWithPreconditionNotMetException() {
        UnivariateRealFunction f = x -> 1 / (x - 2);
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI);
        assertThrows(PreconditionNotMetException.class, () -> solver.solve(100, f, 0d, 2d, AllowedSolution.ANY_SIDE));
    }
}