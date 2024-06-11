package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_RegulaFalsi() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, 1, 1, 1});
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, Method.REGULA_FALSI);
        double root = solver.solve(10, function, -10, 10);
        assertEquals(0.0, root, 1e-7);
    }

    @Test
    public void testSolve_Illinois() {
        UnivariateFunction function = new PolynomialFunction(new double[]{1, -2, 1});
        BaseSecantSolver solver = new IllinoisSolver(1e-6, Method.ILLINOIS);
        double root = solver.solve(10, function, -10, 10);
        assertEquals(0.0, root, 1e-7);
    }

    @Test
    public void testSolve_Pegasus() {
        UnivariateFunction function = new PolynomialFunction(new double[]{1, -2, 1});
        BaseSecantSolver solver = new PegasusSolver(1e-6, Method.PEGASUS);
        double root = solver.solve(10, function, -10, 10);
        assertEquals(0.0, root, 1e-7);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolve_MaxEvalExceeded() {
        UnivariateFunction function = new PolynomialFunction(new double[]{1, -2, 1});
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, Method.REGULA_FALSI);
        solver.solve(0, function, -10, 10);
    }

    @Test
    public void testSolve_LeftSide() {
        UnivariateFunction function = new PolynomialFunction(new double[]{1, -2, 1});
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, Method.REGULA_FALSI, AllowedSolution.LEFT_SIDE);
        double root = solver.solve(10, function, -10, 10);
        assertTrue(root <= 0.0);
        assertEquals(0.0, root, 1e-7);
    }

    @Test
    public void testSolve_RightSide() {
        UnivariateFunction function = new PolynomialFunction(new double[]{1, -2, 1});
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, Method.REGULA_FALSI, AllowedSolution.RIGHT_SIDE);
        double root = solver.solve(10, function, -10, 10);
        assertTrue(root >= 0.0);
        assertEquals(0.0, root, 1e-7);
    }

    @Test
    public void testSolve_BelowSide() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, 1, 1, 1});
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, Method.REGULA_FALSI, AllowedSolution.BELOW_SIDE);
        double root = solver.solve(10, function, -10, 10);
        assertTrue(FunctionUtils.valueLessThan(root, 0.0));
        assertEquals(0.0, root, 1e-7);
    }

    @Test
    public void testSolve_AboveSide() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, 1, 1, 1});
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, Method.REGULA_FALSI, AllowedSolution.ABOVE_SIDE);
        double root = solver.solve(10, function, -10, 10);
        assertTrue(FunctionUtils.valueGreaterThan(root, 0.0));
        assertEquals(0.0, root, 1e-7);
    }
}