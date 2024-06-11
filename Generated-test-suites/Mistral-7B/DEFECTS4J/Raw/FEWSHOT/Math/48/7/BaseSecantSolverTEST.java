package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.commons.math.analysis.PolynomialFunction;
import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math.exception.ConvergenceException;
import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.commons.math.util.FastMath;

/**
 * Test cases for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsi() {
        UnivariateRealFunction f = new PolynomialFunction(3, -1.0, 1.0, 2.0);

        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.REGULA_FALSI);
        double root = solver.solve(f, 1.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(1.0, root, 1e-7);
    }

    @Test
    public void testIllinois() {
        UnivariateRealFunction f = new PolynomialFunction(3, -1.0, 1.0, 2.0);

        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.ILLINOIS, 1e-10, 1e-10);
        double root = solver.solve(f, 1.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(1.0, root, 1e-7);
    }

    @Test
    public void testPegasus() {
        UnivariateRealFunction f = new PolynomialFunction(3, -1.0, 1.0, 2.0);

        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.PEGASUS, 1e-10, 1e-10);
        double root = solver.solve(f, 1.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(1.0, root, 1e-7);
    }

    @Test(expected = ConvergenceException.class)
    public void testStuckRegulaFalsi() {
        UnivariateRealFunction f = new PolynomialFunction(3, -1.0, 1.0, 2.0);

        BaseSecantSolver solver = new RegulaFalsiSolver(1e-16, RegulaFalsiSolver.REGULA_FALSI);
        solver.solve(f, 1.0, 2.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = ConvergenceException.class)
    public void testStuckIllinois() {
        UnivariateRealFunction f = new PolynomialFunction(3, -1.0, 1.0, 2.0);

        BaseSecantSolver solver = new RegulaFalsiSolver(1e-16, RegulaFalsiSolver.ILLINOIS, 1e-16, 1e-16);
        solver.solve(f, 1.0, 2.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = ConvergenceException.class)
    public void testStuckPegasus() {
        UnivariateRealFunction f = new PolynomialFunction(3, -1.0, 1.0, 2.0);

        BaseSecantSolver solver = new RegulaFalsiSolver(1e-16, RegulaFalsiSolver.PEGASUS, 1e-16, 1e-16);
        solver.solve(f, 1.0, 2.0, AllowedSolution.ANY_SIDE);
    }
}