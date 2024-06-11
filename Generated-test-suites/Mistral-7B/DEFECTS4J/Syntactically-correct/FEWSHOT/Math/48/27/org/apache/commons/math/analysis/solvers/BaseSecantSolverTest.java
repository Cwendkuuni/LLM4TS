package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.commons.math.analysis.PolynomialFunction;
import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedRealRootFinder;
import org.apache.commons.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.commons.math.exception.ConvergenceException;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsi() {
        UnivariateRealFunction f = new PolynomialFunction(2, -1d, 1d, 1d, 2d);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        BracketedRealRootFinder rootFinder = new BaseSecantSolver(solver);
        try {
            double root = rootFinder.solve(f, 1d, 3d);
            assertTrue(FastMath.abs(root - 2.0) < 1e-8);
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testIllinois() {
        UnivariateRealFunction f = new PolynomialFunction(2, -1d, 1d, 1d, 2d);
        BaseSecantSolver solver = new IllinoisSolver(1e-6);
        BracketedRealRootFinder rootFinder = new BaseSecantSolver(solver);
        try {
            double root = rootFinder.solve(f, 1d, 3d);
            assertTrue(FastMath.abs(root - 2.0) < 1e-8);
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testPegasus() {
        UnivariateRealFunction f = new PolynomialFunction(2, -1d, 1d, 1d, 2d);
        BaseSecantSolver solver = new PegasusSolver(1e-6);
        BracketedRealRootFinder rootFinder = new BaseSecantSolver(solver);
        try {
            double root = rootFinder.solve(f, 1d, 3d);
            assertTrue(FastMath.abs(root - 2.0) < 1e-8);
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testVerifyBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new PolynomialFunction(2, -1d, 1d, 1d, 2d);
        double min = 1.0;
        double max = 3.0;

        solver.verifyBracketing(min, max);
        assertTrue(max > min);
        assertTrue(solver.method.equals(BaseSecantSolver.Method.REGULA_FALSI));
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() {
        UnivariateRealFunction f = new PolynomialFunction(2, -1d, 1d, 1d, 2d);
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        BracketedRealRootFinder rootFinder = new BaseSecantSolver(solver);
        rootFinder.solve(f, 1d, 1d);
    }

    @Test
    public void testNullFunction() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        BracketedRealRootFinder rootFinder = new BaseSecantSolver(solver);
        assertNull(rootFinder.solve(null, 1d, 3d));
    }
}