package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private UnivariateRealFunction f;
    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        f = x -> Math.pow(x, 3) - 2; // a simple cubic function
        solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.RegulaFalsiMethod.REGULA_FALSI);
    }

    @Test
    public void testSolve_simpleCubicFunction() {
        double root = solver.solve(10, f, -2.0, 2.0, 0.0);
        assertEquals(0.0, root, 1e-7);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolve_noBracketing() {
        f = x -> Math.sin(x) / x;
        solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.RegulaFalsiMethod.REGULA_FALSI);
        solver.solve(10, f, -1.0, 1.0, 0.0);
    }

    @Test(expected = MaxEvalException.class)
    public void testSolve_maxEval() {
        f = x -> 1.0 / (x * x);
        solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.RegulaFalsiMethod.REGULA_FALSI);
        solver.solve(0, f, -1.5, 1.5, 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolve_smallRoot() {
        f = x -> Math.pow(x, 2) - 0.0001;
        solver = new RegulaFalsiSolver(1e-12, RegulaFalsiSolver.RegulaFalsiMethod.REGULA_FALSI);
        solver.solve(10, f, -2.0, 2.0, 0.0);
    }

    @Test
    public void testSolve_allowedSolutionLeftSide() {
        solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.RegulaFalsiMethod.REGULA_FALSI, AllowedSolution.LEFT_SIDE);
        double root = solver.solve(10, f, -2.0, 2.0, 0.0);
        assertTrue(root <= 0.0);
    }

    @Test
    public void testSolve_allowedSolutionRightSide() {
        solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.RegulaFalsiMethod.REGULA_FALSI, AllowedSolution.RIGHT_SIDE);
        double root = solver.solve(10, f, -2.0, 2.0, 0.0);
        assertTrue(root > 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_invalidAllowedSolution() {
        new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.RegulaFalsiMethod.REGULA_FALSI, AllowedSolution.INVALID);
    }

    @Test
    public void testSolve_zeroFunction() {
        f = x -> x;
        solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.RegulaFalsiMethod.REGULA_FALSI);
        double root = solver.solve(10, f, -1.0, 1.0, 0.0);
        assertEquals(0.0, root, 1e-7);
    }

    @Test
    public void testSolve_constantFunction() {
        f = x -> 2.0;
        solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.RegulaFalsiMethod.REGULA_FALSI);
        double root = solver.solve(10, f, -1.0, 1.0, 0.0);
        assertEquals(2.0, root, 1e-7);
    }

    @Test
    public void testSolve_functionWithMultipleRoots() {
        f = x -> Math.pow(x, 3) - 2;
        solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.RegulaFalsiMethod.REGULA_FALSI);
        double root1 = solver.solve(10, f, -2.0, 2.0, 0.0);
        assertEquals(0.0, root1, 1e-7);

        double root2 = solver.solve(10, f, -2.0, 2.0, root1);
        assertEquals(-1.0, root2, 1e-7);
    }
}