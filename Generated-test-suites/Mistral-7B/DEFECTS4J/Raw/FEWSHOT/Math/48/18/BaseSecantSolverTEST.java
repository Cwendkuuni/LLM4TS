package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.PolynomialFunctions;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.RootNotFoundException;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_RegulaFalsi() {
        UnivariateRealFunction function = new PolynomialFunctions.Quadratic(1, -2, 1);
        BaseSecantSolver solver = new IllinoisSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        try {
            double root = solver.solve(10, function, -2.5, 2.5, 0.0);
            assertEquals("Quadratic root not found", 0.5, root, 1e-6);
        } catch (RootNotFoundException e) {
            fail("RootNotFoundException should not be thrown");
        } catch (ConvergenceException e) {
            fail("MaxIterationsExceededException should not be thrown");
        }
    }

    @Test
    public void testSolve_Illinois() {
        UnivariateRealFunction function = new PolynomialFunctions.Quadratic(1, -2, 1);
        BaseSecantSolver solver = new IllinoisSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        try {
            double root = solver.solve(10, function, -2.5, 2.5, 0.0);
            assertEquals("Quadratic root not found", 0.5, root, 1e-6);
        } catch (RootNotFoundException e) {
            fail("RootNotFoundException should not be thrown");
        } catch (ConvergenceException e) {
            fail("MaxIterationsExceededException should not be thrown");
        }
    }

    @Test
    public void testSolve_Pegasus() {
        UnivariateRealFunction function = new PolynomialFunctions.Quadratic(1, -2, 1);
        BaseSecantSolver solver = new PegasusSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        try {
            double root = solver.solve(10, function, -2.5, 2.5, 0.0);
            assertEquals("Quadratic root not found", 0.5, root, 1e-6);
        } catch (RootNotFoundException e) {
            fail("RootNotFoundException should not be thrown");
        } catch (ConvergenceException e) {
            fail("MaxIterationsExceededException should not be thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_InvalidMethod() {
        UnivariateRealFunction function = new PolynomialFunctions.Quadratic(1, -2, 1);
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.INVALID) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException();
            }
        };
        solver.solve(10, function, -2.5, 2.5, 0.0);
    }

    @Test
    public void testAllowedSolution_AnySide() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        assertEquals("Allowed solution should be ANY_SIDE", BaseSecantSolver.AllowedSolution.ANY_SIDE, solver.getAllowedSolution());
    }

    @Test
    public void testAllowedSolution_LeftSide() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6, BaseSecantSolver.Method.ILLINOIS, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals("Allowed solution should be LEFT_SIDE", BaseSecantSolver.AllowedSolution.LEFT_SIDE, solver.getAllowedSolution());
    }

    @Test
    public void testAllowedSolution_RightSide() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6, BaseSecantSolver.Method.ILLINOIS, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals("Allowed solution should be RIGHT_SIDE", BaseSecantSolver.AllowedSolution.RIGHT_SIDE, solver.getAllowedSolution());
    }

    @Test
    public void testAllowedSolution_BelowSide() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6, BaseSecantSolver.Method.ILLINOIS, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals("Allowed solution should be BELOW_SIDE", BaseSecantSolver.AllowedSolution.BELOW_SIDE, solver.getAllowedSolution());
    }

    @Test
    public void testAllowedSolution_AboveSide() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6, BaseSecantSolver.Method.ILLINOIS, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals("Allowed solution should be ABOVE_SIDE", BaseSecantSolver.AllowedSolution.ABOVE_SIDE, solver.getAllowedSolution());
    }

}