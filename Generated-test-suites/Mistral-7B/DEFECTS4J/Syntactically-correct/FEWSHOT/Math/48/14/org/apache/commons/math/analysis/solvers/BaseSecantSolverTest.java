package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.commons.math.analysis.PolynomialFunction;
import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.IllinoisSolver;
import org.apache.commons.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.commons.math.analysis.solvers.PegasusSolver;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsi() {
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1e-6);
        double root = solver.solve(new PolynomialFunction(new double[]{-1, 1, 1}), 0, 1);
        assertEquals("Regula Falsi should find the square root of 2.", Math.sqrt(2), root, 1e-6);
    }

    @Test
    public void testIllinois() {
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new IllinoisSolver(1e-6);
        double root = solver.solve(new PolynomialFunction(new double[]{-1, 1, 1}), 0, 1);
        assertEquals("Illinois method should find the square root of 2.", Math.sqrt(2), root, 1e-6);
    }

    @Test
    public void testPegasus() {
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new PegasusSolver(1e-6);
        double root = solver.solve(new PolynomialFunction(new double[]{-1, 1, 1}), 0, 1);
        assertEquals("Pegasus method should find the square root of 2.", Math.sqrt(2), root, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testFailedConvergence() {
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double[] coefficients = {-1, 1, 1};
        double root = solver.solve(new PolynomialFunction(coefficients), 0, 1);
        fail("The Regula Falsi solver should not be able to find the root of x^3 - 1.");
    }

}