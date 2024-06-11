package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.commons.math.analysis.PolynomialFunction;
import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.AllowedSolution;
import org.apache.commons.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.commons.math.analysis.solvers.IllinoisSolver;
import org.apache.commons.commons.math.analysis.solvers.PegasusSolver;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsi() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        BracketedUnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2 * x + 1);
        double solution = solver.solve(10, function, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, solution, 1e-7);
    }

    @Test
    public void testIllinois() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6);
        BracketedUnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2 * x + 1);
        double solution = solver.solve(10, function, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, solution, 1e-7);
    }

    @Test
    public void testPegasus() {
        BaseSecantSolver solver = new PegasusSolver(1e-6);
        BracketedUnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2 * x + 1);
        double solution = solver.solve(10, function, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, solution, 1e-7);
    }

    @Test(expected = MathInternalError.class)
    public void testInvalidMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.INVALID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFunction() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        BracketedUnivariateRealFunction function = null;
        try {
            solver.solve(10, function, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        } catch (IllegalArgumentException e) {
            assertEquals("function may not be null", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullMin() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        BracketedUnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2 * x + 1);
        double min = null;
        try {
            solver.solve(10, function, min, 1.0, AllowedSolution.ANY_SIDE);
        } catch (IllegalArgumentException e) {
            assertEquals("min may not be null", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullMax() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        BracketedUnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2 * x + 1);
        double max = null;
        try {
            solver.solve(10, function, -1.0, max, AllowedSolution.ANY_SIDE);
        } catch (IllegalArgumentException e) {
            assertEquals("max may not be null", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMin() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        BracketedUnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2 * x + 1);
        double min = -1.0;
        try {
            solver.solve(10, function, min, 1.0, AllowedSolution.ANY_SIDE);
        } catch (IllegalArgumentException e) {
            assertEquals("min must be non-negative", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMax() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        BracketedUnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2 * x + 1);
        double max = -1.0;
        try {
            solver.solve(10, function, -1.0, max, AllowedSolution.ANY_SIDE);
        } catch (IllegalArgumentException e) {
            assertEquals("max must be non-negative", e.getMessage());
            throw e;
        }
    }

}