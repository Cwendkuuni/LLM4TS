package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;

import org.apache.commons.cli.BaseSecantSolver;
import org.apache.commons.cli.BracketedUnivariateRealSolver;
import org.apache.commons.cli.RegulaFalsiSolver;
import org.apache.commons.cli.IllinoisSolver;
import org.apache.commons.cli.PegasusSolver;

public class BaseSecantSolverTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testEmptyConstructor() {
        new BaseSecantSolver<>();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        new BaseSecantSolver((double) 0.0, null);
    }

    @Test
    public void testRegulaFalsiConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, RegulaFalsiSolver.INSTANCE);
        assertEquals(RegulaFalsiSolver.INSTANCE, solver.getMethod());
    }

    @Test
    public void testIllinoisConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, IllinoisSolver.INSTANCE);
        assertEquals(IllinoisSolver.INSTANCE, solver.getMethod());
    }

    @Test
    public void testPegasusConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, PegasusSolver.INSTANCE);
        assertEquals(PegasusSolver.INSTANCE, solver.getMethod());
    }

    @Test
    public void testRelativeAndAbsoluteConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, 0.0001, RegulaFalsiSolver.INSTANCE);
        assertEquals(RegulaFalsiSolver.INSTANCE, solver.getMethod());
        assertEquals(0.001, solver.getRelativeAccuracy(), 1e-7);
        assertEquals(0.0001, solver.getAbsoluteAccuracy(), 1e-7);
    }

    @Test
    public void testFunctionValueAccuracyConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, 0.0001, 1e-6, RegulaFalsiSolver.INSTANCE);
        assertEquals(RegulaFalsiSolver.INSTANCE, solver.getMethod());
        assertEquals(0.001, solver.getRelativeAccuracy(), 1e-7);
        assertEquals(0.0001, solver.getAbsoluteAccuracy(), 1e-7);
        assertEquals(1e-6, solver.getFunctionValueAccuracy(), 1e-7);
    }

    @Test(expected = IllegalStateException.class)
    public void testSolveNoFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, RegulaFalsiSolver.INSTANCE);
        solver.solve(1, null, 0.0, 1.0, 0.5);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveInvalidBounds() {
        UnivariateRealFunction function = (x) -> x * x - 1;
        BaseSecantSolver solver = new BaseSecantSolver(0.001, RegulaFalsiSolver.INSTANCE);
        solver.solve(1, function, Double.NEGATIVE_INFINITY, 0.0, 0.5);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolveNoBracketing() throws NoBracketingException {
        UnivariateRealFunction function = (x) -> x * x - 1;
        BaseSecantSolver solver = new BaseSecantSolver(0.001, RegulaFalsiSolver.INSTANCE);
        solver.solve(1, function, 0.0, 1.0, 1.0);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolveMaxEvalExceeded() throws MaxEvalExceededException {
        UnivariateRealFunction function = (x) -> Math.pow(Math.E, -x);
        BaseSecantSolver solver = new BaseSecantSolver(0.001, RegulaFalsiSolver.INSTANCE);
        solver.solve(0, function, 0.0, 1.0, 1.0);
    }

    @Test
    public void testSolveRegulaFalsi() {
        UnivariateRealFunction function = (x) -> x * x - 1;
        BaseSecantSolver solver = new BaseSecantSolver(0.001, RegulaFalsiSolver.INSTANCE);
        double result = solver.solve(10, function, 0.0, 1.0, 0.5);
        assertEquals(0.0, result, 1e-7);
    }

    @Test
    public void testSolveIllinois() {
        UnivariateRealFunction function = (x) -> x * x - 1;
        BaseSecantSolver solver = new BaseSecantSolver(0.001, IllinoisSolver.INSTANCE);
        double result = solver.solve(10, function, 0.0, 1.0, 0.5);
        assertEquals(0.0, result, 1e-7);
    }

    @Test
    public void testSolvePegasus() {
        UnivariateRealFunction function = (x) -> x * x - 1;
        BaseSecantSolver solver = new BaseSecantSolver(0.001, PegasusSolver.INSTANCE);
        double result = solver.solve(10, function, 0.0, 1.0, 0.5);
        assertEquals(0.0, result, 1e-7);
    }

    @Test
    public void testSolveRegulaFalsiAllowedLeft() {
        UnivariateRealFunction function = (x) -> x * x - 1;
        BaseSecantSolver solver = new BaseSecantSolver(0.001, RegulaFalsiSolver.INSTANCE);
        double result = solver.solve(10, function, 0.0, 1.0, 0.5, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, result, 1e-7);
    }

    @Test
    public void testSolveRegulaFalsiAllowedRight() {
        UnivariateRealFunction function = (x) -> x * x - 1;
        BaseSecantSolver solver = new BaseSecantSolver(0.001, RegulaFalsiSolver.INSTANCE);
        double result = solver.solve(10, function, 0.0, 1.0, 0.5, AllowedSolution.RIGHT_SIDE);
        assertEquals(0.0, result, 1e-7);
    }

    @Test
    public void testSolveRegulaFalsiAllowedBelow() {
        UnivariateRealFunction function = (x) -> x * x - 1;
        BaseSecantSolver solver = new BaseSecantSolver(0.001, RegulaFalsiSolver.INSTANCE);
        double result = solver.solve(10, function, 0.0, 1.0, 0.5, AllowedSolution.BELOW_SIDE);
        assertEquals(0.0, result, 1e-7);
    }

    @Test
    public void testSolveRegulaFalsiAllowedAbove() {
        UnivariateRealFunction function = (x) -> x * x - 1;
        BaseSecantSolver solver = new BaseSecantSolver(0.001, RegulaFalsiSolver.INSTANCE);
        double result = solver.solve(10, function, 0.0, 1.0, 0.5, AllowedSolution.ABOVE_SIDE);
        assertEquals(0.0, result, 1e-7);
    }

    @Test
    public void testSolveRegulaFalsiAllowedAnySide() {
        UnivariateRealFunction function = (x) -> x * x - 1;
        BaseSecantSolver solver = new BaseSecantSolver(0.001, RegulaFalsiSolver.INSTANCE);
        double result = solver.solve(10, function, 0.0, 1.0, 0.5, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, result, 1e-7);
    }

}

enum AllowedSolution {
    LEFT_SIDE,
    RIGHT_SIDE,
    BELOW_SIDE,
    ABOVE_SIDE,
    ANY_SIDE;
}